/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.cache
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.cache;

import java.util.Map;

/** 
 * ClassName: CacheClient.java <br>
 * Description: 缓存基础客户端<br>
 * Create by: name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年4月8日<br>
 */
public interface CacheClient {

	/**
	 * Description： 初始化客户端
	 * @return void
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 */
	public void init();

	/**
	 * Description： 销毁客户端
	 * @return void
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 */
	public void destroy();

	/**
	 * Description：设置配置信息
	 * @param config
	 * @return void
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 */
	public void setConfigs(Map<String, String> config);

	/**
	 * Description：设置服务器地址列表，格式为: http://ip:port/ , 多个地址以逗号分割
	 * @param servers
	 * @return void
	 * @author name：liujie <br>email: liujie@lljqiu.com
	 */
	public void setServers(String servers);
}
