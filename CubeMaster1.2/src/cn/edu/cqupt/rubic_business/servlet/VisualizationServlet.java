package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.ProcessRecordPo;
import cn.edu.cqupt.rubic_business.Model.po.VisualizationPo;
import cn.edu.cqupt.rubic_business.service.AttributeService;
import cn.edu.cqupt.rubic_business.service.DataSetService;
import cn.edu.cqupt.rubic_business.service.ResultService;
import cn.edu.cqupt.rubic_business.service.VisualizationService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @description 可视化加载页面
 * @author LiuMian
 * @date 2015-10-28 下午4:42:59
 * @version 1.0
 * 
 */
public class VisualizationServlet extends HttpServlet {

	

	public void init() throws ServletException {
		super.init();
		
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		VisualizationService visualizationService = null;
		DataSetService dataSetService = null;
		AttributeService attributeService = null;
		ResultService resultService = null;
		WebApplicationContext webAppContext = null;
		
		ServletContext servletContext = this.getServletContext();
		webAppContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		visualizationService = webAppContext
				.getBean(VisualizationService.class);
		dataSetService = webAppContext.getBean(DataSetService.class);
		attributeService = webAppContext.getBean(AttributeService.class);
		resultService = webAppContext.getBean(ResultService.class);
		
		response.setContentType("text/html;charset=utf-8");

		HttpSession session = request.getSession(true);

		List<Integer> datasetList = (List<Integer>) session
				.getAttribute("dataset_id");
		List<Integer> processList = (List<Integer>) session
				.getAttribute("process_id");

		getResultWithAttributes(processList, resultService, attributeService);

		JSONObject visualInfo = new JSONObject();

		if (datasetList != null) {
			visualInfo.put("dataset",
					packDataSet2JSON(getDatasetWithAttributes(datasetList, attributeService), dataSetService));
		} else {
			visualInfo.put("dataset", "");
		}

		if (processList != null) {
			visualInfo.put("result",
					packResult2JSON(getResultWithAttributes(processList, resultService, attributeService), resultService));
		} else {
			visualInfo.put("result", "");
		}
		visualInfo.put("visual", packVisulization2JSON(visualizationService));
		visualInfo.put("rule", packRule2JSON(visualizationService));

//		System.out.println("visualization: " + visualInfo.toString());
		response.getWriter().write(visualInfo.toJSONString());
	}

	/**
	 * @description 绑定数据和属性
	 * @param datasetList
	 * 
	 * @return
	 */
	private Map<Integer, List<AttributePo>> getDatasetWithAttributes(
			List<Integer> datasetList,AttributeService attributeService ) {
		Map<Integer, List<AttributePo>> map = new HashMap<Integer, List<AttributePo>>();

		if (datasetList != null) {
			for (Iterator<Integer> itor = datasetList.iterator(); itor
					.hasNext();) {
				int datasetId = itor.next();
				map.put(datasetId,
						attributeService.getAttributesByDId(datasetId));
			}
		}
		return map;
	}

	/**
	 * @description 绑定结果和属性
	 * @param resultList
	 * @return
	 */
	private Map<Integer, List<AttributePo>> getResultWithAttributes(
			List<Integer> processList,ResultService resultService,AttributeService attributeService) {
		Map<Integer, List<AttributePo>> map = new HashMap<Integer, List<AttributePo>>();

		if (processList != null) {
			for (Integer id : processList) {
				ProcessRecordPo processRecordPo = resultService
						.getProcessRecordPo(id);
				String json = processRecordPo.getJson_detail();
				int resultId = processRecordPo.getResultdataset_id();
				int datasetId = Integer
						.parseInt(json.split(",")[2].split(":")[1]);
				List<AttributePo> list = attributeService
						.getAttributesByDId(datasetId);
				// list.add(resultService.getResultAttributePo(resultId));
				map.put(resultId, resultService.getResultAttributePo(resultId));
				System.out.println("map: " + map);
			}
		}
		return map;
	}

