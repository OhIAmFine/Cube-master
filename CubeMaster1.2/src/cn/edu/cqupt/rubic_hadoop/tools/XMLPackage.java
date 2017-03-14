package cn.edu.cqupt.rubic_hadoop.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.output.Format;

/**
 * XML配置文件构造类
 * Created by Vigo on 15/11/30.
 */
public class XMLPackage {

    //构造XML
	/**
	 * 构造XML对象
	 * @param dataSetSource 源数据集路径
	 * @param subPathDir 工作路径
	 * @param parameters 算法所需参数
	 * @param thresHold 收敛阀值
	 * @param numIterators 迭代次数
	 * @return  Document对象
	 */
	public static Document packageToXML(String dataSetSource,String subPathDir,double[] parameters,Integer thresHold,Integer numIterators)
	{
		Element config = new Element("config");//根节点config
		Document configDoc = new Document(config);
		
		
		
		Element path = new Element("path");
		Element dataSetPath = new Element("dataSetPath");
		Text t2 = new Text(dataSetSource);
//		dataSetPath.setText(dataSetSource);
		dataSetPath.addContent(t2);
		
		Element subPath = new Element("subPath");
		Text t1 = new Text(subPathDir);
//		dataSetPath.setText(subPathDir);
		subPath.addContent(t1);
		path.addContent(dataSetPath);
		path.addContent(subPath);
		config.addContent(path);
		
		
		Element params = new Element("params");
		for (double d : parameters) {
			Element param = new Element("param");
			Text td = new Text(""+d);
//			param.setText(""+d);
			param.addContent(td);
			params.addContent(param);
		}
		config.addContent(params);
		
		Element threshold = new Element("threshold");
		Text tt = null;
		if(thresHold!=null){
			tt = new Text(""+thresHold);
//			threshold.setText(""+thresHold);
		}else{
			tt = new Text("");
//			threshold.setText("");
		}
		threshold.addContent(tt);
		config.addContent(threshold);
		
		Element numIterators_E = new Element("numIterators");
		Text tn = null;
		if(numIterators!=null){  
			tn = new Text(""+numIterators);
//			numIterators_E.setText(""+numIterators);
		}else{
			tn = new Text(""+10);
//			numIterators_E.setText("");
		}
		numIterators_E.addContent(tn);
		config.addContent(numIterators_E);
		
		/*Format format = Format.getCompactFormat();
		format.setIndent("");
		format.setEncoding("UTF-8");
		
		org.jdom.output.XMLOutputter outputer = new org.jdom.output.XMLOutputter(format);
		
		try {
			outputer.output(configDoc, new FileOutputStream(new File("D://config.xml")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		return configDoc;
	}
	
	
	public static void main(String[] args) {
		Document doc= XMLPackage.packageToXML("/Rubic/62/iris", "/Rubic/result/221", new double[]{3.0}, null, null);
		XMLParser.parseXML(doc);
	}

}
