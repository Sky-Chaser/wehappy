package cn.chasers.wehappy.chat.codec;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * protobuf 消息解码器
 *
 * @author lollipop
 * @date 2020/11/4
 */
@Slf4j
public class MessageEncoder extends MessageToMessageEncoder<ProtoMsg.Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtoMsg.Message msg, List<Object> out) throws Exception {
        // 将对象转化为字节
        byte[] bytes = msg.toByteArray();

        // 读取消息的长度
        int len = bytes.length;

        ByteBuf buf = Unpooled.buffer(bytes.length + Integer.BYTES);

        // 写入消息头,即消息长度
        buf.writeInt(len);
        // 写入消息体
        buf.writeBytes(bytes);

        // 封装成WebSocketFrame
        WebSocketFrame frame = new BinaryWebSocketFrame(buf);
        out.add(frame);
    }
}