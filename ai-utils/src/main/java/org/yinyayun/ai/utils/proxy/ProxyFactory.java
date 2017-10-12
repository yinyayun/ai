package org.yinyayun.ai.utils.proxy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxyFactory {
	private List<ProxyEntity> proxyEntities;
	private AtomicInteger indexer = new AtomicInteger(0);

	public ProxyFactory(ProxyCrawler proxyCrawler) {
		if (proxyCrawler != null)
			this.proxyEntities = proxyCrawler.crawler();
	}

	public ProxyEntity take() {
		if (proxyEntities == null) {
			return null;
		}
		int index = indexer.getAndIncrement();
		indexer.compareAndSet(proxyEntities.size() - 1, 0);
		return proxyEntities.get(index);
	}
}
