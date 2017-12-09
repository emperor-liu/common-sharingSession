/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.session
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.session;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/** 
 * ClassName: SessionResponseWrapper.java <br>
 * Description: <br>
 * @author name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年12月9日<br>
 */
public class SessionResponseWrapper extends HttpServletResponseWrapper {

	public SessionResponseWrapper(HttpServletResponse response) {
		super(response);
	}

    public void sendRedirect(String location) throws IOException {
        super.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        super.setHeader("Location", location);
    }

}
