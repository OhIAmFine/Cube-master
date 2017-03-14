package cn.edu.cqupt.rubic_business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.eclipse.core.runtime.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.cqupt.rubic_business.Model.po.AlgorithmPo;
import cn.edu.cqupt.rubic_business.Model.po.KeywordPo;
import cn.edu.cqupt.rubic_business.Model.po.ParameterPo;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.dao.AlgorithmDao;
import cn.edu.cqupt.rubic_business.dao.KeywordDao;
import cn.edu.cqupt.rubic_business.dao.ParameterDao;
import cn.edu.cqupt.rubic_business.dao.UserDao;
import cn.edu.cqupt.rubic_business.service.AlgorithmService;
import cn.edu.cqupt.rubic_business.util.MyDateFormat;

@Service("algorithmService")
public class AlgorithmServiceImpl implements AlgorithmService {
	private KeywordDao keywordDao;
	private ParameterDao parameterDao;
	private AlgorithmDao algorithmDao;
	private UserDao userDao;

	@Resource(name = "algorithmDao")
	public void setAlgorithmDao(AlgorithmDao algorithmDao) {
		this.algorithmDao = algorithmDao;
	}

	@Resource(name = "keywordDao")
	public void setKeywordDao(KeywordDao keywordDao) {
		this.keywordDao = keywordDao;
	}

	@Resource(name = "parameterDao")
	public void setParameterDao(ParameterDao parameterDao) {
		this.parameterDao = parameterDao;
	}

	@Resource(name = "userDao")
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public List<Map<String,Object>> findAllAlgorithm() {
		List<Map<String,Object>> algorithms = algorithmDao.findAllAlgorithm();

		return algorithms;
	}

	public AlgorithmPo findAlgorithmById(int aid) {
		AlgorithmPo algorithm = algorithmDao.findAlgorithmById(aid);
		return algorithm;
	}

	public void addAlgorithm(AlgorithmPo algorithmPo) {
		algorithmDao.addAlgorithm(algorithmPo);

	}

	@Override
	public int findIdByName(String algorithm_name) {
		return algorithmDao.findIdByName(algorithm_name);
	}

	@Override
	public int addParameterRelationship(Map<String, Integer> map) {
		return algorithmDao.addParameterRelationship(map);
	}

	@Override
	public int addKeywordRelationship(Map<String, Integer> map) {
		return algorithmDao.addKeywordRelationship(map);
	}

	@Override
	public int addUserRelationship(Map<String, Integer> map) {
		return algorithmDao.addUserRelationship(map);
	}

	@Override
	public List<Map<String, Object>> findAllAlgorithmAndInfo() {
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();

		List<Map<String,Object>> algorithms = algorithmDao.findAllAlgorithm();
		for (Map<String,Object> algorithm : algorithms) {
			int algorithm_id = (Integer) algorithm.get("algorithm_id");
			Date date = (Date) algorithm.get("submit_datetime");
			algorithm.put("submit_datetime", MyDateFormat.changeDateToLongString(date));

			List<KeywordPo> keywords = keywordDao
					.findKeywordByAid(algorithm_id);
			List<ParameterPo> parameters = parameterDao
					.findParameterByAid(algorithm_id);

			int user_id = userDao.getUserIdByAid(algorithm_id);
			String userName = userDao.getUserNameById(user_id);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("algorithm", algorithm);
			map.put("keywords", keywords);
			map.put("parameters", parameters);
			map.put("user_name", userName);
			maps.add(map);
		}
		return maps;
	}

	@Override
	public Map<String, Object> findAllAlgorithmAndInfoByAid(int algorithm_id) {
		Map<String, Object> map = new HashMap<String, Object>();

		AlgorithmPo algorithm = algorithmDao.findAlgorithmById(algorithm_id);
		
		
		List<KeywordPo> keywords = keywordDao.findKeywordByAid(algorithm_id);
		List<ParameterPo> parameters = parameterDao
				.findParameterByAid(algorithm_id);

		int user_id = userDao.getUserIdByAid(algorithm_id);
		String userName = userDao.getUserNameById(user_id);

		map.put("algorithm", algorithm);
		map.put("keywords", keywords);
		map.put("parameters", parameters);
		map.put("user_name", userName);

		
		return map;
	}

	@Override
	public List<String> findAllAlgorithmClass() {
		return algorithmDao.findAllAlgorithmClass();
	}

	@Override
	public List<Map<String,Object>> findAllAlgorithmByClass(String associated_tasks) {
		return algorithmDao.findAllAlgorithmByClass(associated_tasks);
	}

	@Override
	public List<Map<String, Object>> findAllAlgorithmAndInfoByClass(
			String associated_tasks) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List<Map<String,Object>> algorithmPoList = findAllAlgorithmByClass(associated_tasks);

		for (Map<String,Object> algorithm : algorithmPoList) {
			int algorithm_id = (Integer) algorithm.get("algorithm_id");
			int user_id = userDao.getUserIdByAid(algorithm_id);
			
			Date date = (Date) algorithm.get("submit_datetime");
			algorithm.put("submit_datetime", MyDateFormat.changeDateToLongString(date));
			
			String userName = userDao.getUserNameById(user_id);
			List<KeywordPo> keywordPoList = keywordDao
					.findKeywordByAid(algorithm_id);
			List<ParameterPo> parameterPoList = parameterDao
					.findParameterByAid(algorithm_id);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("algorithm", algorithm);
			map.put("keywords", keywordPoList);
			map.put("parameter", parameterPoList);
			map.put("user_name", userName);

			list.add(map);
		}

