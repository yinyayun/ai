/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.nlp.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yinyayun.nlp.TokenizerTrain;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * NLPTrain.java
 *
 * @author yinyayun
 */
public abstract class NLPTrain {
    @Parameter(names = "-propertiesPath", required = true, description = "系统配置文件，主要含有日志配置信息")
    private String propertiesPath = "conf/system.properties";
    @Parameter(names = "-trainFile", required = true, description = "被训练的文件")
    protected String trainFile;
    @Parameter(names = "-modelSavePath", required = true, description = "模型保存地址")
    protected String modelSavePath;
    @Parameter(names = "-threads", description = "线程数")
    protected String threads = "3";
    @Parameter(names = "-cutoff", description = "词频阈值")
    protected String cutoff = "30";

    public void start(NLPTrain train, String[] args) {
        new JCommander(train, args);
        PropertiesLoader propertiesLoader = new PropertiesLoader(propertiesPath);
        propertiesLoader.initLog4j();
        Logger logger = LoggerFactory.getLogger(TokenizerTrain.class);
        logger.info("train...");
        try {
            train(propertiesLoader);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
        logger.info("end...");
    }

    public abstract void train(PropertiesLoader propertiesLoader) throws Exception;
}
