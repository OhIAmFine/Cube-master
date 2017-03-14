package cn.edu.cqupt.rubic_business.service;

import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.ParameterPo;

public interface ParameterService {
	
	double[] findParameterByAid(int algorithm_id);
	
	int addParameter(ParameterPo parameterPo);
	
	int addParameterInMap(Map<String, String> map);
	
	int findIdByName(String parameter_name);
	
	List<ParameterPo> findParametersByAid(int algorithm_id);
}
