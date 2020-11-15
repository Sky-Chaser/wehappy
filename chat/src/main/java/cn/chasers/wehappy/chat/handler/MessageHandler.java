package cn.chasers.wehappy.chat.handler;

import cn.chasers.wehappy.common.constant.AuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * 接收聊天消息的 Handler
 *
 * @author lollipop
 * @date 2020/11/14
 */
@Component
@Slf4j
public class MessageHandler implements WebSocketHandler {

    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList(AuthConstant.JWT_TOKEN_PREFIX);
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.receive().doOnSubscribe(s -> {
            log.info("发起连接:{}", s);
        }).doOnTerminate(() -> {
            log.info("doOnTerminate");
        }).doOnComplete(() -> {
            log.info("doOnComplete");
        }).doOnCancel(() -> {
            log.info("doOnCancel");
        }).doOnNext(msg -> {
            String message = msg.getPayloadAsText();
            log.info("doOnNext");
            log.info("message = {}", message);
        }).doOnError(e -> {
            log.error("{0}", e);
            e.printStackTrace();
            log.error("doOnError");
        }).doOnRequest(r -> {
            log.info("doOnRequest");
        }).then();
    }
}
