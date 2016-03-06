package org.fastds.resources.dataresources;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.fastds.explorehelpers.ObjectInfo;
import org.fastds.model.ApogeeControl;
import org.fastds.model.CrossIDControls;
import org.fastds.model.Globals;
import org.fastds.model.ImagingControl;
import org.fastds.model.MetaDataControl;
import org.fastds.model.ObjectExplorer;
import org.fastds.model.SpectralControl;
import org.fastds.service.ExplorerService;
import org.glassfish.jersey.server.mvc.Viewable;

import edu.gzu.domain.PhotoTag;
import edu.gzu.utils.Utilities;

@Path("/explore")
public class ExplorerResource {
	@Context
	HttpServletRequest request;
	@Context
	HttpServletResponse response;
	
	private ExplorerService explorerService = new ExplorerService();
	
	protected final String ZERO_ID = "0x0000000000000000";
    public ObjectInfo objectInfo = new ObjectInfo();
    Long id ;
    String apid;
    Long specID;
    String sid = null;
    Double qra ;
    Double qdec ;
    Integer mjd ;
    Short plate ;
    Short fiber ;
    String sidstring = null;
    
    /*
     * ---------以下是页面上显示的相关动态元素的对象---------
     */
    ObjectExplorer master = null;			//导航栏链接元素的动态变量
    MetaDataControl metaDataCtrl = null;	//与meda data 相关的变量元素
    ImagingControl imagingCtrl = null;		//与图像相关联的变量元素
    CrossIDControls crossIDCtrl = null;	//与交叉认证相关的变量元素
    SpectralControl specCtrl = null;		//与光谱相关的变量元素
    ApogeeControl apogeeCtrl = null;			//与apogee（远地点、最高点、最远点）相关的变量元素
    protected Globals globals;
    
    
	@GET
	@Path("summary")
	public Viewable summary(@QueryParam("ra") String ra
						,@QueryParam("dec") String dec) {
		
		qra = Utilities.parseRA(ra); // need to parse J2000
        qdec = Utilities.parseDec(dec); // need to parse J2000
        
        //This is imp function to get all different ids.(获取所有不同的ID)
        getObjPmts();

        //parseID and store ObjectInfo in session（解析ID并且将ObjectInfo存到session中）
        parseIDs();
        request.getSession().setAttribute("objectInfo", objectInfo);
        
        //构造其他所需要的页面动态元素对象
        master = new ObjectExplorer(objectInfo);
        metaDataCtrl = new MetaDataControl(master);
        imagingCtrl = new ImagingControl(master);
        crossIDCtrl = new CrossIDControls(master);
        specCtrl = new SpectralControl(master);
        
        //将上面的对象保存到request域
        request.setAttribute("master", master);
        request.setAttribute("metaDataCtrl", metaDataCtrl);
        request.setAttribute("imagingCtrl", imagingCtrl);
        request.setAttribute("crossIDCtrl", crossIDCtrl);
        request.setAttribute("specCtrl", specCtrl);
        
        try {
			apogeeCtrl = new ApogeeControl(master);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
//		return new Viewable("/tools/summary.jsp", null); 原来的
		return new Viewable("/tools/test.jsp", null);//测试用
	}
	
	
	
	/*-----------------------------down c#--------------------------------------*/
	/*      old
	 * protected final String ZERO_ID = "0x0000000000000000";
    protected Globals globals;
    protected ObjectExplorer master;
    
    public RunQuery runQuery;
    public ObjectInfo objectInfo = new ObjectInfo();

    //protected HRefs hrefs = new HRefs();

     long id = null;
     String apid;
     long specID = null;
     String sid = null;
     double qra = null;
     double qdec = null;

    int mjd = null;
    short plate = null;
    short fiber = null;*/

   /* protected void Page_Load(Object sender, EventArgs e)
    {
        runQuery = new RunQuery();
        globals = (Globals)Application[Globals.PROPERTY_NAME];
        master = (ObjectExplorer)Page.Master;
        Session["objectInfo"] = objectInfo;

        if (Request.QueryString.Keys.Count == 0)
        {
            id = globals.ExploreDefault;
        }

        foreach (String key in Request.QueryString.Keys)
        {
            if (key == "id")
            {
                String s = Request.QueryString["id"];
                id = Utilities.ParseID(s);                   
            }
            if (key == "sid")
            {
                String s = Request.QueryString["sid"].Trim().ToUpper();
                if (s.StartsWith("2M")) sidstring = s;
                else
                sidstring = (String.Equals(s, ""))  s : Utilities.ParseID(s).ToString();                   
            }
            if (key == "spec")
            {
                String s = Request.QueryString["spec"];
                sidstring = (String.Equals(s, ""))  s : Utilities.ParseID(s).ToString();                 
            }
            if (key == "apid")
            {
                String s = HttpUtility.UrlEncode(Request.QueryString["apid"]);                    
                if (s != null & !"".Equals(s))
                {
                        apid = s;
                }                    
            }
            if (key == "ra") qra = Utilities.parseRA(Request.QueryString["ra"]); // need to parse J2000
            if (key == "dec") qdec = Utilities.parseDec(Request.QueryString["dec"]); // need to parse J2000
            if (key == "plate") plate = short.Parse(Request.QueryString["plate"]);
            if (key == "mjd") mjd = int.Parse(Request.QueryString["mjd"]);
            if (key == "fiber") fiber = short.Parse(Request.QueryString["fiber"]);
        }
       
        //This is imp function to get all different ids.
        getObjPmts();

        //parseID and store ObjectInfo in session
        parseIDs();
        
    } old */

    private void parseIDs() {
        if (objectInfo.objID != null && !objectInfo.objID.equals(""))
            objectInfo.id = Utilities.ParseId(objectInfo.objID);

        if (objectInfo.specObjID != null && !objectInfo.specObjID.equals(""))
            objectInfo.specID = Utilities.ParseId(objectInfo.specObjID);

    } 

    private void getObjPmts()
    {
    	System.out.print("fiber:"+fiber==null);
    	System.out.print(";ra,dec:"+(qra==null)+","+(qdec==null));
    	System.out.print(";specID:"+specID==null);
    	System.out.print(";sidstring:"+sidstring==null);
    	System.out.print(";id:"+id==null);
    	System.out.print(";apid:"+apid==null);
        if (fiber != null && plate != null) ObjIDFromPlfib(plate, mjd, fiber);
        else if (qra != null && qdec != null) pmtsFromEq(qra, qdec);
        else if (specID != null || (sidstring!=null && !sidstring.isEmpty())) pmtsFromSpec(sidstring);
        else if (id != null && specID != null) pmtsFromPhoto(id);
        else if (apid!=null && !apid.isEmpty()) parseApogeeID(apid);
    }

    private void ObjIDFromPlfib(short plate, int mjd, short fiber)
    {
        
        Map<String,Object> attrsOne = null;
        attrsOne = explorerService.findObjIDFromPlatefiberMjd(mjd+"",plate+"",fiber+"");
        
        if(attrsOne != null && attrsOne.size()!=0)
        {
        	objectInfo.objID = attrsOne.get("objID")== null ? null :  Utilities.longToHex((Long)attrsOne.get("objID"));
        	objectInfo.specObjID = (String)attrsOne.get("specObjID") == null? null : Utilities.longToHex((Long)attrsOne.get("specObjID"));
        	objectInfo.ra = (Double)attrsOne.get("specObjID");
        	objectInfo.dec = (Double)attrsOne.get("specObjID");
        }

        long apid = explorerService.findApid(objectInfo.ra, objectInfo.dec,(0.5 / 60));
        // if we couldn't find that plate/mjd/fiber, maybe it's an APOGEE object
        if (apid != -1)
        {
            objectInfo.apid = apid +"";
        }

    }
    
   private void apogeeFromEq(double qra, double qdec)
    {
	   double searchRadius = 0.5 / 60;
	   long apid = explorerService.findApid(qra,qdec,searchRadius);
	   objectInfo.apid = ""+apid;
    } 

    private void photoFromEq(double qra, double qdec)
    {
    	double searchRadius = 0.5 / 60;
        
        PhotoTag pt = explorerService.findPhotoFromEq(qra, qdec, searchRadius);
        
        objectInfo.objID = pt.getObjID() == 0? null : Utilities.longToHex(pt.getObjID());
        objectInfo.specObjID = pt.getSpecObjID() == 0? null : Utilities.longToHex(pt.getSpecObjID());
       
    }

    private void pmtsFromEq(double qra, double qdec)
    {
       Map<String,Long> ids = explorerService.fillObjectInfo(qra, qdec);
       if(ids!=null && ids.size()>0)
       {
    	   objectInfo.objID = ids.get("objID")+"";
           objectInfo.specObjID = ids.get("specObjID")+"";
       }
       
       if (objectInfo.objID != null && !objectInfo.objID.equals(""))
        {
            // This is required to get the primary specObjId (with sciprimary=1). PhotoTag.specObjId is not necessarily primary...
            pmtsFromPhoto(Utilities.ParseId(objectInfo.objID));
            apogeeFromEq(qra, qdec);
        }
    }


    private void pmtsFromSpec(String sid)
    {
        long sidnumber = 0;
        try
        {
            pmtsFromSpecWithApogeeID(sidstring);
            if (objectInfo.apid != null && objectInfo.apid != "")
            {
                photoFromEq(objectInfo.ra, objectInfo.dec);
            }
        }
        catch (Exception e) { }

        try
        {
            sidnumber = Long.parseLong(sidstring);
            pmtsFromSpecWithSpecobjID(sidnumber);
            if (objectInfo.specObjID != null && objectInfo.specObjID != ZERO_ID)
            {
                apogeeFromEq(objectInfo.ra, objectInfo.dec);
            }
        }
        catch (Exception e) { }
    }

    private void pmtsFromSpecWithApogeeID(String sid)
    {
        String whatdoiget = null;
        if (sid.startsWith("apogee")) { whatdoiget = "apstar_id"; } else { whatdoiget = "apogee_id"; }

        Map<String,Object> attrs = explorerService.findApogeeStar(whatdoiget,sid);
        objectInfo.apid = ((Long)attrs.get("apid")).toString();
        objectInfo.ra = (Double)attrs.get("ra");
        objectInfo.dec = (Double)attrs.get("dec");
    }
    
    private void pmtsFromSpecWithSpecobjID(long sid)
    {
        Map<String,Object> attrs = null;
        attrs = explorerService.findPmtsFromSpecWithSpecobjID(sid);
        objectInfo.ra = (Double)attrs.get("ra");
        objectInfo.dec = (Double)attrs.get("dec");
        objectInfo.fieldID = (Long)attrs.get("fieldID") == 0 ? null : Utilities.longToHex((Long)attrs.get("fieldID"));
        objectInfo.objID = (Long)attrs.get("objID") == 0 ? null : Utilities.longToHex(((Long)attrs.get("objID")));
        objectInfo.specObjID = (Long)attrs.get("specObjID") == 0 ? null : Utilities.longToHex(((Long)attrs.get("fieldID")));
        objectInfo.plateID = (Long)attrs.get("plateID") == 0 ? null : Utilities.longToHex(((Long)attrs.get("fieldID")));
        objectInfo.mjd = (Integer)attrs.get("mjd");
        objectInfo.fiberID = (Short)attrs.get("fiberID");
        objectInfo.plate = (Short)attrs.get("plate");
    } 


    private void pmtsFromPhoto(long id)
    {
       
        PhotoTag photoTag = explorerService.findPhotoTag(id);
        
        objectInfo.ra = photoTag.getRa();
        objectInfo.dec = photoTag.getDec();
        objectInfo.run = photoTag.getRun();
        objectInfo.rerun = photoTag.getRerun();
        objectInfo.camcol = photoTag.getCamcol();
        objectInfo.field = photoTag.getField();
        objectInfo.fieldID = photoTag.getFieldID() == 0 ? null : Utilities.longToHex(photoTag.getFieldID());
        objectInfo.objID = photoTag.getObjID() == 0 ? null : Utilities.longToHex(photoTag.getObjID());
        objectInfo.specObjID = photoTag.getSpecObjID() == 0 ? null : Utilities.longToHex(photoTag.getSpecObjID());
        
        // get the plateID and fiberID from the specObj, if it exists
        if (objectInfo.specObjID != null && !ZERO_ID.equals(objectInfo.specObjID))
        {
            long specID = Long.parseLong(objectInfo.specObjID.substring(2),16);
            Map<String,Object> attrs = explorerService.findAttrsFormSpecObjAllAndPlateX(specID);
            
            objectInfo.plateID = (Long)attrs.get("plateID") == 0 ? null : Utilities.longToHex((Long)attrs.get("plateID"));
            objectInfo.mjd = (Integer)attrs.get("mjd");
            objectInfo.fiberID = (Short)attrs.get("fiberID");
            objectInfo.plate = (Short)attrs.get("fiberID");
        }

        try
        {
            apogeeFromEq(objectInfo.ra, objectInfo.dec);
        }
        catch(Exception e) { }
    }

    private void parseApogeeID(String idstring)
    {
        double qra =0, qdec=0;
        objectInfo.apid = apid;
        apid = apid.toLowerCase();
        Map<String,Object> attrsOne = null; 
        if(apid.contains("apogee"))
        	attrsOne = explorerService.findApogeeByID(apid);
        else
        	attrsOne = explorerService.findApogeeByID2(apid);
        
        if (attrsOne != null && attrsOne.size()!=0)
        {
            qra = (Double)attrsOne.get("ra");
            qdec =(Double)attrsOne.get("dec");
        }
        
        Map<String,Long> attrsTwo = null; 
        attrsTwo = explorerService.fillObjectInfo(qra, qdec);
        if (attrsTwo != null && attrsTwo.size()!=0)
        {
           objectInfo.objID = attrsTwo.get("objID")==null || attrsTwo.get("objID")==0 ? null : Utilities.longToHex(attrsTwo.get("objID"));
           objectInfo.specObjID = attrsTwo.get("specObjID")==null || attrsTwo.get("specObjID")==0 ? null : Utilities.longToHex(attrsTwo.get("specObjID"));                    
        }
    }
    
}
