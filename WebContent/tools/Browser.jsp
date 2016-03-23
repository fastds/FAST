<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	<style>
	  .h	{font-size:10pt;font-weight:800;background-color:#500000}
	  .v	{font-size:10pt;background-color:#000060;}
	  .o	{font-size:10pt;background-color:#003030;}
	  .d	{font-size:12pt;}
	  .t	{font-size:14pt;color:#ffffff;}
	  .dhead {font-size:10pt;}
	  .ddrop {font-size:10pt;font-weight:100;}
	  samp  {FONT-FAMILY: courier; color:#ffff88;}
	  #transp
	{
	    LEFT: 240px;
	    POSITION: absolute;
	    TOP: 155px
	}
	</style>
	<script type="text/javascript">
	  function search() {
	    var args = 'search ' + document.getElementById('key').value;
	    __doPostBack('<%= UpdatePanel1.UniqueID %>',args);
	  }
	</script>
</head>
<body>

</body>
</html>