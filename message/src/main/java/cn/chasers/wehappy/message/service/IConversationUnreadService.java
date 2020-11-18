package cn.chasers.wehappy.message.service;

import cn.chasers.wehappy.message.entity.ConversationUnread;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会话未读数表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
public interface IConversationUnreadService extends IService<ConversationUnread> {

    /**
     * 增加未读个数
     *
     * @param conversationId 会话Id
     * @param count          个数
     * @return
     */
    boolean increase(Long conversationId, int count);

    /**
     * 更新未读个数
     *
     * @param conversationId 会话Id
     * @param count          个数
     * @return
     */
    boolean update(Long conversationId, int count);

    /**
     * 根据会话最后一次查看的消息 Id 更新未读消息个数
     *
     * @param conversationId 会话 Id
     * @param messageId      消息 Id
     * @return
     */
    boolean updateByLastReadMessageId(Long conversationId, Long messageId);

    /**
     * 查询未读个数
     *
     * @param conversationId 会话Id
     * @return
     */
    ConversationUnread get(Long conversationId);
}
