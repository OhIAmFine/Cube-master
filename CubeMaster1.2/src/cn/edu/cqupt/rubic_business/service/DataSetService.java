package cn.edu.cqupt.rubic_business.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.DataSetPo;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;

public interface DataSetService {
	
	Map<String,Object> findAllDataSetAndInfoByDid(int dataSet_id);
	
	List<Map<String,Object>> findAllDataSetAndInfo();
	/**
	 * 查询所有数据
	 *
	 * @return
	 */
	List<DataSetPo> findAllDataSet();

	/**
	 * 根据dataset_id查找数据
	 * 
	 * @param dataset_id
	 * @return
	 */
	DataSetPo findDataSetById(int dataset_id);
	
	/**
	 * 根据dataset_id查找数据返回List<Map<String,Object>>
	 * 
	 * @param dataset_id
	 * @return List<Map<String,Object>>
	 */
    List<Map<String,Object>> findDataSetByIdReturnMap(int dataset_id);
	
	/**
	 * 根据一组数据的dataset_id查找一组数据
	 * 
	 * @param dataset_ids
	 * @return
	 */
	List<DataSetPo> findDataSetsByIds(int[] dataset_ids);

	/**
	 * @description 添加数据 事物
	 * @param map
	 */
	void addDataSet(Map<String, Object> map);
	
	/**
	 * @description 添加数据
	 * @param map
	 * @return 数据库受影响行数
	 */
	int addDataSetMap(Map<String, Object> map);
	
	/**
	 * 添加数据
	 * 
	 * @param dataSetPo
	 */
	void addDataSet(DataSetPo dataSetPo);
	
	/**
	 * 根据名字查询id
	 * @param dataset_name
	 * @return
	 */
	int findIdByName(String dataset_name);
	
	/**
	 * @description 添加数据集属性关系
	 * @param map
	 * @return
	 */
	int addAttributeRelationship(Map<String, Integer> map);
	
	/**
	 * @description 添加数据集用户关系
	 * @param map
	 * @return
	 */
	int addUserRelationship(Map<String, Integer> map);
	
	/**
	 * @description 根据Id查询属性
	 * @param datasetId
	 * @return
	 */
	List<Integer> getAttributesId(int datasetId);
	
	/**
	 * 得到当前用户的所有数据集
	 * @param currentUser
	 * @return
	 * @author LiangYH
	 */
	List<Map<String,Object>> findCurrentUserDataSet(UserPo currentUser);
	
	HashMap<String, Object> findUserIdAndFilePathByDid(int dataSetId);
	
	/**
	 * @description 根据数据Id查询label序列
	 * @param dataSet_id
	 * @return
	 */
	Integer getLabelSequence(int dataSet_id);
	
	/**
	 * 根据数据集的id获取数据集Lable
	 * @param i
	 * @return
	 */
	AttributePo getLable(int i);

}
