package com.ywl.im.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;


@Slf4j
@Service
public class NacosService {
    @Value("${spring.profiles.active}")
    private String nacosNamespace;
    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddr;
    private NamingService imNamingService;
    @Autowired
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public List<Instance> getAllInstances(String serviceName) throws NacosException {
        return getNacosNamingService().getAllInstances("han-im-netty");
    }

    public void registerNacosListen(String listenServiceName, EventListener eventListener) throws NacosException {
        NamingService nacosNamingService = getNacosNamingService();
        nacosNamingService.subscribe(listenServiceName, eventListener);
    }

    public synchronized NamingService getNacosNamingService() throws NacosException {
        if (imNamingService != null) {
            return imNamingService;
        }
        Properties properties = new Properties();
        properties.setProperty("serverAddr", serverAddr);
        properties.setProperty("namespace", nacosNamespace);
        imNamingService = NamingFactory.createNamingService(properties);
        return imNamingService;
    }

    public void registerInstance(String nacosNettyName, String hostAddress, int port, String group, Runnable callback) throws NacosException {
        scheduledThreadPoolExecutor.submit(() -> {
            try {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                NamingService nacosNamingService = getNacosNamingService();
                nacosNamingService.registerInstance(nacosNettyName, hostAddress, port, group);
                callback.run();
                countDownLatch.await();
            } catch (NacosException | InterruptedException e) {

            }
        });

    }
}
