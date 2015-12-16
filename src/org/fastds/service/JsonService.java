package org.fastds.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fastds.dao.ExQuery;
import org.junit.Test;
import org.scidb.jdbc.ResultSetMetaData;

public class JsonService {
	private ResultSet res = null;
	private ResultSetMetaData meta = null;

	public String getArayaName(ResultSet res) throws SQLException {
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

		return sb.toString();
	}

	public String getResult(ResultSet res) throws SQLException {
		StringBuilder sb = new StringBuilder("");
		meta = (ResultSetMetaData) res.getMetaData();
		int colCount = meta.getColumnCount();
		// System.out.println(colCount);
		while (!res.isAfterLast()) {

			for (int j = 1; j <= colCount; j++) {
				if (j == colCount) {
					if ("string".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getString(meta.getColumnLabel(j)));

					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));

					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBoolean(meta.getColumnLabel(j)));

					} else {

						sb.append(res.getDouble(meta.getColumnLabel(j)));

					}
				} else {

					if ("string".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getString(meta.getColumnLabel(j)));
						sb.append("|");
					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
						sb.append("|");
					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						sb.append("|");
					} else {

						sb.append(res.getDouble(meta.getColumnLabel(j)));
						sb.append("|");
					}
				}
			}
			sb.append("!");
			res.next();
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getAqlList(String statement)
			throws SQLException, IOException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		ExQuery eq = new ExQuery();
		res = eq.aqlQuery(statement);
		String names = getArayaName(res);
		String[] arrayName = names.split(",");
		int nameLen = arrayName.length;

		String results = getResult(res);
		String[] result = results.split("!");

		int len = result.length;
		Map<String, String>[] map = new Map[len];
		for (int i = 0; i < len; i++) {
			map[i] = new HashMap<String, String>();

			for (int j = 0; j < nameLen; j++) {

				map[i].put(arrayName[j], result[i].split("\\|")[j]);

			}
			list.add(map[i]);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getContainerAqlList(String statement,
			String ip) throws SQLException, IOException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		ExQuery eq = new ExQuery();
		res = eq.containerAqlQuery(statement, ip);
		String names = getArayaName(res);
		String[] arrayName = names.split(",");
		int nameLen = arrayName.length;

		String results = getResult(res);
		String[] result = results.split("!");

		int len = result.length;
		Map<String, String>[] map = new Map[len];
		for (int i = 0; i < len; i++) {
			map[i] = new HashMap<String, String>();

			for (int j = 0; j < nameLen; j++) {

				map[i].put(arrayName[j], result[i].split("\\|")[j]);

			}
			list.add(map[i]);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getAflList(String statement)
			throws SQLException, IOException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		ExQuery eq = new ExQuery();
		res = eq.aflQuery(statement);
		String names = getArayaName(res);
		String[] arrayName = names.split(",");
		int nameLen = arrayName.length;

		String results = getResult(res);
		String[] result = results.split("!");

		int len = result.length;
		Map<String, String>[] map = new Map[len];
		for (int i = 0; i < len; i++) {
			map[i] = new HashMap<String, String>();

			for (int j = 0; j < nameLen; j++) {

				map[i].put(arrayName[j], result[i].split("\\|")[j]);
			}
			list.add(map[i]);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getContainerAflList(String statement,
			String ip) throws SQLException, IOException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		ExQuery eq = new ExQuery();
		res = eq.containerAflQuery(statement, ip);
		String names = getArayaName(res);
		String[] arrayName = names.split(",");
		int nameLen = arrayName.length;

		String results = getResult(res);
		String[] result = results.split("!");

		int len = result.length;
		Map<String, String>[] map = new Map[len];
		for (int i = 0; i < len; i++) {
			map[i] = new HashMap<String, String>();

			for (int j = 0; j < nameLen; j++) {
				map[i].put(arrayName[j], result[i].split("\\|")[j]);
			}
			list.add(map[i]);
		}

		return list;
	}

	@Test
	public void testName() throws SQLException, IOException {
		ExQuery eq = new ExQuery();
		String res = getArayaName(eq.aqlQuery("select * from A"));
		String[] name = res.split(",");
		for (int i = 0; i < name.length; i++) {
			System.out.println(name[i]);
		}
	}

	@Test
	public void testResult() throws SQLException, IOException {
		ExQuery eq = new ExQuery();
		String res = getResult(eq.aflQuery("list()"));
		String[] result = res.split("!");
		// String[] rowResult;
		System.out.println(result.length);
		for (int i = 0; i < result.length; i++) {
			// System.out.println(result[i]);
			// rowResult = result[i].split("\\|");
			for (int j = 0; j < 7; j++) {
				System.out.print(result[i].split("\\|")[j]);
				System.out.print("--");
			}
			System.out.println("....");
		}
	}

	@Test
	public void tests() {
		String name = "1|B|1|1|B<val:double> [i=0:9,10,0]|true|false";
		String[] result = name.split("\\|");
		System.out.println("len+::" + result.length);
		for (int i = 0; i < result.length; i++) {
			System.out.print(result[i]);
		}
	}
}
