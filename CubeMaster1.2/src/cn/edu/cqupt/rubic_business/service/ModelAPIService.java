package cn.edu.cqupt.rubic_business.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by LiuMian on 2015/12/2.
 *
 */
public interface ModelAPIService {

    JSONObject callModel(int modelId, Map<String,String> attributes) throws Exception;
    
    
}
