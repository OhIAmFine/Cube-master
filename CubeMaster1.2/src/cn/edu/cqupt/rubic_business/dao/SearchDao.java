package cn.edu.cqupt.rubic_business.dao;

import java.util.List;
import java.util.Map;

public interface SearchDao {
    List<Map<String,Object>> findByKeywords(Map<String,String> map);
    int selectPageCount(Map<String, String> map);
}
