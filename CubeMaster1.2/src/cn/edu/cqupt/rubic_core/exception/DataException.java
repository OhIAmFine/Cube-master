package cn.edu.cqupt.rubic_core.exception;

public class DataException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6955427685875952551L;

	/**
	 * 创建一个新的数据集异常
	 * 
	 * @param msg
	 */
	public DataException(String msg) {
		super(msg);
	}
	
	public DataException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
