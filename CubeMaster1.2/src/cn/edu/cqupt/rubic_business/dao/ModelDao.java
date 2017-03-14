package cn.edu.cqupt.rubic_business.dao;

import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.ModelPo;
/**
 * <p>Title: ModelDao
 * <p>Description: 运行模型Dao接口</p>
 * @author Hey
 * @data 2015-11-13
 */
public interface ModelDao {
	// 插入模型
	Integer insretModel(ModelPo model) throws Exception;

	// 修改模型
	void updateModel(ModelPo model) throws Exception;

	// 删除模型
	void deleteModel(Integer mid) throws Exception;

	// 根据模型Id查找模型
	ModelPo findModelById(Integer mid) throws Exception;

	// 根据用户Id查找模型
	List<ModelPo> findModelByUserId(Integer uid) throws Exception;

	// 更加算法Id查找模型
	List<ModelPo> findModelByAid(Integer aid) throws Exception;
	
	//根据用户id查找模型 返回部分信息
	List<Map<String,Object>> findAllModelByUserId(int userId);
}
