package cn.edu.cqupt.rubic_business.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface SearchService {
    public JSONObject search(Map<String,String> map);
}
