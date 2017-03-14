package cn.edu.cqupt.rubic_business.servlet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @description 反射处理类 根据属性名称(字符串) 设置属性
 * @author wnagjw
 * @created 2015-5-31 上午11:25:36
 */
public class ReflectHandler {

	/**
	 * 把obj对象field属性的值设为value
	 * 
	 * @param obj
	 * @param field
	 * @param value
	 * @throws ParseException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public static void set(Object obj, String fieldName, String value)
			throws ParseException, IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {

		// 验证
		if (!checkField(obj, fieldName)) {
			return;
		}
		// 获得对象属性
		Field field = obj.getClass().getDeclaredField(fieldName);

		// 设置可获得
		field.setAccessible(true);

		//获得属性类型
		String type = field.getType().toString();
		
		if(type.equals("int")){	//整形
			int val = Integer.valueOf(value);
			field.set(obj, val);
		}else {		//字符串 
			field.set(obj, value);
		}
	}

	/**
	 * 验证属性名是否存在
	 * 
	 * @param obj
	 * @param fieldName
	 * @return 存在 true
	 */
	public static boolean checkField(Object obj, String fieldName) {
		Field[] fields = obj.getClass().getDeclaredFields();
		//遍历属性名
		for (Field field : fields) {
			String name = field.getName();
			if (name.equals(fieldName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取所有属性
	 * @param obj
	 * @return
	 */
	public static Field[] getFields(Object obj) {
		return obj.getClass().getDeclaredFields();
	}
	
	/**
	 * 获取所有属性名
	 * @param obj
	 * @return
	 */
	public static String[] getFiledName(Object obj) {
		Field[] fields = getFields(obj);
		int length = fields.length;
		String[] str = new String[length];
		for(int i = 0; i < length; i++) {
			str[i] = fields[i].getName();
		}
		return str;
	}
	
	/**
	 * 根据属性名获取
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static String getFieldValue(Object obj, String fieldName) {
		String name = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
		String getter = "get" + name;
		Object result = null;
		try {
			Method method = obj.getClass().getMethod(getter);
			result = method.invoke(obj, new Object[]{});
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(result != null) {
			if(name.endsWith("datetime")) {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return formatter.format(result);
			}
			return result.toString();
		} else {
			return "";
		}
	}
	
	/**
	 * 根据属性名获取返回ProcessRecord中的属性值
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static String getProcessRecordFieldValue(Object obj, String fieldName) {
		String name = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
		String getter = "get" + name;
		Object result = null;
		try {
			Method method = obj.getClass().getMethod(getter);
			result = method.invoke(obj, new Object[]{});
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(result != null) {
			if(name.equals("Process_start")||name.equals("Process_end")) {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return formatter.format(result);
			}
			return result.toString();
		} else {
			return "";
		}
	}

}
