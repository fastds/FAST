<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.fastds.model.Neighbors" %>
<%@ page import="org.fastds.model.ObjectExplorer" %>
<div id="neighbors" class="content">
<table class="content">
    <tr><td>
    <h2>Nearby objects (neighbors)</h2>

    <p class="content">Many interesting astronomical questions start with asking, "what is near this 
    object"? To enable these kinds of questions, the list below shows all SDSS objects within 0.5 
    arcminutes of the selected object.</p>

    <p class="content">Finding nearby objects is computationally intensive, so we have precomputed 
    a 0.5-arcminute neighbors search for each object in the photoObjAll table. These precomputed 
    neighbor searches are stored in the  
    <a href="../../help/browser/browser.aspx?cmd=description+neighbors+U" class='content'>Neighbors</a> 
    table.</p>
    </td></tr>

    <tr><td>

    <h3>This Object</h3>
    <%=((ObjectExplorer)request.getAttribute("master")).showHTable(((Neighbors)request.getAttribute("Neighbors")).getDs_neighbor1(), 200, "PhotoObj");        
    %>
    <h3>Neighboring objects within 0.5 arcminutes</h3>
    <%=((ObjectExplorer)request.getAttribute("master")).showHTable(((Neighbors)request.getAttribute("Neighbors")).getDs_neighbor2(), 500, "Neighbors");           
    %>
</td></tr></table>
</div>