package cn.edu.cqupt.rubic_business.dao;

import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.APIAttributePo;


/**
 * Created by LY on 2015/12/6.
 */
public interface APIAttributeDao {

       List<String> getAllAttributeType();
    
	   List<APIAttributePo> getApiAttributeByApiId(int apiid);
	
	   List<Map<String,Object>> getAttributeByApiIdReturnMap(int apiid);
	   
	   Map<String,Object> getAllAttributeByApiIdReturnMap(int apiid);
	
}
