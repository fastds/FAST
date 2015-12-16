package org.fastds.resources.dataresources;

import java.awt.image.BufferedImage;
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
import org.fastds.dbutil.VerifyCode;
import org.fastds.model.ResultOfQuery;
import org.fastds.model.User;
import org.fastds.service.JsonService;
import org.glassfish.jersey.server.mvc.Viewable;

import edu.gzu.utils.PageQueryImpl;

@Path("/v1")
public class GetData {
	private ExQuery eq;
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;

	@GET
	@Path("/SciDB_version")
	@Produces("text/plain")
	public String getVersion() {
		return "14.12";
	}

	@GET
	@Path("/csv/AFL")
	@Produces("text/plain")
	public void getAFLResult(@QueryParam("statement") String statement,
			@QueryParam("line") int line) throws SQLException, IOException {
		statement = statement.trim();
		Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
		String[] result;
		String res = null;
		if (statement != null && !statement.equals("")) {
			queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "");
			eq = new ExQuery();
			if (line > 0) {
				// System.out.println(line);
				res = eq.showResultwithline(eq.aflQuery(statement), line);
			} else {
				// System.out.println("all");
				res = eq.showAllResul(eq.aflQuery(statement));
			}
			request.getSession().setAttribute("queryHistory", queryHistory);
		}
		result = res.split("!");
		PrintWriter out = response.getWriter();

