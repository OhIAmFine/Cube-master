package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.cqupt.net.RubicConnection;
import cn.edu.cqupt.net.RubicConnector;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.service.impl.RunOnHadoopServiceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class RunServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		Map<String, Object> requestMap = jsonAnalyze(request);
		String platform = (String) requestMap.get("platform");

		Map<String, Object> resultStatus = null;
//
//		if ("\"java\"".equalsIgnoreCase(platform)
//				|| "java".equalsIgnoreCase(platform)) {
//
//			RubicConnection connect = new RubicConnection();
//			resultStatus = connect.connect(requestMap);
//		} else {
//			RunOnHadoopServiceImpl runService = new RunOnHadoopServiceImpl(
//					this.getServletContext());
//			resultStatus = runService.run(requestMap);
//
//		}
		
		if (platform.startsWith("\"") && platform.endsWith("\"") ) {
			platform = platform.substring(platform.indexOf("\"") + 1, platform.lastIndexOf("\""));
		}
		
		String responseContent = RubicConnector.getResponseXML(requestMap, platform);
//		if ((Integer) resultStatus.get("code") == 1) {
//			resultStatus.put("if_success", "1");
//		}
//		response.getWriter().print(JSONObject.toJSONString(responseContent));
		response.getWriter().print(responseContent);
	}

	/**
	 * 解析前端发送过来的json
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, Object> jsonAnalyze(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 得到登陆者id
		Integer userId = ((UserPo) request.getSession().getAttribute("user"))
				.getUser_id();
		map.put("user_id", userId + "");
		// 得到算法类型hadoop、java
		String platform = (String) request.getParameter("platform");
		map.put("platform", platform);

		// 得到数据id数组
		String data = (String) request.getParameter("data");
		JSONArray datas = JSON.parseArray(data);

		String[] dataIds = new String[datas.size()];
		for (int i = 0; i < datas.size(); i++) {
			String dataId = datas.getJSONObject(i).getString("id");
			dataIds[i] = dataId;
		}
		map.put("data_id_s", dataIds);

		// 得到算法id
		String model = (String) request.getParameter("model");
		JSONArray models = JSON.parseArray(model);
		JSONObject model1 = models.getJSONObject(0);
		Integer algorithm_id = Integer.parseInt(model1.getString("id"));
		map.put("algorithm_id", algorithm_id + "");

		// 得到算法参数
		JSONArray parameterArray = model1.getJSONArray("parameters");
		String[] parameters = new String[parameterArray.size()];
		for (int i = 0; i < parameterArray.size(); i++) {
			String value = parameterArray.getJSONObject(i).getString("value");
			parameters[i] = value;
		}
		map.put("parameter_s", parameters);

		return map;
	}

}