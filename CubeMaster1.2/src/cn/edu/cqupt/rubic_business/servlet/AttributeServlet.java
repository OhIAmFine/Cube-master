package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;

import cn.edu.cqupt.rubic_business.Model.po.ResultPo;
import cn.edu.cqupt.rubic_business.service.AttributeService;
import cn.edu.cqupt.rubic_business.service.ResultService;

public class AttributeServlet extends HttpServlet {

	public void init() throws ServletException {
		super.init();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String resultdataset_id = request.getParameter("resultdataset_id");

		int resultId = Integer.parseInt(resultdataset_id);
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		AttributeService attributeService = webAppContext.getBean(AttributeService.class);
		ResultService ResultService = webAppContext.getBean(ResultService.class);

		ResultPo result = ResultService.findResultById(resultId);

		List<HashMap<String, Object>> attributeList = attributeService
				.findAttributeByResultId(resultId);
		request.getSession().setAttribute("attributeListCol", attributeList);
		
		String str = JSON.toJSONString(addProtocol(result, attributeList));

		response.getWriter().print(str);
	}

	public HashMap<String, Object> addProtocol(ResultPo resultPo,List<HashMap<String, Object>> attributeList) {
		List<HashMap<String, Object>> attributeLists = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> maps = new HashMap<String, Object>();
		maps.put("id", resultPo.getResultdataset_id());
		maps.put("name", resultPo.getResultdataset_name());
		maps.put("attributes", attributeList);
		attributeLists.add(maps);
		HashMap<String,Object>attributeListmap=new HashMap<String, Object>();
		attributeListmap.put("process_id", "A-4-1");
		attributeListmap.put("result", attributeLists);
		return attributeListmap;
	}

}
