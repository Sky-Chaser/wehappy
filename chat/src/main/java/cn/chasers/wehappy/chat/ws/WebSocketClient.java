package cn.chasers.wehappy.chat.ws;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.hutool.core.convert.Convert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.FluxSink;

import java.io.Serializable;

/**
 * 存储 websocket 客户端信息（类似句柄）
 *
 * @author lollipop
 */
@Slf4j
@Data
public class WebSocketClient implements Serializable {
    private Long userId;
    private FluxSink<WebSocketMessage> sink;
    private WebSocketSession session;

    public WebSocketClient(FluxSink<WebSocketMessage> sink, WebSocketSession session, Long userId) {
        this.sink = sink;
        this.session = session;
        this.userId = userId;
    }

    /**
     * 推送消息给客户端的操作
     *
     * @param message 推送的消息
     */
    public void sendData(ProtoMsg.Message message) {
        byte[] body = message.toByteArray();
        sink.next(session.binaryMessage(bufferFactory -> bufferFactory.wrap(body)));
    }
}
