package com.lljqiu.cache;

import java.util.Map;

/** 
 * <p>文件名称: CacheClientConfig.java</p>
 * 
 * <p>文件功能: 缓存配置，</p>
 *
 * <p>编程者: lljqiu</p>
 * 
 * <p>初作时间: 2015年2月10日 下午4:41:44</p>
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
public interface CacheClientConfig {
    /**
     * <pre>
     * Description： 初始化客户端
     * @return void
     * @author name：lljqiu
     * <p>============================================</p>
     * Modified No： 
     * Modified By： 
     * Modified Date： 
     * Modified Description: 
     * <p>============================================</p>
     * </pre>
     */
    public void init();

    /**
     * 
     * <pre>
     * Description： 销毁客户端
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
    public void destroy();

    /**
     * <pre>
     * Description：设置配置信息
     * @param config
     * @return void
     * @author name：lljqiu
     * <p>============================================</p>
     * Modified No： 
     * Modified By： 
     * Modified Date： 
     * Modified Description: 
     * <p>============================================</p>
     * </pre>
     */
    public void setConfigs(Map<String, String> config);

    /**
     * 
     * <pre>
     * Description：设置服务器地址列表，格式为: http://ip:port/ , 多个地址以逗号分割
     * @param servers
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
    public void setServers(String servers);
}
