package cn.chasers.wehappy.chat.handler.dispatcher;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lollipop
 * @date 2020/11/05 11:17:16
 */
@Slf4j
@Component
public class MessageHandlerContainer implements InitializingBean {

    private final Map<Integer, MessageHandler> handlers = Maps.newHashMap();

    private final ApplicationContext applicationContext;

    @Autowired
    public MessageHandlerContainer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        // 通过 ApplicationContext 获得所有 MessageHandler Bean
        applicationContext.getBeansOfType(MessageHandler.class)
                .forEach((name, messageHandler) -> handlers.put(messageHandler.getType(), messageHandler));

        log.info("messageHandler count = {}", handlers.size());
    }

    /**
     * 获取对应类型的MessageHandler
     * @param type 类型
     * @return MessageHandler
     */
    public MessageHandler getMessageHandler(Integer type) {
        return handlers.get(type);
    }
}
