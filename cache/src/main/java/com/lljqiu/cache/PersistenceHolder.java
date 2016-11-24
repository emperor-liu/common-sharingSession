package com.lljqiu.cache;

import java.util.Map;

/**
 * 持久化持有者，用于设置对象的过期时间
 * 
 * @author lljqiu
 * @date 2014-7-4 下午10:19:10
 */
public class PersistenceHolder {
    private Map<String, Integer> typeExpiredMap;

    public Map<String, Integer> getTypeExpiredMap() {
        return typeExpiredMap;
    }

    public void setTypeExpiredMap(Map<String, Integer> typeExpiredMap) {
        this.typeExpiredMap = typeExpiredMap;
    }

}
