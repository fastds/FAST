<%@ page language="java" import="java.util.*,java.io.BufferedReader,java.io.File,java.io.FileReader,java.io.IOException" pageEncoding="UTF-8"%>
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
		
		<!-- codemirror to lead -->
		<link rel="stylesheet" href="codemirror.css" />
		<script src="codemirror.js"></script>
		<script src="sql.js"></script>
		<script src="sql-hint.js"></script>
		<script type="text/javascript">
		function loadlog() {
			var shownum = document.getElementById("shownumID").value;
			var xmlHttp;
			if(window.XMLHttpRequest){
				xmlHttp=new XMLHttpRequest();
			}else{
				xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
			}
			xmlHttp.onreadystatechange = function() {
				if (xmlHttp.readyState==4 && xmlHttp.status==200) {				
					document.getElementById("showlog").value = xmlHttp.responseText;					
				}
			};
			
			xmlHttp.open("get", "ReadLogSer?shownum="+shownum, true);		
		    xmlHttp.send();
			
		
		}
	
		
		</script>
</head>
	<body>
		<div class="container">
			<jsp:include page="top.jsp"></jsp:include>
			<div class="row">
				<div class="span12">					
				            <div> <!--class="form-group" -->  
				            Choose lines of view 
				            <select id="shownumID">
					 		<option value = "5000">5000</option>
							<option value = "7000">7000</option>
					 		<option value = "9000">9000</option> 
							</select>
							&nbsp;&nbsp;
							<input type="button" value="submit" onclick="loadlog()">
							<div class="pull-right">
   							
    						<a class="btn btn-danger"  href="Downloadlogser?filename=scidb.log">Download Log</a>
 						 	</div>
							<textarea  style="width:100%;" style="text-align:left" id="showlog"  cols="50" rows="20" >        				            					           
				            </textarea> 
							</div>		
				</div>	
			</div>	
		</div>	
		
		
	</body>
</html>

