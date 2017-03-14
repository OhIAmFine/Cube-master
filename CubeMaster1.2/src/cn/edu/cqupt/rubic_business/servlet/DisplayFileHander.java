package cn.edu.cqupt.rubic_business.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import cn.edu.cqupt.rubic_core.io.HDFSConnection;


/**
 * 
 * 处理结果集文件
 */
public class DisplayFileHander {
	
	//文件路径
	private String resultPath;
	
	private List<String[]> resultSource;
	
	private int viewtimes;
	
	private final int lineofView = 20;
	
	private String platform;
	
	public DisplayFileHander(String resultPath,int viewtimes,String platform){
		
		this.resultPath = resultPath;
		resultSource = new ArrayList<String[]>();
		this.viewtimes = viewtimes;
		this.platform = platform;
		
	}
	
	/**
	 * 根据请求次数解析文件数据
	 */
	public void parseFileMatrix(){
		BufferedReader br = null;
		try {
			
			if("\"hadoop\"".equals(platform)||"hadoop".equals(platform)){
				FileSystem fs = HDFSConnection.getFileSystem();
				InputStream in = fs.open(new Path(resultPath));
				System.out.println("read dataset from hdfs");
				InputStreamReader inr = new InputStreamReader(in);
				br = new BufferedReader(inr);
			}else{
				
				br = new BufferedReader(new FileReader(new File(resultPath)));
			}
			
			String line = null;
			
			for(int i = 0;(line = br.readLine()) != null && !line.equals("") && i < lineofView * viewtimes; i++){
				if(i >= lineofView * (viewtimes - 1)){
					line=line.replaceAll("\\s{1,}", ",");
					String[] data = line.split(",");
					resultSource.add(data);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if(br !=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 根据请求查看的次数返回相应的数组
	 * @return 
	 */
	public List<String[]> getDatasetDetailsByViewtimes(){
		parseFileMatrix();
		return resultSource;
		
	}
}
