package cn.edu.cqupt.rubic_business.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.DataSetPo;
import cn.edu.cqupt.rubic_business.Model.po.ProcessRecordPo;
import cn.edu.cqupt.rubic_business.Model.po.ResultPo;
import cn.edu.cqupt.rubic_business.dao.AttributeDao;
import cn.edu.cqupt.rubic_business.dao.ProcessRecordDao;
import cn.edu.cqupt.rubic_business.dao.ResultDao;
import cn.edu.cqupt.rubic_business.service.ResultService;
@Service("resultService")
public class ResultServiceImpl implements ResultService {
	private ResultDao resultDao;
	private AttributeDao attributeDao;
	private ProcessRecordDao processRecordDao;
	
	@Resource(name="resultDao")
	public void setResultDao(ResultDao resultDao) {
		this.resultDao = resultDao;
	}

	@Resource(name="processRecordDao")
	public void setProcessRecordDao(ProcessRecordDao processRecordDao) {
		this.processRecordDao = processRecordDao;
	}
	
	@Resource(name="attributeDao")
	public void setAttributeDao(AttributeDao attributeDao) {
		this.attributeDao = attributeDao;
	}


	@Override
	public int insertResult(ResultPo dataSetPo) {
		resultDao.addResult(dataSetPo);
		return dataSetPo.getResultdataset_id();		
	}
	
	@Override
	public int addProcessRecord(ProcessRecordPo processRecord){
		processRecordDao.addProcessRecord(processRecord);
		//返回刚刚插入的process的processID
		return processRecord.getProcess_id();
	}

	@Override
	public ResultPo findResultById(Integer resultId) {
		
		return resultDao.findResultById(resultId);
	}
	
	
	public int getResultDatasetId() {
		return resultDao.getResultDatasetId();
	}
	
	@Override
	public int getDataSetId(int resultId) {
		String details = processRecordDao.getProcessRecordPoByResultId(resultId).getJson_detail();
		int datasetId = Integer.parseInt(details.split(",")[2].split(":")[1]);
		return datasetId;
	}

	@Override
	public int getProcessRecordId() {
		
		return processRecordDao.getProcessRecordId();
	}

	@Override
	public ProcessRecordPo getProcessRecordPo(int process_id) {
		
		return processRecordDao.getProcessRecordPo(process_id);
	}

	@Override
	public List<AttributePo> getResultAttributePo(int result_id) {
		return resultDao.getResultAttributePo(result_id);
	}

	@Override
	public List<AttributePo> getAttributes(int resultId) {
		//ProcessRecordPo process = processRecordDao.getProcessRecordPoByResultId(resultId);
		//int datasetId = Integer.valueOf(process.getJson_detail().split(",")[2].split(":")[1]);
		List<AttributePo> list = attributeDao.findAttributesByDid(resultId);
		//list.add(getResultAttributePo(resultId));
		return list;
	}

	@Override
	public ProcessRecordPo getProcessRecordPoByResultId(int resultId) {
		return processRecordDao.getProcessRecordPoByResultId(resultId);
	}

	@Override
	public void addUserResultDatasetRelation(Map<String, Integer> map) {
		resultDao.addUserResultDatasetRelation(map);
		
	}

	@Override
	public HashMap<String, Object> findUserIdAndFilePathByDid(int resultId) {
		return resultDao.findUserIdAndFilePathByDid(resultId);
	}
	
	@Override
	public List<String> getResultAttributeName(int resultId) {
		return resultDao.getResultAttributeName(resultId);
	}

}
