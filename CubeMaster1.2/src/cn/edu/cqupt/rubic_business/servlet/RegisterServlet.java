package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.service.UserService;
import cn.edu.cqupt.rubic_business.service.impl.LoginException;

import com.alibaba.fastjson.JSONObject;

/**
 * @description 注册Servlet
 * @author wangjw
 * @created 2015-5-27 11:19:30
 */
@WebServlet(name = "RegisterServlet", value = "/servlet/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	
	private UserService userService;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		userService = webAppContext.getBean(UserService.class);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=utf-8"); 
		
		String name = request.getParameter("name");
		String value = request.getParameter("value");
		
		if(name != null && value != null) {
			response.getWriter().write(constructJson(name, value));
			return;
		}
		
		//用户名
		String username = request.getParameter("user_name");
		//密码
		String password = request.getParameter("password");
		//邮箱
		String email = request.getParameter("email");
		
		if(validate("user_name", username) && validate("email", email)) {
			UserPo userPo = new UserPo();
			userPo.setUser_name(username);
			userPo.setEmail(email);
			userPo.setPassword(password);
			userService.addUser(userPo);
			
			try {
				UserPo user = userService.Login(email, password);
				request.getSession(true).setAttribute("user", user);
System.out.println(((UserPo)request.getSession().getAttribute("user")).getUser_id());
			} catch (LoginException e) {
				e.printStackTrace();
			}
			response.sendRedirect("../html/index.html");
		}else {
			response.getWriter().write("注册失败");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * @description 验证
	 * @param name
	 * @return
	 */
	public boolean validate(String name, String value) {
		if(value == null) {
			return false;
		}
		if(name.equals("user_name")) {
			return userService.validateName(value);
		}
		if(name.equals("email")) {
			return userService.validateEmail(value);
		}
		return false;
	}
	
	public String constructJson(String name, String value) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("protocol", "A---response");
		
		if(validate(name, value)) {
			jsonObject.put("status", "1");
			jsonObject.put("reason", "success");
		}else {
			jsonObject.put("status", "0");
			if(name.equals("user_name")) {
				jsonObject.put("reason", "用户名已存在");
			}
			if(name.equals("email")) {
				jsonObject.put("reason", "邮箱已注册");
			}
		}
		return jsonObject.toJSONString();
	}
	
	/**
	 * 验证邮箱格式
	 */
	public boolean validateEmailPattern(String email) {
		String check = "/w+([-+.]/w+)*@/w+([-.]/w+)*/./w+([-.]/w+)*";
		Pattern regex = Pattern.compile(check);
		if(regex.matcher(email).matches()) {
			return true;
		}else {
			return false;
		}
	}
	
}
