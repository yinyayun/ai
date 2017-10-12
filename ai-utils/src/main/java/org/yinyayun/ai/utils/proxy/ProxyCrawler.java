package org.yinyayun.ai.utils.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 代理抓取
 * 
 * @author yinyayun
 *
 */
public class ProxyCrawler {
	private final Pattern IP_PATTERN = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");
	private final Pattern PORT_PATTERN = Pattern.compile("\\d+");
	private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";
	private String proxyUrl = "http://www.xicidaili.com/nt/";
	private String testUrl = "https://aip.baidubce.com";
	private ExecutorService pool = Executors.newFixedThreadPool(100);

	public ProxyCrawler() {
	}

	public ProxyCrawler(String proxyUrl, String testUrl) {
		this.proxyUrl = proxyUrl;
		this.testUrl = testUrl;
	}

	public List<ProxyEntity> crawler() {
		try {
			Document document = Jsoup.connect(proxyUrl).userAgent(userAgent).timeout(5000).get();
			return parser(document);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<ProxyEntity> parser(Document document) {
		List<ProxyEntity> proxyEntities = new ArrayList<ProxyEntity>();
		Element ipTable = document.getElementById("ip_list");
		Elements trs = ipTable.getElementsByTag("tr");
		for (int i = 1; i < trs.size(); i++) {
			Elements tds = trs.get(i).getElementsByTag("td");
			String ip = tds.get(1).text();
			String port = tds.get(2).text();
			if (IP_PATTERN.matcher(ip).find() && PORT_PATTERN.matcher(port).find()) {
				proxyEntities.add(new ProxyEntity(ip, Integer.valueOf(port)));
			}
		}
		// 测试
		List<Future<ProxyEntity>> futures = new ArrayList<Future<ProxyEntity>>();
		for (ProxyEntity proxyEntity : proxyEntities) {
			futures.add(pool.submit(() -> testProxy(proxyEntity)));
		}
		List<ProxyEntity> goodProxys = new ArrayList<ProxyEntity>();
		for (Future<ProxyEntity> future : futures) {
			try {
				ProxyEntity entity = future.get();
				if (entity != null) {
					goodProxys.add(entity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return goodProxys;
	}

	public ProxyEntity testProxy(ProxyEntity entity) {
		Connection connection = Jsoup.connect(testUrl);
		try {
			connection.timeout(1500).proxy(entity.ip, entity.port).get();
		} catch (Exception e) {
			return null;
		}
		System.out.println("验证通过：" + entity);
		return entity;
	}
}
