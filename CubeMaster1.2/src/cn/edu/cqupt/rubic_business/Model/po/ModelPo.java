package cn.edu.cqupt.rubic_business.Model.po;

import java.util.Arrays;

public class ModelPo {
	private Integer modelId;
	private String VerifyCode;
	private String modelName;
	private String modelDescription;
	private Integer userId;
	private Integer algorithmId;
	private byte[] object;
	private byte[] xmlInfo;
	private String modelInfo;

	public byte[] getXmlInfo() {
		return xmlInfo;
	}

	public String getModelInfo() {
		return modelInfo;
	}

	public void setModelInfo(String modelInfo) {
		this.modelInfo = modelInfo;
	}

	public void setXmlInfo(byte[] xmlInfo) {
		this.xmlInfo = xmlInfo;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getAlgorithmId() {
		return algorithmId;
	}

	public void setAlgorithmId(Integer algorithmId) {
		this.algorithmId = algorithmId;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public String getVerifyCode() {
		return VerifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		VerifyCode = verifyCode;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelDescription() {
		return modelDescription;
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public byte[] getObject() {
		return object;
	}

	public void setObject(byte[] object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "ModelPo [modelId=" + modelId + ", VerifyCode=" + VerifyCode
				+ ", modelName=" + modelName + ", modelDescription="
				+ modelDescription + ", userId=" + userId + ", algorithmId="
				+ algorithmId + ", object=" + Arrays.toString(object)
				+ ", xmlInfo=" + xmlInfo + "]";
	}

	public static void main(String[] args) {
		int[] bytes = new int[1024 * 1024 * 100];
		// bytes.
	}
}
