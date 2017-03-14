package cn.edu.cqupt.rubic_business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.cqupt.rubic_business.Model.po.AlgorithmPo;
import cn.edu.cqupt.rubic_business.Model.po.AttributePo;
import cn.edu.cqupt.rubic_business.Model.po.DataSetPo;
import cn.edu.cqupt.rubic_business.Model.po.ModelPo;
import cn.edu.cqupt.rubic_business.dao.ModelDao;
import cn.edu.cqupt.rubic_framework.algorithm_interface.OperationalData;
import cn.edu.cqupt.rubic_framework.model.MyStruct;

/**
 * <p>Title: ModelServiceImpl
 * <p>Description:运行service层具体实现类 </p>
 * @author Hey
 * @data 2015-11-13
 */
@Service("modelService")
public class ModelServiceImpl{
	@Autowired
	private ModelDao modelDao;

	/**
	 * @description:生成运行模型，插入DB
	 * @author hey
	 * @param obj 模型对象
	 * @param algorithmPo 算法对象
	 * @param dataSetPo 数据对象
	 * @param userId 模型拥有者
	 * @return modelId
	 * @throws Exception
	 */
	public Integer createModel(Object obj,AlgorithmPo algorithmPo, DataSetPo[] dataSetPos, List<AttributePo> attrList,int userId,double[] parameters)
			throws Exception {

		ModelPo model = new ModelPo();

		//给模型属性赋值
		String algorithmName = algorithmPo.getAlgorithm_name();
		String dataName = dataSetPos[0].getDataset_name();
		model.setModelDescription(algorithmName + "("+ algorithmPo.getAssociated_tasks() + ")" + dataName + "("+ dataSetPos[0].getAssociated_tasks() + ")");
		model.setModelName(algorithmName + dataName);
		model.setVerifyCode(System.currentTimeMillis() + "");
		model.setAlgorithmId(algorithmPo.getAlgorithm_id());
		model.setUserId(userId);

		byte[] bytes = getByteFromObject(obj);
		model.setObject(bytes);

		//构造xml文件
		byte[] modelXmlInfo=createInfoModel(algorithmPo,attrList,dataSetPos,userId,parameters);
		model.setXmlInfo(modelXmlInfo);

		modelDao.insretModel(model);
		return model.getAlgorithmId();
	}

	/**
	 * @description:使用模型
	 * @author hey
	 * @param operationalData
	 * @param input
	 * @param modelId
	 * @return
	 * @throws Exception
	 */
	public MyStruct [] useringModel(OperationalData operationalData,MyStruct[] input,int modelId) throws Exception{

		ModelPo model=modelDao.findModelById(modelId);
		String modeInfo=(String)getObjectFromByte(model.getXmlInfo());
		model.setModelInfo(modeInfo);
		return operationalData.usingModel(input, getObjectFromByte(model.getObject()));
	}

	/**
	 * @description:训练模型，将训练以后的模型更新到数据库
	 * @author hey
	 * @param model
	 * @throws Exception
	 */
	public void trainModel(ModelPo model) throws Exception {

		modelDao.updateModel(model);
	}

	/**
	 * @description:删除模型
	 * @author hey
	 * @param modelId
	 * @throws Exception
	 */
	public void deleteModel(Integer modelId) throws Exception {

		modelDao.deleteModel(modelId);
	}

	/**
	 * @description:查找模型
	 * @author hey
	 * @param modelId
	 * @return
	 * @throws Exception
	 */
	public ModelPo findModelById(Integer modelId) throws Exception {

		ModelPo model = modelDao.findModelById(modelId);

		return model;
	}

	/**
	 * @description:查找该用户的所有模型
	 * @author hey
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<ModelPo> findModelByUserId(Integer userId) throws Exception {

		return modelDao.findModelByUserId(userId);
	}

	/**
	 * @description:查找该算法的所有模型
	 * @author hey
	 * @param algorithmId
	 * @return
	 * @throws Exception
	 */
	public List<ModelPo> findModelByAid(Integer algorithmId) throws Exception {

		return modelDao.findModelByAid(algorithmId);
	}

