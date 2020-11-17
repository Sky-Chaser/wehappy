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
import org.springframework.core.io.buffer.DataBuffer;
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
    public static ConcurrentHashMap<Long, WebSocketClient> clients = new ConcurrentHashMap<>(200);

    private final MessageHandlerContainer messageHandlerContainer;
    private final ExecutorService executor;

    @Autowired
    public ChatHandler(MessageHandlerContainer messageHandlerContainer) {
        this.messageHandlerContainer = messageHandlerContainer;
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
        UserDto userDto = null;

        try {
            token = handshakeInfo.getHeaders().getFirst("Sec-WebSocket-Protocol");
            if (StrUtil.isEmpty(token)) {
                return Mono.empty();
            }

            String realToken = token.replace(AuthConstant.WS_JWT_TOKEN_PREFIX, "").trim();
            log.info(realToken);
            // 从token中解析用户信息
            userDto = JSONUtil.parse(JWSObject.parse(realToken).getPayload().toString()).toBean(UserDto.class);
        } catch (Exception e) {
            log.error("parse token error", e);
            return Mono.empty();
        }

        if (userDto == null) {
            return Mono.empty();
        }

        final long userId = userDto.getId();

        // 出站
        Mono<Void> output = session.send(Flux.create(sink -> handleClient(userId, new WebSocketClient(sink, session))));

        // 入站
        Mono<Void> input = session.receive().doOnSubscribe(s -> {
            log.info("发起连接:{}", s);
        }).doOnTerminate(() -> {
            log.info("doOnTerminate");
        }).doOnComplete(() -> {
            log.info("doOnComplete");
            removeUser(userId);
            session.close().toProcessor().then();
        }).doOnCancel(() -> {
            log.info("doOnCancel");
            removeUser(userId);
            session.close().toProcessor().then();
        }).doOnNext(msg -> {
            handleMessage(msg, userId);
            log.info("doOnNext");
        }).doOnError(e -> {
            log.error("", e);
            log.error("doOnError");
            removeUser(userId);
            session.close().toProcessor().then();
        }).doOnRequest(r -> {
            log.info("doOnRequest");
        }).then();

        return Mono.zip(input, output).then();
    }

    private void handleMessage(WebSocketMessage msg, long userId) {
        try {
            DataBuffer buffer = msg.getPayload();
            ProtoMsg.Message message = ProtoMsg.Message.parseFrom(buffer.asByteBuffer());
            MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(message.getMessageTypeValue());
            log.info("message = {}", message);
            executor.submit(() -> messageHandler.execute(message, clients.get(userId)));
        } catch (InvalidProtocolBufferException e) {
            log.error("e");
        }
    }

    private void handleClient(long userId, WebSocketClient client) {
        clients.put(userId, client);
        log.info("用户：{}，上线!", userId);

        ProtoMsg.PushMessage pushMessage =
                ProtoMsg.PushMessage.newBuilder()
                        .setContentType(ProtoMsg.ContentType.TEXT)
                        .setTime(System.currentTimeMillis())
                        .setContent("嘿嘿")
                        .build();

        ProtoMsg.Message message =
                ProtoMsg.Message.newBuilder()
                        .setMessageType(ProtoMsg.MessageType.PUSH_MESSAGE)
                        .setTo(userId)
                        .setPushMessage(pushMessage)
                        .setSequence(4892257)
                        .build();
        sendTo(message);
    }

    private void removeUser(long userId) {
        clients.remove(userId);
        log.info("用户：{}，离线!", userId);
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
