package com.mct.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.mct.model.Article;
import com.mct.utils.ArticleUtil;
import com.mct.utils.DbUtil;

public class GetArticleDetailSevlet extends HttpServlet {

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
		String idStr = req.getParameter("id");
		if (null != idStr) {
			int id = Integer.parseInt(req.getParameter("id"));
			Article article = ArticleUtil.getInstance().getArticle(
					DbUtil.openDb(), id);
			pw.write(JSON.toJSONString(article));
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
