package cn.edu.cqupt.rubic_business.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;

public interface AttributeDao {
	
	
	List<AttributePo> findAttributesByDid(int dataset_id);
	
	HashMap<String,Object> findAttributeById(int id);
	
	int findIdByName(String attribute_name);
	
	int addAttribute(AttributePo attributePo);
	
	int addAttributeMap(Map<String, Object> map);

	List<AttributePo> findAttributeByResultId(Integer resuletDateset_Id);
	
	int getResultAttributeId();
	
	AttributePo getAttributeById(int attributeId);
	
	String getAttributeNameByDid(int dataset_id);
	
	List<String> getAttributeNamesByDid(int dataset_id);
	
	List<String> getAttributeNamesByRid(int result_id);
}
