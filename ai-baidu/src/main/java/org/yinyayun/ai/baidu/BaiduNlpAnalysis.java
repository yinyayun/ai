package org.yinyayun.ai.baidu;

import org.json.JSONObject;

import com.baidu.aip.nlp.AipNlp;

/**
 * 百度NLP接口
 * 
 * @author yinyayun
 */
public class BaiduNlpAnalysis {
    private ClientFactory factory;

    public BaiduNlpAnalysis(ClientFactory factory) {
        this.factory = factory;
    }

    public String lexical(String text) {
        return lexical(text, 3);
    }

    /**
     * 词法分析
     * 
     * @param text 长度不能超过16KB
     * @param retryTimes
     * @return null:如果无数据返回，或者调用出错
     */
    public String lexical(String text, int retryTimes) {
        AipNlp client = factory.buildClient();
        for (int i = 0; i < retryTimes; i++) {
            JSONObject object = client.lexer(text);
            if (object.has("error_code")) {
                if (i != (retryTimes - 1)) {
                    continue;
                }
            }
            else {
                return object.toString();
            }
        }
        return null;
    }

    public String sentenceParser(String text) {
        return sentenceParser(text, 3);
    }

    /**
     * 句法分析接口
     * 
     * @return
     */
    public String sentenceParser(String text, int retryTimes) {
        AipNlp client = factory.buildClient();
        for (int i = 0; i < retryTimes; i++) {
            JSONObject object = client.depParser(text, null);
            if (object.has("error_code")) {
                if (i != (retryTimes - 1)) {
                    continue;
                }
            }
            else {
                return object.toString();
            }
        }
        return null;
    }
}
