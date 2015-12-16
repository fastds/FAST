package org.fastds.resources.dataresources;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.fastds.dao.ExQuery;
import org.fastds.model.ResultOfQuery;
import org.fastds.model.User;
import org.fastds.service.DockerService;
import org.fastds.service.JsonService;
import org.fastds.service.UserService;
import org.glassfish.jersey.server.mvc.Viewable;

import edu.gzu.utils.PageQueryImpl;

@Path("/v1")
public class GetUserData {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	private UserService userService;
	private ExQuery eq;

	// private DockerService dockerService;

	@GET
	@Path("container/csv/AQL")
	@Produces("text/plain")
	public void getAQLCsvResult(@QueryParam("statement") String statement,
			@QueryParam("line") int line) throws SQLException, IOException {
		statement = statement.trim();
		Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
		queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), statement);
		request.getSession().setAttribute("queryHistory", queryHistory);
		
		PrintWriter out = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			out.println("Please login first");
			out.close();
			return;
		}
		String ip = (String) request.getSession().getAttribute("ip");
		String[] result;
		String res = null;
		if (statement != null && !statement.equals("")) {
			eq = new ExQuery();
			if (line > 0) {
				System.out.println(line);
				res = eq.showResultwithline(
						eq.containerAqlQuery(statement, ip), line);
			} else {
				System.out.println("all");
				res = eq.showAllResul(eq.containerAqlQuery(statement, ip));
			}
		}
		result = res.split("!");

		for (int i = 0; i < result.length; i++) {
			out.println(result[i]);
		}
		out.close();

	}

	@GET
	@Path("container/json/AQL")
	public void getAQlJsonResult(@QueryParam("statement") String statement,
			@QueryParam("line") int line) throws SQLException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		PrintWriter out = response.getWriter();
		if (user == null) {
			out.println("Please login first");
			out.close();
			return;
		}
		statement = statement.trim();
		
		Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
		queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), statement);
		request.getSession().setAttribute("queryHistory", queryHistory);
		
		ObjectMapper mapper = new ObjectMapper();
		String ip = (String) request.getSession().getAttribute("ip");
		List<Map<String, String>> list = new JsonService().getContainerAqlList(
				statement, ip);
		out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				list));
		out.close();
	}

	@GET
	@Path("container/csv/AFL")
	@Produces("text/plain")
	public void getAFLCsvResult(@QueryParam("statement") String statement,
			@QueryParam("line") int line) throws SQLException, IOException {
		statement = statement.trim();
		
		Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
		queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), statement);
		request.getSession().setAttribute("queryHistory", queryHistory);
		
		PrintWriter out = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			out.print("Please login first");
			out.close();
			return;
		}
		String ip = (String) request.getSession().getAttribute("ip");
		String[] result;
		String res = null;
		if (statement != null && !statement.equals("")) {
			eq = new ExQuery();
			if (line > 0) {
				// System.out.println(line);
				res = eq.showResultwithline(
						eq.containerAflQuery(statement, ip), line);
			} else {
				// System.out.println("all");
				res = eq.showAllResul(eq.containerAflQuery(statement, ip));
			}
		}
		result = res.split("!");
		System.out.println("hahahahaahaha");
		for (int i = 0; i < result.length; i++) {
			out.println(result[i]);
		}
		out.close();

	}

	@GET
	@Path("container/json/AFL")
	public void getAFLJsonResult(@QueryParam("statement") String statement,
			@QueryParam("line") int line) throws SQLException, IOException {
		statement = statement.trim();
		PrintWriter out = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			out.print("Please login first");
			out.close();
			return;
		}
		Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
		queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), statement);
		request.getSession().setAttribute("queryHistory", queryHistory);
		
		String ip = (String) request.getSession().getAttribute("ip");
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, String>> list = new JsonService().getContainerAflList(
				statement, ip);

		out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				list));
		out.close();
	}

	@POST
	@Path("user/aflQuery")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Viewable queryafl(@FormParam("code") String code)
			throws ServletException, IOException {
//		User user = (User) request.getSession().getAttribute("user");
//		if (user == null) {
//			return new Viewable("/login", null);
//		}
//		String queryString = code.trim();
//		
//		if (queryString != null && !queryString.equals("")) {
//			eq = new ExQuery();
//			// dockerService = new DockerService();
//			Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
//			queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), queryString);
//			request.getSession().setAttribute("queryHistory", queryHistory);
//			// String name = user.getUserName();
//			String ip = (String) request.getSession().getAttribute("ip");
//			String result = null;
//			try {
//				result = eq.showResult(eq.containerAflQuery(queryString, ip));
//				// request.setAttribute("result", result);
//			} catch (IOException e) {
//				result = e.toString();
//			} catch (SQLException e) {
//				result = e.toString();
//			}
//			request.setAttribute("result", result);
//			request.setAttribute("sqlstatement", queryString);
//		}
//		return new Viewable("/userquerypage", null);
		
		int showPage = 1;
		int pageCount = 0;
		HttpSession session = request.getSession();

		String query = code;
		query = query.trim();
		if (query == null || query.equals("")) {
			System.out.println("Statement can not be null");
			return new Viewable("/userquerypage", null);
		}

		String queryString = query;
		// give last char of queryString to lastchar ...use lastchar to know
		// whether having ";"
		char lastchar = queryString.charAt(queryString.length() - 1);
		if (lastchar == ';') {
			queryString = query.substring(0, query.length() - 1);
		}
		session.setAttribute("queryStatement", query);
		String queryType = "afl";
		// String queryType = request.getParameter("lang");
		// session.setAttribute("queryType", queryType);
		PageQueryImpl resultsImpl = new PageQueryImpl();
		String result = null;// 执行信息
		@SuppressWarnings("unchecked")
		Map<String, String> queryHistory = (HashMap<String, String>) session
				.getAttribute("queryHistory");
		if (queryHistory == null || queryHistory.isEmpty()) {
			queryHistory = new HashMap<String, String>();
		}

		ArrayList<ResultOfQuery> results = new ArrayList<ResultOfQuery>();
		int rows = 0;
		Date time = null;
		String dataSizeStr = null;
		try {
			System.out.println("into resultsImpl.aflQuery(queryString");
			String ip = (String) request.getSession().getAttribute("ip");
			Date timeBefore = new Date();
			results = resultsImpl.containerAflQuery(queryString,ip);
			Date timeAfter = new Date();
			time = new Date(timeAfter.getTime() - timeBefore.getTime());
			if (!results.isEmpty()) {
				pageCount = results.size();
				result = results.get(0).getShowresult();
				rows = results.get(0).getRows();
				dataSizeStr = results.get(0).getDataSizeStr();
			} else {
				result = "Query was executed successfully";
			}

			queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date()), queryString);

			session.setAttribute("pageCount", pageCount);
			session.setAttribute("results", results);
			session.setAttribute("queryHistory", queryHistory);
			request.setAttribute("rows", rows);// 结果集条数
			request.setAttribute("dataSize", dataSizeStr);// 结果集大小
			request.setAttribute("time", time.getTime());
			request.setAttribute("result", result);
			request.setAttribute("sqlstatement", query);
			request.setAttribute("queryType", queryType);
			request.setAttribute("showPage", showPage);
			return new Viewable("/userquerypage", null);
		} catch (SQLException e) {
			session.setAttribute("pageCount", pageCount);
			session.setAttribute("results", results);

			request.setAttribute("errorInfo", "your afl command has a syntax error, please check it!");
			request.setAttribute("results", results);
			request.setAttribute("sqlstatement", query);
			request.setAttribute("queryType", queryType);
			request.setAttribute("showPage", showPage);
			return new Viewable("/userquerypage", null);
		}

	}

	@POST
	@Path("user/aqlQuery")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Viewable queryaql(@FormParam("code") String code)
			throws ServletException, IOException {
//		User user = (User) request.getSession().getAttribute("user");
//		if (user == null) {
//			return new Viewable("/login", null);
//		}
//		String queryString = code.trim();
//		System.out.println(queryString);
//		
//		if (queryString != null && !queryString.equals("")) {
//			String result = null;
//			Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
//			queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), queryString);
//			request.getSession().setAttribute("queryHistory", queryHistory);
//			eq = new ExQuery();
//			// dockerService = new DockerService();
//			String ip = (String) request.getSession().getAttribute("ip");
//			try {
//				result = eq.showResult(eq.containerAqlQuery(queryString, ip));
//
//			} catch (IOException e) {
//				result = e.toString();
//			} catch (SQLException e) {
//				result = e.toString();
//			}
//			request.setAttribute("result", result);
//			request.setAttribute("sqlstatement", queryString);
//		}
//		return new Viewable("/userquerypage", null);
		
		int showPage = 1;
		int pageCount = 0;
		HttpSession session = request.getSession();

		String query = code;
		query = query.trim();
		if (query == null || query.equals("")) {
			System.out.println("Statement can not be null");
			return new Viewable("/userquerypage", null);
		}

		String queryString = query;
		// give last char of queryString to lastchar ...use lastchar to know
		// whether having ";"
		char lastchar = queryString.charAt(queryString.length() - 1);
		if (lastchar == ';') {
			queryString = query.substring(0, query.length() - 1);
		}
		session.setAttribute("queryStatement", query);
		String queryType = "aql";
		// String queryType = request.getParameter("lang");
		// session.setAttribute("queryType", queryType);
		PageQueryImpl resultsImpl = new PageQueryImpl();
		String result = null;// 执行信息
		@SuppressWarnings("unchecked")
		Map<String, String> queryHistory = (HashMap<String, String>) session
				.getAttribute("queryHistory");
		if (queryHistory == null || queryHistory.isEmpty()) {
			queryHistory = new HashMap<String, String>();
		}

		ArrayList<ResultOfQuery> results = new ArrayList<ResultOfQuery>();
		int rows = 0;
		Date time = null;
		String dataSizeStr = null;
		try {
			System.out.println("into resultsImpl.aflQuery(queryString");
			String ip = (String) request.getSession().getAttribute("ip");
			Date timeBefore = new Date();
			results = resultsImpl.containerAqlQuery(queryString,ip);
			Date timeAfter = new Date();
			time = new Date(timeAfter.getTime() - timeBefore.getTime());
			if (!results.isEmpty()) {
				pageCount = results.size();
				result = results.get(0).getShowresult();
				rows = results.get(0).getRows();
				dataSizeStr = results.get(0).getDataSizeStr();
			} else {
				result = "Query was executed successfully";
			}

			queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date()), queryString);

			session.setAttribute("pageCount", pageCount);
			session.setAttribute("results", results);
			session.setAttribute("queryHistory", queryHistory);
			request.setAttribute("rows", rows);// 结果集条数
			request.setAttribute("dataSize", dataSizeStr);// 结果集大小
			request.setAttribute("time", time.getTime());
			request.setAttribute("result", result);
			request.setAttribute("sqlstatement", query);
			request.setAttribute("queryType", queryType);
			request.setAttribute("showPage", showPage);
			return new Viewable("/userquerypage", null);
		} catch (SQLException e) {
			session.setAttribute("pageCount", pageCount);
			session.setAttribute("results", results);

			request.setAttribute("errorInfo", "your afl command has a syntax error, please check it!");
			request.setAttribute("results", results);
			request.setAttribute("sqlstatement", query);
			request.setAttribute("queryType", queryType);
			request.setAttribute("showPage", showPage);
			return new Viewable("/userquerypage", null);
		}
	}

	@POST
	@Path("container/getJson/aqlQuery")
	@Produces("text/plain")
	public void getAqlJson(@FormParam("code") String code) throws IOException {
		String statement = code.trim();
		PrintWriter out = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			out.print("login first");
			out.close();
			return;
		}
		Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
		queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), statement);
		request.getSession().setAttribute("queryHistory", queryHistory);
		String ip = (String) request.getSession().getAttribute("ip");
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, String>> list = null;
		try {
			list = new JsonService().getContainerAqlList(
					statement, ip);
			out.print(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					list));
		} catch (SQLException e) {
			out.print("your aql command ' "+code+" ' has a syntax error, Please check it!");
			e.printStackTrace();
		}
		finally{
			if(out !=null) out.close();
		}
		
	}

	@POST
	@Path("container/getJson/aflQuery")
	@Produces("text/plain")
	public void getAflJson(@FormParam("code") String code) throws IOException{
		String statement = code.trim();
		PrintWriter out = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			out.print("login first");
			out.close();
			return;
		}
		Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
		queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), statement);
		request.getSession().setAttribute("queryHistory", queryHistory);
		String ip = (String) request.getSession().getAttribute("ip");
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, String>> list = null;
		try {
			list = new JsonService().getContainerAflList(
					statement, ip);
			out.print(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					list));
		} catch (SQLException e) {
			out.print("your afl command ' "+code+" ' has a syntax error, Please check it!");
			e.printStackTrace();
		}finally{
			if(out!=null) out.close();
		}
		
	}

	@POST
	@Path("container/getCsv/aqlQuery")
	@Produces("text/plain")
	public void getAqlCsv(@FormParam("code") String code) throws IOException{
		String statement = code.trim();
		String[] result;
		Map<String,String> errors = new HashMap<String,String>();
		PrintWriter out =  response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			out.print("<h2>login first</h2>");
			out.close();
			return;
		}
		
		Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
		queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), statement);
		request.getSession().setAttribute("queryHistory", queryHistory);
		
		String ip = (String) request.getSession().getAttribute("ip");
		String res = null;
		if (statement != null && !statement.equals("")) {
			eq = new ExQuery();
			// System.out.println("all");
			try {
				res = eq.showAllResul(eq.containerAqlQuery(statement, ip));
				result = res.split("!");

				for (int i = 0; i < result.length; i++) {

					out.println(result[i]);

				}
			} catch (SQLException e) {
				out.print("your aql command ' "+code+" ' has a syntax error, Please check it!");
			}
			finally{
				if(out!=null) out.close();
			}
		}

	
	}

	@POST
	@Path("container/getCsv/aflQuery")
	@Produces("text/plain")
	public void getAflCsv(@FormParam("code") String code) throws IOException{
		String statement = code.trim();
		String[] result;
		PrintWriter out = response.getWriter();
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			out.print("<h2>login first</h2>");
			out.close();
			return;
		}
		Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
		queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), statement);
		request.getSession().setAttribute("queryHistory", queryHistory);
		
		String ip = (String) request.getSession().getAttribute("ip");
		String res = null;
		if (statement != null && !statement.equals("")) {
			eq = new ExQuery();
			try {
				res = eq.showAllResul(eq.containerAflQuery(statement, ip));
				result = res.split("!");

				for (int i = 0; i < result.length; i++) {

					out.println(result[i]);

				}
			} catch (SQLException e) {
				out.print("your afl command ' "+code+" ' has a syntax error, Please check it!");
			}
			finally{
				if (out !=null ){out.close();}
			}
		}

		
	}

	@GET
	@Path("userlogin")
	public String load(@QueryParam("name") String name,
			@QueryParam("password") String password) {
		userService = new UserService();
		User user = userService.findByUsername(name);
		if (user == null) {
			return "name does not exit";
		} else if (!user.getPassword().equals(password)) {

			return "password is not right";
		}
		String ip = new DockerService().getIp(user.getUserName());
		request.getSession().setAttribute("ip", ip);
		request.getSession().setAttribute("user", user);
		String sessionId = request.getSession().getId();
		return sessionId;
	}

	@GET
	@Path("getSessionId")
	public String getSessionId() {
		return request.getSession().getId();
	}

	@GET
	@Path("getIp")
	public String getIp() {
		return (String) request.getSession().getAttribute("ip");
	}

	@GET
	@Path("testview")
	public void test() {

	}

	@GET
	@Path("user/loginout")
	public String loginOut() {
		request.getSession().invalidate();
		return "logout";
	}

}
