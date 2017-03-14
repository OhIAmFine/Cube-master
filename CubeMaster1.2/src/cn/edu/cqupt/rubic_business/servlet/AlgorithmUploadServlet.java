package cn.edu.cqupt.rubic_business.servlet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.edu.cqupt.rubic_business.service.AlgorithmService;
import cn.edu.cqupt.rubic_core.config.Configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Wong JW
 * @date Mar 23, 2016
 */
@SuppressWarnings("serial")
public class AlgorithmUploadServlet extends HttpServlet {
	
	@Override
	public void init() throws ServletException {
		super.init();
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html; charset=utf-8");
		
		ServletContext context = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(context);
		AlgorithmService algorithmService = webAppContext.getBean(AlgorithmService.class);
		
		Map<String, Object> dataMap = handleRequest(request);
		
		String status = "fail";
		
		if (dataMap != null) {
			status = "success";
			algorithmService.addAlgorithm(dataMap);
		}
		
		response.getWriter().write(status);
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> handleRequest(HttpServletRequest request) {
		
		/** create temp file **/
		FileHandler fileHandler = new FileHandler();
		File tempFile = fileHandler.createFile(Configuration.getRubic() + "temp" + Thread.currentThread().getId());
		
		/** create DiskFileItemFactory **/
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(4 * 1024);
		factory.setRepository(tempFile);
		
		/** create ServletFileUpload **/
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		upload.setSizeMax(10 * 1024 * 1024);
		
		Map<String, Object> algorithmMap = null;
		String path = null;
		
		try {
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> itor = items.iterator();
			while (itor.hasNext()) {
				FileItem item = itor.next();
				if (item.isFormField()) {
					algorithmMap = parseForm(item);
					path = (String) ((Map<String, String>)algorithmMap.get("algorithm")).get("file_path");
				} else {
					if (path != null) {
						path = uploadFile(item, path);
						FileHandler.deleteFile(tempFile);
					} 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		((Map<String, String>)algorithmMap.get("algorithm")).put("file_path", path);
		
		return algorithmMap;
		
	}
	
	/**
	 * upload file
	 * @param item
	 * @param path
	 * @return relative file path
	 * @throws Exception
	 */
	private String uploadFile(FileItem item, String path) throws Exception {
		
		FileHandler fileHandler = new FileHandler();
		fileHandler.createFile(Configuration.getRubic() + path);
		String fileName = item.getName();
		path = Configuration.getRubic() + path + File.separator + fileName;
		File uploadedFile = new File(path);
		item.write(uploadedFile);
		return path.substring(Configuration.getRubic().length());
		
	}
	
	/**
	 * Parse form area
	 * @param item
	 * @return parsed map
	 * @throws UnsupportedEncodingException
	 */
	private Map<String, Object> parseForm(FileItem item) throws UnsupportedEncodingException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		JSONObject jsonObj = JSON.parseObject(item.getString("UTF-8"));
		String algorithm = jsonObj.getString("algorithm");
		String keyword = jsonObj.getString("keyword");
		String parameter = jsonObj.getString("parameter");
		
		List<HashMap<String, String>> keywordMap = parseJSONArray(keyword);
		List<HashMap<String, String>> parameterMap = parseJSONArray(parameter);
		Map<String,String> algorithmMap = parseJSON(algorithm);
		
		String userId = algorithmMap.get("user_id");
		
		algorithmMap.put("parameter_count", String.valueOf(parameterMap.size()));
		algorithmMap.put("file_path", userId + File.separator + "algorithm");
		
		map.put("algorithm", algorithmMap);
		map.put("keyword", keywordMap);
		map.put("parameter", parameterMap);
		map.put("userId", userId);
		
		return map;
		
	}
	
	/**
	 * json to map
	 * @param json
	 * @return map
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> parseJSON(String json) {
		return (Map<String, String>) JSON.parse(json);
	}
	
	/**
	 * json array to list
	 * @param json
	 * @return list
	 */
	@SuppressWarnings("unchecked")
	private List<HashMap<String, String>> parseJSONArray(String json) {
		Map<String,String> hashMap = new HashMap<String,String>();
		return (List<HashMap<String, String>>) JSON.parseArray(json, hashMap.getClass());
	}

}
