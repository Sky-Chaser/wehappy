package cn.chasers.wehappy.chat.codec;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
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
public class MessageDecoder extends MessageToMessageDecoder<WebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        ByteBuf in = frame.content();
        // 标记一下当前的读指针 readIndex 的位置
        in.markReaderIndex();

        // 判断包头的长度
        if (in.readableBytes() < Integer.BYTES) {
            // 长度不够
            return;
        }

        // 读取传送过来的的消息体的长度
        int len = in.readInt();

        // 判断消息体的长度
        if (in.readableBytes() < len) {
            // 长度不够
            in.resetReaderIndex();
            return;
        }

        byte[] content;
        if (in.hasArray()) {
            // 堆缓冲区
            ByteBuf slice = in.slice();
            content = slice.array();
        } else {
            // 直接缓冲区
            content = new byte[len];
            in.readBytes(content, 0, len);
        }

        // 将字节数组转化为 Protobuf 对应的 POJO 类对象
        ProtoMsg.Message msg = ProtoMsg.Message.parseFrom(content);
        if (msg == null) {
            return;
        }

        // 传送给下一个 handler
        out.add(msg);
    }
}