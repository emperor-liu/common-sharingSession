package com.lljqiu.session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// UnThreadSafe
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
