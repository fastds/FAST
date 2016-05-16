package org.fastds.explorehelpers;

import org.fastds.model.View;

import edu.gzu.image.Functions;

public class ExplorerQueries {
	 ///Left Side panel of the Explore Page
    //photoObj
    public static String PhotoObjQuery = "SELECT * FROM PhotoObjAll WHERE objID=@objID";
     

    //photoTag
    public static String PhotoTagQuery = "SELECT * FROM PhotoTag WHERE objID=@objID";
    

    //Field
    public static String FieldQuery = "SELECT * FROM Field WHERE fieldID =@fieldID";
       

    //Frame
    public static String FrameQuery = "SELECT * FROM Frame WHERE fieldID=@fieldID";
   

    //specObjall
    public static String SpecObjQuery = "SELECT * FROM SpecObjAll WHERE specObjID=@specID";
     

    //sppLines
    public static String sppLinesQuery = "SELECT * FROM sppLines WHERE SPECOBJID=@specID";
   

    //sppParams
    public static String sppParamsQuery = "SELECT * FROM sppParams WHERE specObjID=@specID";
    

    //galspec
    public static String galSpecLineQuery = "SELECT * FROM galSpecLine WHERE specObjID=@specID";
   

    //galspecIndex
    public static String galSpecIndexQuery = "SELECT * FROM galSpecIndx WHERE specObjID=@specID";
 

    //galspecInfo
    public static String galSpecInfoQuery= "SELECT * FROM galSpecInfo WHERE specObjID=@specID";
    

    //stellarMassStarformingPort
    public static String stellarMassStarformingPortQuery = "SELECT * FROM stellarMassStarformingPort " 
                                                            +" WHERE specObjID=@specID";

    //stellarMassPassivePort
    public static String stellarMassPassivePortQuery = "SELECT * FROM stellarMassPassivePort" 
                                                        +" WHERE specObjID=@specID ";
   
    //emissionLinesPort
    public static String emissionLinesPortQuery="SELECT * FROM emissionLinesPort WHERE specObjID=@specID" ;

    //stellarMassPCAWiscBC03
    public static String stellarMassPCAWiscBC03Query ="SELECT * FROM stellarMassPCAWiscBC03 WHERE specObjID=@specID";

    //stellarMassPCAWiscM11
    public static String stellarMassPCAWiscM11Query= "SELECT * FROM stellarMassPCAWiscM11 WHERE specObjID=@specID";

    //For DR10 and above
    //stellarMassFSPSGranEarlyDust
    public static String stellarMassFSPSGranEarlyDust ="SELECT * FROM stellarMassFSPSGranEarlyDust WHERE specObjID=@specID";

    //stellarMassFSPSGranEarlyNoDust
    public static String stellarMassFSPSGranEarlyNoDust = "SELECT * FROM stellarMassFSPSGranEarlyNoDust WHERE specObjID=@specID";

    //stellarMassFSPSGranWideDust
    public static String stellarMassFSPSGranWideDust="SELECT * FROM stellarMassFSPSGranWideDust WHERE specObjID=@specID"; 
   
    //stellarMassFSPSGranWideDust
    public static String stellarMassFSPSGranWideNoDust = "SELECT * FROM stellarMassFSPSGranWideNoDust WHERE specObjID=@specID";
  
    //apogeeStar
    public static String apogeeStar = "SELECT * FROM apogeeStar WHERE apstar_id='@apid'";
        // HttpUtility.UrlEncode("'" + apogeeID + "'");

    //aspcapStar
    public static String aspcapStar = "SELECT * FROM aspcapStar WHERE apstar_id='@apid'";
        //+HttpUtility.UrlEncode("'" + apogeeID + "'");      

    //PhotoZ
    public static String PhotoZ = "SELECT * FROM Photoz WHERE objID=@objID";
            //String c1 = "SELECT * FROM Photoz2 WHERE objid=" + objid;       

    //PhotzRF
    //public static String PhotozRF= "SELECT * FROM PhotozRF WHERE objid=@objID";
            //String c2 = "SELECT * FROM Photoz2 WHERE objid=" + objid;

    //Plate
    public static String Plate = "SELECT * FROM PlateX WHERE plateID=@plateID";
        
//    public static String PlateShow = " SELECT cast(specObjID as binary(8)) as specObjID, fiberID, class as name, str(z,5,3) as z"+ 
//                           " FROM SpecObjAll WHERE plateID=@plateID order by fiberID";
            
    public static String PlateShow(String plateID)
    {
    	plateID = plateID.startsWith("0x")? Long.parseLong(plateID.substring(2),16)+"" :plateID;
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT specObjID, fiberID, class as name, z ");
    	aql.append(" FROM SpecObjAll WHERE plateID="+plateID+" ORDER BY fiberID ");
    	return aql.toString();
    }
    //AllSpectra Queries
    public static String AllSpec1(String objID)
    {
//    public  static String AllSpec1= "SELECT s.specObjID, s.plate as plate, s.mjd as MJD, s.fiberID as fiber," 
//                        +"str(t.ra,10,5) as ra, str(t.dec,10,5) as dec, str(s.ra,10,5) as specRa, str(s.dec,10,5) as specDec, s.sciencePrimary," 
//                        +"str(dbo.fDistanceArcMinEq(t.ra,t.dec,s.ra,s.dec),10,8) as distanceArcMin, s.class as class "
//                        +"FROM SpecObjAll s, photoobjall t"
//                        +"WHERE t.objid=@objID  and s.bestobjid=t.objid  order by scienceprimary desc, plate, MJD, fiber";
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT s.specObjID, s.plate, s.mjd , s.fiberID as fiber,  t.ra, t.dec, ");
    	aql.append(" s.ra as specRa, s.dec AS specDec, s.sciencePrimary , s.class ");
    	aql.append(" FROM SpecObjAll AS s JOIN (SELECT * FROM PhotoObjAll WHERE objID="+objID+") AS t");
    	aql.append(" ON s.bestObjID=t.objID ORDER BY sciencePrimary desc, plate, s.mjd, fiberID");
    	return aql.toString();
    }
    

