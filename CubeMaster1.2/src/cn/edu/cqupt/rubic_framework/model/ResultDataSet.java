package cn.edu.cqupt.rubic_framework.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultDataSet extends DataSet implements MyStruct {

	public ResultDataSet(String[] labels) {
		super(labels);
		examples = new ArrayList<Example>();
	}

	private List<Example> examples;

	// 增加一行属性
	public boolean addExample(ResultExample example) {
		if (example.size() + 1 != attributeLabels.length) {
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
				+ ", labels=" + Arrays.toString(attributeLabels) + "]";
	}
}
