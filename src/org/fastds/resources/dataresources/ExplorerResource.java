package org.fastds.resources.dataresources;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.fastds.explorehelpers.ObjectInfo;
import org.fastds.model.AllSpec;
import org.fastds.model.ApogeeControl;
import org.fastds.model.CrossIDControls;
import org.fastds.model.DisplayResults;
import org.fastds.model.FitsImg;
import org.fastds.model.FitsSpec;
import org.fastds.model.GalaxyZoo;
import org.fastds.model.Globals;
import org.fastds.model.ImagingControl;
import org.fastds.model.Matches;
import org.fastds.model.MetaDataControl;
import org.fastds.model.Neighbors;
import org.fastds.model.ObjectExplorer;
import org.fastds.model.Parameters;
import org.fastds.model.Plate;
import org.fastds.model.SpectralControl;
import org.fastds.service.ExplorerService;
import org.glassfish.jersey.server.mvc.Viewable;

import edu.gzu.domain.PhotoTag;
import edu.gzu.utils.Resolver;
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
    Long id = null;
    String apid = null;
    Long specID = null;
    String sid = null;
    Double qra = null;
    Double qdec = null;
    Integer mjd = null;
    Short plate = null;
    Short fiber = null;
    String sidstring = null;
    ObjectExplorer master = null;			//导航栏链接元素的动态变量
    
    /*
     * ---------以下是页面上显示的相关动态元素的对象---------
     */
    MetaDataControl metaDataCtrl = null;	//与meda data 相关的变量元素
    ImagingControl imagingCtrl = null;		//与图像相关联的变量元素
    CrossIDControls crossIDCtrl = null;		//与交叉认证相关的变量元素
    SpectralControl specCtrl = null;		//与光谱相关的变量元素
    ApogeeControl apogeeCtrl = null;		//与apogee（远地点、最高点、最远点）相关的变量元素
    protected Globals globals;
    
	@GET
	@Path("Summary")
	public Viewable summary(@QueryParam("ra") String ra
		,@QueryParam("dec") String dec,@QueryParam("id") String qid
		,@QueryParam("sid") String sid,@QueryParam("spec") String spec
		,@QueryParam("apid") String qapid,@QueryParam("plate") String plate
		,@QueryParam("mjd") String mjd,@QueryParam("fiber") String fiber) {
		/*
		 * 对接收到的参数进行处理
		 */
		if(qid != null )
			this.id = Utilities.ParseId(qid);
		if(sid != null)
		{
			if(sid.startsWith("2M"))
				sidstring = sid;
			else
				sidstring = (sid.isEmpty()) ? sid : Utilities.ParseId(sid).toString();
		}
		if(spec != null)
			sidstring = (spec.isEmpty()) ? spec : Utilities.ParseId(spec).toString();
//		try {DR9不支持该操作
//				if(qapid !=null && !qapid.isEmpty())
//				{
//					String s = null;
//					s = URLEncoder.encode(qapid,"UTF-8");
//		            if (qapid != null & !"".equals(qapid))
//		            {
//		                    this.apid = s;
//		            } 
//				}
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
		if(ra != null && !ra.isEmpty()) qra = Utilities.parseRA(ra); 	  // need to parse J2000
        if(dec != null && !dec.isEmpty()) qdec = Utilities.parseDec(dec); // need to parse J2000
        if(plate != null && !plate.isEmpty()) this.plate = Short.parseShort(plate);
        if(mjd != null && !mjd.isEmpty()) this.mjd = Integer.parseInt(mjd);
        if(fiber != null && !fiber.isEmpty()) this.fiber = Short.parseShort(fiber);
        
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
		return new Viewable("/tools/Summary.jsp", null); 
	}
	@GET
	@Path("AllSpec")
	public Viewable getAllSpec(@QueryParam("id") String id) {
		ObjectInfo objInfo = (ObjectInfo)request.getSession().getAttribute("objectInfo");
		if(objInfo==null)
		{
			request.setAttribute("timeout", "the request has timed out…");
			return new Viewable("/tools/Summary.jsp", null);
		}
		ObjectExplorer master = new ObjectExplorer(objInfo);
		AllSpec allSpec = new AllSpec(master);
		allSpec.setObjID(id);
		allSpec.executeQuery();
		request.setAttribute("allSpec", allSpec);
		request.setAttribute("master", master);
		return new Viewable("/tools/AllSpec.jsp", null);
	}
	@GET
	@Path("DisplayResults")
	public Viewable getDisplayResults(@QueryParam("id") String id
			,@QueryParam("apid") String apid,@QueryParam("spec") String spec
			,@QueryParam("field") String field,@QueryParam("cmd") String cmd
			,@QueryParam("name") String name,@QueryParam("url") String url) {
		ObjectInfo objInfo = (ObjectInfo)request.getSession().getAttribute("objectInfo");
		if(objInfo==null)
		{
			request.setAttribute("error", "the request has timed out…");
			return new Viewable("/tools/Summary.jsp", null);
		}
		ObjectExplorer master = new ObjectExplorer(objInfo);
		DisplayResults displayResults = new DisplayResults(master);
		
         if(apid != null && !apid.isEmpty())
			try {
				displayResults.setApid(URLEncoder.encode(apid, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
         displayResults.setObjID(id);
         displayResults.setSpecID(spec);
         
         displayResults.setFieldID(field);

         displayResults.setCmd(cmd);
         displayResults.setName(name);
         displayResults.setUrl(url);
	    
	     if(cmd == null || cmd.equals(""))
	    	 displayResults.getQuery();
	
	     displayResults.executeQuery();
	     
	     request.setAttribute("displayResults", displayResults);
	     request.setAttribute("master", master);
		return new Viewable("/tools/DisplayResults.jsp", null);
	}
	
	@GET
	@Path("Neighbors")
	public Viewable getNeighbors(@QueryParam("id") String id) {
		ObjectExplorer master = new ObjectExplorer((ObjectInfo)request.getSession().getAttribute("objectInfo"));
		Neighbors neighbors = new Neighbors(master);
		neighbors.setObjID(id);
		neighbors.executeQuery();
		
		request.setAttribute("neighbors", neighbors);
		request.setAttribute("master", master);
		return new Viewable("/tools/Neighbors.jsp", null);
	}
	@GET
	@Path("Plate")
	public Viewable getPlate(@QueryParam("plateID") String plateID) {
		ObjectInfo objInfo = (ObjectInfo)request.getSession().getAttribute("objectInfo");
		if(objInfo==null)
		{
			request.setAttribute("error", "the request has timed out…");
			return new Viewable("/tools/Summary.jsp", null);
		}
		ObjectExplorer master = new ObjectExplorer(objInfo);
		Plate plate = new Plate(master);
		plate.setPlateID(Utilities.ParseId(plateID));
		plate.executeQuery();
		
		request.setAttribute("plate", plate);
		request.setAttribute("master", master);
		return new Viewable("/tools/Plate.jsp", null);
	}
	@GET
	@Path("FitsImg")
	public Viewable getFitsImg(@QueryParam("field") String fieldID) {
		ObjectInfo objInfo = (ObjectInfo)request.getSession().getAttribute("objectInfo");
		if(objInfo==null)
		{
			request.setAttribute("error", "the request has timed out…");
			return new Viewable("/tools/Summary.jsp", null);
		}
		ObjectExplorer master = new ObjectExplorer(objInfo);
		FitsImg fitsImg = new FitsImg(master);
		Long id = Utilities.ParseId(fieldID);
		if(id !=null && id != 0)
			fitsImg.setHrefsCf(fitsImg.getCFrame(id.longValue()));
		
		request.setAttribute("fitsImg", fitsImg);
		request.setAttribute("master", master);
		return new Viewable("/tools/FitsImg.jsp", null);
	}
	@GET
	@Path("FitsSpec")
	public Viewable getFitsSpec(@QueryParam("sid") String sid) {
		ObjectInfo objInfo = (ObjectInfo)request.getSession().getAttribute("objectInfo");
		if(objInfo==null)
		{
			request.setAttribute("error", "the request has timed out…");
			return new Viewable("/tools/Summary.jsp", null);
		}
		ObjectExplorer master = new ObjectExplorer(objInfo);
		FitsSpec fitsSpec = new FitsSpec(master);
		fitsSpec.setSpecObjID(Utilities.ParseId(sid));
		fitsSpec.setHrefsSpec(fitsSpec.getFits());
		
		request.setAttribute("fitsSpec", fitsSpec);
		request.setAttribute("master", master);
		return new Viewable("/tools/FitsSpec.jsp", null);
	}
	@GET
	@Path("GalaxyZoo")
	public Viewable getGalaxyZoo(@QueryParam("id") String id) {
		ObjectInfo objInfo = (ObjectInfo)request.getSession().getAttribute("objectInfo");
		if(objInfo==null)
		{
			request.setAttribute("error", "the request has timed out…");
			return new Viewable("/tools/Summary.jsp", null);
		}
		ObjectExplorer master = new ObjectExplorer(objInfo);
		GalaxyZoo galaxyZoo = new GalaxyZoo(master);
		galaxyZoo.setObjID(id);
		galaxyZoo.executeQuery();
		
		request.setAttribute("galaxyZoo", galaxyZoo);
		request.setAttribute("master", master);
		return new Viewable("/tools/galaxyzoo.jsp", null);
	}
	@GET
	@Path("Matches")
	public Viewable getMatches(@QueryParam("id") String id) {
		ObjectInfo objInfo = (ObjectInfo)request.getSession().getAttribute("objectInfo");
		if(objInfo==null)
		{
			request.setAttribute("error", "the request has timed out…");
			return new Viewable("/tools/Summary.jsp", null);
		}
		ObjectExplorer master = new ObjectExplorer(objInfo);
		Matches matches = new Matches(master);
		matches.setObjID(id);
		matches.executeQueries();
		request.setAttribute("matches", matches);
		request.setAttribute("master", master);
		return new Viewable("/tools/Matches.jsp", null);
	}
	@GET
	@Path("Resolver")
	@Produces("text/plain")
	public void getResolver(@QueryParam("name") String name
							,@QueryParam("ra") String ra
							,@QueryParam("dec") String dec
							,@QueryParam("radius") String radius) {
		Resolver resolver = new Resolver();
		response.setContentType("text/plain");
		PrintWriter out =  null;
		try {
			out = response.getWriter();
			if (name != null && (ra == null && dec == null))
	        {
	            
				out.println(resolver.resolveName(name));
				
	        }
	        else if (name == null && (ra != null && dec != null))
	        {
	            if (radius == null) radius = resolver.DEFAULT_RADIUS;
	            String data = resolver.resolveCoords(ra, dec,radius);
	            System.out.println("data..."+data);
	            out.println(data);
	        }
	        else
	        {
	        	out.println("Error: Incorrect request parameters.");
	        }
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@GET
	@Path("Parameters")
	@Produces("text/plain")
	public Viewable getParameters(@QueryParam("spec") String spec) {
		ObjectExplorer master = new ObjectExplorer((ObjectInfo)request.getSession().getAttribute("objectInfo"));
		Parameters params = new Parameters(master);
		String qSpecId = spec;
        try
        {
            if (qSpecId != null && !"".equals(qSpecId))
            {
                // code changed by Jordan on 2013-3-28 to allow either decimal or hex input
                params.setSpecId(Utilities.ParseId(qSpecId));
                //if (qSpecId.StartsWith("0x")) specId = Int64.Parse(qSpecId.SubString(2), NumberStyles.AllowHexSpecifier);
                //else specId = Int64.Parse(qSpecId);
            }
        }
        catch (Exception ex)
        {
            // Could not parse, so leave null
        }

        params.getQueries();
        return new Viewable("/tools/Parameters.jsp", null);
	}
	/**
	 * 将十六进制的字符串形式的ID解析为Long类型
	 * 解析objID、specObjID
	 */
    private void parseIDs() {
        if (objectInfo.objID != null && !objectInfo.objID.equals(""))
            objectInfo.id = Utilities.ParseId(objectInfo.objID);

        if (objectInfo.specObjID != null && !objectInfo.specObjID.equals(""))
            objectInfo.specID = Utilities.ParseId(objectInfo.specObjID);

    } 
    /**
     * 根据不同类型的用户参数构造查询，获取将后续查询需要<br/>
     * 的所有id(objID、specObjID等)以及部分主要信息(坐标等)。
     */
    private void getObjPmts()
    {
    	System.out.println(" fiber,plate:"+fiber==null+","+(plate==null));
    	System.out.println(" ra,dec:"+(qra==null)+","+(qdec==null));
    	System.out.println(" specID:"+specID==null);
    	System.out.println(" sidstring:"+sidstring==null);
    	System.out.println(" id:"+id==null);
    	System.out.println(" apid:"+apid==null);
        if (fiber != null && plate != null) ObjIDFromPlfib(plate, mjd, fiber);
        else if (qra != null && qdec != null) pmtsFromEq(qra, qdec);
        else if (id != null) pmtsFromPhoto(id);
        else if (specID != null || (sidstring!=null && !sidstring.isEmpty())) pmtsFromSpec(sidstring);
//old        else if (id != null && specID == null) pmtsFromPhoto(id);
//        else if (apid!=null && !apid.isEmpty()) parseApogeeID(apid); DR9不支持该功能
    }
    /**
     * 通过三个参数查询满足条件的objID、specObjID
     * @param plate 
     * @param mjd 
     * @param fiber 
     */
    private void ObjIDFromPlfib(short plate, int mjd, short fiber)
    {
        
        Map<String,Object> attrsOne = null;
        attrsOne = explorerService.findObjIDFromPlatefiberMjd(mjd+"",plate+"",fiber+"");
        
        if(attrsOne != null && attrsOne.size()!=0)
        {
        	objectInfo.objID = attrsOne.get("objID")== null ? null :  Utilities.longToHex((Long)attrsOne.get("objID"));
        	objectInfo.specObjID = (String)attrsOne.get("specObjID") == null? null : Utilities.longToHex((Long)attrsOne.get("specObjID"));
        	objectInfo.ra = (Double)attrsOne.get("ra");
        	objectInfo.dec = (Double)attrsOne.get("dec");
        }
        
/*		DR9不支持该操作
        long apid = explorerService.findApid(objectInfo.ra, objectInfo.dec,(0.5 / 60));
        // if we couldn't find that plate/mjd/fiber, maybe it's an APOGEE object
        if (apid != -1)
        {
            objectInfo.apid = apid +"";
        }
*/
        //added by zoe
        objectInfo.apid="";
    }
   
   /**
    * 
    * @param qra 赤经
    * @param qdec 赤纬
    */
    private void photoFromEq(double qra, double qdec)
    {
    	double searchRadius = 0.5 / 60;
        
        PhotoTag pt = explorerService.findPhotoFromEq(qra, qdec, searchRadius);
        
        objectInfo.objID = pt.getObjID() == 0? null : Utilities.longToHex(pt.getObjID());
        objectInfo.specObjID = pt.getSpecObjID() == 0? null : Utilities.longToHex(pt.getSpecObjID());
       
    }
    /**
     * 根据赤经、赤纬查询基本信息参数。
     * @param qra 赤经，单位：度
     * @param qdec 赤纬，单位：度
     */
    private void pmtsFromEq(double qra, double qdec)
    {
       Map<String,Long> ids = explorerService.findIDsByEq(qra, qdec);
       Long objID = ids.get("objID");
       Long specObjID = ids.get("specObjID");
       if(ids!=null && ids.size()>0)
       {
    	   objectInfo.objID = objID ==null || objID.equals(0L)?null:Utilities.longToHex(objID);
           objectInfo.specObjID = specObjID ==null || specObjID.equals(0L)?null:Utilities.longToHex(specObjID);
       }
       
       if (objectInfo.objID != null && !objectInfo.objID.equals(""))
        {
            // This is required to get the primary specObjId (with sciprimary=1). PhotoTag.specObjId is not necessarily primary...
            pmtsFromPhoto(Utilities.ParseId(objectInfo.objID));
//            apogeeFromEq(qra, qdec); DR9不支持该操作
        }
    }

    /**
     * 利用sid查询主要信息参数
     * @param sid 光谱观测对象id
     */
    private void pmtsFromSpec(String sid)
    {
        long sidnumber = 0;
        /*DR9不支持该功能
        try			
        {
            pmtsFromSpecWithApogeeID(sidstring);	
            if (objectInfo.apid != null && objectInfo.apid != "")
            {
                photoFromEq(objectInfo.ra, objectInfo.dec);
            }
        }
        catch (Exception e) { }*/

        try
        {
            sidnumber = Long.parseLong(sidstring);
            pmtsFromSpecWithSpecobjID(sidnumber);
            /*if (objectInfo.specObjID != null && objectInfo.specObjID != ZERO_ID)
            {
                apogeeFromEq(objectInfo.ra, objectInfo.dec);
            } 	DR9 不支持该功能 */
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
  
    /**
     * 由specObjID查询获取天体的基本信息
     * @param sid 即specObjID
     */
    private void pmtsFromSpecWithSpecobjID(long sid)
    {
        Map<String,Object> attrs = null;
        attrs = explorerService.findPmtsFromSpecWithSpecobjID(sid);
        objectInfo.ra = (Double)attrs.get("ra");
        objectInfo.dec = (Double)attrs.get("dec");
        objectInfo.fieldID = (Long)attrs.get("fieldID") == 0 ? null : Utilities.longToHex((Long)attrs.get("fieldID"));
        objectInfo.objID = (Long)attrs.get("objID") == 0 ? null : Utilities.longToHex(((Long)attrs.get("objID")));
        objectInfo.specObjID = (Long)attrs.get("specObjID") == 0 ? null : Utilities.longToHex(((Long)attrs.get("specObjID")));
        objectInfo.plateID = (Long)attrs.get("plateID") == 0 ? null : Utilities.longToHex(((Long)attrs.get("fieldID")));
        objectInfo.mjd = (Integer)attrs.get("mjd");
        objectInfo.fiberID = (Short)attrs.get("fiberID");
        objectInfo.plate = (Short)attrs.get("plate");
    } 

    /**
     * 根据id查询主要信息。			<br/>
     * ra,dec:赤经、赤纬 				<br/>
     * run:
     * rerun:
     * camcol: 相机阵列
     * field: 
     * fieldID: 
     * objID:光度测量对象的id		<br/>
     * specObjID:光谱测量对象id		<br/>
     * @param id 光度测量对象的id	<br/>
     */
    private void pmtsFromPhoto(Long id)
    {
       
        PhotoTag photoTag = explorerService.findPhotoTag(id);
        objectInfo.ra = photoTag.getRa();
        objectInfo.dec = photoTag.getDec();
        objectInfo.run = photoTag.getRun();
        objectInfo.rerun = photoTag.getRerun();
        objectInfo.camcol = photoTag.getCamcol();
        objectInfo.field = photoTag.getField();
        objectInfo.fieldID = photoTag.getFieldID() == null ? null : Utilities.longToHex(photoTag.getFieldID());
        objectInfo.objID = photoTag.getObjID() == null ? null : Utilities.longToHex(photoTag.getObjID());
        objectInfo.specObjID = photoTag.getSpecObjID() == null ? null : Utilities.longToHex(photoTag.getSpecObjID());
        
        // 通过specObjID(如果存在)，获取plateID、 fiberID 
        if (objectInfo.specObjID != null && !ZERO_ID.equals(objectInfo.specObjID))
        {
            long specID = Long.parseLong(objectInfo.specObjID.substring(2),16);
            Map<String,Object> attrs = explorerService.findAttrsFormSpecObjAllAndPlateX(specID);
            
            if(attrs.size()>0)
            {
            	objectInfo.plateID = (Long)attrs.get("plateID") == 0 ? null : Utilities.longToHex((Long)attrs.get("plateID"));
            	objectInfo.mjd = (Integer)attrs.get("mjd");
            	objectInfo.fiberID = (Short)attrs.get("fiberID");
            	objectInfo.plate = (Short)attrs.get("plate");
            }
        }

        /*try		DR9不支持该功能
        {
            apogeeFromEq(objectInfo.ra, objectInfo.dec);
        }
        catch(Exception e) { e.printStackTrace();}*/
    }
  //--------------------------*****以下功能DR9不支持****--------------------------------
    /*
     
   private void apogeeFromEq(double qra, double qdec)
    {
	   double searchRadius = 0.5 / 60;
	   long apid = explorerService.findApid(qra,qdec,searchRadius);
	   objectInfo.apid = ""+apid;
    } */
    /*
    private void pmtsFromSpecWithApogeeID(String sid)
    {
        String whatdoiget = null;
        if (sid.startsWith("apogee")) { whatdoiget = "apstar_id"; } else { whatdoiget = "apogee_id"; }

        Map<String,Object> attrs = explorerService.findApogeeStar(whatdoiget,sid);
        objectInfo.apid = ((Long)attrs.get("apid")).toString();
        objectInfo.ra = (Double)attrs.get("ra");
        objectInfo.dec = (Double)attrs.get("dec");
    }
    */
    /*
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
    }*/
}
