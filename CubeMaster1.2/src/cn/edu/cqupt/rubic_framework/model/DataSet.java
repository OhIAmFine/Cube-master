/**
 * 
 */
package cn.edu.cqupt.rubic_framework.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据集
 * 
 * @author Colin Wang
 * @date Apr 9, 2015
 */

public class DataSet implements MyStruct,Serializable{

	// 数据集描述
	protected String description;
	// 数据集名字
	protected String name;
	// 数据集类型
	protected int type;
	// 属性说明
	protected String[] attributeLabels;
	// 属性值集合，每个Example为一行
	protected List<Example> examples;

	public DataSet(String[] attributeLabels) {
		this.attributeLabels = attributeLabels;
		examples = new ArrayList<Example>();
	}

	public void setDesc(String desc) {
		this.description = desc;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}
	
	public int getType() {
		return this.type;
	}

	public String[] getAttributeLabels() {
		return this.attributeLabels;
	}

	// 增加一行属性
	public boolean addExample(Example example) {
		if (attributeLabels!=null&&example.size()+1 != attributeLabels.length) {
			return false;
		}
		return examples.add(example);
	}

	public List<Example> getExamples() {
		return this.examples;
	}

	public int getSize() {
		return this.examples.size();
	}

	@Override
	public String toString() {
		return "DataSet [description=" + description + ", name=" + name
				+ ", attributeLabels=" + Arrays.toString(attributeLabels)
				+ ", examples=" + examples + "]";
	}

}
