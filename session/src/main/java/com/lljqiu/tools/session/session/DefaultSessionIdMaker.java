/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.session
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.session;

import com.lljqiu.tools.session.session.utils.UUID;

/** 
 * ClassName: DefaultSessionIdMaker.java <br>
 * Description: session ID 生成<br>
 * @author name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年12月9日<br>
 */
public class DefaultSessionIdMaker extends UUID implements SessionIDMaker {

    public String makeNewId() {
        return super.nextID();
    }

}
