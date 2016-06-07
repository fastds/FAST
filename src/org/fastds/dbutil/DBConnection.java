package org.fastds.dbutil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	private static Connection conn = null;
	private static Statement state = null;

	public static Connection getConnection() throws IOException {
		try {
			Class.forName("org.scidb.jdbc.Driver");
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		}
		try {//192.168.100.111:1239，192.168.221.131:1239
			conn = DriverManager.getConnection("jdbc:scidb://192.168.100.111:1239/");
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return conn;
	}

	public static Connection getConnection(String ip) throws IOException {
		try {
			Class.forName("org.scidb.jdbc.Driver");
		} catch (ClassNotFoundException e3) {
			e3.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection("jdbc:scidb://" + ip + "/");
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return conn;
	}

	public static Statement getStatement() throws IOException {
		try {
			state = DBConnection.getConnection().createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return state;
	}

}
