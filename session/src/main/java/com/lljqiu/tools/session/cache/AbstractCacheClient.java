/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.cache
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.cache;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.spy.memcached.AddrUtil;

/** 
 * ClassName: AbstractCacheClient.java <br>
 * Description: 缓存基类<br>
 * Create by: name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年4月8日<br>
 */
public abstract class AbstractCacheClient implements CacheClient {
	private static Logger log = LoggerFactory
			.getLogger(AbstractCacheClient.class);
	protected Map<String, String> config;
	protected String[] servers;
	protected volatile boolean isInitd = false;

	public Map<String, String> getConfigs() {
		return config;
	}

	public void setConfigs(Map<String, String> config) {
		this.config = config;
	}

	public void setServers(String server) {
		if (StringUtils.isNotBlank(server)) {
			server = server.replace(";", ",");
			servers = server.split(",");
		}
	}

	/** 
	 * Description：获取配置的服务器数组
	 * @return
	 * @return String[]
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 **/
	public String[] getServer() {
		return servers;
	}

	/** 
	 * Description：获取配置的服务器地址列表
	 * @return
	 * @return List<InetSocketAddress>
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 **/
	public List<InetSocketAddress> getAddressWithString() {
		if (servers == null || servers.length == 0) {
			return null;
		}
		return AddrUtil.getAddresses(Arrays.asList(servers));
	}

	@SuppressWarnings("unchecked")
	public <C> C getConfigVal(String key, C defValue) {
		String ret = this.getStrConfigVal(key, null);
		if (ret == null) {
			return defValue;
		}
		if (defValue.getClass().isAssignableFrom(Integer.class)) {
			try {
				return (C) Integer.valueOf(ret);
			} catch (NumberFormatException e) {
				log.error("Format config exception.", e);
				return defValue;
			}
		} else if (defValue.getClass().isAssignableFrom(Long.class)) {
			try {
				return (C) Long.valueOf(ret);
			} catch (NumberFormatException e) {
				log.error("Format config exception.", e);
				return defValue;
			}
		}
		return defValue;
	}
	
	public boolean existsConfig(String key) {
		return config != null && config.containsKey(key);
	}

	public String getStrConfigVal(String key, String defValue) {
		String v = existsConfig(key) ? config.get(key) : null;
		return StringUtils.isNotBlank(v) ? v.trim() : defValue;
	}
	
}
