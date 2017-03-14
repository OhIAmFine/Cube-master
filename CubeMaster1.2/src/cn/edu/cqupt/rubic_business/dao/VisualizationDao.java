package cn.edu.cqupt.rubic_business.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.VisualizationPo;

public interface VisualizationDao {
	
	List<Map<String, Object>> getAllVisualization();
	
	List<Integer> getAllVisualizationId();

	HashMap<String,Object> findVisualizationById(int id);
	
	Integer[] findAllAttributeIdByResultdatasetId(int id);
	
	List<Integer> getVisualizationIdByCharacter(int character);
	
	List<String> getAllTypes();
	
	String getTypesById(int id);
	
	List<Map<String, Object>> getVisualizationByType(String type);
	
	List<Map<String, Object>> getCalculationById(int id);
	
	List<Map<String,Object>> getIdByType(String type);
	
	List<VisualizationPo> findAllVisualization();
	
	List<Integer> getCalculationIdsById(int id);
	
	List<HashMap<String,Object>> findAllRule();
	
	List<Integer> findVisualIdsByRuleId(int ruleId);

	String findMethodNameByRid(int ruleId);
	
	List<HashMap<String,Object>> findArgumentsByRuleId(int ruleId);
	
	List<String> findAllRuleType();
	
	List<HashMap<String,Object>> findRuleByType(String type);
}
