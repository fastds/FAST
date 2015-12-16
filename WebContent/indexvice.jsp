<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<!--[if lt IE 7 ]><html lang="en" class="ie6 ielt7 ielt8 ielt9"><![endif]--><!--[if IE 7 ]><html lang="en" class="ie7 ielt8 ielt9"><![endif]--><!--[if IE 8 ]><html lang="en" class="ie8 ielt9"><![endif]--><!--[if IE 9 ]><html lang="en" class="ie9"> <![endif]--><!--[if (gt IE 9)|!(IE)]><!--> 
<html lang="en" class="ie6 ielt7 ielt8 ielt9">
	<head>
	<base href="<%=basePath%>">
		<meta charset="utf-8">
		<title>FASTDB</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
		<link href="css/site.css" rel="stylesheet">
		<!--[if lt IE 9]><script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
	</head>
	<body>
		<div class="container">
			<div class="navbar">
				<div class="navbar-inner">
					<div class="container">
						<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </a> <a class="brand" href="#">FASTDB</a>
						<div class="nav-collapse">
							<ul class="nav">
								<li class="active">
									<a href="index.jsp">主页</a>
								</li>
								<li>
									<a href="settings.htm">账号设置</a>
								</li>
								<li>
									<a href="help.htm">帮助</a>
								</li>
								<li class="dropdown">
									<a href="help.htm" class="dropdown-toggle" data-toggle="dropdown">Tours <b class="caret"></b></a>
									<ul class="dropdown-menu">
										<li>
											<a href="help.htm">Introduction Tour</a>
										</li>
										<li>
											<a href="help.htm">Project Organisation</a>
										</li>
										<li>
											<a href="help.htm">Task Assignment</a>
										</li>
										<li>
											<a href="help.htm">Access Permissions</a>
										</li>
										<li class="divider">
										</li>
										<li class="nav-header">
											Files
										</li>
										<li>
											<a href="help.htm">How to upload multiple files</a>
										</li>
										<li>
											<a href="help.htm">Using file version</a>
										</li>
									</ul>
								</li>
							</ul>
							<form class="navbar-search pull-left" action="">
								<input type="text" class="search-query span2" placeholder="Search" />
							</form>
							<ul class="nav pull-right">
								<li>
									<a href="profile.htm">@username</a>
								</li>
								<li>
									<a href="login.htm">退出</a>
								</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span3">
					<div class="well" style="padding: 8px 0;">
						<ul class="nav nav-list">
							<li class="nav-header">
								导航/Navigation
							</li>
							<li class="active">
								<a href="index.jsp"><i class="icon-white icon-home"></i> 主页/Home</a>
							</li>
                            <li>
								<a href="query.jsp"><i class="icon-check"></i> 查询/Cheak</a>
							</li>
							<li>
								<a href="projects.htm"><i class="icon-folder-open"></i> 增加/Upload(csv)</a>
							</li>
							
							<li>
								<a href="activity.htm"><i class="icon-list-alt"></i> 日志信息/Log Information</a>
							</li>
							<li class="nav-header">
								账号信息
							</li>
							<li>
								<a href="profile.htm"><i class="icon-user"></i> 个人信息</a>
							</li>
							<li>
								<a href="settings.htm"><i class="icon-cog"></i> 设置/setting</a>
							</li>
							<li class="divider">
							</li>
							<li>
								<a href="help.htm"><i class="icon-info-sign"></i> 帮助</a>
							</li>
							<li class="nav-header">
								其他
							</li>
							<li>
								<a href="gallery.htm"><i class="icon-picture"></i> Gallery</a>
							</li>
							
						</ul>
					</div>
				</div>
				<div class="span9">
					<div class="hero-unit">
						<h1>
							欢迎进入FASTDB
						</h1>
						<p>
							了解详情，请查看帮助信息.
						</p>
						<p>
						<a href="help.htm" class="btn btn-primary btn-large">查看帮助</a> 
						</p>
					</div>
					<div class="well summary">
						<ul>
							<li>
								<a href="#"><span class="count">3</span> Projects</a>
							</li>
							<li>
								<a href="#"><span class="count">27</span> Tasks</a>
							</li>
							<li>
								<a href="#"><span class="count">7</span> Messages</a>
							</li>
							<li class="last">
								<a href="#"><span class="count">5</span> Files</a>
							</li>
						</ul>
					</div>
							<form  method="post" action="/FAST/servlet/IndexServlet">
							<input type="submit" class="btn btn-primary" value="查看instance" />
							<input type="hidden" name="status" value="knowinstan" />
							</form>

						<%
							String result = " ";
    							String res = (String)request.getAttribute("insresult");
    							if(res!=null)
    								result = res;
    							String error = "";
    							String errorInfo = (String)request.getAttribute("errorInfo");
    							if(errorInfo!=null)
    								error = errorInfo;
  						%>
  							<div class="form-group">
								<pre><%=result %><%=error %></pre>
  							</div>
					<h2>
						日志记录
					</h2>
					<table class="table table-bordered table-striped">
						<thead>
							
							<tr>
								<th>
									用户
								</th>
								<th>
									操作
								</th>
								<th>
									日期
								</th>
								<th>
									Date
								</th>
								
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>
									Nike.com Redesign
								</td>
								<td>
									Monsters Inc
								</td>
								<td>
									New Task
								</td>
								<td>
									4 days ago
								</td>
								
							</tr>
							
						</tbody>
					</table>
					<ul class="pager">
						<li class="next">
							<a href="activity.htm">更多 &rarr;</a>
						</li>
					</ul>
                  
				</div>
			</div>
		</div>
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/site.js"></script>
	</body>
</html>
