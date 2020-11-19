package cn.chasers.wehappy.message.service;

import cn.chasers.wehappy.message.entity.MessageDelete;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 消息删除记录表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
public interface IMessageDeleteService extends IService<MessageDelete> {

    /**
     * 根据 消息 id 和用户 id 删除消息的数据
     *
     * @param userId 用户 Id
     * @param id 消息 Id
     * @return 操作结果
     */
    Boolean add(Long userId, Long id);
}
