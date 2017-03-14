package cn.edu.cqupt.rubic_chartclassification;

import java.util.ArrayList;
import java.util.Map;

/**
 * @description 统计结果数据类型
 * @author Wong JW
 * @date 2015年10月24日 下午2:48:59 
 * @version 2.0 
 */
public class VisualData extends ArrayList<Map<String, Object>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -682748071859779230L;
	
	/**
	 * 数据维度
	 */
	private int dimension;
	
	/**
	 * 结果数据集描述
	 */
	private String description;
	
	public VisualData(int dimension) {
		this.dimension = dimension;
	}
	
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
	
	public int getDimension() {
		return dimension;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
}
