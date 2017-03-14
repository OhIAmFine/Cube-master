package cn.edu.cqupt.rubic_chartclassification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.VisualizationPo;

/**
 * @description 匹配数据集和可视化图表id
 * @author WongJW
 * @created October 17, 2015
 */
public class Matcher {
	
	private int dataType;
	//属性id和po实体
	private Map<Integer, AttributePo> attributeMap;
	//可视化图表id和po实体
	private Map<Integer, VisualizationPo> visualizationMap;
	
	public Matcher(int dataType, Map<Integer, AttributePo> attributeMap, Map<Integer, VisualizationPo> visualizationMap) {
		this.dataType = dataType;
		this.attributeMap = attributeMap;
		this.visualizationMap = visualizationMap;
	}
	
	/**
	 * 返回可用的可视化图表id
	 * @return
	 */
	public Integer[] matches() {
		//匹配数据类型
		matchType();
		//匹配维度
		matchDimension();
		//匹配列表
		matchLabel();
		//匹配类型
		matchCharacter();
		//返回可用图表id
		Set<Integer> set = visualizationMap.keySet();
		Integer[] visualizationId = new Integer[set.size()];
		return set.toArray(visualizationId);
	}
	
	/**
	 * 数据类型监测 文本 数值 ... 
	 */
	private void matchType() {
		Set<Entry<Integer, VisualizationPo>> set = visualizationMap.entrySet();
		for(Iterator<Entry<Integer, VisualizationPo>> itor = set.iterator(); itor.hasNext(); ) {
			Entry<Integer, VisualizationPo> entry = itor.next();
			VisualizationPo value = entry.getValue();
			int type = value.getVisual_data_type();
			if(type > 0 && type != dataType) {
				itor.remove();
			}
		}
	}
	
	/**
	 * 维度匹配 
	 */
	private void matchDimension() {
		int attributeSize = attributeMap.size();
		Set<Entry<Integer, VisualizationPo>> set = visualizationMap.entrySet();
		for(Iterator<Entry<Integer, VisualizationPo>> itor = set.iterator(); itor.hasNext(); ) {
			Entry<Integer, VisualizationPo> entry = itor.next();
			VisualizationPo value = entry.getValue(); 
			int maxCol = Integer.valueOf(value.getVisual_max_col());
			if(maxCol > 0 && maxCol < attributeSize) {
				itor.remove();
			}
		}
	}
	
	/**
	 * Label匹配   排除不能展示Label列的可视化图表
	 */
	private void matchLabel() {
		int size = labelSize();
		if(size > 0) {
			Set<Entry<Integer, VisualizationPo>> set = visualizationMap.entrySet();
			for(Iterator<Entry<Integer, VisualizationPo>> itor = set.iterator(); itor.hasNext(); ) {
				Entry<Integer, VisualizationPo> entry = itor.next();
				VisualizationPo value = entry.getValue();
				if(value.getVisual_label_display().equals("false")) {
					itor.remove();
				}
			}
		}
	}
	
	/**
	 * 类型匹配
	 */
	private void matchCharacter() {
		Integer[] character = countCharacter();
		for(int dataCharcter : character) {
			Set<Entry<Integer, VisualizationPo>> set = visualizationMap.entrySet();
			for(Iterator<Entry<Integer, VisualizationPo>> itor = set.iterator(); itor.hasNext(); ) {
				Entry<Integer, VisualizationPo> entry = itor.next();
				VisualizationPo value = entry.getValue();
				int visualCharacter = value.getVisual_character();
				if(visualCharacter > 0 && visualCharacter != dataCharcter) {
					itor.remove();
				}
			}
		}
	}
	
	/**
	 * @description 统计属性列character
	 * @return
	 */
	private Integer[] countCharacter() {
		Set<Integer> set = attributeMap.keySet();
		List<Integer> list = new ArrayList<Integer>();
		for(Integer key : set) {
			AttributePo attribute = attributeMap.get(key);
			int character = attribute.getAttribute_character();
			if(!list.contains(character)) {
				list.add(character);
			}
		}
		return list.toArray(new Integer[]{});
	}
	
	/**
	 * 计算属性列中Label列的个数
	 * @return
	 */
	private int labelSize() {
		int size = 0;
		Set<Integer> set = attributeMap.keySet();
		for(Integer key : set) {
			AttributePo attribute = attributeMap.get(key);
			if(attribute.getAttribute_label() == 1) {
				size++;
			}
		}
		return size;
	}
	 
}
