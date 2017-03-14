package cn.edu.cqupt.protocol;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 客户端 打包request，解析response
 * 
 * @author JIANYI
 * 
 */
public class ClientProtocol {

	public JSONObject parserResponse(String str) {
		try {
			StringReader xmlReader = new StringReader(str);
			SAXBuilder builder = new SAXBuilder();

			Document document = builder.build(xmlReader);

//			Map<String, Object> result = new HashMap<String, Object>();

			JSONObject result = new JSONObject();
			Element root = document.getRootElement();

			Element node_type = root.getChild("type");
			// 解析type
			result.put("type", node_type.getValue());

			Element node_result = root.getChild("result");
			// 解析code
			Element code = node_result.getChild("code");
			result.put("if_success", new Integer(code.getValue()));

			// 解析reason
			Element reason = node_result.getChild("reason");
			result.put("reason", reason.getValue());

			// 解析content
			Element content = node_result.getChild("content");
			result.put("content", content.getValue());

			return result;
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public String creatRequest(Map<String, Object> map) {

        /**
         * 平台+xml
         */
		String string = null;

		// 创建根节点
		Element root = new Element("process");
		// 将根节点添加到文档中
		Document doc = new Document(root);
		// 打包type
		Element node_type = new Element("type");
		node_type.setText((String) map.get("type"));
		root.addContent(node_type);

		Element node_protocol = new Element("protocol");
		root.addContent(node_protocol);

		// 打包protocol_id
		Element element1 = new Element("protocol_id");
		element1.setText((String) map.get("protocol_id"));
		node_protocol.addContent(element1);

		// 打包user_id
		Element element2 = new Element("user_id");
		element2.setText((String) map.get("user_id"));
		node_protocol.addContent(element2);

		// 打包plantform
		Element element3 = new Element("platform");
		element3.setText((String) map.get("platform"));
        /**
         * 加入平台信息
         */
		string =  "platform:"+map.get("platform") + "\n";
		node_protocol.addContent(element3);

		// 打包data_id_s
		Element element4 = new Element("data_id_s");
		String[] data_parameter = (String[]) map.get("data_id_s");
		for (int i = 0; i < data_parameter.length; i++) {
			Element eles = new Element("data_id");
			eles.setText(data_parameter[i]);
			element4.addContent(eles);
		}
		node_protocol.addContent(element4);

		// 打包algorithm_id
		Element element5 = new Element("algorithm_id");
		element5.setText((String) map.get("algorithm_id"));
		node_protocol.addContent(element5);

		// 打包 parameter_s
		Element element6 = new Element("parameter_s");
		String[] parameter_p_s = (String[]) map.get("parameter_s");
		for (int i = 0; i < parameter_p_s.length; i++) {
			Element eles = new Element("parameter");
			eles.setText(parameter_p_s[i]);
			element6.addContent(eles);
		}
		node_protocol.addContent(element6);


		try {
			XMLOutputter XMLOut = new XMLOutputter(FormatXML());
			StringWriter writer = new StringWriter();
			XMLOut.output(doc, writer);
			string += writer.toString();

			System.out.println(string);
			// XMLOut.output(doc, new
			// FileOutputStream("C:\\Users\\JIANYI\\Desktop\\rubic\\test.xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return string;

	}

	public Format FormatXML() {
		// 格式化生成的xml文件，如果不进行格式化的话，生成的xml文件将会是很长的一行...
		Format format = Format.getCompactFormat();
		format.setEncoding("utf-8");
		format.setIndent(" ");
		return format;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
				+ "<process>\r\n" + " <type>response</type>\r\n"
				+ "<!--   运行结果代码   1表示成功，2表示失败 -->\r\n" + "<result>\r\n"
				+ " <code>1</code>\r\n" + " <reason>失败原因</reason>\r\n"
				+ " <!--   运行成功的话返回process_record返回的最重要内容-->\r\n"
				+ " <content>123456</content>\r\n" + " </result>\r\n"
				+ " </process>";
		ClientProtocol protocol = new ClientProtocol();
		Map<String, Object> pp = protocol.parserResponse(str);
		System.out.println(pp.toString());


        /**
         * cubemaster发送到Rubic的数据包
         */
        String sendPack ="platform:java\n"+"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<process>" +
                "<type>request</type>" +
                "<protocol>" +
                "<protocol_id>1</protocol_id>" +
                "<user_id>58</user_id>" +
                "<platform>java</platform>" +
                "<data_id_s>" +
                "<data_id>23</data_id>" +
                "</data_id_s>" +
                "<algorithm_id>17</algorithm_id>" +
                "<parameter_s>" +
                "<parameter>4</parameter>" +
                "</parameter_s>" +
                "</protocol >" +
                "</process>";
	}

}
