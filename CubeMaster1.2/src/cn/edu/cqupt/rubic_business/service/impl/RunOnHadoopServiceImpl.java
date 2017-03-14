package cn.edu.cqupt.rubic_business.service.impl;

import cn.edu.cqupt.rubic_business.Model.po.*;
import cn.edu.cqupt.rubic_business.service.*;
import cn.edu.cqupt.rubic_core.config.Configuration;
import cn.edu.cqupt.rubic_core.execute.*;
import cn.edu.cqupt.rubic_core.io.HDFSConnection;
import cn.edu.cqupt.rubic_hadoop.excute.ProcessOnHadoop;
import cn.edu.cqupt.rubic_hadoop.tools.XMLPackage;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.jdom.Document;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Hadoop平台运行类
 * Created by Vigo on 15/11/16.
 */
public class RunOnHadoopServiceImpl {
    private DataSetService dataSetService;
    private AlgorithmService algorithmService;
    private AttributeService attributeService;
    private ResultService resultService;
    private ProcessRecordService processRecordService;
    @SuppressWarnings("unused")
	private ModelServiceImpl modelService;

    public RunOnHadoopServiceImpl(ServletContext servletContext) {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        this.dataSetService = webApplicationContext.getBean(DataSetService.class);
        this.algorithmService = webApplicationContext.getBean(AlgorithmService.class);
        this.attributeService = webApplicationContext.getBean(AttributeService.class);
        this.resultService = webApplicationContext.getBean(ResultService.class);
        this.processRecordService = webApplicationContext.getBean(ProcessRecordService.class);
        this.modelService = webApplicationContext.getBean(ModelServiceImpl.class);
    }

    @SuppressWarnings("finally")
    public Map<String, Object> run(Map<String, Object> request) {
        //返回给前端的运行状态
        Map<String, Object> response = new HashMap<String, Object>();

        //获取前端参数
        int userId = (Integer) request.get("userId");
        String platform = (String) request.get("platform");
        int[] dataIds = (int[]) request.get("dataIds");
        int algorithmId = (Integer) request.get("algorithmId");
        //参数信息
        double[] parameters = (double[]) request.get("parameters");

        //获取数据信息(多数据集)
        DataSetPo[] dataSetPos = getDataSetPos(dataIds);

        //获取一个数据集属性信息
        List<AttributePo> attrList = attributeService.getAttributesByDId(dataIds[0]);

        //构造一个数据路径
        String dataSetPath = Configuration.getHDFS()+dataSetPos[0].getFile_path();
        //构造输出目录
        String subPath = Configuration.getHDFS()+userId+"/result/"+System.currentTimeMillis();

        //获取一个算法信息
        AlgorithmPo algorithmPo = algorithmService.findAlgorithmById(algorithmId);

        //构造算法jar包路径以及算法执行类
        String jarFile = Configuration.getRubic() + algorithmPo.getFile_path();
        String runClass = algorithmPo.getPackage_name();


        //根据平台选择,获取process
//        ProcessOnHadoop process = null;
        cn.edu.cqupt.rubic_hadoop.excute.ProcessOnHadoop process =null;
        try {
            //加载算法
//            process = new ProcessOnHadoop(jarFile, runClass);
        	process = new ProcessOnHadoop(jarFile, runClass);
        } catch (ClassNotFoundException e) {
            response.put("if_success", "0");
            response.put("reason", "运行类没有找到");
            e.printStackTrace();
        } catch (InstantiationException e) {
            response.put("if_success", "0");
            response.put("reason", "运行类没有无参构造函数");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            response.put("if_success", "0");
            response.put("reason", "运行类非法");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            response.put("if_success", "0");
            response.put("reason", "该算法损坏");
            e.printStackTrace();
        } finally {//error1
            if (response.get("if_success") != null) {
                return response;
            }
        }

        //加载算法成功后，初始化运行记录，并插入DB
        ProcessRecordPo processRecord = initProcessRecord(dataSetPos[0], algorithmPo, platform, userId);

        //运行状态信息
        String run_state = "运行结束";//数据库processRecord表的状态字段
        Date endDate = null;//运行结束的时间

        //运行结果信息
        String outputPath = null;
        //MyStruct[] result = null;

        try {
            //执行算法
//            outputPath = process.execute(dataSetPath, subPath, parameters);
        	Document document = XMLPackage.packageToXML(dataSetPath, subPath, parameters, null, null);
        	outputPath = process.execute(document);
           // result = process.execute(inputs, null, parameters);
            //生成模型！！！！！！！！！！！！！！！！

            //获取运行结束时间
            endDate = new Date();
        } catch (OperationProcessException e) {
            response.put("if_success", "0");
            response.put("reason", "运算过程发生错误：" + " " + e.getMessage());
            run_state = "运行失败";
            e.printStackTrace();
        } finally {

            if (response.get("if_success")==null) {
                //添加运行状态
                response.put("if_success", "1");
                //更新processRecord部分信息
                processRecord.setProcess_end(endDate);
                processRecord.setRun_state(run_state);

                //更新运行结果到DB(修改此处！！！！！！！！！！修改outResult函数)
                outHadoopResult(outputPath, dataSetPos[0], attrList, processRecord);
            }else {
                //更新processRecord部分信息

                processRecordService.updateProcessRecordByID(processRecord);
            }


            return response;
        }
    }

    /**
     * 获取数据信息
     * @param dataIds
     * @return
     */
    private DataSetPo[] getDataSetPos(int[] dataIds) {
        DataSetPo[] dataSetPos = new DataSetPo[dataIds.length];
        for (int i = 0; i < dataIds.length; i++) {
            int dataId = dataIds[i];
            dataSetPos[i] = dataSetService.findDataSetById(dataId);
        }
        return dataSetPos;
    }

