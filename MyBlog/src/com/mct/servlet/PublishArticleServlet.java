package com.mct.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mct.utils.ArticleUtil;
import com.mct.utils.DbUtil;

/**
 * ����
 * 
 * @author Whunf
 * 
 */
public class PublishArticleServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setContentType("text/html;charset=UTF-8");
		String title = DbUtil.utfString(req.getParameter("title"));
		String content = DbUtil.utfString(req.getParameter("content"));
		int user_id = Integer.parseInt(req.getParameter("user_id"));
		String time = req.getParameter("time");
		boolean result = ArticleUtil.getInstance().postingArticle(
				DbUtil.openDb(), title, content, user_id, time, 0);
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
