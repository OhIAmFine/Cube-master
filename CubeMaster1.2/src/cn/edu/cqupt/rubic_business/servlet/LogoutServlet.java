package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

public class LogoutServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/html;charset=utf-8");
		
		HttpSession session = req.getSession();
		if(session != null){
			session.removeAttribute("user");
		}
		session.invalidate();
		JSONObject json = new JSONObject();
		json.put("user_name", "");
		resp.getWriter().write(json.toJSONString());
	}
}
