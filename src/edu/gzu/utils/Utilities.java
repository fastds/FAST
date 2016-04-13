package edu.gzu.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class Utilities {
	
    /// Parses an identifier represented as integer or hex String 
    /// starting with '0x...' (e.g. '0x112d0bd721480030'). 
    public static Long ParseId (String s)
    {
        Long id = null;
        if (s != null & !"".equals(s))
        {
            s = s.toLowerCase();
            try
            {
                if (s.startsWith("0x"))
                    id = Long.parseLong(s.substring(2), 16);
                else
                    id = Long.parseLong(s);
            }
            catch (Exception e) { }
        }

        return id;
    }
    
    public static boolean valueCheck(String name, double val, double lo, double hi)
    {
        //var err = false;
        if (val > hi)
        {
            //Response.Write(name + " must be less than " + hi + " <P>");
            return true;
        }
        if (val < lo)
        {
            //Response.Write(name + " must be more than " + lo + "<P>");
            return true;
        }
        if (Double.isNaN(val))
        {
            //Response.Write("Please enter numerical values for " + name + "<P>");
            return true;
        }
        return false;
    }

    public static boolean rangeCheck(String name, double min, double max, double lo, double hi) {
    if(min > max){
	    //Response.Write("Minimum "+name+" value must be less than maximum <P>" );
	    return true;
	    }
    if (max>hi){
	    //Response.Write("Max "+name+" must be less than "+hi+" <P>");
	    return true;
	    }
    if (min<lo){
	    //Response.Write("Min "+name+" must be more than "+lo+"<P>");
	    return true;
	    }
   if (Double.isNaN(min) || Double.isNaN(max) ) {
	    //Response.Write("Please enter numerical values for "+name+" min and max.<P>");
	    return true;
	    }
   return false;
}

    /*public static String getURL(HttpRequest request)
    {
    	String host = request.ServerVariables["SERVER_NAME"];
    	String path = request.ServerVariables["SCRIPT_NAME"];

    	String root = "http://" + host;
    	String[] q = path.split("/");

    	String lang = "";
        for (int i = 0; i < q.length; i++)
        {
            if (i > 0) root += "/";
            root += q[i];
            lang = q[i];
            if (lang == "en" || lang == "de" || lang == "jp"
              || lang == "hu" || lang == "sp" || lang == "ce" || lang == "pt" || lang == "zh" || lang == "uk" || lang == "ru")
            {
                // depth = q.length - i - 2;
                return root;
            }
        }
        return root;
    } old */

    /*public static String bytesToHex(byte[] bytes)
    {
        if (bytes == null) return null;
        else
        	return "0x" + BitConverter.ToString(bytes).Replace("-", String.Empty).toLowerCase();
    }old */

    //long类型转成hex字符串 
    public static String longToHex(long data) { 
    	String hexString = Long.toHexString(data);
    	int len = hexString.length();
    	for(int i = 16-len;  i> 0 ; i--)
    	{
    		hexString = "0" + hexString;
    	}
    	return "0x"+hexString.toLowerCase();
    } 
    
    public static double parseRA(String s_ra) {
    	Pattern p = Pattern.compile("\\d \\d");
        double v;
        if (s_ra.contains(":")) {
	        v = hms2deg(s_ra,':');
        } else {
//	        if(Regex.IsMatch(s_ra,@"\d \d")) {
	        	if(p.matcher(s_ra).matches()) {
		        v = hms2deg(s_ra,' ');
	        } else {
		        v = Double.parseDouble(s_ra);
		        if(Double.isNaN(v)) v=180.0;
		        if (v<0) v+= 360;
		        if (v>360) v-= 360;
	        }
        }
        return v;
    }

    public static double parseDec(String s_dec) {
    	Pattern p = Pattern.compile("\\d \\d");
    	;
        double v;
        if (s_dec.contains(":")) {
	        v = dms2deg(s_dec,':');
        } else {
//	        if(Regex.IsMatch(s_dec,@"\d \d")) {
	        	if(p.matcher("s_dec").matches()) {
		        v = dms2deg(s_dec,' ');
	        } else {
		        v = Double.parseDouble(s_dec);
		        if(Double.isNaN(v)) v=0.0;
		        if (v<-90) v= -90;
		        if (v>90) v= 90;
	        }
        }
        return v;
    }

    public static double hms2deg(String s, char c) {
	
        while(s.length() > 0 && (s.substring(0,1).equals(" ") || s.substring(0,1).equals("+")) )
		    s = s.substring(1);
	    String[] a = s.split(c+"");
	    return 15*Double.parseDouble(a[0])+Double.parseDouble(a[1])/4.0+Double.parseDouble(a[2])/240.0;
    }

    public static double dms2deg(String s, char c) {
        while(s.length() > 0 && (s.substring(0,1).equals(" ") || s.substring(0,1).equals("+")) )
		    s = s.substring(1);
        String[] a = s.split(c+"");
	    if(s.contains("-"))	
		    return -(-1.0*Double.parseDouble(a[0])+Double.parseDouble(a[1])/60.0+Double.parseDouble(a[2])/3600.0);
	    else
		    return 1.0*Double.parseDouble(a[0])+Double.parseDouble(a[1])/60.0+Double.parseDouble(a[2])/3600.0;
    }

    public static String ccut(String name,int count,double min,double max) {
        return ((count==0)?" WHERE ":" AND ")+name+" BETWEEN "+min+" AND "+max;
    }


    public static double dec2glat(double ra, double dec)
    {
        double galPoleRA = 192.859508;
        double galPoleDec = 27.128336;
        double galAscNode = 32.932;

        // convert all angles from degrees to radians for the trig functions
        ra = ra * (Math.PI / 180);
        dec = dec * (Math.PI / 180);
        galPoleRA = galPoleRA * (Math.PI / 180);
        galPoleDec = galPoleDec * (Math.PI / 180);
        galAscNode = galAscNode * (Math.PI / 180);

        double glat = Math.asin((Math.cos(dec) * Math.cos(galPoleDec) * Math.cos(ra - galPoleRA)) + (Math.sin(dec) * Math.sin(galPoleDec)));

        glat = glat * (180 / Math.PI);

        if (glat < -90) glat = glat + 90;
        if (glat > 90) glat = glat - 90;

        return glat;
    }

    public static double ra2glon(double ra, double dec)
    {
        double glat = dec2glat(ra,dec); // first, find the galactic latitude (B) since we'll need it in the formula

        double galPoleRA = 192.859508;
        double galPoleDec = 27.128336;
        double galAscNode = 32.932;

        // convert all angles from degrees to radians for the trig functions
        ra = ra * (Math.PI / 180);
        dec = dec * (Math.PI / 180);
        galPoleRA = galPoleRA * (Math.PI / 180);
        galPoleDec = galPoleDec * (Math.PI / 180);
        galAscNode = galAscNode * (Math.PI / 180);
        glat = glat * (Math.PI / 180);

        double numerator = Math.sin(dec) - (Math.sin(glat) * Math.sin(galPoleDec));
        double denominator = Math.cos(dec) * Math.sin(ra - galPoleRA) * Math.cos(galPoleDec);

        double glon = Math.atan2(numerator, denominator) + galAscNode;  // use atan2 instead of atan to solve ambiguity of arctan

        glon = glon * (180 / Math.PI); // convert answer back to degrees

        if (glon < 0) glon = glon + 360;
        if (glon > 360) glon = glon - 360;

        return glon;
    }


    public static double glon2ra(double L, double B)
    {
        // function to convert from a galactic longitude (b) to right ascension
        // input: the galactic latitude (l) and longitude (b) of a point in degrees
        // output: the celestial RA in degrees

        double i = 192.859508;
        double j = 27.128336;
        double k = 32.932;

        double m = L - k;

        // convert all angles from degrees to radians for the trig functions
        L = L * (Math.PI / 180);
        B = B * (Math.PI / 180);
        double x = i * (Math.PI / 180);
        double y = j * (Math.PI / 180);
        double z = m * (Math.PI / 180);

        double numerator = Math.cos(B) * Math.cos(z);
        double denominator = (Math.sin(B) * Math.cos(y)) - (Math.cos(B) * Math.sin(y) * Math.sin(z));

        double ra = Math.atan2(numerator, denominator) + x;  // use atan2 instead of atan to solve ambiguity of arctan
        // double ra = Math.Atan(numerator / denominator) + x;

        ra = ra * (180 / Math.PI); // convert answer back to degrees
        if (ra > 360) ra = ra - 360;
        return ra;
    }

    public static double glat2dec(double L, double B)
    {
        // function to convert from a galactic latitude (L) to declination
        // input: the galactic latitude (L) and longitude (B) of a point in degrees
        // output: that point's celestial dec in degrees

        double i = 192.859508;
        double j = 27.128336;
        double k = 32.932;

        double m = L - k;

        // convert all angles from degrees to radians for the trig functions
        L = L * (Math.PI / 180);
        B = B * (Math.PI / 180);
        double x = i * (Math.PI / 180);
        double y = j * (Math.PI / 180);
        double z = m * (Math.PI / 180);

        double dec = Math.asin((Math.cos(B) * Math.cos(y) * Math.sin(z)) + (Math.sin(B) * Math.sin(y)));
        dec = dec * (180 / Math.PI); // convert answer back to degrees

        return dec;

    }

   /* public static String getSqlString(Object o)
    {
    	String result = "";      
//    	if(o != null && o instanceof SqlBinary)
        if (o is SqlBinary)
        {
            result = Utilities.BytesToHex(((SqlBinary)o).Value);
        }
        else
        {
            result = o.toString();
        }
        return result;
    }
 old ---*/
    public static void ValueCheckOrFail(String name, double val, double lo, double hi) throws Exception
    {
        if (val > hi)
        {
            throw new Exception(name + " must be less than " + hi);
        }
        if (val < lo)
        {
            throw new Exception(name + " must be more than " + lo);
        }
        if (Double.isNaN(val))
        {
            throw new Exception("Please enter numerical values for " + name);
        }
    }

    public static void RangeCheckOrFail(String name, double min, double max, double lo, double hi) throws Exception
    {
        if (min > max)
        {
            throw new Exception("Minimum " + name + " value must be less than maximum");
        }
        if (max > hi)
        {
            throw new Exception("Max " + name + " must be less than " + hi);
        }
        if (min < lo)
        {
            throw new Exception("Min " + name + " must be more than " + lo);
        }
        if (Double.isNaN(min) || Double.isNaN(max))
        {
            throw new Exception("Please enter numerical values for " + name + " min and max");
        }
    }
    //------------------------------from function--------------------
    public static String hmsPad(double deg)
    {
        double hh = Math.floor(deg / 15.0);
        double qq = 4.0 * (deg - 15 * hh);
        double mm = Math.floor(qq);
        double ss = Math.floor(600.0 * (qq - mm)) / 10.0;
        return (pad(hh) + " " + pad(mm) + " " + pad(ss));
    }

    public static String pad(double val)
    {
        return (val < 10) ? ("0" + val) : ("" + val);
    }
    
    public static String pad(String val)
    {
        return (Double.parseDouble(val)<10) ? ("0" + val) : ("" + val);
    }
    
    public static String dmsPad(double deg)
    {
    	String sign = (deg < 0) ? "-" : "+";
        deg = (deg < 0) ? -deg : deg;
        double dd = Math.floor(deg);
        double qq = 60.0 * (deg - dd);
        double mm = Math.floor(qq);
        double ss = Math.floor(600.0 * (qq - mm)) / 10.0 ;
        return (sign + pad(dd) + " " + pad(mm) + " " + pad(Double.parseDouble(new DecimalFormat("#.00").format(ss))));
    }

    public static String SDSSname(double ra, double dec)
    {
        return "SDSS J" + hmsIAU(ra) + dmsIAU(dec);
    }
    public static String hmsIAU(double deg)
    { 
        double hh = Math.floor(deg / 15.0);
        double qq = 4.0 * (deg - 15 * hh);
        double mm = Math.floor(qq);
        double ss = Math.floor(6000.0 * (qq - mm)) / 100.0;
        String tempHH = pad(hh);
        String tempMM = pad(mm);
        return tempHH.substring(0,tempHH.indexOf('.')) + tempMM.substring(0,tempMM.indexOf('.')) +  pad(Double.parseDouble(new DecimalFormat("#.00").format(ss)));
    }

    public static String dmsIAU(double deg)
    {
    	String sign = (deg < 0) ? "-" : "+";
        deg = (deg < 0) ? -deg : deg;
        double dd = Math.floor(deg);
        double qq = 60.0 * (deg - dd);
        double mm = Math.floor(qq);
        double ss = Math.floor(600.0 * (qq - mm)) / 10.0;
        String tempDD = pad(dd);
        String tempMM = pad(mm);
        return (sign + tempDD.substring(0,tempDD.indexOf('.')) + tempMM.substring(0,tempMM.indexOf('.')) + pad(Double.parseDouble(new DecimalFormat("#.0").format(ss))));
    }

    public static String hmsC(double deg)
    {
        double hh = Math.floor(deg / 15.0);
        double qq = 4.0 * (deg - 15 * hh);
        double mm = Math.floor(qq);
        double ss = Math.floor(6000.0 * (qq - mm)) / 100.0;
        return (pad(hh) + ":" + pad(mm) + ":" + pad(Double.parseDouble(new DecimalFormat("#.00").format(ss))));
    }

    public static String dmsC(double deg)
    {
        String sign = (deg < 0) ? "-" : "+";
        deg = (deg < 0) ? -deg : deg;
        double dd = Math.floor(deg);
        double qq = 60.0 * (deg - dd);
        double mm = Math.floor(qq);
        double ss = Math.floor(6000.0 * (qq - mm)) / 100.0;
        return (sign + pad(dd) + ":" + pad(mm) + ":" + pad(Double.parseDouble(new DecimalFormat("#.00").format(ss))));
    }
    /**
     * Map<0,>:储存属性名称集合</br>
     * Map<1..n,List<>>:储存1-n各个行所有属性值集
     * @param rs 结果集
     * @return 返回一个Map
     */
	public static Map<Integer, List<Object>> resultSet2Map(ResultSet rs) {
		ResultSetMetaData meta = null;
		Map<Integer, List<Object>> res = new HashMap<Integer,List<Object>>();
		List<Object> values = null;
		int num = 0;
		try {
			if (rs!=null && !rs.isAfterLast())
			{
				meta = rs.getMetaData();
				int colCount =  meta.getColumnCount();
				/*把属性名存储如Map<0,List<>>中*/
				values = new ArrayList<Object>();
			    for (int k = 1; k <= colCount; k++)
			    {
			    	values.add(meta.getColumnName(k));
			    }
			    res.put(num++,values);
			    while (!rs.isAfterLast())
			    {
			    	values = new ArrayList<Object>();
			        for (int k = 1; k <= colCount; k++)
			        {
			        	String typeName = meta.getColumnTypeName(k);
			            // think something else if possible for this
		                if ("bool".endsWith(typeName)) {
		                	values.add(rs.getBoolean(k));
						} else if ("double".endsWith(typeName)) {
							values.add(rs.getBigDecimal(k));
						} else if ("string".endsWith(typeName)) {
							String str = rs.getString(k);
							if("img".equals(meta.getColumnName(k)))
								str = "image";
							if(str.contains("<"))
								str = str.replace("<", "&lt;");
							if(str.contains(">"))
								str = str.replace(">","&gt;");
							values.add(str);
							
						} else if ("datetime".endsWith(typeName)) {
							values.add(rs.getTime(k));
						} else {
							values.add(rs.getLong(k));
						}
			        }
			        res.put(num++,values);
			    }
			    rs.next();
			}
			else {
			    return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

}