	public static String AllSpec2(String objID)
    {
//    public static String AllSpec2  = "SELECT s.specObjID, s.plate as plate, s.mjd as MJD, s.fiberID as fiber, str(t.ra,10,5) as ra, str(t.dec,10,5) as dec," 
//                        +"str(s.ra,10,5) as specRa, str(s.dec,10,5) as specDec,  s.sciencePrimary," 
//                        +"str(dbo.fDistanceArcMinEq(t.ra,t.dec,s.ra,s.dec),10,8) as distanceArcMin, s.class as class" 
//                        +"FROM SpecObjAll s, photoobjall t" 
//                        +"WHERE t.objid=@objID  and s.fluxobjid=t.objid order by  plate, MJD, fiber," 
//                        +"scienceprimary desc, distanceArcMin asc";     
    	StringBuilder aql = new StringBuilder();
    	//确实一个排序属性
    	aql.append("SELECT s.specObjID, s.plate, s.mjd, s.fiberID as fiber, t.ra, t.dec,");
    	aql.append(" s.ra as specRa, s.dec as specDec,  s.sciencePrimary, s.class ");
    	aql.append(" FROM SpecObjAll AS s JOIN (SELECT * FROM PhotoObjAll WHERE objID="+objID+") AS t ");
    	aql.append(" ON s.fluxObjID=t.objID ORDER BY  plate, s.mjd, fiberID, ");
    	aql.append(" sciencePrimary desc ");
    	return aql.toString();
    }
    ///Matches Queries   注意函数内容
    public static String getMatches1(String objID)
    {
//    public static  String matches1 = "SELECT dbo.fIAUFromEq(p.ra,p.dec) as 'IAU name', p.objid, p.thingid, dbo.fPhotoModeN(p.mode) as mode"
//                                   +" FROM Photoobjall p WHERE p.objid=@objID";
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT p.ra,p.dec, p.objID, p.thingId, p.mode ");
    	aql.append(" FROM PhotoObjAll AS p WHERE p.objID="+objID);
    	System.out.println("ExplorerQueries.gtMatches1()->aql:"+aql.toString());
    	return aql.toString();
    }
    public static String getMatches2(String objID)
    {
//    	public static String matches2  = " SELECT t.objid, t.thingid, p.mode, dbo.fPhotoModeN(p.mode) as '(mode description)'"
//    		+"FROM thingindex t join photoobjall p on t.objid = p.objid "
//    		+"WHERE t.objid=@objID and p.mode != 1 order by p.mode";
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT t.objID, t.thingId, p.mode ");
    	aql.append(" FROM (SELECT * FROM thingIndex WHERE objID="+objID+") AS t JOIN PhotoObjAll AS p ON t.objID = p.objID ");
    	aql.append(" WHERE p.mode<>1 ORDER BY p.mode ");
    	System.out.println("ExplorerQueries.getMatches2()->aql:"+aql.toString());
    	return aql.toString();
    }
    ///Neighbors
    public static String getNeighbors1(String objID)
    {
//    public static String neighbors1 = " SELECT dbo.fIAUFromEq(p.ra,p.dec) as 'IAU name', p.objid, p.thingid FROM photoobjall p WHERE p.objid=@objID";
    	StringBuilder aql = new StringBuilder();
    	objID = objID.startsWith("0x")? Long.parseLong(objID.substring(2),16)+"" :objID;
    	aql.append(" SELECT p.ra, p.dec, p.objID, p.thingId FROM PhotoObjAll AS p WHERE p.objID="+objID);
    	return aql.toString();
    }

    public static String getNeighbors2(String objID)
    {
//    public static String neighbors2  = " SELECT n.neighborObjID AS objID,str(t.ra,10,5) AS ra, str(t.dec,10,5) AS dec, str(n.distance,5,3) AS distance_arcmin,"
//                            +"dbo.fPhotoTypeN(n.neighborType) AS type, neighborMode AS mode, dbo.fPhotoModeN(n.neighborMode) AS mode_description "
//                            +"FROM Neighbors n, PhotoObjAll AS t WHERE n.NeighborObjID=t.objID AND n.objID=@objID ORDER BY n.distance ASC ";
    	StringBuilder aql = new StringBuilder();
    	objID = objID.startsWith("0x")? Long.parseLong(objID.substring(2),16)+"" :objID;
    	aql.append(" SELECT n.NeighborObjID, t.ra, t.dec , n.distance , ");
    	aql.append(" n.neighborType, neighborMode  ");
    	aql.append(" FROM (SELECT * FROM Neighbors WHERE objID="+objID+")AS n JOIN PhotoObjAll AS t ON n.NeighborObjID=t.objID ORDER BY n.distance ASC ");
    	return aql.toString();
    }
    /// Fits Parameters Queries
            
    public static String getFitsParametersSppParams(String specID)
    {
//    	public static  String fitsParametersSppParams = " SELECT targetString as 'Targeting criteria', flag as 'SEGUE flags',spectypehammer as 'HAMMER spectral type', SPECTYPESUBCLASS as 'Spectral subclass',"
//    		+"str(elodiervfinal,7,2) as 'Radial velocity (km/s)', str(elodiervfinalerr,8,3) as 'RV error', str(teffadop,6,0) as 'Effective temp (K)'," 
//    		+"str(teffadopunc,6,1) as 'Teff error' , str(fehadop,7,2) as '[Fe/H] (dex)', str(fehadopunc,8,3) as '[Fe/H] error', str(loggadop,7,2) as 'log<sub>10</sub>(g) (dex)'," 
//    		+"str(loggadopunc,8,3) as 'log<sub>10</sub>(g) error' FROM sppParams WHERE specObjID=@specID";
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT targetString as 'Targeting criteria', flag as 'SEGUE flags',spectypehammer as 'HAMMER spectral type', SPECTYPESUBCLASS as 'Spectral subclass',");
    	aql.append("str(elodiervfinal,7,2) as 'Radial velocity (km/s)', str(elodiervfinalerr,8,3) as 'RV error', str(teffadop,6,0) as 'Effective temp (K)',");
    	aql.append("str(teffadopunc,6,1) as 'Teff error' , str(fehadop,7,2) as '[Fe/H] (dex)', str(fehadopunc,8,3) as '[Fe/H] error', str(loggadop,7,2) as 'log<sub>10</sub>(g) (dex)',");
    	aql.append("str(loggadopunc,8,3) as 'log<sub>10</sub>(g) error' FROM sppParams WHERE specObjID="+specID);
    	return aql.toString();
    }
    
    public static String getFitsParametersStellarMassStarformingPort(String specID)
    {
//    public static String fitsParametersStellarMassStarformingPort  = "  SELECT logMass as 'Best-fit log<sub>10</sub>(stellar mass)',minLogMass as '1-&sigma; min', maxLogMass as '1-&sigma; max',"
//                    +"age as 'Best-fit age (Gyr)', minAge as '1-&sigma; min Age', maxAge as '1-&sigma; max Age',"
//                    +"SFR as 'Best-fit SFR (M<sub>&#9737;</sub> / yr)', minSFR as '1-&sigma; min SFR', maxSFR as '1-&sigma; max SFR'" 
//                    +"FROM stellarMassStarformingPort WHERE specObjID=@specID";
    	StringBuilder aql = new StringBuilder();
    	aql.append("  SELECT logMass as 'Best-fit log<sub>10</sub>(stellar mass)',minLogMass as '1-&sigma; min', maxLogMass as '1-&sigma; max',");
    	aql.append("age as 'Best-fit age (Gyr)', minAge as '1-&sigma; min Age', maxAge as '1-&sigma; max Age',");
    	aql.append("SFR as 'Best-fit SFR (M<sub>&#9737;</sub> / yr)', minSFR as '1-&sigma; min SFR', maxSFR as '1-&sigma; max SFR'");
    	aql.append("FROM stellarMassStarformingPort WHERE specObjID="+specID);
    	return aql.toString();
    }
    
