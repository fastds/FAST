<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container">
<nav role="navigation" id="header" class="navbar navbar-inverse navbar-fixed-top" >
	<div class="navbar-inner">
	 	<div class="container">
			<a class="brand">FASTDB</a>
			<div id="navigation" class="collapse nav-collapse" >
			
				<ul class="nav navbar-nav" >
					
				<li >
				<a href ="index.jsp"><i class="icon-white icon-home"></i> Home</a>
				</li>
				<li >
				<a href="querypage.jsp"><i class="icon-white icon-edit"></i> Search</a>
				</li>
				<li >
					<a href="<c:url value='listinfo.jsp'/>" target="_blank"><i class="icon-white icon-picture"></i> ImageView</a>
				</li>
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-white icon-info-sign"></i> Help <b class="caret"></b></a>
						<ul class="dropdown-menu">
							
							<li >
								<a href="<c:url value='help.jsp'/>" target="_blank">Document</a>
							</li>
							<li>
								<a href="<c:url value='help.html'/>" target="_blank">API</a>
							</li>
							<li>
								<a href="<c:url value='/v1/schema'/>" target="_blank">Schema</a>
							</li>
							
						</ul>
				  </li>
				
				
				<c:if test="${sessionScope.user ne null}">
					<li >
						<a href="<c:url value='/profile.jsp'/>"><i class="icon-white icon-user"></i> Profile</a>
					</li>
					<li >
						<a href="userquerypage.jsp"><i class="icon-white icon-plus-sign"></i> Container </a>
					</li>
					<!-- <li >
					<a href="<c:url value='upload.jsp'/>" >upload(csv)</a>
					</li> -->
					<!-- <li >
					<a href="<c:url value='userquerypage.jsp'/>" >私有数据</a>
					</li> -->
				</c:if>
				
				
				</ul>
				<ul class="nav pull-right">
					<li class="active">
	  					<c:choose>
							<c:when test="${sessionScope.user ne null }">
								<a 	href="<c:url value='/profile.jsp'/>"><i class="icon-white icon-user"></i>&nbsp${sessionScope.user.getUserName()}</a>
							</c:when>
							<c:otherwise>
							<a  href="<c:url value='/login.jsp'/>">Click to Login</a>
							</c:otherwise>
						</c:choose>
					</li>
					
					<li>
						<c:if test="${sessionScope.user ne null }">
						<li class="active">
							<a href="<c:url value='v1/loginout' />">Exit</a>
						</li>
						</c:if>
					</li>
					
				</ul>
			</div>
		</div>
	 </div>
</nav>
</div> 
<hr class="featurette-divider">


			