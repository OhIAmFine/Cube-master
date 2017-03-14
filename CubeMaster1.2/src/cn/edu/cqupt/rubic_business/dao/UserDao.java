package cn.edu.cqupt.rubic_business.dao;

import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.rubic_business.Model.po.UserPo;

public interface UserDao {
	UserPo findUserByEmail(String email);
	
	UserPo findUserById(int user_id);
	
	int findIdByName(String user_name);
	
	int findIdByEmail(String user_email);
	
	int getUserIdByAid(int algorithm_id);
	
	String getUserNameByAid(int algorithm_id);
	
	int getUserIdByDid(int dataset_id);
	
	int getUserIdByRid(int resultdataset_id);

	String getUserNameById(int user_id);
	
	int addUser(UserPo userPo);
	
	List<HashMap<String, Object>> getProcessRecord(int user_id);

}
