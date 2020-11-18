package cn.chasers.wehappy.message.service;

import cn.chasers.wehappy.message.entity.Message;
import cn.chasers.wehappy.message.entity.MessageIndex;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 消息索引表 服务类
 * </p>
 *
 * @author lollipop
 * @since 2020-11-16
 */
public interface IMessageIndexService extends IService<MessageIndex> {
    /**
     * 保存消息索引信息
     *
     * @param type      消息类型
     * @param from      发送者
     * @param to        接收者
     * @param messageId 消息Id
     * @return MessageIndex
     */
    MessageIndex save(Integer type, Long from, Long to, Long messageId);

    /**
     * 查询所有消息索引信息
     *
     * @param id          会话 Id
     * @param messageId   消息 Id，只查询 messageId 小于这条消息的消息，null 或 0 表示从最新的开始查
     * @param currentPage 当前页数
     * @param size        每页记录数
     * @return 消息索引集合
     */
    List<MessageIndex> queryByConversationId(Long id, Long messageId, Long currentPage, Long size);
}
