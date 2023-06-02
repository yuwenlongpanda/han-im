package com.ywl.framework.job.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.Objects;

@Data
@Configuration
@ConfigurationProperties(prefix = "job")
public class JobConfig {

    private String adminAddresses;

    private String accessToken;

    private String appName;

    private String executorAddress;

    private String executorIp;

    private Integer executorPort;

    private String logPath;

    private Integer logRetentionDays;

    @Resource
    private Environment environment;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        if (Objects.isNull(executorPort)) {
            // 未配置监听端口，则在服务启动端口上增加100
            String port = environment.getProperty("server.port");
            if (StringUtils.isBlank(port)) {
                throw new NullPointerException("获取服务启动端口失败");
            }
            executorPort = Integer.parseInt(port) + 100;
        }
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appName);
        xxlJobSpringExecutor.setAddress(executorAddress);
        xxlJobSpringExecutor.setIp(executorIp);
        xxlJobSpringExecutor.setPort(executorPort);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }

}
