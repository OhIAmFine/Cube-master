package cn.edu.cqupt.rubic_business.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.DataSetPo;
import cn.edu.cqupt.rubic_business.Model.po.ProcessRecordPo;
import cn.edu.cqupt.rubic_business.Model.po.ResultPo;

public interface ResultService {
	int insertResult(ResultPo dataSetPo);

	int addProcessRecord(ProcessRecordPo processRecord);
	
	ResultPo findResultById(Integer resultId);
	
	int getResultDatasetId();
	
	int getDataSetId(int resultId);
	
	int getProcessRecordId();
	
	ProcessRecordPo getProcessRecordPo(int process_id);
	
	ProcessRecordPo getProcessRecordPoByResultId(int resultId);
	
	List<AttributePo> getResultAttributePo(int result_id);
	
	List<AttributePo> getAttributes(int resultId);
	
	void addUserResultDatasetRelation(Map<String, Integer> map);

	HashMap<String, Object> findUserIdAndFilePathByDid(int resultId);
	
	List<String> getResultAttributeName(int resultId);
}
