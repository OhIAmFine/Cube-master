package cn.edu.cqupt.rubic_business.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	static final String driver = "com.mysql.jdbc.Driver";
	static final String url = "jdbc:mysql://172.22.146.251:4342/enron";
	static final String user = "root";
	static final String password = "cqupt2013+";

	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConn() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public static void close(Connection con, Statement stmt, ResultSet rs) {

		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void close(Connection con, Statement stmt) {
		close(con, stmt, null);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;

		try {
			con = DBConnection.getConn();
			stmt = con.createStatement();

			sql = "select * from message limit 5,10 ";

			System.out.println(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {

				String sender = rs.getString("sender");
				String body = rs.getString("body");

				System.out.println(sender + "\t content:" + body);
			}
			System.out.println("done");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con, stmt, rs);
		}
	}

}
