package com.parse.lib;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelUtil{

	public static boolean exportExcel(Map<String,List<String>> map,String fileName) {		//创建excel文件对象
		HSSFWorkbook wb = new HSSFWorkbook();
		//创建一个张表
		Sheet sheet = wb.createSheet();
		//创建第一行(head)
		Row row0 = sheet.createRow(0);
		// 文件头字体
		Font font0 = createFonts(wb, Font.BOLDWEIGHT_BOLD, "宋体", false,(short) 200);
		Font font1 = createFonts(wb, Font.BOLDWEIGHT_NORMAL, "宋体", false,(short) 200);
		//给第二行添加文本
		createCell(wb, row0, 0, "中图分类号", font0);
		createCell(wb, row0, 1, "中文释义", font0);
		createCell(wb, row0, 2, "句子个数", font0);
		// 数据写入		
		int index =1;
		for (Entry<String, List<String>> m :map.entrySet()) {
			//创建一行
			Row rowData = sheet.createRow(index++);
			createCell(wb, rowData, 0, m.getKey(), font1);
			createCell(wb, rowData, 1, "", font1);
			createCell(wb, rowData, 2, m.getValue().size()+"", font1);
	
		}
		OutputStream out;
		try {
			out = new FileOutputStream(fileName);
			try {
				wb.write(out);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}	

/**
	 * 创建单元格并设置样式,值
	 * 
	 * @param wb
	 * @param row
	 * @param column
	 * @param
	 * @param
	 * @param value
	 */
	public static void createCell(Workbook wb, Row row, int column,
			String value, Font font) {
		Cell cell = row.createCell(column);
		cell.setCellValue(value);
//		CellStyle cellStyle = wb.createCellStyle();
//		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
//		cellStyle.setFont(font);
//		cell.setCellStyle(cellStyle);
	}

	/**
	 * 设置字体
	 * 
	 * @param wb
	 * @return
	 */
	public static Font createFonts(Workbook wb, short bold, String fontName,
			boolean isItalic, short hight) {
		Font font = wb.createFont();
		font.setFontName(fontName);
		font.setBoldweight(bold);
		font.setItalic(isItalic);
		font.setFontHeight(hight);
		return font;
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str == null || "".equals(str.trim()) || str.length() > 10)
			return false;
		Pattern pattern = Pattern.compile("^0|[1-9]\\d*(\\.\\d+)?$");
		return pattern.matcher(str).matches();
	}

}