    public static String getFitsParameterSstellarMassPassivePort(String specID)
    {
//    public static String fitsParameterSstellarMassPassivePort = " SELECT logMass as 'Best-fit log<sub>10</sub>(stellar mass)', minLogMass as '1-&sigma; min', maxLogMass as '1-&sigma; max'"
//                     +", age as 'Best-fit age (Gyr)', minAge as '1-&sigma; min Age', maxAge as '1-&sigma; max Age', SFR as 'Best-fit SFR (M<sub>&#9737;</sub> / yr)',"
//                     +"minSFR as '1-&sigma; min SFR', maxSFR as '1-&sigma; max SFR'" 
//                     +"FROM stellarMassPassivePort WHERE specObjID=@specID";
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT logMass as 'Best-fit log<sub>10</sub>(stellar mass)', minLogMass as '1-&sigma; min', maxLogMass as '1-&sigma; max'");
    	aql.append(", age as 'Best-fit age (Gyr)', minAge as '1-&sigma; min Age', maxAge as '1-&sigma; max Age', SFR as 'Best-fit SFR (M<sub>&#9737;</sub> / yr)',");
    	aql.append("minSFR as '1-&sigma; min SFR', maxSFR as '1-&sigma; max SFR'");
    	aql.append("FROM stellarMassPassivePort WHERE specObjID="+specID);
    	return aql.toString();
    }
    public static String getFitsParametersEmissionLinesPort(String specID)
    {
//    public static String fitsParametersEmissionLinesPort  = " SELECT velstars as 'Stellar velocity (km/s)',sigmaStars as 'Stellar velocity disperson (km/s)'," 
//                                                            +"sigmaStarsErr as 'Velocity dispersion error' ,chisq as 'Chi-squared fit of template',"
//                                                            +"bpt as 'BPT classification' FROM emissionLinesPort WHERE specObjID=@specID";
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT velstars as 'Stellar velocity (km/s)',sigmaStars as 'Stellar velocity disperson (km/s)',");
    	aql.append("sigmaStarsErr as 'Velocity dispersion error' ,chisq as 'Chi-squared fit of template',");
    	aql.append("bpt as 'BPT classification' FROM emissionLinesPort WHERE specObjID="+specID);
    	return aql.toString();
    }

    public static String getFisParametersStellarMassPCAWiscBC03(String specID)
    {
//    public static  String fitsParametersStellarMassPCAWiscBC03 = " SELECT str(mstellar_median,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)',"
//                                                                +"str(mstellar_err,8,3) as 'Error', str(vdisp_median,8,2) as 'Median veldisp (km/s)', str(vdisp_err,9,3) as 'Error VelDisp'"
//                                                                +"FROM stellarMassPCAWiscBC03 WHERE specObjID=@specID";
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT str(mstellar_median,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)',");
    	aql.append("str(mstellar_err,8,3) as 'Error', str(vdisp_median,8,2) as 'Median veldisp (km/s)', str(vdisp_err,9,3) as 'Error VelDisp'");
    	aql.append("FROM stellarMassPCAWiscBC03 WHERE specObjID="+specID);
    	return aql.toString();
    }
    public static String fitsParametersstellarMassPCAWiscM11 = "SELECT str(mstellar_median,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)', str(mstellar_err,8,3) as 'Error',"
                                                               +" str(vdisp_median,8,2) as 'Median veldisp (km/s)', str(vdisp_err,9,3) as 'Error VelDisp'"
                                                                +"FROM stellarMassPCAWiscM11 WHERE specObjID=@specID";
    public static String getFitsParametersstellarMassPCAWiscM11(String specID)
    {
    	StringBuilder aql = new StringBuilder();
    	aql.append("SELECT str(mstellar_median,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)', str(mstellar_err,8,3) as 'Error',");
    	aql.append(" str(vdisp_median,8,2) as 'Median veldisp (km/s)', str(vdisp_err,9,3) as 'Error VelDisp'");
    	aql.append("FROM stellarMassPCAWiscM11 WHERE specObjID="+specID);
    	return aql.toString();
    }
    public static String getFitsParametersStellarmassFSPSGranEarlyDust(String specID)
    {
//    public static String fitsParametersStellarmassFSPSGranEarlyDust = "SELECT str(logmass,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)', str(logmass_err,8,3) as 'Error'," 
//                                                                      +"str(age,8,2) as 'Age (Gyr)', "
//                                                                      +"str(ssfr,8,2) as 'SSFR', str(metallicity,8,2) as 'metallicity'"
//                                                                     +" FROM stellarmassFSPSGranEarlyDust WHERE specObjID=@specID";
    	StringBuilder aql = new StringBuilder();
    	aql.append("SELECT str(logmass,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)', str(logmass_err,8,3) as 'Error',");
    	aql.append("str(age,8,2) as 'Age (Gyr)', ");
    	aql.append("str(ssfr,8,2) as 'SSFR', str(metallicity,8,2) as 'metallicity'");
    	aql.append(" FROM stellarmassFSPSGranEarlyDust WHERE specObjID="+specID);
    	return aql.toString();
    }
    public static String getFitsParametersStellarmassFSPSGranEarlyNoDust(String specID)
    {
//    public static String fitsParametersStellarmassFSPSGranEarlyNoDust = "SELECT str(logmass,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)'," 
//                                                                        +"str(logmass_err,8,3) as 'Error', str(age,8,2) as 'Age (Gyr)'," 
//                                                                        +"str(ssfr,8,2) as 'SSFR', str(metallicity,8,2) as 'metallicity'"
//                                                                        +"FROM stellarmassFSPSGranEarlyNoDust WHERE specObjID=@specID";
    	StringBuilder aql = new StringBuilder();
    	aql.append("SELECT str(logmass,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)',");
    	aql.append("str(logmass_err,8,3) as 'Error', str(age,8,2) as 'Age (Gyr)',");
    	aql.append("str(ssfr,8,2) as 'SSFR', str(metallicity,8,2) as 'metallicity'");
    	aql.append("FROM stellarmassFSPSGranEarlyNoDust WHERE specObjID="+specID);
    	return aql.toString();
    }
    public static String getFitsParametersStellarmassFSPSGranWideDust(String specID)
    {
//    public static String fitsParametersStellarmassFSPSGranWideDust= " SELECT str(logmass,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)', str(logmass_err,8,3) as 'Error',"
//                                                                    +"str(age,8,2) as 'Age (Gyr)'," 
//                                                                    +"str(ssfr,8,2) as 'SSFR', str(metallicity,8,2) as 'metallicity'"
//                                                                    +"FROM stellarmassFSPSGranWideDust WHERE specObjID=@specID";
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT str(logmass,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)', str(logmass_err,8,3) as 'Error',");
    	aql.append("str(age,8,2) as 'Age (Gyr)',");
    	aql.append("str(ssfr,8,2) as 'SSFR', str(metallicity,8,2) as 'metallicity'");
    	aql.append("FROM stellarmassFSPSGranWideDust WHERE specObjID="+specID);
    	return aql.toString();
    }
    public static String getFitsParametersStellarmassFSPSGranWideNoDust(String specID)
    {
//    public static  String fitsParametersStellarmassFSPSGranWideNoDust= "SELECT str(logmass,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)', str(logmass_err,8,3) as 'Error',"
//                                                                        +"str(age,8,2) as 'Age (Gyr)'," 
//                                                                        +"str(ssfr,8,2) as 'SSFR', str(metallicity,8,2) as 'metallicity'"
//                                                                        +"FROM stellarmassFSPSGranWideNoDust WHERE specObjID=@specID";
    	StringBuilder aql = new StringBuilder();
    	aql.append("SELECT str(logmass,7,2) as 'Best-fit log<sub>10</sub>(stellar mass)', str(logmass_err,8,3) as 'Error',");
    	aql.append("str(age,8,2) as 'Age (Gyr)',");
    	aql.append("str(ssfr,8,2) as 'SSFR', str(metallicity,8,2) as 'metallicity'");
    	aql.append("FROM stellarmassFSPSGranWideNoDust WHERE specObjID="+specID);
    	return aql.toString();
    }
    //GalaxyZooQueries
    public static String getZooSpec(String objID)
    {
    	StringBuilder aql = new StringBuilder();
    	aql.append("SELECT * FROM zooSpec WHERE objID="+objID);
    	return aql.toString();
    }
    
