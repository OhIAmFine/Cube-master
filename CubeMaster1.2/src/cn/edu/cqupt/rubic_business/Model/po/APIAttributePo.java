package cn.edu.cqupt.rubic_business.Model.po;

/**
 * Created by LY on 2015/12/3.
 */
public class APIAttributePo {

    private int api_attribute_id;
    private int model_api_id;
    private String attribute_item;
    private String attribute_name;
    private String attribute_type;
    private String attribute_example;
    private String type;


    public int getApi_attribute_id() {
        return api_attribute_id;
    }

    public void setApi_attribute_id(int api_attribute_id) {
        this.api_attribute_id = api_attribute_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttribute_example() {
        return attribute_example;
    }

    public void setAttribute_example(String attribute_example) {
        this.attribute_example = attribute_example;
    }

    public String getAttribute_type() {
        return attribute_type;
    }

    public void setAttribute_type(String attribute_type) {
        this.attribute_type = attribute_type;
    }


    public String getAttribute_item() {
		return attribute_item;
	}

	public void setAttribute_item(String attribute_item) {
		this.attribute_item = attribute_item;
	}

	public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public int getModel_api_id() {
        return model_api_id;
    }

    public void setModel_api_id(int model_api_id) {
        this.model_api_id = model_api_id;
    }

	@Override
	public String toString() {
		return "APIAttributePo [api_attribute_id=" + api_attribute_id
				+ ", model_api_id=" + model_api_id + ", attribute_item="
				+ attribute_item + ", attribute_name=" + attribute_name
				+ ", attribute_type=" + attribute_type + ", attribute_example="
				+ attribute_example + ", type=" + type + "]";
	}


}
