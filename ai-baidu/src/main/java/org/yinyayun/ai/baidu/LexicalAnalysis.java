package org.yinyayun.ai.baidu;

import org.json.JSONObject;

import com.baidu.aip.nlp.AipNlp;

/**
 * 词法分析
 * 
 * @author yinyayun
 */
public class LexicalAnalysis {
    private ClientFactory factory;
    
    public LexicalAnalysis(ClientFactory factory) {
        this.factory = factory;
    }

    public String lexical(String text) {
        AipNlp client = factory.buildClient();
        JSONObject object = client.lexer(text);
        return object.toString();
    }
}
