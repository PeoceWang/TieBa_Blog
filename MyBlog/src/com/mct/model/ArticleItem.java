package com.mct.model;

/**
 * ������Ŀ
 * 
 * @author Whunf
 * 
 */
public class ArticleItem {
	private int a_id;
	private int reply_count;
	private String u_name;
	private String title;
	private String time;
	private String u_photo;

	public ArticleItem() {
	}

	public ArticleItem(int aId, int replyCount, String uName, String title,
			String time, String uPhoto) {
		super();
		a_id = aId;
		reply_count = replyCount;
		u_name = uName;
		this.title = title;
		this.time = time;
		u_photo = uPhoto;
	}

	public int getA_id() {
		return a_id;
	}

	public void setA_id(int aId) {
		a_id = aId;
	}

	public int getReply_count() {
		return reply_count;
	}

	public void setReply_count(int replyCount) {
		reply_count = replyCount;
	}

	public String getU_name() {
		return u_name;
	}

	public void setU_name(String uName) {
		u_name = uName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getU_photo() {
		return u_photo;
	}

	public void setU_photo(String uPhoto) {
		u_photo = uPhoto;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "a_id:" + a_id + " user_name:" + u_name + " title:" + title
				+ " reply_count:" + reply_count;
	}

}
