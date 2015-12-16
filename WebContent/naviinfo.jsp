<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'naviinfo.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style> h1, h2, h3, h4, h5, ul, p {FONT-FAMILY: arial,helvetica,sans-serif}
	td {FONT-FAMILY: arial,helvetica,sans-serif;FONT-SIZE: 12pt;}
	.q {FONT-WEIGHT: 800; FONT-STYLE: italic;}
	.c {FONT-FAMILY: arial,helvetica,sans-serif;FONT-SIZE: 10pt;}
	.s {FONT-FAMILY: arial,helvetica,sans-serif;FONT-SIZE: 8pt;}
	.h {FONT-FAMILY: arial,helvetica,sans-serif;FONT-SIZE: 14pt;FONT-WEIGHT:800;}
	    </style>
  </head>
  
  <body>
    <h1>FASTDB Navigate Tool</h1>
		
<table BORDER=0 WIDTH="440"  cellpadding=1 cellspacing=1>
  


 	<tr VALIGN=top><td>
	This page provides easy navigation in the vicinity of a given point on the sky. One can 
	move around by clicking on the frame of the image, or on individual objects. A short list 
	of the basic properties and a magnified thumbnail image of the object nearest to the 
	selected point is then displayed. 
	<p>
	If you're new to the Navigate tool, please see the <a href="<%="url"%>/tools/chart/default.aspx" target="Visual">Visual Tools main page</a>
	and <a href="#navi" onclick="window.open('../started/navi.aspx','popup','width=440,height=580,resizeable,scrollbars');">
	Getting Started with Navigate</a>.

	<br>&nbsp;<br>
	There is also a link to the Explore Tool, which contains further links to the more detailed 
	properties of the object, and its links to the rest of the database, like its spectrum or 
	neighbors, etc. 
 
	For the description of the other drawing options see the Help section of the 
	<a href="chartinfo.aspx">Finding Chart</a>.  
	</td></tr>
</table>

		<hr width="540" align="left">
		<span class='s'>	Authors: Jim Gray, Alex Szalay, Maria Nieto-Santisteban, Tamas Budavari, 
        February 2004.</span>
  </body>
</html>
