package cn.chasers.wehappy.gateway.ws;

import cn.chasers.wehappy.common.msg.ProtoMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息推送 Handler
 *
 * @author lollipop
 */
@Slf4j
@Component
public class PushHandler implements WebSocketHandler {

    public static ConcurrentHashMap<Long, WebSocketClient> clients = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        HandshakeInfo handshakeInfo = session.getHandshakeInfo();
        InetSocketAddress remoteAddress = handshakeInfo.getRemoteAddress();

        String token = null;

        try {
            token = handshakeInfo.getUri().getQuery().split("=")[1];
            log.info("token = {}", token);
        } catch (Exception e) {
            return Mono.empty();
        }

        if (StringUtils.isEmpty(token)) {
            return Mono.empty();
        }

        long userId = 0;

        // 出站
        Mono<Void> output = session.send(Flux.create(sink -> handleClient(userId, new WebSocketClient(sink, session))));

        // 入站
        Mono<Void> input = session.receive()
                .doOnSubscribe(conn -> {
                    log.info("new websocket session：{}, ip：{}", session.getId(), Objects.requireNonNull(remoteAddress).getAddress());
                })
                .doOnNext(msg -> {
                    String message= msg.getPayloadAsText();
                    log.info("message: {}", message);
                    // TODO
                })
                .doOnComplete(() -> {
                    log.info("关闭连接：{}", session.getId());
                    removeUser(userId);
                    session.close().toProcessor().then();
                }).doOnCancel(() -> {
                    log.info("关闭连接：{}", session.getId());
                }).then();

        return Mono.zip(input, output).then();
    }

    private void removeUser(long userId) {
        clients.remove(userId);
        log.info("用户：{}，离线!", userId);
    }

    private void handleClient(long userId, WebSocketClient client) {
        clients.put(userId, client);
        log.info("用户：{}，上线!", userId);
    }

    /**
     * 发送消息给用户
     *
     * @param message 消息
     */
    public static void sendTo(ProtoMsg.Message message) {
        WebSocketClient client = clients.get(message.getTo());
        if (client == null) {
            return;
        }

        log.info("推送消息到用户：{}，消息：{}", message.getTo(), message);
        client.sendData(message);
    }
}