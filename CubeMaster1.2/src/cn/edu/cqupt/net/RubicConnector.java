package cn.edu.cqupt.net;

import java.util.Map;

import cn.edu.cqupt.protocol.ClientProtocol;
import cn.edu.cqupt.protocol.HadoopClientProtocol;
import cn.edu.cqupt.rubic_core.config.Configuration;

/**
 * @author Wong JW
 * @date Mar 22, 2016
 */
public class RubicConnector {
	
	/** java platform **/
	private static final String JAVA_PLATFORM = "java";
	
	/** hadoop platform **/
	private static final String HADOOP_PLATFORM = "hadoop";

	public static String getResponseXML(Map<String, Object> request) {
		return getResponseXML(request, JAVA_PLATFORM);
	}
	
	/**
	 * get response content
	 * @param request Map
	 * @param platform java or hadoop
	 * @return string
	 */
	public static String getResponseXML(Map<String, Object> request, String platform) {
		String response = null;
		if (JAVA_PLATFORM.equalsIgnoreCase(platform)) {
			ClientProtocol clientProtocol = new ClientProtocol();
			response = HttpURLConnector.post(Configuration.RUBIC_URL, clientProtocol.creatRequest(request));
			if (response != null) {
				return clientProtocol.parserResponse(response).toString();
			}
		} else if (HADOOP_PLATFORM.equalsIgnoreCase(platform)) {
			HadoopClientProtocol hadoopClientProtocol = new HadoopClientProtocol();
			response = HttpURLConnector.post(Configuration.RUBIC_URL, hadoopClientProtocol.creatRequest(request));
			if (response != null) {
				return hadoopClientProtocol.parserResponse(response).toString();
			}
		} 
		return null;
	}
	
}
