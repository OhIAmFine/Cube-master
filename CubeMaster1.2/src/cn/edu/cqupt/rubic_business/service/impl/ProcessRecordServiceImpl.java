package cn.edu.cqupt.rubic_business.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.cqupt.rubic_business.Model.po.ProcessRecordPo;
import cn.edu.cqupt.rubic_business.dao.ProcessRecordDao;
import cn.edu.cqupt.rubic_business.service.ProcessRecordService;

/**
 * 
 * @author he GuangQin
 *
 */
@Service("processRecordService")
public class ProcessRecordServiceImpl implements ProcessRecordService {
	
	private ProcessRecordDao processRecordDao;
	
	@Resource(name="processRecordDao")
	public void setProcessRecordDao(ProcessRecordDao processRecordDao) {
		this.processRecordDao = processRecordDao;
	}

	@Override
	public void DeleteProcessRecord(int process_id) {
		
		processRecordDao.DeleteProcessRecord(process_id);

	}

	@Override
	public void updateProcessRecordByID(ProcessRecordPo processRecord) {
		processRecordDao.updateProcessRecordByID(processRecord);
	}

	@Override
	public void updateProcessRunState(Map<String, String> map) {
		processRecordDao.updateProcessRunState(map);
	}
	
	@Override
	public List<ProcessRecordPo> findAllProcessRecordByUid(int userId) {
		
		return processRecordDao.findAllProcessRecordByUid(userId);
	}
	
}
