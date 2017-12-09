/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.session
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.session;

/** 
 * ClassName: SessionStore.java <br>
 * Description: session存储接口，不同的实现可以将session数据持久化到任意地方<br>
 * Create by: name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年4月8日<br>
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
