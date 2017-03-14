package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;

public class StoreServlet extends HttpServlet {
	
	private List<Integer> dataIdList;
	private List<Integer> algorithmIdList;
	private List<Integer> processIdList;
	
	/**
	 * data algorithm result
	 */
	private static final String datasetId = "dataset_id";
	private static final String algorithmId = "algorithm_id";
	private static final String processId = "process_id";
	
	/**
	 * Visualization
	 */
	private static final String result = "result_id";
	private static final String resultCol = "resultCol[]";
	private static final String original = "original_id";
	private static final String originalCol = "originalCol[]";
	private static final String visualCol = "visualCol[]";
	
	/**
	 * visual_details
	 */
	private static final String visual_details = "visual_details";
	
	
	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		System.out.println(session.getMaxInactiveInterval());
		
		
		if(request.getParameter(datasetId) != null) {
			setSession(request, session, datasetId, dataIdList);
		}
		if(request.getParameter(algorithmId) != null) {
			System.out.println("选择算法id: "+request.getParameter(algorithmId));
			setSession(request, session, algorithmId, algorithmIdList);
		}
		if(request.getParameter(processId) != null) {
			setSession(request, session, processId, processIdList);
		}
		
		if(request.getParameter(result) != null && !request.getParameter(result).equals("")) {
			setSingle(request, session, result);
			if(request.getParameter(resultCol) != null) {
				setList(request, session, resultCol);
			}
		}
		
		if(request.getParameter(original) != null && !request.getParameter(original).equals("")) {
			setSingle(request, session, original);
			if(request.getParameter(originalCol) != null) {
				setList(request, session, originalCol);
			}
		}
		
		if(request.getParameter(visualCol) != null) {
			setList(request, session, visualCol);
		}
		
		if(request.getParameter(visual_details) != null){
			String visuaStr = request.getParameter(visual_details);
			JSONObject reqJson = JSONObject.parseObject(visuaStr);
			session.setAttribute(visual_details, reqJson);
			
		}
		

	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	@SuppressWarnings("unchecked")
	public void setSession(HttpServletRequest request, HttpSession session, String attribute, List<Integer> list) {
		list = (List<Integer>)session.getAttribute(attribute);
		if(list == null) {
			list = new ArrayList<Integer>();
		}
		int id = Integer.parseInt((request.getParameter(attribute)));
		if(!list.contains(id)) {
			list.add(id);
		}
		session.setAttribute(attribute, list);
	}
	
	public void setList(HttpServletRequest request, HttpSession session, String attribute) {
		String[] s = request.getParameterValues(attribute);
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < s.length; i++) {
			int id = Integer.valueOf(s[i]);
			if(!list.contains(id)) {
				list.add(id);
			}
		}
		session.setAttribute(attribute, list);
	}
	
	public void setSingle(HttpServletRequest request, HttpSession session, String attribute) {
		String s = request.getParameter(attribute);
		int i = Integer.valueOf(s);
		session.setAttribute(attribute, i);
	}

}
