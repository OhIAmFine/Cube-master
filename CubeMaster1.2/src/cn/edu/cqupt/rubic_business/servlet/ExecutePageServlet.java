package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.edu.cqupt.rubic_business.service.AlgorithmService;
import cn.edu.cqupt.rubic_business.service.DataSetService;

/**
 * 
 * @description
 * @author wangjw
 * @created 2015-5-27 下午9:29:25
 *
 */

//@WebServlet(name = "OperatePage",value = "/OperatePage")
public class ExecutePageServlet extends HttpServlet{

	private AlgorithmService algorithmService; 
	
	private DataSetService dataSetService;
	
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppcontext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		algorithmService = webAppcontext.getBean(AlgorithmService.class);
		dataSetService = webAppcontext.getBean(DataSetService.class);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

//		//获取算法id和数据id
//		int algorithm_id=25;
//		int dataset_id=25;
		
//		String algorithm=(String)request.getParameter("dataset");
//		System.out.println(algorithm);
//		Enumeration<String> names=request.getParameterNames();
//		while(names.hasMoreElements()){
//			String str=names.nextElement();
//			if(str.contains("algo")){
//				algorithm_id=Integer.parseInt(request.getParameter(str));
//				
//			}else if(str.contains("data")){
//				dataset_id=Integer.parseInt(request.getParameter(str));
//			
//			}
//			System.out.println("***"+request.getParameter(names.nextElement()));
//		}
		
		HttpSession session = request.getSession();
		List<Integer> datasetIdList = (List<Integer>)session.getAttribute("dataset_id");
		List<Integer> algorithmIdList = (List<Integer>)session.getAttribute("algorithm_id");
		
		Map<String, List<Map<String,Object>>> dataAlgorithmMap = new HashMap<String, List<Map<String, Object>>>();
		
		if(null != datasetIdList) {
			Integer[] datasetId = datasetIdList.toArray(new Integer[]{});
			List<Map<String, Object>> datasetList = getList("dataSet", datasetId);
			dataAlgorithmMap.put("dataSet", datasetList);
//			System.out.println("put dataSet -----");
		}
		
		if(null != algorithmIdList) {
			Integer[] algorithmId = algorithmIdList.toArray(new Integer[]{});
			List<Map<String, Object>> algorithmList = getList("algorithm", algorithmId);
			dataAlgorithmMap.put("algorithm", algorithmList);
//			System.out.println("put algorithm -----");
		}
		
		JSONHandler jsonHandler = new JSONHandler();
//		if(datasetIdList != null && algorithmIdList != null) {
//		}
		response.getWriter().write(jsonHandler.packDataAndAlgorithm(dataAlgorithmMap));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	/**
	 * 根据id和名称获取算法|数据集list
	 * @param which
	 * @param idArr
	 * @return
	 */
	public List<Map<String, Object>> getList(String which, Integer[] ids) {
		
		if(null == ids) {
			return null;
		}
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		if(which.equals("algorithm")) {
			for(int i = 0; i < ids.length; i++) {
				Map<String, Object> map = algorithmService.findAllAlgorithmAndInfoByAid(ids[i]);
				if(null != map.get("algorithm")) {
					list.add(map);
				}
			}
		}
		
		if(which.equals("dataSet")) {
			for(int i = 0; i < ids.length; i++) {
				Map<String, Object> map = dataSetService.findAllDataSetAndInfoByDid(ids[i]);
				if(null != map.get("dataSet")) {
					list.add(map);
				}
			}
		}
		
		return list;
	}
	
}