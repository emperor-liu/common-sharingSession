package com.lljqiu.cache.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.util.ShardInfo;
import redis.clients.util.Sharded;

/**
 * 分片的信息是链接池
 * 
 * @author lljqiu
 * @date 2015年4月14日
 */
public class JedisPoolShardInfo extends ShardInfo<JedisPool> {
	
	private String host;
	
	private int port;
	
	private GenericObjectPoolConfig poolConfig;
	
	public JedisPoolShardInfo(String host) {
		this(host, null);
	}

	public JedisPoolShardInfo(String host, GenericObjectPoolConfig poolConfig) {
		this(host, Protocol.DEFAULT_PORT, poolConfig);
	}
	
	public JedisPoolShardInfo(String host, int port) {
		this(host, port, null);
	}
	
	public JedisPoolShardInfo(String host, int port, GenericObjectPoolConfig poolConfig) {
		this(host, port, Sharded.DEFAULT_WEIGHT, poolConfig);
	}
	
	public JedisPoolShardInfo(String host, int port, int weight, GenericObjectPoolConfig poolConfig) {
		super(weight);
		this.host = host;
		this.port = port;
		this.poolConfig = poolConfig != null ? poolConfig : new JedisPoolConfig();
	}

	public String getName() {
		return (host + port);
	}

	public JedisPool createResource() {
		return new JedisPool(poolConfig, host, port);
	}
	
	public String toString() {
		return host + ":" + port + "*" + getWeight();
	}

}
