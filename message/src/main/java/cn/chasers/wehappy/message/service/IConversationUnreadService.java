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
     * 减少未读个数
     *
     * @param conversationId 会话Id
     * @param count          个数
     * @return
     */
    boolean decrease(Long conversationId, int count);

    /**
     * 更新未读个数
     *
     * @param conversationId 会话Id
     * @param count          个数
     * @return
     */
    boolean update(Long conversationId, int count);

    /**
     * 查询未读个数
     *
     * @param conversationId 会话Id
     * @return
     */
    boolean get(Long conversationId);
}
