package cn.edu.cqupt.protocol;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取文件协议
 * Created by Vigo on 16/3/5.
 */
public class GetFileProtocol {

    /**
     * 打包xml，构造运行返回结果
     * @return
     */
    public String creatResquest(Map<String, Object> protocolMap){
        Document doc=null;
        Element root=null;
        String string="";
        try {
            //构造根节点
            root=new Element("process");
            doc=new Document(root);
            //构造type节点，并放入根节点
            Element typeElement=new Element("type");
            typeElement.setText("request");
            root.addContent(typeElement);
            //构造result节点，并放入根节点
            Element protocolElement=new Element("protocol");
            Element idElement=new Element("protocol_id");
            idElement.setText(protocolMap.get("protocol_id").toString());

            Element pathElement = new Element("file_path");
            pathElement.setText(protocolMap.get("file_path").toString());

            protocolElement.addContent(idElement);
            protocolElement.addContent(pathElement);
            root.addContent(protocolElement);

            //转换为xml,并返回字符串型
            XMLOutputter XMLOut = new XMLOutputter(FormatXML());
            StringWriter writer = new StringWriter();
            XMLOut.output(doc, writer);
            string=writer.toString();
            //System.out.println(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }
    //设置XML文件格式
    public Format FormatXML(){
        Format format = Format.getCompactFormat();
        format.setEncoding("utf-8");
        format.setIndent(" ");
        return format;
    }

    public static void main(String[] args) {
        Map<String, Object> protocolMap=new HashMap<String, Object>();
        protocolMap.put("file_path", "/Users/lwg.iris.data");

        System.out.println(new GetFileProtocol().creatResquest(protocolMap));
    }
}
