package org.fastds.service;


import java.sql.ResultSet;
import java.util.Map;

import org.fastds.dao.ExplorerDao;
import org.fastds.model.ApogeeVisit;

import edu.gzu.domain.PhotoTag;

public class ExplorerService {
	private ExplorerDao dao = new ExplorerDao();

	public Map<String,Long> fillObjectInfo(double qra, double qdec) {
		
		return dao.getObjIDAndSpecObjIDByCoord(qra,qdec);
	}

	public long findApid(double qra, double qdec, double searchRadius) {
		
		return dao.getApid( qra,  qdec,  searchRadius);
	}

	public PhotoTag findPhotoTag(long id) {
		
		return dao.getPhotoTag(id) ;
	}
	
	public Map<String, Object> findAttrsFormSpecObjAllAndPlateX(long specID) {
//		objectInfo.objId = reader["objId"] is DBNull  null : Functions.BytesToHex((byte[])reader["objId"]);
//        objectInfo.specObjId = reader["specObjId"] is DBNull  null : Functions.BytesToHex((byte[])reader["specObjId"]);
		
		return dao.getAttrsFormSpecObjAllAndPlateX(specID);
	}

	public PhotoTag findPhotoFromEq(double qra, double qdec, double searchRadius) {
		return dao.getPhotoFromEq(qra,qdec,searchRadius);
	}

	public Map<String, Object> findApogeeStar(String whatdoiget, String sid) {
		return dao.getApogeeStar(whatdoiget,sid);
	}

	public Map<String, Object> findPmtsFromSpecWithSpecobjID(long sid) {
		return dao.getPmtsFromSpecWithSpecobjID(sid);
	}

	public Map<String, Object> findImaging(String objID) {
		return dao.getImaging(objID);
	}

	public String findUnit(String tablename, String columname) {
		return dao.getUnitFromDBColumns(tablename,columname);
	}
	public Map<String,String> findUnit() {
		return dao.getAttrsFromDBColumns();
	}
	
	public ApogeeVisit findApogeevisitsByID(String id)
	{
		return dao.getApogeevisitsByID(id);
	}

	public Map<String, Object> findApogeeByAql(String command) {
		return dao.getApogeeByAql(command);
	}

	public Map<String, Object> findAttrsFromUSNOByID(String objID) {
		return dao.getAttrsFromUSNOByID(objID);
	}

	public Map<String, Object> findAttrsFromFirst(String objID) {
		return dao.findAttrsFromFirst(objID);
	}

	public Map<String, Object> findAttrsFromRosat(String objID) {
		return dao.getAttrsFromRosat(objID);
	}

	public Map<String, Object> findAttrsFromRC3(String objID) {
		// TODO Auto-generated method stub
		return dao.getAttrsFromRC3(objID);
	}

	public Map<String, Object> findAttrsFromWISE(String objID) {
		return dao.getAttrsFromWISE(objID);
	}

	public Map<String, Object> findAttrsFromTWOMASS(String objID) {
		// TODO Auto-generated method stub
		return dao.getAttrsFromTWOMASS(objID);
	}

	public Map<String, Object> findParamsFromTables(String objID) {
		return dao.getParamsFromTables(objID);
	}

	public Map<String, Object> findAttrsFromSpecObjAllAndPlateX(String objID,
			String specID) {
		return dao.getAttrsFromSpecObjAllAndPlateX(objID,specID);
	}

	public Map<String, Object> findApogeeByID(String apid) {
		// TODO Auto-generated method stub
		return dao.getApogeeByID(apid);
	}

	public Map<String, Object> findApogeeByID2(String apid) {
		// TODO Auto-generated method stub
		return dao.getApogeeByID2(apid);
	}

	public Map<String, Object> findObjIDFromPlatefiberMjd(String mjd,
			String plate, String fiber) {
		return dao.getObjIDFromPlatefiberMjd(mjd,plate,fiber);
	}

	public ResultSet findAllSpec1RS(String objID) {
		// TODO Auto-generated method stub
		return dao.getAllSpec1RS(objID);
	}

	public ResultSet findAllSpec2RS(String objID) {
		// TODO Auto-generated method stub
		return dao.getAllSpec2RS(objID);
	}

	public ResultSet runCmd(String cmd) {
		
		return dao.getResultSetFromCmd(cmd);
	}

}
