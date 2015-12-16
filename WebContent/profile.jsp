<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en" class="ie6 ielt7 ielt8 ielt9">
	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8">
		<title>FASTDB</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
		<link href="css/site.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>
  		<script src="js/bootstrap.min.js"></script>
		<script src="js/site.js"></script>
		<link href="validator/jquery.validator.css" rel="stylesheet">
	
    <script src="validator/jquery-1.11.2.min.js"></script>
     <script src="validator/jquery.validator.min.js"></script>
     <script src="validator/zh_CN.js"></script>
	  <script type="text/javascript">
     	$(function(){
     		$('#edit-profile').validator({
     			theme: 'yellow_right',

     			fields : {
     				//userName : {rule : 'required;length[3~]',tip:'输入英文登陆名称'},
     				name :{rule : 'required;chinese;;length[~10]',tip:'输入中文的姓名'},
     				email : {rule : 'required;email',tip : '输入你常用的邮箱地址'},
     				cellphone: {rule : 'required;mobile', tip : '输入你常用的电话号码'},
     				telephone: {rule : 'tel', tip : '输入你固定电话号码'},
     				qq : {rule: 'qq',tip :'输入你常用的QQ号码'},
     				password : {rule : 'required;password',tip :'输入6-16位数字、字母组成的密码'},
     				rpassword : {rule : 'required;match(password)',tip :'确认输入的密码',msg: {'match(password)': "请填写密码"}},
     			},
     			 valid: function(form){
     				 $.ajax({
     					type: "POST",
      		            url: "v1/userUpdate",
      		          data: $("#edit-profile").serialize(), 
      		            dataType : 'text',
      		            success: function(data){
      		            	alert("修改成功");
      		            	//$("#msg").html("修改成功"); 
      		            	setTimeout("show()",1000);
      		            }
      		        });
				
     		    }
     			
     		});
     		
     		
     	});
     	
     	function show(){
     		 $("#msg").html(""); 
     	}
     </script>
		
	
</head>
	<body>
		<div class="container">
			<jsp:include page="top.jsp"></jsp:include>
			<div class="row">
				<div class="span12">
					<h1> 个人信息 </h1>
					<div id="message"></div>
					<form id="edit-profile" class="form-horizontal" method="post" action="">
						<fieldset>
							<legend align="center"> 
							<c:if test="${sessionScope.user eq null}">
								<div class="alert alert-warning">Click <a href="<c:url value='/login.jsp'/>"><b>here</b></a> to login</div>
							</c:if>
							</legend>
							
							<font color="green">${requestScope.msg }</font>
							<div class="control-group">
								<input type="hidden" name="id" id="id" value="${sessionScope.user.getId()}"/>
								<input type="hidden" name="gender" id="gender" value="${sessionScope.user.getGender()}"/>
						
								<label class="control-label" for="input01"> Account </label>
								<div class="controls">
									<input type="text" class="input-xlarge" id="userName" value="${sessionScope.user.getUserName()}" name="userName" readOnly="true"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="input01">E-mail </label>
								<div class="controls">
									<input type="text" class="input-xlarge" id="email" value="${sessionScope.user.getEmail() }" name="email" />
								</div>
							</div>	
							
							<div class="control-group">
								<label class="control-label" for="input01">Area For Research  </label>
								<div class="controls">
									<input type="text" class="input-xlarge" id="researchArea" value="${sessionScope.user.getResearchArea() }" name="researchArea" />
								</div>
							</div>	
							
							<div class="control-group">
								<label class="control-label" for="input01">Actived </label>
								<div class="controls">
									<input type="text" class="input-xlarge" id="activated" value="${sessionScope.user.isActivated() }" name="activated" readOnly="true"/>
								</div>
							</div>	
							
							<div class="control-group">
								<label class="control-label" for="input01">Telephone </label>
								<div class="controls">
									<input type="text" class="input-xlarge" id="cellphone" value="${sessionScope.user.getCellphone() }" name="cellphone"/>
								</div>
							</div>
											
							<div class="control-group">
								<label class="control-label" for="textarea">Description </label>
								<div class="controls">
									<textarea   style="resize:none" class="input-xlarge" id="description" rows="4" name="description">${sessionScope.user.getDescription() }</textarea>
								</div>
							</div>	
							<div class="form-actions">
							<c:if test="${sessionScope.user ne null}">					
								
									<button type="submit" class="btn btn-primary"> Submit </button> <a href="<c:url value='/modifypsw.jsp'/>" class="btn"> Modify PassWord</a>
								
							</c:if>
							</div>
						</fieldset>
					</form>
				</div>
			</div>
			</div>
		
	</body>
</html>
