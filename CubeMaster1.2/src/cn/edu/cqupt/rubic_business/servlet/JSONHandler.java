package cn.edu.cqupt.rubic_business.servlet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.edu.cqupt.rubic_business.Model.po.AlgorithmPo;
import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.DataSetPo;
import cn.edu.cqupt.rubic_business.Model.po.KeywordPo;
import cn.edu.cqupt.rubic_business.Model.po.ParameterPo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONHandler {
	
	/**
	 * @description 打包数据集原子
	 * @param map
	 * @return JSONObject
	 */
	public JSONObject packDataSetPoAtom(Map<String, Object> map) {
		
		JSONObject jsonObject = new JSONObject();
		
		DataSetPo dataSetPo = (DataSetPo) map.get("dataSet");
		List<AttributePo> attributePoList = (List<AttributePo>) map.get("attributes");
		String userName = (String)map.get("user_name");
		
		String[] fieldName = ReflectHandler.getFiledName(dataSetPo);
		for (int i = 0; i < fieldName.length; i++) {
			String fieldValue = ReflectHandler.getFieldValue(dataSetPo,
					fieldName[i]);
			jsonObject.put(fieldName[i], fieldValue);
		}
		
		jsonObject.put("user_name", userName);
		jsonObject.put("attributes", attributePoList);
		
		return jsonObject;
		
	}
	
	/**
	 * @description 打包数据集详细信息
	 * @param map
	 * @return JSONObject
	 */
	public JSONObject packDataSetDetails(Map<String, Object> map) {
		
		JSONObject jsonObject = packDataSetPoAtom(map);
		
		jsonObject.put("protocol", "A-1-3-response");
	
		return jsonObject;
	}

	/**
	 * @description 打包数据集列表
	 * @param dataSetList
	 * @return JSONArray
	 */
	public JSONArray packDataSet(List<Map<String, Object>> dataSetList) {

		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < dataSetList.size(); i++) {

			Map<String, Object> map = dataSetList.get(i);
			JSONObject jsonObject = packDataSetPoAtom(map);
			
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	/**
	 * @description 打包数据集列表整体
	 * @param dataSetList
	 * @return String
	 */
	public String packDataSetList(List<Map<String, Object>> dataSetList) {

		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = packDataSet(dataSetList);

		jsonObject.put("protocol", "A-1-2-response");
		jsonObject.put("dataset", jsonArray);

		return jsonObject.toJSONString();
	}
	
	/**
	 * @description 打包算法原子
	 * @param map
	 * @return
	 */
	public JSONObject packAlgorithmPoAtom(Map<String, Object> map) {
		
		JSONObject jsonObject = new JSONObject();
		
		AlgorithmPo algorithmPo = (AlgorithmPo) map.get("algorithm");
		List<ParameterPo> parameterPoList = (List<ParameterPo>) map.get("parameters");
		List<KeywordPo> keywordPoList = (List<KeywordPo>) map.get("keywords");
		String userName = (String)map.get("user_name");

		String[] fieldName = ReflectHandler.getFiledName(algorithmPo);
		for (int i = 0; i < fieldName.length; i++) {
			String fieldValue = ReflectHandler.getFieldValue(algorithmPo,
					fieldName[i]);
			jsonObject.put(fieldName[i], fieldValue);
		}

		jsonObject.put("user_name", userName);
		jsonObject.put("parameters", parameterPoList);

		if(keywordPoList == null || keywordPoList.contains(null)) {
			jsonObject.put("key_word", "");
		}else {
			jsonObject.put("key_word", keywordPoList);
		}
		
		return jsonObject;
	}
	
	/**
	 * @description 打包算法详情
	 * @param map
	 * @return
	 */
	public String packAlgorithmDetails(Map<String, Object> map) {
		
		JSONObject jsonObject = packAlgorithmPoAtom(map);
		
		jsonObject.put("protocol", "A-2-3-response");
		
		return jsonObject.toJSONString();
	}
	
	
	/**
	 * @description 打包算法列表
	 * @param algorithmList
	 * @return JSONArray
	 */
	public JSONArray packAlgorithm(List<Map<String, Object>> algorithmList) {
		
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < algorithmList.size(); i++) {

			Map<String, Object> map = algorithmList.get(i);
			JSONObject jsonObject = packAlgorithmPoAtom(map);

			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	/**
	 * @description 打包算法列表整体
	 * @param algorithmList
	 * @return
	 */
	public String packAlgorithmList(Map<String, List<Map<String, Object>>> map ) {
		
		JSONObject jsonObject = new JSONObject();

		JSONArray jsonArray = new JSONArray();
		
		Set<Entry<String, List<Map<String, Object>>>> set = map.entrySet();
		for(Iterator iterator = set.iterator(); iterator.hasNext(); ) {
			Entry<String, List<Map<String, Object>>> entry = (Entry<String, List<Map<String, Object>>>)iterator.next();
			String algorithm_class = entry.getKey();			
			List<Map<String, Object>> algorithmList = map.get(algorithm_class);			
			
			JSONArray subArray = packAlgorithm(algorithmList);			
			Map<String, Object> subMap = new HashMap<String, Object>();
			subMap.put("algorithm_class", algorithm_class);
			subMap.put("algorithms", subArray);
			
			jsonArray.add(subMap);			
		}
		
		jsonObject.put("protocol", "A-2-2-response");
		jsonObject.put("type_tree", "");
		jsonObject.put("page", "");
		jsonObject.put("algorithm", jsonArray);

		return jsonObject.toJSONString();
	}

	/**
	 * @description 打包数据集+算法
	 * @param map
	 * @return
	 */
	public String packDataAndAlgorithm(Map<String, List<Map<String, Object>>> map) {

		JSONObject jsonObject = new JSONObject();
		
		if(map.containsKey("algorithm")){
//			System.out.println("containsKey -- algorithm");
			List<Map<String, Object>> algorithmList = map.get("algorithm");
			JSONArray algorithmJsonArray = packAlgorithm(algorithmList);
			jsonObject.put("algorithm", algorithmJsonArray);
		}else{
			jsonObject.put("algorithm",new JSONArray());
		}

		if(map.containsKey("dataSet")){
//			System.out.println("containsKey -- dataSet");
			List<Map<String, Object>> dataSetList = map.get("dataSet");
			JSONArray datasetJsonArray = packDataSet(dataSetList);
			jsonObject.put("dataset", datasetJsonArray);
			
		}else{
			jsonObject.put("dataset", new JSONArray());
		}
		jsonObject.put("protocol", "A-3-1-response");
		
		return jsonObject.toJSONString();
	}

}
