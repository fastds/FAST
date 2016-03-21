package org.fastds.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.fastds.dbutil.DBConnection;
import org.junit.Test;
import org.scidb.jdbc.IStatementWrapper;
import org.scidb.jdbc.ResultSetMetaData;

public class ExQuery {
	private Connection conn = null;
	private Statement state = null;
	private ResultSet res = null;
	private ResultSetMetaData meta = null;

	public ResultSet containerAflQuery(String queryString, String ip)
			throws IOException, SQLException {
		try {
			conn = DBConnection.getConnection(ip);
			state = conn.createStatement();// 27DBConnection.getConnection().createStatement();
			IStatementWrapper staWrapper = state
					.unwrap(IStatementWrapper.class);
			staWrapper.setAfl(true);

			res = (ResultSet) state.executeQuery(queryString);
			conn.commit();
		} catch (SQLException e) {
			closeRes();
			closeState();
			closeConn();
			throw new SQLException(e.getMessage());
		}
		return res;
	}

	public ResultSet aflQuery(String queryString) throws IOException,
			SQLException {
		try {
			conn = DBConnection.getConnection();
			state = conn.createStatement();// 27DBConnection.getConnection().createStatement();
			IStatementWrapper staWrapper = state
					.unwrap(IStatementWrapper.class);
			staWrapper.setAfl(true);
			
			res = (ResultSet) state.executeQuery(queryString);
			conn.commit();
		} catch (SQLException e) {
			closeRes();
			closeState();
			closeConn();
			throw e;
		}
		return res;
	}

