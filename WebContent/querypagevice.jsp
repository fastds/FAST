<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en" class="ie6 ielt7 ielt8 ielt9">
	<head>
		<meta charset="utf-8">
		<title>FASTDB</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
		<link href="css/site.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>
        <script src="js/jquery-1.7.1.js"></script>
  		<script src="js/bootstrap.min.js"></script>
		<script src="js/site.js"></script>
		
		<!-- codemirror to lead -->
		<link rel="stylesheet" href="codemirror.css" />
		<script src="codemirror.js"></script>
		<script src="sql.js"></script>
		<script src="sql-hint.js"></script>
		<script type="text/javascript">
		window.onload = function() {
			var mime = 'text/x-mariadb';
			// get mime type
			if (window.location.href.indexOf('mime=') > -1) {
			mime = window.location.href.substr(window.location.href.indexOf('mime=') + 5);
			}
			window.editor = CodeMirror.fromTextArea(document.getElementById('code'), {
				mode: mime,
				indentWithTabs: true,
				smartIndent: true,
				lineNumbers: true,
				matchBrackets : true,
				autofocus: true,
				extraKeys: {"Ctrl-Space": "autocomplete"},
				hintOptions: {
					tables: {
						users: {name: null, score: null, birthDate: null},
						countries: {name: null, population: null, size: null}
					}		
				}
			});
		};
		//////////////////////////////////////////////////////////////////////////
		 	function createXMLHttpRequest()
		 	{
		 		try{
		 			return new createXMLHttpRequest();
		 		}
		 		catch(e){
		 			try{
		 				return ActiveXObject("Msxml2.XMLHTTP");	
		 			}
		 			catch(e)
		 			{
		 				try{
		 					return ActiveXobject("Microsoft.XMLHTTP");
		 				}catch(e)
		 				{
		 					alert("对不起，您的浏览器不支持该操作!");
		 				}
		 			}
		 		}
		 	}
		 	function addStmtClick()
		 	{
		 		var addStmtEle =  getElementById("addStmt");
		 		alert("sdfa");
		 	}
		</script>
</head>
	<body>
		<div class="container">
			<jsp:include page="top.jsp"></jsp:include>
			
				<div class="span12">
                    	<form  method="post" action="QueryServlet"><!--role="form"-->
 							<div> <!--class="form-group" -->   
								<h1>Please enter a query statement</h1>
								<textarea style="text-align:left width:20%;" id="code"  cols="10" rows="10" name = "code">${requestScope.sqlstatement }</textarea> 
							 </div>								
								<button type="submit" class="btn btn-default" name="lang" value="afl" ">AFL</button>
								<button type="submit" class="btn btn-default" name="lang" value="aql">AQL</button>
								<c:if test="${sessionScope.user ne null}">
									<a href="<c:url value='/UserQueryServlet?method=findAll'/>" class="btn btn-default">查看常用查询</a>
								</c:if>
								<c:if test="${not empty requestScope.rows and requestScope.rows  ne 0}">
								<div class="alert alert-info" role="alert">本次查询共返回${requestScope.rows}条记录,合计${requestScope.dataSize},耗时${requestScope.time}毫秒</div>
								</c:if>
						</form>
							
 
 					<ul id="myTab" class="nav nav-tabs">
  					 <li class="active">
    					<a href="#qs" data-toggle="tab">
       					  查询结果
      					</a>
 				  	</li>
   					<li><a href="#history" data-toggle="tab">历史查询</a></li>
   					<li> 
   						<a href="#userQuery" data-toggle="tab">常用查询 </a>
				   </li>
				</ul>
				<div id="myTabContent" class="tab-content">
				   <div class="tab-pane fade in active" id="qs">
				   <%
							int pageCount;
							int showPage = 1;		
							String result = " ";						
    						String res = (String)request.getAttribute("result");
							if(res!=null){
							%>
							<div style="overflow-x: auto; overflow-y: auto;">
							<table class="table table-bordered table-striped">
								<tbody>
								<%=res%>
								</tbody>
							</table>	
							</div>	
							<%	
							try
							{
								pageCount=(Integer) request.getSession().getAttribute("pageCount");
								//System.out.println(pageCount);
  							}
							catch(NumberFormatException e)
							{
   							pageCount = 1;
  							}
							
							//String pages = request.getAttribute("showPage").toString();
							try
							{
								showPage = (Integer) request.getAttribute("showPage");
							}
							catch(NumberFormatException e)
							{
   								pageCount = 1;
  							}
							if (pageCount != 0){						
							%>
							第<%=showPage %>页（共<%=pageCount %>页）
 							<a href="QuerypageSer?showPage=1">首页</a>
 							<a href="QuerypageSer?showPage=<%=showPage-1%>">上一页</a>
							<% //根据pageCount的值显示每一页的数字并附加上相应的超链接
  							for(int i=1;i<=pageCount;i++){
							%>
  							<a href="QuerypageSer?showPage=<%=i%>"><%=i%></a>
							<% 
							}
 							%> 
							<a href="QuerypageSer?showPage=<%=showPage+1%>">下一页</a>
 							<a href="QuerypageSer?showPage=<%=pageCount%>">末页</a>
							<!-- 通过表单提交用户想要显示的页数 -->
							<form action="QuerypageSer" method="post">
 							 跳转到第<input type="text" name="showPage" size="10">页
  							<input type="submit" name="submit" value="跳转">
							</form> 
								
							<%	
							}
							}
							%>
						<!--<div class="form-group">-->
								
								
  									<!--</div>-->
    						<%	
    							String error = " ";
    							String errorInfo = (String)request.getAttribute("errorInfo");
    							if(errorInfo!=null)
    							{
							%>
								<table class="table table-bordered table-striped">
								<tbody>
								<%=errorInfo%>
								</tbody>
								</table>
							<%
    							}
  							%>

  							<!--<div class="form-group">-->
  									<!--</div>-->
				   
				   </div>
				   
				   <div class="tab-pane fade" id="history">
				   	<!-- 显示历史查询信息 -->
	 				<table class="table table-bordered table-striped">
	  					<tr>
	  						<td>历史查询</td><td>查询时间</td><td>操作</td>
	  					</tr>
	  					<c:forEach items="${sessionScope.queryHistory}" var="item">
	  					<tr><td>${item.value }</td> <td>${item.key}</td>
	  					<c:if test="${sessionScope.user ne null}">
	  					 <td><a href="<c:url value='/UserQueryServlet?method=add&qs=${item.value}' />" id="addStmt" onclick="addStmtClick()" class="btn btn-primary btn-small">添加为常用语句</a></td></tr>
						</c:if>
						</c:forEach>
	 				</table>
				   </div>
				   <div class="tab-pane fade" id="userQuery">
				   <!-- 显示已注册用户的常用查询语句 -->
					<table class="table table-bordered table-striped">
	  					<tr>
	  						<td>查询语句</td><td>查询时间</td><td>操作</td>
	  					</tr>
	  					<c:if test="${sessionScope.userQuery ne null}">
	  						<c:forEach items="${sessionScope.userQuery}" var="uq">
	  						<tr><td>${uq.queryString }</td> <td>${uq.queryTime}</td><td><a href="<c:url value='/UserQueryServlet?method=delete&qid=${uq.queryId}'/>" class="btn btn-danger btn-small" id="delStmt" onclick="delStmt">删除</a></td></tr>
								</c:forEach>
						</c:if>
  					</table>
				   </div>
				</div>
			</div>	
		</div>	
	</div>	
	</body>
</html>
