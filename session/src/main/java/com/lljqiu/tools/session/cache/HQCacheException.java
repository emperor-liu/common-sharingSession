/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.cache
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.cache;

/** 
 * ClassName: HQCacheException.java <br>
 * Description: 自定义异常<br>
 * @author name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年12月9日<br>
 */
public class HQCacheException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HQCacheException() {
        super();
    }

    public HQCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public HQCacheException(String message) {
        super(message);
    }

    public HQCacheException(Throwable cause) {
        super(cause);
    }

}
