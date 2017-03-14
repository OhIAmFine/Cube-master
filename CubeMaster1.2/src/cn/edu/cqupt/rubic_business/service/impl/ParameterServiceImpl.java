package cn.edu.cqupt.rubic_business.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.cqupt.rubic_business.Model.po.ParameterPo;
import cn.edu.cqupt.rubic_business.dao.ParameterDao;
import cn.edu.cqupt.rubic_business.service.ParameterService;

@Service("parameterService")
public class ParameterServiceImpl implements ParameterService {
	private ParameterDao parameterDao;


	@Resource(name="parameterDao")
	public void setParameterService(ParameterDao parameterDao) {
		this.parameterDao = parameterDao;
	}


	@Override
	public double[] findParameterByAid(int algorithm_id) {
		List<ParameterPo> parameterPos=parameterDao.findParameterByAid(algorithm_id);
		double[] parameters=new double[parameterPos.size()];
		for(int i=0;i<parameters.length;i++){
			String value=parameterPos.get(i).getParameter_value();
			parameters[i]=Double.parseDouble(value);
		}
		return parameters;
	}


	@Override
	public int addParameter(ParameterPo parameterPo) {
		return parameterDao.addParameter(parameterPo);
	}

	@Override
	public int addParameterInMap(Map<String, String> map) {
		return parameterDao.addParameterInMap(map);
	}

	@Override
	public int findIdByName(String parameter_name) {
		return parameterDao.findIdByName(parameter_name);
	}


	@Override
	public List<ParameterPo> findParametersByAid(int algorithm_id) {
		return parameterDao.findParameterByAid(algorithm_id);
	}
	
}
