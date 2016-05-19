package com.parse.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.parse.lib.ExcelUtil;

/**
* Copyright (c) 2016,HUYI<br>
* All rights reserved.<br>
* 
* 文件名称：MainTest.java<br>
* 摘要：统计分类号 下面的句子和分类号出现的频率<br>
* -------------------------------------------------------<br>
* 当前版本：1.1.1<br>
* 作者：HUYI<br>
* 完成日期：2016-3-20<br>
* -------------------------------------------------------<br>
* 取代版本：1.1.0<br>
* 原作者：HUYI<br>
* 完成日期：2016-3-20<br>
 */
public class MainTest {
	
	private static final String suffix = "ClcNumbers："; 
	
	/**
	 * @date 2016-3-15 上午12:02:51
	 * @author HUYI
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		// 保存当前分类结果
		Map<String,List<String>> result = new HashMap<String, List<String>>();
		// to save clcList
		List<String> clcList = null;
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		while (true) {
			showMenu(1);
			String floderPath = sc.nextLine().trim();
			File floder = new File(floderPath);
			if(floder.isDirectory()){
				System.out.println("开始读取文件列表..."+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				long start = System.currentTimeMillis();
				// 获取结果失败的参数
				StringBuilder failedStr = new StringBuilder();
				File[] files = floder.listFiles();
				String thisFileName = new String();
				for (int i = 0; i < files.length; i++) {
					String fileName = files[i].getName();
					if(!(fileName.endsWith("txt"))){
						continue;
					}else{
						thisFileName = files[i].getAbsolutePath();
					}
					if(!fileName.equals("train.zh.txt")){
						continue;
					}
					System.out.println("开始解析"+fileName+"内容..");
					BufferedReader br = CommonUtil.getFileBuffer(files[i].getAbsolutePath());
					String line = null;
					int index =1;
					while ((line = br.readLine()) != null) {
						// 获取分类号
						if(line.equals("")){continue;}
						System.out.println("获取第"+(index)+"条结果...");
						String clc = line.substring(line.indexOf(suffix)+11, line.length()-1);
						String[] clcs = clc.split(";");
						for (String c : clcs) {
							if(result.containsKey(c)){
								clcList = result.get(c);
								
							}else{
								clcList = new ArrayList<String>();
							}
							clcList.add(index+":"+line);
							result.put(c, clcList);
						}
						index++;
						try {
						} catch (Exception e) {
							failedStr.append(line+"、");
						}
					}
					
					if(failedStr.length()>0){
						System.out.println(fileName+"解析完成，其中"+failedStr.toString()+"参数获取结果出错。");
						CommonUtil.writeFile(fileName+"错误日志", failedStr.toString(), "log", true);
					}
					// 统计每个分类号下包含的句子，写入txt
					String absoluteFile = thisFileName+".clc.txt";
					File file = new File(absoluteFile);
					if(file.getParentFile() != null){	file.getParentFile().mkdirs();	}
					OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file, true));
					BufferedWriter bw = new BufferedWriter(write);
					try {
						for (Entry<String, List<String>> map : result.entrySet()) {
							CommonUtil.writeFile(write, bw, map.getKey()+"\r\n");
							for (String context : map.getValue()) {
								String sub =context.substring(0, context.indexOf(suffix)-1);
								CommonUtil.writeFile(write, bw, sub+"\r\n");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						if(null!=write){
							write.close();
						}
						if(null!=bw){
							bw.close();
						}
					}
//					for (Entry<String, List<String>> map : result.entrySet()) {
//						CommonUtil.writeFile(thisFileName+".clc", map.getKey()+"\r\n", "txt", true);
//						for (String context : map.getValue()) {
//							String sub =context.substring(0, context.indexOf(suffix)-1);
//							CommonUtil.writeFile(thisFileName+".clc", sub+"\r\n", "txt", true);
//						}
//					}
					// 统计每个分类号出现的次数，写入excel
					ExcelUtil.exportExcel(result, thisFileName.substring(0, thisFileName.lastIndexOf("."))+".统计表.xls");
				}
				
				System.out.println("所有文件解析完成，共耗时："+(System.currentTimeMillis()-start)/(1000*60)+"分钟");
			}else{
				System.out.println("无法找到输入的文件夹，请重新输入:");
			}
		}
	}
	
	/**
	 * 
	 * @date 2016-3-20 上午1:04:56
	 * @author HUYI 
	 * @param i 输入步骤
	 */
	private static void showMenu(int i) {
		switch (2) {
		case 1:
			System.out.println("1、扩展词汇");
			System.out.println("2、获取分类号");
			break;
		case 2:
			System.out.println("请输入所在需要检索的文件文件夹路径：");
			break;

		default:
			break;
		}
		
	}

}
