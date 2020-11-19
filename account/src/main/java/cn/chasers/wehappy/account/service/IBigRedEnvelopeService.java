package cn.chasers.wehappy.account.service;

import cn.chasers.wehappy.account.entity.BigRedEnvelope;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 大红包信息表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-12
 */
public interface IBigRedEnvelopeService extends IService<BigRedEnvelope> {

    /**
     * 发送红包操作
     *
     * @param userId 用户 Id
     * @param type   红包类型，7表示私聊红包，8表示群聊运气红包
     * @param to     接收者Id，type为7时表示用户Id，type为8时，表示群聊I
     * @param money  红包金额
     * @return 发没发出去
     */
    Boolean send(Long userId, Long type, Long to, BigDecimal money);

    /**
     * 抢红包操作
     *
     * @param userId           用户 Id
     * @param bigRedEnvelopeId 大红包 Id
     * @return 抢没抢到
     */
    Boolean snap(Long userId, Long bigRedEnvelopeId);

    /**
     * 获取大红包详细信息
     *
     * @param userId 用户 Id
     * @param id     大红包 Id
     * @return 大红包详细信息
     */
    BigRedEnvelope get(Long userId, Long id);
}
