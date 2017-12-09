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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

/** 
 * ClassName: SessionFilter.java <br>
 * Description: 分布式session总入口过滤器<br>
 * Create by: name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年4月12日<br>
 */
public class SessionFilter implements Filter {
	
	private ServletContext servletContext;
	private SessionStore sessionStore;
	private int sessionTimeouts = 30 * 60; // 单位：秒

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }
    
    public void setSessionTimeouts(int sessionTimeouts) {
	    	if (sessionTimeouts > 0) {
	    		this.sessionTimeouts = sessionTimeouts * 60;
	    	}
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
        String sessionTimeouts = filterConfig.getInitParameter("sessionTimeouts");
        if (StringUtils.isNotBlank(sessionTimeouts)) {
        		setSessionTimeouts(Integer.parseInt(sessionTimeouts.trim()));
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = new SessionRequestWrapper((HttpServletRequest) request, resp, servletContext, sessionStore, sessionTimeouts);
        chain.doFilter(req, resp);
    }

    public void destroy() {

    }

}
