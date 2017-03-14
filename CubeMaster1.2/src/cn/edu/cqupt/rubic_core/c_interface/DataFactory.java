package cn.edu.cqupt.rubic_core.c_interface;

import cn.edu.cqupt.rubic_core.exception.DataException;
import cn.edu.cqupt.rubic_framework.model.DataSet;


/**
 * 数据集工厂，基础IoC容器
 * 
 * @author Colin Wang *
 */
public interface DataFactory {

	DataSet getData(String name) throws DataException;

}
