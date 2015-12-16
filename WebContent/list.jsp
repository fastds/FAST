<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

	<base href="<%=basePath%>"/>
	<link href="css/tools.css" rel="stylesheet" type="text/css" /> 
	<!-- 
	<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	 -->
    <style type="text/css">
      #toc   {position:absolute;top:10px;left:10px;}
      #content {position:absolute;top:0px;left:200px;background-color:White;}
      .tx	 	{WIDTH:160px; TEXT-ALIGN: left; FONT-SIZE: 8pt;}
    </style>
    
    <script src="js/ctrlscript.js" type="text/javascript"></script>
    <!-- 
    <script type="text/javascript" src="js/jquery-1.9.0.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
     -->
    <title></title>
</head>
<body>
    <script type="text/javascript">
        var branchname = "list";
    </script>
    <script type="text/javascript">
      function init(def) {
            //loadZoombar();
            drawzoombar();
            if (def == "0") {	// the opt came from the caller, set the checkboxes
                setoptstr(0);
                //drawzoombar();
                //resubmit();
            }
            var opt = "${opt}";
            setoptstr(opt);
            alert(opt);
        }

        function popup() {
            var s = "blank.htm";
            var w = window.open(s, "PRINTIMAGE",
			    "width=720,height=940,resizable=yes,scrollbars,menubar=yes");
            document.getElementById("getjpeg").target = "PRINTIMAGE";
            document.getElementById("getjpeg").action = "printlist.aspx";
            document.getElementById("getjpeg").method = "post";
            document.getElementById("getjpeg").submit();
            document.getElementById("getjpeg").target = "main";
            document.getElementById("getjpeg").action = "list.aspx";
            w.focus();
            return false;
        }
        //尾页
        function lastPage(pageCode)
        {
        	document.getElementById("cp").value = pageCode;
        	resubmit();
        }
        //首页
        function firstPage()
        {
	       	 document.getElementById("cp").value=1;
	       	 resubmit();
        }
        //上一页
        function prePage(pageCode)
        {
       		document.getElementById("cp").value = pageCode;
       		resubmit();
        }
        //下一页
        function nextPage(pageCode)
        {
       		document.getElementById("cp").value = pageCode;
       		resubmit();
        }
        //某一页
        function somePage(pageCode)
        {
       		document.getElementById("cp").value = pageCode;
       		resubmit();
        }
    </script>
    <form method="post" action="<c:url value='/v1/image/JpegList'/>" id="getjpeg">
		<div class="aspNetHidden">
		<input type="hidden" name="__VIEWSTATE" id="__VIEWSTATE" value="/wEPDwUJMzI3MzEzMjQ0ZGT7oXGsEWmshnfYo21h3E3jsulOZD+E7xet6xyg1zxH4g==" />
		</div>
		<input type="hidden" id="cp" name="cp" value="1"/>
		<div class="aspNetHidden">
			<input type="hidden" name="__VIEWSTATEGENERATOR" id="__VIEWSTATEGENERATOR" value="2CA4315D" />
		</div>
	    <div id="toc">
	      <table width="180" border="0" cellspacing="0" cellpadding="2" bgcolor="black">
		    <tr>
		      <td width="40"><a href="/dr12/" target="_top">
		        <img src="images/planet.jpg" border="0" width="40" height="50"/></a></td>
		      <td class='title' align="left" width="140">&nbsp;&nbsp;FASTDB</td>
	      </tr>
        </table>
        <!-- 导航区 -->
        <table border="0" cellspacing="0" cellpadding="0" >
	        <tr>
                <td class='s' align="left" onmouseover="this.T_WIDTH='140';return escape('SkyServer Home page')">|<a target="_top" href="/dr12/">Home&nbsp;|</a></td>
                <td class='s' align="left" onmouseover="this.T_WIDTH='140';return escape('Help on the current tool')"><a target="MosaicWindow" href="<c:url value='/listinfo.jsp'/>">Help&nbsp;|</a></td>
        
                        <td class='s' align="left" onmouseover="this.T_WIDTH='140';return escape('Go to the Finding Chart')"><a href="javascript:void(0)" onclick="return gotochart();">Chart&nbsp;|</a></td>
				         
					    <td class='s' align="left" onmouseover="this.T_WIDTH='140';return escape('Go to the Navigate Tool')"><a href="navi.jsp?ra=337.138&dec=-0.948">Navi&nbsp;|</a></td>
			            			
		        <td class='s' align="left" onmouseover="this.T_WIDTH='140';return escape('Go to the Object Explorer')"><A href="javascript:void(0)" onclick="return gotoExp();">Explore&nbsp;|</A></td>	
	        </tr>
        </table>

        <table width="180" cellpadding="0" cellspacing="0" border="0">
        <tr><td class='s'><a href="sqltoform.aspx" target="_top">Use query to fill form</a>
        </td></tr>
        <tr><td>
	        <textarea wrap="off" class='tx' cols="12" 
		        name="paste" rows="5"><c:if test="${paste == null}">     name        ra     dec  
