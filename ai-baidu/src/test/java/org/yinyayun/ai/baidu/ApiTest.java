package org.yinyayun.ai.baidu;

import org.yinyayun.ai.baidu.api.BaiduApi;
import org.yinyayun.ai.baidu.api.BaiduNlpHttp;
import org.yinyayun.ai.baidu.utils.AppConfig;

public class ApiTest {
	public static void main(String[] args) {
		// 我在你们网上买的一份旅游意外保险的发票能快递吗
		BaiduApi baiduApi = new BaiduNlpHttp(
				new AppConfig("10203449", "OAwFGMygZuM2ePM1kD0jTE90", "1yy1baWs1ln1QvEH0Ky37ZVnOCUkMebR"), null);
		System.out.println(baiduApi.sentenceParser("帮我打开客厅的吸顶灯。"));
	}
}
