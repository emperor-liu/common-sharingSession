package com.lljqiu.cache.redis;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.commons.lang3.SerializationUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

import com.lljqiu.cache.Assert;
import com.lljqiu.cache.HQCacheException;
import com.lljqiu.cache.redis.BasicJedisCluster.JedisCommand;

/**
 * 支持bytes操作
 * 
 * @author lljqiu
 * @date 2015年4月14日
 */
public class ClusterRedisClient<T extends Serializable> extends ConfiguredRedisClient implements RedisClient<T> {

	private static final int VAL_INT_ZERO = 0;

	private BasicJedisCluster jedisCluster;
	
	private int redirections = 0;
	
	public void setRedirections(int redirections) {
		this.redirections = redirections;
	}

	public void init() {
		if (!isInitd) {
			checkConfig();
			initCluster();
			isInitd = true;
		}
	}

	private void initCluster() {
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		for (String server : getServer()) {
			String[] serverInfo = buildConfig(server);
			HostAndPort node = null;
			if (serverInfo.length == 1) {
				node = new HostAndPort(serverInfo[0], Protocol.DEFAULT_PORT);
			} else {
				node = new HostAndPort(serverInfo[0], Integer.parseInt(serverInfo[1]));
			}
			nodes.add(node);
		}
		jedisCluster = new BasicJedisCluster(nodes, configRedisPool(), Protocol.DEFAULT_TIMEOUT, redirections);
	}

	public void destroy() {
		if (isInitd) {
			jedisCluster.close();
			isInitd = false;
		}
	}

	// =====================function================

	public void put(final String key, final T val) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotNull("value is null", val);
		jedisCluster.execute(key, new JedisCommand<Object>() {
			public Object run(Jedis jds) {
				jds.set(key.getBytes(), SerializationUtils.serialize(val));
				return null;
			}
		});
	}

	public void put(final String key, final T val, final int expiredTime) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotNull("value is null", val);
		jedisCluster.execute(key, new JedisCommand<Object>() {
			public Object run(Jedis jds) {
				jds.setex(key.getBytes(), expiredTime, SerializationUtils.serialize(val));
				return null;
			}
		});
	}

	public Future<Object> asyncGet(String key) throws HQCacheException {
		throw new HQCacheException("Not support.");
	}

	public T get(final String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		return jedisCluster.execute(key, new JedisCommand<T>() {
			public T run(Jedis jedis) {
				byte[] ret = jedis.get(key.getBytes());
				if (ret == null) {
					return null;
				}
				return SerializationUtils.<T> deserialize(ret);
			}});
	}

	public void delete(final String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		jedisCluster.execute(key, new JedisCommand<Object>() {
			public Object run(Jedis jedis) {
				jedis.del(key);
				return null;
			}});
	}

	public void incr(final String key, final int by, int expiredTime) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotEqual("incr value is zero.", by, VAL_INT_ZERO);
		jedisCluster.execute(key, new JedisCommand<Object>() {
			public Object run(Jedis jedis) {
				jedis.incrBy(key, by);
				return null;
			}});
	}

	public void incr(final String key, final int expiredTime) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		jedisCluster.execute(key, new JedisCommand<Object>() {
			public Object run(Jedis jedis) {
				jedis.incr(key);
				jedis.expire(key, expiredTime);
				return null;
			}});
	}

	public void expire(final String key, final int expiredTime) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		jedisCluster.execute(key, new JedisCommand<Object>() {
			public Object run(Jedis jedis) {
				jedis.expire(key, expiredTime);
				return null;
			}});
	}

	// ========[list]==========>
	public void lpush(final String key, final T val) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotNull("value is null", val);
		jedisCluster.execute(key, new JedisCommand<Object>() {
			public Object run(Jedis jedis) {
				jedis.lpush(key.getBytes(), SerializationUtils.serialize(val));
				return null;
			}});
	}

	public void rpush(final String key, final T val) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotNull("value is null", val);
		jedisCluster.execute(key, new JedisCommand<Object>() {
			public Object run(Jedis jedis) {
				jedis.rpush(key.getBytes(), SerializationUtils.serialize(val));
				return null;
			}});
	}

	public T lpop(final String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		return jedisCluster.execute(key, new JedisCommand<T>() {
			public T run(Jedis jedis) {
				byte[] ret = jedis.lpop(key.getBytes());
				if (ret == null) {
					return null;
				}
				return SerializationUtils.<T> deserialize(ret);
			}});
	}

	public T rpop(final String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		return jedisCluster.execute(key, new JedisCommand<T>() {
			public T run(Jedis jedis) {
				byte[] ret = jedis.rpop(key.getBytes());
				if (ret == null) {
					return null;
				}
				return SerializationUtils.<T> deserialize(ret);
			}});
	}

	public T rindex(final String key, final long index) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		return jedisCluster.execute(key, new JedisCommand<T>() {
			public T run(Jedis jedis) {
				byte[] ret = jedis.lindex(key.getBytes(), index);
				if (ret == null) {
					return null;
				}
				return SerializationUtils.<T> deserialize(ret);
			}});
	}

	public Long rindex(final String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		return jedisCluster.execute(key, new JedisCommand<Long>() {
			public Long run(Jedis jedis) {
				return jedis.llen(key);
			}});
	}

	// =======[map]=============>>>>
	public void hset(final String key, final String field, final T v) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotBlank("field is blank.", field);
		jedisCluster.execute(key, new JedisCommand<Object>() {
			public Object run(Jedis jedis) {
				jedis.hset(key.getBytes(), field.getBytes(), SerializationUtils.serialize(v));
				return null;
			}});
	}

	public T hget(final String key, final String field) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotBlank("field is blank.", field);
		return jedisCluster.execute(key, new JedisCommand<T>() {
			public T run(Jedis jedis) {
				byte[] ret = jedis.hget(key.getBytes(), field.getBytes());
				if (ret == null) {
					return null;
				}
				return SerializationUtils.<T> deserialize(ret);
			}});
	}

	public void hdel(final String key, final String field) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotBlank("field is blank.", field);
		jedisCluster.execute(key, new JedisCommand<Object>() {
			public Object run(Jedis jedis) {
				jedis.hdel(key.getBytes(), field.getBytes());
				return null;
			}});
	}

	public Long hlen(final String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		return jedisCluster.execute(key, new JedisCommand<Long>() {
			public Long run(Jedis jedis) {
				return jedis.hlen(key.getBytes());
			}});
	}

	public Set<String> hkeys(final String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		return jedisCluster.execute(key, new JedisCommand<Set<String>>() {
			public Set<String> run(Jedis jedis) {
				Set<byte[]> keys = jedis.hkeys(key.getBytes());
				Set<String> results = new HashSet<String>();
				if (keys != null) {
					for (byte[] k : keys) {
						results.add(new String(k));
					}
				}
				return results;
			}});
	}

	// ======[set]========>>>
	public void sAdd(final String key, final T val) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotNull("value is null.", val);
		jedisCluster.execute(key, new JedisCommand<Object>() {
			public Object run(Jedis jedis) {
				jedis.sadd(key.getBytes(), SerializationUtils.serialize(val));
				return null;
			}});
	}

	public T sPop(final String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		return jedisCluster.execute(key, new JedisCommand<T>() {
			public T run(Jedis jedis) {
				byte[] ret = jedis.spop(key.getBytes());
				if (ret == null) {
					return null;
				}
				return SerializationUtils.<T> deserialize(ret);
			}});
	}

}