    public static String zooSpec1 = "SELECT objid,nvote as 'Votes',str(p_el_debiased,5,3) 'Elliptical proabability (debiased)',"
                         +"str(p_cs_debiased,5,3) 'Spiral probability (debiased)'"
                         +"FROM zooSpec WHERE objid=@objID";

   public static String getZooSpec2(String objID)
   {
//    public static String zooSpec2  = " SELECT str(p_cw,5,3) as 'Clockwise spiral probability', str(p_acw,5,3) as 'Anticlockwise spiral probability',"
//                        +"str(p_edge,5,3) as 'Edge-on spiral probablity', str(p_mg,5,3) as 'Merger system probability'"
//                        +"FROM zooSpec WHERE objid=@objID";                
	   StringBuilder aql = new StringBuilder();
	   aql.append(" SELECT p_cw as Clockwise_spiral_probability, p_acw as Anticlockwise_spiral_probability,");
	   aql.append(" p_edge as Edgeon_spiral_probablity, p_mg as Merger_system_probability");
	   aql.append(" FROM zooSpec WHERE objID="+objID);
	   return aql.toString();
   }
    public static  String zooNoSpec  = "SELECT  * FROM  zooNoSpec WHERE objid =@objID";
            
    public static String galaxyZoo3 = "SELECT objid,nvote,p_el,p_cs  FROM zooNoSpec WHERE objid=@objID";            
        
    public static String zooConfidence = "SELECT * FROM zooConfidence WHERE objid=@objID";            

    public static String zooConfidence2= " SELECT objid,f_unclass_clean,f_misclass_clean FROM zooConfidence WHERE objid=@objID";               
        
    public static String zooMirrorBias ="SELECT * FROM zooMirrorBias WHERE objid=@objID";

    public static String zooMirrorBias2 =" SELECT objid,nvote_mr1,nvote_mr2 FROM zooMirrorBias WHERE objid=@objID";

    public static String zooMonochromeBias = "SELECT * FROM zooMonochromeBias WHERE objid=@objID";

    public static String zooMonochromeBias2 ="SELECT objid,nvote_mon FROM zooMonochromeBias WHERE objid=@objID";                

    public static String zoo2MainSpecz = "SELECT * FROM zoo2MainSpecz WHERE dr8objid=@objID";

    public static String zoo2MainSpecz2 ="SELECT dr8objid, total_classifications, total_votes"
                                           +"FROM zoo2MainSpecz WHERE dr8objid=@objID";

    public static String zoo2MainPhotoz ="SELECT * FROM zoo2MainPhotoz WHERE dr8objid=@objID";

    public static String zoo2MainPhotoz2 = "SELECT dr8objid, total_classifications, total_votes"
                                           +"FROM zoo2MainPhotoz WHERE dr8objid=@objID";

    public static String zoo2Stripe82Normal="SELECT * FROM zoo2Stripe82Normal WHERE dr8objid=@objID";

    public static String zoo2Stripe82Normal2 = "SELECT dr8objid, total_classifications, total_votes"
                                                +"FROM zoo2Stripe82Normal WHERE dr8objid=@objID";                

    public static String zoo2Stripe82Coadd1 ="SELECT * FROM zoo2Stripe82Coadd1 WHERE dr8objid=@objID";
        
    public static String zoo2Stripe82Coadd1_2 = "SELECT dr8objid, total_classifications, total_votes"
                                               +" FROM zoo2Stripe82Coadd1 WHERE dr8objid=@objID";

    public static String zoo2Stripe82Coadd2= "SELECT * FROM zoo2Stripe82Coadd2 WHERE dr8objid=@objID";

    public static String zoo2Stripe82Coadd2_2 = "SELECT dr8objid, total_classifications, total_votes"
                                                +"FROM zoo2Stripe82Coadd2 WHERE dr8objid=@objID";
    
    //Metadata Queries
    public static String getObjParamaters(String objID)
    {
//    public static String getObjParamaters = "SELECT p.ra, p.dec, s.specObjID, p.clean, s.survey, cast(p.mode as int) as mode," 
//                                            +"dbo.fPhotoTypeN(p.type) as otype, p.mjd"
//                                            +"FROM PhotoObjAll p LEFT OUTER JOIN SpecObjAll s ON s.bestobjid=p.objid AND s.scienceprimary=1"
//                                            +"WHERE p.objID= @objID";  注意otype
    	objID = (objID!=null && objID.startsWith("0x"))? Long.parseLong(objID.substring(2),16)+"":objID;
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT p.ra, p.dec, s.specObjID, p.clean, s.survey, p.mode, ");
    	aql.append(" p.type AS otype, p.mjd ");
    	aql.append(" FROM (SELECT objID, ra, dec, clean, mode, type, mjd ");
    	aql.append(" FROM PhotoObjAll WHERE objID="+objID+") AS p JOIN SpecObjAll AS s ON s.bestObjID=p.objID AND s.sciencePrimary=1 ");
    	return aql.toString();
    }

    /* 
     * // Imaing Query
    public static  String getImagingQuery= " SELECT   "         
//        --phototag
         +"dbo.fPhotoFlagsN(pt.flags) as 'flags',pt.ra, pt.dec, pt.run, pt.rerun, pt.camcol, pt.field," 
         +"cast(pt.fieldID as binary(8)) as fieldID, cast(pt.objID as binary(8)) as objID," 
//        --PhotoObjall
         +"pa.clean,  dbo.fPhotoTypeN(pa.type) as otype, "
         +"pa.u as u, pa.g  as g, pa.r as r, pa.i as i, pa.z as z,"
         +"pa.err_u as err_u,  pa.err_g  as err_g,  pa.err_r  as err_r, pa.err_i  as err_i, pa.err_z as err_z," 
//        -- photoObj
       +" dbo.fPhotoModeN(po.mode) as mode,po.mjd as 'mjdNum',  (po.nDetect-1) as 'Other observations', po.parentID, po.nChild, str(po.extinction_r,7,2) as extinction_r,"
        +"str(po.petroRad_r,9,2)+' &plusmn; '+str(po.petroRadErr_r,10,3) as 'petrorad_r',"
//        --- photz,photozRF,zoospec 
        +"(str(phz.z,7,3)+' &plusmn; '+str(phz.zerr,8,4))as 'photoZ_KD'," 
       +" case (1*zz.spiral+10*zz.elliptical+100*zz.uncertain) when 1 then 'Spiral' when 10 then 'Elliptical' when 100 then 'Uncertain' else '-' end as 'GalaxyZoo_Morph'" 
//        --all joins
        +"FROM PhotoTag pt  "
         +"left outer join PhotoObj po on po.objid = pt.objid"
         +"left outer join Photoz phz on pt.objid=phz.objid "
         +"left outer join zooSpec zz on pt.objid=zz.objid" 
         +"left outer join field f on f.fieldID=pt.fieldID "
         +"left outer join photoobjall pa with (nolock)on  pa.objid = pt.objid" 
         +"WHERE pt.objID= @objID";  old    注意dbo.fPhotoFlagsN(pt.flags) 转换otype。。dbo.fPhotoModeN(po.mode)*/
//        po.petroRad_r  phz.zerr 连接方式等等 
    	public static String[] getImagingQuery(String objID)
         {
    		 objID = objID!=null && objID.startsWith("0x")?Long.parseLong(objID.substring(2),16)+"":objID;
        	 StringBuilder aqlOne = new StringBuilder();
        	 StringBuilder aqlTwo = new StringBuilder();
        	 StringBuilder aqlThree = new StringBuilder();
        	 StringBuilder aqlFour = new StringBuilder();
//  原来的      	 aql.append(" SELECT  ");
//        	 aql.append(" pt.flags,pt.ra, pt.dec, pt.run, pt.rerun, pt.camcol, pt.field,");
//        	 aql.append(" pt.fieldID, pt.objID, ");
//        	 aql.append(" pa.clean, pa.type AS otype, ");
//        	 aql.append(" pa.u , pa.g , pa.r , pa.i , pa.z , pa.err_u ,  pa.err_g ,  pa.err_r , pa.err_i , pa.err_z , ");
//        	 aql.append(" po.mode , po.mjd AS mjdNum,  (po.nDetect-1) AS Other_observations, po.parentID, po.nChild, po.extinction_r,");
//        	 aql.append(" po.petroRad_r, po.petroRadErr_r , phz.z, phz.zErr, ");
//        	 aql.append(" (1*zz.spiral+10*zz.elliptical+100*zz.uncertain) AS GalaxyZoo_Morph ");
//        	 aql.append(" FROM ("+View.getPhotoTag()+") AS pt, ("+View.getPhotoObj()+") AS po,  Photoz AS phz, zooSpec AS zz, Field AS f, PhotoObjAll AS pa ");
//        	 aql.append(" WHERE  po.objID = pt.objID AND pt.objID=phz.objID AND  pt.objID=zz.objID AND  f.fieldID=pt.fieldID AND pa.objID = pt.objID AND pt.objID="+objID);
        	 aqlOne.append(" SELECT   flags,ra, dec, run, rerun, camcol, field, fieldID, objID, clean, type AS otype,  u , g , r , i , z , err_u ,  err_g ,  err_r , err_i , err_z ");
        	 aqlOne.append(" FROM PhotoObjAll WHERE objID="+objID);
        	 
        	 aqlTwo.append(" SELECT mode , mjd AS mjdNum,  (nDetect-1) AS Other_observations, parentID, nChild, extinction_r, petroRad_r, petroRadErr_r ");
        	 aqlTwo.append(" FROM PhotoObjAll WHERE (mode=1 OR mode=2) AND objID="+objID);
        	 
        	 aqlThree.append(" SELECT z,zErr FROM Photoz WHERE objID="+objID);
        	 
        	 aqlFour.append(" SELECT (1*zz.spiral+10*zz.elliptical+100*zz.uncertain) AS GalaxyZoo_Morph  FROM zooSpec AS zz WHERE objID="+objID);
        	 return new String[]{aqlOne.toString(), aqlTwo.toString(), aqlThree.toString(), aqlFour.toString()};
         }

