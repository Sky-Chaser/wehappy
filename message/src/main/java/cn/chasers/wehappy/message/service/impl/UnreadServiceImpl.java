package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.Unread;
import cn.chasers.wehappy.message.mapper.UnreadMapper;
import cn.chasers.wehappy.message.service.IUnreadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户未读数表 服务实现类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
@Service
public class UnreadServiceImpl extends ServiceImpl<UnreadMapper, Unread> implements IUnreadService {

    private final UnreadMapper unreadMapper;

    @Autowired
    public UnreadServiceImpl(UnreadMapper unreadMapper) {
        this.unreadMapper = unreadMapper;
    }

    @Override
    public boolean increase(Long userId, int count) {
        Unread unread = new Unread();
        unread.setUserId(userId);
        unread.setCount(count);
        return unreadMapper.increase(unread);
    }

    @Override
    public boolean update(Long userId, int count) {
        Unread unread = new Unread();
        unread.setCount(count);
        return lambdaUpdate().eq(Unread::getUserId, userId).update(unread);
    }

    @Override
    public Integer getCount(Long userId) {
        Unread unread = lambdaQuery().eq(Unread::getUserId, userId).one();
        if (unread == null) {
            return 0;
        }

        return unread.getCount();
    }
}
