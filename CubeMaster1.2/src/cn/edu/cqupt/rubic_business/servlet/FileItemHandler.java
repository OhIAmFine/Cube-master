package cn.edu.cqupt.rubic_business.servlet;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.KeywordPo;
import cn.edu.cqupt.rubic_business.Model.po.ParameterPo;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.service.AlgorithmService;
import cn.edu.cqupt.rubic_business.service.AttributeService;
import cn.edu.cqupt.rubic_business.service.DataSetService;
import cn.edu.cqupt.rubic_business.service.KeywordService;
import cn.edu.cqupt.rubic_business.service.ParameterService;

/**
 * 
 * @description 表单处理工具类
 * @author wangjw
 * @create 2015/6/1 下午7:27:39
 * 
 */
public class FileItemHandler {

	private List<String> parameter_name_list = new ArrayList<String>();

	private List<String> parameter_type_list = new ArrayList<String>();

	private List<String> parameter_value_list = new ArrayList<String>();
	
	private List<String> attribute_name_list = new ArrayList<String>();
	
	private List<String> attribute_type_list = new ArrayList<String>();
	
	private List<String> attribute_range_list = new ArrayList<String>();
	
	private List<String> attribute_missing_list = new ArrayList<String>();
	
	private List<Integer> attribute_character_list = new ArrayList<Integer>();
	
	private List<Integer> attribute_label_list = new ArrayList<Integer>();

	private AlgorithmService algorithmService;

	private DataSetService dataSetService;

	private ParameterService parameterService;

	private KeywordService keywordService;

	private AttributeService attributeService;

	private final String separator = ",";

	private String algorithm_name;
	
	private String dataset_name;

	private String keywords;

	public FileItemHandler() {

	}

	public FileItemHandler(DataSetService dataSetService,
			AttributeService attributeService) {
		this.dataSetService = dataSetService;
		this.attributeService = attributeService;
	}

	public FileItemHandler(AlgorithmService algorithmService,
			ParameterService parameterService, KeywordService keywordService) {
		this.algorithmService = algorithmService;
		this.parameterService = parameterService;
		this.keywordService = keywordService;
	}

	/**
	 * 处理普通表单域 封装成对象
	 * 
	 * @param item
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws ParseException
	 * @throws UnsupportedEncodingException 
	 */
	public void processFormField(FileItem item, Object obj)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, ParseException, UnsupportedEncodingException {

		String parameter_regex = "^parameter.*";

		String attribute_regex = "^attribute.*";

		// 表单名
		String formName = item.getFieldName();

		// 表单内容
		String formValue = item.getString("utf-8");
		
		if (formName.matches(parameter_regex)) {
			if (formName.equals("parameter_name")) {
				parameter_name_list.add(formValue);
			} else if (formName.equals("parameter_type")) {
				parameter_type_list.add(formValue);
			} else if (formName.equals("parameter_value")) {
				parameter_value_list.add(formValue);
			}

		} else if (formName.matches(attribute_regex)) {
			if (formName.equals("attribute_name")) {
				attribute_name_list.add(formValue);
			} else if (formName.equals("attribute_type")) {
				attribute_type_list.add(formValue);
			} else if (formName.equals("attribute_range")) {
				attribute_range_list.add(formValue);
			} else if (formName.equals("attribute_missing")) {
				attribute_missing_list.add(formValue);
			} else if (formName.equals("attribute_label")) {
				attribute_label_list.add(Integer.valueOf(formValue));
			} else if (formName.equals("attribute_character")){
				attribute_character_list.add(Integer.valueOf(formValue));
			}
		} else if (formName.equals("key_word")) {
			keywords = formValue;
		} else {
			if (formName.equals("algorithm_name")) {
				algorithm_name = formValue;
			}else if(formName.equals("dataset_name")) {
				dataset_name = formValue;
			}
			// 实例化对像
			ReflectHandler.set(obj, formName, formValue);
		}
	}

	/**
	 * 处理上传文件域
	 * 
	 * @param item
	 * @param path
	 *            存储路径
	 * @throws Exception
	 */
	public void processUploadedField(FileItem item, String path)
			throws Exception {

		// 表单内容 文件原始路径
		String filePath = item.getName();

		int index = filePath.lastIndexOf("\\");

		// 文件名称
		String fileName = filePath.substring(index + 1, filePath.length());

		// 检查文件名是够重复 后缀名是否需要过滤 未实现

		// 文件存储路径
		File uploadedFile = new File(path + "\\" + fileName);

		// 写到指定文件
		item.write(uploadedFile);
	}

