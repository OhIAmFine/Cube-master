package cn.edu.cqupt.rubic_chartclassification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @description 规则类
 * @author Wong JW
 * @date 2015年10月23日 下午7:20:38
 * @version 2.0
 */
public class Rule {
	
	/** 区间频率统计 区间划分个数 */
	private final int INTERVAL_NUMBER = 10;
	
	/** double类型保留小数格式 */
	private final String PATTERN = "0.00";

	/**
	 * @description 计算正确率
	 * @param oldLabel
	 *            原始标签列
	 * @param newLabel
	 *            计算后新标签列
	 * @return 正确率
	 */
	public VisualData correctRate(String[] oldLabel, String[] newLabel) {
		VisualData visualData = new VisualData(2);
		visualData.setDescription("正确率");
		
		int length = oldLabel.length > newLabel.length ? newLabel.length
				: oldLabel.length;
		int equalNumber = 0;
		for (int i = 0; i < length; i++) {
			if (oldLabel[i].equals(newLabel[i]))
				equalNumber++;
		}
		double correctRate = equalNumber * 1.00 / length;

		visualData.add(pack("true", new double[] { correctRate }));
		visualData.add(pack("false", new double[] { 1 - correctRate }));

		return visualData;
	}

	/**
	 * @description 三维散点图统计
	 * @param reference 参照列
	 * @param x x轴数据
	 * @param y y轴数据
	 * @return
	 */
	public <T extends Comparable<T>> VisualData threeDimensionScatterplot(
			T[] reference, String[] x, String[] y) {
		VisualData visualData = new VisualData(3);
		visualData.setDescription("三维散点图");

		int len = x.length < y.length ? x.length : y.length;
		
		Double[] dx = transformStringToDouble(x, 0, len);
		Double[] dy = transformStringToDouble(y, 0, len);
		
		Map<T, List<List<Double>>> sortedMap = sort(reference, dx, dy);

		for (Iterator<Entry<T, List<List<Double>>>> itor = sortedMap.entrySet()
				.iterator(); itor.hasNext();) {
			Entry<T, List<List<Double>>> entry = itor.next();
			visualData.add(pack(entry.getKey(), entry.getValue()));
		}

		return visualData;
	}
	
	/**
	 * @description 四维散点图统计
	 * @param oneReference 参照列
	 * @param anotherReference 参照列
	 * @param x x轴数据
	 * @param y y轴数据
	 * @return 可视化数据
	 */
	public <T extends Comparable<T>> VisualData fourDimensionScatterplot(
			T[] oneReference, T[] anotherReference, String[] x, String[] y) {
		VisualData visualData = new VisualData(3);
		visualData.setDescription("四维散点图");
		
		int len = x.length < y.length ? x.length : y.length;
		
		Double[] dx = transformStringToDouble(x, 0, len);
		Double[] dy = transformStringToDouble(y, 0, len);
		
		Map<T, Map<T, List<List<Double>>>> sortedMap = sort(oneReference, anotherReference, dx, dy);
		for(Iterator<Entry<T, Map<T, List<List<Double>>>>> itor = sortedMap.entrySet().iterator(); itor.hasNext(); ) {
			Entry<T, Map<T, List<List<Double>>>> entry = itor.next();
			visualData.add(pack(entry.getKey(), entry.getValue()));
		}
		
		return visualData;
	}
	
	/**
	 * @description 数值频率统计
	 * @param s 数据
	 * @return
	 */
	public VisualData numericalFrequencyStatistics(String[] s) {
		VisualData visualData = new VisualData(2);
		visualData.setDescription("数值频率统计");
		
		Arrays.sort(s);
		
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0; i < s.length; i++) {
			Integer[] num = (Integer[]) map.get(s[i]);
			if(num == null)
				num = new Integer[]{1};
			else 
				num[0]++;
			map.put(s[i], new Integer[] {num[0]});
		}
		
		for(Iterator<Entry<String, Object>> itor = map.entrySet().iterator(); itor.hasNext(); ) {
			Entry<String, Object> entry = itor.next();
			visualData.add(pack(entry.getKey(), entry.getValue()));
		}
		
