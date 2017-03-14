package cn.edu.cqupt.rubic_business.servlet;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

/**
 * 
 * <p>
 * Description:上传文件的进度的监听器
 * </p>
 * 
 * @author hey
 * @date 2015-9-20
 */
public class UploadListener implements ProgressListener {

	private ProgressStatus status = null;
	public HttpSession session = null;

	public UploadListener(HttpSession session) {

		this.session = session;
		status = (ProgressStatus) session.getAttribute("session_status");
		if (status == null) {
			this.status = new ProgressStatus();
		}
	}

	@Override
	public void update(long bytesRead, long cotentLength, int items) {
		status.setBytesRead(bytesRead);
		status.setContentLength(cotentLength);
		status.setItems(items);
		session.setAttribute("session_status", status);
	}

}
