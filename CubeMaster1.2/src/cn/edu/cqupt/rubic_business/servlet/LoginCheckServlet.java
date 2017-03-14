package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cn.edu.cqupt.rubic_business.Model.po.UserPo;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
public class LoginCheckServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(LoginCheckServlet.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");

		HttpSession session = request.getSession(false);
		
		logger.debug("doPost executing...");

		JSONObject jsonObject = new JSONObject();
		String userName = "";
		if (session != null) {
			UserPo user = (UserPo) session.getAttribute("user");
			if (user != null) {
				userName = user.getUser_name();
				jsonObject.put("user_id",user.getUser_id());
			}
		}
		
		jsonObject.put("user_name", userName);
		response.getWriter().write(jsonObject.toJSONString());

	}

}
