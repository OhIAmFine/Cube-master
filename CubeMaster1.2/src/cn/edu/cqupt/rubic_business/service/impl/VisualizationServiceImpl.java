package cn.edu.cqupt.rubic_business.service.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.cqupt.rubic_business.Model.po.VisualizationPo;
import cn.edu.cqupt.rubic_business.dao.AttributeDao;
import cn.edu.cqupt.rubic_business.dao.VisualizationDao;
import cn.edu.cqupt.rubic_business.service.VisualizationService;

@Service("visualizationService")
public class VisualizationServiceImpl implements VisualizationService {

	private VisualizationDao visualizationDao;
	private AttributeDao attributeDao;
	
	@Resource(name="visualizationDao")
	public void setVisualizationDao(VisualizationDao visualizationDao){
		this.visualizationDao = visualizationDao;
	}
	
	@Resource(name="attributeDao")
	public void setAttributeDao(AttributeDao attributeDao){
		this.attributeDao = attributeDao;
	}
	
	@Override
	public List<Integer> getAllVisualizationId() {
		return visualizationDao.getAllVisualizationId();
	}
	
	@Override
	public HashMap<String, Object> findVisualizationById(int id) {
		return visualizationDao.findVisualizationById(id);
	}
	
	@Override
	public ArrayList<Map<String, Object>> findVisualizationsByIds(int[] ids) {
		ArrayList<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int id:ids){
			list.add(findVisualizationById(id));
		}
		return list;
	}

	@Override
	public int[] findAllAttributeIdByResultdatasetId(int id) {
		Integer[] attributeIds_Integer = visualizationDao.findAllAttributeIdByResultdatasetId(id);
		int[] attributeIds_int = new int[attributeIds_Integer.length];
		for(int i = 0;i < attributeIds_Integer.length;i++){
			//如果Integer为null，应该赋予int什么值？！
			attributeIds_int[i] = attributeIds_Integer[i];
		}
		
		return attributeIds_int;
	}

	@Override
	public List<Map<String,Object>> findAllAttributeByResultdatasetId(int id){
		List<Map<String,Object>> attributes = new ArrayList<Map<String,Object>>();
		int[] attributeIds = findAllAttributeIdByResultdatasetId(id);
		for(int attributeId:attributeIds){
			attributes.add(attributeDao.findAttributeById(attributeId));
		}
		return attributes;
	}

	@Override
	public <T> List<T> getAttribute(String path, int attributeId) {
		File file = new File(path);
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getAllVisualization() {
		return visualizationDao.getAllVisualization();
	}
	
	@Override
	public List<String> getAllTypes() {
		return visualizationDao.getAllTypes();
	}

	@Override
	public List<Map<String, Object>> getVisualizationByType(String type) {
		return visualizationDao.getVisualizationByType(type);
	}
	
	@Override
	public List<Map<String, Object>> getCalculationById(int id) {
		return visualizationDao.getCalculationById(id);
	}
	
	@Override
	public List<VisualizationPo> findAllVisualization(){
		return visualizationDao.findAllVisualization();
	}

	@Override
	public Map<String, Object> getfindVisualizationById(int id) {
		return visualizationDao.findVisualizationById(id);
	}

	@Override
	public String getTypesById(int id) {
		return visualizationDao.getTypesById(id);
	}

	@Override
	public List<Map<String, Object>> getIdByType(String type) {
		return visualizationDao.getIdByType(type);
	}

	@Override
	public List<Integer> getCalculationIdsById(int id) {
		return visualizationDao.getCalculationIdsById(id);
	}
	
	@Override
	public List<HashMap<String,Object>> findAllRule(){
		return visualizationDao.findAllRule();
	}
	
	public List<Integer> findVisualIdsByRuleId(int ruleId){
		return visualizationDao.findVisualIdsByRuleId(ruleId);
	}

	@Override
	public String findMethodNameByRid(int ruleId) {
		return visualizationDao.findMethodNameByRid(ruleId);
	}

	@Override
	public List<HashMap<String, Object>> findArgumentsByRuleId(int ruleId) {
		return visualizationDao.findArgumentsByRuleId(ruleId);
	}

	@Override
	public List<String> findAllRuleType() {
		return visualizationDao.findAllRuleType();
	}

	@Override
	public List<HashMap<String, Object>> findRuleByType(String type) {
		return visualizationDao.findRuleByType(type);
	}
}
