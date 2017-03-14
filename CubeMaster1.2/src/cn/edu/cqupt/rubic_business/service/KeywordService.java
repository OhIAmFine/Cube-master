package cn.edu.cqupt.rubic_business.service;

import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.KeywordPo;

public interface KeywordService {
	
	int addKeywordInMap(Map<String, String> map);
	
	int addKeyword(KeywordPo keywordPo);
	
	int findIdByName(String keyword);
	
	List<KeywordPo> findKeywordByAid(int algorithm_id);

}
