package org.yinyayun.ai.baidu.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * @author yinyayun
 */
public class PropertiesUtil {
    public final static String APP_ID = "app.id";
    public final static String API_KEY = "api.key";
    public final static String SECRET_KEY = "secret.key";
    public final static String CONN_TIMEOUT = "conn.timeout";
    public final static String SOCKET_TIMEOUT = "socket.timeout";
    //
    private static Properties properties = new Properties();
    static {
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("system.properties"));
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

    public static int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public static List<AppConfig> allAppConfigs() {
        List<AppConfig> configs = new ArrayList<AppConfig>();
        int connTimeout = PropertiesUtil.getInt(CONN_TIMEOUT, 3000);
        int socketTimeout = PropertiesUtil.getInt(SOCKET_TIMEOUT, 60000);
        Map<String, Map<String, String>> groupConfigs = PropertiesUtil
                .groupSuffixconfigKeys(new String[]{APP_ID, API_KEY, SECRET_KEY});
        groupConfigs.forEach((k, v) -> configs
                .add(new AppConfig(v.get(APP_ID), v.get(API_KEY), v.get(SECRET_KEY), connTimeout, socketTimeout)));
        return configs;
    }

    public static Map<String, Map<String, String>> groupSuffixconfigKeys(String[] suffixKeys) {
        Set<String> keys = properties.stringPropertyNames();
        Map<String, Map<String, String>> groups = new HashMap<String, Map<String, String>>(
                keys.size() / suffixKeys.length + 1);
        for (String key : keys) {
            for (String suffix : suffixKeys) {
                if (key.endsWith(suffix)) {
                    String prefix = key.substring(0, key.length() - suffix.length());
                    if (StringUtils.isEmpty(prefix)) {
                        prefix = "default";
                    }
                    groups.putIfAbsent(prefix, new HashMap<String, String>());
                    groups.get(prefix).put(suffix, properties.getProperty(key));
                }
            }
        }
        return groups;
    }
}