274-51913-230 159.815 -0.655
275-51910-275 161.051  0.152
275-51910-525 161.739  0.893
276-51909-19  164.090 -0.889
278-51900-39  168.470  0.004
278-51900-112 168.092 -0.255
278-51900-225 167.091 -0.216
278-51900-430 167.114  0.249
279-51984-456 168.956  0.860
279-51984-520 169.472 -0.007
281-51614-230 171.109 -0.427
282-51658-167 173.898 -0.585
285-51930-309 178.908 -0.771
286-51999-359 180.271  0.114
288-52000-173 184.837 -0.242
349-51699-582 255.537 64.206
353-51703-328 255.737 60.563
353-51703-365 256.157 60.585
355-51788-167 258.984 57.238
355-51788-563 260.121 58.797
358-51818-349 260.930 57.007
387-51791-72    0.744  0.142
389-51795-481   3.874  0.640
390-51900-196   5.183 -0.440
390-51900-464   5.432  0.296</c:if><c:if test="${paste ne null }">${paste}</c:if></textarea> 
        </td></tr>
        <tr><td class='s'>
        Cut and paste ra/dec list<br/>
        </td></tr>
        </table>

        <p />
        <table width="180" bgcolor="lightblue" border="1" cellspacing="0" cellpadding="0" valign="middle">
	        <tr><td colspan="2" align="center" bgcolor="skyblue">Parameters</td></tr>
        <tr>
	<td><span  onmouseover="this.T_TEMP='3000';this.T_WIDTH='180';return escape('')">scale</span></td>
		<td>
		<table cellspacing="0" cellpadding="0">
		<tr><td>
			<input class='in' type='text' size='3' maxlength='20' align='right'
			value='${scale eq null?0.4:scale}' name='scale' id='scale' onchange='setscale(0)' /></td><td>&nbsp;<span  onmouseover="this.T_TEMP='3000';this.T_WIDTH='180';return escape('')">''/pix			</span></td></tr></table>
		</td>
		</tr>
<tr>
	<td><span  onmouseover="this.T_TEMP='3000';this.T_WIDTH='180';return escape('')">opt </span></td>
		<td>
		<table cellspacing="0" cellpadding="0"><tr><td>
			<input class='in' type='text' size='3' maxlength='20' align='right'
			value='${opt}' name='opt' id='opt' onchange='setoptstr(1);' /></td><td>&nbsp;<span  onmouseover="this.T_TEMP='3000';this.T_WIDTH='180';return escape('')">			</span></td></tr></table>
		</td>
