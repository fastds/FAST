<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${not empty spectralCtrl.specID }">
	<div id="spectro">
	   <h3>Optical Spectra</h3>
	    <div class="infobox">
	        <table width="100%">
	            <tr>
	                <td width="60%">
	                  <p><b> SpecObjID = ${spectralCtrl.specObjID}</b></p>                
	                </td>
	                <td width="40%">
	                    <c:set var="spectrumlink" value="${spectralCtrl.globals.dasUrl+'spectrumDetail?plateid='+spectralCtrl.plate+'&mjd=' +spectralCtrl.mjd+'fiber'+spectralCtrl.fiberid}"></c:set>
	                    <p><b>
	                      <a class='content' href="${spectrumlink }"  target='_blank'>
	                          Interactive spectrum<img src='../../images/new_window_black.png' alt=' (new window)' />
	                      </a>
	                    </b></p>
	                </td>
	            </tr>
	        </table>
	   </div>
	   <table class="content">
	        <tr>
	            <c:set var="instrumentLink" value="${spectralCtrl.globals.sdssUrlBase+'instruments' }"></c:set>
		   	      <td>             
			            <a href="../../get/SpecByID.ashx?id=${ spectralCtrl.specObjID }">
			                <img alt="" src="../../get/SpecByID.ashx?id=${ spectralCtrl.specObjID }" width="316" height="253" border="0" align="left" />
			            </a>
	              </td>
	              <td>                     
	                 <table cellpadding=2 cellspacing=2 border=0 width=300>
	                   
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>Spectrograph</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.instrument }</td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>class</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.objclass }</td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>Redshift (z)</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.redshift_z.ToString("F3") }</td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>Redshift error</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.redshift_err.ToString("F5") }</td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>Redshift flags</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.redshift_flags }</td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>survey</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.survey }</td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>programname</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.programname }</td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>primary</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.primary }</td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>Other spec</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.otherspec }</td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>sourcetype</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.sourcetype }</td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>Velocity dispersion (km/s)</span></td>
	                       <td valign='top' class='t'><fmt:formatNumber value="${spectralCtrl.veldisp}" pattern="#.##"></fmt:formatNumber></td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>veldisp_error</span></td>
	                       <td valign='top' class='t'><fmt:formatNumber value="${spectralCtrl.veldisp_err}" pattern="#.###"></fmt:formatNumber></td>
	                   </tr>
	                   <tr align='left' >
	                       <td  valign='top' class='h'><span>targeting_flags</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.targeting_flags }</td>
	                   </tr>
	                     <tr align='left' >
	                       <td  valign='top' class='h'><span>plate</span></td>
	                       <td valign='top' class='t'>${spectralCtrl.plate }</td>
	                   </tr>
	                  <tr align='left' >
	                      <td  valign='top' class='h'><span>mjd</span></td>
	                      <td valign='top' class='t'>${spectralCtrl.mjd }</td>
	                  </tr>
	                  <tr align='left' >
	                      <td  valign='top' class='h'><span>fiberid</span></td>
	                      <td valign='top' class='t'>${spectralCtrl.fiberid }</td>
	                  </tr>
	                 </table>
		   	      </td>            
	        </tr>   
	    </table> 
	</div>  <!-- end of spectro div -->

</c:if>
