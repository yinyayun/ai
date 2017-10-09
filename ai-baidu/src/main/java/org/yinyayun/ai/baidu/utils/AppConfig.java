/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.ai.baidu.utils;

/**
 * AppConfig.java
 *
 * @author yinyayun
 */
public class AppConfig {
    public String appid;
    public String apiKey;
    public String secretKey;
    public int connTimeout;
    public int socketTimeout;

    public AppConfig(String appid, String apiKey, String secretKey) {
        this(appid, apiKey, secretKey, 3000, 60000);
    }

    public AppConfig(String appid, String apiKey, String secretKey, int connTimeout, int socketTimeout) {
        this.appid = appid;
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.connTimeout = connTimeout;
        this.socketTimeout = socketTimeout;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(appid).append("-").append(apiKey).append("-").append(secretKey);
        return builder.toString();
    }
}
