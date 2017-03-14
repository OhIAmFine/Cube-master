package cn.edu.cqupt.rubic_business.servlet;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.edu.cqupt.rubic_core.io.HDFSFileDaoImpl;
import org.apache.commons.io.IOUtils;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;

import cn.edu.cqupt.rubic_business.Model.po.AlgorithmPo;
import cn.edu.cqupt.rubic_business.Model.po.DataSetPo;
import cn.edu.cqupt.rubic_business.Model.po.UserPo;
import cn.edu.cqupt.rubic_business.service.AlgorithmService;
import cn.edu.cqupt.rubic_core.config.Configuration;

/**
 * 
 * <p>
 * Description:读取算法代码详情
 * </p>
 * 
 * @author hey
 * @date 2015-9-9
 */
@SuppressWarnings("serial")
public class AlgorithmCodeServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		ServletContext servletContext = this.getServletContext();
		WebApplicationContext webAppContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		AlgorithmService algorithmService = webAppContext
				.getBean(AlgorithmService.class);

		String algorithmId = request.getParameter("algorithm_id");
		Integer aid = Integer.parseInt(algorithmId);
		String javaFile = request.getParameter("javaFile");

		if (javaFile == null || javaFile.trim().isEmpty() || javaFile == "") {
			javaFile = null;
		}

		HttpSession session = request.getSession(false);

		String jarFile = getRealJarFile(session, aid, algorithmService);

		// 保存文件读取的进度
		@SuppressWarnings("unchecked")
		HashMap<String, Integer> numMap = (HashMap<String, Integer>) request
				.getSession().getAttribute("session_numMap");

		Integer num;
		if (numMap != null && javaFile != null) {
			num = numMap.get(javaFile) + 1;

		} else {
			num = 1;
		}

		HashMap<String, Object> maps = new HashMap<String, Object>();
		maps.put("protocol", "A-2-4-response");
		maps.put("algorithm_id", algorithmId);

		try {
			HashMap<String, Object> map = readJar(jarFile, num, javaFile);

			// 将每个文件的行数标记保存在session中
			if (javaFile == null || numMap == null) {
				numMap = new HashMap<String, Integer>();
				Set<String> keySet = map.keySet();
				for (String key : keySet) {
					numMap.put(key, 1);
				}
			} else {
				Integer numFile = numMap.get(javaFile);
				numMap.put(javaFile, ++numFile);
			}
			request.getSession().setAttribute("session_numMap", numMap);

			// 将读取的java文件返回
			if (map.size() == 0) {
				map.put("msg", "作者没有公开的源代码");
			}
			maps.put("source", map);
			//System.out.println(JSON.toJSONString(maps));
			response.getWriter().print(JSON.toJSONString(maps));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getRealJarFile(HttpSession session, int algorithmId,
			AlgorithmService algorithmService) {

		HashMap<String, Object> pathAndIdMap = algorithmService
				.findUserIdAndFilePathByAid(algorithmId);
		
		String nowPath = (String) pathAndIdMap.get("file_path");

		String initialPath = new Configuration().getRubic();

		String jarFile = initialPath + "\\" + nowPath;

		return jarFile;
	}

	/**
	 * 读取所有的jar文件中的java文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param index
	 *            标记读取多少行
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> readJar(String filePath, Integer index,
			String javaFile) throws Exception {

		File file = null;
		HashMap<String, Object> maps = new HashMap<String, Object>();
		;

		if (filePath == null) {
			maps.put("msg", "该文件不存在");
			return maps;
		}

		file = new File(filePath);

		if (javaFile == null || javaFile.isEmpty()) {
			if (!file.exists()) {
				// System.out.println("对不起，你所查找的文件不存在");
			} else {
				JarFile jarFile = new JarFile(new File(filePath));
				Enumeration<JarEntry> entries = jarFile.entries();

				while (entries.hasMoreElements()) {

					JarEntry entry = entries.nextElement();
					String entryName = entry.getName();
					if (!entry.isDirectory() && entryName.endsWith(".java")) {

						maps.put(entryName,
								readFile(filePath, entryName, index));

					}

				}

			}
		} else {
			maps.put(javaFile, readFile(filePath, javaFile, index));
		}

		return maps;

	}

	/**
	 * 读取文件的内容
	 * 
	 * @param filePath
	 *            jar文件所在位置
	 * @param entryName
	 *            读取的java文件的名字
	 * @param index
	 *            用来标记读取多少行
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	public String readFile(String filePath, String entryName, Integer index) {
		InputStream in = null;
		BufferedReader br = null;
		StringBuffer sb = null;

		try {
			in = getJarInputStream(filePath, entryName);
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

			int before = (index - 1) * 50;
			int after = index * 50;
			String con = null;
			int row = 0;

			sb = new StringBuffer();
			while ((con = br.readLine()) != null) {

				if (before <= row && row < after) {

					if (!con.trim().isEmpty()) {
						con = con.replaceAll("\r\n*", "");
						con = con.replaceAll("\t*", "");
						sb.append(con);
						sb.append("\r\n");
					}

				}

				if (row >= index * 50)
					break;
				row++;
			}

			if ((row - before) < 49) {
				sb.append("该文件代码读取完了");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (in != null)
					in.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param filePath
	 *            jar文件置
	 * @param name
	 *            java文件的相对位置
	 * @return
	 * @throws Exception
	 */
	public InputStream getJarInputStream(String filePath, String name)
			throws Exception {
		URL url = new URL("jar:file:" + filePath + "!/" + name);
//		URL url = new URL("jar:file:" + "localhost:8080/FileSystem/servlet/JarFile?interface_id=jar" + "!/" + "WebServiceRef.class");
		JarURLConnection jarConnection = (JarURLConnection) url
				.openConnection();
		InputStream in = jarConnection.getInputStream();

		return in;
	}

}
