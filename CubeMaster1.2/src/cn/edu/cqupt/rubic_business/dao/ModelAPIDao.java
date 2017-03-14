package cn.edu.cqupt.rubic_business.dao;


import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.ModelAPIPo;
import cn.edu.cqupt.rubic_business.Model.po.ModelPo;

/**
 * Created by LY on 2015/12/6.
 */
public interface ModelAPIDao {

       List<ModelAPIPo> getAllModelApiPo();
       
       List<String> getAllModelApiType();

       List<Map<String,Object>> getAllModelApiPoReturnMap();

       List<ModelAPIPo> getModelApiPoByType(String relateid);

       List<Map<String,Object>> getRelateModelApiByType(String relateid);
       
       
       ModelAPIPo getModelApiById(int apiid);
      	
   	   List<Map<String,Object>> getModelApiByIdReturnMap(int apiid);
   	   
   	   List<Map<String,Object>> findAllAPIByUserId(int userID);
   	   
   	   ModelPo findModelByAPIId(int apiId);
   	
   	   int insertModelApiInfo(Map<String, Object> modelInfoMap)throws Exception;
   	
   	   void insertModelApiParameter(List<Map<String,Object>> dataInfoList)throws Exception;
   	   
   	   void deleteModelApi(int apiId)throws Exception;
   	
}
