package cn.edu.cqupt.rubic_business.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.edu.cqupt.rubic_business.service.impl.DataSetServiceImpl;

public class Test extends HttpServlet {
	@Autowired
	private DataSetServiceImpl dataSetService;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//System.out.println(dataSetService.findAllDataSet());
		
	}

}
