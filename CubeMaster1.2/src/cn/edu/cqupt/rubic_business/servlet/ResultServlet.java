package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import cn.edu.cqupt.rubic_business.Model.po.ProcessRecordPo;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.service.ProcessRecordService;
import cn.edu.cqupt.rubic_framework.service_interface.EventListener;

/**
 * <p>Title: ResultServlet
 * <p>Description: 所有结果显示</p>
 * @author Hey
 * @data 2015-11-30
 */
@SuppressWarnings("serial")
public class ResultServlet extends HttpServlet {

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");

		// 定义service
		ProcessRecordService processRecordService = null;

		// 从IOC容器中获取service
		ServletContext context = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(context);
		processRecordService = webAppContext.getBean(ProcessRecordService.class);

		// 保存运行记录
		HashMap<String, Object> processHash = (HashMap<String, Object>) context
				.getAttribute("processHash");

		HttpSession session = request.getSession();
		
		//运行记录
		List<ProcessRecordPo>recordList=null;
		//构造json
		List<Map<String,Object>> recordMapList=null;
		Map<String,Object>jsonMap=null;
		
		UserPo user = null;
		EventListener listener = null;
		String content=null;
		String key=null;
		
		//初始化
		recordMapList=new ArrayList<Map<String,Object>>();
		jsonMap=new HashMap<String,Object>();
		
		user = (UserPo) session.getAttribute("user");
		if (user == null)return;
		//获得该用户的所有记录
		recordList=processRecordService.findAllProcessRecordByUid(user.getUser_id());
		
		//System.out.println(recordList.size());
		for(ProcessRecordPo record:recordList){
			
			key=record.getProcess_id()+"";
			
			if(record.getRun_state().equals("运行结束")){
				//record.setProcess_end(new Date());
				processHash.remove(key);
			}else{
				
				listener=(EventListener) processHash.get(key);
				if(listener!=null){
					content=listener.getContent();
					record.setRun_state(content);
				}
			}

			recordMapList.add(recordToMap(record));
		}
		//返回json
		
		jsonMap.put("protocol_id", "A-5-1");
		jsonMap.put("model", recordMapList);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.putAll(jsonMap);
		
		//System.out.println(jsonObj.toJSONString());
		response.getWriter().print(jsonObj.toString());
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);

	}

	/**
	 * @description:将record转化为map
	 * @author hey
	 * @param record
	 * @return
	 */
	private Map<String, Object> recordToMap(ProcessRecordPo record) {
		
		Map<String, Object> recordMap = new HashMap<String, Object>();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date dateStart=record.getProcess_start();
		Date dateEnd=record.getProcess_end();
		
		//将数据封装到map，最后将map转化为json
		recordMap.put("process_id", record.getProcess_id());
		if(dateStart!=null)
		recordMap.put("process_start", format.format(dateStart));
		if(dateEnd!=null)
		recordMap.put("process_end", format.format(dateEnd));
		recordMap.put("json_detail", stringToMap(record.getJson_detail()));
		recordMap.put("result_path", record.getResult_path());
		recordMap.put("run_state", record.getRun_state());
		recordMap.put("run_count", record.getRun_count());
		recordMap.put("user_id", record.getUser_id());

		return recordMap;
	}

	/**
	 * @description:将jsondetial转换为map
	 * @author hey
	 * @param jsonDetial
	 * @return
	 */
	private Map<String, Object> stringToMap(String jsonDetial) {
		Map<String, Object> jsonDetialMap = new HashMap<String, Object>();
		jsonDetial=jsonDetial.replaceAll("\"", "");
		jsonDetial=jsonDetial.substring(1,jsonDetial.length()-1);
		String[] infoArr = jsonDetial.split(",");
		
		int index = 0;
		for (String str : infoArr) {
			index = str.lastIndexOf(":");
			jsonDetialMap
					.put(str.substring(0, index), str.substring(index + 1));
		}
		
		return jsonDetialMap;
	}

}
