package cn.edu.cqupt.rubic_business.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/*
 *@description 网页过滤器 
 *@author wangjw
 *@created  2015-5-24 下午15:18:30
 */
//@WebFilter(filterName="WebPageFilter", urlPatterns="*.html")
public class WebPageFilter implements Filter{
	
	private FilterConfig filterConfig;

	/**
	 * 过滤器销毁
	 */
	public void destroy() {
		filterConfig = null;
	}

	/**
	 * 过滤
	 * @param request
	 * @param response
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String URI = ((HttpServletRequest)request).getRequestURI();
		int indexOfPoint = URI.lastIndexOf(".");
		int indexOfFileName = URI.lastIndexOf("/");
		String suffixName = URI.substring(indexOfPoint+1, URI.length());
		String fileName = URI.substring(indexOfFileName+1, URI.length());
		String newURI = new String("/" + suffixName + "/" + fileName);
		RequestDispatcher dispatcher = request.getRequestDispatcher(newURI);
		dispatcher.forward(request, response);		
	}

	/**
	 * 过滤器初始化
	 * @param filterConfig
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

}
