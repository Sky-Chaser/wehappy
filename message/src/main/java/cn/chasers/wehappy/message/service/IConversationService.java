package cn.chasers.wehappy.message.service;

import cn.chasers.wehappy.message.entity.Conversation;
import cn.chasers.wehappy.message.entity.MessageIndex;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 最近会话表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
public interface IConversationService extends IService<Conversation> {

    /**
     * 保存最近会话表信息，若记录已存在则更新
     *
     * @param index 消息索引
     * @return 返回操作结果
     */
    Conversation saveOrUpdate(MessageIndex index);

    /**
     * 删除会话信息
     * 同时更新会话未读数和用户总未读数
     *
     * @param id 会话 Id
     * @return 返回操作结果
     */
    boolean remove(Long id);
}
