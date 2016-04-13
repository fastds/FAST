function showChart(ra, dec) {
    var url = "../chartinfo.jsp?ra=" + ra + "&dec=" + dec;
    var w = window.open(url, "_top");
    w.focus();
}

function showNavi(ra, dec, scale) {
    var url = "../navi.jsp?ra=" + ra + "&dec=" + dec + "&scale=" + scale;
    var w = window.open(url, "_blank");
    w.focus();
}

function showLink(thelink) {
    document.getElementById(thelink).style.display = "inline";
}
function hideLink(thelink) {
    document.getElementById(thelink).style.display = "none";
}

function showSection(thediv) {
    document.getElementById(thediv).className += "shown ";
    document.getElementById(thediv).className =
        document.getElementById(thediv).className.replace
              (/(?:^|\s)hidden(?!\S)/g, '')
    /* code wrapped for readability - above is all one statement */
}

function hideSection(thediv) {
    document.getElementById(thediv).className += "hidden ";
    document.getElementById(thediv).className =
        document.getElementById(thediv).className.replace
            (/(?:^|\s)shown(?!\S)/g, '')
    /* code wrapped for readability - above is all one statement */
}

function morePrecise(short, long) {
    var theShortOne = document.getElementById(short);
    var theLongOne = document.getElementById(long);
    theShortOne.style.display = 'none';
    theLongOne.style.display = 'block';
}

function lessPrecise(short, long) {
    var theShortOne = document.getElementById(short);
    var theLongOne = document.getElementById(long);
    theShortOne.style.display = 'block';
    theLongOne.style.display = 'none';
}

function findOtherNames(thera, thedec) {
    //alert("Test");
    var theothernames = document.getElementById("othernames");
    var sdssnamedisplay = document.getElementById("sdssname");
    $.ajax({
        type: "GET",
        url: "./Resolver?ra=" + thera + "&dec=" + thedec,
        success: function (response) {
	    	alert(response);
            if (response.indexOf("Error:") == 0) {
                theothernames.innerHTML = "No common name found";
            }
            else {
                var s = response.split('\n');
                theothernames.innerHTML = s[0].substring(6);
               // sdssnamedisplay.innerHTML = <%= Functions.SDSSname(0.0, 0.0)%>;
            }
        },
        error: function (err,type,response) {
        	alert(err+','+type+','+response)
        	alert(err.responseText);
            alert("Error: Could not resolve coordinates.");
        }
    });
}