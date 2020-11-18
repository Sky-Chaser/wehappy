package cn.chasers.wehappy.message.service;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.chasers.wehappy.message.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
