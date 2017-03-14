package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;


/**
 * 用于删除session中的属性
 * @author heGuangQin
 *
 */
public class RemoveSessionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Integer> datasetIdList;
	private List<Integer> algorithmIdList;
	
	/**
	 * data algorithm result
	 */
	private static final String datasetId = "dataset_id";
	private static final String algorithmId = "algorithm_id";
	
	

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		
		JSONObject jsonObj = new JSONObject();
		
		String dataset_id = request.getParameter("dataset_id");
		String algorithm_id = request.getParameter("algorithm_id");
		
		if(dataset_id!=null){
			removeListFromSession(request,session,datasetId,dataset_id,jsonObj);
		}
		
		if(algorithm_id!=null){
			removeListFromSession(request,session,algorithmId,algorithm_id,jsonObj);
		}
		
		response.getWriter().write(jsonObj.toString());
		
		
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	public void removeListFromSession(HttpServletRequest request,HttpSession session, String attribute,String id,JSONObject jsonObj){
		@SuppressWarnings("unchecked")
		List<Integer> list = (List<Integer>)session.getAttribute(attribute);
		if(list!=null){
			System.out.println("iiiiiiiiiiiiii    "+list.toString());
			jsonObj.put("dalete_status", "false");
			for(int i=0;i<list.size();i++){
				
				if(list.get(i)==Integer.parseInt(id)){
					System.out.println("jjjjjjjj");
					list.remove(i);
					jsonObj.put("dalete_status", "true");
				}
				
			}
		}
		
		session.setAttribute(attribute, list);
	}

}
