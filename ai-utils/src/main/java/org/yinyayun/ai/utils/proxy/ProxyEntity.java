package org.yinyayun.ai.utils.proxy;

public class ProxyEntity {
	public String ip;
	public int port;
	public String user;
	public String passwd;
	public int failureTimes = 0;

	public ProxyEntity(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public ProxyEntity(String ip, int port, String user, String passwd) {
		this.ip = ip;
		this.port = port;
		this.user = user;
		this.passwd = passwd;
	}

	@Override
	public String toString() {
		return String.join(":", ip, String.valueOf(port));
	}

	public boolean equal(ProxyEntity proxyEntity) {
		if (proxyEntity.ip.equals(this.ip) && proxyEntity.port == this.port) {
			return true;
		}
		return false;
	}
}
