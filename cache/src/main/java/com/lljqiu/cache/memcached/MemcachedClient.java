package com.lljqiu.cache.memcached;

import java.io.Serializable;

import com.lljqiu.cache.CacheClientConfig;
import com.lljqiu.cache.SimpleCache;

/** 
 * <p>文件名称: MemcacheClient.java</p>
 * 
 * <p>文件功能: 缓存操作</p>
 *
 * <p>编程者: lljqiu</p>
 * 
 * <p>初作时间: 2015年2月10日 下午4:28:19</p>
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
 */
public interface MemcachedClient<T extends Serializable> extends
CacheClientConfig, SimpleCache<T> {

}
