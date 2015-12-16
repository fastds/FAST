<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<!--[if lt IE 7 ]><html lang="en" class="ie6 ielt7 ielt8 ielt9"><![endif]--><!--[if IE 7 ]><html lang="en" class="ie7 ielt8 ielt9"><![endif]--><!--[if IE 8 ]><html lang="en" class="ie8 ielt9"><![endif]--><!--[if IE 9 ]><html lang="en" class="ie9"> <![endif]--><!--[if (gt IE 9)|!(IE)]>--> 
<html lang="en">
	<head>
	
		<base href="<%=basePath%>"/>
		<meta charset="utf-8">
		<title>FASTDB</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
		<link href="css/site.css" rel="stylesheet">
		<link href="js/uploadify.css" rel="stylesheet" type="text/css" />
		
		 <link rel="stylesheet" href="webuploader/webuploader.css" type="text/css"></link>   
   
   		<script type="text/javascript" src="webuploader/jquery.min.js"></script>
		
		<link rel="stylesheet" href="webuploader/style.css" type="text/css"></link>
		<script type="text/javascript" src="webuploader/bootstrap.min.js"></script>
    	<script type="text/javascript" src="webuploader/webuploader.js"></script>
    	<script type="text/javascript">
	  $(function(){
		   var $ = jQuery,
        $list = $("#thelist"),
        $btn = $("#ctlBtn"),
        state = 'pending',
        uploader;
	 uploader = WebUploader.create({
		
		    // swf文件路径
		    swf:'webuploader/Uploader.swf',
		
		    // 文件接收服务端。
		    server: 'v1/uploade',
			
		
		    // 选择文件的按钮。可选。
		    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
		    pick: '#picker',
			
			//由于Http的无状态特征，在往服务器发送数据过程传递一个进入当前页面是生成的GUID作为标示
			//formData: {guid:new Date()},
		    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
		    resize: false
		});
		// 当有文件添加进来的时候
	    uploader.on( 'fileQueued', function( file ) {
	        $list.append( '<div id="' + file.id + '" class="item">' +
	            '<h4 class="info">' + file.name + '</h4>' +
	            '<p class="state">等待上传...</p>' +
	        '</div>' );
	    });
	
	    // 文件上传过程中创建进度条实时显示。
	    uploader.on( 'uploadProgress', function( file, percentage ) {
	        var $li = $( '#'+file.id ),
	            $percent = $li.find('.progress .progress-bar');
	
	        // 避免重复创建
	        if ( !$percent.length ) {
	            $percent = $('<div class="progress progress-striped active">' +
	              '<div class="progress-bar" role="progressbar" style="width: 0%">' +
	              '</div>' +
	            '</div>').appendTo( $li ).find('.progress-bar');
	        }
	
	        $li.find('p.state').text('上传中');
	
	        $percent.css( 'width', percentage * 100 + '%' );
	    });
	
	    uploader.on( 'uploadSuccess', function(file,response) {
	        $( '#'+file.id ).find('p.state').text('已上传');
	        console.info(response.data);
	    });
	
	    uploader.on( 'uploadError', function( file ) {
	        $( '#'+file.id ).find('p.state').text('上传出错');
	    });
	
	    uploader.on( 'uploadComplete', function( file) {
	        $( '#'+file.id ).find('.progress').fadeOut();        
	    });
	
	  uploader.on( 'all', function( type ) {
        if ( type === 'startUpload' ) {
            state = 'uploading';
        } else if ( type === 'stopUpload' ) {
            state = 'paused';
        } else if ( type === 'uploadFinished' ) {
            state = 'done';
        }

        if ( state === 'uploading' ) {
            $btn.text('暂停上传');
        } else {
            $btn.text('开始上传');
        }
    });

    $btn.on( 'click', function() {
            uploader.upload();
        
    });
	});
	
	
	function load(){
	var form = document.getElementById("load");
			//form.method="post";
			form.action="v1/upload";
			form.submit();
	}
  </script>
    	
	</head>
	
	<body>
		<div class="container">
			<jsp:include page="top.jsp"></jsp:include>
			
			
				<div class="span9">
					<h1>
						上传CSV文件
					</h1>
					
					<div id="uploader" class="">
    <!--用来存放文件信息-->
    <div id="thelist" class="uploader-list"></div>
    <div class="btns">
        <div id="picker">选择文件</div>
        <button id="ctlBtn" class="btn btn-default">开始上传</button>
    	</div>
	</div>
					
					
					<div class="container">
				<form method="post"  action="" enctype="multipart/form-data" id="load"> 
					
					    <table style="width:100%;">
					     
					   
					    <tr>
					    <td style="width:22%;">Array schema</td><td>
					    <input style="width:60%;" id="arraySchema" name="a_s" type="text" placeholder="Array schema" value="${requestScope.arraySchema}"/>
					    </td>
					    </tr>
					    <tr>
					    <td>Array name</td>
					    <td>
					    <input style="width:60%;" id="arrayName" name="a_n" type="text"  placeholder="Array name" value="${requestScope.arrayName}"/>
					    </td>
					    </tr>
					    <tr>
					    <td>Maximum errors</td><td>
					    <input style="width:60%;" id="errorlimit" name="a_e" type="text" value="0" placeholder="0"/>
					    </td>
					    </tr>
					    </table>
					<br/>
					<ul>
					<li><i>Edit the schema as required after selecting your CSV file.</i>
					<li><i>The following link shows a small example file (Fisher's iris data). Save the file locally first, then select it above: </i><a href="http://illposed.net/iris.csv" target="_blank">http://illposed.net/iris.csv</a>
					<li><i>Upload uses HTML 5 technology not available on all browsers (IE &lt; 10)</i>
					</ul>
					  </div>
				
					    
					  <input type="submit" value="load" style="width:150px;" class="btn btn-primary" data-dismiss="modal" onclick="load()"/>
					   
					 
					  </form>
					
					</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- <script src="js/jquery.min.js"></script> -->
		
		<script src="js/bootstrap.min.js"></script>
		<script src="js/site.js"></script>
		<script src="js/cook.js"></script>
		<script src="js/index.js"></script>
		<script src="js/spin.min.js"></script>
		<script src="js/spin.js"></script>
		<script src="js/scidb.js"></script>
	</body>
</html>
