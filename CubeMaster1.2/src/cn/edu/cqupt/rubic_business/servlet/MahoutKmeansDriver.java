package cn.edu.cqupt.rubic_business.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.Kluster;
import org.apache.mahout.clustering.syntheticcontrol.canopy.Job;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.VectorWritable;


import cn.edu.cqupt.rubic_framework.algorithm_interface.ErrorInputFormatException;
import cn.edu.cqupt.rubic_framework.algorithm_interface.OperationalDataOnHadoop;
import cn.edu.cqupt.rubic_hadoop.config.HadoopConfiguration;
/**
 * Mahout Kmeans Demo
 * @author he guangqin
 *
 */
public class MahoutKmeansDriver implements OperationalDataOnHadoop{
	
	private Configuration conf;
	private FileSystem fs;
	
	@Override
	public String run(HadoopConfiguration configuration)
			throws ErrorInputFormatException {
		
			this.conf = new Configuration();
//			this.conf = (Configuration) configuration;
			String subPath = configuration.getSubPath();
			String dataSource = configuration.getDataSetPath();
			String seqFileDir = subPath+"seqfile/";//经转换后的向量数据文件在HDFS系统上的存放位置
			String clusterDir = subPath+"cluster/";//初始化蔟的存放位置
			int k = conf.getInt("k", 3);//the number of clusters in Kmeans 聚类的数量
//			int maxIterations = conf.getInt("maxIterations", 10);//the int maximum number of iterations 最大迭代次数
			int maxIterations = configuration.getNumIterators();
			try {
				int label = conf.getInt("label", -1);
				
				String platform = init(dataSource,subPath);//初始化seqFileDir、clusterDir，并判断dataSource的平台（本地磁盘、HDFS）
				
				List<NamedVector> nvs = transformToVector(platform,dataSource,label);//源数据转换为向量数据
				
				writeSequenceFile(nvs,seqFileDir);//将向量数据经转码后写入HDFS文件系统
				
				initCluster(nvs,clusterDir,k);//初始化蔟中心
				
				
				Path resultPath = new Path(subPath);
				KMeansDriver.run(conf, new Path(seqFileDir), new Path(clusterDir), resultPath, 0.001, maxIterations, true, 0, false);
			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			
		
		
		return subPath;
	}
	
	/**
	 * 
	 * @param dataSource 数据源位置
	 * @param subPath 工作目录，所有结果文件都放在该目录下
	 * @return platform 源数据存放的文件系统平台
	 * @throws IOException
	 */
	private String init(String dataSource,String subPath) throws IOException{
		fs = FileSystem.get(conf);
		String platform;
		int index = dataSource.indexOf(":");
		if(index==-1||dataSource.substring(0, index).equalsIgnoreCase("hdfs")){
			platform = "hadoop";
		}else{
			platform = "java";
		}
		
		
		return platform;
	}
	
	
	/**
	 * 
	 * @param platform 源数据存放的文件系统平台
	 * @param dataSource
	 * @return List<NamedVector> nvs 经转换后的向量数据
	 * @throws IOException
	 */
	private List<NamedVector> transformToVector(String platform,String dataSource,int labelseq) throws IOException{
		
		InputStreamReader inr = null;
		BufferedReader br = null;
		
		if("java".equalsIgnoreCase(platform)){
			File dataSet = new File(dataSource);
			
			FileInputStream in = new FileInputStream(dataSet);
			inr = new InputStreamReader(in);
			br = new BufferedReader(inr);
		}else{
			Path dataSet = new Path(dataSource);
			FSDataInputStream in = fs.open(dataSet);
			inr = new InputStreamReader(in);
			br = new BufferedReader(inr);
		}
		
		
		List<NamedVector> nvs = new ArrayList<NamedVector>();
		NamedVector nv;
		String line = br.readLine();
		while(line!=null&&line!=""&&line.length()>0){
			String[] strs = line.split(",");
			List<String> ls = new ArrayList<String>();
			ls.addAll(Arrays.asList(strs));
			
			String label = null;
			if(labelseq>0)
			{
				label = ls.get(labelseq-1);
				ls.remove(labelseq-1);
			}
			
//			DecimalFormat dcmFormat = new DecimalFormat("0.00");
//			dcmFormat.format(number)
			BigDecimal bd = null;
//			bd = bd.setScale(2);
			double[] dl = new double[ls.size()];
			int i=0;
			for (String string : ls) {
				Double d = Double.valueOf(string);
				bd = new BigDecimal(d);
				bd.setScale(2, BigDecimal.ROUND_HALF_UP);
				dl[i] = bd.doubleValue();
				i++;
			}
			
			nv = new NamedVector(new DenseVector(dl), label);
			nvs.add(nv);
			
			line = br.readLine();
		}
		System.out.println(nvs.toString());
		return nvs;
		
	}
	
	/**
	 * 向量数据经转码后写入HDFS文件系统
	 * @param nvs
	 * @return
	 * @throws IOException
	 */
	private Path writeSequenceFile(List<NamedVector> nvs,String seqFileDir) throws IOException {
		
		Path seqFilePath = new Path(seqFileDir+"part-s-00000");
		
		SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, seqFilePath, Text.class, VectorWritable.class);
		
		VectorWritable vec = new VectorWritable();
		for(NamedVector vector:nvs){
			vec.set(vector);
			writer.append(new Text(vector.getName()), vec);
		}
		writer.close();
		System.out.println("ok");
		
		return seqFilePath;
	}
	
	/**
	 * 初始化聚类蔟中心
	 * @param nvs
	 * @return
	 * @throws IOException
	 */
	private Path initCluster(List<NamedVector> nvs,String clusterDir,int k) throws IOException{
		
		Path clusterPath = new Path(clusterDir+"part-c-00000");
		
		DistanceMeasure measure = new EuclideanDistanceMeasure();//距离测度
		
		
//		clusterPath = RandomSeedGenerator.buildRandom(conf, path, clusterPath, 10, measure);
		
		SequenceFile.Writer Cwriter = new SequenceFile.Writer(fs, conf, clusterPath,
				Text.class, Kluster.class);
		
		for(int i=0;i<k;i++){
			NamedVector vector = nvs.get(i);
			Kluster cluster = new Kluster(vector, i, measure);
			Cwriter.append(new Text(cluster.getIdentifier()), cluster);
		}
		Cwriter.close();
		
		return clusterPath;
		
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		
		
		Configuration configuration = new Configuration();
//		configuration.setInt("label", 5);
//		MahoutKmeansDriver v = new MahoutKmeansDriver();
//		try {
//			v.run("D:/ADT/iris.data", "/Rubic/62/mahout/2015_11_23_23_08/kmens+iris/", configuration, 0,1);
//		} catch (ErrorInputFormatException e) {
//			e.printStackTrace();
//		}
		
//		Job.main(new String[]{});
		org.apache.mahout.clustering.syntheticcontrol.kmeans.Job.run(configuration, new Path("/user/hadoop/testdata/synthetic_control.data"), new Path("/Rubic/62/canopytest/synthetic_control/test3/"), new EuclideanDistanceMeasure(), 80, 55, 0.0, 10);
	
	}

	
	
	
	

}
