/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.session
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/** 
 * ClassName: SessionImpl.java <br>
 * Description: HttpSession的自定义实现，<br>
 * Create by: name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年4月8日<br>
 */
public class SessionImpl implements HttpSession {
	
	private static String SESSION_ID_NAME = "lljqiu_jsession_id";
	private static String SESSION_CREATE_TIME = "lljqiu_jsession_create_time";
	private static String SESSION_LAST_ACCESS_TIME = "lljqiu_jsession_last_access_time";
	public static String COOKIE_PATH_VAL = "/";
	
	private SessionIDMaker sidMaker = new DefaultSessionIdMaker();

	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServletContext servletContext;
	private int maxInactiveInterval;
	private SessionInternal session;
	private SessionStore sessionStore;
	private boolean isNew;
	private boolean keepalived;

    public SessionImpl(HttpServletRequest request, HttpServletResponse response,
                       ServletContext servletContext, 
                       SessionStore sessionStore, int maxInactiveInterval) {
        this.request = request;
        this.response = response;
        this.servletContext = servletContext;
        this.sessionStore = sessionStore;
        this.maxInactiveInterval = maxInactiveInterval;
        init();
    }

    private void init() {
        session = restoreSessionState(request);
        if (session == null) {
            session = createNewSession();
            isNew = true;
            return;
        }
        validateSession();
    }

    private void validateSession() {
        if (new Date().getTime() - session.getLastAccessTime() > (maxInactiveInterval * 1000)) {
            invalidate();
            return;
        }
        session.setLastAccessTime(new Date().getTime());
        // 刷新最新访问时间cookie
        Cookie lastAccessTime = new Cookie(SESSION_LAST_ACCESS_TIME, String.valueOf(session.getLastAccessTime()));
        lastAccessTime.setPath(COOKIE_PATH_VAL);
        lastAccessTime.setHttpOnly(true);
        response.addCookie(lastAccessTime);
    }

    private SessionInternal createNewSession() {
        SessionInternal session = new SessionInternal();
        String id = sidMaker.makeNewId();
        Cookie sid = new Cookie(SESSION_ID_NAME, id);
        sid.setPath(COOKIE_PATH_VAL);
        sid.setHttpOnly(true);
        Date now = new Date();
        String time = String.valueOf(now.getTime());
        Cookie createTime = new Cookie(SESSION_CREATE_TIME, time);
        createTime.setPath(COOKIE_PATH_VAL);
        createTime.setHttpOnly(true);
        Cookie lastAccessTime = new Cookie(SESSION_LAST_ACCESS_TIME, time);
        lastAccessTime.setPath(COOKIE_PATH_VAL);
        lastAccessTime.setHttpOnly(true);
        response.addCookie(sid);
        response.addCookie(createTime);
        response.addCookie(lastAccessTime);
        session.setSessionId(id);
        session.setCreationTime(time);
        sessionStore.newSession(getId(), maxInactiveInterval);
        return session;
    }

    private SessionInternal restoreSessionState(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isEmpty(cookies)) { return null; }
        String sessionId = null;
        String creationTime = null;
        String lastAccessTime = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SESSION_ID_NAME.equalsIgnoreCase(cookie.getName())) {
                	sessionId = cookie.getValue();
                    continue;
                }
                if (SESSION_CREATE_TIME.equalsIgnoreCase(cookie.getName())) {
                	creationTime = cookie.getValue();
                    continue;
                }
                if (SESSION_LAST_ACCESS_TIME.equalsIgnoreCase(cookie.getName())) {
                	lastAccessTime = cookie.getValue();
                    continue;
                }
            }
        }
        if (StringUtils.isBlank(sessionId) || StringUtils.isBlank(creationTime) || StringUtils.isBlank(lastAccessTime)) {
        	return null;
        }
        SessionInternal session = new SessionInternal();
        session.setSessionId(sessionId);
        session.setCreationTime(creationTime);
        try {
        	session.setLastAccessTime(Long.parseLong(lastAccessTime));
        } catch (Exception ignore) {
        	session.setLastAccessTime(0);
        }
        return session;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isSessionIdFromURL() {
        return false;
    }

    public boolean isSessionIdFromCookie() {
        return true;
    }

    public long getCreationTime() {
        if (session == null || StringUtils.isEmpty(session.getCreationTime())) {
            return 0;
        }
        return Long.valueOf(session.getCreationTime());
    }

    public String getId() {
        return session == null ? null : session.getSessionId();
    }

    public long getLastAccessedTime() {
        return session == null ? 0 : session.getLastAccessTime();
    }

    public ServletContext getServletContext() {
        return this.servletContext;
    }

    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    public int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    @SuppressWarnings("deprecation")
    public javax.servlet.http.HttpSessionContext getSessionContext() {
        throw new IllegalAccessError("this method can't use in our work,is deprecated.");
    }

    public Object getAttribute(String name) {
    	if (!keepalived) {
    		sessionStore.keepalive(getId(), maxInactiveInterval);
    		keepalived = true;
    	}
        return sessionStore.get(this.getId(), name);
    }

    public Object getValue(String name) {
        return getAttribute(name);
    }

    public Enumeration<String> getAttributeNames() {
        List<String> array = new ArrayList<String>();
        Collections.addAll(array, getValueNames());
        return Collections.enumeration(array);
    }

    public String[] getValueNames() {
    	if (!keepalived) {
    		sessionStore.keepalive(getId(), maxInactiveInterval);
    		keepalived = true;
    	}
        Set<String> set = new HashSet<String>();
        String[] names = sessionStore.getAllKeys(this.getId());
        if (names != null) {
            for (String name : names) {
                set.add(name);
            }
        }
        return (String[]) set.toArray();
    }

    public void setAttribute(String name, Object value) {
    	sessionStore.put(getId(), name, value);
    	if (!keepalived) {
    		sessionStore.keepalive(getId(), maxInactiveInterval);
    		keepalived = true;
    	}
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeAttribute(String name) {
    	if (!keepalived) {
    		sessionStore.keepalive(getId(), maxInactiveInterval);
    		keepalived = true;
    	}
    	sessionStore.delete(getId(), name);
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void invalidate() {
    	sessionStore.clean(getId());
        this.session = createNewSession();
        isNew = true;
        keepalived = false;
    }

    public boolean isNew() {
        return isNew;
    }

    private static class SessionInternal {
        private String sessionId;
        private String creationTime;
        private long   lastAccessTime;

        public long getLastAccessTime() {
            return lastAccessTime;
        }

        public void setLastAccessTime(long lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(String creationTime) {
            this.creationTime = creationTime;
        }

    }

}
