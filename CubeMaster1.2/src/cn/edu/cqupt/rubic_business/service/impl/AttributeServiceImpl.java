package cn.edu.cqupt.rubic_business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.ResultdatasetAttributeRelationshipPo;
import cn.edu.cqupt.rubic_business.dao.AttributeDao;
import cn.edu.cqupt.rubic_business.dao.ResultDao;
import cn.edu.cqupt.rubic_business.dao.ResultdatasetAttributeRelationshipDao;
import cn.edu.cqupt.rubic_business.service.AttributeService;

@Service("attributeService")
public class AttributeServiceImpl implements AttributeService {
	private AttributeDao attributeDao;
	@Autowired
	private ResultDao resultDao;
	
	@Autowired
	private ResultdatasetAttributeRelationshipDao resultdatasetAttributeRelationshipDao;

	@Resource(name = "attributeDao")
	public AttributeDao getAttributeDao(AttributeDao attributeDao) {
		return this.attributeDao = attributeDao;
	}

	@Override
	public int findIdByName(String attribute_name) {
		return attributeDao.findIdByName(attribute_name);
	}

	
	@Override
	public int addAttribute(AttributePo attributePo) {
		attributeDao.addAttribute(attributePo);
		return attributePo.getAttribute_id();
	}

	@Override
	public List<HashMap<String, Object>> findAttributeByResultId(Integer resuletDateset_Id) {

		List<Integer> attributeIdList = resultDao
				.findAttribute(resuletDateset_Id);
		List<HashMap<String, Object>> attributePoList = new ArrayList<HashMap<String,Object>>();
		for (Integer attribute : attributeIdList) {
			attributePoList.add(attributeDao.findAttributeById(attribute));
		}
		return attributePoList;
	}

	@Override
	public List<AttributePo> getAttributesByDId(int datasetId) {
		return attributeDao.findAttributesByDid(datasetId);
	}

	@Override
	public int getResultAttributeId() {
		return attributeDao.getResultAttributeId();
	}

	@Override
	public void addResultdatasetAttributeRelation(
			ResultdatasetAttributeRelationshipPo resultdatasetAttributeRelationshipPo) {
		
		resultdatasetAttributeRelationshipDao.addResultdatasetAttributeRelation(resultdatasetAttributeRelationshipPo);
		
	}

	@Override
	public List<AttributePo> getAttributesByRId(int resultId) {
		return null;
	}

	@Override
	public AttributePo getAttributeById(int attributeId) {
		return attributeDao.getAttributeById(attributeId);
	}

	@Override
	public int addAttributeMap(Map<String, Object> map) {
		return attributeDao.addAttributeMap(map);
	}
	
	@Override
	public List<String> getAttributeNameByIds(Integer[] attributesId){
		List<String> attributeName = new ArrayList<String>();
		for(int dataset_id:attributesId){
			attributeName.add(attributeDao.getAttributeNameByDid(dataset_id));
		}
		return attributeName;
	}

	@Override
	public List<String> getAttributeNamesByDid(int datasetId) {
		List<String> attributeNames = attributeDao.getAttributeNamesByDid(datasetId);
		return attributeNames != null ? attributeNames:null;
	}

}
