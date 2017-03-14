package cn.edu.cqupt.rubic_business.servlet;
/**
 * 
 * <p>Description:上传文件信息</p>
 * @author hey
 * @date 2015-9-20
 */
public class ProgressStatus {
	// 已经读取的长度
	private long bytesRead;

	// 文件的总长度
	private long contentLength;

	// 一共有多少个文件，用于多文件上传
	private int items;

	// 开始时间
	private long startTime = System.currentTimeMillis();

	// 预计多少时间完成
	private long times;

	private double rate;

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getTimes() {
		return times;
	}

	public void setTimes(long times) {
		this.times = times;
	}

	public ProgressStatus() {
		super();
	}

	public long getBytesRead() {
		return bytesRead;
	}

	public void setBytesRead(long bytesRead) {
		this.bytesRead = bytesRead;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public int getItems() {
		return items;
	}

	public void setItems(int items) {
		this.items = items;
	}

}
