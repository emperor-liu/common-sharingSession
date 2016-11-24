package com.lljqiu.session;


/** 
 * <p>文件名称: SessionStore.java</p>
 * 
 * <p>文件功能: session存储接口，不同的实现可以将session数据持久化到任意地方</p>
 *
 * <p>编程者: lljqiu</p>
 * 
 * <p>初作时间: 2015年6月8日 下午9:31:12</p>
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
public interface SessionStore {
    
    /**
     * 新建session存储空间
     * 
     * @param sessionId
     * @param lifetime 单位：秒
     */
    public void newSession(String sessionId, int lifetime);
    
    /**
     * 保活
     * 
     * @param sessionId
     * @param lifetime 单位：秒
     */
    public void keepalive(String sessionId, int lifetime);

    /**
     * put数据到store中
     * 
     * @param sessionId
     * @param key
     * @param value
     */
    public void put(String sessionId, String key, Object value);

    /**
     * 从store中根据key获取数据
     * 
     * @param sessionId
     * @param key
     * @return
     */
    public Object get(String sessionId, String key);

    /**
     * 获取所有的key列表
     * 
     * @param sessionId
     * @return
     */
    public String[] getAllKeys(String sessionId);

    /**
     * 删除指定key的内容
     * 
     * @param sessionId
     * @param key
     */
    public void delete(String sessionId, String key);

    /**
     * 清理store
     * 
     * @param sessionId
     */
    public void clean(String sessionId);

}
