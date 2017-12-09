/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.cache
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.cache;

import java.io.Serializable;

/** 
 * ClassName: MemcachedClient.java <br>
 * Description: memcached缓存客户端<br>
 * Create by: name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年4月8日<br>
 */
public interface MemcachedClient<T extends Serializable> extends
		CacheClient, SimpleCache<T> {
}