    /**
     * 初始化运行记录
     * @param dataSetpo
     * @param algorithmPo
     * @param platform
     * @param userId
     * @return
     */
    private ProcessRecordPo initProcessRecord(DataSetPo dataSetpo, AlgorithmPo algorithmPo, String platform, int userId) {
        ProcessRecordPo processRecord = new ProcessRecordPo();
        Date process_start = new Date();
        String json_detail = "{\"algorithm_id\":"
                + algorithmPo.getAlgorithm_id()
                + ",\"algorithm_name\":"
                + algorithmPo.getAlgorithm_name()
                + ",\"dataset_id\":"
                + dataSetpo.getDataset_id()
                + ","
                + "\"dataset_name\":"
                + dataSetpo.getDataset_name() + "," + "\"platform\":"
                + platform + "}";
        processRecord.setProcess_start(process_start);
        processRecord.setPlatform(platform);
        processRecord.setJson_detail(json_detail);
        processRecord.setRun_state("running");
        processRecord.setUser_id(userId);
        int processID = resultService.addProcessRecord(processRecord);
        processRecord.setProcess_id(processID);
        return processRecord;
    }

    /**
     * 合并输出目录下所有输出文件，更新DB
     * @param outputPath
     * @param dataSetPo
     * @param attrList
     * @param processRecord
     * @return
     */
    private void outHadoopResult(String outputPath, DataSetPo dataSetPo, List<AttributePo> attrList, ProcessRecordPo processRecord) {
    	
    	//将多个结果文件合并为mergerd-result
    	String resultFileName = outputPath+"/mergerd-result";
		try {
			FileSystem fs;
			fs = HDFSConnection.getFileSystem();
			Path src = new Path(outputPath);
	    	Path resultFile = new Path(resultFileName);//合并后的结果文件
	    	
	    	FileStatus[] status = fs.listStatus(src);
			FSDataOutputStream out = fs.create(resultFile);
			
			for (FileStatus fileStatus : status) {
				if(!fileStatus.isDir()){          //判断是否为文件夹，如果为文件夹则不合并
					Path file = fileStatus.getPath();
					
					FSDataInputStream in = fs.open(file);
					IOUtils.copyBytes(in, out, 4096, false);
					fs.deleteOnExit(file);   //删除源文件
					in.close();
				}
			}
			out.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	

        //找到数据集标签列，构造结果标签label，并插入attribute表中,返回结果标签属性id即lableId
        AttributePo lable = null;
        for (int i = 0; i < attrList.size(); i++) {
            AttributePo attributepo = attrList.get(i);
            if (attributepo.getAttribute_label() == 1){
                lable = attributepo;
                break;
            }
        }
        
        AttributePo resultLabel = new AttributePo();
        
        resultLabel.setAttribute_label(1);
        resultLabel.setAttribute_character(lable.getAttribute_character());
        resultLabel.setAttribute_missing(lable.getAttribute_missing());
        resultLabel.setAttribute_range(lable.getAttribute_range());
        resultLabel.setAttribute_type(lable.getAttribute_type());
        resultLabel.setAttribute_name("result_attribute");
        resultLabel.setAttribute_sequence(lable.getAttribute_sequence() + 1);
        int resultLabelId = attributeService.addAttribute(resultLabel);
        

        //构造结果集resultPo，并将结果集插入DB,返回结果集id
        ResultPo resultPo = dataSetPoToResultPo(dataSetPo, resultFileName);
        int resultdataset_id = resultService.insertResult(resultPo);

        //更新resultData-Attribute关系表
        ResultdatasetAttributeRelationshipPo relationship = new ResultdatasetAttributeRelationshipPo();
        for (AttributePo attr : attrList) {
            relationship.setResultdataset_id(resultdataset_id);
            relationship.setAttribute_id(attr.getAttribute_id());
            attributeService.addResultdatasetAttributeRelation(relationship);
        }
        
        relationship.setResultdataset_id(resultdataset_id);
        relationship.setAttribute_id(resultLabelId);
        attributeService.addResultdatasetAttributeRelation(relationship);

        //更新ProcessRecord表
        processRecord.setResult_path(resultFileName.substring(Configuration.getHDFS().length()));
        processRecord.setResultdataset_id(resultdataset_id);

        processRecordService.updateProcessRecordByID(processRecord);

    }


    /**
     * 将DataSetPo转为ResultPo，方便数据库插入
     * @param dataSetPo
     * @return
     */
    private ResultPo dataSetPoToResultPo(DataSetPo dataSetPo, String filePath) {
        ResultPo resultPo = new ResultPo();
        resultPo.setArea(dataSetPo.getArea());
        resultPo.setAssociated_tasks(dataSetPo.getAssociated_tasks());
        resultPo.setAttribute_count(dataSetPo.getAttribute_count() + 1);
        resultPo.setDataset_type(dataSetPo.getDataset_type());
        resultPo.setDescription(dataSetPo.getDescription());
        resultPo.setDownload_count(dataSetPo.getDownload_count());
        resultPo.setFile_path(filePath.substring(7));
        resultPo.setIspublic(dataSetPo.getIspublic());
        resultPo.setNumber_examples(dataSetPo.getNumber_examples());
        resultPo.setResultdataset_name(dataSetPo.getDataset_name());
        resultPo.setSubmit_datetime(dataSetPo.getSubmit_datetime());
        resultPo.setPlatform(dataSetPo.getPlatform());

        return resultPo;
    }


}
