package cn.edu.cqupt.rubic_business.Model.po;

import java.util.Date;

public class DataSetPo {
	private int dataset_id;
	private String dataset_name;
	private int dataset_type;
	private String associated_tasks;
	private int attribute_count;
	private String description;
	private String number_examples;
	private Date submit_datetime;
	private String area;
	private int download_count;
	private String file_path;
	private int ispublic;
	private String platform;
	public int getDataset_id() {
		return dataset_id;
	}
	public void setDataset_id(int dataset_id) {
		this.dataset_id = dataset_id;
	}
	public String getDataset_name() {
		return dataset_name;
	}
	public void setDataset_name(String dataset_name) {
		this.dataset_name = dataset_name;
	}
	public int getDataset_type() {
		return dataset_type;
	}
	public void setDataset_type(int dataset_type) {
		this.dataset_type = dataset_type;
	}
	public String getAssociated_tasks() {
		return associated_tasks;
	}
	public void setAssociated_tasks(String associated_tasks) {
		this.associated_tasks = associated_tasks;
	}
	public int getAttribute_count() {
		return attribute_count;
	}
	public void setAttribute_count(int attribute_count) {
		this.attribute_count = attribute_count;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNumber_examples() {
		return number_examples;
	}
	public void setNumber_examples(String number_examples) {
		this.number_examples = number_examples;
	}
	public Date getSubmit_datetime() {
		return submit_datetime;
	}
	public void setSubmit_datetime(Date submit_datetime) {
		this.submit_datetime = submit_datetime;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public int getDownload_count() {
		return download_count;
	}
	public void setDownload_count(int download_count) {
		this.download_count = download_count;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public int getIspublic() {
		return ispublic;
	}
	public void setIspublic(int ispublic) {
		this.ispublic = ispublic;
	}
	
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	@Override
	public String toString() {
		return "DataSetPo [dataset_id=" + dataset_id + ", dataset_name="
				+ dataset_name + ", dataset_type=" + dataset_type
				+ ", associated_tasks=" + associated_tasks
				+ ", attribute_count=" + attribute_count + ", description="
				+ description + ", number_examples=" + number_examples
				+ ", submit_datetime=" + submit_datetime + ", area=" + area
				+ ", download_count=" + download_count + ", file_path="
				+ file_path + ", ispublic=" + ispublic+", platform=" +platform+ "]";
	}
	

	
	
	
	
}