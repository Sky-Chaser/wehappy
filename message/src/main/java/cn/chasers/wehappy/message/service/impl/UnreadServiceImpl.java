package cn.chasers.wehappy.message.service.impl;

import cn.chasers.wehappy.message.entity.Unread;
import cn.chasers.wehappy.message.mapper.UnreadMapper;
import cn.chasers.wehappy.message.service.IUnreadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public boolean increase(Long userId, int count) {
        return false;
    }

    @Override
    public boolean decrease(Long userId, int count) {
        return false;
    }

    @Override
    public boolean update(Long userId, int count) {
        return false;
    }

    @Override
    public boolean get(Long userId) {
        return false;
    }
}