	/**
	 * 算法参数
	 */
	public int handlerParameter() {

		int size = parameter_name_list.size();

		if (size > 0) {
			for (int i = 0; i < size; i++) {
				ParameterPo parameterPo = new ParameterPo();
				parameterPo.setParameter_name(parameter_name_list.get(i));
				parameterPo.setParameter_type(parameter_type_list.get(i));
				parameterPo.setParameter_value(parameter_value_list.get(i));
				parameterService.addParameter(parameterPo);
			}
		}
		
		return size;
	}

	/**
	 * 算法关键字
	 * 
	 * @param keywords
	 */
	public void handlerKeyword() {
		keywords=keywords.replace("，", ",");
		String[] keyword = keywords.split(separator);

		for (int i = 0; i < keyword.length; i++) {
			KeywordPo keywordPo = new KeywordPo();
			keywordPo.setKeyword(keyword[i]);
			keywordService.addKeyword(keywordPo);
		}

		handlerKeywordRelationship(keyword);
	}

	/**
	 * 数据集属性
	 */
	public void handlerAttribute() {
		
		int size = attribute_name_list.size();
		
		if(size > 0) {
			for(int i = 0; i < size; i++) {
				AttributePo attributePo = new AttributePo();
				attributePo.setAttribute_name(attribute_name_list.get(i));
				attributePo.setAttribute_type(attribute_type_list.get(i));
				attributePo.setAttribute_range(attribute_range_list.get(i));
				attributePo.setAttribute_missing(attribute_missing_list.get(i));
				attributePo.setAttribute_label(attribute_label_list.get(i));
				attributePo.setAttribute_character(attribute_character_list.get(i));
				attributeService.addAttribute(attributePo);
			}
		}
	}

	/**
	 * 算法参数关系
	 */
	public void handlerParameterRelationship() {

		int algorithm_id = algorithmService.findIdByName(algorithm_name);

		for (int i = 0; i < parameter_name_list.size(); i++) {
			String parameter_name = parameter_name_list.get(i);
			int parameter_id = parameterService.findIdByName(parameter_name);
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("algorithm_id", algorithm_id);
			map.put("parameter_id", parameter_id);
			map.put("parameter_sequence", i + 1);
			algorithmService.addParameterRelationship(map);
		}
	}

	/**
	 * 算法关键字关系
	 * 
	 * @param keyword
	 */
	public void handlerKeywordRelationship(String[] keyword) {

		int algorithm_id = algorithmService.findIdByName(algorithm_name);

		for (int i = 0; i < keyword.length; i++) {
			int keyword_id = keywordService.findIdByName(keyword[i]);
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("algorithm_id", algorithm_id);
			map.put("keyword_id", keyword_id);
			algorithmService.addKeywordRelationship(map);
		}
	}
	
	/**
	 * 数据集属性关系
	 */
	public void handlerAttributeRelationship() {
		
		int dataset_id = dataSetService.findIdByName(dataset_name);
		
		for(int i = 0; i < attribute_name_list.size(); i++) {
			int attribute_id = attributeService.findIdByName(attribute_name_list.get(i));
			Map<String, Integer> map = new HashMap<String, Integer>();
			map.put("dataset_id", dataset_id);
			map.put("attribute_id", attribute_id);
			dataSetService.addAttributeRelationship(map);
		}

	}
	
	/**
	 * 算法和用户
	 * @param user
	 */
	public void handlerUserAlgorithmRelationship(UserPo user) {
		int algorithm_id = algorithmService.findIdByName(algorithm_name);
		int user_id = user.getUser_id();
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("user_id", user_id);
		map.put("algorithm_id", algorithm_id);
		algorithmService.addUserRelationship(map);
	}
	/**
	 * 数据和用户
	 * @param user
	 */
	public void handlerUserDataSetRelationship(UserPo user) {
		int dataset_id = dataSetService.findIdByName(dataset_name);
		int user_id = user.getUser_id();
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("user_id", user_id);
		map.put("dataset_id", dataset_id);
		dataSetService.addUserRelationship(map);
	}

}
