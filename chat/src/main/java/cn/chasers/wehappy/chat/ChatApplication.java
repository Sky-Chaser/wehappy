package cn.chasers.wehappy.chat;

import cn.chasers.wehappy.chat.server.ChatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lollipop
 * @date 2020/11/4
 */
@SpringBootApplication(scanBasePackages = "cn.chasers.wehappy")
public class ChatApplication implements CommandLineRunner {

    private final ChatServer chatServer;

    @Autowired
    public ChatApplication(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        chatServer.start();
    }
}
