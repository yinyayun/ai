package org.yinyayun.ai.utils.proxy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxyFactory {
	private List<ProxyEntity> proxyEntities;
	private AtomicInteger indexer = new AtomicInteger(0);
	private ProxyCrawler proxyCrawler;

	public ProxyFactory(ProxyCrawler proxyCrawler) {
		this.proxyCrawler = proxyCrawler;
		if (proxyCrawler != null) {
			this.proxyEntities = proxyCrawler.crawler();
		}
	}

	public ProxyEntity take() {
		if (proxyEntities == null) {
			return null;
		}
		int index = indexer.getAndIncrement();
		indexer.compareAndSet(proxyEntities.size() - 1, 0);
		return proxyEntities.get(index);
	}

	public void recycle(ProxyEntity proxyEntity) {
		if (proxyEntities == null) {
			return;
		}
		proxyEntity.failureTimes++;
		synchronized (ProxyFactory.class) {
			if (proxyEntity.failureTimes >= 3) {
				int pos = -1;
				for (int i = 0; i < proxyEntities.size(); i++) {
					if (proxyEntities.get(i).equal(proxyEntity)) {
						pos = i;
						break;
					}
				}
				if (pos > -1) {
					proxyEntities.remove(pos);
				}
			}
			if (proxyEntities.size() == 0) {
				this.proxyEntities = proxyCrawler.crawler();
			}
		}
	}
}
