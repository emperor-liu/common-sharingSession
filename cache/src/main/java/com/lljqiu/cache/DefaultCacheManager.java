package com.lljqiu.cache;

import java.io.Serializable;
import java.util.concurrent.Future;

import com.lljqiu.cache.memcached.MemcachedClient;
import com.lljqiu.cache.redis.RedisClient;

/**
 * 
 * <pre>
 * <p>文件名称: DefaultCacheManager.java</p>
 * 
 * <p>文件功能: 缓存集中管理器</p>
 * 
 * <p>编程者: lljqiu</p>
 * 
 * <p>初作时间: 2015年4月10日 下午5:10:04</p>
 * 
 * <p>版本: version 1.0 </p>
 * 
 * <p>输入说明: </p>
 * 
 * <p>输出说明: </p>
 * 
 * <p>程序流程: </p>
 * 
 * <p>============================================</p>
 * <p>修改序号:</p>
 * <p>时间:	 </p>
 * <p>修改者:  </p>
 * <p>修改内容:  </p>
 * <p>============================================</p>
 * </pre>
 */
public class DefaultCacheManager<T extends Serializable> extends
		AbstractCacheClient implements MemcachedClient<T>, RedisClient<T> {

	private MemcachedClient<T> memcacheClient;
	private RedisClient<T> redisClient;

	private String memCacheServer;
	private String redisServer;

	public void init() {

		if (!isInitd) {
			if (memcacheClient == null) {
				Assert.isNotBlank("memcache server config is blank.",
						memCacheServer);
				memcacheClient.setServers(memCacheServer);
			}

			if (redisClient == null) {
				Assert.isNotBlank("redis server config is blank.", redisServer);

				memcacheClient.setServers(redisServer);
			}

			memcacheClient.init();
			redisClient.init();
			isInitd = true;
		}
	}

	public void destroy() {
		if (isInitd) {
			Assert.isArrayNotNull("cache Client is null.", memcacheClient,
					redisClient);
			redisClient.destroy();
			memcacheClient.destroy();
			isInitd = false;
		}

	}

	public void put(String key, T val) throws HQCacheException {
		memcacheClient.put(key, val);
	}

	public void put(String key, T val, int expiredTime) throws HQCacheException {
		memcacheClient.put(key, val, expiredTime);
	}

	public Future<Object> asyncGet(String key) throws HQCacheException {
		return memcacheClient.asyncGet(key);
	}

	public T get(String key) throws HQCacheException {
		return memcacheClient.get(key);
	}

	public void delete(String key) throws HQCacheException {
		memcacheClient.delete(key);
	}

	public void incr(String key, int by, int expiredTime)
			throws HQCacheException {
		memcacheClient.incr(key, by, expiredTime);

	}

	public void incr(String key, int expiredTime) throws HQCacheException {
		memcacheClient.incr(key, expiredTime);
	}

	public void expire(String key, int expiredTime) throws HQCacheException {
		redisClient.expire(key, expiredTime);
	}

	public void lpush(String key, T val) throws HQCacheException {
		redisClient.lpush(key, val);
	}

	public void rpush(String key, T val) throws HQCacheException {
		redisClient.rpush(key, val);
	}

	public T lpop(String key) throws HQCacheException {
		return redisClient.lpop(key);
	}

	public T rpop(String key) throws HQCacheException {
		return redisClient.rpop(key);
	}

	public T rindex(String key, long index) throws HQCacheException {
		return redisClient.rindex(key, index);
	}

	public Long rindex(String key) throws HQCacheException {
		return redisClient.rindex(key);
	}

	public void hset(String key, String field, T v) throws HQCacheException {
		redisClient.hset(key, field, v);
	}

	public T hget(String key, String field) throws HQCacheException {
		return redisClient.hget(key, field);
	}

	public void hdel(String key, String field) throws HQCacheException {
		redisClient.hdel(key, field);
	}

	public Long hlen(String key) throws HQCacheException {
		return redisClient.hlen(key);
	}

	public void sAdd(String key, T val) throws HQCacheException {
		redisClient.sAdd(key, val);
	}

	public T sPop(String key) throws HQCacheException {
		return redisClient.sPop(key);
	}

	public void setMemcacheClient(MemcachedClient<T> memcacheClient) {
		this.memcacheClient = memcacheClient;
	}

	public void setRedisClient(RedisClient<T> redisClient) {
		this.redisClient = redisClient;
	}

	public void setMemCacheServer(String memCacheServer) {
		this.memCacheServer = memCacheServer;
	}

	public void setRedisServer(String redisServer) {
		this.redisServer = redisServer;
	}

}
