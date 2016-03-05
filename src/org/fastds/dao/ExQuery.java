package org.fastds.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.fastds.dbutil.DBConnection;
import org.scidb.jdbc.IStatementWrapper;
import org.scidb.jdbc.ResultSetMetaData;

public class ExQuery {
	private Connection conn = null;
	private Statement state = null;
	private ResultSet res = null;
	private ResultSetMetaData meta = null;

	public ResultSet containerAflQuery(String queryString, String ip)
			throws IOException, SQLException {
		try {
			conn = DBConnection.getConnection(ip);
			state = conn.createStatement();// 27DBConnection.getConnection().createStatement();
			IStatementWrapper staWrapper = state
					.unwrap(IStatementWrapper.class);
			staWrapper.setAfl(true);

			res = (ResultSet) state.executeQuery(queryString);
			conn.commit();
		} catch (SQLException e) {
			closeRes();
			closeState();
			closeConn();
			throw new SQLException(e.getMessage());
		}
		return res;
	}

	public ResultSet aflQuery(String queryString) throws IOException,
			SQLException {
		try {
			conn = DBConnection.getConnection();
			state = conn.createStatement();// 27DBConnection.getConnection().createStatement();
			IStatementWrapper staWrapper = state
					.unwrap(IStatementWrapper.class);
			staWrapper.setAfl(true);
			
			res = (ResultSet) state.executeQuery(queryString);
			conn.commit();
		} catch (SQLException e) {
			closeRes();
			closeState();
			closeConn();
			throw e;
		}
		return res;
	}

	public ResultSet containerAqlQuery(String queryString, String ip)
			throws IOException, SQLException {
		try {
			conn = DBConnection.getConnection(ip);
			state = conn.createStatement();// 27DBConnection.getConnection().createStatement();
			res = (ResultSet) state.executeQuery(queryString);
			conn.commit();
		} catch (SQLException e) {
			closeRes();
			closeState();
			closeConn();
			throw new SQLException(e.getMessage());
		}
		return res;
	}

	public ResultSet aqlQuery(String queryString) throws SQLException {
		try {
			try {
				conn = DBConnection.getConnection();
			} catch (IOException e) {

				e.printStackTrace();
			}
			state = conn.createStatement();// 27DBConnection.getConnection().createStatement();
			res = state.executeQuery(queryString);
			// conn.commit();
		} catch (SQLException e) {

			closeRes();
			closeState();
			closeConn();
			throw e;

		}
		return res;
	}

	public String showResult(ResultSet res) throws SQLException, IOException {
		StringBuilder sb = new StringBuilder("");
		if (res == null) {
			return "No array exits";
		}
		meta = (ResultSetMetaData) res.getMetaData();
		int colCount = meta.getColumnCount();
		sb.append("<tr>");
		for (int i = 1; i <= colCount; i++) {
			if (i == colCount) {
				sb.append("<td>");
				sb.append(meta.getColumnName(i));
				sb.append("</td>");
			} else {
				sb.append("<td>");
				sb.append(meta.getColumnName(i) + "\t");
				sb.append("</td>");
			}
		}
		sb.append("</tr>");
		while (!res.isAfterLast()) {
			sb.append("<tr>");
			for (int j = 1; j <= colCount; j++) {
				if (j == colCount) {
					if ("string".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getString(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else {
						sb.append("<td>");
						sb.append(res.getDouble(meta.getColumnLabel(j)));
						sb.append("</td>");
					}
				} else {
					// sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
					if ("string".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getString(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else {
						sb.append("<td>");
						sb.append(res.getDouble(meta.getColumnLabel(j)));
						sb.append("</td>");
					}
				}
			}
			sb.append("</tr>");
			res.next();
		}
		closeRes();
		closeState();
		closeConn();
		return sb.toString();
	}

	public String showResultwithline(ResultSet res, int line)
			throws SQLException, IOException {
		StringBuilder sb = new StringBuilder("");
		if (res == null)
			return "";
		meta = (ResultSetMetaData) res.getMetaData();
		int colCount = meta.getColumnCount();
		for (int i = 1; i <= colCount; i++) {
			if (i == colCount) {

				sb.append(meta.getColumnName(i));

			} else {

				sb.append(meta.getColumnName(i));
				sb.append(",");
			}
		}
		sb.append("!");
		int n = 1;
		while (!res.isAfterLast()) {
			if (n > line) {
				break;
			}

			for (int j = 1; j <= colCount; j++) {
				if (j == colCount) {
					if ("string".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getString(meta.getColumnLabel(j)));

					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));

					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						;
					} else {

						sb.append(res.getDouble(meta.getColumnLabel(j)));

					}
				} else {

					if ("string".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getString(meta.getColumnLabel(j)));
						sb.append(",");
					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
						sb.append(",");
					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						sb.append(",");
					} else {

						sb.append(res.getDouble(meta.getColumnLabel(j)));
						sb.append(",");
					}
				}
			}
			sb.append("!");
			res.next();
			n++;
		}

		closeRes();
		closeState();
		closeConn();
		return sb.toString();
	}

	public String showAllResul(ResultSet res) throws SQLException, IOException {
		StringBuilder sb = new StringBuilder("");
		if (res == null)
			return "";
		meta = (ResultSetMetaData) res.getMetaData();
		int colCount = meta.getColumnCount();
		// sb.append("<table>");
		// sb.append("<tr>");
		for (int i = 1; i <= colCount; i++) {
			if (i == colCount) {
				// sb.append("<td>");
				sb.append(meta.getColumnName(i));
				// sb.append(" ");
			} else {
				// sb.append("<td>");
				sb.append(meta.getColumnName(i));
				sb.append(",");
			}
		}
		sb.append("!");
		while (!res.isAfterLast()) {
			// sb.append("<tr>");
			for (int j = 1; j <= colCount; j++) {
				if (j == colCount) {
					if ("string".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getString(meta.getColumnLabel(j)));
						// sb.append(" ");
					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
						// sb.append(" ");
					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						// sb.append(" ");
					} else {
						// sb.append("<td>");
						sb.append(res.getDouble(meta.getColumnLabel(j)));
						// sb.append(" ");
					}
				} else {
					// sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
					if ("string".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getString(meta.getColumnLabel(j)));
						sb.append(",");
					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
						sb.append(",");
					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						sb.append(",");
					} else {
						// sb.append("<td>");
						sb.append(res.getDouble(meta.getColumnLabel(j)));
						sb.append(",");
					}
				}
			}
			sb.append("!");
			res.next();
		}
		// sb.append("</table>");
		closeRes();
		closeState();
		closeConn();
		return sb.toString();
	}

	public void closeRes() {
		try {
			if (res != null) {
				res.close();
				res = null;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void closeState() {
		try {
			if (state != null) {
				state.close();
				state = null;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void closeConn() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
