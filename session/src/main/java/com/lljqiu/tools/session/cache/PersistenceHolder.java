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
 * ClassName: PersistenceHolder.java <br>
 * Description: 持久化持有者，用于设置对象的过期时间<br>
 * Create by: name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年4月8日<br>
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
