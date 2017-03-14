package cn.edu.cqupt.rubic_business.dao;

import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.ParameterPo;

public interface ParameterDao {
	
	List<ParameterPo> findParameterByAid(int algorithm_id);
	
	/**
	 * @description 添加参数 数组形式
	 * @param map
	 * @return 数据库受影响行数
	 */
	int addParameterInMap(Map<String, String> map);
	
	int addParameter(ParameterPo parameterPo);
	
	int findIdByName(String parameter_name);
	
}
