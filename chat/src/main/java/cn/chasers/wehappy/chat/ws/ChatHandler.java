package cn.chasers.wehappy.chat.ws;

import cn.chasers.wehappy.chat.handler.dispatcher.MessageHandler;
import cn.chasers.wehappy.chat.handler.dispatcher.MessageHandlerContainer;
import cn.chasers.wehappy.common.constant.AuthConstant;
import cn.chasers.wehappy.common.domain.UserDto;
import cn.chasers.wehappy.common.msg.ProtoMsg;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;


/**
 * 接收聊天消息的 Handler
 *
 * @author lollipop
 * @date 2020/11/14
 */
@Component
@Slf4j
public class ChatHandler implements WebSocketHandler {

    /**
     * 存储所有websocket客户端session信息
     */
    public final ConcurrentHashMap<Long, WebSocketClient> clients;

    private final MessageHandlerContainer messageHandlerContainer;
    private final ExecutorService executor;

    @Autowired
    public ChatHandler(MessageHandlerContainer messageHandlerContainer) {
        this.messageHandlerContainer = messageHandlerContainer;
        clients = new ConcurrentHashMap<>(400);
        executor = new ThreadPoolExecutor(
                200,
                200,
                30,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList(AuthConstant.JWT_TOKEN_PREFIX.trim());
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        HandshakeInfo handshakeInfo = session.getHandshakeInfo();

        String token;
        UserDto userDto;

        try {
            token = handshakeInfo.getHeaders().getFirst(AuthConstant.SEC_WEBSOCKET_PROTOCOL);
            if (StrUtil.isEmpty(token)) {
                return Mono.empty();
            }

            // 从token中解析用户信息
            String realToken = token.replace(AuthConstant.WS_JWT_TOKEN_PREFIX, "").trim();
            userDto = JSONUtil.parse(JWSObject.parse(realToken).getPayload().toString()).toBean(UserDto.class);
        } catch (Exception e) {
            log.error("parse token error", e);
            return Mono.empty();
        }

        if (userDto == null) {
            return Mono.empty();
        }

        final long userId = userDto.getId();

        // 出站，保存 websocket session 信息
        Mono<Void> output = session.send(Flux.create(sink -> handleClient(userId, new WebSocketClient(sink, session, userId))));

        // 入站
        Mono<Void> input = session.receive()
                .doOnComplete(() -> {
                    removeUser(userId);
                    session.close().toProcessor().then();
                })
                .doOnCancel(() -> {
                    removeUser(userId);
                    session.close().toProcessor().then();
                })
                .doOnNext(msg -> handleMessage(msg, userId))
                .doOnError(e -> {
                    log.error("ws error: ", e);
                    removeUser(userId);
                    session.close().toProcessor().then();
                })
                .then();

        return Mono.zip(input, output).then();
    }

    private void handleMessage(WebSocketMessage msg, long userId) {
        try {
            ProtoMsg.Message message = ProtoMsg.Message.parseFrom(msg.getPayload().asByteBuffer());
            MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(message.getMessageTypeValue());
            executor.submit(() -> messageHandler.execute(message, clients.get(userId)));
        } catch (InvalidProtocolBufferException e) {
            log.error("handlerMessage error: ", e);
        }
    }

    private void handleClient(long userId, WebSocketClient client) {
        clients.put(userId, client);
        log.info("User：{}，online!", userId);

        // Todo 发送上线消息到 mq
    }

    private void removeUser(long userId) {
        clients.remove(userId);
        log.info("User：{}，offline!", userId);
    }
}
