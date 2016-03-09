<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.fastds.explorehelpers." %>
<asp:Content ID="Content2" ContentPlaceHolderID="OEContent" runat="server">

    <div id="neighbors" class="content">
    
    <table class="content">
    <tr><td>
    <h1>Galaxy Zoo</h1>

    <p class="content">SDSS photometric data also includes morphological classifications of galaxies from the Galaxy Zoo project. More than 200,000 
        online volunteer citizen scientists. <a href="http://zoo1.galaxyzoo.org" class="content">Galaxy Zoo 1 
        <img src="../../images/offsite_black.png" alt="offiste" /></a> data consists of simple morphological type classification 
        (spiral vs. elliptical) for 893,212 galaxies. Galaxy Zoo 1 data are described in 
        <a href="http://adsabs.harvard.edu/abs/2011MNRAS.410..166L" class="content">Lintott et al. 2011 
        <img src="../../images/offsite_black.png" alt="(offsite)" /></a>, and details of the project are available 
        in <a href="http://adsabs.harvard.edu/abs/2008MNRAS.389.1179L" class="content">
        Linott et al. 2008 <img src="../../images/offsite_black.png" alt="(offsite)" /></a>.
    </p>

    <p class="content">New in SDSS Data Release 10 are more detailed classifications of internal structure of the brightest 25% of the Main Galaxy 
        Sample from <a href="http://zoo2.galaxyzoo.org" class="content">Galaxy Zoo 2 <img src="../../images/offsite_black.png" alt="(offiste)" /></a>. 
        Full details of the processes used to combine the votes (from a median of ~40 people per galaxy) and construct debiased classification 
        likelihoods are described in Willett et al. (in press at <i>MNRAS</i>).
    </p>

    <p class="content">If this object has matches in any of the Galaxy Zoo tables, the most commonly accessed columns in those tables are 
        displayed below. For full data, click on the table names. To return to this page, click "Galaxy Zoo" in the left-hand column.
    </p>

    </td></tr>
    <tr><td>
        
    <h2>Galaxy Zoo 1</h2>
        <h3><a href="${galaxyZoo.zooSpec}" class="content">zooSpec</a></h3>
		${galaxyZoo.show.showZooSpec }
		${galaxyZoo.show.showZooSpec2 }
        <h3><a href="${galaxyZoo.zooNoSpec }" class="content">zooNoSpec</a></h3>
        ${galaxyZoo.show.showZooNoSpec }

        <h3><a href="${galaxyZoo.zooConfidence }" class="content">zooConfidence</a></h3>
        ${galaxyZoo.show.showZooConfidence2 }

        <h3><a href="${galaxyZoo.zooMirrorBias }" class="content">zooMirrorBias</a></h3>
        ${galaxyZoo.show.showZooMirrorBias2 }

        <h3><a href="${galaxyZoo.zooMonochromeBias }" class="content">zooMonochromeBias</a></h3>
        ${galaxyZoo.show.showZooMonochromeBias2 }
     <!-- 
      //------------delete the release > 10 code ,please see source code
     -->

    </td></tr></table>
    </div>
</asp:Content>
    