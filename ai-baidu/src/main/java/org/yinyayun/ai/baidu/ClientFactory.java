package org.yinyayun.ai.baidu;

import org.yinyayun.ai.baidu.utils.PropertiesUtil;

import com.baidu.aip.nlp.AipNlp;

/**
 * 根据配置构建百度aip的客户端
 * 
 * @author yinyayun
 */
public class ClientFactory {
    private final String APP_ID = "app.id";
    private final String API_KEY = "api.key";
    private final String SECRET_KEY = "secret.key";
    private final String CONN_TIMEOUT = "conn.timeout";
    private final String SOCKET_TIMEOUT = "socket.timeout";

    public AipNlp buildClient() {
        String appid = PropertiesUtil.getString(APP_ID);
        String aipKey = PropertiesUtil.getString(API_KEY);
        String secretKey = PropertiesUtil.getString(SECRET_KEY);
        int connTimeout = PropertiesUtil.getInt(CONN_TIMEOUT, 3000);
        int socketTimeout = PropertiesUtil.getInt(SOCKET_TIMEOUT, 60000);
        AipNlp aipNlp = new AipNlp(appid, aipKey, secretKey);
        aipNlp.setConnectionTimeoutInMillis(connTimeout);
        aipNlp.setSocketTimeoutInMillis(socketTimeout);
        return aipNlp;
    }
}
