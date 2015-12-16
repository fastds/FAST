<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en" class="ie6 ielt7 ielt8 ielt9">
	<head>
		<base href="<%=basePath%>">
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
		 	
		 	function afl()
		{
			var form = document.getElementById("query");
			var obj = document.getElementsByName("type");
   			for(var i=0; i<obj.length; i ++){
        		if(obj[i].checked){
            		if(obj[i].value=="json"){
            		//alert("json");
            		form.method="post";
            		form.target="_blank";
					form.action="v1/getJson/aflQuery";
					form.submit();
            		}else if(obj[i].value=="csv"){
            		//alert("csv");
            		form.method="post";
            		form.target="_blank";
					form.action="v1/getCsv/aflQuery";
					form.submit();
            		}else if(obj[i].value=="html"){
            		//alert("html");
            		form.method="post";
            		form.target="_self";
					form.action="v1/aflQuery";
					form.submit();
            		}
        		}
   			 }
		}
		 	function aql()
		{
			var form = document.getElementById("query");
			var obj = document.getElementsByName("type");
   			for(var i=0; i<obj.length; i ++){
        		if(obj[i].checked){
            		if(obj[i].value=="json"){
            		//alert("json");
            		form.method="post";
            		form.target="_blank";
					form.action="v1/getJson/aqlQuery";
					form.submit();
            		}else if(obj[i].value=="csv"){
            		//alert("csv");
            		form.method="post";
            		form.target="_blank";
            		
					form.action="v1/getCsv/aqlQuery";
					form.submit();
            		}else if(obj[i].value=="html"){
            		//alert("html");
            		form.method="post";
            		form.target="_self";
					form.action="v1/aqlQuery";
					form.submit();
            		}
        		}
   			 }
			/*form.method="post";
			form.action="v1/aqlQuery";
			form.submit();*/
		}
		</script>
</head>
	<body>
		<div class="container">
			<jsp:include page="top.jsp"></jsp:include>
			<div class="row">
				
				<div class="span12">
                    	<form  method="POST" action="" id="query" target=""><!--role="form"-->
 							<div> <!--class="form-group" -->   
								<h3>Please enter a query statement</h3>
								<p class="lead">
									You can search the  database with a <em>query</em>. A query is a request for information. 
									You specify what data you want, and what conditions you want the data to satisfy. For example, 
									you might ask the database to return the positions of all stars brighter than a certain 
									magnitude in a certain filter.</p>
								<textarea style="text-align:left width:20%;" id="code"  cols="10" rows="10" name="code">${sqlstatement }</textarea> 
							 </div>								
								<input type="button" value="AQL"  onclick="aql()" />
            					<input type="button" value="AFL"  onclick="afl()" />
            					<input name="type" type="radio" value="csv" checked/><font size="4">csv</font>
            					<input name="type" type="radio" value="json" /><font size="4">json</font>
            					<input name="type" type="radio" value="html" /><font size="4">html</font>
								<c:if test="${not empty requestScope.rows and requestScope.rows  ne 0}">
								<div class="alert alert-info" role="alert">Total ${requestScope.rows} records, all ${requestScope.dataSize}, takes${requestScope.time} ms</div>
								</c:if>
								<!-- 
								<c:if test="${not empty requestScope.errorInfo }">
								<div class="alert alert-error" role="alert"> ${requestScope.errorInfo}</div>
								</c:if> -->
						</form>
							
 	
 					<ul id="myTab" class="nav nav-tabs">
  					 <li class="active">
    					<a href="#qs" data-toggle="tab">
       					  Query Result
      					</a>
 				  	</li>
   					<li><a href="#history" data-toggle="tab">History</a></li>
   					<!-- 
   					<li> 
   						<a href="#userQuery" data-toggle="tab">My Statement </a> 
				   </li>-->
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
							${result}
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
							NO<%=showPage %>（ALL <%=pageCount %>）
 							<a href="QuerypageSer?showPage=1"><b>First</b></a>
 							<a href="QuerypageSer?showPage=<%=showPage-1%>">Pre</a>
							<% //根据pageCount的值显示每一页的数字并附加上相应的超链接
  							for(int i=1;i<=pageCount;i++){
							%>
  							<a href="QuerypageSer?showPage=<%=i%>"><%=i%></a>
							<% 
							}
 							%> 
							<a href="QuerypageSer?showPage=<%=showPage+1%>">Next</a>
 							<a href="QuerypageSer?showPage=<%=pageCount%>">End</a>
							<!-- 通过表单提交用户想要显示的页数 -->
							<form action="QuerypageSer" method="post">
 							 GO <input type="text" name="showPage" size="10">
  							<input type="submit" name="submit" value="Go">
							</form> 
								
							<%	
							}
							}
							%>
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
					 </div>
				   <div class="tab-pane fade" id="history">
				   	<!-- 显示历史查询信息 -->
	 				<table class="table table-bordered table-striped">
	  					<tr>
	  						<td>History</td><td>Time</td><td>Operation</td>
	  					</tr>
	  					<c:forEach items="${sessionScope.queryHistory}" var="item">
	  					<tr><td>${item.value }</td> <td>${item.key}</td>
	  					<c:if test="${sessionScope.user ne null}">
	  					 <td><a href="<c:url value='/findQuery' />" id="addStmt" onclick="addStmtClick()" class="btn btn-primary btn-small">添加为常用语句</a></td></tr>
						</c:if>
						</c:forEach>
	 				</table>
				   </div> 
				   <!-- 显示已注册用户的常用查询语句 
				   <div class="tab-pane fade" id="userQuery">
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
				   </div>-->
				</div>
			</div>	
		</div>	
	</div>	
	</body>
</html>
