package net.apmoller.crb.portal.executionEngine;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

//import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.aventstack.extentreports.Status;
import net.apmoller.crb.portal.config.ExtentManager;
import net.apmoller.crb.portal.config.TestActions;
import net.apmoller.crb.portal.config.Constants;
import net.apmoller.crb.portal.utils.ExcelReader;
import net.apmoller.crb.portal.utils.Log;
import net.apmoller.crb.portal.utils.ObjectReader;


public class TestRunner  {
	
	
	public static TestActions testActions;
	public static String sTestActions;
	public static String sPageObject;
	public static String sPageName;
	public static Method method[]; 
	public static int col_TestData;
	public static int iTestStep;
	public static int iTestLastStep;
	public static String sTestCaseID;
	public static String testName;
	public static String sTestStepName;		
	public static String sRunMode;
	public static String sData;
	public static boolean testCaseStatus;
	public static boolean testStepResult;
	public static boolean loginResult;
	public static boolean browserResult;
	public static boolean testcaseResult;
	public static boolean  isAllAvailable = false;
	public static String  imagePath = null;
	public static String  stepException  = null;
	public static String  beforeTestException = null;		
	public static String  failedMessage = null;
	public static Logger log_TestRunner;
	public static ExtentReports htmlReport ;	
	public static ExtentTest reportLogger;
	public static  String browser;
	public static  String env;
	public static  String brand;
	public static  String url;
	public static  String username;
	public static  String password; 
	public static  String customerCode;	
	public static String bookingNum=null;
	
	
	public TestRunner() throws NoSuchMethodException, SecurityException, IOException
	{
	//	super();
		testActions = new TestActions();
		method = testActions.getClass().getMethods();	
		Log.loadLog4j("Log4j.xml");
    	log_TestRunner = Log.getLog(TestRunner.class.getName());
    	ExcelReader.setExcelFile(Constants.Path_TestData);    
   	//htmlReport = ExtentManager.getReporter(System.getProperty("user.dir")+"\\Reports\\Report.html");
   //  	testStepResult = true;
   //  	loginResult = false;
    // 	browserResult = false;
    	ObjectReader.readObjRepository();	
    			
    }
	
	
	@BeforeTest
	@Parameters({"Browser"})
	public void beforeSuite(String broswer) throws Exception
	{
		//htmlReport = ExtentManager.getReporter(System.getProperty("user.dir")+"\\Reports\\Report.html");	
		String timeStamp = new SimpleDateFormat("MMddyyyy_HHmmss").format(Calendar.getInstance().getTime());
		htmlReport = ExtentManager.getReporter(System.getProperty("user.dir")+"\\Reports\\Report"+timeStamp+".html");
		/*htmlReport = ExtentManager.getReporter(System.getProperty("D:\\Reports\\Report"+timeStamp+".html"));*/
		SimpleDateFormat formDate = new SimpleDateFormat("dd/MM/yyyy");

	       // String strDate = formDate.format(System.currentTimeMillis()); // option 1
	       String strDate = formDate.format(new Date()); // option 2
	      
		
		/*TestActions.openBrowser(broswer);*/
	}

	
	@BeforeClass
	@Parameters({"Browser","ENV","Brand","Url","userName","Password","customerCode"})
	public void beforeClass(String broswer1,String env , String brand, String url, String username, String password, String customerCode) throws Exception
	{
		
		/*if(TestRunner.browserResult==true)
		{		
		log_TestRunner.info("Before login method");
		TestActions.login(broswer1, env, brand, url, username,  password,  customerCode);		
		if(loginResult==true && broswer1.equals("IE") && !env.equals("dev3"))		
		TestActions.changeToEnglish( "language_menu_English",  " ",   "Login",  "Change to English");			
		}
		else
		log_TestRunner.error("Error in opening browser  ");*/
			}
	@AfterSuite
	@Parameters({"Browser"})
	public void afterSuite() throws Exception
	{
		htmlReport.flush();
	}

