package cn.edu.cqupt.rubic_business.dao;

import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.ProcessRecordPo;

/**
 * 
 * @author He Huangqin
 *
 */
public interface ProcessRecordDao {

	int addProcessRecord(ProcessRecordPo processRecord); 
	
	int getProcessRecordId();
	
	ProcessRecordPo getProcessRecordPo(int process_id);
	
	ProcessRecordPo getProcessRecordPoByResultId(int resultId);
	
	void DeleteProcessRecord(int process_id);
	
	/**
	 * 更新算法运行记录
	 * @param processRecord
	 * @author LiangYH
	 */
	void updateProcessRecordByID(ProcessRecordPo processRecord);
	
	/**
	 * 更新运行状态
	 * key：run_state、processRecordID
	 * @param map
	 */
	void updateProcessRunState(Map<String,String> map);
	
	/**
	 * @description:更加用户Id查运行记录
	 * @author hey
	 * @param userId
	 * @return
	 */
	List<ProcessRecordPo> findAllProcessRecordByUid(int userId);
}
