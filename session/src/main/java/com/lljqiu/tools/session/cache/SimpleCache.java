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
import java.util.concurrent.Future;

/** 
 * ClassName: SimpleCache.java <br>
 * Description: 缓存服务基础服务接口<br>
 * Create by: name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年4月8日<br>
 */
public interface SimpleCache<T extends Serializable> {

	/** 
	 * Description：添加缓存项到缓存服务器，缓存项超时时间为默认值（配置文件中超时项值）
	 * @param key 缓存键
	 * @param val 缓存值
	 * @throws HQCacheException
	 * @return void
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 **/
	public void put(String key, T val) throws HQCacheException;

	/** 
	 * Description：添加缓存项到缓存服务器
	 * @param key 缓存键
	 * @param val 缓存值
	 * @param expiredTime 超时时间，单位秒
	 * @throws HQCacheException
	 * @return void
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 **/
	public void put(String key, T val, int expiredTime) throws HQCacheException;

	/** 
	 * Description：异步获取缓存项值
	 * @param key 缓存键
	 * @throws HQCacheException
	 * @return Future<Object>
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 **/
	public Future<Object> asyncGet(String key) throws HQCacheException;

	/** 
	 * Description：获取缓存项
     * @param key 缓存键
	 * @throws HQCacheException
	 * @return T
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 **/
	public T get(String key) throws HQCacheException;

	/** 
	 * Description： 删除
	 * @param key
	 * @throws HQCacheException
	 * @return void
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 **/
	public void delete(String key) throws HQCacheException;

	/** 
	 * Description：
	 * @param key
	 * @param by
	 * @param expiredTime
	 * @throws HQCacheException
	 * @return void
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 **/
	public void incr(String key, int by, int expiredTime)
			throws HQCacheException;

	/** 
	 * Description：
	 * @param key
	 * @param expiredTime
	 * @throws HQCacheException
	 * @return void
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 **/
	public void incr(String key, int expiredTime) throws HQCacheException;
}
