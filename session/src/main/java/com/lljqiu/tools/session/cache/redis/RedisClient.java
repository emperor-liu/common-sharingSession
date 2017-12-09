/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.cache.redis
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.cache.redis;

import java.io.Serializable;
import java.util.Set;

import com.lljqiu.tools.session.cache.CacheClient;
import com.lljqiu.tools.session.cache.HQCacheException;
import com.lljqiu.tools.session.cache.SimpleCache;

/** 
 * ClassName: RedisClient.java <br>
 * Description: redis 客户端封装<br>
 * Create by: name：liujie <br>email: jie_liu1@asdc.com.cn <br>
 * Create Time: 2017年4月8日<br>
 */
public interface RedisClient<T extends Serializable> extends CacheClient,
		SimpleCache<T> {

	/**
	 * 
	 * <pre>
	 * Description：缓存过期
	 * @param key 过期key
	 * @param expiredTime 单位秒
	 * @throws HQCacheException
	 * @return void
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn
	 */
	public void expire(String key, int expiredTime) throws HQCacheException;

	// ========[list]==========>
	/**
	 * 
	 * <pre>
	 * Description：添加一个项到列表左边
	 * @param key
	 * @param val
	 * @throws HQCacheException
	 * @return void
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public void lpush(String key, T val) throws HQCacheException;

	/**
	 * 
	 * <pre>
	 * Description：添加一个项到列表右边
	 * @param key
	 * @param val
	 * @throws HQCacheException
	 * @return void
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public void rpush(String key, T val) throws HQCacheException;

	/**
	 * 
	 * <pre>
	 * Description：从列表左边获取并删除一个列表项
	 * @param key
	 * @return
	 * @throws HQCacheException
	 * @return T
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public T lpop(String key) throws HQCacheException;

	/**
	 * 
	 * <pre>
	 * Description：从列表右边获取并删除一个列表项
	 * @param key
	 * @return
	 * @throws HQCacheException
	 * @return T
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public T rpop(String key) throws HQCacheException;

	/**
	 * 
	 * <pre>
	 * Description：获取一个元素，通过其索引列表
	 * @param key
	 * @param index
	 * @return
	 * @throws HQCacheException
	 * @return T
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public T rindex(String key, long index) throws HQCacheException;

	/**
	 * 
	 * <pre>
	 * Description：获取列表长度
	 * @param key
	 * @return
	 * @throws HQCacheException
	 * @return Long
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public Long rindex(String key) throws HQCacheException;

	// =======[map]=============>>>>
	/**
	 * 
	 * <pre>
	 * Description：设置hash里面一个字段的值
	 * @param key
	 * @param field
	 * @param v
	 * @throws HQCacheException
	 * @return void
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public void hset(String key, String field, T v) throws HQCacheException;

	/**
	 * 
	 * <pre>
	 * Description：读取哈希域的的值
	 * @param key
	 * @param field
	 * @return
	 * @throws HQCacheException
	 * @return T
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public T hget(String key, String field) throws HQCacheException;

	/**
	 * 
	 * <pre>
	 * Description：删除map中key的所有项
	 * @param key
	 * @param field
	 * @throws HQCacheException
	 * @return void
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public void hdel(String key, String field) throws HQCacheException;

	/**
	 * 
	 * <pre>
	 * Description：获取hash里所有字段的数量
	 * @param key
	 * @return
	 * @throws HQCacheException
	 * @return Long
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public Long hlen(String key) throws HQCacheException;
	
	public Set<String> hkeys(String key) throws HQCacheException;
	
	/** 
	 * Description：模糊查询所有 key
	 * @param key
	 * @return
	 * @throws HQCacheException
	 * @return Set<String>
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn
	 **/
	public Set<String> getKeys(String key) throws HQCacheException;

	// ======[set]========>>>
	/**
	 * <pre>
	 * Description：添加一个或者多个元素到集合(set)里
	 * @param key
	 * @param val
	 * @throws HQCacheException
	 * @return void
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public void sAdd(String key, T val) throws HQCacheException;

	/**
	 * 
	 * <pre>
	 * Description：删除并获取一个集合里面的元素
	 * @param key
	 * @return
	 * @throws HQCacheException
	 * @return T
	 * @author name：liujie <br>email: jie_liu1@asdc.com.cn 
	 */
	public T sPop(String key) throws HQCacheException;
}
