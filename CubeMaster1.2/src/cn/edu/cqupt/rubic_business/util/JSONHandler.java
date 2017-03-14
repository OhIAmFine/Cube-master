package cn.edu.cqupt.rubic_business.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;

/**
 * @description JSON处理类
 * @author Wong JW
 * @date 2015年10月29日 下午7:17:47 
 * @version 1.0 
 */
public class JSONHandler {
	
	/**
	 * @description
	 * @param jsonObj
	 * @return 
	 */
	public static Map<String, Object> parseJSONToMap(JSONObject jsonObj) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		for(Iterator<Entry<String, Object>> itor = jsonObj.entrySet().iterator(); itor.hasNext(); ) {
			Entry<String, Object> entry = itor.next();
			map.put(entry.getKey(), entry.getValue());
		}
		
		return map;
	}

}
