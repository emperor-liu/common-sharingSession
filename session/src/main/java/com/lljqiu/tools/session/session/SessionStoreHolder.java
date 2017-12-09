/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.session
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.session;

import java.util.Map;

/** 
 * ClassName: SessionStoreHolder.java <br>
 * Description: <br>
 * @author name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年12月9日<br>
 */
public class SessionStoreHolder {

    private Map<String, SessionStore> storeMap;
    private String                    defaultStoreName;
    private Map<String, String>       keyStoreMap;
    private int                       sessionInvalidTime;

    public int getSessionInvalidTime() {
        return sessionInvalidTime;
    }

    public void setSessionInvalidTime(int sessionInvalidTime) {
        this.sessionInvalidTime = sessionInvalidTime;
    }

    public Map<String, SessionStore> getStoreMap() {
        return storeMap;
    }

    public void setStoreMap(Map<String, SessionStore> storeMap) {
        this.storeMap = storeMap;
    }

    public String getDefaultStoreName() {
        return defaultStoreName;
    }

    public void setDefaultStoreName(String defaultStoreName) {
        this.defaultStoreName = defaultStoreName;
    }

    public Map<String, String> getKeyStoreMap() {
        return keyStoreMap;
    }

    public void setKeyStoreMap(Map<String, String> keyStoreMap) {
        this.keyStoreMap = keyStoreMap;
    }

}
