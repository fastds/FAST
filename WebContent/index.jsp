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
  		<script src="js/bootstrap.min.js"></script>
		<script src="js/site.js"></script>
</head>
	<body class="reverse">
		<div class="container">
			<jsp:include page="top.jsp"></jsp:include>
			<div class="row">
				<div class="span12">
					<div class="hero-unit" style="height:250px;">
						<div style="width:300px;height:200px;" class="span2">
						 <a href="<c:url value='/listinfo.jsp'/>"><img alt="" src="images/main1.jpg" style="width:300;height:200px;"/></a>
						 <h3 align="center">Image List</h3>
						</div>
						<div style="width:300px;height:200px;" class="span2">
						 <a href="<c:url value='/querypage.jsp'/>"><img alt="" src="images/main2.jpg" style="width:300;height:200px;"/></a>
						 <h3 align="center">Query</h3>
						</div>
						<div style="width:300px;height:200px;" class="span2">
						 <a href="<c:url value='/buildpage.jsp'/>"><img alt="" src="images/main3.jpg" style="width:300;height:200px;"/></a>
						 <h3 align="center">Build Space</h3>
						</div>
					</div>
				</div>
			</div><!-- row -->
			
			<hr/>
			<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<div class="row-fluid">
				<div class="span4">
					<table class="table table-striped">
						<h3>About Us</h3>
						<tbody>
							<tr>
								<td>
									<a href="http://www.gzu.edu.cn/">GuiZhou University Homepage</a>
								</td>
							</tr>
							<tr>
								<td>
									<a href="http://cs.gzu.edu.cn/">College Of Computer & Technology,GuiZhou University </a>
								</td>
							</tr>
							<tr>
								<td>
									<a href="http://acmis.gzu.edu.cn/s/455/t/1481/main.jspy">
									GuiZhou Engineering Laboratory for Advance Computing and Medical Information Service 
									</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="span4">
					<table class="table table-striped">
						<h3>
							Documents
						</h3>
						<tbody>
							<tr>
								<td>
									<a href="#">GuiZhou University Homepage</a>
								</td>
							</tr>
							
						</tbody>
					</table>
				</div>
				<div class="span4">
					<table class="table table-striped">
						<h3>
							Contact Us
						</h3>
						<tbody>
							<tr>
								<td>Tel</td>
								<td>
									0851-83621366
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>	
			
			
	</div><!-- container -->
</body>
</html>
