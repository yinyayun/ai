/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.nlp.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * LoaderLog.java
 *
 * @author yinyayun
 */
public class PropertiesLoader {
    private final static String LOG_PATH = "log.path";
    private Properties properties;
    private String dir;

    public PropertiesLoader(String dir) {
        try {
            this.dir = dir;
            this.properties = new Properties();
            this.properties.load(new FileInputStream(new File(dir, "system.properties")));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLogPath() {
        return this.properties.getProperty(LOG_PATH);
    }

    public String getPropertiesDir() {
        return dir;
    }

    public String getValue(String key) {
        return properties.getProperty(LOG_PATH);
    }

    /**
     * 日志文件加载
     * 
     * @param logConf
     * @param logPath
     */
    public void initLog4j() {
        String logOutPath = getLogPath();
        File fold = new File(logOutPath);
        if (!fold.exists()) {
            fold.mkdirs();
        }
        try (InputStream inputStream = new FileInputStream(new File(dir, "log4j.properties"))) {
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.setProperty("log4j.file.dir", logOutPath);
            PropertyConfigurator.configure(properties);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