</tr>

        </table>

            <table  width="180" cellspacing="4" cellpadding="0" border="0">
	            <tr><td align="center" onmouseover="this.T_WIDTH='140';return escape('Get an image of the sky at the specified coordinates')">
	            <a href="javascript:void(0);" onclick="return resubmit();"><img 
			            src="images/get_image.jpg" alt="Submit" 
			            border="0" width="112" height="40"/></a>
	            </td>
	            <!-- 
	            <td align="center" onmouseover="this.T_WIDTH='140';return escape('Open a new window with a printable image')">
	            <a href="javascript:void(0);" onclick="return popup();"><img 
			            src="images/printer_icon_blue.jpg" alt="Printable Image" 
			            border="0" width="40" height="40"/></a>
	            </td> -->
	            </tr>
	
            </table>

            <table width="180" cellpadding="0" cellspacing="0" border="0">
            <tr>
	            <td><img src="./images/zoombar5.jpg" alt="Zoom In" 
			            border="0" width="160" height="32" ismap="ismap" 
			            usemap="#zoom_bar_map" id="zoombar"/></td>
            </tr>
            </table>

            <p />

            <map name="zoom_bar_map">
            <area shape="rect" alt="zoom in" coords=" 2,8,24,28" href="javascript:void(0);" onmouseover="this.T_WIDTH='140';return escape('Zoom in')" onclick="return stepzoom(0.5)"/>
            <area shape="rect" alt="zoom=5"  coords="25,8,30,24" href="javascript:void(0);" onclick="return setzoom(5)"/>
            <area shape="rect" alt="zoom=4"  coords="33,8,38,24" href="javascript:void(0);" onclick="return setzoom(4)"/>
            <area shape="rect" alt="zoom=3"  coords="41,8,46,24" href="javascript:void(0);" onclick="return setzoom(3)"/>
            <area shape="rect" alt="zoom=2"  coords="50,8,55,24" href="javascript:void(0);" onclick="return setzoom(2)"/>
            <area shape="rect" alt="zoom=1"  coords="58,8,63,22" href="javascript:void(0);" onclick="return setzoom(1)"/>
            <area shape="rect" alt="zoom=0"  coords="66,8,71,24" href="javascript:void(0);" onclick="return setzoom(0)"/>
            <area shape="rect" alt="zoom=-1" coords="75,8,80,24" href="javascript:void(0);" onclick="return setzoom(-1)"/>
            <area shape="rect" alt="zoom=-2" coords="83,8,88,24" href="javascript:void(0);" onclick="return setzoom(-2)"/>
            <area shape="rect" alt="zoom=-3" coords="91,8,96,24" href="javascript:void(0);" onclick="return setzoom(-3)"/>
            <area shape="rect" alt="zoom=-4" coords="99,8,104,24" href="javascript:void(0);" onclick="return setzoom(-4)"/>
            <area shape="rect" alt="zoom=-5" coords="107,8,112,24" href="javascript:void(0);" onclick="return setzoom(-5)"/>
            <area shape="rect" alt="zoom=-6" coords="115,8,120,24" href="javascript:void(0);" onclick="return setzoom(-6)"/>
            <area shape="rect" alt="zoom=-7" coords="123,8,128,24" href="javascript:void(0);" onclick="return setzoom(-7)"/>
            <area shape="rect" alt="zoom out" coords="132,6,156,28" href="javascript:void(0);" onmouseover="this.T_WIDTH='140';return escape('Zoom out')" onclick="return stepzoom(2.0)"/>
            </map>
	

            <table width="180" cellpadding="0" cellspacing="0" border="1"> 
              <tr><td align="center" bgcolor="skyblue" onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Mark various options on the image')">Drawing options</td></tr>
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Grid with tickmarks to show image scale')"><input type="checkbox" onclick="setopt(this,'G')" name="Grid"  id="Grid" /> Grid</td></tr>
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Information on the Navigation window view')"><input type="checkbox" onclick="setopt(this,'L')" name="Label"  id="Label"/> Label</td></tr>
		      <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Marks photometrically identified objects with light blue circles')"><input type="checkbox" onclick="setopt(this,'P')" name="PhotoObjs"  id="PhotoObjs"/> Photometric objects</td></tr>
		      <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Marks spectroscopic objects with red squares')"><input type="checkbox" onclick="setopt(this,'S')" name="SpecObjs"  id="SpecObjs"/> Objects with spectra</td></tr>
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Inverts black and white in the image')"><input type="checkbox" onclick="setopt(this,'I')" name="InvertImage"   id="InvertImage"/> Invert Image</td></tr>	
			  <tr><td align="center" bgcolor="skyblue">Advanced options</td></tr>	
			  <!-- 
		      <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows APOGEE DATA')"><input type="checkbox" onclick="setopt(this,'A')" name="APOGEE" id="Checkbox3"/> APOGEE Spectra</td></tr> 		
			   <tr><td  onmouseover="this.T_TEMP='5000';this.T_WIDTH='140';return escape('Draws the outline (green) of each photometric object except at the largest zoom-out scales (where they are not legible)')"><input type="checkbox" onclick="setopt(this,'O')" name="Outline"  id="Outline"/>SciDB Outlines</td></tr> 
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Draws a rectangular box (pink) around each photometric object')"><input type="checkbox" onclick="setopt(this,'B')" name="BoundingBox" id="BoundingBox"/>SDSS Bounding Boxes</td></tr>-->
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows each SDSS field (~10x14 arcmin) in gray')"><input type="checkbox" onclick="setopt(this,'F')" name="Fields" id="Fields"/>SDSS Fields</td></tr>
			  <!-- <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows masks (pink) around bright objects and data artifacts')"><input type="checkbox" onclick="setopt(this,'M')" name="Masks" id="Masks"/>SDSS Masks</td></tr> 
			  <tr><td  onmouseover="this.T_TEMP='2000';this.T_WIDTH='140';return escape('Shows plates (lavender) used to collect spectra')"><input type="checkbox" onclick="setopt(this,'Q')" name="Plates" id="Plates"/>SDSS Plates</td></tr>		-->
			</table>

    </div>
    <input type="hidden" value="1" name="page"/>
    </form>
    <div id="content">
        
