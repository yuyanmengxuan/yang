package com.wei.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 测试配置的加载
 */
@Configuration
@ConfigurationProperties("callback.system")
public class ConfigURL {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
