package org.yinyayun.ai.baidu.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author yinyayun
 */
public class PropertiesUtil {
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
}
