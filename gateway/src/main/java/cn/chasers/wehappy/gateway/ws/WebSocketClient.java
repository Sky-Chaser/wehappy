package cn.chasers.wehappy.gateway.ws;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.hutool.Hutool;
import cn.hutool.core.convert.Convert;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.io.Serializable;

/**
 * 存储 websocket 客户端信息（类似句柄）
 *
 * @author zhangyuanhang
 */
@Slf4j
@Data
public class WebSocketClient implements Serializable {
    private FluxSink<WebSocketMessage> sink;
    private WebSocketSession session;

    public WebSocketClient(FluxSink<WebSocketMessage> sink, WebSocketSession session) {
        this.sink = sink;
        this.session = session;
    }

    /**
     * 推送消息给客户端的操作
     *
     * @param message 推送的消息
     */
    public void sendData(ProtoMsg.Message message) {
        byte[] body = message.toByteArray();
        int len = Integer.BYTES + body.length;
        byte[] head = Convert.intToBytes(len);
        sink.next(session.binaryMessage(bufferFactory -> bufferFactory.allocateBuffer(len).write(head).write(body)));
    }
}