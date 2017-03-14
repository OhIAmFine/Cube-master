package cn.edu.cqupt.rubic_business.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.DataSetPo;

public interface DataSetDao {
	
	List<DataSetPo> findAllDataSet();
	
	DataSetPo findDataSetById(int dataSet_id);
	
	List<Map<String,Object>> findDataSetByIdReturnMap(int dataSet_id);
	
	int addDataSetMap(Map<String, Object> map); 
	
	int addDataSet(DataSetPo dataSetPo);
	
	int findIdByName(String dataset_name);
	
	int addAttributeRelationship(Map<String, Integer> map);
	
	int addUserRelationship(Map<String, Integer> map);
	
	List<Integer> getAttributesId(int datasetId);
	
	public List<DataSetPo> findCurrentUserDataSet(int currentUserID);
	
	HashMap<String, Object> findUserIdAndFilePathByDid(int dataSetId);
	
	Integer getLabelSequence(int dataSet_id);

	AttributePo getLable(int i);
	
}
