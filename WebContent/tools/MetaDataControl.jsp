<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="edu.gzu.utils.Utilities" %>
<%@ page import="org.fastds.model.MetaDataControl" %>
<c:choose>
	<c:when test="${not empty master.objID}">
		<div id="metadata">
		    <table class="content">
		        <tr>
		            <td colspan="2">
		                <h1 id="sdssname">
		                <%= Utilities.SDSSname(((MetaDataControl)request.getAttribute("metaDataCtrl")).getRa(), ((MetaDataControl)request.getAttribute("metaDataCtrl")).getDec())%>
		                </h1>
		                <h2 id="othernames">&nbsp;<input type="button" onclick=" findOtherNames(${ra}, ${dec});" value="Look up common name" /></h2>
		            </td>
		        </tr>
		        <tr>
		            <td>
		                <table width="620">
		                    <tr>
		                        <td align="center" class="h" colspan="2">Type</td>
		                        <td align="center" class="h" colspan="2">SDSS Object ID</td>
		                    </tr>
		                    <tr>
		                        <td class="t" align="center" colspan="2"><b>${metaDataCtrl.otype}</b></td>
		                        <td class="t" align="center" colspan="2">${master.id}</td>
		                    </tr>
		                    <tr>
		                        <td class="h" align="center" colspan="2">RA, Dec</td>
		                        <td class="h" align="center" colspan="2">Galactic Coordinates (<i>l</i>, <i>b</i>)</td>
		                    </tr>
		                    <tr>
		                        <td align="center" class="h">Decimal</td>
		                        <td align="center" class="h">Sexagesimal</td>
		                        <td align="center" class="h"><i>l</i></td>
		                        <td align="center" class="h"><i>b</i></td>
		                    </tr>
		                    <tr>
		                        <td align="center" class="t">
		                            <script type="text/javascript" language="javascript">
		                                var thera = new Number(<%=((MetaDataControl)request.getAttribute("metaDataCtrl")).getRa() %>);
		                                var thedec = new Number(<%=((MetaDataControl)request.getAttribute("metaDataCtrl")).getDec()%>);
		                                document.write(thera.toFixed(5) + ', ' + thedec.toFixed(5));
		                            </script>
		                        </td>
		                        <td align="center" class="t">
		                            <span class="large"><%= Utilities.hmsC(((MetaDataControl)request.getAttribute("metaDataCtrl")).getRa()) + ", " + Utilities.dmsC(((MetaDataControl)request.getAttribute("metaDataCtrl")).getDec()) %></span>
		                        </td>
		                        <td align="center" class="t">
		                            <script type="text/javascript" language="javascript">
		                                var L = new Number(<%=Utilities.ra2glon(((MetaDataControl)request.getAttribute("metaDataCtrl")).getRa(), ((MetaDataControl)request.getAttribute("metaDataCtrl")).getDec())%>);
		                                document.write(L.toFixed(5));
		                            </script>
		                        </td>
		                        <td align="center" class="t">
		                            <script type="text/javascript" language="javascript">
		                                var B = new Number(<%=Utilities.dec2glat(((MetaDataControl)request.getAttribute("metaDataCtrl")).getRa(), ((MetaDataControl)request.getAttribute("metaDataCtrl")).getDec())%>);
		                                document.write(B.toFixed(5));
		                            </script>
		                        </td>
		                    </tr>
		                 </table>
		              </td>
		        </tr>
		    </table>
		</div> <!-- end of metadata div -->
	</c:when>
	<c:otherwise>
		<table cellpadding=2 cellspacing=2 border=0 width=625>
            <%--<tr><td class='nodatafound'>Object is out of SDSS footprint</td></tr>--%>  
            <c:choose>
            	<c:when test="${empty master.apid and empty master.specObjID }">
            		<tr><td class='nodatafound'>The object corresponding to the id specified does not exist in the database.<br> Please try another object.</td></tr>
            	</c:when>
            	<c:when test="${not empty master.apid or not empty master.specObjID }">
            		<tr><td class='nodatafound'>There is no image corresponding to the specified id in the SDSS imaging data.</td></tr>
            	</c:when>
            </c:choose>
	    </table>
	</c:otherwise>
</c:choose>
