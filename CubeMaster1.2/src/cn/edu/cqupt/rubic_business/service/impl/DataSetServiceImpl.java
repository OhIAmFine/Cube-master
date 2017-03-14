package cn.edu.cqupt.rubic_business.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.eclipse.core.runtime.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.DataSetPo;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.dao.AttributeDao;
import cn.edu.cqupt.rubic_business.dao.DataSetDao;
import cn.edu.cqupt.rubic_business.dao.UserDao;
import cn.edu.cqupt.rubic_business.service.DataSetService;
import cn.edu.cqupt.rubic_business.util.MyDateFormat;

@Service("dataSetService")
public class DataSetServiceImpl implements DataSetService {
	private DataSetDao dataSetDao;
	private AttributeDao attributeDao;
	private UserDao userDao;
	
	@Resource(name = "dataSetDao")
	public void setDataSetDao(DataSetDao dataSetDao) {
		this.dataSetDao = dataSetDao;
	}
	
	@Resource(name = "attributeDao")
	public void setAttributeDao(AttributeDao attributeDao) {
		this.attributeDao = attributeDao;
	}

	@Resource(name = "userDao")
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public List<DataSetPo> findAllDataSet(){
		List<DataSetPo> dataSets=dataSetDao.findAllDataSet();
		
		return dataSets;
	}

	
	public DataSetPo findDataSetById(int dataset_id) {
		DataSetPo dataSet=dataSetDao.findDataSetById(dataset_id);
		return dataSet;
	}

	
	public List<DataSetPo> findDataSetsByIds(int[] dataset_ids) {
		List<DataSetPo> dataSets=new ArrayList<DataSetPo>();
		for(int i=0;i<dataset_ids.length;i++){
			DataSetPo dataSet=dataSetDao.findDataSetById(dataset_ids[i]);
			dataSets.add(dataSet);
		}
		return dataSets;
	}

	
	public void addDataSet(DataSetPo dataSetPo) {
		dataSetDao.addDataSet(dataSetPo);
		
	}

	@Override
	public List<Map<String, Object>> findAllDataSetAndInfo() {
		List<Map<String,Object>> maps= new ArrayList<Map<String, Object>>();

		List<DataSetPo> dataSets=dataSetDao.findAllDataSet();
		for(DataSetPo dataSet:dataSets){
			int dataSet_id=dataSet.getDataset_id();
			int user_id = userDao.getUserIdByDid(dataSet_id);

			String userName = userDao.getUserNameById(user_id);

			List<AttributePo> attributes=attributeDao.findAttributesByDid(dataSet_id);

			Map<String,Object> map=new HashMap<String,Object>();

			map.put("dataSet", dataSet);
			map.put("attributes", attributes);
			map.put("user_name", userName);

			maps.add(map);
		}
		return maps;
	}

	@Override
	public int findIdByName(String dataset_name) {
		return dataSetDao.findIdByName(dataset_name);
	}

	@Override
	public int addAttributeRelationship(Map<String, Integer> map) {
		return dataSetDao.addAttributeRelationship(map);
	}
	
	@Override
	public int addUserRelationship(Map<String, Integer> map) {
		return dataSetDao.addUserRelationship(map);
	}

	@Override
	public Map<String, Object> findAllDataSetAndInfoByDid(int dataSet_id) {
		Map<String,Object> map=new HashMap<String,Object>();
		
		DataSetPo dataset=dataSetDao.findDataSetById(dataSet_id);
		
//		MyDateFormat.changeDateToLongString(dataset);
		
		List<AttributePo> attributes=attributeDao.findAttributesByDid(dataSet_id);
		
		
		int user_id = userDao.getUserIdByDid(dataSet_id);
		String userName = userDao.getUserNameById(user_id);
		
		map.put("dataSet",dataset);
		map.put("attributes", attributes);
		map.put("user_name", userName);
		return map;
	}

	@Override
	public List<Integer> getAttributesId(int datasetId) {
		return dataSetDao.getAttributesId(datasetId);
	}

	@Override
	public List<Map<String,Object>> findCurrentUserDataSet(UserPo currentUser) {
		Assert.isNotNull(currentUser, "the currentUser must not be null");
		
		List<DataSetPo> dataSets = null;
		dataSets = dataSetDao.findCurrentUserDataSet(currentUser.getUser_id());
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		int len = dataSets.size();
		for(int i = 0; i < len; i++){
			Map<String,Object> map = new HashMap<String,Object>();
			DataSetPo dataSet = dataSets.get(i);
			
			List<AttributePo> attributes = null;
			attributes = attributeDao.findAttributesByDid(dataSet.getDataset_id());
			
			map.put("user_name", currentUser.getUser_name());
			map.put("dataSet", dataSet);
			map.put("attributes", attributes);
			String timeStr = MyDateFormat.changeDateToLongString(dataSet.getSubmit_datetime());
			map.put("submit_datetime", timeStr);
			list.add(map);
		}
		return list;
	}

	@Override
	public int addDataSetMap(Map<String, Object> map) {
		return dataSetDao.addDataSetMap(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void addDataSet(Map<String, Object> map) {
		Integer userId = (Integer) map.get("userId");
		Map<String, Object> dataset = (Map<String, Object>) map.get("dataset");
		List<Map<String, Object>> attribute = (List<Map<String, Object>>) map.get("attribute");
		
		dataset.put("submit_datetime", new Date());

		/**
		 * 插入数据集
		 */
		addDataSetMap(dataset);
		Integer datasetId = (Integer) dataset.get("dataset_id");
		
		
		/**
		 * 插入用户和数据集关系表
		 */
		Map<String, Integer> userRelationship = new HashMap<String, Integer>();
		userRelationship.put("user_id", userId);
		userRelationship.put("dataset_id", datasetId);
		addUserRelationship(userRelationship);
		
		/**
		 * 插入attribute和数据集、attribute关系表 
		 */
		for(Map<String, Object> attributeMap:attribute){
			attributeDao.addAttributeMap(attributeMap);
			Integer attributeId = (Integer) attributeMap.get("attribute_id");
		    Map<String, Integer> attributeRelationship = new HashMap<String, Integer>();
		    attributeRelationship.put("dataset_id", datasetId);
		    attributeRelationship.put("attribute_id", attributeId);
		    dataSetDao.addAttributeRelationship(attributeRelationship);
		}	
	}

	@Override
	public HashMap<String, Object> findUserIdAndFilePathByDid(int dataSetId) {
		return dataSetDao.findUserIdAndFilePathByDid(dataSetId);
	}
	
	@Override
	public Integer getLabelSequence(int dataSet_id) {
		return dataSetDao.getLabelSequence(dataSet_id);
	}
	
	@Override
	public List<Map<String, Object>> findDataSetByIdReturnMap(int dataset_id) {
		return dataSetDao.findDataSetByIdReturnMap(dataset_id);
	}

	@Override
	public AttributePo getLable(int i) {
		AttributePo a=dataSetDao.getLable(i);
		System.out.println("zheshsi po"+a);
		return a;
		
	}

}
