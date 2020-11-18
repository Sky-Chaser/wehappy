package cn.chasers.wehappy.message.service;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.message.entity.Message;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 消息表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
public interface IMessageService extends IService<Message> {
    /**
     * 1. 将从 mq 中读取到的消息进行持久化保存
     * 2. 保存消息索引数据
     * 3. 更新最近会话数据
     * 4. 更新会话未读数和用户未读数
     * 5. 当判断消息的接收者在线时，将消息发送到 kafka 中进行推送
     *
     * @param msg ProtoMsg.Message
     * @return 返回操作结果
     */
    boolean save(ProtoMsg.Message msg);

    /**
     * 查询会话的所有未读消息信息
     *
     * @param conversationId 会话 Id
     * @return 消息集合
     */
    List<Message> getUnreadMessagesByConversationId(Long conversationId);

    /**
     * 分页查询会话的消息信息
     *
     * @param conversationId 会话 Id
     * @param messageId      消息 Id，只查询 messageId 小于这条消息的消息
     * @param currentPage    当前页数
     * @param size           每页记录数
     * @return 消息集合
     */
    IPage<Message> getMessagesByConversationId(Long conversationId, Long messageId, Long currentPage, Long size);

    /**
     * 分页查询私聊或群聊的消息信息
     *
     * @param type        聊天类型
     * @param fromId      当前用户 Id
     * @param toId        用户 Id 或 群聊 Id
     * @param messageId   消息 Id，只查询 messageId 小于这条消息的消息
     * @param currentPage 当前页数
     * @param size        每页记录数
     * @return 消息集合
     */
    IPage<Message> getMessagesByToId(Integer type, Long fromId, Long toId, Long messageId, Long currentPage, Long size);
}
