package cn.edu.cqupt.rubic_business.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.ResultdatasetAttributeRelationshipPo;

public interface AttributeService {
	// /**
	// * 根据数据集id查找属性说明
	// * @param dataset_id
	// * @return
	// */
	// String[] findAttributeLabelsByDid(int dataset_id);

	/**
	 * 根据名字获取id
	 * 
	 * @param attribute_name
	 * @return
	 */
	int findIdByName(String attribute_name);
	
	/**
	 * @description 添加attribute
	 * @param map
	 * @return 数据库受影响行数
	 */
	int addAttributeMap(Map<String, Object> map);

	/**
	 * 添加attribute
	 * 
	 * @param attributePo
	 * @return
	 */
	int addAttribute(AttributePo attributePo);
	
	List<AttributePo> getAttributesByDId(int datasetId);
	
	/**
	 * 通过运行结果Id查询，运行结果的attribute
	 * @return
	 */
	public List<HashMap<String, Object>> findAttributeByResultId(Integer resuletDateset_Id);
	
	int getResultAttributeId();
	
	void addResultdatasetAttributeRelation(ResultdatasetAttributeRelationshipPo resultdatasetAttributeRelationshipPo);
	
	List<AttributePo> getAttributesByRId(int resultId);
	
	AttributePo getAttributeById(int attributeId);
	
	/**
	 * 通过属性数组id查找所有属性名
	 * @param attributesId
	 * @return 
	 * @deprecated
	 */
	List<String> getAttributeNameByIds(Integer[] attributesId);
	
	/**
	 * 通过数据集id查找所有属性名
	 * @param datasetId
	 * @return
	 */
	List<String> getAttributeNamesByDid(int datasetId);

}
