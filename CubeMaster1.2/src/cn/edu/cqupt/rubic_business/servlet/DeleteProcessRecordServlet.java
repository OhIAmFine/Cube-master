package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;

import cn.edu.cqupt.rubic_business.service.ProcessRecordService;
import cn.edu.cqupt.rubic_business.service.ResultService;

/**
 * 删除process_record记录
 * @author he GuangQin
 *
 */
public class DeleteProcessRecordServlet extends HttpServlet {
	
	private ProcessRecordService processRecordService;
	private ResultService resultService;

	/**
	 * Constructor of the object.
	 */
	public DeleteProcessRecordServlet() {
		super();
	}

	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		JSONObject jsonObj = new JSONObject();
		
		String process_id = request.getParameter("process_id");
//		System.out.println(process_id);
		
		processRecordService.DeleteProcessRecord(Integer.parseInt(process_id));
		
		if(resultService.getProcessRecordPo(Integer.parseInt(process_id))==null){
			jsonObj.put("is_delete", "true");
		}else{
			jsonObj.put("is_delete", "false");
		}
		
		response.getWriter().write(jsonObj.toString());

		
	}

	
	public void init() throws ServletException {
		
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppcontext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		
		processRecordService = webAppcontext.getBean(ProcessRecordService.class);
		resultService = webAppcontext.getBean(ResultService.class);
		
	}

}
