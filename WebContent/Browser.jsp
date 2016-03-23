<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.fastds.model.DBColumns"  %>
<%@ page import="java.util.*"  %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Schema Browser</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
 <link href="css/bootstrap.css" rel="stylesheet">
<script src="js/jquery.min.js"></script>
   <script src="js/bootstrap.min.js"></script>
    <link href="css/bootstrap-responsive.css" rel="stylesheet">
</head>
<body>
<div class="container">
	<jsp:include page="top.jsp"></jsp:include>
     <div class="row">
        <div class="span3">
          <div class="well">
          	<h4><p>Arrays</p></h4>
			<ul class="nav nav-pills nav-stacked">
			   <c:forEach items="${arrays }" var="name">
			   	<li><a href="<c:url value='/v1/schema?name=${name}'/>">${name}</a></li>
			   </c:forEach>
			</ul>
          	
          </div><!--/.well -->
        </div><!--/span3-->
         <div class="span9">
        	<div class="table-responsive">
			   <table class="table">
			      <caption>${cols.arrayname}</caption>
			      <thead>
			         <tr>
			            <th>name</th>
			            <th>description</th>
			         </tr>
			      </thead>
			      <tbody>
			      <%
			      	List<String> attrNames = ((DBColumns)request.getAttribute("cols")).getAttrNames();
			      	List<String> descriptions = ((DBColumns)request.getAttribute("cols")).getDescriptions();
			      	
			      	for(int i = 0;i<attrNames.size();i++)
			      	{
			      	
			      %>
			      	<tr>
			            <td><%=attrNames.get(i) %></td>
			            <td><%=descriptions.get(i) %></td>
			         </tr>
		         <%
			      	}
		         %>
			        
			      </tbody>
			   </table>
			</div>  	
			        	
        </div><!--/span9-->
      </div><!-- row -->
        
       
        
</div><!-- container -->
</body>
</html>