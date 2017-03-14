package cn.edu.cqupt.rubic_business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.edu.cqupt.rubic_business.Model.po.APIAttributePo;
import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.ModelAPIPo;
import cn.edu.cqupt.rubic_business.Model.po.ModelPo;
import cn.edu.cqupt.rubic_business.dao.APIAttributeDao;
import cn.edu.cqupt.rubic_business.dao.ModelAPIDao;
import cn.edu.cqupt.rubic_business.dao.ModelDao;
import cn.edu.cqupt.rubic_business.service.ModelIntroductionService;
import cn.edu.cqupt.rubic_business.util.ModelUtils;
import cn.edu.cqupt.rubic_business.util.json.JSONHandler;

/**
 * 
 * Created by LY on 2015/12/9.
 */
@Service("modelIntroductionService")
public class ModelIntroductionServiceImpl implements ModelIntroductionService{
    
	private ModelAPIDao modelAPIDao;
	private APIAttributeDao apiAttributeDao;
    private ModelDao modelDao;

	@Resource(name="modelAPIDao")
	public void setModelAPIDao(ModelAPIDao modelAPIDao) {
		this.modelAPIDao = modelAPIDao;
	}
	
	@Resource(name="APIAttributeDao")
	public void setApiAttributeDao(APIAttributeDao apiAttributeDao) {
		this.apiAttributeDao = apiAttributeDao;
	}
	
	@Resource(name="modelDao")
	public void setModelDao(ModelDao modelDao){
		this.modelDao = modelDao;
	}



	/**
	 * 返回所有modelAPIPo对象
	 * @return List<ModelApiDao>
	 */
	@Override
	public List<ModelAPIPo> getAllModelApiPo() {
		return modelAPIDao.getAllModelApiPo();
	}

	/**
	 * 以Map形式返回所有modelAPIPo对象
	 * @return List<Map<String, Object>>
	 */
	@Override
	public List<Map<String, Object>> getAllModelApiPoReturnMap() {
		return modelAPIDao.getAllModelApiPoReturnMap();
	}

	/**
	 * 根据api_type返回指定modelAPIPo对象
	 * @param relateid
	 * @return List<ModelApiPo>
	 */
	@Override
	public List<ModelAPIPo> getModelApiPoByType(String type) {
		return modelAPIDao.getModelApiPoByType(type);
	}
	/**
	 * 根据api_type以Map形式返回指定modelAPIPo对象
	 * @param relateid
	 * @return List<Map<String, Object>>
	 */
	@Override
	public List<Map<String, Object>> getRelateModelApiByType(String type) {
		return modelAPIDao.getRelateModelApiByType(type);
	}

	/**
	 * 返回所有的api_type
	 * @return List<String>
	 */
	@Override
	public List<String> getAllModelApiType() {
		return modelAPIDao.getAllModelApiType();
	}
 
	/**
	 * 将modelAPI的信息存入数据库
	 * @author huangy
	 * @param Map<String, Object> modelAPI的基本信息
	 * @param JSONArray modelAPI的参数信息
	 */

	@Transactional
	@Override
	public void uploadModelAPIInfo(Map<String, Object> modelInfoMap,JSONArray dataInfo) throws Exception {
		
		
		//插入model信息并返回modelAPIId  
		modelAPIDao.insertModelApiInfo(modelInfoMap);
		int modelAPIId = (Integer)modelInfoMap.get("model_api_id");
		
		//插入参数信息包括输入，输出和modelAPIId
		List<Map<String,Object>> dataInfoList=new ArrayList<Map<String,Object>>();
		for(int i=0;i<dataInfo.size();i++){
			Map<String,Object> map=(JSONObject)dataInfo.get(i);
			map.put("model_api_id", modelAPIId);
			dataInfoList.add(map);
		}
		modelAPIDao.insertModelApiParameter(dataInfoList);

	}
	public List<String> getAllAttributeType() {
		return apiAttributeDao.getAllAttributeType();
	}
    
	/**
	 * 根据id返回modelApi
	 * @return ModelAPIPo
	 * 
	 * 
	 */
	@Override
	public ModelAPIPo getModelApiById(int apiid) {
		return modelAPIDao.getModelApiById(apiid);
	}


	@Override
	public List<Map<String, Object>> getModelApiByIdReturnMap(int apiid) {
		return modelAPIDao.getModelApiByIdReturnMap(apiid);
	}

	@Override
	public List<APIAttributePo> getApiAttributeByApiId(int apiid) {
		return apiAttributeDao.getApiAttributeByApiId(apiid);
	}

	@Override
	public List<Map<String, Object>> getAttributeByApiIdReturnMap(int apiid) {
		return apiAttributeDao.getAttributeByApiIdReturnMap(apiid);
	}

