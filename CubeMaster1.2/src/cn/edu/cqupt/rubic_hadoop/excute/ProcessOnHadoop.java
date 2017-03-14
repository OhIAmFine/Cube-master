package cn.edu.cqupt.rubic_hadoop.excute;

import cn.edu.cqupt.rubic_core.execute.OperationProcessException;
import cn.edu.cqupt.rubic_framework.algorithm_interface.ErrorInputFormatException;
import cn.edu.cqupt.rubic_framework.algorithm_interface.OperationalDataOnHadoop;
import cn.edu.cqupt.rubic_hadoop.config.HadoopConfiguration;
import cn.edu.cqupt.rubic_hadoop.tools.XMLParser;


import java.net.MalformedURLException;

import org.jdom.Document;

/**
 * Hadoop平台执行类
 * Created by Vigo on 15/11/30.
 */
public class ProcessOnHadoop {

	private OperationalDataOnHadoop operationalData;

	private HadoopConfiguration configuration;	//配置文件类

	/**
	 * 加载算法
	 * @param jarFile jar文件路径
	 * @param runClass 运行主类
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public ProcessOnHadoop(String jarFile, String runClass)
			throws MalformedURLException, ClassNotFoundException,
	InstantiationException, IllegalAccessException {

		//RubicClassLoader loader = new RubicClassLoader(jarFile);
		//this.operationalData = (OperationalDataOnHadoop) loader.getInstance(runClass);
	}

	/**
	 * 解析XML文件并且运行Hadoop平台上算法
	 * @param document xml配置文件对象
	 * @return
	 * @throws OperationProcessException
	 * @throws ErrorInputFormatException
	 */
	public String execute(Document document) throws OperationProcessException, ErrorInputFormatException {
		//解析XML文件对象，赋值给configuration对象

		configuration = XMLParser.parseXML(document);
		String output = operationalData.run(configuration);
		return output;
	}

	public static void main(String[] args) {

	}
}
