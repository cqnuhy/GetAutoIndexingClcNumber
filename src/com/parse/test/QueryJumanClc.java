package com.parse.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QueryJumanClc {
	
	final String  CATEGORY = "category:";
	final String DUMAIN = "domain:";

	// 保存原始句子，方便将clc写到句子后面
	private List<String> list_ja = new ArrayList<String>();
	
	public QueryJumanClc() throws IOException {
		String basicPath = System.getProperty("user.dir")+File.separator;
		System.out.println("请输入文件前缀...");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String fileprev = sc.nextLine();
		String ja_filename = basicPath+fileprev+".ja";
		String juman_filename =  basicPath+fileprev+"_juman.txt";
		System.out.println("你输入的是"+fileprev+"...文件解析中...");
		long starttime = System.currentTimeMillis();
		BufferedReader ja_reader = CommonUtil.getFileBuffer(ja_filename);
		String ll = null;
		while ((ll = ja_reader.readLine())!=null) {
			list_ja.add(ll);
		}
		BufferedReader juman_reader = CommonUtil.getFileBuffer(""+juman_filename);
		String line = null;
		int index = -1; // 记录EOS 标记，对应 list_ja索引
		Set<String> clcNumberList = new HashSet<String>();
		
		while ((line = juman_reader.readLine()) != null) {
			if(!line.equals("EOS")){
				int categoryIndex = line.indexOf(CATEGORY);
				int dumianIndex = line.indexOf(DUMAIN);
				if(-1!=categoryIndex){
					String result = new String(); 
					Pattern cp = Pattern.compile(CATEGORY+"(.*)[ ]");
					Matcher m = cp.matcher(line);
					if(m.find()){
						result = m.group().trim();
						if(result.contains(DUMAIN)){
							result = result.substring(0, result.indexOf(DUMAIN)).trim();
						}
						result = result.substring(CATEGORY.length(),result.length());
					}else{
						result = line.substring(categoryIndex+CATEGORY.length(), line.lastIndexOf("\""));
					}
					String[] results = this.split(result);
					for (String str : results) {
						clcNumberList.add(str);
					}
 				 }
				 
				 if(-1!=dumianIndex){
					String result = new String();
					Pattern cp = Pattern.compile(DUMAIN+"(.*)[ ]");
					Matcher m = cp.matcher(line);
					if(m.find()){
						result = m.group().trim();
						if(result.contains(CATEGORY)){
							result = result.substring(0, result.indexOf(CATEGORY)).trim();
						}
						result = result.substring(DUMAIN.length(),result.length());
					}else{
						result = line.substring(dumianIndex+DUMAIN.length(), line.lastIndexOf("\""));
					}
					String[] results = this.split(result);
					for (String str : results) {
						clcNumberList.add(str);
					}
				 }
			}else{
				index++;
				String indexStr = list_ja.get(index);
				indexStr += "(clcNumber:"+listToString(clcNumberList)+")";
				clcNumberList.clear();
				list_ja.remove(index);
				list_ja.add(index, indexStr);
				long modtime = System.currentTimeMillis();
				if((modtime-starttime)/(1000*60)==10){
					System.out.println("已经执行了10分钟了...");
				}
			}
		}
		// write to file when this loop is over 
		long endtime = System.currentTimeMillis();
		System.out.println("开始写入文件...");
		for (String item : list_ja) {
			CommonUtil.writeFile(ja_filename, item+"\r\n", "txt", true);
//			for (String context : map.getValue()) {
//				String sub =context.substring(0, context.indexOf(suffix)-1);
//				CommonUtil.writeFile(thisFileName+".clc", sub+"\r\n", "txt", true);
//			}
		}
		System.out.println("写入文件耗时："+(System.currentTimeMillis()-endtime)+"毫秒");
	}
	
	private String[] split(String str){
		return str.split(";");
	}
	
	private String listToString(Set<String> set){
		String tmp = "";
		for (String str : set) {
			tmp += str+",";
		}
		return  tmp.equals("")?"":tmp.substring(0, tmp.length()-1);
	}
	
	public static void main(String[] args) {
		try {
			new QueryJumanClc();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
