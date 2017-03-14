package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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

import cn.edu.cqupt.rubic_business.Model.po.AlgorithmPo;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.dao.UserDao;
import cn.edu.cqupt.rubic_business.service.AlgorithmService;
import cn.edu.cqupt.rubic_business.service.KeywordService;
import cn.edu.cqupt.rubic_business.service.ParameterService;
import cn.edu.cqupt.rubic_business.service.UserService;
import cn.edu.cqupt.rubic_business.util.MyDateFormat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @description 算法列表展示
 * @author WongJW
 * @author LiangYH
 * @created 2015/6/6 
 * @change 2015/9/25
 */
public class AlgorithmListServlet extends HttpServlet{
	
	@Override
	public void init() throws ServletException {
		super.init();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=utf-8"); 
		
		String operate = request.getParameter("operate");
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		AlgorithmService algorithmService = webAppContext.getBean(AlgorithmService.class);
		KeywordService keywordService = webAppContext.getBean(KeywordService.class);
		ParameterService parameterService = webAppContext.getBean(ParameterService.class);
		UserService userService = webAppContext.getBean(UserService.class);
		
		if(operate == null || operate == ""){

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("protocol", "A-2-2-request");
			jsonObj.put("algorithm", packAlgoList(algorithmService, keywordService, parameterService, userService));
			response.getWriter().write(jsonObj.toJSONString());
			return ;
		}
		
		if("getCurrentUserAlgorithms".equals(operate)){
			//获取用户id
			HttpSession session = request.getSession();
			UserPo currentUser = (UserPo) session.getAttribute("user");
			//数据库操作：查询该用户算法
			Map<String, List<Map<String,Object>>> algorithmPos = null;
			algorithmPos = algorithmService.findCurrentUserAlgorithms(currentUser);

			JSONObject jsonObj = new JSONObject();
			jsonObj.putAll(algorithmPos);
			response.getWriter().write(jsonObj.toJSONString());
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	private JSONArray packAlgoList(AlgorithmService algorithmService, KeywordService keywordService
			, ParameterService parameterService, UserService userService) {
		JSONArray jsonArr = new JSONArray();
		List<String> associatedTasks = algorithmService.findAllAlgorithmClass();
		for(String task : associatedTasks) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("algorithm_class", task);
			JSONArray array = new JSONArray();
			List<Map<String,Object>> algos = algorithmService.findAllAlgorithmByClass(task);
			for(Map<String,Object> algo : algos) {
				JSONObject obj = new JSONObject();
				
				MyDateFormat.formatDate(algo);
				
				Integer algorithm_id = (Integer) algo.get("algorithm_id");
				obj.put("algorithm", algo);
				obj.put("key_word", keywordService.findKeywordByAid(algorithm_id));
				obj.put("parameters", parameterService.findParametersByAid(algorithm_id));
				obj.put("user_name", userService.getUserNameByAid(algorithm_id));
				array.add(obj);
			}
			jsonObj.put("algorithms", array);
			jsonArr.add(jsonObj);
		}
		
		return jsonArr;
	}
	

}
