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
		
		<!-- codemirror to lead -->
		<link rel="stylesheet" href="codemirror.css" />
		<script src="codemirror.js"></script>
		<script src="sql.js"></script>
		<script src="sql-hint.js"></script>
</head>
	<body>
		<div class="container">
			<jsp:include page="top.jsp"></jsp:include>
			<div class="row">
				
				<div class="span12">
					
					<table align=center>
						<tr>
						<td class='tsml'>
						<b><i>Basic AQL:</i></b>
						<br><a href="SamplePage/select_from_where.jsp">Basic SELECT-FROM-WHERE</a>
						<br><a href="SamplePage/Basic_position_search.jsp">Basic position search</a>
						<br><a href="#phototag">Using PhotoTag</a>
						<br><a href="#between">Search for a Range of Values</a>
						<br><a href="#rect">Rectangular position search</a>
						<br><a href="#join">More than one table: JOIN...ON</a>
						<br><a href="#specphoto">Photometry & spectroscopy</a>
						<br><a href="#countgroup">Counting by type or category</a>
						<br><a href="#starflag">Using flags</a>
						
						<br><hr>
						
						<b><i>SQL Jujitsu:</i></b>
						<br><a href="SamplePage/Data_subsample.jsp">Data subsample</a>
						<br><a href="#closepairs">Objects in close pairs</a>
						<br><a href="#nbrrun">Selected neighbors in run</a>
						<br><a href="#objcount">Object counts and logic</a>
						<br><a href="#repeatz">Repeated high-<i>z</i> objects</a>
						<br><a href="#split64">Splitting 64-bit values</a>
						<br><a href="#outer">Using LEFT OUTER JOIN</a>
						<br><a href="#nested">Using Nested Queries</a>
						
						<br><hr>
						
						<b><i>Quasars:</i></b>
						<br><a href="SamplePage/QSOs_by_spectroscopy.jsp">QSOs by spectroscopy</a>
						<br><a href="#qsocol">QSOs by colors</a>
						<br><a href="#quasar3">FIRST matches for quasars</a>
						
						</td>
						
						<td width="5">&nbsp;</td>
						
						<td class='tsml'>
						<b><i>General Astronomy:</i></b>
						<br><a href="SamplePage/Only_stars_or_galaxies.jsp">Only stars or galaxies</a>
						<br><a href="#clean">Clean photometry</a>
						<br><a href="#mjd">Using Field MJD</a>
						<br><a href="#lines">Objects by spectral lines</a>
						<br><a href="#unspec">Spectra by classification</a>
						<br><a href="#movast">Moving asteroids</a>
						<br><a href="#repeat">Plates with repeat spectra</a>
						<br><a href="#galstar">Galaxies blended with stars</a>
						<br><a href="#countsbytype">Counts by type and program</a>
						<br><a href="#footprint">Checking SDSS footprint</a>
						
						<br><hr>
						
						<b><i>Stars:</i></b>
						<br><a href="#Clean_photometry_Stars.jsp">Clean photometry - Stars</a>
						<br><a href="#cv">CVs using colors</a>
						<br><a href="#bincol">Binary stars colors</a>
						<br><a href="#spplines">Using sppLines table</a>
						<br><a href="#sppparams">Using sppParams table</a>
						<br /><a href="#pm">Proper motions</a>
						<!-- <br><font color="red"><b>Stars by spectral class (KS)</b></font> -->
						
						<br><hr>
						
						<b><i>Miscellaneous:</i></b>
						<br><a href="SamplePage/Photometric_Redshifts.jsp">Photometric Redshifts</a>
						<br><a href="#specOther1">Spectra in Other Programs - I</a>
						<br><a href="#specOther2">Spectra in Other Programs - II</a>
						<br><a href="#wisexmatch">Using WISE Cross-Match</a>
						
						</td>
						
						<td width="5">&nbsp;</td>
						
						<td class='tsml'>
						<b><i>Galaxies:</i></b>
						<br><a href="SamplePage/Clean_photometry_Galaxies.jsp">Clean photometry - Galaxies</a>
						<br><a href="#galblue">Galaxies with blue centers</a>
						<br><a href="#diamlim">Diameter limited sample</a>
						<br><a href="#lrg">LRG sample selection</a>
						<br><a href="#htm">Galaxy counts on HTM grid</a>
						<br><a href="#zoo">Classifications from Galaxy Zoo</a>
						<br><a href="#bosstarg">BOSS target selection</a>
						<br><a href="#bossgal1">BOSS Stellar Masses</a>
						<br><a href="#bossgal2">BOSS Stellar Vel. Disps.</a>
						
						<br><hr>
						
						<b><i>Varaibility Queries:</i></b>
						<br><a href="SamplePage/Stars_multiply_measured.jsp">Stars multiply measured</a>
						<br><a href="#timeseries">Multiple Detections and Time Series</a>
						
						<br><hr>
						
						<b><i>APOGEE:</i></b>
						<br><a href="SamplePage/All_APOGEE_Plate_Visits.jsp">All APOGEE Plate Visits</a>
						<br><a href="#aspcapParams">ASPCAP Parameters and Errors</a>
						<br><a href="#apogeeStars">APOGEE Stars No BAD Flags</a>
						<br><a href="#aspcapCluster">ASPCAP Params for Cluster Mbrs</a>
						<br><a href="#apogeePMs">APOGEE Proper Motions</a>
						<br><a href="#apogeeClusterCenters">APOGEE Stars Near Cluster Ctr</a>
						<br><a href="#apogeeRVs">RVs for Individual APOGEE Visits</a>
						<br><a href="#apogeeSegue">APOGEE and SEGUE Spectra</a>
						<br><a href="#apogeeStarPhoto">SDSS photometry for APOGEE Stars</a>
						
						<br>
						</td>
						</tr>
					</table>				
					</div>
					
					
						
  						
				</div>
			
		</div>	
	</body>
</html>
