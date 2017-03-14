package cn.edu.cqupt.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import cn.edu.cqupt.protocol.ClientProtocol;
import cn.edu.cqupt.protocol.HadoopClientProtocol;
import cn.edu.cqupt.rubic_core.config.Configuration;

/**
 * @description: 用于书Rubic进行数据交换
 * @author hey
 * @data 2016年1月19日
 * @deprecated by Michael Wong
 * @version 1.0
 */
public class RubicConnection {
	//==========================本机测试
	// 链接地址
	public static  String DEFAULT_RUBICURL = Configuration.RUBIC_URL;
//	private static String DEFAULT_RUBICURL = "http://localhost:80/rubic/protocol_1";

	private String rubicUrl = null;

	// 使用默认连接接口
	public RubicConnection() {
		this(DEFAULT_RUBICURL);
	}

	// 设定端口号
	public RubicConnection(String rubicUrl) {
		this.rubicUrl = rubicUrl;
	}

	/**
	 * @description: 链接Rubic工程
	 * @author hey
	 * @data 2016年1月19日
	 */
	public Map<String,Object> connect(Map<String, Object> requestMap,String platform) {
		/*// 打包
		ClientProtocol protocol = new ClientProtocol();
		String request = protocol.creatRequest(requestMap);

		// 发送请求
		System.out.println(request);*/
		
		String request = null;
		if ("\"java\"".equalsIgnoreCase(platform)
				|| "java".equalsIgnoreCase(platform)) {
			ClientProtocol protocol = new ClientProtocol();
			request = protocol.creatRequest(requestMap).replaceAll("\\r\\n","");
		} else {
			HadoopClientProtocol protocol = new HadoopClientProtocol();
			request = protocol.creatRequest(requestMap).replaceAll("\\r\\n","");
		}
		
		
		// 发送请求
		System.out.println(sendRequest(request,platform));
		
		return null;
//		return protocol.parserResponse(sendRequest(request));
		// 获取返回数据
	}

	/**
	 * @description: 发送请求信息
	 * @author hey
	 * @data 2016年1月20日
	 */
	public String sendRequest(String info,String platform) {

		/**
		 * 定义HttpURLConnection，URL，OutputStream以及InputStream的对象用于连接服务器以及信息收发
		 */
		HttpURLConnection httpConn = null;
		URL url;
		OutputStream os = null;
		InputStream is = null;

		try {
			/**
			 * 将上述对象实例化
			 */
			url = new URL(rubicUrl);

			httpConn = (HttpURLConnection) url.openConnection();
			/**
			 * 请求类型设置为POST
			 */
			httpConn.setRequestMethod("POST");
			/**
			 * 设置允许输入输出
			 */
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			/**
			 * 将消息包以UTF-8编码形式发送给服务器
			 */
			os = httpConn.getOutputStream();
			os.write(info.getBytes("UTF-8"));
			os.flush();
			/**
			 * 设置接受缓冲区，读取服务器发送来的消息包并转换为GBK编码的字符串
			 */
			System.out.println("发送结束");
			is = httpConn.getInputStream();
			int currentsize = 0;
			int count = 0;
			int maxsize = 65535;
			byte[] buf = new byte[maxsize];

			while ((currentsize = is.read(buf, count, maxsize - count)) != -1)
				count += currentsize;
			String getPack = new String(buf, 0, count, "utf-8");
			return getPack;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
