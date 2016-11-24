package com.lljqiu.cache.redis;

import java.io.Serializable;

import com.lljqiu.cache.CacheClientConfig;
import com.lljqiu.cache.HQCacheException;
import com.lljqiu.cache.SimpleCache;

/**
 * 
 * <pre>
 * <p>文件名称: RedisClient.java</p>
 * 
 * <p>文件功能: redis 客户端封装</p>
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
public interface RedisClient<T extends Serializable> extends CacheClientConfig,
		SimpleCache<T> {

	/**
	 * 
	 * <pre>
	 * Description：缓存过期
	 * @param key 过期key
	 * @param expiredTime 单位秒
	 * @throws HQCacheException
	 * @return void
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
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
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
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
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
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
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
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
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
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
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
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
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
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
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
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
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
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
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
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
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
	 */
	public Long hlen(String key) throws HQCacheException;

	// ======[set]========>>>
	/**
	 * 
	 * <pre>
	 * Description：添加一个或者多个元素到集合(set)里
	 * @param key
	 * @param val
	 * @throws HQCacheException
	 * @return void
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
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
	 * @author name：lljqiu
	 * <p>============================================</p>
	 * Modified No： 
	 * Modified By： 
	 * Modified Date： 
	 * Modified Description: 
	 * <p>============================================</p>
	 * </pre>
	 *
	 */
	public T sPop(String key) throws HQCacheException;
}
