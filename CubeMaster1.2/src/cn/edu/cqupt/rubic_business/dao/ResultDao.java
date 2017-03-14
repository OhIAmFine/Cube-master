package cn.edu.cqupt.rubic_business.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.ResultPo;

public interface ResultDao {
	int addResult(ResultPo dataSetPo);

	public List<Integer> findAttribute(Integer resultId);

	ResultPo findResultById(Integer resultId);
	
	int getResultDatasetId();
	
	List<AttributePo> getResultAttributePo(int resultdataset_id);
	
	void addUserResultDatasetRelation(Map<String, Integer> map);

	HashMap<String, Object> findUserIdAndFilePathByDid(int resultId);
	
	List<String> getResultAttributeName(int resultId);
	
}
