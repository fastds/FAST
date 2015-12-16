<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<!--[if lt IE 7 ]><html lang="en" class="ie6 ielt7 ielt8 ielt9"><![endif]--><!--[if IE 7 ]><html lang="en" class="ie7 ielt8 ielt9"><![endif]--><!--[if IE 8 ]><html lang="en" class="ie8 ielt9"><![endif]--><!--[if IE 9 ]><html lang="en" class="ie9"> <![endif]--><!--[if (gt IE 9)|!(IE)]><!--> 
<html lang="en"><!--<![endif]--> 
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8">
		<title>FASTDB</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
		<link href="css/site.css" rel="stylesheet">
		<!--[if lt IE 9]><script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
		
	
	</head>
	<body>
	<%
    String msg = "";
    String v=(String)request.getAttribute("msg");
    if(v!=null)
    	msg = v;
     %>
		<div id="login-page" class="container">
			<h1>用户注册</h1>
			<font color = "red">${requestScope.msg}</font>
			<form id="login-form" class="well" action="<c:url value='/UserServlet'/>" method ="post">
				<input type="hidden" name="method" value="regist">
				Name：<input type="text" class="span2" placeholder="用户名（6-20字母、数字、下划线、无空格）" name="username" value="${requestScope.user.username }"/><font color = "red">${requestScope.errors.username}</font><br />
				Password：<input type="password" class="span2" placeholder="密码（6-20字母、数字、无空格）" name="password"/><font color = "red">${requestScope.errors.password}</font><br />
				<label class="checkbox"> <input type="checkbox" /> Remember me </label>
				<button type="submit" class="btn btn-primary" >注册</button>
				<a href="<c:url value='/login.jsp'/>">登陆</a>
			</form>	
		</div>
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/site.js"></script>
	</body>
</html>