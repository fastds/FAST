function aflupload(){
	var arrayName = document.getElementById('arrayName').value;
	 $.post("AFLUpload",{arrayName:arrayName},callback);

}
function callback(data){
	 var resultObj = $("#result");
	  resultObj.html("<table width=500>"+data+"</table>");

}
