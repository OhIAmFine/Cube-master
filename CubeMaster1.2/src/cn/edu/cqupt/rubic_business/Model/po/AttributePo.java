package cn.edu.cqupt.rubic_business.Model.po;

public class AttributePo {
	private int attribute_id;
	private String attribute_name;
	private String attribute_type;
	private String attribute_range;
	private String attribute_missing;
	private int attribute_character;
	private int attribute_label;
	private int attribute_sequence;

	public int getAttribute_id() {
		return attribute_id;
	}

	public void setAttribute_id(int attribute_id) {
		this.attribute_id = attribute_id;
	}

	public String getAttribute_name() {
		return attribute_name;
	}

	public void setAttribute_name(String attribute_name) {
		this.attribute_name = attribute_name;
	}

	public String getAttribute_type() {
		return attribute_type;
	}

	public void setAttribute_type(String attribute_type) {
		this.attribute_type = attribute_type;
	}

	public String getAttribute_range() {
		return attribute_range;
	}

	public void setAttribute_range(String attribute_range) {
		this.attribute_range = attribute_range;
	}

	public String getAttribute_missing() {
		return attribute_missing;
	}

	public void setAttribute_missing(String attribute_missing) {
		this.attribute_missing = attribute_missing;
	}

	public int getAttribute_label() {
		return attribute_label;
	}

	public void setAttribute_label(int attribute_label) {
		this.attribute_label = attribute_label;
	}

	public int getAttribute_character() {
		return attribute_character;
	}

	public void setAttribute_character(int attribute_character) {
		this.attribute_character = attribute_character;
	}

	public int getAttribute_sequence() {
		return this.attribute_sequence;
	}

	public void setAttribute_sequence(int attribute_sequence) {
		this.attribute_sequence = attribute_sequence;
	}

	@Override
	public String toString() {
		return "AttributePo [attribute_id=" + attribute_id
				+ ", attribute_name=" + attribute_name + ", attribute_type="
				+ attribute_type + ", attribute_range=" + attribute_range
				+ ", attribute_missing=" + attribute_missing
				+ ", attribute_character=" + attribute_character
				+ ", attribute_label=" + attribute_label
				+ ", attribute_sequence=" + attribute_sequence + "]";
	}

}
