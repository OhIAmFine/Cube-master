package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;

import cn.edu.cqupt.rubic_business.Model.po.DataSetPo;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.service.DataSetService;
import cn.edu.cqupt.rubic_business.util.MyDateFormat;
import cn.edu.cqupt.rubic_core.config.Configuration;

/**
 * 
 * @description 数据详情展示Servlet
 * @updateauthor Zhangx,Heguqngqin
 */

@SuppressWarnings("serial")
public class DataSetDetailsServlet extends HttpServlet{
	
	@Override
	public void init() throws ServletException {
		super.init();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=utf-8"); 
		HttpSession session = null;
		JSONObject jsonObj = new JSONObject();
		String isViewMoreDataset = request.getParameter("dataset_viewmore");
		String dataSetIdStr = request.getParameter("dataset_id");
		int dataSetId = Integer.valueOf(dataSetIdStr);
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		DataSetService dataSetService = webAppContext.getBean(DataSetService.class);
		
		Map<String, Object> dataSetAndAttrMap = dataSetService.findAllDataSetAndInfoByDid(dataSetId);
		DataSetPo dataSet = (DataSetPo) dataSetAndAttrMap.get("dataSet");
		Map<String,Object> formatDate = MyDateFormat.formatDataSetDate(dataSet);
		
		String data_platform = (String) formatDate.get("platform");
		
		if(isViewMoreDataset.equals("false")){
			
			
			jsonObj.put("protocol", "A-1-3-response");
			jsonObj.put("user_name", dataSetAndAttrMap.get("user_name"));
			jsonObj.put("dataset", formatDate);
			jsonObj.put("attributes", dataSetAndAttrMap.get("attributes"));
			
			session = request.getSession(true);
			session.setAttribute("viewmore_times", new Integer(0));
			
		} else{
			session = request.getSession(false);
			String filePath = getRealFilePath(session, dataSetId, dataSetService, data_platform);

			int viewmore_times = (Integer) session.getAttribute("viewmore_times");
			DisplayFileHander hander = new DisplayFileHander(filePath,viewmore_times + 1,data_platform);
			
			List<String[]> datasetSource = hander.getDatasetDetailsByViewtimes();
			
			if(datasetSource == null || datasetSource.size() < 20){
				jsonObj.put("data_hasmore", "false");
			} else{
				jsonObj.put("data_hasmore", "true");
			}
			jsonObj.put("data_source", datasetSource);
			
			session.setAttribute("viewmore_times", viewmore_times + 1);
		}
		response.getWriter().write(jsonObj.toString());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	private String getRealFilePath(HttpSession session, int dataSetId, DataSetService dataSetService, 
			String data_platform){
		
		String rootPath = "";
		String dataPath = dataSetService.findDataSetById(dataSetId).getFile_path();
		
		if("hadoop".equals(data_platform)){
			rootPath = Configuration.getHDFS();
		}else{
			rootPath = Configuration.getRubic();
		}
		
		return rootPath + dataPath;
	}

}
