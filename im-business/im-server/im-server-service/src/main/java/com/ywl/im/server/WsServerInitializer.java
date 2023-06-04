package com.ywl.im.server;

import com.ywl.im.server.core.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WsServerInitializer implements CommandLineRunner {

    @Autowired
    Server server;

    @Override
    public void run(String... args) throws Exception {
        initWsServer();
    }

    private void initWsServer() throws Exception {
        server.start();

        new Thread(() -> {

            // 作为客户端连接redis中保存的其他服务器

            // 将netty服务注册到nacos中 同时将当前netty服务添加到redis中

            // 定时轮循到nacos 监听是否有新的服务器注册上来 注册上来 进行连接

        }).start();
    }
}
