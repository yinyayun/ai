
package org.yinyayun.ai.baidu;

import java.io.IOException;

/**
 * @author yinyayun
 */
public class BaiduNlpAnalysisTest {
    public static void main(String[] args) throws IOException {
        BaiduNlpAnalysis lexicalAnalysis = new BaiduNlpAnalysis(new ClientFactory());
        String response = lexicalAnalysis.sentenceParser("新一站保险网是新一站保险代理有限公司所有并运营的在线保险服务网站.");
        System.out.println(response);
        // String source = FileUtils.readFileToString(new File("data/response_json.txt"), Charset.forName("utf-8"));
        // JSONObject jsonObject = new JSONObject(source);
        // System.out.println(jsonObject);
    }
}
