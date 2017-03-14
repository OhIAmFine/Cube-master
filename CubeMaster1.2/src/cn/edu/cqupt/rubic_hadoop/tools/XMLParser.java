package cn.edu.cqupt.rubic_hadoop.tools;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cn.edu.cqupt.rubic_hadoop.config.HadoopConfiguration;

/**
 * XML文件解析类
 * Created by Vigo on 15/12/3.
 */
public class XMLParser {
    //解析XML
	
	public static HadoopConfiguration parseXML(Document document)
	{
		
		Element config = document.getRootElement();
		
		Element path = config.getChild("path");
		
		Element dataSetPath = path.getChild("dataSetPath");
		String dataSetPathString = dataSetPath.getValue();
		System.out.println("dataSetPathString   "+dataSetPathString);
		
		Element subPath = path.getChild("subPath");
		String subPathString = subPath.getValue();
		System.out.println("subPathString   "+subPathString);
		
		Element params = config.getChild("params");
		List<Element> list = params.getChildren();
		double[] parameters = new double[list.size()];
		int i=0;
		for (Element element : list) {
			String value = element.getValue();
			double param = Double.parseDouble(value);
			parameters[i] = param;
			System.out.println(parameters[i]);
			i++;
		}
		
		
		Element threshold = config.getChild("threshold");
		String thresholdString = threshold.getValue();
		double d;
		if(thresholdString!=""&&thresholdString.length()>=1){
			d = Double.parseDouble(thresholdString);
		}else{
			d = 0.0;
		}
		
		Element numIterators = config.getChild("numIterators");
		String numIteratorsString = numIterators.getValue();
		int num;
		if(thresholdString!=""&&thresholdString.length()>=1){
			num = Integer.parseInt(numIteratorsString);
		}else{
			num = 10;
		}
		
		
		HadoopConfiguration conf = new HadoopConfiguration.HadoopConfigurationBuilder(dataSetPathString, subPathString)
		.threshold(d).numIterators(num).parameters(parameters).build();
		
		return conf;
	}
	
}
