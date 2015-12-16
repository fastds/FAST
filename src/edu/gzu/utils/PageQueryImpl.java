package edu.gzu.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.fastds.dbutil.DBConnection;
import org.fastds.model.ResultOfQuery;
import org.scidb.jdbc.IStatementWrapper;

public class PageQueryImpl {
	int pageSize = 10;
	public ArrayList<ResultOfQuery> containerAflQuery(String queryString,String ip) throws IOException, SQLException{
		System.out.println("into containerAflQuery(String queryString)");
		ArrayList<ResultOfQuery> results = new ArrayList<ResultOfQuery>();
		Connection conn = null;
		ResultSet res = null;
	
		try {
			conn = DBConnection.getConnection(ip);
			Statement st = conn.createStatement();
			IStatementWrapper staWrapper = st.unwrap(IStatementWrapper.class);
			staWrapper.setAfl(true);
			res = st.executeQuery(queryString);	
		} catch(SQLException e) {
			throw new SQLException(e.getMessage());
		}
		
		if (res == null){
			return results;			
		}
		
		ResultSetMetaData meta = null;
		meta = res.getMetaData();
		int showpages = 0;
		int rows = 0;//rows:记录结果集条数
		long dataSize=0;//dataSize：数据大小
		
		while (!res.isAfterLast()){
			System.out.println("PageQueryImpl showpages   ==="+showpages);
			showpages++;
			StringBuilder sb = new StringBuilder();
			ResultOfQuery result = new ResultOfQuery();
			result.setShowPage(showpages);
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
			///////////////////////////
			for(int i=1;i<=pageSize;i++){
				if (!res.isAfterLast()) {
					sb.append("<tr>");
					for (int j = 1; j <= colCount; j++) {
							if ("bool".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getBoolean(meta.getColumnLabel(j)));
								sb.append("</td>");
								dataSize += 1;
							} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
								sb.append("</td>");
								dataSize += 8;
							} else if ("string".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
//								System.out.println("column=======:"+res.findColumn(meta.getColumnLabel(j)));
								String str = res.getString(meta.getColumnLabel(j));
								dataSize += str.getBytes().length;
								if(str.contains("<"))
									str = str.replace("<", "&lt;");
								if(str.contains(">"))
									str = str.replace(">","&gt;");
								//System.out.println(str);
								sb.append(str);
								sb.append("</td>");
								
							} else if ("datetime".endsWith(meta.getColumnTypeName(j))) {
								
								sb.append("<td>");
								sb.append(res.getTime(meta.getColumnLabel(j)));
								sb.append("</td>");
								dataSize += res.getTime(meta.getColumnLabel(j)).toString().getBytes().length;
							} else {
								sb.append("<td>");
								sb.append(res.getDouble(meta.getColumnLabel(j)));
								sb.append("</td>");
								dataSize += 8;
							}
					}
					sb.append("</tr>");
					res.next();
					rows++;
					// System.out.println(sb.toString());
				}
			}
			dataSize*=rows;
			result.setRows(rows);
			result.setDataSize(dataSize);
			result.setShowresult(sb.toString());
			results.add(result);
		}
		
		conn.commit();
		res.close();//
		conn.close();
		return 	results;		
	}
	public ArrayList<ResultOfQuery> aflQuery(String queryString) throws IOException, SQLException{
		System.out.println("into aflQuery(String queryString)");
		ArrayList<ResultOfQuery> results = new ArrayList<ResultOfQuery>();
		Connection conn = null;
		ResultSet res = null;
	
		try {
			conn = DBConnection.getConnection();
			Statement st = conn.createStatement();
			IStatementWrapper staWrapper = st.unwrap(IStatementWrapper.class);
			staWrapper.setAfl(true);
			res = st.executeQuery(queryString);	
		} catch(SQLException e) {
			throw new SQLException(e.getMessage());
		}
		
		if (res == null){
			return results;			
		}
		
		ResultSetMetaData meta = null;
		meta = res.getMetaData();
		int showpages = 0;
		int rows = 0;//rows:记录结果集条数
		long dataSize=0;//dataSize：数据大小
		
		while (!res.isAfterLast()){
			System.out.println("PageQueryImpl showpages   ==="+showpages);
			showpages++;
			StringBuilder sb = new StringBuilder();
			ResultOfQuery result = new ResultOfQuery();
			result.setShowPage(showpages);
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
			///////////////////////////
			for(int i=1;i<=pageSize;i++){
				if (!res.isAfterLast()) {
					sb.append("<tr>");
					for (int j = 1; j <= colCount; j++) {
							if ("bool".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getBoolean(meta.getColumnLabel(j)));
								sb.append("</td>");
								dataSize += 1;
							} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
								sb.append("</td>");
								dataSize += 8;
							} else if ("string".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
//								System.out.println("column=======:"+res.findColumn(meta.getColumnLabel(j)));
								String str = res.getString(meta.getColumnLabel(j));
								dataSize += str.getBytes().length;
								if(str.contains("<"))
									str = str.replace("<", "&lt;");
								if(str.contains(">"))
									str = str.replace(">","&gt;");
								//System.out.println(str);
								sb.append(str);
								sb.append("</td>");
								
							} else if ("datetime".endsWith(meta.getColumnTypeName(j))) {
								
								sb.append("<td>");
								sb.append(res.getTime(meta.getColumnLabel(j)));
								sb.append("</td>");
								dataSize += res.getTime(meta.getColumnLabel(j)).toString().getBytes().length;
							} else {
								sb.append("<td>");
								sb.append(res.getDouble(meta.getColumnLabel(j)));
								sb.append("</td>");
								dataSize += 8;
							}
					}
					sb.append("</tr>");
					res.next();
					rows++;
					// System.out.println(sb.toString());
				}
			}
			dataSize*=rows;
			result.setRows(rows);
			result.setDataSize(dataSize);
			result.setShowresult(sb.toString());
			results.add(result);
		}
		
		conn.commit();
		res.close();//
		conn.close();
		return 	results;		
	}
	public ArrayList<ResultOfQuery> aqlQuery(String queryString) throws IOException, SQLException{
		System.out.println("into aflQuery(String queryString)");
		ArrayList<ResultOfQuery> results = new ArrayList<ResultOfQuery>();
		Connection conn = null;
		ResultSet res = null;
	
		try {
			conn = DBConnection.getConnection();
			Statement st = conn.createStatement();
			
			res = st.executeQuery(queryString);			
			//res.c
		} catch(SQLException e) {
			throw new SQLException(e.getMessage());
		}
		
		if (res == null){
			//ResultOfQuery result = new ResultOfQuery();
			return results;			
		}
		
		ResultSetMetaData meta = null;
		//StringBuilder sb = new StringBuilder("");
		meta = res.getMetaData();
		int showpages = 0;
		
		while (!res.isAfterLast()){
			System.out.println("PageQueryImpl showpages   ==="+showpages);
			showpages++;
			StringBuilder sb = new StringBuilder();
			ResultOfQuery result = new ResultOfQuery();
			result.setShowPage(showpages);
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
			
			for(int i=1;i<=pageSize;i++){
				if (!res.isAfterLast()) {
					sb.append("<tr>");
					for (int j = 1; j <= colCount; j++) {
							if ("bool".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getBoolean(meta.getColumnLabel(j)));
								sb.append("</td>");
							} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
								sb.append("</td>");
							} else if ("string".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getString(meta.getColumnLabel(j)));
								sb.append("</td>");
							} else if ("datetime".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getTime(meta.getColumnLabel(j)));
								sb.append("</td>");
							} else {
								sb.append("<td>");
								sb.append(res.getDouble(meta.getColumnLabel(j)));
								sb.append("</td>");
							}
					}
					sb.append("</tr>");
					res.next();
					// System.out.println(sb.toString());
				}
			}
			result.setShowresult(sb.toString());
			results.add(result);
		}
		conn.commit();
		if(res!=null)
			res.close();//
		
		return 	results;		
	}

	public ArrayList<ResultOfQuery> containerAqlQuery(String queryString,String ip) throws IOException, SQLException{
		System.out.println("into aqlQuery(String queryString)");
		ArrayList<ResultOfQuery> results = new ArrayList<ResultOfQuery>();
		Connection conn = null;
		ResultSet res = null;
	
		try {
			conn = DBConnection.getConnection(ip);
			Statement st = conn.createStatement();
			
			res = st.executeQuery(queryString);			
			//res.c
		} catch(SQLException e) {
			throw new SQLException(e.getMessage());
		}
		
		if (res == null){
			//ResultOfQuery result = new ResultOfQuery();
			return results;			
		}
		
		ResultSetMetaData meta = null;
		//StringBuilder sb = new StringBuilder("");
		meta = res.getMetaData();
		int showpages = 0;
		
		while (!res.isAfterLast()){
			System.out.println("PageQueryImpl showpages   ==="+showpages);
			showpages++;
			StringBuilder sb = new StringBuilder();
			ResultOfQuery result = new ResultOfQuery();
			result.setShowPage(showpages);
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
			
			for(int i=1;i<=pageSize;i++){
				if (!res.isAfterLast()) {
					sb.append("<tr>");
					for (int j = 1; j <= colCount; j++) {
							if ("bool".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getBoolean(meta.getColumnLabel(j)));
								sb.append("</td>");
							} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
								sb.append("</td>");
							} else if ("string".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getString(meta.getColumnLabel(j)));
								sb.append("</td>");
							} else if ("datetime".endsWith(meta.getColumnTypeName(j))) {
								sb.append("<td>");
								sb.append(res.getTime(meta.getColumnLabel(j)));
								sb.append("</td>");
							} else {
								sb.append("<td>");
								sb.append(res.getDouble(meta.getColumnLabel(j)));
								sb.append("</td>");
							}
					}
					sb.append("</tr>");
					res.next();
					// System.out.println(sb.toString());
				}
			}
			result.setShowresult(sb.toString());
			results.add(result);
		}
		conn.commit();
		if(res!=null)
			res.close();//
		
		return 	results;		
	}

}










