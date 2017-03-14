package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginRedirectServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("redirect login");
		String suffixURL = null;
		suffixURL = request.getParameter("redirect");
		String path = request.getContextPath();
//		resp.sendRedirect(suffixURL+"&setCookieURL=" + req.getScheme() + "://"
//                + "rubic.cn" + ":" + req.getServerPort()
////                + req.getServerName() + ":" + req.getServerPort()
//                + path + "/setCookie&gotoURL=http://rubic.cn" + path);
		String redirectURL = suffixURL + "&setCookieURL=http://"
				+ request.getServerName() + ":" + request.getServerPort()

				+ path + "/setCookie&gotoURL=" + path;

		response.sendRedirect(redirectURL);
		
		return;
	}

}
