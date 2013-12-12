package com.gravitant.utils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;

import com.gravitant.tests.RunTests;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class CSV_Reader{
	RunTests runTest = new RunTests();
	public String path = runTest.testCasePath;
	public String pathToXL = null;
	public  FileInputStream fileInputStream = null;
	public  FileOutputStream fileOut =null;
	
	public CSV_Reader()throws IOException{
		//this.path=path;
	}
	
	public int getRowCount(String testCaseName) throws IOException{
		int rowCount = 0;
		CSVReader reader = new CSVReader(new FileReader(testCaseName));
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			rowCount++;
	    }
	    reader.close();
	    //System.out.println(noOfSteps);
		return rowCount;
	}
	public int getColumnCount(String testCaseName) throws IOException{
		int columnCount = 0;
		CSVReader reader = new CSVReader(new FileReader(testCaseName));
		List<String[]> myDatas = reader.readAll();
		String[] line = myDatas.get(0);
		String[] nextLine;
		
	    reader.close();
	    //System.out.println(noOfSteps);
		return columnCount;
	}
	
	public ArrayList<String> getColumnData(String csvFilePath, String columnName) throws IOException{
		ArrayList<String> columnValues = new ArrayList<String>();
		int columnIndex = this.getColumnIndex(csvFilePath, columnName);
        CSVReader csvreader = new CSVReader(new FileReader(csvFilePath));
        String [] nextLine = null;
        while((nextLine = csvreader.readNext()) != null) {
        	columnValues.add(nextLine[columnIndex]);
        }
        columnValues.remove(0);
        //System.out.println(columnValues.toString());
        csvreader.close();
		return columnValues;
	}
	public int getColumnIndex(String csvFilePath, String columnName) throws IOException{
		CSVReader csvReader = new CSVReader(new FileReader(csvFilePath));
	    String [] headerLine = csvReader.readNext();
	    int columnIndex = 0;
	    for (int i = 0; i < headerLine.length; i++){
            if (headerLine[i].equals(columnName)){
            	columnIndex = i;
            }
	    }
	    csvReader.close();
		return columnIndex;
	}
	// returns true if data is set successfully else false
	/*public boolean setCellData(String sheetName,String colName,int rowNum, String data){
		try{
			fileInputStream = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fileInputStream);
			if(rowNum<=0)
				return false;
			
			int index = workbook.getSheetIndex(sheetName);
			int colNum=-1;
			if(index==-1)
				return false;
			
			sheet = workbook.getSheetAt(index);
			row=sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				//System.out.println(row.getCell(i).getStringCellValue().trim());
				if(row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum=i;
		}
		if(colNum==-1)
			return false;

		sheet.autoSizeColumn(colNum); 
		row = sheet.getRow(rowNum-1);
		if (row == null)
			row = sheet.createRow(rowNum-1);
		
		cell = row.getCell(colNum);	
		if (cell == null)
	        cell = row.createCell(colNum);

	    // cell style
	    //CellStyle cs = workbook.createCellStyle();
	    //cs.setWrapText(true);
	    //cell.setCellStyle(cs);
	    cell.setCellValue(data);
	    fileOut = new FileOutputStream(path);
		workbook.write(fileOut);
	    fileOut.close();	
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}*/
	
	// returns true if data is set successfully else false
	/*public boolean setCellData(String sheetName,String colName,int rowNum, String data,String url){
		//System.out.println("setCellData setCellData******************");
		try{
			fileInputStream = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fileInputStream);
			if(rowNum<=0)
				return false;
			
			int index = workbook.getSheetIndex(sheetName);
			int colNum=-1;
			if(index==-1)
				return false;
			
			sheet = workbook.getSheetAt(index);
			//System.out.println("A");
			row=sheet.getRow(0);
			for(int i=0;i<row.getLastCellNum();i++){
				//System.out.println(row.getCell(i).getStringCellValue().trim());
				if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
					colNum=i;
			}
		
			if(colNum==-1)
				return false;
			sheet.autoSizeColumn(colNum); 
			row = sheet.getRow(rowNum-1);
			if (row == null)
				row = sheet.createRow(rowNum-1);
			
			cell = row.getCell(colNum);	
			if (cell == null)
		        cell = row.createCell(colNum);
				
		    cell.setCellValue(data);
		    XSSFCreationHelper createHelper = workbook.getCreationHelper();

		    //cell style for hyperlinks
		    //by default hypelrinks are blue and underlined
		    CellStyle hlink_style = workbook.createCellStyle();
		    XSSFFont hlink_font = workbook.createFont();
		    hlink_font.setUnderline(XSSFFont.U_SINGLE);
		    hlink_font.setColor(IndexedColors.BLUE.getIndex());
		    hlink_style.setFont(hlink_font);
		    //hlink_style.setWrapText(true);
	
		    XSSFHyperlink link = createHelper.createHyperlink(XSSFHyperlink.LINK_FILE);
		    link.setAddress(url);
		    cell.setHyperlink(link);
		    cell.setCellStyle(hlink_style);
		      
		    fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
		    fileOut.close();	
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}*/
	
	// returns true if sheet is created successfully else false
	/*public boolean addSheet(String  sheetname){		
		
		FileOutputStream fileOut;
		try {
			 workbook.createSheet(sheetname);	
			 fileOut = new FileOutputStream(path);
			 workbook.write(fileOut);
		     fileOut.close();		    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}*/
	
	// returns true if sheet is removed successfully else false if sheet does not exist
	/*public boolean removeSheet(String sheetName){		
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1)
			return false;
		
		FileOutputStream fileOut;
		try {
			workbook.removeSheetAt(index);
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
		    fileOut.close();		    
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
		return true;
	}*/
	// returns true if column is created successfully
	/*public boolean addColumn(String sheetName,String colName){
		//System.out.println("**************addColumn*********************");
		try{				
			fileInputStream = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fileInputStream);
			int index = workbook.getSheetIndex(sheetName);
			if(index==-1)
				return false;
			
			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			
			sheet=workbook.getSheetAt(index);
			row = sheet.getRow(0);
			if (row == null)
				row = sheet.createRow(0);
			
			//cell = row.getCell();	
			//if (cell == null)
			//System.out.println(row.getLastCellNum());
			if(row.getLastCellNum() == -1)
				cell = row.createCell(0);
			else
				cell = row.createCell(row.getLastCellNum());
		        cell.setCellValue(colName);
		        cell.setCellStyle(style);
		        fileOut = new FileOutputStream(path);
				workbook.write(fileOut);
			    fileOut.close();		    
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}*/
	
	//removes a column and all the column's contents
	/*public boolean removeColumn(String sheetName, int colNum) {
		try{
			if(!findIfSheetExists(sheetName))
				return false;
			fileInputStream = new FileInputStream(path); 
			workbook = new XSSFWorkbook(fileInputStream);
			sheet=workbook.getSheet(sheetName);
			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			XSSFCreationHelper createHelper = workbook.getCreationHelper();
			style.setFillPattern(HSSFCellStyle.NO_FILL);
			for(int i =0;i<getRowCount(sheetName);i++){
				row=sheet.getRow(i);	
				if(row!=null){
					cell=row.getCell(colNum);
					if(cell!=null){
						cell.setCellStyle(style);
						row.removeCell(cell);
					}
				}
			}
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
		    fileOut.close();
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}*/

	// returns number of columns in a sheet	
	/*public int getColumnCount(String sheetName){
		// check if sheet exists
		if(!findIfSheetExists(sheetName))
		 return -1;
		
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);
		
		if(row==null)
			return -1;
		
		return row.getLastCellNum();
	}*/

	/*public int getCellRowNum(String sheetName,String colName,String cellValue){
		for(int i=2;i<=getRowCount(sheetName);i++){
	    	if(getCellData(sheetName,colName , i).equalsIgnoreCase(cellValue)){
	    		return i;
	    	}
	    }
		return -1;
	}*/
	
	//stand alone runner
	/*public static void main(String arg[]) throws IOException{
		CSV_Reader datatable = new CSV_Reader("C:\\Users\\Ramkanth Manga\\Documents\\Automation\\FunctionalAutomation\\TestInputMethods\\Webdriver-PageFactory\\Scenarios_Suite_WebDriver.xlsx");
		for(int col=0 ;col< datatable.getColumnCount("Test Cases"); col++){
				System.out.println(datatable.getCellData("Test Cases", col, 1));
		}
		datatable.setCellData("Test_Steps","Result", 2, "PASS");
		
	}*/
}
