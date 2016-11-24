package com.lljqiu.session;

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
 * <p>文件名称: SessionFilter.java</p>
 * 
 * <p>文件功能: 分布式session总入口过滤器</p>
 *
 * <p>编程者: lljqiu</p>
 * 
 * <p>初作时间: 2015年6月8日 下午10:04:52</p>
 * 
 * <p>版本: version 1.0 </p>
 *
 * <p>输入说明: </p>
 *
 * <p>输出说明: </p>
 *
 * <p>程序流程: </p>
 * 
 * <p>============================================</p>
 * <p>修改序号:</p>
 * <p>时间:	 </p>
 * <p>修改者:  </p>
 * <p>修改内容:  </p>
 * <p>============================================</p>
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
//    	BbcpLog.debug("start session....  ");
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = new SessionRequestWrapper((HttpServletRequest) request, resp, servletContext, sessionStore, sessionTimeouts);
        chain.doFilter(req, resp);
    }

    public void destroy() {

    }

}
