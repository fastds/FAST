package edu.gzu.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class Resolver {
	 private final String BASE_URL = "http://simbad.u-strasbg.fr/simbad/sim-script?script=";
     public final String DEFAULT_RADIUS = "0.5m";

     public void ProcessRequest()
     {
//         context.Response.ContentType = "text/plain";
//
//         try
//         {
//             String name = context.Request["name"];
//             String ra = context.Request["ra"];
//             String dec = context.Request["dec"];
//
//             if (name != null && (ra == null && dec == null))
//             {
//                 context.Response.Write(resolveName(name));
//             }
//             else if (name == null && (ra != null && dec != null))
//             {
//                 String radius = context.Request["radius"];
//                 if (radius == null) radius = DEFAULT_RADIUS;
//                 context.Response.Write(resolveCoords(ra, dec,radius));
//             }
//             else
//             {
//                 context.Response.Write("Error: Incorrect request parameters.");
//             }
//         }
//         catch (Exception e)
//         {
//             context.Response.Write("Error: " + e.Message);
//         }
     }

     public String resolveName(String name)
     {
         String result = "";
         String script = "output console=off script=off\nformat object \"%D,%MAIN_ID,%COO(d;A,D)\"\n" + name;
         String s;
	try {
			s = sendGet(BASE_URL + URLEncoder.encode(script,"UTF-8"),"");
		
         if (!s.startsWith("1,"))
         {
             throw new Exception("Nothing found.");
         }
         String[] parts = split(s,new String[] { ",", "\n" });
         
         result += "Name: " + parts[1] + "\n";
         result += "RA: " + parts[2] + "\n";
         result += "Dec: " + parts[3] + "\n";
	} catch (Exception e) {
		e.printStackTrace();
	}
	System.out.println("result:::"+result);
         return result;
     }

     private String[] split(String s, String[] strings) {
    	 String[] splitOnce = s.split(strings[0]);
    	 List<String> result = new ArrayList<String>();
    	 for(int i = 0 ;  i< splitOnce.length; i++)
    	 {
    		 String[] splitTwice = splitOnce[i].split(strings[1]);
    		 for(int j = 0;j< splitTwice.length;j++)
    		 {
    			 if(splitTwice[j].trim().length()!=0)
    				 result.add(splitTwice[j]);
    		 }
    	 }
    	 String[] res = new String[result.size()];
		return  result.toArray(res);
	}

	public String resolveCoords(String ra, String dec,String radius)
     {
         String result = "";
         String script = "output console=off script=off\nformat object \"%D,%MAIN_ID,%COO(d;A,D),%DIST\"\nquery coo " + ra + " " + dec + " radius="+radius;
         
      try {
         String s = sendGet(BASE_URL + URLEncoder.encode(script,"UTF-8"),"");
         if (!s.startsWith("1,"))
         {
             
				throw new Exception("Nothing found.");
			
         }
         String[] parts = split(s,new String[] { ",", "\n" });
         
         result += "Name: " + parts[1] + "\n";
         result += "RA: " + parts[2] + "\n";
         result += "Dec: " + parts[3] + "\n";
         result += "Distance: " + parts[4] + "\n";
     } catch (Exception e) {
			e.printStackTrace();
		}
         return result;
     }
	 /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    @Test
    public void test()
    {
//    	String res = resolveCoords("57.50890","0.06656","0.5m");
    	String res = resolveCoords("57.5088996887207","0.06656374037265778","0.5m");
    	System.out.println(res);
    }
}
