package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.service.UserService;
import cn.edu.cqupt.rubic_business.service.impl.LoginException;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(LoginServlet.class);


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		
		UserService userService;
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppcontext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		userService = webAppcontext.getBean(UserService.class);

		String email = (String) request.getParameter("email");
		String password = (String) request.getParameter("password");

		JSONObject jsonObj = new JSONObject();
		try {
			UserPo userPo = userService.Login(email, password);
			request.getSession().setAttribute("user", userPo);
			
			/** log **/
			logger.info("Session Id: " + request.getSession().getId());
			logger.info("UserName: " + userPo.getUser_name());
			
			jsonObj.put("status", "1");
			jsonObj.put("reason", "登录成功");
			jsonObj.put("user_name", userPo.getUser_name());
			//设置cookie
			handleCookie(request, response);
		} catch (LoginException e) {
			jsonObj.put("status", "0");
			jsonObj.put("reason", e.getMessage());
		}

		response.getWriter().write(jsonObj.toJSONString());
	}

	private void handleCookie(HttpServletRequest request,
			HttpServletResponse response) {
		String sessionValue = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JSESSIONID")) {
					sessionValue = cookie.getValue();
				}
			}
		}
		if (sessionValue != null) {
			Cookie newCookie = new Cookie("JSESSIONID", sessionValue);
			// 设置失效时间为一周
			newCookie.setMaxAge(60 * 60 * 24);
			response.addCookie(newCookie);
		}
	}

}