    /// Spectral parameters
    /*public static String getSpectroQuery=
            " SELECT s.plate,s.mjd,fiberid ,s.instrument ,class as 'objclass', z as 'redshift_z', zerr as 'redshift_err' "
    	+" , dbo.fSpecZWarningN(zWarning) as 'redshift_flags',s.survey, s.programname, s.scienceprimary as 'primary'," 
    	+" (x.nspec-1) as 'otherspec',s.sourcetype, velDisp as 'veldisp', velDispErr as 'veldisp_err' "
    	+" ,case s.survey "
    	+"  WHEN 'sdss' THEN (SELECT(dbo.fPrimtargetN(s.legacy_target1)+' '+dbo.fPrimTargetN(s.legacy_target2)+' '+dbo.fSpecialTarget1N(s.special_target1)))"
    	+"  WHEN 'boss' THEN (SELECT str(boss_target1)+','+str(ancillary_target1)+','+str(ancillary_target2))"
    	+"  WHEN 'segue1' THEN (SELECT dbo.fSEGUE1target1N(segue1_target1)+','+dbo.fSEGUE1target2N(segue1_target2))" 
    	+" WHEN 'segue2' THEN (SELECT dbo.fSEGUE2target1N(segue2_target1)+','+ dbo.fSEGUE2target2N(segue2_target2) )"             
    	+" ELSE ' No Data ' "
    	+" END "
    	+"  as 'targeting_flags' "
    	+"  FROM  PlateX p ,SpecObjAll s "
    	+"  join (SELECT bestobjid, count(*) as nspec FROM SpecObjAll WHERE bestobjid=@objID"
    	+" group by bestobjid) x on s.bestobjid=x.bestobjid  WHERE p.plateID=s.plateID and  s.specObjID=@specID";  old  */
            //iQuery += " --WHEN 'apogee' THEN (SELECT apogee_target1,apogee_target2 ) ";
         public static String getSpectroQuery(String objID, String specID) {
        	 objID = objID!=null && objID.startsWith("0x")?Long.parseLong(objID.substring(2),16)+"":objID;
        	 StringBuilder aql = new StringBuilder();
        	 aql.append("SELECT s.plate,s.mjd,fiberID ,s.instrument ,class AS objClass, z AS redshift_z, zErr AS redshift_err ");
        	 aql.append(" , zWarning AS redshift_flags,s.survey, s.programname, s.sciencePrimary AS primary,");
        	 aql.append(" (x.nspec-1) AS otherspec,s.sourceType, velDisp AS veldisp, velDispErr AS veldisp_err ");
        	 aql.append(" ,s.survey, s.legacy_target1 ,s.legacy_target2 ,s.special_target1 ");
        	 aql.append(" ,boss_target1 , ancillary_target1 , ancillary_target2, segue1_target1, segue1_target2, segue2_target1, segue2_target2 ");
        	 aql.append(" FROM PlateX AS p JOIN ");
        	 aql.append(" (SELECT * FROM (SELECT * FROM SpecObjAll WHERE specObjID="+specID+") AS s ");
        	 aql.append(" JOIN (SELECT min(bestObjID) AS bestObjID, count(*) AS nspec FROM SpecObjAll WHERE bestObjID="+objID+") AS x ");
        	 aql.append(" ON s.bestObjID=x.bestObjID) ON p.plateID=s.plateID");
        	 
     		return aql.toString();
     	}
         
   // cross_id    
    public static String USNO(String objID)
    {
    	/*public static String USNO = " SELECT 'USNO' as Catalog, str(10*propermotion,6,2)+' &plusmn; '+str(sqrt(power(muraerr,2)+power(mudecerr,2)),8,3) as 'Proper motion (mas/yr)',"
    	+" angle as 'PM angle (deg E)' FROM USNO WHERE objID=@objID";*/
    	objID = objID!=null && objID.startsWith("0x")?Long.parseLong(objID.substring(2),16)+"":objID;
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT 'USNO' AS Catalog," );
    	aql.append(" 10*PROPERMOTION, sqrt(pow(MURAERR,2)+pow(MUDECERR,2)) , ");
    	aql.append(" ANGLE AS PM_angle_deg_E FROM USNO WHERE OBJID="+objID);
    	return aql.toString();
    }
    public static String FIRST(String objID) {
    	StringBuilder aql = new StringBuilder();
    	objID = (objID!=null && objID.startsWith("0x"))? Long.parseLong(objID.substring(2),16)+"":objID;
//    	aql.append(" SELECT 'FIRST' as Catalog, ");
//    	aql.append(" str(peak,8,2)+' &plusmn; '+str(rms,8,2) as 'Peak flux (mJy)', ");
//    	aql.append(" major as 'Major axis (arcsec)', ");
//    	aql.append(" minor as 'Minor axis (arcsec)' ");
    	aql.append(" SELECT 'FIRST' AS Catalog, ");
    	aql.append(" peak, rms , ");
    	aql.append(" major AS Major_axis_arcsec, ");
    	aql.append(" minor AS Minor_axis_arcsec ");
    	aql.append(" FROM FIRST WHERE objID="+objID);
		return aql.toString();
	}

