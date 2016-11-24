package com.lljqiu.cache.redis;

import java.io.Closeable;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;
import redis.clients.jedis.exceptions.JedisAskDataException;
import redis.clients.jedis.exceptions.JedisClusterException;
import redis.clients.jedis.exceptions.JedisClusterMaxRedirectionsException;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisMovedDataException;
import redis.clients.jedis.exceptions.JedisRedirectionException;
import redis.clients.util.JedisClusterCRC16;

public class BasicJedisCluster implements Closeable {
	
	private static final int DEFAULT_TIMEOUT = 2000;

	private JedisSlotBasedConnectionHandler connectionHandler;

	private int redirections = 0;
	
	private ThreadLocal<Jedis> askJedis = new ThreadLocal<Jedis>();

	public BasicJedisCluster(Set<HostAndPort> nodes) {
		this(nodes, DEFAULT_TIMEOUT);
	}

	public BasicJedisCluster(Set<HostAndPort> nodes, int timeout) {
		this(nodes, new GenericObjectPoolConfig(), timeout, 0);
	}

	public BasicJedisCluster(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig) {
		this(nodes, poolConfig, 0);
	}
	
	public BasicJedisCluster(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig, int redirections) {
		this(nodes, poolConfig, DEFAULT_TIMEOUT, redirections);
	}

	public BasicJedisCluster(Set<HostAndPort> nodes, final GenericObjectPoolConfig poolConfig, int timeout, int redirections) {
		this.connectionHandler = new JedisSlotBasedConnectionHandler(nodes, poolConfig, timeout);
		this.redirections = redirections;
	}

	public void close() {
		if (connectionHandler != null) {
			for (JedisPool pool : connectionHandler.getNodes().values()) {
				try {
					if (pool != null) {
						pool.destroy();
					}
				} catch (Exception ignore) {
					// pass
				}
			}
		}
	}

	public <T> T execute(String key, JedisCommand<T> command) {
	    if (key == null) {
	    	throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
	    }
	    return runWithRetries(key, command, this.redirections, false, false);
	}
	
	private <T> T runWithRetries(String key, JedisCommand<T> command, int redirections, boolean tryRandomNode, boolean asking) {
		if (redirections < 0) {
			throw new JedisClusterMaxRedirectionsException("Too many Cluster redirections?");
		}

		Jedis jedis = null;
		try {
			if (asking) {
				jedis = askJedis.get();
				jedis.asking();
				asking = false;
			} else {
				if (tryRandomNode) {
					jedis = connectionHandler.getConnection();
				} else {
					jedis = connectionHandler.getConnectionFromSlot(JedisClusterCRC16.getSlot(key));
				}
			}
			return command.run(jedis);
		} catch (JedisConnectionException jce) {
			if (tryRandomNode) {
				throw jce;
			}
			releaseConnection(jedis, true);
			jedis = null;
			return runWithRetries(key, command, redirections - 1, true, asking);
		} catch (JedisRedirectionException jre) {
			if (jre instanceof JedisAskDataException) {
				asking = true;
				askJedis.set(this.connectionHandler.getConnectionFromNode(jre.getTargetNode()));
			} else if (jre instanceof JedisMovedDataException) {
				this.connectionHandler.renewSlotCache();
			} else {
				throw new JedisClusterException(jre);
			}
			releaseConnection(jedis, false);
			jedis = null;
			return runWithRetries(key, command, redirections - 1, false, asking);
		} finally {
			releaseConnection(jedis, false);
		}
	}

	private void releaseConnection(Jedis connection, boolean broken) {
		if (connection != null) {
			if (broken) {
				connectionHandler.returnBrokenConnection(connection);
			} else {
				connectionHandler.returnConnection(connection);
			}
		}
	}
	
	public static interface JedisCommand<T> {
		
		T run(Jedis jedis);
		
	}
	
}
