package cn.edu.cqupt.rubic_business.service;

import java.util.List;

import cn.edu.cqupt.rubic_business.Model.po.ModelPo;
import cn.edu.cqupt.rubic_framework.algorithm_interface.OperationalData;
import cn.edu.cqupt.rubic_framework.model.MyStruct;

/**
 * <p>Title: ModelService
 * <p>Description: 运行service层接口</p>
 * @author Hey
 * @data 2015-11-13
 */
public interface ModelService {
	
	public void setOperationalData(OperationalData operationalData);

	// 创建模型
	Integer createModel(int algorithmId, int dataId, int userId)
			throws Exception;

	// 修改模型
	void trainModel(ModelPo model) throws Exception;

	// 删除模型
	void deleteModel(Integer modelId) throws Exception;

	// 根据模型Id查找模型
	ModelPo findModelById(Integer modelId) throws Exception;

	// 根据用户Id查找模型
	List<ModelPo> findModelByUserId(Integer userId) throws Exception;

	// 更加算法Id查找模型
	List<ModelPo> findModelByAid(Integer algorithmId) throws Exception;

	// useringModel
	public MyStruct[] useringModel(OperationalData operationalData,
			MyStruct[] input, int mid) throws Exception;
}
