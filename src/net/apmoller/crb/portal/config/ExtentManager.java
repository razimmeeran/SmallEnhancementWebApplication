package net.apmoller.crb.portal.config;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import net.apmoller.crb.portal.executionEngine.TestRunner;
import net.apmoller.crb.portal.utils.Log;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

    public class ExtentManager {
	public static String appURL ;
	public static String env ;
	private static ExtentReports reports ;	
	private static ExtentTest logger;
	
	/*public static void getAppInfo(String appURL, String env){
		ExtentManager.appURL = "http://uicmd.int.ml.apmoller.net:16210/md/customer";
		ExtentManager.env = "CMD INT";
	}*/

    public synchronized static ExtentReports getReporter(String filePath) {
        if (reports == null) {

        	reports = new ExtentReports(filePath, true);
            
        	reports
                .addSystemInfo("Host URL", Constants.URL)
                .addSystemInfo("Environment", Constants.Env);
        }
        
        return reports;
    }
    
    public  static void flushReport()
	{
    	reports.endTest(logger);
    	reports.flush();
	}
    
    public  static void closeReport()
	{
    	reports.close();
	 
	}
	/*public static ExtentReports instance() {
		ExtentReports extent;
		String Path = "./report.html";
		System.out.println(Path);
		extent = new ExtentReports(Path, true);
		extent
        .addSystemInfo("URL", appURL)
        .addSystemInfo("Environment", env);
		Log.info(appURL);
		return extent;
	}
*/
	/*public static String ScreenshotCapture(WebDriver driver, String ImagesPath) {
		String timestamp = new SimpleDateFormat("dd.HH.mm.ss").format(new Date());
		TakesScreenshot oScn = (TakesScreenshot) driver;
		File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
		File oDest = new File(ImagesPath + timestamp +".jpg");
		try {
			FileUtils.copyFile(oScnShot, oDest);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return ImagesPath + timestamp + ".jpg";
	}*/

	public static String CaptureScreen(WebDriver driver, String ImagesPath, String testCase) 
	{
		String timestamp = new SimpleDateFormat("HHmmss_ddMMyyyy").format(new Date());
		String imageFilePath = ImagesPath + testCase + "_" + timestamp +".png" ;
		Log.info(imageFilePath);
		TakesScreenshot oScn = (TakesScreenshot) driver;
		File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
		File oDest = new File(imageFilePath);
		try 
		{
			FileUtils.copyFile(oScnShot, oDest);
		} 
		catch (IOException e) 
		{
			Log.error("Error while capturing screenshot"+e.getMessage());
		}
		return imageFilePath;
	}

	
}
