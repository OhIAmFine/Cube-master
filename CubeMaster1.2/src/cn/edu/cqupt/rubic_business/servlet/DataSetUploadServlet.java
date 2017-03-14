package cn.edu.cqupt.rubic_business.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.service.DataSetService;
import cn.edu.cqupt.rubic_core.config.Configuration;
import cn.edu.cqupt.rubic_core.io.HDFSFileDaoImpl;

/**
 * 
 * @description 数据上传Servlet
 * @author wangjw
 * @updateauthor Zhangx,HeGuangqin
 */
// @WebServlet(name = "DataSetUpload", value = "servlet/DataSetUpload")
public class DataSetUploadServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		DataSetService dataSetService = webAppContext.getBean(DataSetService.class);
		
		HttpSession session = (HttpSession)request.getSession();
		UserPo userPo = (UserPo) session.getAttribute("user");
		Configuration config = new Configuration(userPo);
		
		Map<String, Object> map = handlRequest(request, config, userPo);
		String status = "";
		if(map!=null){
			status = "success";
			dataSetService.addDataSet(map);
		}
		response.getWriter().write(status);
	}

	private Map<String, Object> handlRequest(HttpServletRequest request, Configuration config, 
			UserPo userPo) {
	
		String path = config.getDATASET_PATH();
		
		//文件处理类
		FileHandler fileHandler = new FileHandler();
		
		// 文件存储路径
		fileHandler.createFile(path);
		
		// 创建存放临时目录 如果上传文件超过缓存 将存储到临时目录
		File tempFile = fileHandler.createTempFile(path);
		tempFile.mkdirs();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置缓存大小 极限临界值 4k
		factory.setSizeThreshold(4 * 1024);
		// 设置容器 即临时存放目录
		factory.setRepository(tempFile);
		//文件上传监听器
		UploadListener listener = new UploadListener(request.getSession());

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setProgressListener(listener);
		upload.setHeaderEncoding("utf-8");
		// 设置允许上传最大文件大小 10M
		upload.setSizeMax(10 * 1024 * 1024);
		// 解析http请求
		List<FileItem> items = null;
		Map<String,Object> formMap = null;
		try {
			String filePath = null;
			FileItem formItem = null;
			FileItem fileItem = null;
			items = upload.parseRequest(request);
			// 遍历表单
			Iterator<FileItem> it = items.iterator();
			while (it.hasNext()) {
				FileItem item = it.next();
				if (item.isFormField()) { 
					formItem = item;
				}else{
					fileItem = item;
				}
			}
			formMap = handlForm(formItem,userPo.getUser_id(),fileItem,config);//表单域与文件域同时处理

			// 删除临时目录
			fileHandler.deleteFile(tempFile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		return formMap;
	}

	/**
	 * 处理前台传来的json
	 * @param json 需要解析的json
	 * @param path 文件保存路径
	 * @param userId 用户id
	 * @return 封装好的map
	 * @throws Exception 
	 */
	public Map<String, Object> handlForm(FileItem item,int userId,FileItem fileItem,Configuration config) throws Exception{
		
		JSONObject jsonObj = JSON.parseObject(item.getString("utf-8"));
		String dataset = jsonObj.getString("dataset");
		String attribute = jsonObj.getString("attribute");
		
		Map<String, Object> formMap = new HashMap<String,Object>();
		
		Map<String,String> datasetMap = parseJSON(dataset);
		List<HashMap<String, String>> attributeMap = parseJSONArray(attribute);
			
		//处理文件域
		String data_platform = datasetMap.get("data_platform");
		String filePath = handlUploadFile(fileItem, data_platform , config);
		
		
		datasetMap.put("attribute_count", String.valueOf(attributeMap.size()));
		datasetMap.put("file_path", filePath);
		
		formMap.put("dataset", datasetMap);
		formMap.put("attribute", attributeMap);
		formMap.put("userId", userId);
		
		return formMap;
	}
	
	/**
	 * 处理上传算法文件
	 * @param item
	 * @param path
	 * @throws Exception
	 */
	public String handlUploadFile(FileItem item, String data_platform, Configuration config)
			throws Exception {

		// 表单内容 文件文件名
		String fileName = item.getName();
		
		String path = null;

		// 检查文件名是够重复 后缀名是否需要过滤 未实现
		//判断数据存储的平台
		if(!"hadoop".equals(data_platform)){
			// 获取文件在服务器磁盘上的保存路径
			path = config.getDATASET_PATH() + "\\" +fileName;
			// 文件存储路径
			File uploadedFile = new File(path);
			
			// 写到指定文件
			item.write(uploadedFile);
			return path.substring(Configuration.getRubic().length());
		}else{
			//获取文件在HDFS系统上的存放路径
			path = config.getHDFS_DATASET_PATH()+ "/" +fileName;
			
			HDFSFileDaoImpl fileDao = new HDFSFileDaoImpl();
			//写入到指定文件
			fileDao.createFile(path, item.get());
			return path.substring(Configuration.getHDFS().length());

		}
		
		
	}
	
	/**
	 * 将json转换成map格式
	 * @param json
	 * @return
	 */
	private Map<String, String> parseJSON(String json) {
		return (Map<String, String>) JSON.parse(json);
	}
	
	/**
	 * 将json数组转换成list格式
	 * @param json
	 * @return
	 */
	private List<HashMap<String, String>> parseJSONArray(String json) {
		Map<String,String> hashMap = new HashMap<String,String>();
		return (List<HashMap<String, String>>) JSON.parseArray(json, hashMap.getClass());
	}
}