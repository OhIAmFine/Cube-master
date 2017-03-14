package cn.edu.cqupt.rubic_business.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.edu.cqupt.rubic_business.Model.po.UserPo;


public class CheckLoginFilter implements Filter {

	private FilterConfig config = null;
	public void destroy() {
	
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		HttpSession session = httpRequest.getSession();
		if(session == null ){
			ServletContext context = config.getServletContext();
			
			RequestDispatcher dispatcher = context.getRequestDispatcher("/servlet/LogoutServlet");
			dispatcher.forward(httpRequest, httpResponse);
		}
		
		
		chain.doFilter(request, response);
	}


	public void init(FilterConfig fConfig) throws ServletException {
		this.config = fConfig;
	}

}