	@Override
	public Map<String, Object> getAllAttributeByApiIdReturnMap(int apiid) {
		return apiAttributeDao.getAllAttributeByApiIdReturnMap(apiid);
	}

	@Override
	public JSONObject getAllModelAndAPIInfo(int userId) {
		
		JSONObject modelAPIInfo = new JSONObject();
		
		modelAPIInfo.put("apis", modelAPIDao.findAllAPIByUserId(userId));
		modelAPIInfo.put("models", modelDao.findAllModelByUserId(userId));
		
		
		return modelAPIInfo;
	}
	
	public List<AttributePo>getModelAttrList(ModelPo model) throws JDOMException, IOException{
		//获得文件输入流
		ByteArrayInputStream input = new ByteArrayInputStream(model.getXmlInfo());
		//拿到Docment对象
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(input);
		Element root = doc.getRootElement();
		//返回节点
		return root==null?null:ModelUtils.getAttributes(root, "/model/dataSets/dataSet/attributes/*");
	}

	@Override
	public void deleteAPIInfo(int apiId) throws Exception {
		try {
			modelAPIDao.deleteModelApi(apiId);
		} catch (Exception e) {
			throw new Exception("数据删除失败");
		}
		
	}
	
    /**
     * 打包成JSON数据
     * @param apiid
     * @return jsonRetrun
     */
	@Override
	public JSONObject parseTheApi(int apiid) {
		List<Map<String,Object>> modelApiPoList = getModelApiByIdReturnMap(apiid);
    	List<Map<String,Object>> attributes = getAttributeByApiIdReturnMap(apiid);
    	List<String> attributeTypes = getAllAttributeType();
    	String Stringid  = String.valueOf(apiid);
    	ModelAPIPo modelApiPo = getModelApiById(apiid);
    	List<ModelAPIPo> modelApiListByType = getModelApiPoByType(modelApiPo.getApi_type());
    	JSONArray jsonRecoApi = new JSONArray();
    	JSONArray jsonAttributes = new JSONArray();
    	JSONObject jsonApiDetails = new JSONObject();
    	for(int i=0;i<modelApiPoList.size();i++){jsonApiDetails.putAll(modelApiPoList.get(i));}
    	for(int i=0;i<attributeTypes.size();i++){
    		JSONArray jsonApiValue = new JSONArray();
    		for(int j=0;j<attributes.size();j++){
    			if(attributeTypes.get(i).equals(attributes.get(j).get("type"))){
    				Map<String,Object> attribute = attributes.get(j);
    				jsonApiValue.add(attribute);
    			}
    		}     
    		Map<String,Object> attributeMap = getAllAttributeByApiIdReturnMap(apiid);
    		attributeMap.put("api_example", JSONHandler.formatForHTML(String.valueOf(attributeMap.get("api_example"))));
    		attributeMap.put("type", attributeTypes.get(i));
    		attributeMap.put("attribute_values", jsonApiValue);
    		jsonAttributes.add(attributeMap);
    	}
    	for(int i=0;i<modelApiListByType.size();i++){
    		ModelAPIPo modelApi = modelApiListByType.get(i);
    		List<Map<String,Object>> modelAPIMap = getModelApiByIdReturnMap(modelApi.getModel_api_id());
    		String theApiId = String.valueOf(modelApi.getModel_api_id());
    		if(!theApiId.equals(Stringid)){
    			for(int j=0;j<modelAPIMap.size();j++){
    				jsonRecoApi.add(modelAPIMap.get(j));
    			}
    		}
    	}
    	
    	JSONObject jsonRetrun = new JSONObject();
    	jsonRetrun.put("protocol_id", "A-6-2-request");
    	jsonRetrun.put("api_details",jsonApiDetails);
    	jsonRetrun.put("api_attributes", jsonAttributes);
    	jsonRetrun.put("reco_api",jsonRecoApi);
    	return jsonRetrun;
	}
	
	/**
     * 打包成JSON数据
     * @param list
     * @return jsonValues
     */
	@Override
	public JSONArray parseApiList(List<String> list) {
		List<String> apiType  = list;
        JSONArray jsonValues = new JSONArray();
        for(int i=0;i<apiType.size();i++){
        	Map<String,Object> apiMap = new HashMap<String, Object>();
        	JSONArray jsonApiValue = new JSONArray();
        	List<Map<String,Object>> apiAttribute = getRelateModelApiByType(apiType.get(i));
            for(int j=0;j<apiAttribute.size();j++){
                Map<String,Object> modelApi = apiAttribute.get(j);
                jsonApiValue.add(modelApi);
            }
            apiMap.put("api_type", apiType.get(i));
            apiMap.put("api_value", jsonApiValue);
            jsonValues.add(apiMap);
        }
            return jsonValues ;
	}
	
}