    public static String ROSAT(String objID) {
    	objID = objID!=null && objID.startsWith("0x")?Long.parseLong(objID.substring(2),16)+"":objID;
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT 'ROSAT' AS Catalog, CPS, HR1, HR2, EXT FROM ROSAT WHERE OBJID="+objID);
		return aql.toString();
	}
//    public static String RC3 = " SELECT 'RC3' as Catalog, hubble as 'Hubble type', str(m21,5,2)+' &plusmn; '+str(m21err,6,3) as '21 cm magnitude'," 
//    	+" hi as 'Neutral Hydrogen Index' FROM RC3 WHERE objID=@objID";
    public static String RC3(String objID) {
    	objID = objID!=null && objID.startsWith("0x")?Long.parseLong(objID.substring(2),16)+"":objID;
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT 'RC3' AS Catalog, HUBBLE AS Hubble_type,");
    	aql.append(" M21, M21ERR, HI AS Neutral_Hydrogen_Index ");
    	aql.append(" FROM RC3 WHERE objID="+objID);
		return aql.toString();
	}
    /*public static String WISE = " SELECT 'WISE' as Catalog,w.w1mag,w.w2mag,w.w3mag,w.w4mag,'link' as 'Full WISE data'" 
    	+"  FROM WISE_xmatch x join WISE_allsky w on x.wise_cntr=w.cntr WHERE x.sdss_objid=@objID"; old */
                   //cmd = cmd.Replace("@wiselink", wiseLinkCrossID);
    public static String WISE(String id)
    {
    	id = id !=null && id.startsWith("0x")?Long.parseLong(id.substring(2),16)+"":id;
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT 'WISE' AS Catalog,w.w1mag,w.w2mag,w.w3mag,w.w4mag,'link' AS Full_WISE_data ");
    	aql.append(" FROM WISE_xmatch AS x JOIN WISE_allsky AS w ON x.wise_cntr=w.cntr WHERE x.sdss_objid="+id);
    	return aql.toString();
    }
           
    
    public static String TWOMASS(String objID) {
    	StringBuilder aql = new StringBuilder();
    	objID = objID !=null && objID.startsWith("0x")?Long.parseLong(objID.substring(2),16)+"":objID;
    	aql.append(" SELECT '2MASS' AS Catalog, j AS J, h AS H, k AS K_s, phQual FROM TwoMass WHERE objID="+objID);
		return aql.toString();
	}
   

    // Summary.jsp
    /**
     * @return AQL语句，该语句对满足给定条件并对objID、specObjID、ra、dec进行查询
     */
    public static String getObjIDFromPlatefiberMjd(String mjd, String plate,String fiber)
    {
//    public static String getObjIDFromPlatefiberMjd= " SELECT cast(p.objID as binary(8)) as objID,cast(s.specObjID as binary(8)) as specObjID"
//    	+"  ,p.ra,p.dec"
//    	+"  FROM SpecObjAll s JOIN PhotoTag p ON s.bestobjid=p.objid JOIN PlateX q ON s.plateID=q.plateID"
//    	+"  WHERE s.mjd = @mjd and s.fiberID = @fiberID  and q.plate = @plate";
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT p.objID ,s.specObjID ,p.ra,p.dec");
    	aql.append(" FROM (SELECT specObjID,bestObjID");
    	aql.append(" FROM SpecObjAll WHERE mjd="+mjd+" AND fiberID="+fiber+") AS s ");
    	aql.append(" JOIN ("+View.getPhotoTag()+") AS p ON s.bestObjID=p.objID ");
    	aql.append(" JOIN (SELECT plateID FROM PlateX WHERE plate="+plate+") AS q ON s.plateID=q.plateID");
    	return aql.toString();
    }

   
    public static String getPhotoFromEq(double qra, double qdec,
			double searchRadius) {
//    public static String getPhotoFromEq = " SELECT top 1 cast(p.objID as binary(8)) as objID, cast(p.specObjID as binary(8)) as specObjID"
//    	+"  FROM PhotoTag p, dbo.fGetNearbyObjAllEq(@qra , @qdec , @searchRadius) n "
//    	+"  WHERE p.objID=n.objID order by n.mode asc, n.distance asc";
    	StringBuilder aql = new StringBuilder();
    	String subselect = Functions.fGetNearbyObjAllEq(qra, qdec, searchRadius);
    	aql = aql.append(" SELECT p.objID, p.specObjID ");
    	aql = aql.append(" FROM ("+View.getPhotoTag()+") p JOIN ("+subselect+") AS n ");
    	aql = aql.append(" ON p.objID=n.objID order by n.mode asc, n.distance asc");
    	
		return aql.toString();
	}

    
    /**
     * 
     * @param ra 赤经
     * @param dec 赤纬
     * @param searchRadius 搜索半径
     * @return 对给定坐标的搜索半径范围内的天体的查询AQL语句
     */
    public static String getPmtsFromEq(double ra,double dec,double searchRadius)
    {
//    public static String getpmtsFromEq = " SELECT top 1 cast(p.objID as binary(8)) as objID, cast(p.specObjID as binary(8)) as specObjID" 
//    	+"FROM PhotoTag p, dbo.fGetNearbyObjAllEq(@qra , @qdec , @searchRadius) n"
//    	+" WHERE p.objID=n.objID order by n.mode asc, n.distance asc";
    	StringBuilder aql = new StringBuilder();
    	String subselect = Functions.fGetNearbyObjAllEq(ra, dec, searchRadius);
    	String photoTag = View.getPhotoTag();
    	/*
    	 * 注意：存在类型转换
    	 * 返回 top 1 
    	 */
    	aql = aql.append(" SELECT p.objID, p.specObjID");
    	aql = aql.append(" FROM ("+photoTag+") AS p JOIN ("+subselect.toString()+") AS n ");
    	aql = aql.append(" ON p.objID=n.objID ORDER BY n.mode,n.distance");
    	
    	return aql.toString();
    }

    public static String getPmtsFromSpecWithApogeeId(String whatdoiget,
			String sid) {
    	StringBuilder aql = new StringBuilder();
    	aql = aql.append("SELECT st.apstar_id, st.ra, st.dec");
    	aql = aql.append(" FROM apogeeStar AS st");
    	aql = aql.append(" WHERE st."+whatdoiget+"="+sid);
		return aql.toString();
	}    

      public static String getPmtsFromSpecWithSpecobjID(long sid)
      {
//    	public static String getpmtsFromSpecWithSpecobjID= " SELECT p.ra, p.dec,"
//    	+" cast(p.fieldID as binary(8)) as fieldID,"
//    	+" cast(s.specObjID as binary(8)) as specObjID,"
//    	+" cast(p.objID as binary(8)) as objID,"
//    	+" cast(s.plateID as binary(8)) as plateID, s.mjd, s.fiberID, q.plate"
//    	+" FROM SpecObjAll s JOIN PhotoTag p ON s.bestobjID=p.objid JOIN PlateX q ON s.plateID=q.plateID"
//    	+" WHERE s.specObjID= @sid";
    	  
    	  StringBuilder aql = new StringBuilder();
    	  aql.append(" SELECT p.ra, p.dec,p.fieldID,s.specObjID ,p.objID,");
    	  aql.append(" s.plateID , s.mjd, s.fiberID, q.plate ");
    	  aql.append(" FROM (SELECT plateID, mjd, fiberID, specObjID, bestObjID, plateID ");
    	  aql.append(" FROM SpecObjAll WHERE specObjID="+sid+") AS s JOIN ("+View.getPhotoTag()+") AS p ON s.bestobjID=p.objID JOIN PlateX AS q ON s.plateID=q.plateID");
    	  return aql.toString();
      }

