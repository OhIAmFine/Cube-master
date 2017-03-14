package cn.edu.cqupt.rubic_business.Model.po;

/**
 * Created by LY on 2015/12/3.
 */

public class ModelAPIPo {
    private  int model_api_id;
    private int model_id;
    private String api_description;
    private String data_description;
    private String api_example;
    private String api_type;
    private String version;
    private String api_name;
    private String interface_method;
    private String interface_add;
    private String example_add;
    private String format;
   
	public String getApi_name() {
		return api_name;
	}

	public void setApi_name(String api_name) {
		this.api_name = api_name;
	}

	public int getModel_api_id() {
        return model_api_id;
    }

    public void setModel_api_id(int model_api_id) {
        this.model_api_id = model_api_id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApi_type() {
        return api_type;
    }

    public void setApi_type(String api_type) {
        this.api_type = api_type;
    }


    
    public String getApi_example() {
		return api_example;
	}

	public void setApi_example(String api_example) {
		this.api_example = api_example;
	}

	public String getData_description() {
        return data_description;
    }

    public void setData_description(String data_description) {
        this.data_description = data_description;
    }

	public String getInterface_method() {
		return interface_method;
	}

	public void setInterface_method(String interface_method) {
		this.interface_method = interface_method;
	}

	public String getInterface_add() {
		return interface_add;
	}

	public void setInterface_add(String interface_add) {
		this.interface_add = interface_add;
	}

	public String getExample_add() {
		return example_add;
	}

	public void setExample_add(String example_add) {
		this.example_add = example_add;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getApi_description() {
        return api_description;
    }

    public void setApi_description(String api_description) {
        this.api_description = api_description;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

	@Override
	public String toString() {
		return "ModelAPIPo [model_api_id=" + model_api_id + ", model_id="
				+ model_id + ", api_description=" + api_description
				+ ", data_description=" + data_description + ", api_example="
				+ api_example + ", api_type=" + api_type + ", version="
				+ version + ", api_name=" + api_name + ", interface_method="
				+ interface_method + ", interface_add=" + interface_add
				+ ", example_add=" + example_add + ", format=" + format + "]";
	}



}
