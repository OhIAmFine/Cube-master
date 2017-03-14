//package cn.edu.cqupt.rubic_core.execute;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//
//import cn.edu.cqupt.rubic_core.io.HDFSFileDaoImpl;
//import cn.edu.cqupt.rubic_core.runtime.RubicClassLoader;
//import cn.edu.cqupt.rubic_framework.algorithm_interface.ErrorInputFormatException;
//import cn.edu.cqupt.rubic_framework.algorithm_interface.OperationalDataOnHadoop;
//
//
//public class ProcessOnHadoop {
//
//	private OperationalDataOnHadoop operationalData;
//
//	/**
//	 *
//	 * @param jarFile jar文件路径
//	 * @param runClass 运行主类
//	 * @throws MalformedURLException
//	 * @throws ClassNotFoundException
//	 * @throws InstantiationException
//	 * @throws IllegalAccessException
//	 */
//	public ProcessOnHadoop(String jarFile, String runClass)
//			throws MalformedURLException, ClassNotFoundException,
//	InstantiationException, IllegalAccessException {
//
//		RubicClassLoader loader = new RubicClassLoader(jarFile);
//		this.operationalData = (OperationalDataOnHadoop) loader.getInstance(runClass);
//	}
//
//	/**
//	 *
//	 * @param dataSetPath 数据路径
//	 * @param subPath 输出路径
//	 * @param parameters 算法参数
//	 * @return
//	 * @throws OperationProcessException
//	 */
//	public String execute(String dataSetPath, String subPath, double[] parameters) throws OperationProcessException {
//		
//		HDFSFileDaoImpl hdfs = new HDFSFileDaoImpl();
//		try {
//			if(hdfs.isExist(subPath)){
//				hdfs.remove(subPath);
//			}
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		
//		// 分配唯一执行路径
//		// 运行算法
//		
//			String output;
//			try {
//				output = operationalData
//						.run(dataSetPath, subPath, null, parameters);
//			} catch (ErrorInputFormatException e) {
//				throw new OperationProcessException(e);
//			}
//			return output;
//		
//		
//	}
//
//	public static void main(String[] args) {
////		String dataId = null;
////		String algorithmId = "D://kmeans.jar";
////		double[] parameters = { 3 };
////		ProcessOnHadoop poh = new ProcessOnHadoop(dataId, algorithmId,
////				parameters);
////		poh.execute();
//	}
//}
