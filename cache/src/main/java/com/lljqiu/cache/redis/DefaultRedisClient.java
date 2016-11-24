package com.lljqiu.cache.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.commons.lang3.SerializationUtils;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import com.lljqiu.cache.Assert;
import com.lljqiu.cache.HQCacheException;

/**
 * <pre>
 * <p>文件名称: DefaultRedisClient.java</p>
 * 
 * <p>文件功能: redis 封装</p>
 * 
 * <p>编程者: lljqiu</p>
 * 
 * <p>初作时间: 2014年7月8日 下午5:39:32</p>
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
public class DefaultRedisClient<T extends Serializable> extends ConfiguredRedisClient implements
        RedisClient<T> {

    private static final int    VAL_INT_ZERO        = 0;

    private ShardedJedisPool    pool;

    public void init() {
        if (!isInitd) {
            // 配置参数检查
            checkConfig();
            // 初始化缓存池
            initPool();
            isInitd = true;
        }
    }

    private void initPool() {
        List<JedisShardInfo> jdsInfoList = new ArrayList<JedisShardInfo>(getServer().length);

        for (String server : getServer()) {
            //
            String[] serverInfo = buildConfig(server);

            JedisShardInfo infoA = new JedisShardInfo(serverInfo[0], Integer.parseInt(serverInfo[1]), server);
            jdsInfoList.add(infoA);
        }

        pool = new ShardedJedisPool(configRedisPool(), jdsInfoList, Hashing.MURMUR_HASH,
                Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    public void destroy() {
        if (isInitd) {
            pool.destroy();
            isInitd = false;
        }
    }

    // =====================function================

    public void put(String key, T val) throws HQCacheException {
        // 1. 检查参数
        Assert.isNotBlank("key is blank.", key);
        Assert.isNotNull("value is null", val);

        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key).set(key.getBytes(), SerializationUtils.serialize(val));
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

        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key).setex(key.getBytes(), expiredTime, SerializationUtils.serialize(val));
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

        ShardedJedis jds = pool.getResource();
        try {
            byte[] ret = jds.getShard(key).get(key.getBytes());
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

        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key).del(key);

        } catch (Throwable e) {
            throw new HQCacheException("delete cache exception.", e);
        } finally {
        	if (jds != null) {
        		jds.close();
        	}
        }
    }

    public void incr(String key, int by, int expiredTime) throws HQCacheException {

        // 1. 检查参数
        Assert.isNotBlank("key is blank.", key);
        Assert.isNotEqual("incr value is zero.", by, VAL_INT_ZERO);
        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key).incrBy(key, by);

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

        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key).incr(key);
            jds.getShard(key).expire(key, expiredTime);

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

        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key).expire(key, expiredTime);

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
        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key).lpush(key.getBytes(), SerializationUtils.serialize(val));

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
        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key).rpush(key.getBytes(), SerializationUtils.serialize(val));

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

        ShardedJedis jds = pool.getResource();
        try {
            byte[] ret = jds.getShard(key).lpop(key.getBytes());

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

        ShardedJedis jds = pool.getResource();
        try {
            byte[] ret = jds.getShard(key).rpop(key.getBytes());

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

        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key);
            byte[] ret = jds.getShard(key).lindex(key.getBytes(), index);

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

        ShardedJedis jds = pool.getResource();
        try {
        	return jds.getShard(key).llen(key);
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

        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key).hset(key.getBytes(), field.getBytes(), SerializationUtils.serialize(v));

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

        ShardedJedis jds = pool.getResource();
        try {
            byte[] ret = jds.getShard(key).hget(key.getBytes(), field.getBytes());

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

        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key).hdel(key.getBytes(), field.getBytes());
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

        ShardedJedis jds = pool.getResource();
        try {
        	return jds.getShard(key).hlen(key.getBytes());
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

        ShardedJedis jds = pool.getResource();
        try {
        	Set<byte[]> keys = jds.getShard(key).hkeys(key.getBytes());
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

        ShardedJedis jds = pool.getResource();
        try {
        	jds.getShard(key).sadd(key.getBytes(), SerializationUtils.serialize(val));

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

        ShardedJedis jds = pool.getResource();
        try {
            byte[] ret = jds.getShard(key).spop(key.getBytes());
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

}
