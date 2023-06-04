package com.ywl.im.client;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ywl.framework.common.callback.ImCallback;
import com.ywl.im.client.core.impl.WsClient;
import com.ywl.im.client.handler.RedisHandler;
import com.ywl.im.client.handler.netty.MessageHandler;
import com.ywl.im.client.utils.ClientUtil;
import com.ywl.im.enums.ConnectTypeEnum;
import com.ywl.im.nacos.NacosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class ClientInitializer implements CommandLineRunner {

    /**
     * ip和端口号分割符
     */
    public static final String ADDRESS_SPLIT = ":";

    /**
     * ws协议
     */
    public static final String WS_PROTOCOL = "ws://";

    @Autowired
    private MessageHandler messageHandler;
    @Autowired
    private RedisHandler redisHandler;
    @Autowired
    private NacosService nacosService;
    @Autowired
    private ScheduledThreadPoolExecutor executorService;

    @Override
    public void run(String... args) throws Exception {
        initWsClient();
    }

    /**
     * 初始化 WebSocket 客户端
     * 从 Redis 中获取所有的 Netty 服务器地址，并连接到这些服务器
     * 每个服务器对应一个 WebSocket 客户端
     */
    private void initWsClient() throws Exception {
        // 定时轮循到 nacos 监听是否有新的服务器注册上来 注册上来 进行连接
        executorService.scheduleWithFixedDelay(() -> {
            try {
                List<Instance> instances = nacosService.getAllInstances("han-im-netty");
                log.debug("定时获取到nacos上的netty服务 {}", instances);
                for (Instance instance : instances) {
                    redisHandler.addNettyServerIfNotExist(instance.getIp(), instance.getPort());
                    connectServer(instance.getIp(), instance.getPort());
                }

            } catch (NacosException e) {

            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    private void connectServer(String ip, int port) {
        if (ClientUtil.containsClient(ip, port)) {
            return;
        }

        // 构造 WebSocket 连接的 URL
        String url = WS_PROTOCOL + ip + ADDRESS_SPLIT + port + "/ws" + "?connect=" + ConnectTypeEnum.INTERNAL.getCode();

        // 创建 WebSocket 客户端实例
        WsClient client = new WsClient(ip, port, url, messageHandler);

        ImCallback callback = () -> {
            // 连接成功回调
            // 在此可以执行一些操作，比如将客户端添加为服务器的客户端
            ClientUtil.addClient(ip, port, client);
        };
        log.info("{} 作为客户端连接其他服务器 {} {}", "?", ip, port);

        // 提交任务给线程池，启动 WebSocket 客户端连接
        executorService.submit(() -> {
            client.open(callback);
        });
    }

}
