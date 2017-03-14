package cn.edu.cqupt.rubic_core.config;

import java.io.File;

import cn.edu.cqupt.rubic_business.Model.po.UserPo;

/**
 * Configuration
 * @author dave
 * @author lw
 * @date 2015-9-1
 */
public class Configuration {

//	public static final String RUBIC_URL = "http://202.202.43.206/rubic/protocol_1";
	//----公测----
	public static final String RUBIC_URL = "http://172.22.146.4/rubic/runSingleAlgorithm";

	/**
	 * 文件服务器ip
	 */
	public static final String SERVER_IP ="172.22.146.5";

	/**
	 * 文件服务器端口号
	 */
	public static final int SERVER_PORT =1234;

	/**
	 * 从文件服务器上接受文件存放临时目录
	 */

	private String TMP_PATH;

	/**
	 * 算法存放路径根目录
	 */
	private String ALGORITHM_PATH;

	/**
	 * 运行结果保存路径根目录
	 */
	private String RESULT_DATA_FILE_PATH;
	/**
	 * 数据存放根目录
	 */
	private String DATASET_PATH;
	/**
	 * 数据集在HDFS系统上的存放根目录
	 */
	private String HDFS_DATASET_PATH;
	
	private String HDFS_RESULT_DATA_FILE_PATH;
	
	/**
	 * 文件分隔符
	 */
	private static final String separator = File.separator;
	/**
	 * Rubic根目录
	 */
	private static final String RUBIC_FILE_PATH = "C:" + separator + "Rubic"+separator;
	
	/**
	 * HDFS根目录 
	 */
	private static final String HDFS = "/Rubic/";
	
	/**
	 * jar下载目录
	 */
	public static final String UTIL_JAR = RUBIC_FILE_PATH  + "util"
			+ separator + "jar" + separator + "algorithm_interface6.2.jar";
	/**
	 * 文档下载根目录
	 */
	public static final String UTIL_DOC = RUBIC_FILE_PATH  + "util"
			+ separator + "doc" + separator + "rubic_doc.rar";

	public Configuration() {
		super();
	}

	public Configuration(UserPo user) {
		this.ALGORITHM_PATH = RUBIC_FILE_PATH + user.getUser_id() + "\\algorithm";
		this.RESULT_DATA_FILE_PATH = RUBIC_FILE_PATH + user.getUser_id()
				+ "\\result";
		this.DATASET_PATH = RUBIC_FILE_PATH + user.getUser_id() + "\\dataset";
		this.HDFS_DATASET_PATH = "/Rubic/" + user.getUser_id() + "/dataset";
		this.HDFS_RESULT_DATA_FILE_PATH = "/Rubic/" + user.getUser_id() + "/result/";
        //临时文件目录
        this.TMP_PATH = RUBIC_FILE_PATH + user.getUser_id() + File.separator + "tmp_" + System.currentTimeMillis();
	}

	public String getALGORITHM_PATH() {
		return ALGORITHM_PATH;
	}

	public String getRESULT_DATA_FILE_PATH() {
		return RESULT_DATA_FILE_PATH;
	}

	public String getDATASET_PATH() {
		return DATASET_PATH;
	}

	public static String getRubic() {
		return RUBIC_FILE_PATH;
	}
	
	public static String getHDFS() {
		return HDFS;
	}

	public void setRESULT_DATA_FILE_PATH(String rESULT_DATA_FILE_PATH) {
		RESULT_DATA_FILE_PATH = rESULT_DATA_FILE_PATH;
	}

	public String getHDFS_DATASET_PATH() {
		return HDFS_DATASET_PATH;
	}

	public void setHDFS_DATASET_PATH(String hDFS_DATASET_PATH) {
		HDFS_DATASET_PATH = hDFS_DATASET_PATH;
	}

	public String getHDFS_RESULT_DATA_FILE_PATH() {
		return HDFS_RESULT_DATA_FILE_PATH;
	}

	public void setHDFS_RESULT_DATA_FILE_PATH(String hDFS_RESULT_DATA_FILE_PATH) {
		HDFS_RESULT_DATA_FILE_PATH = hDFS_RESULT_DATA_FILE_PATH;
	}

    public String getTMP_PATH(){
        return TMP_PATH;
    }
	
}
