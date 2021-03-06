package com.mct.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// File file = new File("D:\\upload_test\\pic_001.png");
		// download(file, resp);
		resp.setContentType("text/html;charset=GBK");
		// 得到下载文件的名字
		// String filename=request.getParameter("filename");
		// 解决中文乱码问题
		String filename = new String(req.getParameter("filename").getBytes(
				"iso-8859-1"), "gbk");
		// 创建file对象
		File file = new File(UpdateUserInfo.PHOTO_PATH + filename);
		// 设置response的编码方式
		resp.setContentType("application/x-msdownload");

		// 写明要下载的文件的大小
		resp.setContentLength((int) file.length());
		// 设置附加文件名
		// response.setHeader("Content-Disposition","attachment;filename="+filename);
		// 解决中文乱码
		resp.setHeader("Content-Disposition", "attachment;filename="
				+ new String(filename.getBytes("gbk"), "iso-8859-1"));
		// 读出文件到i/o流
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream buReader = new BufferedInputStream(fis);
		byte[] b = new byte[1024];// 相当于我们的缓存
		// 从response对象中得到输出流,准备下载
		OutputStream toClient = resp.getOutputStream();
		int num;
		// 开始循环下载
		while ((num = buReader.read(b)) != -1) {
			// 将b中的数据写到客户端的内存
			toClient.write(b, 0, num);
		}
		// 将写入到客户端的内存的数据,刷新到磁盘
		toClient.flush();
		toClient.close();
		fis.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