<div class="content">
<!-- 错误提示 -->
${errors}
<!-- 分页 -->
<div align="center" style="margin:20px;">
	<c:if test="${page!=null}">
		<c:choose>
			<c:when test="${page.totalPages<=10}">
				<c:set var="begin" value="1"></c:set>
				<c:set var="end" value="${page.totalPages}"></c:set>
			</c:when>
			<c:otherwise>
				<!-- 头溢出 -->
				<c:if test="${page.currentPage-5<1}">
					<c:set var="begin" value="1"></c:set>
					<c:set var="end" value="10"></c:set>
				</c:if>
				<!-- 尾溢出 -->
				<c:if test="${page.currentPage+4>page.totalPages}">
					<c:set var="begin" value="${page.totalPages-9}"></c:set>
					<c:set var="end" value="${page.totalPages}"></c:set>
				</c:if>
				<!-- 其他 -->
				<c:if test="${page.currentPage-5>=1 and page.currentPage+4<=page.totalPages}">
					<c:set var="begin" value="${page.currentPage-5}"></c:set>
					<c:set var="end" value="${page.currentPage+4}"></c:set>
				</c:if>
			</c:otherwise>
		</c:choose>
		<c:if test="${page.currentPage ne 1}">
	   		<a href="#" onclick="return prePage(${page.currentPage-1});">&lt;&lt;</a>
	   	</c:if>	
   		<c:forEach var="i" begin="${begin}" end="${end}">
   			<c:if test="${page.currentPage eq i}">
   				[${i}] 
   			</c:if>
   			<c:if test="${page.currentPage ne i}">
   				<a href="#" onclick="return somePage('${i}');"> ${i} </a>
   			</c:if>
   		</c:forEach>
		<c:if test="${page.currentPage ne page.totalPages}">
			<a href="#" onclick="return nextPage('${page.currentPage+1}');">&gt;&gt;</a>
		</c:if>
	   [${page.currentPage}/${page.totalPages}]
	 </c:if>
 </div>
<table border="1"  align="center"  cellpadding="1" cellspacing="1">
	<c:set var="flag" value="false"></c:set><!-- flag:表示是否打印<tr> -->
	
	<c:forEach items="${page.jpegInfo}" var="jpeg" varStatus="vs">
	<c:choose>
		<c:when test="${vs.index%5 eq 0 and flag eq false}">
			<tr>
		  <c:set var="flag" value="true"></c:set>
		</c:when>
		<c:when test="${vs.index%5 eq 0 and flag eq true}">
			</tr>
			<tr>
		</c:when>
	</c:choose>
		<td class="i">
		<a class="i" href="<c:url value='/navi.jsp?ra=${jpeg.ra}&dec=${jpeg.dec}'/>" target="_blank">${jpeg.info}<br/><img src="${jpeg.url}" width="120px" height="120px"/></a>
		</td>
		<c:if test="${vs.last}">
			</tr>
		 	 <c:set var="flag" value="false"></c:set>
		</c:if>
	</c:forEach>
	
</table>

</div>

    </div>
    <script type="text/javascript">init('1');</script>
    <script language="JavaScript" type="text/javascript" src="js/wz_tooltip.js"></script>
</body>
</html>