	@AfterClass
	@Parameters({"ENV","userName","Password","customerCode"})
	public void afterClass() throws Exception
	{
	/*TestActions.logout("link_logout",  " ",   "Dashboard_Page",  "Logout from the application");
		TestActions.close_Browser();*/
		
	}
	@Parameters({"Browser","Execution","GridURL","Brand","ENV", "Url","userName","Password","customerCode"})
    @Test  
    public void regressionSuite(String browser,String executionMode, String gridURL, String brand , String env, String url, String username, String password, String customerCode) throws Exception 
    {
  
      try 
      {    
            
          TestRunner startEngine = new TestRunner();
          startEngine.execute_TestCase(browser,executionMode, gridURL, brand, env, url, username, password, customerCode); 
    
    
      }
       catch(Exception e)
      {
          log_TestRunner.error("Error in regressionSuite  is  = " +e.getMessage());
    
      }
    
    }

	/*@Parameters({"Browser","Brand","ENV", "Url","userName","Password","customerCode"})
	@Test	
	
	public void regressionSuite(String browser, String brand , String env, String url, String username, String password, String customerCode) throws Exception 
	{
     
	  try 
	  {	
		  
		TestRunner startEngine = new TestRunner();
		startEngine.execute_TestCase(browser, brand, env, url, username, password, customerCode); 
	
	
	  }
	   catch(Exception e)
	  {
		log_TestRunner.error("Error in regressionSuite  is  = " +e.getMessage());
	 
	  }
	
	}
	*/
	private void execute_TestCase(String browser,String executionMode, String gridURL, String brand, String env, String url, String username, String password, String customerCode) throws Exception

    
    {

	   	int iTotalTestCases = ExcelReader.getRowCount(Constants.Sheet_TestCases);
	   	//TestActions.open_ChromeBrowser();
	 	log_TestRunner.info("iTotalTestCases "+iTotalTestCases);	
		log_TestRunner.info("Executing all test cases ");	
	 	
		//Iterating the Test Cases from the "Test Cases" sheet of Data Engine
	       	
	  	//Setting the value of testStepResult variable to 'true' before starting every test case
	     	
		  for(int iTestcase=1;iTestcase<=iTotalTestCases;iTestcase++)
	       	{	       		
			  testcaseResult = true;	     
	       	
			sRunMode = ExcelReader.getCellData(iTestcase,Constants.Col_RunMode,Constants.Sheet_TestCases);
					
			
		//	Check on the RunMode value for true
			if (sRunMode.equalsIgnoreCase("Yes"))
			{
				sTestCaseID = ExcelReader.getCellData(iTestcase,Constants.Col_TestCase_ID, Constants.Sheet_TestCases);									
				testName = ExcelReader.getCellData(iTestcase,Constants.Col_TestCaseName,Constants.Sheet_TestCases);					
						
				log_TestRunner.info("RunMode for test case "+sTestCaseID+ " is "+sRunMode);
				
				reportLogger= htmlReport.startTest(testName + " - " +brand+" - "+browser).assignCategory(brand+" - "+browser );				
				Log.startTestCase(sTestCaseID);			
				log_TestRunner.info("Executing Yes run mode test case "+sTestCaseID);		
//				
				  iTestStep = ExcelReader.getRowContains(sTestCaseID,Constants.Col_TestCase_ID, Constants.Sheet_TestSteps);
				  iTestLastStep = ExcelReader.getTestStepsCount(Constants.Sheet_TestSteps, sTestCaseID, iTestStep);
				
				  log_TestRunner.info("iTestStep "+iTestStep);
				  log_TestRunner.info("iTestLastStep "+iTestLastStep);
	     		  
  //	        Iterating the Test Steps for each Test Case
				  
				  testActions.openBrowser(browser, executionMode, gridURL);
				  
				  
				  
				for (int stepRepeater=iTestStep;stepRepeater<iTestStep+iTestLastStep;stepRepeater++)
				{
					testStepResult=true;
					log_TestRunner.info("Executing TestStep "+stepRepeater);
					
					sTestStepName = ExcelReader.getCellData(stepRepeater, Constants.Col_TestStepName,Constants.Sheet_TestSteps);
					sTestActions = ExcelReader.getCellData(stepRepeater, Constants.Col_Keyword,Constants.Sheet_TestSteps);
		    		sPageObject = ExcelReader.getCellData(stepRepeater, Constants.Col_PageObject, Constants.Sheet_TestSteps);
		    		sPageName = ExcelReader.getCellData(stepRepeater, Constants.Col_PageName, Constants.Sheet_TestSteps);
		    		switch(env)
		    		{		
		    				
		    		case "PP" :	   col_TestData = Constants.Col_PPTestData; break;			    			
		    		/*case "sealand" :	col_TestData = Constants.Col_SealandTestData;	break;		
		    		case "Seagoline" :	col_TestData = Constants.Col_SeaGoTestData;	break; 		
		    		case "mcc" :	col_TestData = Constants.Col_MccTestData;	break;  */  			
		    	   		
	    		    default : col_TestData = Constants.Col_SITTestData;  break;       		
	    		   		 		
	    		    
		    		}		
		    		
		    		sData = ExcelReader.getCellData(stepRepeater, col_TestData, Constants.Sheet_TestSteps);
	  	
//				     Calls the TestActions class to execute the keyword-functions
		    		TestRunner.execute_Actions(); 
		    		
		    		
//		    		Check on each Test Step and posting the result back to the Excel
		    		if(testStepResult==true)
		    		{	    		
		    			testcaseResult = true;
		    			 log_TestRunner.info(sTestStepName +" Passed");	
		    			 
		    			
		    			 reportLogger.log(LogStatus.PASS, sTestStepName, Constants.KEYWORD_PASS);
		    			
		    			 TestRunner.isAllAvailable = false;
		    	
						 ExcelReader.setCellData(Constants.KEYWORD_PASS,stepRepeater,Constants.Col_TestStepResult,Constants.Sheet_TestSteps); 					 
						//break;			    
		    		}   		
		    		
		    		else;
		    			if(testStepResult==false)	    		
		    		{	    		
		    			 testcaseResult = false;
		    			 log_TestRunner.error(sTestStepName +" Failed");
		    			 if(TestRunner.stepException != null)	
		    				 
		    			 {
			    			  if(TestRunner.stepException.length() > 100) 
		    				 reportLogger.log(LogStatus.FAIL, sTestStepName, TestRunner.stepException.substring(0, 100));	
			    			  else
			    			  reportLogger.log(LogStatus.FAIL, sTestStepName, TestRunner.stepException);
		    			 }
			    			 else if(TestRunner.failedMessage != null)		
			    			 reportLogger.log(LogStatus.FAIL, sTestStepName, failedMessage);	
		    			 reportLogger.log(LogStatus.FAIL, sTestStepName, Constants.KEYWORD_FAIL );
			    			
		    			 ExcelReader.setCellData(Constants.KEYWORD_FAIL,stepRepeater,Constants.Col_TestStepResult,Constants.Sheet_TestSteps);
		    			 
		    			 TestRunner.stepException = null;		
		    			 TestRunner.failedMessage = null;
		    			    break; 
	    			
		    		}	 
		    	
				}
				/*testActions.logout("", "", "", "");
				System.out.println("Logout successfully");*/
				testActions.close_Browser();
				
				
				if(testcaseResult==false)
				{
					
					log_TestRunner.info(sTestCaseID +" Failed");
						
					ExcelReader.setCellData(Constants.KEYWORD_FAIL,iTestcase,Constants.Col_Result,Constants.Sheet_TestCases);				
				//break;
   			
					}  	
				//This will only execute after the last step of the test case, if value is not 'false' at any step	
			    if(testcaseResult==true)
			   {				
			    	log_TestRunner.info(sTestCaseID +" passed");
						
			       ExcelReader.setCellData(Constants.KEYWORD_PASS,iTestcase,Constants.Col_Result,Constants.Sheet_TestCases);            
			      
			   }	
			    
				Log.endTestCase(sTestCaseID);
				
				htmlReport.endTest(reportLogger);
				
														
	       	} //if loop of to check run mode 
		
		}	
			/*testActions.logout("", "", "", "");
		  System.out.println("Logout successfully");*/
		  //testActions.close_Browser();
	     //TestActions.close_Browser();
	}
//       Calls the TestActions class to execute the keyword-functions
 
    private static void execute_Actions() throws Exception
    {
		for(int i=0;i<method.length;i++)
		{
			if(method[i].getName().equals(sTestActions))
			{
				testActions.AcceptCookies("","","",""); 
				method[i].invoke(testActions,sPageObject,sData,sPageName,sTestStepName);
				break;	
				
            }
			
		}
    }
}
	