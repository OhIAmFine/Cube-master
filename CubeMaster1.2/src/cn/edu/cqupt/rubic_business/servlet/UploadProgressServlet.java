package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

/**
 * 
 * <p>
 * Description:上传文件的进度，将文件状态打包成json，返回前台
 * </p>
 * 
 * @author hey
 * @date 2015-9-20
 */
@SuppressWarnings("serial")
public class UploadProgressServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");

		response.setHeader("Cache-Control", "no-store");// 禁止浏览器缓存
		response.setHeader("Pragrma", "no-cache");// 禁止浏览器缓存
		response.setDateHeader("Expires", 0);// 禁止浏览器缓存

		ProgressStatus status = (ProgressStatus) request.getSession(true)
				.getAttribute("session_status");

		if (status == null) {
			status = new ProgressStatus();
		}
		response.getWriter().print(JSON.toJSONString(status));
	}

}
