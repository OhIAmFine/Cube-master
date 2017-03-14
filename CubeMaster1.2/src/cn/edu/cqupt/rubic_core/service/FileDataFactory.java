/**
 * 
 */
package cn.edu.cqupt.rubic_core.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.cqupt.rubic_core.c_interface.DataFactory;
import cn.edu.cqupt.rubic_core.exception.DataException;
import cn.edu.cqupt.rubic_framework.model.DataSet;
import cn.edu.cqupt.rubic_framework.model.Example;

/**
 * @author Colin Wang
 * @date Apr 7, 2015
 */
public class FileDataFactory implements DataFactory {

	private DataSet dataSet;

	public DataSet getData(String name) throws DataException {
		return this.dataSet;
	}

	public FileDataFactory(String filePath, int dataType, String[] attributeLabels, Integer labelSequence) {

		dataSet = new DataSet(attributeLabels);
		dataSet.setType(dataType);

		File file = new File(filePath);
		readFile(file, labelSequence);
	}

	private void readFile(File file, Integer labelSequence) {
		String line = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) {
					break;
				}
				Example example = null;
				if(dataSet.getType() == 1) {
					example = generateExample(line, labelSequence);
				} else {
					example = generateNumericalExample(line, labelSequence);
				}
				this.dataSet.addExample(example);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理数值类数据集
	 * @param line
	 * @param labelSequence
	 * @return
	 */
	private Example generateNumericalExample(String line,Integer labelSequence) {
		Example example = new Example();
		
		line.replace("^\\s+|\\s+$", "");
		line=line.replaceAll("\\s{1,}", ",");
		
		String[] tokens = line.split(",");
		if(labelSequence == 0){//修改labelSequence==null为labelSequence==0
			for (int i = 0; i < tokens.length; i++) 
				example.addAttribute(tokens[i]);
		}else{
			List<String> strs = new ArrayList<String>();
			strs.addAll(Arrays.asList(tokens));//将tokens放入数组中
			String label = strs.get(labelSequence-1);//取出label，并将label从数组中删除，其他的都是attribute
			strs.remove(labelSequence-1);
			for (String string : strs) {
				example.addAttribute(string);
			}
//			for (int i = 0; i < tokens.length -1; i++) 
//				example.addAttribute(tokens[i]);
			example.setLabel(label);
		}
		//System.out.println("***********"+example.toString());
		return example;
	}
	
	private Example generateExample(String line, int sequence) {
		Example example = new Example();
		if(sequence == 1) {
			int index = line.indexOf(",");
			example.setLabel(line.substring(0, index));
			example.addAttribute(line.substring(index + 1));
		} else if(sequence == -1) {
			int index = line.lastIndexOf(",");
			example.setLabel(line.substring(index + 1));
			example.addAttribute(line.substring(0, index));
		}
		return example;
	}

	public static void main(String[] args) {
//		String path = "dataset/iris.data";
//		DataFactory factory = new FileDataFactory(path,"iris");
//		DataSet dataSet = factory.getData("iris");
//		System.out.println(dataSet.toString());
//		System.out.println(dataSet.getSize());
	}

}
