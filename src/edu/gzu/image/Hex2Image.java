package edu.gzu.image;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 十六进制转成图片
 * @author Administrator
 *
 */
public class Hex2Image {
    public static void main(String[] args) throws Exception {
        Hex2Image to=new Hex2Image();
        InputStream is=new FileInputStream("C:\\Users\\zhouyu\\Desktop\\spec.txt");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String str = null;
        StringBuilder sb = new StringBuilder();
        while ((str = br.readLine()) != null) {
            System.out.println(str);
            sb.append(str);
        }
        to.saveToImgFile(sb.toString().toUpperCase(),"C:\\Users\\zhouyu\\Desktop\\spec.gif");
    }
    public void saveToImgFile(String src,String output){
    	
           if(src==null||src.length()==0){
               return;
           }
           try{
               FileOutputStream out = new FileOutputStream(new File(output));
               byte[] bytes = src.getBytes();
               System.out.println(bytes.length);
               for(int i=0;i<bytes.length;i+=2){ 
            	   out.write(charToInt(bytes[i])*16+charToInt(bytes[i+1]));
               }
               out.close();
           }catch(Exception e){
               e.printStackTrace();
           }
       }
       private int charToInt(byte ch){
           int val = 0;
           if(ch>=0x30&&ch<=0x39){
               val=ch-0x30;
           }else if(ch>=0x41&&ch<=0x46){
               val=ch-0x41+10;
           }
           return val;
       }
       /**
        * 
        * @param hexStr 一个十六进制的字符串，
        * @return
        */
       public InputStream hex2Binary(String hexStr)
       {
    	   if(hexStr==null||hexStr.trim().isEmpty())
    		   return null;
    	   hexStr = hexStr.startsWith("0x")? hexStr.substring(2).toUpperCase():hexStr.toUpperCase();
           byte[] bytes = hexStr.getBytes();
           byte[] dest = new byte[bytes.length/2];
           System.out.println("img.getBytes().length:"+bytes.length);
           for(int i=0;i<bytes.length;i+=2){
        	   dest[i/2] = (byte)(charToInt(bytes[i])*16+charToInt(bytes[i+1]));
           }
           ByteArrayInputStream input = new ByteArrayInputStream(dest);
           
    	   return input;
       }
}
