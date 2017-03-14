package cn.edu.cqupt.rubic_business.util;
/**
 * @description TODO
 * @author Wong JW
 * @date 2015年11月18日 下午9:34:56 
 * @version 1.0 
 */
public abstract class RegexUtils {
	
	/** 逗号 */
	public static final String COMMA = ",";
	
	/** 匹配开头结尾或中间的逗号和|或空格 */
	public static final String COMMA_BLANK_REGEX = "\\,\\s{0,}|\\s{1,}";

}
