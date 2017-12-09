/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.cache.memcached
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.cache.memcached;

import java.io.Serializable;

import com.lljqiu.tools.session.cache.CacheClient;
import com.lljqiu.tools.session.cache.SimpleCache;

/**
 * memcached缓存客户端
 * 
 * @author chenke
 * @date 2014-6-30 下午1:25:40
 */
public interface MemcachedClient<T extends Serializable> extends
		CacheClient, SimpleCache<T> {
}
