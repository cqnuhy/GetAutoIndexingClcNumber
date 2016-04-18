package com.parse.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* 文件名称：MyUtil.java<br>
* 摘要：公用工具类，用于获取HTTP请求对象，文件读写，XML解析<br>
* -------------------------------------------------------<br>
* 当前版本：1.1.1<br>
* 作者：丁亮<br>
* 完成日期：2016年2月24日<br>
* -------------------------------------------------------<br>
* 取代版本：1.1.0<br>
* 原作者：丁亮<br>
* 完成日期：2016年2月24日<br>
 */
public class CommonUtil {

	// 获取一个普通HTTP请求连接，Need to close after the connection be used;
	public static HttpURLConnection getHttpConnection(String url,String action) throws IOException{
		URL u = null;
		HttpURLConnection connection = null;
		u = new URL(url);
		connection = (HttpURLConnection) u.openConnection(); 
		// 设置通用的请求属性
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestMethod(action);
        
		return connection;
	}
	
	// 读取文件字符流信息
	public static BufferedReader getFileBuffer(String fileName) throws FileNotFoundException{
		File file = new File(fileName);
		InputStream in = new FileInputStream(file);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return br;
	}
	
	// 向文件写入数据
	public static boolean writeFile(String word,String result,String fileType,boolean append) throws FileNotFoundException{
		File file = new File(word+"."+fileType);
		if(file.getParentFile() != null){
			file.getParentFile().mkdirs();
		}
		OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file, append));
		BufferedWriter bw = new BufferedWriter(write);
        try {
        	bw.write(result);
        	bw.flush(); 
        	write.close();
			bw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
        return false;
	}
	
	// 将文件内容转化为文本
	public static String getFileContextStr(String fileName){
		String result = "";
		BufferedReader br = null;
		try {
			br = CommonUtil.getFileBuffer(fileName);
			String line;
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		result = result.replaceAll("\r", "");
		return result;
	}
	// parse XML by java API
//	public static void parseXMLByJDKAPI(String protocolXML) {   
//        
//        try {   
//             DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
//             DocumentBuilder builder = factory.newDocumentBuilder();   
//             org.w3c.dom.Document doc = builder.parse(new InputSource(new StringReader(protocolXML)));   
//             org.w3c.dom.Element root = doc.getDocumentElement();   
//         } catch (Exception e) {   
//             e.printStackTrace();   
//         }   
//     } 
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}
