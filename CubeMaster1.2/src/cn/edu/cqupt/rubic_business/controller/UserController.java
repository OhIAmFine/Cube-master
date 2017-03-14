package cn.edu.cqupt.rubic_business.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cqupt.rubic_business.service.ModelIntroductionService;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @description 用户相关操作
 * @author LiuMian
 * @date 2015-12-14 下午2:54:34
 * @version 1.0
 *
 */

@Controller
@RequestMapping("/user")
public class UserController {
	
	private static final Logger logger = Logger.getLogger(ModelIntroductionController.class);

	
	@Resource
	private ModelIntroductionService modelIntroductionService;
	
	@RequestMapping(value="/modelapi",produces = "application/json;charset=UTF-8"
			,method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getModelAndAPI(@RequestParam("user_id") int userId){
		
		logger.debug("user/modelapi userid:"+userId);
		JSONObject modelAPIInfo = modelIntroductionService.getAllModelAndAPIInfo(userId);
		
		return modelAPIInfo;
	}
	
	
	@RequestMapping(value="/deleteapi",produces = "application/json;charset=UTF-8"
			,method = RequestMethod.GET)
	@ResponseBody
	public JSONObject deleteModelAndAPI(@RequestParam("api_id") int apiId){
		
		logger.debug("user/delete api APIID:"+apiId);
		JSONObject result=new JSONObject();
		result.put("status", 0);
		try {
			modelIntroductionService.deleteAPIInfo(apiId);
		} catch (Exception e) {
			result.put("status", 1);
			e.printStackTrace();
			result.put("errmsg",e.getMessage());
		}
		return result;
	}

}
