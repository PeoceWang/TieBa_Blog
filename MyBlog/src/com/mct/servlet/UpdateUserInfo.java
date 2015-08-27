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
	// ע��
	private static final int TYPE_REGIST = 1;
	// ����
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

		// ����������λ��
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
		// ���ļ��ϴ���Я������
		if (type != 0
				&& (contentType.contains("multipart/form-data") || contentType
						.contains("multipart/mixed"))) {
			final long MAX_SIZE = 3 * 1024 * 1024;// �����ϴ��ļ����Ϊ 3M
			// ʵ����һ��Ӳ���ļ�����,���������ϴ����ServletFileUpload
			DiskFileItemFactory dfif = new DiskFileItemFactory();
			dfif.setSizeThreshold(4096);// �����ϴ��ļ�ʱ������ʱ����ļ����ڴ��С,������4K.���ڵĲ��ֽ���ʱ����Ӳ��
			String tempPath = PHOTO_PATH + "temp\\";
			// �ж���ʱ�ļ����Ƿ���ڣ��������򴴽���ʱ�ļ���
			UploadUtil.isDirsExist(tempPath);
			dfif.setRepository(new File(tempPath));
			// ���ô����ʱ�ļ���Ŀ¼,web��Ŀ¼�µ�ImagesUploadTempĿ¼
			// �����Ϲ���ʵ�����ϴ����
			ServletFileUpload sfu = new ServletFileUpload(dfif);
			sfu.setSizeMax(MAX_SIZE);// ��������ϴ��ߴ�

			try {
				// ��request�õ� ���� �ϴ�����б�
				List<FileItem> fileList = sfu.parseRequest(req);
				// �õ������ϴ����ļ�
				Iterator<FileItem> fileItr = fileList.iterator();
				// ѭ�����������ļ�
				while (fileItr.hasNext()) {
					FileItem fileItem = null;
					// �õ���ǰ�ļ�
					fileItem = (FileItem) fileItr.next();
					// ���Լ�form�ֶζ������ϴ�����ļ���(<input type="text" />��)
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
					// ��ͷ���ע��
					String u_name = PHOTO_PATH + saveName;
					// �����ļ�
					fileItem.write(new File(u_name));
					// ��ȡ��ǰ��Ŀ����ַ,�磺icon:http://a0:8080/MyBlog/d?filename=aaa.jpg
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
			// ��ͨ�ı��ύ���ϴ�����
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
				// ע���û�����photo����ΪҪ���ص�ͷ��ķ�������ַ���磺http://a0:8080/MyBlog/download?filename=abc.jpg
				result = UserDbUtil.getInstance().regist(DbUtil.openDb(),
						map.get("name"), map.get("psw"), map.get("photo"));
			}
		}
		if (result) {
			pw.write("{result:1}");
			// ע��ɹ�
		} else {
			pw.write("{result:-1}");
			// ע��ʧ��
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