package cn.edu.cqupt.rubic_business.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;



import cn.edu.cqupt.rubic_business.Model.po.AlgorithmPo;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;

public interface AlgorithmService {
	
	/**
	 * @description 添加算法
	 * @param map
	 * @return 数据库受影响行数
	 */
	int addAlgorithmInMap(Map<String, Object> map);
	
	/**
	 * @description 添加算法 事物
	 * @param map
	 */
	void addAlgorithm(Map<String, Object> map);
	
	Map<String,Object> findAllAlgorithmAndInfoByAid(int algorithm_id);
	/**
	 * 查找左右算法的信息
	 * @return
	 */
	List<Map<String,Object>> findAllAlgorithmAndInfo();
	/**
	 * 查找所有算法
	 * @return
	 */
	List<Map<String,Object>> findAllAlgorithm();
	/**
	 * 根据aid查询算法
	 * @param aid
	 * @return
	 */
	AlgorithmPo findAlgorithmById(int aid);
	/**
	 * 增加算法
	 * @param algorithmPo
	 */
	void addAlgorithm(AlgorithmPo algorithmPo);

	/**
	 * @description 根据名字查询algorithm_id
	 * @param algorithm_name
	 * @return 受影响的行数
	 */
	int findIdByName(String algorithm_name);
	
	/**
	 * @description 添加算法参数关系
	 * @param algorithm_id
	 * @param parameter_id
	 * @return 受影响的行数
	 */
	int addParameterRelationship(Map<String, Integer> map);
	
	/**
	 * @description 添加算法关键字关系
	 * @param algorithm_id
	 * @param keyword_id
	 * @return 受影响的行数
	 */
	int addKeywordRelationship(Map<String, Integer> map);
	
	/**
	 * @description 添加算法用户关系
	 * @param map
	 * @return
	 */
	int addUserRelationship(Map<String, Integer> map);
	
	/**
	 * @description 查询算法所有类型 聚类|分类|..
	 * @return List<Stirng>
	 */
	List<String> findAllAlgorithmClass();
	
	/**
	 * 根据类型返回所有算法
	 * @param associated_tasks
	 * @return
	 */
	List<Map<String,Object>> findAllAlgorithmByClass(String associated_tasks);
	
	/**
	 * 根据类型返回所有算法的所有信息
	 * @param associated_tasks
	 * @return
	 */
	List<Map<String, Object>> findAllAlgorithmAndInfoByClass(String associated_tasks);
	
	/**
	 * 返回所有算法所有信息以类型分组
	 * @return
	 */
	Map<String, List<Map<String, Object>>> findAllAlgorithmAndInfoOrderByClass();
	/**
	 * 根据id插入正确率
	 */
	void insertCorrect_rateByAlgorithmId(int algorithmId,double correct_rate);
	/**
	 * 根据算法id查找正确率
	 * @param algorithmId
	 * @return
	 */
	double findCorrect_rateByAlgorithmId(int algorithmId);
	
	/**
	 * 获取当前用户的算法
	 * @param currentUserID
	 * @return
	 * @author LiangYH
	 */
	Map<String, List<Map<String,Object>>> findCurrentUserAlgorithms(UserPo user); 
	
	/**
	 * 获取算法的作者Id和filepath
	 * @param algorithmId
	 * @return
	 */
	HashMap<String, Object> findUserIdAndFilePathByAid(int algorithmId);
}
