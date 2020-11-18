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
     * 将从 mq 中读取到的消息进行持久化保存
     *
     * @param msg ProtoMsg.Message
     * @return 返回操作结果
     */
    boolean saveMessage(ProtoMsg.Message msg);
}
