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
<title>FASTDB</title> 
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<script type="text/javascript" src="js/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="images/login.js"></script>
<link href="css/login2.css" rel="stylesheet" type="text/css" />
<link href="css/bootstrap.min.css" rel="stylesheet" />
<link href="css/bootstrap-responsive.min.css" rel="stylesheet"/>
<link href="css/site.css" rel="stylesheet"/>
<script type="text/javascript">
		/*
		如果一个表单项的name和<img>的id相同，那么可能会出问题！一般只有IE出问题！
		*/
		function change() {
			/*
			1. 获取<img>元素
			*/
			var ele = document.getElementById("vCode");
			ele.src = "<c:url value='v1/verify'/>?xxx="+new Date().getTime();
			
		}
		
		function login()
		{
			var form = document.getElementById("logUser");
			var name = document.getElementById("name").value;;
			var password = document.getElementById("password").value;
			var verifyCode = document.getElementById("verifyCode").value;
			if(name.trim()==""||password.trim()==""){
				 document.getElementById("loginUserCue").innerHTML ="密码不能为空！";
			}else if(name.trim()!=""&&password.trim()!=""){
				form.action="v1/login";
				form.submit();
			}
		}
		
</script>
</head>
<script type="text/javascript">
		/*
		如果一个表单项的name和<img>的id相同，那么可能会出问题！一般只有IE出问题！
		*/
		function verifyUsername(inputEle)
		{
			var username = inputEle.value;
			var reg = /^(\w{6,20})$/;
			if(!username.match(reg))
			{
				inputEle.style.borderColor="red";
			}
			else{
				inputEle.style.borderColor="green";
			}
		}
		function verifyPassword(inputEle)
		{
			var password = inputEle.value;
			var reg = /^([a-zA-Z0-9]{6,20})$/;
			if(!password.match(reg))
			{
				inputEle.style.borderColor="red";
			}
			else{
				inputEle.style.borderColor="green";
			}
		}
		function verifyEmail(inputEle)
		{
			var email = inputEle.value;
			var reg = /^(\w+@([a-zA-Z0-9])+.([a-zA-Z0-9])+)$/;
			if(!email.match(reg))
			{
				inputEle.style.borderColor="red";
			}
			else{
				inputEle.style.borderColor="green";
			}
		}
		function isSame(inputEle)
		{
			var regPsw2 = inputEle.value;
			var regPsw1 = document.getElementById("regPsw1").value;
			var reg = /^([a-zA-Z0-9]{6,20})$/;
			
			if(!regPsw2.match(reg) || regPsw2!=regPsw1)
			{
				inputEle.style.borderColor="red";
			}
			else{
				inputEle.style.borderColor="green";
			}
		}

		
</script>
<body>

<h1 style="color:white">FASTDB</h1>

<div class="login" style="margin-top:50px;">
    
    <div class="header">
        <div class="switch" id="switch"><a class="switch_btn_focus" id="switch_qlogin" href="javascript:void(0);" tabindex="7">快速登录</a>
			<a class="switch_btn" id="switch_login" href="javascript:void(0);" tabindex="8">快速注册</a><div class="switch_bottom" id="switch_bottom" style="position: absolute; width: 64px; left: 0px;"></div>
        </div>
    </div>    
  
    
    <div  id="web_qr_login" style="display: block; height: 320px;">    

            <!--登录-->
         <div class="web_login" id="web_login">
            <div class="qlogin" >   
				<form action="" name="loginform" accept-charset="utf-8" id="logUser" class="loginForm" method="post">
				<input type="hidden" name="method" value="login" class="inputstyle2"/>
				 <ul class="reg_form">
				 <div id="loginUserCue">
				 </div>
				 <br/>
					 <li>
						<!-- <font color = "red">${requestScope.errors.username}</font> -->
						<input style="width:250px;" type="text"  class="inputstyle2" placeholder="用户名(6-20字母、数字、下划线)" name="name" id="name" />
					</li>
					<li>
						<!-- <font color = "red">${requestScope.errors.password}</font> -->
						<input style="width:250px;" type="password"   placeholder="密码(6-20字母、数字)" name="password" id="password" class="inputstyle2" />
					</li>
					<li>
						<input type="text" placeholder="验证码" name="verifyCode" id="verifyCode" value="${user.verifyCode }" style="width:150px;" class="inputstyle2"/>
				       	<img id="vCode" src="<c:url value='v1/verify'/>" border="2" />
				       	<a href="javascript:change()">换一张</a>
					</li>
					<font color="red">${error0}</font><br/>
		                <div style="padding-left:50px;margin-top:20px;">
							<input type="submit" value="登 录" style="width:150px;" class="button_blue" onclick="login()"/>
						</div>
              	 </ul>
              </form>
           </div>
               
            </div>
            <!--登录end-->
  </div>

  <!--注册-->
    <div class="qlogin" id="qlogin" style="display: none; ">
   
    <div class="web_login">
    <form name="form2" id="v1/regist" accept-charset="utf-8"  action="<c:url value='v1/register'/>" method="post">
	    <input type="hidden" name="method" value="regist"/>
		<input type="hidden" name="did" value="0"/>
        	<ul class="reg_form" id="reg-ul">
        
        		<div id="regUserCue" class="cue">快速注册请注意格式</div>
                <li>
                    <input style="width:250px;" type="text" name="userName" id="userName" placeholder="用户名(6-20字母、数字、下划线)"  maxlength="20" class="inputstyle2" onkeyup="verifyUsername(this);"/><font color = "red">${requestScope.errors.username}</font>
                    
                </li>
                
                <li>
                    <input style="width:250px;" type="text" name="email" id="email" placeholder="邮箱"   class="inputstyle2" onkeyup="verifyEmail(this);"/><font color = "red">${requestScope.errors.email}</font>
                </li>
                <li>
                    <input style="width:250px;" type="password" id="password" placeholder="密码(6-20字母、数字)" name="password" maxlength="20" class="inputstyle2" onkeyup="verifyPassword(this);"/><font color = "red">${requestScope.errors.password}</font>
                </li>
                <li>
                    <input style="width:250px;" type="password" id="regPsw2" placeholder="确认密码" name="confirmPassword" maxlength="20" class="inputstyle2" onkeyup="isSame(this);"/>
                    <div class="inputArea">
                        <input type="submit" id="reg"  style="margin-top:10px;margin-left:85px;" class="button_blue" value="快速注册"/>
                    </div>
                </li><div class="cl"></div>
            </ul>
	</form>
    </div>
   
    
    </div>
    <!--注册end-->
</div>
</body></html>