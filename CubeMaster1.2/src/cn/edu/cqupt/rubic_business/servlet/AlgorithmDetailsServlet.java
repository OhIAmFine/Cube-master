package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;

import cn.edu.cqupt.rubic_business.Model.po.AlgorithmPo;
import cn.edu.cqupt.rubic_business.Model.po.KeywordPo;
import cn.edu.cqupt.rubic_business.Model.po.ParameterPo;
import cn.edu.cqupt.rubic_business.service.AlgorithmService;
import cn.edu.cqupt.rubic_business.util.MyDateFormat;

public class AlgorithmDetailsServlet extends HttpServlet{

	@Override
	public void init() throws ServletException {
		super.init();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=utf-8"); 
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		AlgorithmService algorithmService = webAppContext.getBean(AlgorithmService.class);
		
		String algorithmIdStr = request.getParameter("algorithm_id");
		int algorithmId = Integer.valueOf(algorithmIdStr);
		
		Map<String, Object> algorithmAndInfoMap = algorithmService.findAllAlgorithmAndInfoByAid(algorithmId);
		
		JSONObject jsonObj = new JSONObject();
		
		String username = (String) algorithmAndInfoMap.get("user_name");
		AlgorithmPo algorithm=(AlgorithmPo) algorithmAndInfoMap.get("algorithm");
		List<KeywordPo> keywords = (List<KeywordPo>) algorithmAndInfoMap.get("keywords");
		List<ParameterPo> parameters = (List<ParameterPo>) algorithmAndInfoMap.get("parameters");
		
		jsonObj.put("user_name", username);
		jsonObj.put("algorithm", algorithm);
		jsonObj.put("key_word", keywords);
		jsonObj.put("parameters", parameters);
		
		jsonObj = MyDateFormat.formatAlgorithmDate(jsonObj);
		System.out.println("details: "+jsonObj.toString());
		response.getWriter().write(jsonObj.toString());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
