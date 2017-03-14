package cn.edu.cqupt.rubic_framework.algorithm_interface;

import cn.edu.cqupt.rubic_framework.service_interface.EventListener;

/**
 * 基本适配器，如果界面类想对回调事件有所展示，就需要继承此接口
 * @author Felixly
 *
 */
public interface BasicAdapter {

	/**
	 * 注册监听器，接受自定义回调事件
	 * @param listener
	 */
	public void addActionListener(EventListener listener);
}
