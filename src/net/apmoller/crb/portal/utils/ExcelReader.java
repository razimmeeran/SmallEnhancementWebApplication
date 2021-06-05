package net.apmoller.crb.portal.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.apmoller.crb.portal.config.Constants;
import net.apmoller.crb.portal.executionEngine.TestRunner;

import org.apache.log4j.LogMF;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import net.apmoller.crb.portal.utils.Log;

public class ExcelReader {

	   public static FileInputStream ExcelFile;
       private static XSSFSheet ExcelWSheet;
       private static XSSFWorkbook ExcelWBook;
       private static XSSFCell Cell;
       private static XSSFRow Row;
      
       public static void setExcelFile(String Path){ 
 	     try {
          ExcelFile = new FileInputStream(Path);
         ExcelWBook = new XSSFWorkbook(ExcelFile);
 	     } catch (Exception e){
 	     Log.error("Class Utils | Method setExcelFile | Exception desc : "+e.getMessage());
 		 TestRunner.testStepResult = false;
     	}
 	  }

       public static String getCellData(int RowNum, int ColNum, String SheetName ) throws Exception{
         try{
    	ExcelWSheet = ExcelWBook.getSheet(SheetName);
      	Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
         String CellData = Cell.getStringCellValue();
         return CellData;
      }catch (Exception e){
          Log.error("Class Utils | Method getCellData | Exception desc : "+e.getMessage());
          TestRunner.testStepResult = false;
          return"";
          }
       }
      public static String readCelldata(String sheetName,int rowNumber, int colNumber) throws Exception{
		String celldata=null;
		ExcelWSheet = ExcelWBook.getSheet(sheetName);	
			if(ExcelWSheet.getRow(rowNumber)==null){
			celldata=" ";
		}
		else
		{
			if(ExcelWSheet.getRow(rowNumber).getCell(colNumber)==null)
			{		
				celldata="";		
			}
			else
			{
				if(ExcelWSheet.getRow(rowNumber).getCell(colNumber).getCellType()==XSSFCell.CELL_TYPE_NUMERIC)
				{
					celldata=String.valueOf(ExcelWSheet.getRow(rowNumber).getCell(colNumber).getNumericCellValue());					
				}
				else
				{
					celldata=ExcelWSheet.getRow(rowNumber).getCell(colNumber).getStringCellValue();
				}
			}
		}
      return celldata;		
      }
  
     public static int getRowCount(String SheetName){
		int iNumber=0;
		try {
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			iNumber=ExcelWSheet.getLastRowNum()+1;
			
		} catch (Exception e){
			Log.error("Class Utils | Method getRowCount | Exception desc : "+e.getMessage());
			TestRunner.testStepResult = false;
			}
		return iNumber;
		}

	public static int getRowContains(String sTestCaseName, int colNum,String SheetName) throws Exception{
		int iRowNum=0;	
		try {
			int rowCount = ExcelReader.getRowCount(SheetName);
			for (; iRowNum<rowCount; iRowNum++){
				if  (ExcelReader.getCellData(iRowNum,colNum,SheetName).equalsIgnoreCase(sTestCaseName)){
					break;
				}
			}       			
		} catch (Exception e){
			Log.error("Class Utils | Method getRowContains | Exception desc : "+e.getMessage());
			TestRunner.testStepResult = false;
			}
		return iRowNum;
		}

	public static int getTestStepsCount(String SheetName, String sTestCaseID, int iTestCaseStart) throws Exception{
		try {
			int counter=0;
			
 		for(int i=iTestCaseStart;i<ExcelReader.getRowCount(SheetName);i++){
         	if(sTestCaseID.equals(ExcelReader.getCellData(i, Constants.Col_TestCase_ID, SheetName))){
        		counter++;
 			}
 			
 			}
 		//ExcelWSheet = ExcelWBook.getSheet(SheetName);
 		//int number=ExcelWSheet.getLastRowNum()+1;
 		return counter;
		} catch (Exception e){
			Log.error("Class Utils | Method getRowContains | Exception desc : "+e.getMessage());
			TestRunner.testStepResult = false;
			return 0;
     }
	}

	@SuppressWarnings("static-access")
	public static void setCellData(String Result,  int RowNum, int ColNum, String SheetName) throws Exception    {
        try{

     	   ExcelWSheet = ExcelWBook.getSheet(SheetName);
            Row  = ExcelWSheet.getRow(RowNum);
            Cell = Row.getCell(ColNum, Row.RETURN_BLANK_AS_NULL);
            if (Cell == null) {
         	   Cell = Row.createCell(ColNum);
         	   Cell.setCellValue(Result);
             } else {
                 Cell.setCellValue(Result);
             }
              FileOutputStream fileOut = new FileOutputStream(Constants.Path_TestData);
              ExcelWBook.write(fileOut);
              fileOut.close();
              ExcelWBook = new XSSFWorkbook(new FileInputStream(Constants.Path_TestData));
          }catch(Exception e){
         	// DriverScript.testStepResult = false;
        	 Log.error("Class Utils | Method setCellData | Exception desc : "+e.getMessage());
         	 System.out.println(e.getMessage());

          }
     }

}