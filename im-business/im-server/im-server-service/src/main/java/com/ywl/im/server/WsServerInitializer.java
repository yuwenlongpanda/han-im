package com.ywl.im.server;

import com.alibaba.nacos.api.exception.NacosException;
import com.ywl.framework.common.callback.ImCallback;
import com.ywl.im.server.core.Server;
import com.ywl.im.server.handler.RedisHandler;
import com.ywl.im.server.nacos.NacosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WsServerInitializer implements CommandLineRunner {

    @Autowired
    private Server server;
    @Autowired
    private NacosService nacosService;
    @Autowired
    private RedisHandler redisHandler;


    @Override
    public void run(String... args) throws Exception {
        initWsServer();
    }

    private void initWsServer() throws Exception {
        ImCallback startCallback = () -> {
            ImCallback callback = () -> {
                redisHandler.addNettyServer(server.getIp(), server.getPort());
            };

            try {
                nacosService.registerInstance("han-im-netty", server.getIp(), server.getPort(), "DEFAULT", callback);
            } catch (NacosException e) {
                throw new RuntimeException(e);
            }
        };

        server.start(startCallback);
    }
}
