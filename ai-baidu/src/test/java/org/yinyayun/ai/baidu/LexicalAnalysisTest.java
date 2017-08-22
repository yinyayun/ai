
package org.yinyayun.ai.baidu;

/**
 * @author yinyayun
 */
public class LexicalAnalysisTest {
    public static void main(String[] args) {
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis(new ClientFactory());
        String response = lexicalAnalysis.lexical("新一站保险网是新一站保险代理有限公司所有并运营的在线保险服务网站，是焦点科技旗下继中国制造网后的又一大型电子商务平台");
        System.out.println(response);
    }
}
