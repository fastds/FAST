package org.fastds.model;

public class View {
	/**
	 * 注意：size列（mRrCc）推迟到获取结果集后进行处理
	 * @return A select of The most popular columns from PhotoObjAll.
	 */
	public static String getPhotoTag()
	{
		StringBuilder aql = new StringBuilder();
		aql.append(" SELECT objID"
			      +",skyVersion"
			      +",run"
			      +",rerun"
			      +",camcol"
			      +",field"
			      +",obj"
			      +",mode"
			      +",nChild"
			      +",type"
			      +",probPSF"
			      +",insideMask"
			      +",flags"
			      +",flags_u"
			      +",flags_g"
			      +",flags_r"
			      +",flags_i"
			      +",flags_z"
			      +",psfMag_u"
			      +",psfMag_g"
			      +",psfMag_r"
			      +",psfMag_i"
			      +",psfMag_z"
			      +",psfMagErr_u"
			      +",psfMagErr_g"
			      +",psfMagErr_r"
			      +",psfMagErr_i"
			      +",psfMagErr_z"
			      +",petroMag_u"
			      +",petroMag_g"
			      +",petroMag_r"
			      +",petroMag_i"
			      +",petroMag_z"
			      +",petroMagErr_u"
			      +",petroMagErr_g"
			      +",petroMagErr_r"
			      +",petroMagErr_i"
			      +",petroMagErr_z"
			      +",petroR50_r"
			      +",petroR90_r"
			      +",u " /*as modelMag_u*/
			      +",g "/*as modelMag_g*/
			      +",r "/*as modelMag_r*/
			      +",i "/*as modelMag_i*/
			      +",z "/*as modelMag_z*/
			      +",err_u " /*as modelMagErr_u*/
			      +",err_g "/*as modelMagErr_g*/
			      +",err_r "/*as modelMagErr_r*/
			      +",err_i "/* as modelMagErr_i*/
			      +",err_z "/*as modelMagErr_z*/
			      +",cModelMag_u"
			      +",cModelMag_g"
			      +",cModelMag_r"
			      +",cModelMag_i"
			      +",cModelMag_z"
			      +",cModelMagErr_u"
			      +",cModelMagErr_g"
			      +",cModelMagErr_r"
			      +",cModelMagErr_i"
			      +",cModelMagErr_z"
			      +",mRrCc_r"
			      +",mRrCcErr_r"
			      +",mRrCcPSF_r"/*mRrCcPSF_r*/
			      +",fracDeV_u"
			      +",fracDeV_g"
			      +",fracDeV_r"
			      +",fracDeV_i"
			      +",fracDeV_z"
			      +",psffwhm_u"
			      +",psffwhm_g"
			      +",psffwhm_r"
			      +",psffwhm_i"
			      +",psffwhm_z"
			      +",resolveStatus"
			      +",thingId"
			      +",balkanId"
			      +",nObserve"
			      +",nDetect"
			      +",calibStatus_u"
			      +",calibStatus_g"
			      +",calibStatus_r"
			      +",calibStatus_i"
			      +",calibStatus_z"
			      +",ra"
			      +",dec"
			      +",cx"
			      +",cy"
			      +",cz"
			      +",extinction_u"
			      +",extinction_g"
			      +",extinction_r"
			      +",extinction_i"
			      +",extinction_z"
			      +",htmID"
			      +",fieldID"
			      +",specObjID "
			      +" FROM PhotoObjAll "
			      );
		/*,( case when mRrCc_r > 0 then SQRT(mRrCc_r/2.0)else 0 end) as size
		  这一列推迟到获取结果集时处理*/
		return aql.toString();
	}

	public static String getPhotoObj() {
		return "SELECT * FROM PhotoObjAll WHERE mode=1 OR mode=2";
	}
	public static String getPhotoFlags()
	{
	    return "SELECT name,value,description FROM DataConstants WHERE field='PhotoFlags' AND name != ''";
	}

	public static String getPrimTarget() {
		return "SELECT name,value,description FROM DataConstants WHERE field='PrimTarget' AND name != ''";
	}
	public static String getPhotoMode() {
		return "SELECT name,value,description FROM DataConstants WHERE field='PhotoMode' AND name != ''";
	}
}
