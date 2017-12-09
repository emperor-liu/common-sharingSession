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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.commons.lang3.SerializationUtils;

import com.lljqiu.tools.session.cache.Assert;
import com.lljqiu.tools.session.cache.HQCacheException;
import com.lljqiu.tools.session.cache.PersistenceHolder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

/** 
 * ClassName: ShardedRedisClient.java <br>
 * Description: 先分片，然后再建链接池<br>
 * Create by: name：liujie <br>email: jie_liu1@asdc.com.cn <br>
 * Create Time: 2017年4月8日<br>
 */
public class ShardedRedisClient<T extends Serializable> extends ConfiguredRedisClient implements RedisClient<T> {

	private PersistenceHolder                             persistenceHolder;
	public void setPersistenceHolder(PersistenceHolder persistenceHolder) {
        this.persistenceHolder = persistenceHolder;
    }
	
	public PersistenceHolder getPersistenceHolder() {
		return persistenceHolder;
	}

	private static final int VAL_INT_ZERO = 0;

	private JedisPoolSharded sharded;

	public void init() {
		if (!isInitd) {
			checkConfig();
			initPoolSharded();
			isInitd = true;
		}
	}

	private void initPoolSharded() {
		List<JedisPoolShardInfo> jedisPoolList = new ArrayList<>(getServer().length);
		JedisPoolConfig poolConfig = configRedisPool();
		for (String server : getServer()) {
			String[] serverInfo = buildConfig(server);
			JedisPoolShardInfo info = null;
			if (serverInfo.length == 1) {
				info = new JedisPoolShardInfo(serverInfo[0], poolConfig);
			} else if (serverInfo.length == 2) {
				info = new JedisPoolShardInfo(serverInfo[0], Integer.parseInt(serverInfo[1]), poolConfig);
			} else {
				info = new JedisPoolShardInfo(serverInfo[0], Integer.parseInt(serverInfo[1]), Integer.parseInt(serverInfo[2]), poolConfig);
			}
			jedisPoolList.add(info);
		}
		sharded = new JedisPoolSharded(jedisPoolList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
	}

	public void destroy() {
		if (isInitd) {
			for (JedisPool pool : sharded.getAllShards()) {
				pool.destroy();
			}
			isInitd = false;
		}
	}

	// =====================function================

	public void put(String key, T val) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotNull("value is null", val);
//		int expiredTime = 42000;
//		if (persistenceHolder != null && persistenceHolder.getTypeExpiredMap() != null) {
//            expiredTime = persistenceHolder.getTypeExpiredMap().get(val.getClass().getName());
//        }

		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
//			jds.setex(key.getBytes(), expiredTime, SerializationUtils.serialize(val));
			jds.set(key.getBytes(), SerializationUtils.serialize(val));
		} catch (Throwable e) {
			throw new HQCacheException("Put cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}

	}

	public void put(String key, T val, int expiredTime) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotNull("value is null", val);

		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			jds.setex(key.getBytes(), expiredTime, SerializationUtils.serialize(val));
		} catch (Throwable e) {
			throw new HQCacheException("Put cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public Future<Object> asyncGet(String key) throws HQCacheException {
		throw new HQCacheException("Not support.");
	}

	public T get(String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);

		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			byte[] ret = jds.get(key.getBytes());
			if (ret == null) {
				return null;
			}

			return SerializationUtils.<T> deserialize(ret);

		} catch (Throwable e) {
			throw new HQCacheException("get cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public void delete(String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);

		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			jds.del(key);
		} catch (Throwable e) {
			throw new HQCacheException("delete cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public void incr(String key, int by, int expiredTime)
			throws HQCacheException {

		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotEqual("incr value is zero.", by, VAL_INT_ZERO);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			jds.incrBy(key, by);
		} catch (Throwable e) {
			throw new HQCacheException("incr cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}

	}

	public void incr(String key, int expiredTime) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);

		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			jds.incr(key);
			jds.expire(key, expiredTime);
		} catch (Throwable e) {
			throw new HQCacheException("incr cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}

	}

	public void expire(String key, int expiredTime) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);

		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			jds.expire(key, expiredTime);
		} catch (Throwable e) {
			throw new HQCacheException("expire cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	// ========[list]==========>
	public void lpush(String key, T val) throws HQCacheException {

		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotNull("value is null", val);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
//		int expiredTime = 42000;
//		if (persistenceHolder != null && persistenceHolder.getTypeExpiredMap() != null) {
//            expiredTime = persistenceHolder.getTypeExpiredMap().get(val.getClass().getName());
//        }
		try {
			jds.lpush(key.getBytes(), SerializationUtils.serialize(val));

		} catch (Throwable e) {
			throw new HQCacheException("lpush cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public void rpush(String key, T val) throws HQCacheException {

		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotNull("value is null", val);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			jds.rpush(key.getBytes(), SerializationUtils.serialize(val));

		} catch (Throwable e) {
			throw new HQCacheException("rpush cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public T lpop(String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			byte[] ret = jds.lpop(key.getBytes());
			if (ret == null) {
				return null;
			}
			return SerializationUtils.<T> deserialize(ret);
		} catch (Throwable e) {
			throw new HQCacheException("lpop cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public T rpop(String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			byte[] ret = jds.rpop(key.getBytes());
			if (ret == null) {
				return null;
			}
			return SerializationUtils.<T> deserialize(ret);
		} catch (Throwable e) {
			throw new HQCacheException("rpop cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public T rindex(String key, long index) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			byte[] ret = jds.lindex(key.getBytes(), index);
			if (ret == null) {
				return null;
			}
			return SerializationUtils.<T> deserialize(ret);
		} catch (Throwable e) {
			throw new HQCacheException("rpop cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public Long rindex(String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			return jds.llen(key);
		} catch (Throwable e) {
			throw new HQCacheException("rpop cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	// =======[map]=============>>>>
	public void hset(String key, String field, T v) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotBlank("field is blank.", field);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			jds.hset(key.getBytes(), field.getBytes(), SerializationUtils.serialize(v));
		} catch (Throwable e) {
			throw new HQCacheException("hset cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public T hget(String key, String field) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotBlank("field is blank.", field);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			byte[] ret = jds.hget(key.getBytes(), field.getBytes());
			if (ret == null) {
				return null;
			}
			return SerializationUtils.<T> deserialize(ret);
		} catch (Throwable e) {
			throw new HQCacheException("hget cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public void hdel(String key, String field) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotBlank("field is blank.", field);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			jds.hdel(key.getBytes(), field.getBytes());
		} catch (Throwable e) {
			throw new HQCacheException("hget cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public Long hlen(String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			return jds.hlen(key.getBytes());
		} catch (Throwable e) {
			throw new HQCacheException("hget cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public Set<String> hkeys(String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			Set<byte[]> keys = jds.hkeys(key.getBytes());
			Set<String> results = new HashSet<String>();
			if (keys != null) {
				for (byte[] k : keys) {
					results.add(new String(k));
				}
			}
			return results;
		} catch (Throwable e) {
			throw new HQCacheException("hget cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	// ======[set]========>>>
	public void sAdd(String key, T val) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		Assert.isNotNull("value is null.", val);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			jds.sadd(key.getBytes(), SerializationUtils.serialize(val));
		} catch (Throwable e) {
			throw new HQCacheException("sAdd cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

	public T sPop(String key) throws HQCacheException {
		// 1. 检查参数
		Assert.isNotBlank("key is blank.", key);
		JedisPool pool = sharded.getShard(key);
		Jedis jds = pool.getResource();
		try {
			byte[] ret = jds.spop(key.getBytes());
			if (ret == null) {
				return null;
			}
			return SerializationUtils.<T> deserialize(ret);
		} catch (Throwable e) {
			throw new HQCacheException("sAdd cache exception.", e);
		} finally {
			if (jds != null) {
				jds.close();
			}
		}
	}

    /* (non-Javadoc)
     * @see com.asdc.vss.session.cache.redis.RedisClient#getKeys(java.lang.String)
     */
    @Override
    public Set<String> getKeys(String key) throws HQCacheException {
        // 1. 检查参数
        Assert.isNotBlank("key is blank.", key);
        JedisPool pool = sharded.getShard(key);
        Jedis jds = pool.getResource();
        try {
            Set<byte[]> keys = jds.keys(key.getBytes());
            Set<String> results = new HashSet<String>();
            if (keys != null) {
                for (byte[] k : keys) {
                    results.add(new String(k));
                }
            }
            return results;
        } catch (Throwable e) {
            throw new HQCacheException("hget cache exception.", e);
        } finally {
            if (jds != null) {
                jds.close();
            }
        }
    }

}
