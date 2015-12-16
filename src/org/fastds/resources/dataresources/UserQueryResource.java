package org.fastds.resources.dataresources;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.fastds.dbutil.UUIDUtil;
import org.fastds.model.User;
import org.fastds.model.UserQuery;
import org.fastds.service.UserQueryService;
import org.glassfish.jersey.server.mvc.Viewable;
@Path("/")
public class UserQueryResource {
	@Context
	HttpServletRequest req;
	@Context
	HttpServletResponse resp;
	
	private UserQueryService service = new UserQueryService();
	@GET
	@Path("findQuery")
	public Viewable findAll(){
			try {
				req.setCharacterEncoding("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			resp.setContentType("text/html;charset=utf-8");
			User user = (User) req.getSession().getAttribute("user");
			if(user==null)
			{
				req.setAttribute("loginInfo", "please login！");
				return new Viewable("/userquerypage.jsp",null);
			}
			
			ArrayList<UserQuery> userQuery = service.findAll(user);
			req.getSession().setAttribute("userQuery", userQuery);
			return new Viewable("/userquerypage.jsp",null);
	}
	@GET
	@Path("addQuery")
	public Viewable add(@QueryParam("qs")final String queryString){
			try {
				req.setCharacterEncoding("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			resp.setContentType("text/html;charset=utf-8");
			User user = (User) req.getSession().getAttribute("user");
			if(user==null)
			{
				req.setAttribute("loginInfo", "please login！");
				return new Viewable("/userquerypage.jsp",null);
			}
			System.out.println("queryStirng:=========="+queryString);
			UserQuery userQuery = new UserQuery();
			UUID.randomUUID().toString().split("-");
			userQuery.setQueryId(UUIDUtil.generateUUID());
			userQuery.setQueryString(queryString);
			userQuery.setQueryTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			userQuery.setUsername(user.getUserName());
			
			service.add(userQuery);
			req.setAttribute("addInfo", "success!");
			return new Viewable("/userquerypage.jsp",null);
	}
	@GET
	@Path("deleteQuery")
	public Viewable delete(@QueryParam("qid")String queryId) {
			try {
				req.setCharacterEncoding("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			resp.setContentType("text/html;charset=utf-8");
			User user = (User) req.getSession().getAttribute("user");
			ArrayList<UserQuery> userQueryList = (ArrayList<UserQuery>) req.getSession().getAttribute("userQuery");
			if(user==null)
			{
				req.setAttribute("loginInfo", "please login!");
				return  new Viewable("/userquerypage.jsp",null);
			}
			UserQuery userQuery = service.findByQueryId(queryId);
			service.delete(userQuery);
			userQueryList.clear();
			userQueryList = (ArrayList<UserQuery>) service.findAll(user);
			req.getSession().setAttribute("userQuery", userQueryList);
			return  new Viewable("/userquerypage.jsp",null);
	}

}
