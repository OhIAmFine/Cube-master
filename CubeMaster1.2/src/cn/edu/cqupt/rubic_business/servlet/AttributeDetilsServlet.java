package cn.edu.cqupt.rubic_business.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;

import cn.edu.cqupt.rubic_business.Model.po.ResultPo;
import cn.edu.cqupt.rubic_business.dao.ResultDao;
import cn.edu.cqupt.rubic_business.service.AttributeService;
import cn.edu.cqupt.rubic_business.service.ResultService;

/**
 * 
 * <p>
 * Description:数据列的数据
 * </p>
 * 
 * @author dave
 * @date 2015-9-8
 */
public class AttributeDetilsServlet extends HttpServlet {

	public void init() throws ServletException {
		super.init();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String resultDasetId = request.getParameter("resultdataset_id");
		@SuppressWarnings("unchecked")
		// 获得选择的列保存在该session中
		Set<Integer> attributeSet = (Set<Integer>) request.getSession(true)
				.getAttribute("attributeSet");
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		AttributeService attributeService = webAppContext.getBean(AttributeService.class);
		ResultService resultService = webAppContext.getBean(ResultService.class);
		
		// 保存的结果
		ResultPo result = resultService.findResultById(Integer
				.parseInt(resultDasetId));
		// String resultFile = result.getFile_path();
		String resultFile = "F:\\Rubic\\57\\result\\iris.txt";

		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> attributeLists = (List<HashMap<String, Object>>) request
				.getSession(true).getAttribute("attributeListCol");

		List<Integer> colList = new ArrayList<Integer>();
		
		for (Integer attributeId : attributeSet) {
			for (int i = 0; i < attributeLists.size(); i++) {
				int aid = (Integer) attributeLists.get(i).get("attribute_id");
				if (attributeId == aid) {
					colList.add(i);
				}
			}

		}

		Integer[] colArray = colList.toArray(new Integer[0]);

		Map<String, Object> maps = new HashMap<String, Object>();

		for (int i = 0; i < colArray.length; i++) {
			List<String> dataList = new ArrayList<String>();

			int num = colArray[i];
			File file = new File(resultFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String con = null;
			try {

				while ((con = br.readLine()).trim().isEmpty()
						|| (con = br.readLine()) != null) {
					System.out.println(con + "====");
					if (!con.trim().isEmpty()) {

						String[] dataArray = con.split(",");

						dataList.add(dataArray[num]);
					}

				}
			} catch (Exception e) {
				e.toString();
			}
			maps.put("col" + num, dataList);
		}

		String jsonString = JSON.toJSONString(maps);
		response.getWriter().print(jsonString);

	}

}
