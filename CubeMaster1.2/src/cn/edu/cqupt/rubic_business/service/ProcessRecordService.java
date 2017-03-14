package cn.edu.cqupt.rubic_business.service;

import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.ProcessRecordPo;

/**
 * 
 * @author he GuangQin
 *
 */
public interface ProcessRecordService {
	
	/**
	 * 从process_record表删除记录
	 * @param process_id
	 */
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
	 * @author LiangYH
	 */
	void updateProcessRunState(Map<String,String> map);
	
	/**
	 * @description:通过用户查运行记录
	 * @author hey
	 * @param userId
	 * @return
	 */
	List<ProcessRecordPo> findAllProcessRecordByUid(int userId);

}