		return list;
	}

	@Override
	public Map<String, List<Map<String, Object>>> findAllAlgorithmAndInfoOrderByClass() {
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();

		List<String> classList = algorithmDao.findAllAlgorithmClass();

		for (String associated_tasks : classList) {
			List<Map<String, Object>> allAlgorithmAndInfoList = findAllAlgorithmAndInfoByClass(associated_tasks);
			map.put(associated_tasks, allAlgorithmAndInfoList);
		}

		return map;
	}

	@Override
	public void insertCorrect_rateByAlgorithmId(int algorithmId,double correct_rate) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("algorithmId", algorithmId);
		map.put("correct_rate", correct_rate);
		algorithmDao.insertCorrect_rateByAlgorithmId(map);
		
	}

	@Override
	public double findCorrect_rateByAlgorithmId(int algorithmId) {
		return algorithmDao.findCorrect_rateByAlgorithmId(algorithmId);
		
	}

	@Override
	public Map<String, List<Map<String,Object>>> findCurrentUserAlgorithms(UserPo user) {
		Assert.isNotNull(user, "UserPo must not be null");
		
		String userName = user.getUser_name();
		List<AlgorithmPo> list = null;
		//数据库操作：查询
		list = algorithmDao.findCurrentUserAlgorithms(user.getUser_id());
		
		List<Map<String, Object>> listAll = new ArrayList<Map<String, Object>>();
		
		//把algorithm、userName、algorithmID封装到一个HashMap里面
		int len = list.size();
		for(int i = 0; i < len; i++){
			AlgorithmPo algorithmPo = list.get(i);
			Map<String, Object> map = new HashMap<String, Object>();
			int algorithm_id = algorithmPo.getAlgorithm_id();
			//数据库操作：查询
			List<KeywordPo> keywordPoList = keywordDao.findKeywordByAid(algorithm_id);
			List<ParameterPo> parameters = parameterDao.findParameterByAid(algorithm_id);
			
			map.put("algorithm", algorithmPo);
			map.put("user_name", userName);
			map.put("keywords", keywordPoList);
			map.put("parameter", parameters);
			
			String timeString = MyDateFormat.changeDateToLongString(algorithmPo.getSubmit_datetime());
			map.put("submit_datetime", timeString);
			listAll.add(map);
		}
		
		/**
		 * 根据 Associated_tasks来分类
		 * 下面的HashMap中的key是Associated_tasks，value是上面的map
		 * 总体的封装结构：Map<String, List<Map<String,Object>>>
		 */
		Map<String, List<Map<String,Object>>> alMap = null;
		alMap = new HashMap<String, List<Map<String,Object>>>();
		List<Map<String,Object>> tempList = null;
		String tempAC = "";
		for(int i = 0; i < len; i++){
			String temp = list.get(i).getAssociated_tasks();
			if(!tempAC.equals(temp)){
				tempAC = temp;
				tempList = new ArrayList<Map<String,Object>>();
				alMap.put(temp, tempList);
			}
			tempList.add(listAll.get(i));
		}
		
		return alMap;
	}

	@Override
	public int addAlgorithmInMap(Map<String, Object> map) {
		return algorithmDao.addAlgorithmInMap(map);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public void addAlgorithm(Map<String, Object> map) {
		int userId = Integer.valueOf(String.valueOf(map.get("userId")));
		Map<String, Object> algorithm = (Map<String, Object>) map.get("algorithm");
		algorithm.put("submit_datetime", new Date());
		List<Map<String, String>> keyword = (List<Map<String, String>>) map.get("keyword");
		List<Map<String, String>> parameter = (List<Map<String, String>>) map.get("parameter");
		
		/**
		 * 插入算法
		 */
		algorithmDao.addAlgorithmInMap(algorithm);
		int algorithmId = Integer.valueOf(String.valueOf(algorithm.get("algorithm_id")));
		
		/**
		 * 插入用户算法关系表
		 */
		Map<String, Integer> userRelationship = new HashMap<String, Integer>();
		userRelationship.put("user_id", userId);
		userRelationship.put("algorithm_id", algorithmId);
		algorithmDao.addUserRelationship(userRelationship);
		
		/**
		 * 插入关键字和关系表
		 */
		for(Map<String, String> keyMap : keyword) {
			keywordDao.addKeywordInMap(keyMap);
			int keywordId = Integer.valueOf(String.valueOf(keyMap.get("keyword_id")));
			Map<String, Integer> keyRelationship= new HashMap<String, Integer>();
			keyRelationship.put("algorithm_id", algorithmId);
			keyRelationship.put("keyword_id", keywordId);
			algorithmDao.addKeywordRelationship(keyRelationship);
		}
		
		/**
		 * 插入参数和关系表
		 */
		for(Map<String, String> parameterMap : parameter) {
			parameterDao.addParameterInMap(parameterMap);
			int parameterId = Integer.valueOf(String.valueOf(parameterMap.get("parameter_id")));
			Map<String, Integer> parameterRelationship = new HashMap<String, Integer>();
			parameterRelationship.put("algorithm_id", algorithmId);
			parameterRelationship.put("parameter_id", parameterId);
			algorithmDao.addParameterRelationship(parameterRelationship);
		}
	}

	@Override
	public HashMap<String, Object> findUserIdAndFilePathByAid(int algorithmId) {
		
		return algorithmDao.findUserIdAndFilePathByAid(algorithmId);
	}

} 