      /**
       * public static String getpmtsFromPhoto = " SELECT p.ra, p.dec, p.run, p.rerun, p.camcol, p.field,"
    	+" cast(p.fieldID as binary(8)) as fieldID,"
    	+" cast(s.specobjid as binary(8)) as specObjID,"
    	+" cast(p.objID as binary(8)) as objID "
    	+"  FROM PhotoTag p "
    	+"left outer join SpecObjAll s ON s.bestobjid=p.objid AND s.scienceprimary=1"
    	+" WHERE p.objID=dbo.fObjID(@objid)"; 
    	
    	左外连接变成內連接，以后修改
       * 这个语句中存在左外连接，由于SciDB本身的局限性，分为两个步骤来实现
       * 做外连接
       */
    public static String[] getPmtsFromPhoto(long objID)
    {
    	StringBuilder aqlFromPhotoObjAll = new StringBuilder();
    	StringBuilder aqlFromSpecObjAll = new StringBuilder();
    	long id = Functions.fObjID(objID);
    	/*
    	 *第一步：先从PhotoObjAll中选择出符合要求的属性
    	 */
    	aqlFromPhotoObjAll.append(" SELECT p.ra, p.dec, p.run, p.rerun, p.camcol, p.field, ");
    	aqlFromPhotoObjAll.append(" p.fieldID, p.objID ");
//    	aqlFromPhotoObjAll.append(" FROM ("+photoTag+") AS p ");old
    	aqlFromPhotoObjAll.append(" FROM PhotoObjAll AS p ");
    	aqlFromPhotoObjAll.append(" WHERE p.objID="+id);
    	/*
    	 * 第二步：从SpecObjAll中选择出符合要求的属性
    	 */
    	aqlFromSpecObjAll.append(" SELECT specObjID ");
    	aqlFromSpecObjAll.append(" FROM SpecObjAll ");
    	aqlFromSpecObjAll.append(" WHERE sciencePrimary=1 AND bestObjID="+id);
    	/*
    	 * 第三步：将这两个语句都返回给上一层，由上一层来进行结果拼接
    	 * [0]:aqlFromPhotoObjAll
    	 * [1]:aqlFromSpecObjAll
    	 */
    	return new String[]{aqlFromPhotoObjAll.toString(),aqlFromSpecObjAll.toString()};
    }

    public static String getPlateFiberFromSpecObj(long specObjID)
    {
    	/*
    	public static String getPlateFiberFromSpecObj = "SELECT cast(s.plateID as binary(8)) as plateID, s.mjd, s.fiberID, q.plate" 
    	+"FROM SpecObjAll s JOIN PlateX q ON s.plateID=q.plateID "
    	+"WHERE specObjID=@specID"  ;
         old */
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT s.plateID , s.mjd, s.fiberID, q.plate");
    	aql.append(" FROM (SELECT plateID, mjd, fiberID,specObjID FROM SpecObjAll WHERE specObjID="+specObjID+") AS s JOIN ");
    	aql.append("(SELECT plate,plateID FROM PlateX) AS q");
    	aql.append(" ON s.plateID=q.plateID ");
    	
    	return aql.toString();
    }
    public static String getParseApogeeID= "SELECT apstar_id FROM apogeeStar WHERE apstar_id= @id";
       

// old   public static String getUnit= "SELECT unit FROM DBcolumns WHERE tablename='@tablename' and [name]='@name'";
    public static String getUnit(String tablename, String columname) {
    	StringBuilder aql = new StringBuilder();
    	aql = aql.append("SELECT unit FROM DBcolumns ");
    	aql = aql.append(" WHERE tablename='"+tablename+"' and name='"+columname+"'");
		return aql.toString();
	}

    public static String getSpec= "SELECT specObjID,survey FROM specObjAll WHERE specObjID= @specID";
       
    
    
           

    public static String fitsimg = "SELECT"
    	+" dbo.fGetUrlFitsCFrame(@fieldID,'u'),"
    	+"  dbo.fGetUrlFitsCFrame(@fieldID,'g'),"
    	+"  dbo.fGetUrlFitsCFrame(@fieldID,'r'),"
    	+"  dbo.fGetUrlFitsCFrame(@fieldID,'i'),"
    	+" dbo.fGetUrlFitsCFrame(@fieldID,'z'),"
    	+" dbo.fGetUrlFitsBin(@fieldID,'u'),"
    	+" dbo.fGetUrlFitsBin(@fieldID,'g'),"
    	+"  dbo.fGetUrlFitsBin(@fieldID,'r'),"
    	+" dbo.fGetUrlFitsBin(@fieldID,'i'),"
    	+" dbo.fGetUrlFitsBin(@fieldID,'z'),"
    	+" dbo.fGetUrlFitsMask(@fieldID,'u'),"
    	+"  dbo.fGetUrlFitsMask(@fieldID,'g'),"
    	+"  dbo.fGetUrlFitsMask(@fieldID,'r'),"
    	+"  dbo.fGetUrlFitsMask(@fieldID,'i'),"
    	+"   dbo.fGetUrlFitsMask(@fieldID,'z'),"
    	+"   dbo.fGetUrlFitsAtlas(@fieldID),"
    	+"   dbo.fGetUrlFitsField(@fieldID)";
    public static String[] getFitsimg(String fieldID)
    {
    	fieldID = fieldID.startsWith("0x")? Long.parseLong(fieldID.substring(2), 16)+"":fieldID;
    	String[] results = new String[17];
    	results[0] = Functions.fGetUrlFitsCFrame(fieldID,"u");
    	results[1] = Functions.fGetUrlFitsCFrame(fieldID,"g");
    	results[2] = Functions.fGetUrlFitsCFrame(fieldID,"r");
    	results[3] = Functions.fGetUrlFitsCFrame(fieldID,"i");
    	results[4] = Functions.fGetUrlFitsCFrame(fieldID,"z");
    	results[5] = Functions.fGetUrlFitsBin(Long.parseLong(fieldID),"u");
    	results[6] = Functions.fGetUrlFitsBin(Long.parseLong(fieldID),"g");
    	results[7] = Functions.fGetUrlFitsBin(Long.parseLong(fieldID),"r");
    	results[8] = Functions.fGetUrlFitsBin(Long.parseLong(fieldID),"i");
    	results[9] = Functions.fGetUrlFitsBin(Long.parseLong(fieldID),"z");
    	results[10] = Functions.fGetUrlFitsMask(Long.parseLong(fieldID),"u");
    	results[11] = Functions.fGetUrlFitsMask(Long.parseLong(fieldID),"g");
    	results[12] = Functions.fGetUrlFitsMask(Long.parseLong(fieldID),"r");
    	results[13] = Functions.fGetUrlFitsMask(Long.parseLong(fieldID),"i");
    	results[14] = Functions.fGetUrlFitsMask(Long.parseLong(fieldID),"z");
    	results[15] = Functions.fGetUrlFitsAtlas(Long.parseLong(fieldID));
    	results[16] = Functions.fGetUrlFitsField(Long.parseLong(fieldID));
    	return results;
    }


