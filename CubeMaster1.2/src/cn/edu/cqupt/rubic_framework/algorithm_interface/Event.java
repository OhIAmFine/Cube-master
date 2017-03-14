package cn.edu.cqupt.rubic_framework.algorithm_interface;

/**
 * 事件接口
 * @author Felix
 *
 */
public interface Event {

	/**
	 * 获得按界面需求封装好的自定义事件内容
	 * 
	 * @return
	 */
	Object getContent();

	/**
	 * 输出事件基本内容
	 */
	void print();
}
