package cn.edu.cqupt.rubic_business.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.dao.UserDao;
import cn.edu.cqupt.rubic_business.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
	private UserDao userDao;

	@Resource(name = "userDao")
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserPo Login(String email, String password) throws LoginException {
		UserPo userPo = userDao.findUserByEmail(email);
		if (userPo == null) {
			throw new LoginException("该用户不存在");
		} else if (!userPo.getPassword().equals(password)) {
			throw new LoginException("密码错误");
		}
		return userPo;
	}

	@Override
	public boolean validateName(String name) {
		int userNum = userDao.findIdByName(name);
		if (userNum == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean validateEmail(String email) {
		int userNum = userDao.findIdByEmail(email);
		if (userNum == 0) {
			return true;
		}
		return false;
	}

	@Override
	public int addUser(UserPo userPo) {
		return userDao.addUser(userPo);
	}

	@Override
	public List<HashMap<String, Object>> getProcessRecord(int user_id) {
		return userDao.getProcessRecord(user_id);
	}

	@Override
	public int getUserIdByAid(int algorithm_id) {
		return userDao.getUserIdByAid(algorithm_id);
	}

	@Override
	public String getUserNameByAid(int algorithm_id) {
		return userDao.getUserNameByAid(algorithm_id);
	}
	
	@Override
	public int getUserIdByDid(int dataset_id) {
		int user_id = userDao.getUserIdByDid(dataset_id);
		return user_id;
	}

	@Override
	public int getUserIdByRid(int result_id) {
		return userDao.getUserIdByRid(result_id);
	}
	
	@Override
	public String getUserNameById(int user_id) {
		return userDao.getUserNameById(user_id);
	}

	@Override
	public UserPo findUserById(Integer uid) {

		return userDao.findUserById(uid);
	}

}