	/**
	 * @description:将对象转化为数组
	 * @author hey
	 * @param bytes
	 * @return
	 */
	public byte[] getByteFromObject(Object obj) {
		ByteArrayOutputStream output = null;
		ObjectOutputStream oop = null;
		try {
			output = new ByteArrayOutputStream();
			oop = new ObjectOutputStream(output);
			oop.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(oop);
			close(output);
		}

		return output.toByteArray();
	}

	/**
	 * @description:将数组转化为对象
	 * @author hey
	 * @param bytes
	 * @return
	 */
	public Object getObjectFromByte(byte[] bytes) {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		Object object = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			object = ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			close(ois);
			close(bis);
		}

		return object;
	}

	/**
	 * @description:关闭输出流
	 * @author hey
	 * @param out
	 */
	public void close(OutputStream out) {
		if (out != null)
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * @description:关闭输入流
	 * @author hey
	 * @param in
	 */
	public void close(InputStream in) {
		if (in != null)
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * @description:生成xml文件
	 * @author hey
	 * @param algorithmPo
	 * @param attrList
	 * @param dataSetPos
	 * @param usersId
	 * @return
	 */
	private byte[] createInfoModel(AlgorithmPo algorithmPo,
			List<AttributePo> attrList, DataSetPo[] dataSetPos, int usersId,double[]algorithmParameters) {

		Element root = new Element("model");
		Document doc = new Document(root);

		// userId
		Element userId = new Element("userId");
		userId.setText(usersId + "");
		root.addContent(userId);
		

		// 算法
		Element algorithms = new Element("algorithms");
		root.addContent(algorithms);
		
		Element algorithm = new Element("algorithm");
		algorithms.addContent(algorithm);
		
		//算法Id
		Element algorithmId=new Element("algorithmId");
		algorithmId.setText(algorithmPo.getAlgorithm_id()+"");
		algorithm.addContent(algorithmId);
		
		//算法保存路径
		Element jarFile=new Element("jarFile");
		jarFile.setText(algorithmPo.getFile_path()+"");
		algorithm.addContent(jarFile);
		
		//算法运行主类
		Element runClass=new Element("runClass");
		runClass.setText(algorithmPo.getPackage_name()+"");
		algorithm.addContent(runClass);
		
		//算法描述
		Element description=new Element("description");
		description.setText(algorithmPo.getDescription());
		algorithm.addContent(description);
		
		//数据
		Element dataSets=new Element("dataSets");
		root.addContent(dataSets);
		
		//数据信息
		for (DataSetPo dataSetPo : dataSetPos) {
			// 数据
			Element dataSet = new Element("dataSet");
			dataSets.addContent(dataSet);
			//数据ID
			Element dataSetId=new Element("dataSetId");
			dataSet.addContent(dataSetId);
			dataSetId.setText(dataSetPo.getDataset_id()+"");

			// 数据里面的attributes
			Element attributes = new Element("attributes");
			dataSet.addContent(attributes);
			Integer labelSequence=null;

			for (int i = 0; i < attrList.size(); i++) {

				AttributePo attr = attrList.get(i);
				//属性
				Element attribute = new Element("attribute");
				attributes.addContent(attribute);
				//属性名称
				Element name = new Element("name");
				name.setText(attr.getAttribute_name());
				attribute.addContent(name);
				//属性类型
				Element type = new Element("type");
				type.setText(attr.getAttribute_type());
				attribute.addContent(type);
				//属性列数
				Element order = new Element("order");
				order.setText("" + (i + 1));
				attribute.addContent(order);
				
				//该属性是否结果列
				if(attrList.get(i).getAttribute_label()==1){
					Element label=new Element("label");
					label.setText((i+1)+"");
					attribute.addContent(label);
				}
			}
		}
		
		//参数信息
		if(algorithmParameters!=null){
			
			Element parameters=new Element("parameters");
			root.addContent(parameters);
			
			for(int i=0;i<algorithmParameters.length;i++){
				Element parameter=new Element("parameter");
				parameter.setText(algorithmParameters[i]+"");
				parameters.addContent(parameter);
			}
			
		}

		XMLOutputter XMLOut = new XMLOutputter();
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		try {  
			Format f = Format.getPrettyFormat();  
			f.setEncoding("UTF-8");//default=UTF-8  
			XMLOut.setFormat(f);  

			XMLOut.output(doc,out);  

			return out.toByteArray();
		} catch (Exception e) {  
			e.printStackTrace();  
		}
		return null;
	}
}
