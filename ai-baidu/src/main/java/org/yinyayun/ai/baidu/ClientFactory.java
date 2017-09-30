package org.yinyayun.ai.baidu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.yinyayun.ai.baidu.utils.PropertiesUtil;

import com.baidu.aip.nlp.AipNlp;

/**
 * 根据配置构建百度aip的客户端
 * 
 * @author yinyayun
 */
public class ClientFactory {
	public final static String APP_ID = "app.id";
	public final static String API_KEY = "api.key";
	public final static String SECRET_KEY = "secret.key";
	public final static String CONN_TIMEOUT = "conn.timeout";
	public final static String SOCKET_TIMEOUT = "socket.timeout";
	//
	private final int connTimeout;
	private final int socketTimeout;
	private AtomicInteger indexer = new AtomicInteger(0);
	private List<AppConfig> configs = new ArrayList<AppConfig>();
	private Map<String, AipNlp> clients = new HashMap<String, AipNlp>();

	private static ClientFactory clientFactory;

	public static ClientFactory clientFactoryInstance() {
		if (clientFactory == null) {
			clientFactory = new ClientFactory();
		}
		return clientFactory;
	}

	private ClientFactory() {
		this.connTimeout = PropertiesUtil.getInt(CONN_TIMEOUT, 3000);
		this.socketTimeout = PropertiesUtil.getInt(SOCKET_TIMEOUT, 60000);
		Map<String, Map<String, String>> groupConfigs = PropertiesUtil
				.groupSuffixconfigKeys(new String[] { APP_ID, API_KEY, SECRET_KEY });
		groupConfigs.forEach((k, v) -> configs.add(new AppConfig(v.get(APP_ID), v.get(API_KEY), v.get(SECRET_KEY))));
	}

	public AipNlp buildClient() {
		int index = indexer.getAndIncrement();
		indexer.compareAndSet(configs.size(), 0);
		AppConfig config = configs.get(index);
		String configKey = config.toString();
		clients.computeIfAbsent(configKey, k -> {
			AipNlp client = new AipNlp(config.appid, config.apiKey, config.secretKey);
			client.setConnectionTimeoutInMillis(connTimeout);
			client.setSocketTimeoutInMillis(socketTimeout);
			return client;
		});
		return clients.get(configKey);
	}

	class AppConfig {
		String appid;
		String apiKey;
		String secretKey;

		public AppConfig(String appid, String apiKey, String secretKey) {
			this.appid = appid;
			this.apiKey = apiKey;
			this.secretKey = secretKey;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(appid).append("-").append(apiKey).append("-").append(secretKey);
			return builder.toString();
		}
	}
}
