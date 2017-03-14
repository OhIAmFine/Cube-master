package cn.edu.cqupt.rubic_business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.VisualizationPo;

public interface VisualizationService {
	
	/**
	 * 查找所有visual Id
	 * @return List
	 */
	List<Integer> getAllVisualizationId();
	
	/**
	 * 查找一条记录
	 * @param id 
	 * @return 一条可以直接放进json的记录
	 */
	HashMap<String,Object> findVisualizationById(int id);
	
	/**
	 * 根据数组中的id查询所有的记录
	 * @param ids int数组
	 * @return 查询到的所有记录
	 */
	ArrayList<Map<String,Object>> findVisualizationsByIds(int[] ids);
	
	/**
	 * 根据ResultdatasetId查询所有的AttributeId
	 * @param id ResultdatasetId
	 * @return 查询到的AttributeId数组
	 */
	int[] findAllAttributeIdByResultdatasetId(int id);
	
	/**
	 * 根据ResultdatasetId查询所有的Attribute
	 * @param id ResultdatasetId
	 * @return 查询到的（可以直接放进json的）Attribute数组
	 */
	List<Map<String,Object>> findAllAttributeByResultdatasetId(int id);
	
	/**
	 * 获取属性具体数据
	 * @param attributeId
	 * @return
	 */
	<T> List<T> getAttribute(String path, int attributeId); 
	
	List<String> getAllTypes();
	
	String getTypesById(int id);
	
	List<Map<String, Object>> getAllVisualization();
	
	List<Map<String, Object>> getVisualizationByType(String type);
	
	List<Map<String,Object>> getIdByType(String type);
	
	Map<String, Object> getfindVisualizationById(int id);
	
	List<VisualizationPo> findAllVisualization();
	
	List<Map<String, Object>> getCalculationById(int id);
	
	List<Integer> getCalculationIdsById(int id);
	
	/**
	 * 查找所有rule
	 * @return
	 */
	List<HashMap<String,Object>> findAllRule();

	/**
	 * 根据ruleId查找所有图表Id
	 * @param ruleId
	 * @return
	 */
	List<Integer> findVisualIdsByRuleId(int ruleId);
	
	/**
	 * 根据ruleId查询method_name
	 * @param ruleId
	 * @return String
	 */
	String findMethodNameByRid(int ruleId);
	
	/**
	 * 根据入了Id查询argument
	 * @param ruleId
	 * @return
	 */
	List<HashMap<String,Object>> findArgumentsByRuleId(int ruleId);
	
	/**
	 * 查找所有Rule类型
	 * @return
	 */
	List<String> findAllRuleType();
	
	/**
	 * 根据类型查找Rule
	 * @param type 类型
	 * @return
	 */
	List<HashMap<String,Object>> findRuleByType(String type);
	
}
