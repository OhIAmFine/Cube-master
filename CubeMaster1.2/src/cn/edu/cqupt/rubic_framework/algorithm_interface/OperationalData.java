package cn.edu.cqupt.rubic_framework.algorithm_interface;

import cn.edu.cqupt.rubic_framework.model.MyStruct;
import cn.edu.cqupt.rubic_framework.service_interface.EventListener;

/**
 * 基本算法类，用于算法实现 实例函数在cqupt.example.algorithm.MatrixPlus
 * 
 * 
 * @author Felixly
 * 
 */
public interface OperationalData extends BasicAdapter {

	/**
	 * 运算过程，可输入输出多个结构体 输入算法的结构体数组 算法输出的结构体数组
	 * 
	 * @param input
	 * @param parameters
	 * @param configuration
	 * @return
	 * @throws ErrorInputFormatException
	 */
	public MyStruct[] run(MyStruct[] input, Object configuration,
			double... parameters) throws ErrorInputFormatException;

	/**
	 * 持久化模型，如果您除了得到算法运行的结果，还想保存算法运行后产生的模型，请实现该方法。
	 * Notes！：持久化以后的模型可以通过Rubic平台得到该模型的使用ID和模型修改密码
	 * @return
	 */
	public Object creatModel();
	
	/**
	 * 继续训练该模型/算法，该方法针对已经存在的模型，如果您想在模型存在的情况下继续训练，请实现该方法。
	 * 
	 * 参数意义：id：平台分配给该模型的唯一ID；一个算法对应多个ID，如bayes算法对应Alice使用bayes算法训练的模型和Bob使用bayes算发训练的模型。
	 * trainingPassword：更改模型的密码，使用数据+算法训练后的模型所有者持有。
	 * 
	 * @param input
	 * @param object
	 * @param
	 * @return
	 */
	public Object trainingModel(MyStruct[] input, Object object);
	
	/**
	 * 该模型/算法的使用者（非训练者）使用的接口，如果您想将模型提供给第三方使用，请实现该方法。
	 *例：1、带有确定K值得k-means算法就是一个提供给第三方使用的模型 。
	 *2、已经确定参数的线性模型就是一个提供给第三方使用的模型 。（重新训练参数可以使用trainingModel方法）。
	 *
	 * @param input
	 * @param object
	 * @return
	 */
	public MyStruct[] usingModel(MyStruct[] input, Object object);
	
}
