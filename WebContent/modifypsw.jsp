<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.fastds.model.*" %>
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
  		<script src="js/bootstrap.min.js"></script>
		<script src="js/site.js"></script>
		
		<!-- codemirror to lead -->
		<link rel="stylesheet" href="codemirror.css" />
		<script src="codemirror.js"></script>
		<script src="sql.js"></script>
		<script src="sql-hint.js"></script>
<script type="text/javascript">

function start()
{
	//alert("start");
	var form = document.getElementById("edit-profile");
	var newPassword = document.getElementById("newPassword").value;;
	var confirmPassword = document.getElementById("confirmPassword").value;
	if(confirmPassword.trim()==""||newPassword.trim()==""){
		 document.getElementById("msg").innerHTML ="密码不能为空！";
	}else if(confirmPassword.trim()==newPassword.trim()){
		form.action="v1/pswUpdate";
		form.submit();
	}else if(confirmPassword.trim()!=newPassword.trim()){
		 document.getElementById("msg").innerHTML ="两次输入的密码不一致！";
	}
}
</script>
</head>
	<body>
		<div class="container">
			<jsp:include page="top.jsp"></jsp:include>
			<div class="row">
				<!-- <div class="span3">
					<div class="well" style="padding: 8px 0;">
						<ul class="nav nav-list">
							<li class="nav-header">
								导航/Navigation
							</li>
							<li>
								<a href ="index.jsp"><i class="icon-white icon-home"></i> 主页/Home</a>
							</li>
                            <li >
								<a href="querypage.jsp"><i class="icon-check"></i> 查询/Check</a>
							</li>
							<li >
								<a href="upload.jsp"><i class="icon-folder-open"></i> 增加/Upload(csv)</a>
							</li>
							
							<li>
								<a href="ReadLogSer"><i class="icon-list-alt"></i> 日志信息/Log Information</a>
							</li>
							<li class="nav-header">
								账号信息
							</li>
							<!-- 只有session中有user才显示个人信息链接 -->
							<!--<c:if test="${sessionScope.user ne null}">
								<li class="active">
									<a href="<c:url value='/profile.jsp'/>"><i class="icon-user"></i> 个人信息</a>
								</li>
							</c:if>
							<li>
								<a 				><i class="icon-cog"></i> 设置</a>
							</li>
							<li class="divider">
							</li>
							<li>
								<a 				><i class="icon-info-sign"></i> 帮助</a>
							</li>
							<li class="nav-header">
								其他
							</li>
							<li>
								<a 				><i class="icon-picture"></i> Gallery</a>
							</li>
						</ul>
					</div>
				</div> -->
				<div class="span9">

					<h1> Personal Information</h1>
					<form id="edit-profile" class="form-horizontal" method="post" action="">
					
						<fieldset>
							<legend> Information Modify </legend>
							<font color="green">${requestScope.msg }</font>
							<div class="control-group">
								<label class="control-label" for="input01"> User Name </label>
								<div class="controls">
									<input type="hidden" name="id" id="id" value="1"/>
									<input type="text" class="input-xlarge" id="input01" value="${sessionScope.user.getUserName() }" name="username" disabled="true" />
								</div>
							</div>
							<font color="green">${requestScope.successInfo}</font>
							<font color="red">${requestScope.msg}</font>
							<div class="control-group">
								<label class="control-label" for="input01"> New PassWord </label>
								<div class="controls">
									<input type="password" class="input-xlarge" id="newPassword"  name="newPassword"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="input01"> Confirm PassWord </label>
								<div class="controls">
									<input type="password" class="input-xlarge" id="confirmPassword"  name="confirmPassword"/><div id="msg"></div>
								</div>
							</div>
							<div class="form-actions">
								<input type="button" value="Submit" onclick="start()"/> <a href="<c:url value='/profile.jsp'/>" class="btn">上一步</a>
							</div>
						</fieldset>
					</form>
				</div>
			</div>
			</div>
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/site.js"></script>
		<script src="js/cook.js"></script>
		<script src="js/index.js"></script>
		<script src="js/spin.min.js"></script>
		<script src="js/spin.js"></script>
		<script src="js/scidb.js"></script>
	</body>
</html>
