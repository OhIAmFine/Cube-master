package cn.edu.cqupt.rubic_framework.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据集中的一行样本
 * 
 * @author Colin Wang
 * @date Apr 9, 2015
 */
public class Example extends ArrayList<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7205951020878470818L;
	
	private String label;

	public String[] getAttribute() {
		return this.toArray(new String[0]);
	}

	public double[] getAttributeInDouble() {
		String[] attributes = getAttribute();
		double[] attrInDouble = new double[attributes.length];
		for (int i = 0; i < attributes.length; i++) {
			attrInDouble[i] = Double.valueOf(attributes[i]);
		}
		return attrInDouble;
	}
	
	public boolean addAttribute(String value) {
		return this.add(value);
	}

	public boolean addAllAttributes(String[] values) {
		boolean flag = false;
		for (String value : values) {
			flag = addAttribute(value);
			if (flag == false) {
				return false;
			}
		}
		return flag;
	}

	@Override
	public String toString() {
		String[] attributes = getAttribute();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < attributes.length; i++) {
			sb.append(attributes[i]);
			if (i != attributes.length - 1)
				sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("hell");
		list.add("colin");
		String[] array = list.toArray(new String[0]);
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
		}
	}

	public String getLabel() {
		return label;
	}
	public double getLabelDouble(){
		return Double.parseDouble(label);
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
