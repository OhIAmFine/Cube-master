package cn.edu.cqupt.rubic_business.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.AlgorithmPo;

public interface AlgorithmDao {
	
	List<Map<String,Object>> findAllAlgorithm();
	
	AlgorithmPo findAlgorithmById(int aid);
	
	int addAlgorithm(AlgorithmPo algorithmPo);
	
	int addAlgorithmInMap(Map<String, Object> map);
	
	int findIdByName(String algorithm_name);
	
	int addParameterRelationship(Map<String, Integer> map);
	
	int addKeywordRelationship(Map<String, Integer> map);
	
	int addUserRelationship(Map<String, Integer> map);
	
	List<String> findAllAlgorithmClass();	
	
	List<Map<String,Object>> findAllAlgorithmByClass(String associated_tasks);

	void insertCorrect_rateByAlgorithmId(Map map);
	
	double findCorrect_rateByAlgorithmId(int algorithmId);
	
	public List<AlgorithmPo> findCurrentUserAlgorithms(int currentUserID); 
	
	HashMap<String, Object> findUserIdAndFilePathByAid(int algorithmId);
}
