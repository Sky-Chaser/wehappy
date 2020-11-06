package cn.chasers.wehappy.gateway.config;

import cn.chasers.wehappy.gateway.ws.PushHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * websocket 配置类
 *
 * @author zhangyuanhang
 */
@Configuration
public class WebSocketConfiguration {
    @Bean
    public HandlerMapping webSocketMapping(PushHandler pushHandler) {

        // 使用 map 指定 WebSocket 协议的路由，路由为 ws://localhost:8080/receive
        Map<String, WebSocketHandler> map = new HashMap<>(1);
        map.put("/receive", pushHandler);

        // SimpleUrlHandlerMapping 指定了 WebSocket 的路由配置
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        mapping.setUrlMap(map);
        return mapping;
    }

    /**
     * WebSocketHandlerAdapter 负责将 PushHandler 处理类适配到 WebFlux 容器中
     *
     * @return WebSocketHandlerAdapter
     */
    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}