package cn.edu.cqupt.rubic_business.servlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.edu.cqupt.net.FileSystemConnector;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.edu.cqupt.rubic_business.service.DataSetService;
import cn.edu.cqupt.rubic_business.service.ResultService;
import cn.edu.cqupt.rubic_business.service.VisualizationService;
import cn.edu.cqupt.rubic_chartclassification.Rule;
import cn.edu.cqupt.rubic_core.config.Configuration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @description 可视化结果展示
 * @author Wong JW
 * @date 2015年10月24日 上午11:23:09 
 * @version 1.0 
 */
public class VisualResultDisplayServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		/** init **/
		ServletContext sc = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(sc);
		DataSetService dataSetService = webAppContext.getBean(DataSetService.class);
		ResultService resultService = webAppContext.getBean(ResultService.class);
		VisualizationService visualizationService = webAppContext.getBean(VisualizationService.class);

		response.setContentType("text/html;charset=utf-8");
		
		String details = String.valueOf(request.getParameter("visual_details"));

		
		/** 获取相应JSONObject **/
		JSONObject visualDetailsJSONObj = JSONObject.parseObject(details);
		JSONObject dataJSONObj = visualDetailsJSONObj.getJSONObject("dataset");
		JSONObject ruleJSONObj = visualDetailsJSONObj.getJSONObject("rule");
		JSONObject visualJSONObj = visualDetailsJSONObj.getJSONObject("visual");
		JSONArray visualJSONArray = visualJSONObj.getJSONArray("visual_id");

		
		/** 获取文件路径 **/
		int datasetId = Integer.parseInt(String.valueOf(dataJSONObj.get("dataset_id")));
		String flag = String.valueOf(dataJSONObj.get("flag"));
		String filePath = null;
		if ("original".equals(flag)) {
			filePath = getDataPath(dataSetService, datasetId);
		}
		if("result".equals(flag)) {
			filePath = getResultPath(resultService, datasetId);
		}

		HttpSession session = request.getSession(false);
		UserPo userPo = (UserPo) session.getAttribute("user");
		System.out.println("user_id: " + userPo.getUser_id());
        Configuration config = new Configuration(userPo);

		/** 从文件系统中获取数据文件 */
        filePath = getFileFromFileSystem(filePath, config);

        /** 获取方法名称 **/
		String methodName = String.valueOf(ruleJSONObj.get("method_name"));
		
		/** 获取参数顺序 **/
		JSONArray attributeArray = dataJSONObj.getJSONArray("attribute");
		Map<Object, Object> argsAttributeMap = generateRelationshipMap(attributeArray, "argument_sequence", "attribute_sequence");
		Integer[] columnSequence = getColumnSequence(argsAttributeMap);
		
		/** 生成参数数组 **/
		Object[] args = generateArgs(filePath, columnSequence);

		/** 删除从文件系统中取得的文件 */
        System.out.println("删除从文件系统中取得的文件： " + config.getTMP_PATH());
        FileHandler.deleteFile(new File(config.getTMP_PATH()));

		/** 封装返回数据 **/		 
		JSONObject returnedJSONObj = new JSONObject();
		returnedJSONObj.put("protocol_id", "A-4-2");
		returnedJSONObj.put("visuals", packVisualization(visualizationService, visualJSONArray));
		returnedJSONObj.put("data", Rule.getVisualData(methodName, args));
		response.getWriter().write(returnedJSONObj.toJSONString());
	}
	
	

	/**
	 * 获取文件路径
	 * @param service dataSetService
	 * @param id 数据集ID
	 * @return 文件路径 
	 */
	private String getDataPath(DataSetService service, int id) {
		//return Configuration.getRubic() + service.findDataSetById(id).getFile_path();
        return service.findDataSetById(id).getFile_path();
	}
	
	/**
	 * 获取结果文件路径
	 * @param service resultDataService
	 * @param id 结果数据集id
	 * @return 文件路径
	 */
	private String getResultPath(ResultService service, int id) {
//		return Configuration.getRubic() + service.findResultById(id).getFile_path();
		return service.findResultById(id).getFile_path();
	}
	
	/**
	 * @description 根据参数顺序 构造attribute列序数组
	 * @param argsAttributeMap args 和 attribute 映射关系
	 * @return
	 */
	private Integer[] getColumnSequence(Map<Object, Object> argsAttributeMap) {
		List<Integer> columnSequenceList = new ArrayList<Integer>();
		Set<Object> set = argsAttributeMap.keySet();
		for(Object key : set) {
			columnSequenceList.add(Integer.valueOf(String.valueOf((argsAttributeMap.get(key)))));
		}
		return columnSequenceList.toArray(new Integer[]{});
	}
	
	/**
	 * @description 将JSONArray 两个字段keyName valueName 对应的值 加入到Map
	 * @param jsonArray JSON属猪
	 * @param keyName key名称
	 * @param valueName value名称
	 * @return
	 */
	private Map<Object, Object> generateRelationshipMap(JSONArray jsonArray, String keyName, String valueName) {
		Map<Object, Object> relationshipMap = new TreeMap<Object, Object>();
		for(int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			relationshipMap.put(jsonObj.get(keyName), jsonObj.get(valueName));
		}
		return relationshipMap;
	}
	
	/**
	 * @description 生成方法参数
	 * @param path 文件路径
	 * @param columnSequences 数据列序
	 * @return
	 */
	private Object[] generateArgs(String path, Integer[] columnSequences) {
		List<String[]> list = new ArrayList<String[]>();
		for(int sequence : columnSequences) {
			list.add(FileHandler.readOneColumn(path, sequence));
		}
		return list.toArray(new Object[]{});
	}
	
	/**
	 * @description 封装可视化数据
	 * @param visualId 可视化ID数组
	 * @return
	 */
	private JSONArray packVisualization(VisualizationService service, JSONArray visualId) {
		JSONArray jsonArray = new JSONArray();
		for(int i = 0; i < visualId.size(); i++) {
			int id = Integer.parseInt(String.valueOf(visualId.get(i)));
			jsonArray.add(service.findVisualizationById(id));
		}
		return jsonArray;
	}

    private String getFileFromFileSystem(String file_path, Configuration config){

        Map<String, Object> requestMap=new HashMap<String, Object>();

        requestMap.put("protocol_id","2");

        requestMap.put("file_path", file_path);

        FileSystemConnector connector = new FileSystemConnector();

        connector.sendGetFileRequest(requestMap);

        int index = file_path.lastIndexOf("\\");

        String file_name = file_path.substring(index + 1, file_path.length());

        connector.saveFile(config.getTMP_PATH(), file_name);

        String data_path = config.getTMP_PATH() + File.separator + file_name;

        return data_path;
    }

}
