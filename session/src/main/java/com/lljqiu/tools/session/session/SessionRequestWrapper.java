/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.session
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/** 
 * ClassName: SessionRequestWrapper.java <br>
 * Description: 重写 HttpServletRequest<br>
 * @author name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年12月9日<br>
 */
public class SessionRequestWrapper extends HttpServletRequestWrapper {

	private SessionImpl session;
	private HttpServletResponse response;
	private ServletContext servletContext;
	private SessionStore sessionStore;
	private int sessionTimeouts; // 单位：秒

	public SessionRequestWrapper(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext,
			SessionStore sessionStore, int sessionTimeouts) {
        super(request);
        this.response = response;
        this.servletContext = servletContext;
        this.sessionStore = sessionStore;
        this.sessionTimeouts = sessionTimeouts;
    }

    public String getRequestedSessionId() {
    	return session != null ? session.getId() : null;
    }

    public HttpSession getSession(boolean create) {
        return getSession();
    }

	public HttpSession getSession() {
		return session != null ? session : 
			new SessionImpl(this, response, servletContext, sessionStore, sessionTimeouts);
    }

    public boolean isRequestedSessionIdValid() {
        return session.isRequestedSessionIdValid();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return session.isSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromURL() {
        return session.isSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }

}