		return visualData;
	}
	
	/**
	 * @description 区间频率统计
	 * @param s 数据列
	 * @return
	 */
	public VisualData intervalFrequencyStatistics(String[] s) {
		VisualData visualData = new VisualData(2);
		visualData.setDescription("区间频率统计");

		int length = s.length;
		
		Double[] d = transformStringToDouble(s);
		
		Arrays.sort(d);

		String pattern = PATTERN;
		
		double step = formateDouble((d[length - 1] - d[0]) / INTERVAL_NUMBER, pattern); 
		double start = formateDouble(d[0], pattern);
		double next = formateDouble(start + step, pattern);
		Map<String, Object> map = new TreeMap<String, Object>();

		for(int i = 0; i < length; i++) {
			int num = 0;
			String intervalScope = "[" + start + "," + next + "]";
			while(i < length && d[i] <= next) {
				num++;
				i++;
			}
			map.put(intervalScope, new Integer[]{num});
			start = formateDouble(next, pattern);
			next = formateDouble(next + step, pattern);
			if(i > 0 && i < length)
				i--;
			if(i == length - 1)
				map.put(intervalScope, new Integer[] {num});
		}
		
		for(Iterator<Entry<String, Object>> itor = map.entrySet().iterator(); itor.hasNext(); ) {
			Entry<String, Object> entry = itor.next();
			visualData.add(pack(entry.getKey(), entry.getValue()));
		}
		
		return visualData;
	}
	
	/**
	 * @description double数据格式化 指定有效位数
	 * @param number 数据
	 * @param pattern 格式
	 * @return
	 */
	public static double formateDouble(double number, String pattern) {
		return formateDouble(number, new DecimalFormat(pattern));
	}
	
	/**
	 * @description double数据格式化 
	 * @param number 数据
	 * @param formatter 格式
	 * @return
	 */
	public static double formateDouble(double number, DecimalFormat formatter) {
		return Double.parseDouble(formatter.format(number));
	}
	
	/**
	 * @description 根据参照列对可视化数据排序排序
	 * e.g.
	 * input:
	 * 1.0 2.0 "red"
	 * 1.5 2.5 "red"
	 * 1.6 2.8 "green"
	 * returned:
	 * {
	 * 		"red" : [[1.0, 2.0], [1.5, 2.5]]
	 * 		"green" : [[1.6, 1.8]]
	 * }
	 * @param reference 参照列
	 * @param args 可视化数据
	 * @return
	 */
	protected <T extends Comparable<T>> Map<T, List<List<Double>>> sort(
			T[] reference, Double[]... args) {
		Map<T, List<List<Double>>> sortedMap = new TreeMap<T, List<List<Double>>>();

		int dimension = args.length;
		for (int i = 0; i < reference.length; i++) {
			List<List<Double>> coordinateList = sortedMap.get(reference[i]);
			if (coordinateList == null)
				coordinateList = new ArrayList<List<Double>>();
			List<Double> coordinateSubList = new ArrayList<Double>();
			int j = 0;
			while (j < dimension)
				coordinateSubList.add(args[j++][i]);
			coordinateList.add(coordinateSubList);
			sortedMap.put(reference[i], coordinateList);
		}

		return sortedMap;
	}
	
	/**
	 * @description 根据多个参照列对可视化数据排序
	 * e.g.
	 * input:
	 * 1.0 2.0 "red" "big"
	 * 1.5 2.5 "red" "small"
	 * 1.6 2.8 "green" "small"
	 * 1.2 2.2 "red": "small"
	 * 1.9 2.9 "green" "big"
	 * returned:
	 * {
	 * 		"red" : [ {"small" : [[1.5, 2.5], [1.2, 2.2]]}, {"big" : [[1.0, 2.0]]}]
	 * 		"green" : [{"small": [[1.6, 1.8]]}, {"big" : [[1.9. 2.9]]}]
	 * }
	 * @param oneReference
	 * @param anotherReference
	 * @param args
	 * @return
	 */
	protected <T extends Comparable<T>> Map<T, Map<T, List<List<Double>>>> sort(
			T[] oneReference, T[] anotherReference, Double[]...args) {
		Map<T, Map<T, List<List<Double>>>> sortedMap = new TreeMap<T, Map<T, List<List<Double>>>>();
		
		int dimension = args.length;
		for(int i = 0; i < oneReference.length; i++) {
			Map<T, List<List<Double>>> coordinateMap = sortedMap.get(oneReference[i]);
			if(coordinateMap == null) {
				coordinateMap = new TreeMap<T, List<List<Double>>>();
			}
			for(int j = 0; j < anotherReference.length; j++) {
				if(!oneReference[j].equals(oneReference[i])) {
					continue;
				}
				List<List<Double>> coordinateList = coordinateMap.get(anotherReference[j]);
				if(coordinateList == null) {
					coordinateList = new ArrayList<List<Double>>();
				}
				int k = 0;
				List<Double>coordinateSubList = new ArrayList<Double>();
				while(k < dimension) {
					coordinateSubList.add(args[k++][j]);
				}
				if(!coordinateList.contains(coordinateSubList)) {
					coordinateList.add(coordinateSubList);
				}
				coordinateMap.put(anotherReference[j], coordinateList);
			}
			sortedMap.put(oneReference[i], coordinateMap);
		}
		
		return sortedMap;
	} 
	
	/**
	 * @description 将字符串数组转化为Double数组
	 * @param s 字符串数组
	 * @return
	 */
	public static Double[] transformStringToDouble(String[] s) {
		return transformStringToDouble(s, 0, s.length);
	}
	
	/**
	 * @description 将字符串数组转化为Double数组
	 * @param s 字符串数组
	 * @param off 偏移量
	 * @param len 长度
	 * @return
	 */
	public static Double[] transformStringToDouble(String[] s, int off, int len) {
		Double[] d = new Double[len];
		for(int i = 0; i < len; i++) {
			d[i] = Double.valueOf(s[i]);
		}
		return d;
	}

	/**
	 * @description 封装
	 * @param label
	 * @param value
	 * @return
	 */
	public static <T> Map<String, Object> pack(T label, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("label", label);
		map.put("value", value);
		return map;
	}

	/**
	 * @description 提供给外界调用 返回可视化封装后的数据
	 * @param methodName 反射调用的方法名
	 * @param args 方法参数
	 * @return
	 */
	public static VisualData getVisualData(String methodName, Object[] args) {
		VisualData visualData = null;

		Method[] methods = Rule.class.getMethods();
		try {
			for (Method method : methods) {
				if (method.getName().equals(methodName))
					visualData = (VisualData) method.invoke(new Rule(), args);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return visualData;
	}
	
	
	
	public static void main(String[] args) {
		
		String[] one = {"red", "red", "green", "green"};
		String[] another = {"big", "small", "small", "small"};
		String[] x = {"1.2", "1.5", "1.6", "1.8"};
		String[] y = {"2.2", "2.5", "2.6", "2.8"};
		
		Object[] arguments = {one, another, x, y};
		System.out.println(Rule.getVisualData("fourDimensionScatterplot", arguments));
		
		//Object[] arguments = {one, x, y};
		//System.out.println(Rule.getVisualData("threeDimensionScatterplot", arguments));
		
	}
	

}
