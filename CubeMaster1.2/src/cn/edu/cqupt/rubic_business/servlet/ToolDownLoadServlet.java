package cn.edu.cqupt.rubic_business.servlet;

import java.io.*;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.cqupt.rubic_core.config.Configuration;
public class ToolDownLoadServlet extends HttpServlet {


    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		this.doPost(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fileType = request.getParameter("type");
        //根据前台传值选择下载路径
        if (fileType.equals("jar")) {
            String filePath = Configuration.UTIL_JAR;
            InputStream in = getFileInputStream(filePath);
            writeFileToBrowser(response,in,"RubicJar.jar");

        } else {
            String filePath = Configuration.UTIL_DOC;
            InputStream in = getFileInputStream(filePath);
            writeFileToBrowser(response,in,"RubicDoc.rar");
        }

    }


    private InputStream getFileInputStream(String filePath) throws FileNotFoundException {
        return new FileInputStream(filePath);
    }

    private void writeFileToBrowser(HttpServletResponse response, InputStream inputStream, String fileName) throws IOException {

        response.reset();
        response.setContentType("multipart/form-data");
        response.addHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        OutputStream out = response.getOutputStream();
        byte buffer[] = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        inputStream.close();
        out.close();
    }


}

