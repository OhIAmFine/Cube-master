package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;


import cn.edu.cqupt.rubic_business.Model.po.ProcessRecordPo;
import cn.edu.cqupt.rubic_business.service.ResultService;
import cn.edu.cqupt.rubic_core.config.Configuration;
/**
 * 
 * @author he guangqin
 *
 */
@SuppressWarnings("serial")
public class ResultDetailsServlet extends HttpServlet {



	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		doPost(request,response);
		
	}

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		HttpSession session = null;
		JSONObject jsonObj = new JSONObject();
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		ResultService resultService = webAppContext.getBean(ResultService.class);
		String result_platform;

		String processRecordId = request.getParameter("process_id");
		int process_id = Integer.valueOf(processRecordId);
		String isViewMoreResultDataset = request.getParameter("resultdataset_viewmore");

		//		DataSetPo dataset = dataSetService.findDataSetById(dataset_id);
		ProcessRecordPo processRecord = resultService.getProcessRecordPo(process_id);
		result_platform = processRecord.getPlatform();

		String jsonString = processRecord.getJson_detail();
		String[] strs = jsonString.split(",");
		for (String string : strs) {
			if(string.substring(1, string.lastIndexOf("\"")).equals("algorithm_name")){
				jsonObj.put(string.substring(1, string.lastIndexOf("\"")), string.substring(string.lastIndexOf("\"")+2,string.length()));
			}
			if(string.substring(1, string.lastIndexOf("\"")).equals("dataset_name")){
				jsonObj.put(string.substring(1, string.lastIndexOf("\"")), string.substring(string.lastIndexOf("\"")+2,string.length()));
			}
		}

		String[] fieldName = ReflectHandler.getFiledName(processRecord);
		for (int i = 0; i < fieldName.length; i++) {

			String fieldValue = ReflectHandler.getProcessRecordFieldValue(processRecord,
					fieldName[i]);

			jsonObj.put(fieldName[i], fieldValue);
		}

		if(isViewMoreResultDataset==null ||
				isViewMoreResultDataset.equals("")||
				isViewMoreResultDataset.equals("false")){

			session = request.getSession(true);
			session.setAttribute("viewmore_times", new Integer(1));

		}else{
			session =request.getSession(false);
			String result_path;
			if("java".equalsIgnoreCase(result_platform)||"\"java\"".equalsIgnoreCase(result_platform)){
				
				result_path = Configuration.getRubic() + processRecord.getResult_path();
			}else{
				result_path = Configuration.getHDFS() + processRecord.getResult_path();
			}

			int viewmore_times = (Integer) session.getAttribute("viewmore_times");

			DisplayFileHander resultFileHander = new DisplayFileHander(result_path, viewmore_times,result_platform);

			List<String[]> resultSource = resultFileHander.getDatasetDetailsByViewtimes();

			if(resultSource == null || resultSource.size() < 20){
				jsonObj.put("data_hasmore", "false");
			}else{
				jsonObj.put("data_hasmore", "true");
			}

			jsonObj.put("data_source", resultSource);
			session.setAttribute("viewmore_times", viewmore_times+1);

		}
		List<Object> process_id_session = (List<Object>) session.getAttribute("process_id");
		
		String isselected = "1";
		
		if(process_id_session != null && process_id_session.contains(process_id)){
			isselected = "0";
		}
		
		jsonObj.put("isselected", isselected);


		response.getWriter().write(jsonObj.toString());

	}

}
