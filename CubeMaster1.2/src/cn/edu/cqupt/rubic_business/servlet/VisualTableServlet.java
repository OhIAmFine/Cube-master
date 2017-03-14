package cn.edu.cqupt.rubic_business.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.edu.cqupt.rubic_business.service.AttributeService;
import cn.edu.cqupt.rubic_business.service.DataSetService;
import cn.edu.cqupt.rubic_business.service.ResultService;
import cn.edu.cqupt.rubic_business.util.RegexUtils;
import cn.edu.cqupt.rubic_chartclassification.Rule;
import cn.edu.cqupt.rubic_chartclassification.VisualData;
import cn.edu.cqupt.rubic_core.config.Configuration;

import com.alibaba.fastjson.JSONObject;

/**
 * @description 可视化表格展示
 * @author Wong JW
 * @date 2015年11月18日 下午8:49:36 
 * @version 1.0 
 */
public class VisualTableServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		/** Servlet Context*/
		ServletContext servletContext = this.getServletContext(); 
		
		/** Spring WebApplicationContext */
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		
		/** 数据标志 原始 | 结果 */
		String flag = request.getParameter("flag");
		
		/** 数据id */
		String id = request.getParameter("id");
		
		/** 文件路径 */
		String path = getFilePath(Integer.parseInt(id), flag, webAppContext); 
		
		/** 数据每列属性名称 */
		String[] attributeNames = getAttributeNames(Integer.parseInt(id), flag, webAppContext);
		
		/** 可视化展示数据 */
		VisualData visualData = getVisualData(fileContentToMap(path, attributeNames));
		
		/** 封装返回前段数据 */
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", visualData);
		response.getWriter().write(jsonObject.toJSONString());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	/**
	 * @description 将从文件读取封装好的数据封装成可视化可以展示的数据格式
	 * @param data 原始数据
	 * @return 可视化数据格式
	 */
	protected VisualData getVisualData(Map<String, List<String>> data) {
		if(data == null) {
			return null;
		}
		VisualData visualData = new VisualData(0);
		visualData.setDescription("Table");
		for(Iterator<Entry<String, List<String>>> it = data.entrySet().iterator(); it.hasNext(); ) {
			Entry<String, List<String>> entry = it.next();		
			visualData.add(Rule.pack(entry.getKey(), entry.getValue()));
		}
		return visualData;
	}
	
	/**
	 * @description 将文件内容转化为map
	 * @param path 文件路径
	 * @param attributeName 每列属性名称
	 * @return 封装好的map
	 * @throws FileNotFoundException throws when path == null 
	 */
	protected Map<String, List<String>> fileContentToMap(String path, String[] attributeName) throws FileNotFoundException {
		return fileContentToMap(new File(path), attributeName);
	}
	
	/**
	 * @description 将文件内容转化为map
	 * @param file 文件
	 * @param attributeName 每列属性名称
	 * @return 封装好的map
	 */
	protected Map<String, List<String>> fileContentToMap(File file, String[] attributeName) {
		Map<String, List<String>> data = new HashMap<String, List<String>>();
		
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null) {
				line = line.trim();
				line = Pattern.compile(RegexUtils.COMMA_BLANK_REGEX).matcher(line).replaceAll(RegexUtils.COMMA);
				String[] str = line.split(RegexUtils.COMMA);
				if(attributeName.length != str.length) {
					throw new IllegalArgumentException("The length of attribute names array must equal to "
							+ "the number of each line spilit by delimiter of real data readed from file");
				}
				for(int i = 0; i < attributeName.length; i++) {
					List<String> columnData = data.get(attributeName[i]);
					if(columnData == null) {
						columnData = new ArrayList<String>();
					}
					columnData.add(str[i]);
					data.put(attributeName[i], columnData);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	/**
	 * @description 获取数据所在文件路径
	 * @param id 数据id
	 * @param flag 数据标志
	 * @param context Spring 容器
	 * @return 文件路径
	 */
	protected String getFilePath(int id, String flag, WebApplicationContext context) {
		String path = null;
		if("original".equals(flag)) {
			DataSetService dataSetService = context.getBean(DataSetService.class);
			path = dataSetService.findDataSetById(id).getFile_path();
		}
		if("result".equals(flag)) {
			ResultService resultService = context.getBean(ResultService.class);
			path = resultService.findResultById(id).getFile_path();
		}
		if(path != null) {
			return Configuration.getRubic() + path;
		}
		return path;
	}
	
	/**
	 * @description 获取数据每列属性名称
	 * @param id 数据id
	 * @param flag 数据标志
	 * @param context Spring 容器
	 * @return 数据每列名称
	 */
	protected String[] getAttributeNames(int id, String flag, WebApplicationContext context) {
		List<String> attributeNamesList = new ArrayList<String>();
		AttributeService attributeService = context.getBean(AttributeService.class);
		if("original".equals(flag)) {
			attributeNamesList = attributeService.getAttributeNamesByDid(id);
		}
		if("result".equals(flag)) {
			ResultService resultService = context.getBean(ResultService.class);
			attributeNamesList = resultService.getResultAttributeName(id);
		}
		return attributeNamesList.toArray(new String[]{});
	}

}
