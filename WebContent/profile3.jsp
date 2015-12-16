<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
	<title>FASTDB</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- Bootstrap -->
    <link href="validator/bootstrap.3.2.css" rel="stylesheet">
    <link href="validator/jquery.validator.css" rel="stylesheet">
	
	<link href="validator/core.css" rel="stylesheet">
    <script src="validator/jquery-1.11.2.min.js"></script>
     <script src="validator/jquery.validator.min.js"></script>
     <script src="validator/zh_CN.js"></script>
     <script type="text/javascript">
     	$(function(){
     		$('#registe-form').validator({
     			theme: 'yellow_right',

     			fields : {
     				username : {rule : 'required;length[3~]',tip:'Login can not be blank'},
     				name :{rule : 'required;chinese;;length[~10]',tip:'输入中文的姓名'},
     				email : {rule : 'required;email',tip : '输入你常用的邮箱地址'},
     				cellphone: {rule : 'required;mobile', tip : '输入你常用的电话号码'},
     				telephone: {rule : 'tel', tip : '输入你固定电话号码'},
     				qq : {rule: 'qq',tip :'输入你常用的QQ号码'},
     				password : {rule : 'required;password',tip :'输入6-16位数字、字母组成的密码'},
     				rpassword : {rule : 'required;match(password)',tip :'确认输入的密码',msg: {'match(password)': "请填写密码"}},
     			},
     			 valid: function(form){
     		        //è¡¨åéªè¯éè¿ï¼æäº¤è¡¨åå°æå¡å¨
     		      alert("tongguo");
     		    }
     			
     		});
     		
     		
     	});
     </script>
  </head>
 <body>
		<div class="container">
			<jsp:include page="top.jsp"></jsp:include>
			<div class="row">
				<div class="span12">
					<h1> Personal Information </h1>
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
								<label class="control-label" for="input01"> User Name </label>
								<div class="controls">
									<input type="text" class="input-xlarge" id="userName" value="${sessionScope.user.getUserName()}" name="userName" disabled="disabled" />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="input01"> Contact Number</label>
								<div class="controls">
									<input type="text" class="input-xlarge" id="cellphone" value="${sessionScope.user.getCellphone() }" name="cellphone"/>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="input01">Email</label>
								<div class="controls">
									<input type="text" class="input-xlarge" id="email" value="${sessionScope.user.getEmail() }" name="email" />
								</div>
							</div>					
							<div class="control-group">
								<label class="control-label" for="textarea"> Description</label>
								<div class="controls">
									<textarea   style="resize:none" class="input-xlarge" id="description" rows="4" name="description">${sessionScope.user.getDescription() }</textarea>
								</div>
							</div>	
							<c:if test="${sessionScope.user ne null}">					
								<div class="form-actions">
									<button  type="submit" id="regist_btn"  class="regist-btn"> Submit</button> <a href="<c:url value='/modifypsw.jsp'/>" class="btn"> Modify PassWordÂÂ</a>
								</div>
							</c:if>
						</fieldset>
					</form>
				</div>
			</div>
			</div>
		
	</body>
</html>