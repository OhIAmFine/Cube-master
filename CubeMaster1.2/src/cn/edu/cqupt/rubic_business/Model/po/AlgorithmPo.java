package cn.edu.cqupt.rubic_business.Model.po;

import java.util.Date;

public class AlgorithmPo {
	private int algorithm_id;
	private String algorithm_name;
	private String platform;
	private String description;
	private String package_name;
	private int parameter_count;
	private String associated_tasks;
	private String data_requirements;
	private String in_pattern;
	private String out_pattern;
	private String scope;
	private Date submit_datetime;
	private int ispublic;
	private String data_test;
	private String file_path;
	
	public int getAlgorithm_id() {
		return algorithm_id;
	}
	public void setAlgorithm_id(int algorithm_id) {
		this.algorithm_id = algorithm_id;
	}
	public String getAlgorithm_name() {
		return algorithm_name;
	}
	public void setAlgorithm_name(String algorithm_name) {
		this.algorithm_name = algorithm_name;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform){
		this.platform= platform;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPackage_name() {
		return package_name;
	}
	public void setPackage_name(String package_name) {
		this.package_name = package_name;
	}
	public int getParameter_count() {
		return parameter_count;
	}
	public void setParameter_count(int parameter_count) {
		this.parameter_count = parameter_count;
	}
	public String getAssociated_tasks() {
		return associated_tasks;
	}
	public void setAssociated_tasks(String associated_tasks) {
		this.associated_tasks = associated_tasks;
	}
	public String getData_requirements() {
		return data_requirements;
	}
	public void setData_requirements(String data_requirements) {
		this.data_requirements = data_requirements;
	}
	public String getIn_pattern() {
		return in_pattern;
	}
	public void setIn_pattern(String in_pattern) {
		this.in_pattern = in_pattern;
	}
	public String getOut_pattern() {
		return out_pattern;
	}
	public void setOut_pattern(String out_pattern) {
		this.out_pattern = out_pattern;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Date getSubmit_datetime() {
		return submit_datetime;
	}
	public void setSubmit_datetime(Date submit_datetime) {
		this.submit_datetime = submit_datetime;
	}
	public int getIspublic() {
		return ispublic;
	}
	public void setIspublic(int ispublic) {
		this.ispublic = ispublic;
	}
	public String getData_test() {
		return data_test;
	}
	public void setData_test(String data_test) {
		this.data_test = data_test;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	@Override
	public String toString() {
		return "AlgorithmPo [algorithm_id=" + algorithm_id
				+ ", algorithm_name=" + algorithm_name + ", platform="
				+ platform + ", description=" + description + ", package_name="
				+ package_name + ", parameter_count=" + parameter_count
				+ ", associated_tasks=" + associated_tasks
				+ ", data_requirements=" + data_requirements + ", in_pattern="
				+ in_pattern + ", out_pattern=" + out_pattern + ", scope="
				+ scope + ", submit_datetime=" + submit_datetime
				+ ", ispublic=" + ispublic + ", data_test=" + data_test
				+ ", file_path=" + file_path + "]";
	}
	
	
}