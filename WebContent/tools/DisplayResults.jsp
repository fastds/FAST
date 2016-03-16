<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.fastds.model.DisplayResults" %>
<%@ page import="org.fastds.model.ObjectExplorer" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<link href="../css/tools.css" rel="stylesheet" type="text/css" />
	<link href="../css/alerts.css" rel="stylesheet" type="text/css" />
	
	<title>
		Object Explorer
	</title>
	
    <script src="../js/jquery-1.7.1.js"></script>
    <script type="text/javascript" src="../js/master.js"> </script>
    <script type="text/javascript" src="../js/explore.js"></script>
</head>
<body>
 	<%@ include file="ObjectExplorer.jsp" %>
 	<div class="content">
   <div id="QueryResults">
      <!--  url = master.getURL(); 后台已有，但不明确这里赋值是否有影响，属于内嵌java代码 -->
       
    <p />
     <h2><a href="${displayResults.url }/help/browser/browser.aspx?cmd=description+${displayResults.name}+U" target="_top" class="content">${displayResults.name}</a></h2>
    <p />   
    <!--    put the option for Plate Objects -->
    <%=((ObjectExplorer)request.getAttribute("master")).showVTable(((DisplayResults)request.getAttribute("displayResults")).getDs(), 300)%>
    </div>
 </div>    
</body>
</html>

  