	/**
	 * @description 封装数据集
	 * @param map
	 * @return
	 */
	private JSONArray packDataSet2JSON(Map<Integer, List<AttributePo>> map,DataSetService dataSetService) {
		JSONArray jsonArray = new JSONArray();

		Set<Entry<Integer, List<AttributePo>>> set = map.entrySet();

		for (Iterator<Entry<Integer, List<AttributePo>>> itor = set.iterator(); itor
				.hasNext();) {
			Entry<Integer, List<AttributePo>> entry = itor.next();
			Map<String, Object> datasetMap = new HashMap<String, Object>();
			int datasetId = entry.getKey();
			datasetMap.put("id", String.valueOf(datasetId));
			datasetMap.put("name", dataSetService.findDataSetById(datasetId)
					.getDataset_name());
			List<AttributePo> list = entry.getValue();
			datasetMap.put("attributes", packAttributes2JSON(list));
			jsonArray.add(datasetMap);
		}

		return jsonArray;
	}

	/**
	 * @description 封装结果
	 * @param map
	 * @return
	 */
	private JSONArray packResult2JSON(Map<Integer, List<AttributePo>> map,ResultService resultService) {
		JSONArray jsonArray = new JSONArray();

		Set<Entry<Integer, List<AttributePo>>> set = map.entrySet();

		for (Iterator<Entry<Integer, List<AttributePo>>> itor = set.iterator(); itor
				.hasNext();) {
			Entry<Integer, List<AttributePo>> entry = itor.next();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			int resultId = entry.getKey();
			System.out.println("resultId: " + resultId);
			// JSONObject jsonObject = new JSONObject();
			resultMap.put("id", String.valueOf(resultId));
			resultMap.put("name", resultService.findResultById(resultId)
					.getResultdataset_name());
			List<AttributePo> list = entry.getValue();
			resultMap.put("attributes", packAttributes2JSON(list));
			jsonArray.add(resultMap);
		}

		return jsonArray;
	}

	/**
	 * @description 封装属性 attribute
	 * @param list
	 * @return
	 */
	private JSONArray packAttributes2JSON(List<AttributePo> list) {
		JSONArray jsonArray = new JSONArray();
		jsonArray.addAll(list);
		return jsonArray;
	}

	/**
	 * @description 封装visual数据
	 * @return
	 */
	private JSONArray packVisulization2JSON(VisualizationService visualizationService) {
		JSONArray jsonArray = new JSONArray();

		List<String> TypeList = visualizationService.getAllTypes();
		for (String type : TypeList) {
			Map<String, Object> visualInfoMap = new HashMap<String, Object>();
			visualInfoMap.put("visual_type", type);
			List<Map<String, Object>> visualsList = visualizationService
					.getVisualizationByType(type);
			visualInfoMap.put("visuals", visualsList);
			jsonArray.add(visualInfoMap);
		}
		return jsonArray;
	}

	/**
	 * 打包rule
	 * 
	 * @return
	 */
	private JSONArray packRule2JSON(VisualizationService visualizationService) {
		JSONArray ruleArray = new JSONArray();
		
		List<String> ruleTypes = visualizationService.findAllRuleType();
		
		for(String type:ruleTypes){
			Map<String,Object> assortedRules = new HashMap<String,Object>();
			List<HashMap<String,Object>> rules = visualizationService.findRuleByType(type);
			
			for (Map<String, Object> rule : rules) {
				Integer ruleId = (Integer) rule.get("rule_id");
				rule.put("arguments",
						visualizationService.findArgumentsByRuleId(ruleId));
				rule.put("visual_id",
						visualizationService.findVisualIdsByRuleId(ruleId));
				
			}
			assortedRules.put("rule_type", type);
			assortedRules.put("rules", rules);
			ruleArray.add(assortedRules);
		}
		
		return ruleArray;
	}
	

}
