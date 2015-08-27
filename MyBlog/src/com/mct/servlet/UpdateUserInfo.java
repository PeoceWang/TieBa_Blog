package com.mct.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mct.utils.DbUtil;
import com.mct.utils.UploadUtil;
import com.mct.utils.UserDbUtil;

public class UpdateUserInfo extends HttpServlet {
	public static final String PHOTO_PATH = "D:/blog/photo";
	// 注册
	private static final int TYPE_REGIST = 1;
	// 更新
	private static final int TYPE_UPDATE = 2;

	private int type;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = resp.getWriter();

		// 服务器工程位置
		// String path= this.getServletContext().getRealPath(PHOTO_PATH);

		String typeStr = req.getServletPath();
		if (null != typeStr) {
			typeStr = typeStr.substring(1);
			if (typeStr.equals("regist")) {
				type = TYPE_REGIST;
			} else {
				type = TYPE_UPDATE;
			}
		}
		String contentType = req.getContentType();
		Map<String, String> map = new HashMap<String, String>();
		// 从文件上传中携带参数
		if (type != 0
				&& (contentType.contains("multipart/form-data") || contentType
						.contains("multipart/mixed"))) {
			final long MAX_SIZE = 3 * 1024 * 1024;// 设置上传文件最大为 3M
			// 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
			DiskFileItemFactory dfif = new DiskFileItemFactory();
			dfif.setSizeThreshold(4096);// 设置上传文件时用于临时存放文件的内存大小,这里是4K.多于的部分将临时存在硬盘
			String tempPath = PHOTO_PATH + "temp\\";
			// 判断临时文件夹是否存在，不存在则创建临时文件夹
			UploadUtil.isDirsExist(tempPath);
			dfif.setRepository(new File(tempPath));
			// 设置存放临时文件的目录,web根目录下的ImagesUploadTemp目录
			// 用以上工厂实例化上传组件
			ServletFileUpload sfu = new ServletFileUpload(dfif);
			sfu.setSizeMax(MAX_SIZE);// 设置最大上传尺寸

			try {
				// 从request得到 所有 上传域的列表
				List<FileItem> fileList = sfu.parseRequest(req);
				// 得到所有上传的文件
				Iterator<FileItem> fileItr = fileList.iterator();
				// 循环处理所有文件
				while (fileItr.hasNext()) {
					FileItem fileItem = null;
					// 得到当前文件
					fileItem = (FileItem) fileItr.next();
					// 忽略简单form字段而不是上传域的文件域(<input type="text" />等)
					if (fileItem == null) {
						continue;
					}
					if (fileItem.isFormField()) {
						String fieldName = fileItem.getFieldName();
						String value = DbUtil.utfString(fileItem.getString());
						map.put(fieldName, value);
						continue;
					}
					String saveName;
					if (map.containsKey("name")) {
						saveName = map.get("name") + ".jpg";
					} else {
						saveName = fileItem.getFieldName() + ".jpg";
					}
					// String toDbpath=PHOTO_PATH+"/"+saveName;
					// 有头像的注册
					String u_name = PHOTO_PATH + saveName;
					// 保存文件
					fileItem.write(new File(u_name));
					// 获取当前项目的网址,如：icon:http://a0:8080/MyBlog/d?filename=aaa.jpg
					String basePath = req.getScheme() + "://"
							+ req.getServerName() + ":" + req.getServerPort()
							+ req.getContextPath() + "/";
					String iconUrl = basePath + "download?filename=" + saveName;
					map.put("photo", iconUrl);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// 普通文本提交中上传参数
			if (contentType.contains("application/x-www-form-urlencoded")) {
				Iterator it = req.getParameterMap().entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) it
							.next();
					map.put(entry.getKey().toString(), DbUtil.utfString(req
							.getParameter(entry.getKey().toString())));
				}
			} else {
				pw.write("{error:-1}");
			}
		}
		boolean result = false;
		System.out.println(map.toString());
		if (type == TYPE_UPDATE) {
			result = UserDbUtil.getInstance().updateUserInfo(DbUtil.openDb(),
					map, Integer.parseInt(map.get("id").toString()));
		} else {
			if (null == map.get("photo")) {
				result = UserDbUtil.getInstance().regist(DbUtil.openDb(),
						map.get("name"), map.get("psw"));
			} else {
				// 注册用户其中photo内容为要下载的头像的服务器地址，如：http://a0:8080/MyBlog/download?filename=abc.jpg
				result = UserDbUtil.getInstance().regist(DbUtil.openDb(),
						map.get("name"), map.get("psw"), map.get("photo"));
			}
		}
		if (result) {
			pw.write("{result:1}");
			// 注册成功
		} else {
			pw.write("{result:-1}");
			// 注册失败
		}
		pw.flush();
		pw.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