	public ResultSet containerAqlQuery(String queryString, String ip)
			throws IOException, SQLException {
		try {
			conn = DBConnection.getConnection(ip);
			state = conn.createStatement();// 27DBConnection.getConnection().createStatement();
			res = (ResultSet) state.executeQuery(queryString);
			conn.commit();
		} catch (SQLException e) {
			closeRes();
			closeState();
			closeConn();
			throw new SQLException(e.getMessage());
		}
		return res;
	}
	@Test
	public void test()
	{
		try {
//			aqlQuery("SELECT q.objID AS fieldID, m.span  FROM AtlasOutline AS m JOIN  (SELECT min(f.objID) AS objID  FROM AtlasOutline AS o JOIN (SELECT ra,dec,objID  FROM PhotoPrimary  WHERE htmID BETWEEN 12094627905536 AND 13194139533311 AND pow(0.9209378372080361-cx,2)+pow(-0.38936036482073366-cy,2)+pow(-0.016492613657328997-cz,2)<1.740325247409794E-5  AND r<=23.5) AS f  ON f.objID=o.objID GROUP BY o.rmin,o.rmax,o.cmin,o.cmax ) AS q  ON m.objID=q.objID");
			ResultSet rs = aqlQuery("SELECT   pt.flags,pt.ra, pt.dec, pt.run, pt.rerun, pt.camcol, pt.field, pt.fieldID, pt.objID,  pt.clean, pt.type AS otype,  pt.u , pt.g , pt.r , pt.i , pt.z , pt.err_u ,  pt.err_g ,  pt.err_r , pt.err_i , pt.err_z ,  po.mode , po.mjd AS mjdNum,  (po.nDetect-1) AS Other_observations, po.parentID, po.nChild, po.extinction_r, po.petroRad_r, po.petroRadErr_r , phz.z, phz.zErr,  (1*zz.spiral+10*zz.elliptical+100*zz.uncertain) AS GalaxyZoo_Morph"  
+" FROM ( SELECT objID,clean,skyVersion,run,rerun,camcol,field,obj,mode,nChild,type,probPSF,insideMask,flags,flags_u,flags_g,flags_r,flags_i,flags_z,psfMag_u,psfMag_g,psfMag_r,psfMag_i,psfMag_z,psfMagErr_u,psfMagErr_g,psfMagErr_r,psfMagErr_i,psfMagErr_z,petroMag_u,petroMag_g,petroMag_r,petroMag_i,petroMag_z,petroMagErr_u,petroMagErr_g,petroMagErr_r,petroMagErr_i,petroMagErr_z,petroR50_r,petroR90_r,u ,g ,r ,i ,z ,err_u ,err_g ,err_r ,err_i ,err_z ,cModelMag_u,cModelMag_g,cModelMag_r,cModelMag_i,cModelMag_z,cModelMagErr_u,cModelMagErr_g,cModelMagErr_r,cModelMagErr_i,cModelMagErr_z,mRrCc_r,mRrCcErr_r,mRrCcPSF_r,fracDeV_u,fracDeV_g,fracDeV_r,fracDeV_i,fracDeV_z,psffwhm_u,psffwhm_g,psffwhm_r,psffwhm_i,psffwhm_z,resolveStatus,thingId,balkanId,nObserve,nDetect,calibStatus_u,calibStatus_g,calibStatus_r,calibStatus_i,calibStatus_z,ra,dec,cx,cy,cz,extinction_u,extinction_g,extinction_r,extinction_i,extinction_z,htmID,fieldID,specObjID"
+" FROM PhotoObjAll ) AS pt, (SELECT * FROM PhotoObjAll WHERE mode=1 OR mode=2) AS po,  Photoz AS phz, zooSpec AS zz"
	  +" WHERE  po.objID = pt.objID AND pt.objID=phz.objID AND  pt.objID=zz.objID AND pt.objID=1237645942905569462");
			System.out.println(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ResultSet aqlQuery(String queryString) throws SQLException {
		try {
			try {
				conn = DBConnection.getConnection();
			} catch (IOException e) {

				e.printStackTrace();
			}
			state = conn.createStatement();// 27DBConnection.getConnection().createStatement();
			res = state.executeQuery(queryString);
			// conn.commit();
		} catch (SQLException e) {

			closeRes();
			closeState();
			closeConn();
			throw e;

		}
		return res;
	}

	public String showResult(ResultSet res) throws SQLException, IOException {
		StringBuilder sb = new StringBuilder("");
		if (res == null) {
			return "No array exits";
		}
		meta = (ResultSetMetaData) res.getMetaData();
		int colCount = meta.getColumnCount();
		sb.append("<tr>");
		for (int i = 1; i <= colCount; i++) {
			if (i == colCount) {
				sb.append("<td>");
				sb.append(meta.getColumnName(i));
				sb.append("</td>");
			} else {
				sb.append("<td>");
				sb.append(meta.getColumnName(i) + "\t");
				sb.append("</td>");
			}
		}
		sb.append("</tr>");
		while (!res.isAfterLast()) {
			sb.append("<tr>");
			for (int j = 1; j <= colCount; j++) {
				if (j == colCount) {
					if ("string".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getString(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else {
						sb.append("<td>");
						sb.append(res.getDouble(meta.getColumnLabel(j)));
						sb.append("</td>");
					}
				} else {
					// sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
					if ("string".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getString(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {
						sb.append("<td>");
						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						sb.append("</td>");
					} else {
						sb.append("<td>");
						sb.append(res.getDouble(meta.getColumnLabel(j)));
						sb.append("</td>");
					}
				}
			}
			sb.append("</tr>");
			res.next();
		}
		closeRes();
		closeState();
		closeConn();
		return sb.toString();
	}

	public String showResultwithline(ResultSet res, int line)
			throws SQLException, IOException {
		StringBuilder sb = new StringBuilder("");
		if (res == null)
			return "";
		meta = (ResultSetMetaData) res.getMetaData();
		int colCount = meta.getColumnCount();
		for (int i = 1; i <= colCount; i++) {
			if (i == colCount) {

				sb.append(meta.getColumnName(i));

			} else {

				sb.append(meta.getColumnName(i));
				sb.append(",");
			}
		}
		sb.append("!");
		int n = 1;
		while (!res.isAfterLast()) {
			if (n > line) {
				break;
			}

			for (int j = 1; j <= colCount; j++) {
				if (j == colCount) {
					if ("string".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getString(meta.getColumnLabel(j)));

					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));

					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						;
					} else {

						sb.append(res.getDouble(meta.getColumnLabel(j)));

					}
				} else {

					if ("string".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getString(meta.getColumnLabel(j)));
						sb.append(",");
					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
						sb.append(",");
					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {

						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						sb.append(",");
					} else {

						sb.append(res.getDouble(meta.getColumnLabel(j)));
						sb.append(",");
					}
				}
			}
			sb.append("!");
			res.next();
			n++;
		}

		closeRes();
		closeState();
		closeConn();
		return sb.toString();
	}

	public String showAllResul(ResultSet res) throws SQLException, IOException {
		StringBuilder sb = new StringBuilder("");
		if (res == null)
			return "";
		meta = (ResultSetMetaData) res.getMetaData();
		int colCount = meta.getColumnCount();
		// sb.append("<table>");
		// sb.append("<tr>");
		for (int i = 1; i <= colCount; i++) {
			if (i == colCount) {
				// sb.append("<td>");
				sb.append(meta.getColumnName(i));
				// sb.append(" ");
			} else {
				// sb.append("<td>");
				sb.append(meta.getColumnName(i));
				sb.append(",");
			}
		}
		sb.append("!");
		while (!res.isAfterLast()) {
			// sb.append("<tr>");
			for (int j = 1; j <= colCount; j++) {
				if (j == colCount) {
					if ("string".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getString(meta.getColumnLabel(j)));
						// sb.append(" ");
					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
						// sb.append(" ");
					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						// sb.append(" ");
					} else {
						// sb.append("<td>");
						sb.append(res.getDouble(meta.getColumnLabel(j)));
						// sb.append(" ");
					}
				} else {
					// sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
					if ("string".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getString(meta.getColumnLabel(j)));
						sb.append(",");
					} else if ("int64".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getBigDecimal(meta.getColumnLabel(j)));
						sb.append(",");
					} else if ("bool".endsWith(meta.getColumnTypeName(j))) {
						// sb.append("<td>");
						sb.append(res.getBoolean(meta.getColumnLabel(j)));
						sb.append(",");
					} else {
						// sb.append("<td>");
						sb.append(res.getDouble(meta.getColumnLabel(j)));
						sb.append(",");
					}
				}
			}
			sb.append("!");
			res.next();
		}
		// sb.append("</table>");
		closeRes();
		closeState();
		closeConn();
		return sb.toString();
	}

	public void closeRes() {
		try {
			if (res != null) {
				res.close();
				res = null;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void closeState() {
		try {
			if (state != null) {
				state.close();
				state = null;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void closeConn() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
