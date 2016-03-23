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
         <c:choose>
         	<c:when test="${requestScope.cols ne null }">
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
         	</c:when>
         	<c:otherwise>
         		<h2>Schema Browser</h2>
         		<p>
         		The data in the database is contained in Tables, organized in columns and rows.
We have defined Views over the tables. These represent special subsets of the
original table.
				</p>
         		<p>
         		Most of the tables also have one or more Indices defined on them to speed up
searches on them. Please see the Archive Intro Help page for more information
on the types of indices.
         		</p>
         		<p>
         		The table DataConstants contains most of the bit-flags and enumerated quantities
relevant to the SDSS. Their values " can be displayed by clicking on the link in the
left hand panel. There are several access functions to make ineterpretations and
the " back and forth conversions easier. They are displayed when you look" at the
individual enumerated fields."
         		</p>
         	</c:otherwise>
         </c:choose>
			        	
        </div><!--/span9-->
      </div><!-- row -->
        
</div><!-- container -->
</body>
</html>