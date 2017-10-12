package org.yinyayun.ai.utils;

import java.io.IOException;
import java.util.List;

import org.yinyayun.ai.utils.proxy.ProxyCrawler;
import org.yinyayun.ai.utils.proxy.ProxyEntity;

public class ProxyTest {
	public static void main(String[] args) throws IOException {
		List<ProxyEntity> entitys = new ProxyCrawler().crawler();
		System.out.println(entitys);
	}
}
