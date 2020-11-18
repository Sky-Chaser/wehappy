package cn.chasers.wehappy.message.service;

import cn.chasers.wehappy.message.entity.MessageIndex;
import com.baomidou.mybatisplus.extension.service.IService;


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
}
