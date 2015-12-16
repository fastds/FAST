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
			<div class="row">
				<div class="span12">
                    <form  method="post" action="BuildSpace"><!--role="form"-->
                    	<button type="submit" class="btn btn-default" name="status" value="Refresh" >Refresh</button>
                    	
						<button type="submit" class="btn btn-default" name="status" value="Build" >Build My Space</button>
					</form> 			
				<div class="content">
				<table class="table">
				<thead>
                  <tr>
                    <th>
                      <div class="table-heade">
                        <table class="table">
                          <thead style="background: #F5F6F6;">
                          <tr>
                            <th style="width:20%;padding-left: 5px;">Name</th>
                            <th style="width:10%;text-indent: 8px;">Status</th>
                            <th style="width:10%; padding-left: 8px;">Image Name</th>
                            <th style="width:20%">Into My Space<a role="button" data-trigger="focus" tabindex="0" data-container="article" data-toggle="popover" data-placement="bottom" data-content="运行状态变为&quot;运行中&quot;后便可使用容器服务，使用相应的工具访问服务地址，例如：<br>1.使用SSH工具登录操作系统：ssh -p 41231 root@ubuntu.tenxcloud.net<br>2.通过浏览器直接访问 http://demo.tenxcloud.net:41231" class="hint-info" data-original-title="" title=""><i class="fa fa-question-circle"></i></a></th>
                            <th style="width:10%;text-indent: 8px;">Control</th>
                            <th style="width:10%">Built Date</th>
                          </tr>
                          </thead>
                          <tbody>
                          <tr>
                          <c:if test="${sessionScope.mUserBuildData != null}">
                          	<td>
                          		${mUserBuildData.username }
                          	</td>
                          	
                          	<td>
                          		<c:choose>
                          		<c:when test="${mUserBuildData.spacestatus == 0 }">
                          			Stop
                          		</c:when>
                          		<c:when test="${mUserBuildData.spacestatus ==1 }">
                          			Running
                          		</c:when>
                          		</c:choose>
                          	</td>
                          	
                          	<td>
                          		scidbcoord:0.4
                          	</td>
                          	
                          	<td>
                          		<c:if test="${mUserBuildData.spacestatus == 1 }">
                          			<a href="<c:url value='/space/index.jsp' />" target="_blank"> Into My Space</a>
                          		</c:if>
                          	</td>
                          	
                          	<td>
                          		<c:choose>
                          		<c:when test="${mUserBuildData.spacestatus == 0 }">
                          			<a href="BuildSpaceServlet?status=Start">Start</a>
                          		</c:when>
                          		<c:when test="${mUserBuildData.spacestatus ==1 }">
                          			<a href="BuildSpaceServlet?status=Stop">Stop</a>
                          		</c:when>
                          		</c:choose>
                          	</td>
                          	
                          	<td>
                          		${mUserBuildData.buildtime }
                          	</td>
                          </c:if>
                          </tr>
                          </tbody>
                        </table>
                      </div>
                    </th>
                  </tr>
                </thead>
                </table>
				</div>
				
			</div>	
		</div>	
	</div>	
	</body>
</html>

