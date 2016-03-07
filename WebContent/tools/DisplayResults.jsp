<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.fastds.model.DisplayResults %>
<%@ page import="org.fastds.model.ObjectExplorer" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<asp:Content ID="Head1" ContentPlaceHolderID="OEHead" runat="server">
    <script type="text/javascript" src="./javascript/explore.js"></script>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="OEContent" runat="server">
   <div class="content">
   <div id="QueryResults">
      <!--  url = master.getURL(); 后台已有，但不明确这里赋值是否有影响，属于内嵌java代码 -->
       
    <p />
     <h2><a href="${displayResults.url }/help/browser/browser.aspx?cmd=description+${displayResults.name}+U" target="_top" class="content">${displayResults.name}</a></h2>
    <p />   
    <!--    put the option for Plate Objects -->
    <%=((ObjectExplorer)request.getAttribute("master")).showVTable(((DisplayResults)request.getAttribute("displayResults")).getDs(), 300))%>
    </div>
 </div>    
</asp:Content>