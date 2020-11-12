package cn.chasers.wehappy.gateway.ws;

import cn.chasers.wehappy.common.constant.AuthConstant;
import cn.chasers.wehappy.common.domain.UserDto;
import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
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

    public static ConcurrentHashMap<Long, WebSocketClient> clients = new ConcurrentHashMap<>(200);

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        HandshakeInfo handshakeInfo = session.getHandshakeInfo();
        InetSocketAddress remoteAddress = handshakeInfo.getRemoteAddress();

        String token;
        UserDto userDto;

        try {
            token = handshakeInfo.getUri().getQuery().split("=")[1];
            if (StrUtil.isEmpty(token)) {
                return Mono.empty();
            }
            // 从token中解析用户信息并设置到Header中去
            String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userStr = jwsObject.getPayload().toString();
            userDto = JSONUtil.toBean(userStr, UserDto.class);
            log.info("PushHandler.handle() userDto:{}", userDto);
        } catch (Exception e) {
            return Mono.empty();
        }

        if (userDto == null) {
            return Mono.empty();
        }

        // 出站
        Mono<Void> output = session.send(Flux.create(sink -> handleClient(userDto.getId(), new WebSocketClient(sink, session))));

        // 入站
        Mono<Void> input = session.receive()
                // 建立连接时触发
                .doOnSubscribe(conn -> {
                    log.info("new websocket session: {}, ip: {}", session.getId(), Objects.requireNonNull(remoteAddress).getAddress());
                })
                // 客户端发送消息时触发
                .doOnNext(msg -> {
                    String message = msg.getPayloadAsText();
                    log.info("message: {}", message);
                    ProtoMsg.PushMessage pushMessage =
                            ProtoMsg.PushMessage.newBuilder()
                                    .setContentType(ProtoMsg.ContentType.TEXT)
                                    .setTime(System.currentTimeMillis())
                                    .setContent("hahahaha")
                                    .build();

                    ProtoMsg.Message m =
                            ProtoMsg.Message.newBuilder()
                                    .setMessageType(ProtoMsg.MessageType.PUSH_MESSAGE)
                                    .setTo(userDto.getId())
                                    .setPushMessage(pushMessage)
                                    .setSequence(2222)
                                    .build();

                    sendTo(m);
                })
                // 连接结束时触发
                .doOnComplete(() -> {
                    log.info("websocket session completed: {}", session.getId());
                    removeUser(userDto.getId());
                    session.close().toProcessor().then();
                })
                // 连接关闭时触发
                .doOnCancel(() -> {
                    log.info("websocket session closed: {}", session.getId());
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
