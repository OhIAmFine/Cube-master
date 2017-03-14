package cn.edu.cqupt.rubic_business.Model.po;

import java.util.Date;

public class ProcessRecordPo {
	
	private int process_id;
	private Date process_start;
	private Date process_end;
	private String json_detail;
	private String result_path;
	private String run_state;
	private int run_count;
	private int user_id;
	private String platform;
	private int resultdataset_id;
	public int getProcess_id() {
		return process_id;
	}
	public void setProcess_id(int process_id) {
		this.process_id = process_id;
	}
	public Date getProcess_start() {
		return process_start;
	}
	public void setProcess_start(Date process_start) {
		this.process_start = process_start;
	}
	public Date getProcess_end() {
		return process_end;
	}
	public void setProcess_end(Date process_end) {
		this.process_end = process_end;
	}
	public String getJson_detail() {
		return json_detail;
	}
	public void setJson_detail(String json_detail) {
		this.json_detail = json_detail;
	}
	public String getResult_path() {
		return result_path;
	}
	public void setResult_path(String result_path) {
		this.result_path = result_path;
	}
	public String getRun_state() {
		return run_state;
	}
	public void setRun_state(String run_state) {
		this.run_state = run_state;
	}
	public int getRun_count() {
		return run_count;
	}
	public void setRun_count(int run_count) {
		this.run_count = run_count;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public int getResultdataset_id() {
		return resultdataset_id;
	}
	public void setResultdataset_id(int resultdataset_id) {
		this.resultdataset_id = resultdataset_id;
	}
	
	
	
	

}
