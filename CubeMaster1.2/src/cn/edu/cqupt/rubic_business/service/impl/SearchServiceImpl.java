package cn.edu.cqupt.rubic_business.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.edu.cqupt.rubic_business.dao.SearchDao;
import cn.edu.cqupt.rubic_business.service.SearchService;



/**
 * 用法 key不能变，value拼接
 * 		Map<String, String> map=new HashMap<String, String>();
 *    	map.put("table_name", "model_api,...");
    
    	map.put("request_page_number", String.valueOf(requestPageNumber));
    	map.put("conditions", "second_level_id=2 and ... ");
    	map.put("per_page_number", String.valueOf(perPageNumber));
 * */
@Service
public class SearchServiceImpl implements SearchService {
    
	@Autowired
	private SearchDao searchDao;
	@Override
	public JSONObject search(Map<String, String> map) {
		// TODO Auto-generated method stub
		JSONObject result=new JSONObject();
		result.put("total_page_number",(int)Math.ceil(searchDao.selectPageCount(map)*1.0/Integer.parseInt(map.get("per_page_number"))));
		int startNum=(Integer.parseInt(map.get("request_page_number"))-1)*Integer.parseInt(map.get("per_page_number"));
		map.put("request_page_number", startNum+"");
		result.put("result",searchDao.findByKeywords(map));
		return result;
	}

	
	
}
