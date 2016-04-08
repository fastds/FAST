<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.fastds.model.Plate"%>
<%@ page import="org.fastds.model.ObjectExplorer"%>
<%@ page import="edu.gzu.utils.Utilities"%>
<!-- 
<asp:Content ID="Content1" ContentPlaceHolderID="OEHead" runat="server"> -->
    <style type="text/css">
        div.content       
            { background-color: #ffffff; }
        table.content td  
            { font-size:9pt; font-family: arial,helvetica,sans-serif; }
        .h		            
            { background-color: #b0c4de; }
        .b		            
            { background-color: #eeeeee; }
        .t		            
            { background-color: #e0e0e0; }
        .sb		{ background-color: #eeeeee;font-size:8pt;}
        .st		{ background-color: #e0e0e0;font-size:8pt;}
        a.content		    
            { color: Navy; text-decoration: none; }
        a.content:hover	
            { color: Blue; text-decoration: underline;}
    </style>
    <!-- 
</asp:Content> -->
  <!-- 
<asp:Content ID="Content2" ContentPlaceHolderID="OEContent" runat="server">
 -->
<div class="content">

    <table class="content">
    <tr>
      <td colspan="3">
        <h1>Plate</h1>
        <p>The SDSS takes spectra using a large aluminum <em>plate</em> that can collect light 
        from hundreds to thousands of spectra at a time. Plates are given incremental plate 
        numbers, and are labeled with the MJD on which they were observed. If a plate is observed 
        on multiple nights, it will appear under each MJD. Data about each plate are are stored in the 
        <a href="<c:url value='/v1/Schema?name=Neighbors'/>" class='content'>Plate</a> table.</p>
        <p>The <em>Plate Data</em> table on the left below shows the data associated with the 
        plate on which this object's spectrum was taken. The <em>All Spectra on this plate</em> table 
        shows all the spectra that were measured on that plate, including this object's. Click on 
        one of those links to go to the Explore page for that object.</p>
      </td>
    </tr>
    <tr>
        <td colspan="2"><h2>Plate Data</h2></td>
    </tr>
    <tr>
    <td valign="top">
        <div id="plate">
        <%=((ObjectExplorer)request.getAttribute("master")).showVTable(((Plate)request.getAttribute("plate")).getDs(), 200)%>
        </div>
    </td>
    <td valign="top">
        <div id="objects">
        <h2>All Spectra on this plate</h2>
        <%=
        	((Plate)request.getAttribute("plate")).showFTable()
       %> 
        </div>
    </td>
    </tr>
    </table>
</div>
<!-- 
</asp:Content> -->