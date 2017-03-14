package cn.edu.cqupt.rubic_business.controller;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.ModelPo;
import cn.edu.cqupt.rubic_business.service.ModelIntroductionService;
import cn.edu.cqupt.rubic_business.service.SearchService;
import cn.edu.cqupt.rubic_business.service.impl.ModelServiceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 
 * Created by LY on 2015/12/3.
 */
@Controller
@RequestMapping("/list")
public class ModelIntroductionController {

    @Autowired
    private ModelIntroductionService modelIntroductionService;
    @Autowired
    private ModelServiceImpl modelservice;
    
    @Autowired
    private SearchService searchService;
    /**
     * 返回所有modelAPI数据
     * @return JSONObject
     */
    @RequestMapping(value = "/apilist",produces = "application/json;charset=UTF-8"
            ,method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getApiList(){
        //ResponseDataPo responseDataPo = new ResponseDataPo();
        JSONObject json = new JSONObject();
        List<String> allType = modelIntroductionService.getAllModelApiType();
        json.put("protocol_id","A-6-1-request");
        json.put("api",modelIntroductionService.parseApiList(allType));
        return json;
    }
  
    /**
     * 返回指定的某个modelAPI详细数据
     * @param apiid
     * @return JSONObject
     */
    @RequestMapping(value = "/api",produces = "application/json;charset=UTF-8"
            ,method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getTheApi(@RequestParam("api_id") int apiid){
        //ResponseDataPo responseDataPo = new ResponseDataPo();
          return modelIntroductionService.parseTheApi(apiid);
    }
    
    /**
     * @description:model信息，返回给前台，让用户填写model信息
     * @author hey
     * @param modelId
     * @return
     */
    @RequestMapping(value="modelAPIInfo",produces = "application/json;charset=UTF-8"
            ,method = RequestMethod.GET)
    @ResponseBody
    public JSONObject modelAPIInfo(@RequestParam("modelId") int modelId){
    	
    	JSONObject jsonInfo =null;
    	try {
			ModelPo model=modelservice.findModelById(modelId);
			
			jsonInfo=new JSONObject();
			jsonInfo.put("protocol", "A-6-3-response");
			jsonInfo.put("model_id", model.getModelId()+"");
			jsonInfo.put("model_name", model.getModelName());
			jsonInfo.put("model_description", model.getModelDescription());
			
			List<AttributePo>attrList=modelIntroductionService.getModelAttrList(model);
			jsonInfo.put("attributes", getAttrInfoMap(attrList));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return jsonInfo;
    }
 
    /**
     * @author huangy
     * 将modelAPI的信息插入数据库
     * @param json 关于modelAPI的全部信息
     * @return JSONObject 插入是否成功
     */
    @RequestMapping(value = "/upload",produces = "application/json;charset=UTF-8",
			method = RequestMethod.POST)
    @ResponseBody
	public JSONObject uploadModelApiInfo(@RequestBody String string){    
    	

		//解析json并调用saveModelInfo接口保存
		JSONObject jsonObject=JSON.parseObject(string);
		
		Map<String,Object> modelInfoMap=jsonObject.getJSONObject("api");

		JSONArray dataInfo=jsonObject.getJSONArray("attributes");
		
		JSONObject result = new JSONObject();
		result.put("protocol", "A-6-3-request");
		result.put("status", 0);
		try {
			modelIntroductionService.uploadModelAPIInfo(modelInfoMap, dataInfo);
		} catch (Exception e) {
			result.put("status", 1);
			e.printStackTrace();
			result.put("errmsg",e.getMessage());
		}
		return result;
	}
    
    /**
     * @description:打包属性信息
     * @author hey
     * @param attrList
     * @return
     */
    private List<Map<String,String>> getAttrInfoMap(List <? extends AttributePo>attrList){
    	
    	List<Map<String, String>>attrInfo=null;
    	if(attrList!=null){
    		//初始化容器
    		attrInfo=new ArrayList<Map<String,String>>();
    		Map<String,String>attrs=null;
    		for(AttributePo attr:attrList){
    			//间属性信息封装到容器
    			attrs=new HashMap<String, String>();
    			attrs.put("attribute_type", attr.getAttribute_type());
    			attrs.put("attribute_name", attr.getAttribute_name());
    			attrInfo.add(attrs);
    		}
    		return attrInfo;
    	}
    	return null;
    }

    @RequestMapping(value = "/search",produces = "application/json;charset=UTF-8",
			method = RequestMethod.GET)
    @ResponseBody
    public String searchByKeywords(@RequestParam("table_name")String tableName,
    		                        @RequestParam("keywords")String keywords,
    		                        @RequestParam("request_page_number") int start,
    		            			@RequestParam("per_page_number") int offset){
    	
    	
    	try {
			tableName=URLDecoder.decode(tableName, "UTF-8");
			//keywords=URLDecoder.decode(keywords, "UTF-8");
			System.out.println(tableName);
			System.out.println(keywords);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	Map<String,String>map=new HashMap<String, String>();
    	map.put("table_name", tableName);
    	if(tableName.equals("dataset")){
    	  map.put("conditions","dataset_name like "+"'%"+keywords+"%'"+" order by dataset_id DESC " );
    	}else if(tableName.equals("algorithm")){
    		 map.put("conditions","algorithm_name like "+"'%"+keywords+"%'"+" order by algorithm_id DESC " );
    	}else{
    		System.out.println("暂缓");
    	}
    	map.put("request_page_number", String.valueOf(start));
		map.put("per_page_number", String.valueOf(offset));
		
		JSONObject result=searchService.search(map);
		String status=String.valueOf(1);
		String reason=null;
		
		if(result.getString("result")!=null){
			status=String.valueOf(0);
			reason="search is successful";
		}else{
			reason="search is failure";
		}
		result.put("status", status);
		result.put("reason", reason);
		
		return result.toJSONString();
		}
    
}