   /* public static String unitQuery = "SELECT name, unit,tablename FROM DBcolumns"
    	+"   WHERE tablename in('PhotoObjAll') "
    	+"    AND name IN ('u', 'g', 'r', 'i', 'z', 'err_u','err_g','err_r','err_i','err_z',"
    	+"   'mjd', 'mode', 'nDetect', 'parentID','nChild','extinction_r','petroRad_r')"
    	+"    ORDER BY name"; old*/
    public static String unitQuery()
    {
    	StringBuilder aql =new StringBuilder();
    	aql = aql.append(" SELECT name, unit,tablename FROM DBColumns ");
    	aql = aql.append(" WHERE tablename='PhotoObjAll' ");
    	aql = aql.append(" AND (name='u' OR name='g' OR name='r' OR name='i' OR name='z' OR name='err_u' OR name='err_g' OR name='err_r' OR name='err_i' OR name='err_z' ");
    	aql = aql.append(" OR name='mjd' OR name='mode' OR name='nDetect' OR name='parentID' OR name='nChild' OR name='extinction_r' OR name='petroRad_r')");
    	aql = aql.append(" ORDER BY name");
    	return aql.toString();
    }

    //--------------------***** DR9不支持以下查询  *****--------------------------
    public static String wiseLinkCrossID(String objID) {
    	StringBuilder aql = new StringBuilder();
    	objID = objID !=null && objID.startsWith("0x")?Long.parseLong(objID.substring(2),16)+"":objID;
    	aql.append("SELECT * FROM wise_xmatch AS x join wise_allsky AS a on x.wise_cntr=a.cntr ");
    	aql.append(" WHERE x.sdss_objid="+objID);
    	
		return aql.toString();
	}

    // Apogee_Queries
    
//    public static String APOGEE_BASE_QUERY= " SELECT   a.ra,    a.dec,   a.apstar_id,    a.apogee_id,    a.glon,    a.glat,    a.location_id,   a.commiss,   a.vhelio_avg,    a.vscatter,     b.teff,"
//    	+" b.teff_err,   b.logg,    b.logg_err,  b.param_m_h,    b.param_m_h_err,     b.param_alpha_m,    b.param_alpha_m_err, c.j,   c.h,   c.k,   c.j_err,   c.h_err,   c.k_err," 
//    	+" case c.src_4_5      when 'none' then NULL      when 'WISE' then c.wise_4_5      when 'IRAC' then c.irac_4_5      end      as mag_4_5,   case c.src_4_5"     
//    	+"  when 'none' then NULL      when 'WISE' then c.wise_4_5_err      when 'IRAC' then c.irac_4_5_err      end      as mag_4_5_err,   c.src_4_5,"  
//    	+" dbo.fApogeeTarget1N(a.apogee_target1) as apogeeTarget1N,   dbo.fApogeeTarget2N(a.apogee_target2) as apogeeTarget2N," 
//    	+" dbo.fApogeeStarFlagN(a.starflag) as apogeeStarFlagN,   dbo.fApogeeAspcapFlagN(aspcapflag) as apogeeAspcapFlagN  "
//    	+" FROM apogeeStar a join aspcapStar b on a.apstar_id = b.apstar_id join apogeeObject c on a.apogee_id = c.apogee_id ";                   
    public static String getApogeeBaseQuery()
    {
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT a.ra, a.dec, a.apstar_id, a.apogee_id, a.glon, a.glat, a.location_id, a.commiss, a.vhelio_avg, a.vscatter, b.teff, ");
    	aql.append(" b.teff_err,   b.logg,    b.logg_err,  b.param_m_h,    b.param_m_h_err,     b.param_alpha_m, b.param_alpha_m_err, c.j, c.h, c.k, c.j_err, c.h_err,   c.k_err,");
    	aql.append(" case c.src_4_5      when 'none' then NULL      when 'WISE' then c.wise_4_5      when 'IRAC' then c.irac_4_5      end      as mag_4_5,   case c.src_4_5");
    	aql.append(" when 'none' then NULL      when 'WISE' then c.wise_4_5_err      when 'IRAC' then c.irac_4_5_err      end      as mag_4_5_err,   c.src_4_5,");
    	aql.append(" dbo.fApogeeTarget1N(a.apogee_target1) as apogeeTarget1N,   dbo.fApogeeTarget2N(a.apogee_target2) as apogeeTarget2N,");
    	aql.append(" dbo.fApogeeStarFlagN(a.starflag) as apogeeStarFlagN,   dbo.fApogeeAspcapFlagN(aspcapflag) as apogeeAspcapFlagN  ");
    	aql.append(" FROM apogeeStar a join aspcapStar b on a.apstar_id = b.apstar_id join apogeeObject c on a.apogee_id = c.apogee_id ");
    	return aql.toString();
    }

    public static String apogeevisitsBaseQuery(String id)
    {
    	StringBuilder aql = new StringBuilder();
    	aql.append(" SELECT visit_id, plate,  mjd, fiberid, dateobs, vrel");
    	aql.append(" FROM apogeeVisit ");
    	aql.append(" WHERE apogee_id='"+id+"' ORDER BY dateobs");
    	return aql.toString();
    }
    public static String getAPOGEEID_PlateFiberMjd = " SELECT s.apstar_id"
    	+"FROM apogeeVisit v JOIN apogeeStar s ON s.apogee_id=v.apogee_id"
    	+" WHERE v.plate = @plate  and v.mjd = @mjd  and v.fiberID = @fiberID";
    /**
     * DR9不支持该查询。
     * @param qra 赤经
     * @param qdec 赤纬
     * @param searchRadius 搜索半径
     * @return
    */
    public static String getApogeeFromEq(double qra, double qdec, double searchRadius)
    {
    	StringBuilder aql = new StringBuilder();
    	String subselect = Functions.fGetNearestApogeeStarEq(qra, qdec, searchRadius);
    	aql.append(" SELECT top 1 p.apstar_id");
    	aql.append(" FROM apogeeStar AS p, ("+subselect+") AS n");
    	aql.append(" WHERE p.apstar_id=n.apstar_id");
    	return aql.toString();
    } 
    public static String getApogee(String apid) {
//      public static String getApogee = " SELECT apstar_id, ra, dec, apogee_id, glon, glat,location_id,commiss"
//      	+"   FROM apogeeStar WHERE apstar_id='@apogeeID'";
      	StringBuilder aql = new StringBuilder();
      	aql.append(" SELECT apstar_id, ra, dec, apogee_id, glon, glat,location_id,commiss");
      	aql.append("   FROM apogeeStar WHERE apstar_id='"+apid+"'");
  		return aql.toString();
  	}
//      public static String getApogee2 = " SELECT apstar_id, ra, dec, apogee_id, glon, glat,location_id,commiss"
//      	+" FROM apogeeStar WHERE apogee_id='@apogeeID'";
      public static String getApogee2(String apid)
      {
      	StringBuilder aql = new StringBuilder();
      	aql.append(" SELECT apstar_id, ra, dec, apogee_id, glon, glat,location_id,commiss");
      	aql.append(" FROM apogeeStar WHERE apogee_id='"+apid+"'");
  		return aql.toString();
      }

      public static String getPlateFromApogee = "SELECT  top 1   a.ra,    a.dec,   a.apstar_id,    a.apogee_id, v.plate,v.fiberID,v.mjd" 
      	+" FROM apogeeStar a join apogeeVisit v on a.apogee_id=v.apogee_id "
      	+"  WHERE a.apstar_id = '@apogeeID'";

	


}