		for (int i = 0; i < result.length; i++) {
			out.println(result[i]);
		}
		out.close();

	}

	@GET
	@Path("/json/AQL")
	public void getAQlJsonResult(@QueryParam("statement") String statement,
			@QueryParam("line") int line) throws SQLException, IOException {
		PrintWriter out = response.getWriter();
		
		statement = statement.trim();
		Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
		queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), statement);
		request.getSession().setAttribute("queryHistory", queryHistory);
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, String>> list = new JsonService()
				.getAqlList(statement);
		out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				list));
		out.close();
	}

	@GET
	@Path("/json/AFL")
	public void getAFLJsonResult(@QueryParam("statement") String statement,
			@QueryParam("line") int line) throws SQLException, IOException {
		Map<String,String> queryHistory = request.getSession().getAttribute("queryHistory")==null?new HashMap<String,String>():(HashMap<String, String>) request.getSession().getAttribute("queryHistory");
		statement = statement.trim();
		queryHistory.put(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "");
		PrintWriter out = response.getWriter();
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, String>> list = new JsonService()
				.getAflList(statement);

		out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				list));
		out.close();
	}

	@GET
	@Path("/csv/AQL")
	@Produces("text/plain")
	public void getAQLResult(@QueryParam("statement") String statement,
			@QueryParam("line") int line) throws SQLException, IOException {
		statement = statement.trim();
		String[] result;
		String res = null;
		if (statement != null && !statement.equals("")) {
			eq = new ExQuery();
			if (line > 0) {
				System.out.println(line);
				res = eq.showResultwithline(eq.aqlQuery(statement), line);
			} else {
				System.out.println("all");
				res = eq.showAllResul(eq.aqlQuery(statement));
			}
		}
		result = res.split("!");
		PrintWriter out = response.getWriter();

		for (int i = 0; i < result.length; i++) {
			out.println(result[i]);
		}
		out.close();

	}

	@POST
	@Path("aflQuery")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Viewable queryafl(@FormParam("code") String code)
			throws ServletException, IOException {
		// String queryString = code.trim();
		// if (queryString != null && !queryString.equals("")) {
		// eq = new ExQuery();
		// String result = null;
		// try {
		// result = eq.showResult(eq.aflQuery(queryString));
		// // request.setAttribute("result", result);
		// } catch (IOException e) {
		// result = e.toString();
		// } catch (SQLException e) {
		// result = e.toString();
		// }
		// request.setAttribute("result", result);
		// request.setAttribute("sqlstatement", queryString);
		// }
		// -----------------------以上是罗云鹏的代码---------------------------------------//

		int showPage = 1;
		int pageCount = 0;
		HttpSession session = request.getSession();

		String query = code;
		query = query.trim();
		if (query == null || query.equals("")) {
			System.out.println("query is null");
			return new Viewable("/querypage", null);
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
			queryHistory = new LinkedHashMap<String, String>();
		}

		ArrayList<ResultOfQuery> results = new ArrayList<ResultOfQuery>();
		int rows = 0;
		Date time = null;
		String dataSizeStr = null;
		try {
			System.out.println("into resultsImpl.aflQuery(queryString");
			Date timeBefore = new Date();
			results = resultsImpl.aflQuery(queryString);
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
			return new Viewable("/querypage", null);
		} catch (SQLException e) {
			session.setAttribute("pageCount", pageCount);
			session.setAttribute("results", results);

			request.setAttribute("errorInfo", "your afl command has a syntax error, please check it!");
			request.setAttribute("results", results);
			request.setAttribute("sqlstatement", query);
			request.setAttribute("queryType", queryType);
			request.setAttribute("showPage", showPage);
			return new Viewable("/querypage", null);
		}
	}

	@POST
	@Path("aqlQuery")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Viewable queryaql(@FormParam("code") String code)
			throws ServletException, IOException {
		// String queryString = code.trim();
		// System.out.println(queryString);
		// if (queryString != null && !queryString.equals("")) {
		// String result = null;
		// eq = new ExQuery();
		// try {
		// result = eq.showResult(eq.aqlQuery(queryString));
		// // request.setAttribute("result", result);
		// } catch (IOException e) {
		// result = e.toString();
		// } catch (SQLException e) {
		// result = e.toString();
		// }
		// request.setAttribute("result", result);
		// request.setAttribute("sqlstatement", queryString);
		// }
		// return new Viewable("/querypage", null);
		// ----------------------以上是罗云鹏的代码-------------------------------//
		int showPage = 1;
		int pageCount = 0;
		HttpSession session = request.getSession();

		String query = code;
		query = query.trim();
		if (query == null || query.equals("")) {
			System.out.println("query is null");
			return new Viewable("/querypage", null);
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
		PageQueryImpl resultsImpl = new PageQueryImpl();
		String result = null;// 执行信息
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> queryHistory = (LinkedHashMap<String, String>) session
				.getAttribute("queryHistory");
		if (queryHistory == null || queryHistory.isEmpty()) {
			queryHistory = new LinkedHashMap<String, String>();
		}

		ArrayList<ResultOfQuery> results = new ArrayList<ResultOfQuery>();
		int rows = 0;
		Date time = null;
		String dataSizeStr = null;
		try {
			System.out.println("into resultsImpl.aflQuery(queryString");
			Date timeBefore = new Date();
			results = resultsImpl.aqlQuery(queryString);
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
			return new Viewable("/querypage", null);
		} catch (SQLException e) {
			session.setAttribute("pageCount", pageCount);
			session.setAttribute("results", results);

			request.setAttribute("errorInfo", "your aql command has a syntax error，please check it！");
			request.setAttribute("results", results);
			request.setAttribute("sqlstatement", query);
			request.setAttribute("queryType", queryType);
			request.setAttribute("showPage", showPage);
			return new Viewable("/querypage", null);
		}
	}

	@GET
	@Path("/getInstances")
	public Viewable list() {
		// request.setAttribute("instances", "instances");
		String query = "list('instances')";
		String result = null;
		eq = new ExQuery();
		try {
			result = eq.showResult(eq.aflQuery(query));
			request.setAttribute("instances", result);
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return new Viewable("/index", null);
	}

	@GET
	@Path("verify")
	public void verify() {

		// 1. 创建验证码类

		VerifyCode vc = new VerifyCode();

		// 2. 得到验证码图片

		BufferedImage image = vc.getImage();

		// 3. 把图片上的文本保存到session中

		request.getSession().setAttribute("session_vcode", vc.getText());

		// 4. 把图片响应给客户端

		try {
			VerifyCode.output(image, response.getOutputStream());
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@POST
	@Path("/getJson/aqlQuery")
	@Produces("text/plain")
	public void getAqlJson(@FormParam("code") String code) throws IOException{
		String statement = code.trim();
		// System.out.println("::::::::::here" + line);
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, String>> list = null;
		PrintWriter out = response.getWriter();
		try {
			list = new JsonService()
					.getAqlList(statement);
			out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					list));
		} catch (SQLException e) {
			out.print("your aql command ' "+code+" ' has a syntax error, Please check it!");
			e.printStackTrace();
		}
		finally{
			if(out!=null)out.close();
		}
		
	}

	@POST
	@Path("/getJson/aflQuery")
	@Produces("text/plain")
	public void getAflJson(@FormParam("code") String code) throws IOException {
		String statement = code.trim();
		// System.out.println("::::::::::here" + line);
		ObjectMapper mapper = new ObjectMapper();
		PrintWriter out = response.getWriter();
		List<Map<String, String>> list = null;
		try {
			list = new JsonService()
					.getAflList(statement);
			out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					list));
		} catch (SQLException e) {
			out.print("your afl command ' "+code+" ' has a syntax error, Please check it!");
			e.printStackTrace();
		}
		finally{
			if(out!=null)out.close();
		}
		
	}

	@POST
	@Path("/getHtml/aqlQuery")
	@Produces("text/plain")
	public void getAqlHtml(@FormParam("code") String code) throws IOException {

		String queryString = code.trim();
		String result = null;
		if (queryString != null && !queryString.equals("")) {

			eq = new ExQuery();
			try {
				result = eq.showResult(eq.aqlQuery(queryString));
				// request.setAttribute("result", result);
			} catch (IOException e) {
				result = e.toString();
			} catch (SQLException e) {
				result = e.toString();
			}
			// request.setAttribute("result", result);
			// request.setAttribute("sqlstatement", queryString);
		}

		PrintWriter out = response.getWriter();
		out.print("<html>");
		out.print("<head>");
		out.print("</head>");
		out.print("<body>");
		out.print("<table border=\"\">");
		out.print(result);
		out.print("</table>");
		out.print("</body>");
		out.print("</html>");
		out.close();

	}

	@POST
	@Path("/getCsv/aqlQuery")
	@Produces("text/plain")
	public void getAqlCsv(@FormParam("code") String code) throws IOException
	 {
		String statement = code.trim();
		String[] result;
		// System.out.println("::::::::::here" + line);
		String res = null;
		PrintWriter out = response.getWriter();
		if (statement != null && !statement.equals("")) {
			eq = new ExQuery();
			// System.out.println("all");
			
			try {
				res = eq.showAllResul(eq.aqlQuery(statement));
				result = res.split("!");
				

				for (int i = 0; i < result.length; i++) {

					out.println(result[i]);

				}
				out.close();
			} catch (SQLException e) {
				out.print("your aql command ' "+code+" ' has a syntax error, Please check it!");
				e.printStackTrace();
			}finally{
				if(out!=null){}
				
				out.close();
			}
		}

		
	}

	@POST
	@Path("/getCsv/aflQuery")
	@Produces("text/plain")
	public void getAflCsv(@FormParam("code") String code) throws IOException{
		String statement = code.trim();
		String[] result;
		// System.out.println("::::::::::here" + line);
		PrintWriter out = response.getWriter();
		String res = null;
		if (statement != null && !statement.equals("")) {
			eq = new ExQuery();
			// System.out.println("all");
			try {
				res = eq.showAllResul(eq.aflQuery(statement));
				result = res.split("!");

				for (int i = 0; i < result.length; i++) {

					out.println(result[i]);

				}
			} catch (SQLException e) {
				out.print("your afl command ' "+code+" ' has a syntax error, Please check it!");
				e.printStackTrace();
			}finally{
				if(out!=null) out.close();
			}
		}

	}

	@GET
	@Path("userQuery")
	public Viewable findAll() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			request.setAttribute("loginInfo", "please login first!");
			return new Viewable("/querypage", null);
		}
		return new Viewable("/querypage", null);
	}
}
