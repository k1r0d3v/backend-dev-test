package com.example.backenddevtest.repository.products.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ExternalProductsRepository} adapter configuration class.
 */
@Configuration
@ConfigurationProperties(prefix = "products.repository.external")
public class ExternalProductsRepositoryConfiguration {
    private String host;
    private String port;
    private Integer connectionTimeoutSeconds;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Integer getConnectionTimeoutSeconds() {
        return connectionTimeoutSeconds;
    }

    public void setConnectionTimeoutSeconds(Integer connectionTimeoutSeconds) {
        this.connectionTimeoutSeconds = connectionTimeoutSeconds;
    }
}
