package cn.edu.cqupt.rubic_business.dao;

import java.util.List;
import java.util.Map;

import cn.edu.cqupt.rubic_business.Model.po.KeywordPo;

public interface KeywordDao {
	
	int addKeyword(KeywordPo keywordPo);	
	
	/**
	 * @description 添加关键字 map形式
	 * @param map
	 * @return 数据库受影响行数
	 */
	int addKeywordInMap(Map<String, String> map);
	
	KeywordPo findKeywordByKid(int keyword_id);
	
	List<KeywordPo> findKeywordByAid(int algorithm_id);
	
	int findIdByName(String keyword);

}
