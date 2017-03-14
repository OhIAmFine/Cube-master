package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;


import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;





import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.DataSetPo;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.service.DataSetService;
import cn.edu.cqupt.rubic_business.util.MyDateFormat;

/**
 * 数据集
 * @author LiangYH
 * @author Liuy
 *
 */
public class DataSetListServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		super.init();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=utf-8"); 
		
		String operate = request.getParameter("operate");
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		DataSetService dataSetService = webAppContext.getBean(DataSetService.class);
		
		if(operate == null || "".equals(operate)){

			JSONObject JSONobject = new JSONObject();
			JSONobject.put("protocol", "A-1-3-response");
			JSONobject.put("dataset", packDataSetList(dataSetService));
			response.getWriter().write(JSONobject.toString());
			return ;
		}
		if("getCurrentUserDataSet".equals(operate)){
			//获取用户id
			HttpSession session = request.getSession();
			UserPo currentUser = (UserPo) session.getAttribute("user");
			//数据库操作：查询数据集
			List<Map<String,Object>> dataSets = null;
			dataSets = dataSetService.findCurrentUserDataSet(currentUser);
			
			PrintWriter writer = response.getWriter();
			writer.write(JSONArray.toJSONString(dataSets));
			writer.close();
			return;
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doGet(request, response);
		
	}
	/**
	 * 
	 * 从数据库查找数据并封装json
	 * @return JSONarray
	 * 
	 */
	@SuppressWarnings({ "unchecked"})
	private JSONArray packDataSetList(DataSetService dataSetService){
		List<Map<String, Object>> dataSet = dataSetService.findAllDataSetAndInfo();
		JSONArray JSONarray = new JSONArray();
		for(int i=0;i<dataSet.size();i++){
		  JSONObject jsonObj = new JSONObject();
		  JSONObject dataFiledObj = new JSONObject();
		  Map<String,Object> dataSetMap = dataSet.get(i);
		  DataSetPo dataSetPo = (DataSetPo) dataSetMap.get("dataSet");
		  List<AttributePo> attributes = (List<AttributePo>) dataSetMap.get("attributes");
		  String userName = (String)dataSetMap.get("user_name");
	      int dataset_id = dataSetPo.getDataset_id();
		  List<Map<String,Object>> dataSetList = dataSetService.findDataSetByIdReturnMap(dataset_id);
		  for (Map<String, Object> map : dataSetList) {
			  MyDateFormat.formatDate(map);
			  dataFiledObj.putAll(map);
		}
		  jsonObj.put("dataset", dataFiledObj);
		  jsonObj.put("attributes", attributes);
		  jsonObj.put("user_name", userName);
		  
		  JSONarray.add(jsonObj);
		  
		}
		
		return  JSONarray;
	}
	
}
