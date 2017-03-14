package cn.edu.cqupt.rubic_framework.service_interface;

import cn.edu.cqupt.rubic_framework.algorithm_interface.Event;

/**
 * 监听器接口，实现自定义监听器，为回调函数提供信息（Event）
 * @author Felixly
 *
 */
public interface EventListener {

	/**
	 * 基本函数回调基本事件或者自定义事件E
	 * @param e
	 */
	public void callBackAndView(Event e);
	
	/**
	 * 状态改变回调函数
	 * @param e
	 */
	public void stuteChanged(Event e);
	

	public <T> T getContent();
}
