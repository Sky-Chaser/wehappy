package cn.chasers.wehappy.chat;

import cn.chasers.wehappy.chat.server.ChatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lollipop
 * @date 2020/11/4
 */
@SpringBootApplication
public class ChatApplication implements CommandLineRunner {

    private final ExecutorService executor;

    private final ChatServer chatServer;

    @Autowired
    public ChatApplication(ChatServer chatServer) {
        executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.AbortPolicy());
        this.chatServer = chatServer;
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        executor.submit(chatServer::start);
    }
}
