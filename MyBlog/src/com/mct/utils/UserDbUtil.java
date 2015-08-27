package com.mct.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mct.model.User;

/**
 * �û���Ϣ��������
 * 
 * @author huajian.zhang
 * 
 */
public class UserDbUtil {
	private static UserDbUtil instance;

	private UserDbUtil() {

	}

	public static UserDbUtil getInstance() {
		if (null == instance) {
			instance = new UserDbUtil();
		}
		return instance;
	}

	/**
	 * �û���¼
	 * 
	 * @param conn
	 * @param name
	 * @param psw
	 * @return
	 */
	public User login(Connection conn, String name, String psw) {
		User user = null;
		try {
			Statement statement = conn.createStatement();
			// Ҫִ�е�SQL���
			String sql = "select * from users where name = '" + name
					+ "' and psw = '" + psw + "'";
			ResultSet rs = statement.executeQuery(sql);
			if (rs.next()) {
				int id = rs.getInt("_id");
				String nick = rs.getString("nick");
				String phone = rs.getString("phone");
				String birth = rs.getString("birth");
				String sex = rs.getString("sex");
				String photo = rs.getString("photo");
				user = new User(id, name);
				user.setPhoto(photo).setBirth(birth).setNick(nick).setSex(sex)
						.setPhone(phone);
			}
			rs.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * ע���û����û���������
	 * 
	 * @param conn
	 * @param userName
	 * @param psw
	 * @return
	 */
	public boolean regist(Connection conn, String userName, String psw) {
		return regist(conn, userName, psw, "", "");
	}

	/**
	 * ע���û����û��������롢ͷ��
	 * 
	 * @param conn
	 * @param userName
	 * @param psw
	 * @param photo
	 * @return
	 */
	public boolean regist(Connection conn, String userName, String psw,
			String photo) {
		return regist(conn, userName, psw, "", photo);
	}

	/**
	 * ע���û����û��������롢�ǳơ�ͷ��
	 * 
	 * @param conn
	 * @param userName
	 * @param psw
	 * @param nick
	 * @param photo
	 * @return
	 */
	public boolean regist(Connection conn, String userName, String psw,
			String nick, String photo) {
		try {
			Statement statement = conn.createStatement();
			String sql = "insert into users (name,psw,photo,nick) values ('"
					+ userName
					+ "','"
					+ psw
					+ "','"
					+ photo
					+ "','"
					+ nick
					+ "');";
			int row = statement.executeUpdate(sql);
			if (row > 0) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * �����û���Ϣ
	 * 
	 * @param conn
	 * @param id
	 * @param userName
	 * @param nick
	 * @param photo
	 * @param birth
	 * @param sex
	 * @param phone
	 * @return
	 */
	// public boolean updateUserInfo(Connection conn, int id,
	// HashMap<String, String> map) {
	// if (null != user.getUserName() && !"".equals(user.getUserName())) {
	// map.put("user_name", user.getUserName());
	// }
	// if (null != user.getNick() && !"".equals(user.getNick())) {
	// map.put("nick", user.getNick());
	// }
	// if (null != user.getPhoto() && !"".equals(user.getPhoto())) {
	// map.put("photo", user.getPhoto());
	// }
	// if (null != user.getBirth() && !"".equals(user.getBirth())) {
	// map.put("birth", user.getBirth());
	// }
	// if (null != phone && !"".equals(phone)) {
	// map.put("phone", phone);
	// }
	// if (null != sex && !"".equals(sex)) {
	// map.put("sex", sex);
	// }
	// return updateUserInfo(conn, map, id);
	// }

	public boolean updateUserInfo(Connection conn, Map<String, String> map,
			int id) {
		if (map.size() > 0) {
			StringBuffer sb = new StringBuffer("update users set ");
			Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				sb.append(entry.getKey()).append("='").append(entry.getValue())
						.append("',");
			}
			// ȡ�����һ������
			sb.deleteCharAt(sb.length() - 1);
			sb.append(" where _id=").append(id);
			String sql = sb.toString();
			System.out.println(sql);
			try {
				Statement statement = conn.createStatement();
				int row = statement.executeUpdate(sql);
				if (row > 0) {
					return true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public String getUserName(Connection conn, int id) {
		try {
			String sql = "select name from users where _id = " + id;
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			String userName = null;
			if (rs.next()) {
				userName = rs.getString("name");
			}
			rs.close();
			statement.close();
			return userName;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}