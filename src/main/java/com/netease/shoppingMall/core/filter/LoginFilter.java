package com.netease.shoppingMall.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		
		HttpSession session = request.getSession(true);
		
		String url = request.getRequestURI();
		
		//访问的路径为空，访问的不是登录页面，session中没有登录用户
		if(url != null && ! url.equals("") && (url.toLowerCase().indexOf("login") < 0 ) 
				&& url.toLowerCase().indexOf("countCartGoods".toLowerCase()) < 0 && session.getAttribute("user") == null) {
			if(isAjaxRequest(request)) {
				ServletOutputStream out = response.getOutputStream();
				out.print("loginRequired");
				out.flush();
				out.close();	
				return ;
			} else {
				response.sendRedirect(request.getContextPath() + "/login?returnTo" + url);
				return ;
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 判断请求是否为ajax请求
	 * @param request
	 * @return
	 */
	private boolean isAjaxRequest(HttpServletRequest request) {
		return request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
	}
	
}
