
package org.yinyayun.ai.baidu;

import java.util.List;

import org.yinyayun.ai.baidu.api.BaiduNlpSDK;
import org.yinyayun.ai.baidu.utils.AppConfig;
import org.yinyayun.ai.baidu.utils.PropertiesUtil;

/**
 * @author yinyayun
 */
public class BaiduNlpAnalysisTest {
	public static void main(String[] args) {
		List<AppConfig> configs = PropertiesUtil.allAppConfigs();
		BaiduNlpSDK lexicalAnalysis = new BaiduNlpSDK(configs.get(0), null);
		try {
			String response = lexicalAnalysis.lexical("新一站保险网是新一站保险代理有限公司所有并运营的在线保险服务网站.");
			System.out.println(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// String source = FileUtils.readFileToString(new
		// File("data/response_json.txt"), Charset.forName("utf-8"));
		// JSONObject jsonObject = new JSONObject(source);
		// System.out.println(jsonObject);
	}
}
