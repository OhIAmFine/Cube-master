package cn.edu.cqupt.rubic_business.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.cqupt.rubic_business.Model.po.KeywordPo;
import cn.edu.cqupt.rubic_business.dao.KeywordDao;
import cn.edu.cqupt.rubic_business.service.KeywordService;

@Service("keywordService")
public class KeywordServiceImpl implements KeywordService{

	private KeywordDao keywordDao;
	
	@Resource(name = "keywordDao")
	public void setKeywordDao(KeywordDao keywordDao) {
		this.keywordDao = keywordDao;
	}
	
	@Override
	public int addKeywordInMap(Map<String, String> map) {
		return keywordDao.addKeywordInMap(map);
	}
	
	@Override
	public int addKeyword(KeywordPo keywordPo) {
		return keywordDao.addKeyword(keywordPo);
	}

	@Override
	public int findIdByName(String keyword) {
		return keywordDao.findIdByName(keyword);
	}

	@Override
	public List<KeywordPo> findKeywordByAid(int algorithm_id) {
		return keywordDao.findKeywordByAid(algorithm_id);
	}

}
