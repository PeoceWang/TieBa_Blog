package com.mct.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mct.utils.DbUtil;
import com.mct.utils.ReplyUtil;

public class PublishReplySevlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 获取帖子id
		int article_id = Integer.parseInt(req.getParameter("article_id"));
		// 获取回复者id
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		String content = DbUtil.utfString(req.getParameter("content")); // 获取回复内容
		String time = req.getParameter("time");
		String parentStr = req.getParameter("parent");
		boolean result = false;
		if (null != parentStr && !"".equals(parentStr.trim())) {
			int parent = Integer.parseInt(parentStr);
			result = ReplyUtil.getInstance().insertReply(DbUtil.openDb(),
					user_id, article_id, content, time, parent);
		} else {
			result = ReplyUtil.getInstance().insertReply(DbUtil.openDb(),
					user_id, article_id, content, time);
		}
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter pw = resp.getWriter();
		if (result) {
			pw.write("{result:1}");
		} else {
			pw.write("{result:-1}");
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
