package cn.chasers.wehappy.account.service.impl;

import cn.chasers.wehappy.account.domain.RedEnvelopeInfo;
import cn.chasers.wehappy.account.entity.BigRedEnvelope;
import cn.chasers.wehappy.account.entity.SmallRedEnvelope;
import cn.chasers.wehappy.account.feign.IGroupService;
import cn.chasers.wehappy.account.feign.IUserService;
import cn.chasers.wehappy.account.mapper.BigRedEnvelopeMapper;
import cn.chasers.wehappy.account.mq.Consumer;
import cn.chasers.wehappy.account.mq.Producer;
import cn.chasers.wehappy.account.service.IAccountService;
import cn.chasers.wehappy.account.service.IBigRedEnvelopeService;
import cn.chasers.wehappy.account.service.ISmallRedEnvelopeService;
import cn.chasers.wehappy.common.config.SnowflakeConfig;
import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.common.service.IRedisService;
import cn.chasers.wehappy.common.util.MessageUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

/**
 * <p>
 * 大红包信息表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
@Service
public class BigRedEnvelopeServiceImpl extends ServiceImpl<BigRedEnvelopeMapper, BigRedEnvelope> implements IBigRedEnvelopeService {
    private final SnowflakeConfig snowflakeConfig;

    private final IRedisService redisService;

    private final ISmallRedEnvelopeService smallRedEnvelopeService;
    private final IAccountService accountService;

    private final Producer producer;

    private final IUserService userService;
    private final IGroupService groupService;

    @Value("{redis.redEnvelopeInfo.key}")
    private String redEnvelopeInfoKey;

    @Value("{redis.redEnvelopeInfo.expire}")
    private long redEnvelopeInfoExpire;

    @Value("{redis.redEnvelopeUserIds.key}")
    private String redEnvelopeUserIdsKey;

    @Value("{redis.redEnvelopeUserIds.expire}")
    private long redEnvelopeUserIdsExpire;

    @Value("{redis.database}")
    private String redisDatabase;

    @Value("{redis.separator}")
    private String redisSeparator;

    @Autowired
    public BigRedEnvelopeServiceImpl(SnowflakeConfig snowflakeConfig, IRedisService redisService, ISmallRedEnvelopeService smallRedEnvelopeService, IAccountService accountService, Producer producer, IUserService userService, IGroupService groupService) {
        this.snowflakeConfig = snowflakeConfig;
        this.redisService = redisService;
        this.smallRedEnvelopeService = smallRedEnvelopeService;
        this.accountService = accountService;
        this.producer = producer;
        this.userService = userService;
        this.groupService = groupService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean send(Long userId, Integer type, Long to, BigDecimal money) {
        BigRedEnvelope bigRedEnvelope = new BigRedEnvelope();
        if (type != 7 && type != 8) {
            return false;
        }

        // 私聊红包
        if (type == 7) {
            if (!userService.isFriend(userId, to).getData()) {
                return false;
            }
        } else {
            if (groupService.getGroupUser(to, userId).getData() == null) {
                return false;
            }
        }

        bigRedEnvelope.setUserId(userId);
        bigRedEnvelope.setType(type);
        bigRedEnvelope.setMoney(money);

        accountService.pay(userId, money);
        save(bigRedEnvelope);

        String sequence = String.valueOf(snowflakeConfig.snowflakeId());

        producer.sendMessage(MessageUtil.newMessage(
                "0",
                sequence,
                String.valueOf(System.currentTimeMillis()),
                to.toString(),
                type == 7 ? ProtoMsg.MessageType.SINGLE_MESSAGE : ProtoMsg.MessageType.GROUP_MESSAGE,
                MessageUtil.newChatMessage(
                        to.toString(),
                        userId.toString(),
                        type == 7 ? ProtoMsg.ContentType.SINGLE_RED_ENVELOPE : ProtoMsg.ContentType.UNFAIR_RED_ENVELOPE,
                        JSONUtil.toJsonStr(bigRedEnvelope)
                )
        ));

        redisService.hSet(redisDatabase + redisSeparator + redEnvelopeInfoKey, bigRedEnvelope.getId().toString(), new RedEnvelopeInfo(bigRedEnvelope, null), redEnvelopeInfoExpire);

        return true;
    }

    @Override
    public boolean snap(Long userId, Long bigRedEnvelopeId) {
        RedEnvelopeInfo redEnvelopeInfo = (RedEnvelopeInfo) redisService.hGet(redisDatabase + redisSeparator + redEnvelopeInfoKey, bigRedEnvelopeId.toString());
        // 红包信息不存在
        if (redEnvelopeInfo == null) {
            return false;
        }

        // 已经领过了
        if (redEnvelopeInfo.getSmallRedEnvelopes() != null && redEnvelopeInfo.getSmallRedEnvelopes().stream().anyMatch(smallRedEnvelope -> smallRedEnvelope.getUserId().equals(userId))) {
            return false;
        }

        // 领完了
        long smallRedEnvelopeSize = redisService.sSize(redisDatabase + redisSeparator + redEnvelopeUserIdsKey);
        if (smallRedEnvelopeSize >= redEnvelopeInfo.getBigRedEnvelope().getTotal()) {
            return false;
        }

        redisService.sAdd(redisDatabase + redisSeparator + redEnvelopeUserIdsKey, redEnvelopeUserIdsExpire, userId);

        SmallRedEnvelope smallRedEnvelope = new SmallRedEnvelope();
        smallRedEnvelope.setBigRedEnvelopeId(bigRedEnvelopeId);
        smallRedEnvelope.setUserId(userId);

        producer.sendSnap(smallRedEnvelope);

        return true;
    }

    @Override
    public RedEnvelopeInfo get(Long id) {
        RedEnvelopeInfo redEnvelopeInfo = (RedEnvelopeInfo) redisService.hGet(redisDatabase + redisSeparator + redEnvelopeInfoKey, id.toString());
        if (redEnvelopeInfo != null) {
            return redEnvelopeInfo;
        }

        BigRedEnvelope bigRedEnvelope = lambdaQuery().eq(BigRedEnvelope::getUserId, id).one();
        if (bigRedEnvelope == null) {
            return null;
        }

        return new RedEnvelopeInfo(bigRedEnvelope, smallRedEnvelopeService.getAllByBigRedEnvelopeId(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doSnap(SmallRedEnvelope smallRedEnvelope) {
        BigRedEnvelope bigRedEnvelope = lambdaQuery().eq(BigRedEnvelope::getId, smallRedEnvelope.getBigRedEnvelopeId()).one();

        if (bigRedEnvelope.getRemains() <= 0) {
            pushSnapMessage(smallRedEnvelope, bigRedEnvelope.getType());
            return;
        }

        smallRedEnvelopeService.save(smallRedEnvelope);
        bigRedEnvelope.setRemains(bigRedEnvelope.getTotal() - 1);
        updateById(bigRedEnvelope);

        RedEnvelopeInfo redEnvelopeInfo = (RedEnvelopeInfo) redisService.hGet(redisDatabase + redisSeparator + redEnvelopeInfoKey, bigRedEnvelope.getId().toString());
        if (redEnvelopeInfo.getSmallRedEnvelopes() == null) {
            redEnvelopeInfo.setSmallRedEnvelopes(Collections.singletonList(smallRedEnvelope));
        } else {
            redEnvelopeInfo.getSmallRedEnvelopes().add(smallRedEnvelope);
        }

        redisService.hSet(redisDatabase + redisSeparator + redEnvelopeInfoKey, bigRedEnvelope.getId().toString(), redEnvelopeInfo);
        pushSnapMessage(smallRedEnvelope, bigRedEnvelope.getType());
    }

    private void pushSnapMessage(SmallRedEnvelope smallRedEnvelope, Integer type) {
        producer.sendPushMessage(MessageUtil.newMessage(
                "0",
                String.valueOf(snowflakeConfig.snowflakeId()),
                String.valueOf(System.currentTimeMillis()),
                smallRedEnvelope.getUserId().toString(),
                ProtoMsg.MessageType.PUSH_MESSAGE,
                MessageUtil.newPushMessage(
                        type == 7 ? ProtoMsg.ContentType.SINGLE_RED_ENVELOPE : ProtoMsg.ContentType.UNFAIR_RED_ENVELOPE,
                        JSONUtil.toJsonStr(smallRedEnvelope)
                )
        ));
    }
}
