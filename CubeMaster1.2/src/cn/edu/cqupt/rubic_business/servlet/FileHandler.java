package cn.edu.cqupt.rubic_business.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @description 文件处理类
 * @author Wong JW
 * @date 2015年6月3日 下午1:06:06
 * @version 2.0
 */
public class FileHandler {

	/**
	 * @description 创建文件夹
	 * @param path 路径
	 * @return
	 */
	public File createFile(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
	
	/**
	 * @description 创建临时文件夹
	 * @param path 存储文件路径
	 * @return
	 */
	public File createTempFile(String path) {
		int index = path.lastIndexOf("\\");
		String subPath = path.substring(0, index);
		String tempPath = subPath + "\\temp";
		return new File(tempPath);
	}

	/**
	 * @description 删除文件夹 /文件
	 * @param file 待删除文件
	 * @throws FileNotFoundException 文件不存在 
	 */
	public static void deleteFile(File file) throws FileNotFoundException {
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File item : files) {
					deleteFile(item);
				}
				file.delete();
			}
		} else {
			throw new FileNotFoundException();
		}
	}
	
	/**
	 * @description 读取文件某一列数据
	 * @param path 文件路径
	 * @param columnSequence 列序 从1开始
	 * @return
	 */
	public static String[] readOneColumn(String path, int columnSequence) {
		return readOneColumn(new File(path), columnSequence);
	}
	
	/**
	 * @description 读取文件某一列数据
	 * @param file 文件
	 * @param columnSequence  列序  从1开始
	 * @return
	 */
	public static String[] readOneColumn(File file, int columnSequence) {
		
		BufferedReader reader = null;
		List<String> dataList = new ArrayList<String>();
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			String regex = "\\,\\s{0,}|\\s{1,}";
			String replacement = ",";
			while((line = reader.readLine()) != null) {
				line = line.trim();
				line = Pattern.compile(regex).matcher(line).replaceAll(replacement);
				dataList.add(line.split(replacement)[columnSequence - 1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(reader != null) 
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return dataList.toArray(new String[]{});
		
	}
	
	/**
	 * @description 从文件读取某一列
	 * @param path 文件路径
	 * @param columnSequence 列序 从0开始
	 * @return
	 */
	public static List<String> readColumn(String path, int columnSequence) {
		
		File file = new File(path);
		
		BufferedReader br = null;
		List<String> list = new ArrayList<String>();
		
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line = null;
			String regex = "\\,\\s{0,}|\\s{1,}";
			String replacement = ",";
			while( (line = br.readLine()) != null) {
				line = line.trim();
				line = Pattern.compile(regex).matcher(line).replaceAll(replacement);
				list.add(line.split(replacement)[columnSequence - 1]);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
