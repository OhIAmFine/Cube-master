package cn.edu.cqupt.rubic_business.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jdom.JDOMException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.edu.cqupt.rubic_business.Model.po.APIAttributePo;
import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.ModelAPIPo;
import cn.edu.cqupt.rubic_business.Model.po.ModelPo;


/**
 * 
 * Created by LY on 2015/12/9.
 */
public interface ModelIntroductionService {

    List<ModelAPIPo> getAllModelApiPo();
    
    List<String>  getAllModelApiType();

    List<Map<String,Object>> getAllModelApiPoReturnMap();

    List<ModelAPIPo> getModelApiPoByType(String type);

    List<Map<String,Object>> getRelateModelApiByType(String type);
	
    void uploadModelAPIInfo (Map<String, Object> modelInfoMap,JSONArray dataInfo)throws Exception;
    
    List<String> getAllAttributeType();
    
	ModelAPIPo getModelApiById(int apiid);
	
	List<Map<String,Object>> getModelApiByIdReturnMap(int apiid);
	
	List<APIAttributePo> getApiAttributeByApiId(int apiid);
	
	List<Map<String,Object>> getAttributeByApiIdReturnMap(int apiid);
	
	Map<String,Object> getAllAttributeByApiIdReturnMap(int apiid);
	
	JSONObject getAllModelAndAPIInfo(int userId);
	
	void deleteAPIInfo(int apiId)throws Exception;
	
	JSONObject parseTheApi(int apiid);
	
	JSONArray parseApiList(List<String> list);
	
	/**
     * @description:获得model的数据集属性信息
     * @author hey
     * @param model
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    List<AttributePo>getModelAttrList(ModelPo model) throws JDOMException, IOException;
	
	
	
}
