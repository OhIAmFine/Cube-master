package cn.edu.cqupt.rubic_business.service;

import java.util.HashMap;
import java.util.List;

import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.service.impl.LoginException;

public interface UserService {
	UserPo Login(String email,String password) throws LoginException;
	
	boolean validateName(String name);
	
	boolean validateEmail(String email);
	
	int addUser(UserPo userPo);
	
	List<HashMap<String, Object>> getProcessRecord(int user_id);
	
	int getUserIdByAid(int algorithm_id);
	
	String getUserNameByAid(int algorithm_id);
	
	int getUserIdByDid(int dataset_id);
	
	int getUserIdByRid(int result_id);
	
	String getUserNameById(int user_id);
	
	UserPo findUserById(Integer uid);
}