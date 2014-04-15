package com.ram.utils;

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

import com.ram.test.RunTests;

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
}
