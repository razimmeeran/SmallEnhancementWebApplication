package net.apmoller.crb.portal.config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Constants {
	//System Variables
  	public static final String URL = "http://myt.apmoller.net";
  	
  	
  	
  	public static final String Env = "Test Environment";
  	//SIT
  	//public static final String Path_TestData = System.getProperty("user.dir")+ File.separator + "resources"+ File.separator + "dataEngine" + File.separator + "SSP_Regression.xlsx";  	
 
  	
 	//SIT-saran
  	//public static final String Path_TestData = System.getProperty("user.dir")+ File.separator + "resources"+ File.separator + "dataEngine" + File.separator + "SSP_Regression_saran.xlsx";  	
 
  	
	//SIT-OD3CP
  	//public static final String Path_TestData = System.getProperty("user.dir")+ File.separator + "resources"+ File.separator + "dataEngine" + File.separator + "SSP_Regression_SIT_OD3CP_final.xlsx";  	

  	
  //SIT-OD3CP-FLAG-CONSOLIDATE
  	public static final String Path_TestData = System.getProperty("user.dir")+ File.separator + "resources"+ File.separator + "dataEngine" + File.separator + "SSP_Regression_SIT_flag_CONSOLIDATE.xlsx";  	

  	
  	
  	// for PP
  	public static final String Path_TestData_PP = System.getProperty("user.dir")+ File.separator + "resources"+ File.separator + "dataEngine" + File.separator + "SSP_Regression_PP.xlsx";
  	
  	public static final String Path_TestData_PP_AkmaiChangeTest = System.getProperty("user.dir")+ File.separator + "resources"+ File.separator + "dataEngine" + File.separator + "SSP_Regression_PP - AkmaiChangeTest.xlsx";
  	
  	
 /// for PP -OD3CP
   	//public static final String Path_TestData_PP = System.getProperty("user.dir")+ File.separator + "resources"+ File.separator + "dataEngine" + File.separator + "SSP_Regression_PP_OD3CP.xlsx";
   	
   	
 
  	
    // Object Repository
    public static final String Path_ObjectRepo = System.getProperty("user.dir")+ File.separator + "resources" + File.separator + "objRepo" + File.separator + "SSP.properties";
    public static final String Path_ChromeDriver = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "drivers" + File.separator + "chromedriver.exe";
    public static final String Path_IEDriver = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "objRepo" + File.separator + "drivers" + File.separator + "IEDriverServer.exe";
    public static final String Path_geckoDriver = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "drivers" + File.separator + "geckodriver.exe";

	public static final String KEYWORD_FAIL = "FAIL";
	public static final String KEYWORD_PASS = "PASS";
	//Data Sheet Column Numbers
	public static final int Col_TestCase_ID = 0;
	//public static final int Col_TestStepID = 1;
	public static final int Col_TestStepName = 1;
	public static final int Col_PageObject = 2;
	public static final int Col_PageName = 3;
	public static final int Col_Keyword = 4;
	/*public static final int Col_MymlTestData = 5;*/
	public static final int Col_SITTestData = 5;
	public static final int Col_PPTestData = 6;
	
	/*public static final int Col_SafmarineTestData = 6;*/
	public static final int Col_SeaGoTestData = 7;
	public static final int Col_SealandTestData = 8;
	public static final int Col_MccTestData = 9;
	public static final int Col_TestStepResult = 10;
	
	//public static final int Col_Desc = 1;
	public static final int Col_TestCaseName = 1;
	public static final int Col_RunMode = 2;
	public static final int Col_Result = 3;		
	
	//List of Data Engine Excel sheets
	public static final String Sheet_TestSteps = "Test Steps";
	public static final String Sheet_TestCases = "Test Cases";
	public static final String Sheet_TestData="TestData";
	
	public static final String Sheet_config = "config";
	public static final String SITECODE_MAERSKLINE = "MAEU";
	public static final String SITECODE_SAFMARINE = "SAFM";
	public static final String SITECODE_MCC = "MCPU";
	public static final String SITECODE_SEALAND = "SEAU";
	public static final String SITECODE_SEAGOLINE = "SEJJ";
	public static final String Track_Page_URL="https://myt.apmoller.net/shipmentbinder/";
	public static final String NewBooking_Page_URL="https://myt.apmoller.net/booking/new";
	public static final String NewBooking_Existing_URL="https://myt.apmoller.net/shipmentoverview/duplicate";
	public static String Booking_Number=null;
	public static String Current_Date=null;
	public static String radioBtnAttributeValue=null;
	public static final String userName="testuser21@xleap";
	public static final String passWord="xleap123";
	public static final String Browser="Mozilla";
	public static final String CustmerCode="10000007951";
	public static final String Url="https://myt.apmoller.net/login";
	public static final String ENV="SIT";
	public static final String Brand="maerskline";
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	 Date todayDate= Calendar.getInstance().getTime();
	 String todayDateString=formatter.format(todayDate);
	 
	 public static final String  mymlApplicationUrl="https://myt.apmoller.net/workas?attemptedUrl=%2Fdashboard";
	 public static final String mymlLoginUrl="https://myt.apmoller.net/portaluser/#login?originalUrl=https%3A%2F%2Fmyt.apmoller.net%3A443%2Fdashboard";
	 public static final String mymlLoginUrl2="https://myt.apmoller.net";
	 
	 public static final String  mccApplicationUrl="https://myt.mcc.apmoller.net/workas?attemptedUrl=%2F";
	 //public static final String mccLoginUrl="https://myt.mcc.apmoller.net/login";
	 public static final String mccLoginUrl="https://myt.mcc.apmoller.net/dashboard";
	 public static final String mccLoginUrl2="https://myt.mcc.apmoller.net/";
	 
	 public static final String  safApplicationUrl="https://myt.safmarine.apmoller.net/workas?attemptedUrl=%2F";
	 public static final String safLoginUrl="https://myt.safmarine.apmoller.net/dashboard";
	 public static final String safLoginUrl2="https://myt.safmarine.apmoller.net/";
	 
	 public static final String  seaApplicationUrl="https://myt.sealand.apmoller.net/workas?attemptedUrl=%2F";
	 public static final String seaLoginUrl="https://myt.sealand.apmoller.net/dashboard";
	 public static final String seaLoginUrl2="https://myt.sealand.apmoller.net/";
	 
	 
	 public static final String  sglApplicationUrl="https://myt.seagoline.apmoller.net/workas?attemptedUrl=%2F";
	 public static final String sglLoginUrl="https://myt.seagoline.apmoller.net/dashboard";
	 public static final String sglLoginUrl2="https://myt.seagoline.apmoller.net/";
	 
	 //public static final String  mymlPPApplicationUrl="https://my-demo.maerskline.com/";
	 //public static final String mymlPPLoginUrl="https://my-demo.maerskline.com";
	 
	 
	 public static final String mymlPPApplicationUrl="https://demo.maersk.com/portaluser/#login/set-customer?originalUrl=https://demo.maersk.com/dashboard/";
	 //public static final String mymlPPLoginUrl="https://demo.maersk.com/dashboard/";
	 
	 public static final String mymlPPLoginUrl="https://demo.maersk.com/portaluser/login";
	 

	 
	//public static final String  mccPPApplicationUrl="https://myd.mcc.com.sg/";
	 //public static final String mccPPLoginUrl="https://myd.mcc.com.sg/";
	 
	 public static final String mccPPLoginUrl="https://demo.sealandmaersk.com/regional-selection";
	 
	 
	 public static final String safPPLoginUrl="https://myd.safmarine.com";
	 
	 
	 public static final String seaPPLoginUrl="https://myd.sealand.com";
	 
	 //public static final String  sglPPApplicationUrl="https://myd.seagoline.com/";
	 public static final String sglPPLoginUrl="https://myd.seagoline.com/";
	 
	 
	 public static final String PATH_SCREENSHOTS = System.getProperty("user.dir")+File.separator+ "screenshots" + File.separator + "Failed" + File.separator;
	
	
	
	
	
	
	
	    
}


