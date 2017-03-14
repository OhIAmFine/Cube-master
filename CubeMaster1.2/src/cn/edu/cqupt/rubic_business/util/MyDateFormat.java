package cn.edu.cqupt.rubic_business.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

//import org.mortbay.util.ajax.JSON;

import cn.edu.cqupt.rubic_business.Model.po.DataSetPo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MyDateFormat {

	public static String DATE_KEYWORD = "submit_datetime";
	
	/**
	 * 将date类型转化为带时分秒字符串
	 * @param date
	 * @return
	 * @author LiangYH
	 */
	public static String changeDateToLongString(java.util.Date date) {
		if(date != null) {
			String dateStr = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateStr = sdf.format(date);
			return dateStr;
		}else {
			return null;
		}
	}

	/**
	 * 将map里面的submit_datetime(Date)转化成String
	 * @param map
	 * @author LiuMian
	 */
	public static Map<String,Object> formatDate(Map<String,Object> map){

		Object obj = map.get(DATE_KEYWORD);
		Date date = null;
		if(obj.getClass() == Long.class){
			date = new Date((Long)obj);
		} else{
			date = (Date) obj;
		}
		map.put(DATE_KEYWORD, formatDate(date));
		return map;
	}

	/**
	 * 将JSONObject里面的submit_datetime(Date)转化成String
	 * @param obj
	 * @return
	 * @author LiuMian
	 */
	public static JSONObject formatAlgorithmDate(JSONObject jsonObj){
		jsonObj = formatJSONObject(jsonObj);
		Map<String,Object> toFormat = null;
		toFormat = (JSONObject) jsonObj.get("algorithm");
		if(toFormat != null){
			toFormat = formatDate(toFormat);
			jsonObj.put("algorithm", toFormat);
		}
		return jsonObj;
	}
	
	/**
	 * 格式化DataSetPo的时间类型为String类型
	 * @param dataSet
	 * @return 
	 */
	public static Map<String,Object> formatDataSetDate(DataSetPo dataSet){
		String text = JSON.toJSONString(dataSet);
		Map<String,Object> dataSetMap = JSONObject.parseObject(text);
		return formatDate(dataSetMap);
	}

	/**
	 * 将obj的所有属性的类型都转换为String
	 * @param Obj
	 * @return
	 */
	public static JSONObject formatJSONObject(JSONObject obj){
		return JSONObject.parseObject(obj.toString());
	}
	
	/**
	 * 将Date转换成String
	 * @author LiuMian
	 * @param forFormat date
	 * @return 时间格式的字符串
	 */
	public static String formatDate(Date forFormat){
		if(forFormat != null) {
			String dateStr = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateStr = sdf.format(forFormat);
			return dateStr;
		}else {
			return null;
		}
	}
}
