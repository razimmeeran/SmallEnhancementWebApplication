
package net.apmoller.crb.portal.config;

import java.io.File;

//import static net.apmoller.crb.portal.ssp.executionEngine.TestRunner.OR;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/*import java.text.SimpleDateFormat;
import java.time.LocalDate;*/
/*import java.time.ZoneId;*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
//import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.net.URL;

import net.apmoller.crb.portal.executionEngine.TestRunner;
import net.apmoller.crb.portal.config.Constants;
import net.apmoller.crb.portal.config.TestActions;
import net.apmoller.crb.portal.utils.ExcelReader;
import net.apmoller.crb.portal.utils.Log;
import net.apmoller.crb.portal.utils.ObjectReader;

import org.apache.log4j.Logger;
import org.apache.poi.util.SystemOutLogger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.remote.RemoteWebDriver;

import junit.framework.Assert;

import java.awt.RenderingHints.Key;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import java.lang.Boolean;

import org.openqa.selenium.Cookie;



public class TestActions {

	public static ObjectReader objectReader;
	public static ExcelReader excelReader;
	public static Constants constants;
	public static Log log;
	public static TestRunner testRunner;
	public static Logger log_testActions;
	public static WebDriver driver;
	public static String tempVariable = null;
	public static String applicationUrl = null;
	public static String envurl = null;
	public static String loggedin_customerCode = null;
	public static String shipmentNumber = null;
	public static String testCaseName = null;

	public TestActions() {
		log_testActions = Log.getLog(TestActions.class.getName());
	}

	public static void openBrowser(String browser, String executionMode, String gridURL, String akamaiNetstorageFolder)
			throws Exception {
		log_testActions.info("Opening Browser");

		try {

			if (executionMode.equalsIgnoreCase("Grid")) {

				if (browser.equals("chrome")) {
					System.out.println("Executing in grid");
					DesiredCapabilities capabilities = DesiredCapabilities.chrome();
					ChromeOptions options = new ChromeOptions();
					options.addArguments("start-maximized");
					options.addArguments("no-sandbox");

					capabilities.setBrowserName("chrome");
					capabilities.setPlatform(Platform.LINUX);
					capabilities.setCapability(ChromeOptions.CAPABILITY, options);
					driver = new RemoteWebDriver(new URL("http://" + gridURL + ":4444/wd/hub"), capabilities); // localhost
					Log.info("Successfully invoked Chrome browser");
					TestRunner.browserResult = true;
				} else if (browser.equals("IE")) {
					System.setProperty("webdriver.ie.driver",
							System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
									+ File.separator + "resources" + File.separator + "Drivers" + File.separator
									+ "IEDriverServer.exe");
					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(CapabilityType.BROWSER_NAME, "IE");
					capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
					capabilities.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL,
							"https://myt2.maerskline.com/");
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
					driver = new InternetExplorerDriver(capabilities);
					Log.info("Successfully invoked IE browser");
					TestRunner.browserResult = true;
				} else if (browser.equals("Mozilla")) {
					driver = new FirefoxDriver();
					Log.info("Successfully invoked Firefox browser");
					TestRunner.browserResult = true;
				}

//				driver.manage().window().maximize();
//				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

			} else if (browser.equals("chrome")) {

				System.setProperty("webdriver.chrome.driver", constants.Path_ChromeDriver);
				driver = new ChromeDriver();
			}

			else if (browser.equals("IE")) {

				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + File.separator + "Resources"
						+ File.separator + "IEDriverServer.exe");

				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();

				capabilities.setCapability(CapabilityType.BROWSER_NAME, "IE");
				capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);

				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				driver = new InternetExplorerDriver(capabilities);

				Log.info("Opening IE browser Success");
				testRunner.browserResult = true;
			}

			else if (browser.equals("Mozilla")) {
				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + File.separator + "Resources"
						+ File.separator + "geckodriver.exe");
				driver = new FirefoxDriver();
			}

			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			// System.out.println("clearing the cookies");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			testRunner.browserResult = true;
			log_testActions.info("Opening Browser Success");
			System.out.println("Opening Browser Success");
		} catch (Exception e) {
			testRunner.beforeTestException = e.getMessage();
			testRunner.browserResult = false;
			log_testActions.error("Not able to open browser to " + browser + " successfully ");
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : close_Browser Description : Used to close the browser Author: Razim
	 ********************************************************************************************************************
	 */

	public static void close_Browser() throws Exception{

		try {
			
			System.out.println("Reached Close_Browser method: "+driver.getCurrentUrl());

			Actions action = new Actions(driver);

			for (int i = 1; i <= 3; i++) {
				action.sendKeys(Keys.ESCAPE);
			}
			String url = driver.getCurrentUrl();

			if (url.contains("mybeta-stage")) {
				driver.navigate().back();
				Thread.sleep(1000);
			}

			String[] split_URL = url.split("/");
			String brandName = split_URL[2];
			driver.navigate().to("https://" + brandName + "/logout");
			log_testActions.info("The application is logout");
			System.out.println("The application is Logout");
			log_testActions.info("Closing the browser");
			driver.quit();
			testRunner.browserResult = false;
			testRunner.testStepResult = true;
			
		} catch (Exception e) {
			testRunner.beforeTestException = e.getMessage();
			//log_testActions.error("Not able to Close the Browser" + e.getMessage());
			log_testActions.error("Not able to Logging out of the application" + testRunner.beforeTestException);
			testRunner.browserResult = true;
			testRunner.testStepResult = false;

		}
	}
	/*
	 ********************************************************************************************************************
	 * Method : login Description : Used to login into the application Author:
	 * Santhosh Date:
	 ********************************************************************************************************************
	 */

	public void login(String browser, String env, String brand, String url, String username, String password,
			String customerCode) throws Exception {
		envurl = url;
		applicationUrl = url;

		if (env.equals("SIT") || env.equals("PP")) {
			try {
				driver.get(envurl);
				if (browser.equals("IE") && !brand.equals("SSP Portal")) {

					driver.get("javascript:document.getElementById('overridelink').click();");
				}
				waitFor("Home_url ", "5000", "Login Page");

				goToUrl("", url, "Login_Page", "Enter the Application Url");
				input("txtbx_UsernameLogin", username, "Login ", "Login");
				input("Maersk_SSP_txtbx_PasswordLogin", username, "Login", "Login");
				waitFor("Maersk_SSP_btn_login", "2000", "Login");
				clickElement("Maersk_SSP_btn_login", " ", "Login", "Login");
				/* waitFor("btn_Code ", "3000", "Login Page"); */
				goToUrl("", "https://myt.apmoller.net/", "Login", "Login");

				if (verifyElementExist("txt_customerCodePage", "", "Login", "Login")) {
					input("txtbx_Code", customerCode, "Login", "Login");
					clickElement("Maersk_SSP_btn_Select", " ", "Login", "Login");
					log_testActions.info("logged in to " + env + "successfully");
					System.out.println("logged in to " + env + "successfully");

					testRunner.loginResult = true;
				} else {
					log_testActions.error("Not able to login to " + env + " successfully ");
					System.out.println("Not able to login to " + env + " successfully ");

					testRunner.loginResult = false;

				}
			}

			catch (Exception e) {
				log_testActions.error("Not able to login to " + env + " successfully. Exception =  " + e.getMessage());
				testRunner.loginResult = false;
			}
		}

	}

	/*
	 ********************************************************************************************************************
	 * Method : loginStep Description : Used to Login in to the application
	 * Author:Ashok Modified Date: 21/02/2021 Modified By: Razim
	 ********************************************************************************************************************
	 */
	
		public static void loginStep(String object, String data, String pageName, String StepName)throws Exception {

		
		String[] objectProperties = object.split(";");
		String userNameProperty = objectProperties[0];
		String passWordProperty = objectProperties[1];
		String loginBtnProperty = objectProperties[2];
		String custCodeProperty = objectProperties[3];
		String selectBtnProperty = objectProperties[4];
		String[] testData = data.split(";");
		String env = testData[0];
		String brand = testData[1];
		String url = null;
		String username = testData[2];
		String password = testData[3];
		String customerCode = testData[4];
		
		envurl = url;
		applicationUrl = url;
		String loginUrl = null;
			try 
			{
				driver.manage().deleteAllCookies();								
				
				
				if(env.equals("SIT"))
				{
					
				}
				else
				{
					
					if (brand.equalsIgnoreCase("mcc")) 
					{
						
						loginUrl = constants.mccPPLoginUrl;
						driver.navigate().refresh();
						goToUrl("", loginUrl, "Login", "Login");
						System.out.println("Navigate to the url" + loginUrl + "successfully");
						
						String Asia = ObjectReader.OR.getProperty("Maersk_Sea_regional_selection_Asia");
						javaClick(Asia, "", "Sealand regional-selection", "Login to Sealand Asia");
						
						
						WebDriverWait wait_coi_banner_wrapper = new WebDriverWait(driver, 10);
						wait_coi_banner_wrapper
								.until(ExpectedConditions.visibilityOfElementLocated(
										By.xpath("//*[@class='coi-banner__accept coi-banner__accept--fixed-margin']")))
								.click();	
						driver.findElement(By.xpath("//button[@id='confirmRegion']")).click();
						driver.get("https://demo.sealandmaersk.com/portaluser/login");
					}
					else if (brand.equalsIgnoreCase("sgl")) 
					{
						
						loginUrl = constants.mccPPLoginUrl;
						driver.navigate().refresh();
						goToUrl("", loginUrl, "Login", "Login");
						System.out.println("Navigate to the url" + loginUrl + "successfully");
						String Europe = ObjectReader.OR.getProperty("Maersk_Sea_regional_selection_Europe");
						
						javaClick(Europe, "", "Sealand regional-selection", "Login to Sealand Europe");
						
						WebDriverWait wait_coi_banner_wrapper = new WebDriverWait(driver, 10);
						wait_coi_banner_wrapper
								.until(ExpectedConditions.visibilityOfElementLocated(
										By.xpath("//*[@class='coi-banner__accept coi-banner__accept--fixed-margin']")))
								.click();	
						driver.findElement(By.xpath("//button[@id='confirmRegion']")).click();
						driver.get("https://demo.sealandmaersk.com/portaluser/login");

						
					}
					else if (brand.equalsIgnoreCase("sea"))
					{
						
						loginUrl = constants.mccPPLoginUrl;
						driver.navigate().refresh();
						goToUrl("", loginUrl, "Login", "Login");
						System.out.println("Navigate to the url" + loginUrl + "successfully");
						String America = ObjectReader.OR.getProperty("Maersk_Sea_regional_selection_America");
						javaClick(America, "", "Sealand regional-selection", "Login to Sealand America");
						
						WebDriverWait wait_coi_banner_wrapper = new WebDriverWait(driver, 10);
						wait_coi_banner_wrapper
								.until(ExpectedConditions.visibilityOfElementLocated(
										By.xpath("//*[@class='coi-banner__accept coi-banner__accept--fixed-margin']")))
								.click();	
						driver.findElement(By.xpath("//button[@id='confirmRegion']")).click();
						driver.get("https://demo.sealandmaersk.com/portaluser/login");

						
					}
					else if (brand.equalsIgnoreCase("saf"))
					{
						
					}
					else
					{
						applicationUrl = constants.mymlPPLoginUrl;
						loginUrl = constants.mymlPPLoginUrl;
						driver.navigate().refresh();
						goToUrl("", loginUrl, "Login", "Login");
						System.out.println("Navigate to the url" + loginUrl + "successfully");

					}
					
					WebDriverWait wait_coi_banner_wrapper = new WebDriverWait(driver, 30);
					wait_coi_banner_wrapper
							.until(ExpectedConditions.visibilityOfElementLocated(
									By.xpath("//*[@class='coi-banner__accept coi-banner__accept--fixed-margin']")))
							.click();		
					
				}
				
				String LoginPageURL = driver.getCurrentUrl();
				
				if (getObject(getObjectLocator(userNameProperty)) != null) {
					
					input(userNameProperty, username, "Login ", "Login");
						
				}
				else {
					log_testActions.info("element not found for  " + userNameProperty);
					testRunner.testStepResult = false;
				}
				
				if (getObject(getObjectLocator(passWordProperty)) != null) {
					
					input(passWordProperty, password, "Login", "Login");
						
				}
				else {
					log_testActions.info("element not found for  " + passWordProperty);
					testRunner.testStepResult = false;

				}
				
								
				javaClick(loginBtnProperty, " ", "Login", "Login");
				Thread.sleep(2000);			
				waitForVisible(custCodeProperty);
				String CustomerPageURL = driver.getCurrentUrl();

				if (getObject(getObjectLocator(custCodeProperty)) != null) {
					
					input(custCodeProperty, customerCode, "Login", "Login");
						
				}
				else {
					log_testActions.info("element not found for  " + custCodeProperty);
					testRunner.testStepResult = false;
				}
				
				javaClick(selectBtnProperty, " ", "Login", "Login");				
				Thread.sleep(10000);
				HandeleAddWindow("", "", "", "");
				testRunner.testStepResult = true;	
			}
			catch (Exception e) {
				log_testActions.error("Not able to login to " + env + " successfully. Exception =  " + e.getMessage());
				System.out.println("Not able to login");
				testRunner.testStepResult = false;
			}
		
		}

	 //********************************************************************************************************************

	 
	/* public void loginStep(String object, String data, String pageName, String stepName) throws Exception {

		String[] objectProperties = object.split(";");
		String userNameProperty = objectProperties[0];
		String passWordProperty = objectProperties[1];
		String loginBtnProperty = objectProperties[2];
		String custCodeProperty = objectProperties[3];
		String selectBtnProperty = objectProperties[4];
		String[] testData = data.split(";");
		String env = testData[0];
		String brand = testData[1];
		String url = null;
		String username = testData[2];
		String password = testData[3];
		String customerCode = testData[4];

		WebDriverWait wait = new WebDriverWait(driver, 20);
		WebElement waitforelement;
		envurl = url;
		applicationUrl = url;
		String loginUrl = null;

		String currentURL = driver.getCurrentUrl();
		System.out.println(currentURL);

		if (env.equals("SIT") || env.equals("PP")) {
			try {

				if (env.equals("SIT")) {

					if (brand.equalsIgnoreCase("myml")) {
						applicationUrl = constants.mymlApplicationUrl;
						loginUrl = constants.mymlLoginUrl;

						driver.get(loginUrl);

						if (username.contains("testuser90")) {
							loginUrl = constants.mymlLoginUrl2;

						}
						goToUrl("", loginUrl, "Login", "Login");

					}
					if (brand.equalsIgnoreCase("mcc")) {
						applicationUrl = constants.mccApplicationUrl;
						loginUrl = constants.mccLoginUrl;

						driver.get(loginUrl);

						if (username.contains("testuser90")) {
							loginUrl = constants.mccLoginUrl2;

						}

						goToUrl("", loginUrl, "Login", "Login");

					}
					if (brand.equalsIgnoreCase("sgl")) {
						applicationUrl = constants.sglApplicationUrl;
						loginUrl = constants.sglLoginUrl;

						driver.get(loginUrl);

						if (username.contains("testuser90")) {
							loginUrl = constants.sglLoginUrl2;

						}
						goToUrl("", loginUrl, "Login", "Login");

					}
					if (brand.equalsIgnoreCase("sea")) {
						applicationUrl = constants.seaApplicationUrl;
						loginUrl = constants.seaLoginUrl;

						driver.get(loginUrl);

						if (username.contains("testuser90")) {
							loginUrl = constants.seaLoginUrl2;

						}

						goToUrl("", loginUrl, "Login", "Login");

					}
					if (brand.equalsIgnoreCase("saf")) {
						applicationUrl = constants.safApplicationUrl;
						loginUrl = constants.safLoginUrl;

						driver.get(loginUrl);

						if (username.contains("testuser90")) {
							loginUrl = constants.safLoginUrl2;

						}
						goToUrl("", loginUrl, "Login", "Login");

					}

				}
				if (env.equals("PP")) {
					if (brand.equalsIgnoreCase("myml")) {
						applicationUrl = constants.mymlPPLoginUrl;
						loginUrl = constants.mymlPPLoginUrl;

						driver.navigate().refresh();

						goToUrl("", loginUrl, "Login", "Login");
						System.out.println("Navigate to the url" + loginUrl + "successfully");

						WebDriverWait wait_coi_banner_wrapper = new WebDriverWait(driver, 30);
						wait_coi_banner_wrapper
								.until(ExpectedConditions.visibilityOfElementLocated(
										By.xpath("//*[@class='coi-banner__accept coi-banner__accept--fixed-margin']")))
								.click();

					}
					if (brand.equalsIgnoreCase("mcc")) {
						applicationUrl = constants.mccPPLoginUrl;
						loginUrl = constants.mccPPLoginUrl;

						driver.navigate().refresh();

						goToUrl("", loginUrl, "Login", "Login");
						System.out.println("Navigate to the url" + loginUrl + "successfully");

						javaClick("Maersk_SSP_IGN_lnk_loginlink", "", "", "Click on Login");

					}
					if (brand.equalsIgnoreCase("sgl")) {
						applicationUrl = constants.sglPPLoginUrl;
						loginUrl = constants.sglPPLoginUrl;
						driver.navigate().refresh();

						goToUrl("", loginUrl, "Login", "Login");
						System.out.println("Navigate to the url" + loginUrl + "successfully");
						javaClick("Maersk_SSP_IGN_lnk_loginlink", "", "", "Click on Login");

					}
					if (brand.equalsIgnoreCase("sea")) {
						applicationUrl = constants.seaPPLoginUrl;
						loginUrl = constants.seaPPLoginUrl;
						driver.navigate().refresh();
						goToUrl("", loginUrl, "Login", "Login");
						System.out.println("Navigate to the url" + loginUrl + "successfully");

						javaClick("Maersk_SSP_IGN_lnk_loginlink", "", "", "Click on Login");
					}
					if (brand.equalsIgnoreCase("saf")) {
						applicationUrl = constants.safPPLoginUrl;
						loginUrl = constants.safPPLoginUrl;
						driver.navigate().refresh();
						goToUrl("", loginUrl, "Login", "Login");
						System.out.println("Navigate to the url" + loginUrl + "successfully");

						javaClick("Maersk_SSP_IGN_lnk_loginlink", "", "", "Click on Login");

					}
				}

				if (username.contains("testuser90")) {

					navigateMenu("Maersk_SSP_lnk_youAreLoggedIn;Maersk_SSP_lnk_loginYouAreLogged", "", "Login Page",
							"Navigate login page");

				}

				input(userNameProperty, username, "Login ", "Login");
				input(passWordProperty, password, "Login", "Login");
				javaClick(loginBtnProperty, " ", "Login", "Login");

				Thread.sleep(2000);

				String currentURLofthepage = driver.getCurrentUrl();
				String URLofthepage = "https://myt.sealand.apmoller.net/portaluser/#login?originalUrl=https%3A%2F%2Fmyt.sealand.apmoller.net%3A443%2Fdashboard";
				System.out.println(currentURLofthepage);

				if (username.contains("AAS181")) {
					if (env.equals("SIT")) {
						WebElement pin = driver.findElement(By.xpath("//*[@id='pin']"));
						pin.sendKeys("1234");
						WebElement continue_pin = driver.findElement(By.xpath("//*[@id='submitBtn']"));
						continue_pin.click();

						Thread.sleep(2000);

						input(custCodeProperty, customerCode, "Login", "Login");
						waitFor(" ", "2000", "Login Page");
						javaClick(selectBtnProperty, " ", "Login", "Login");

					}
				}

				else if (username.contains("testuser90")) {
					WebElement custmerCodeRadio = driver.findElement(
							By.xpath("//input[contains(@id,'customerCode') and @value='" + customerCode + "']"));
					Thread.sleep(2000);
					custmerCodeRadio.click();

				}

				else if (username.contains("spotuser.ex.mul.cc.booking")) {
					WebElement logo = driver.findElement(By.xpath("//*[@id='ign-navbar']/a[3]/img"));
					Thread.sleep(2000);
					logo.click();

				}

				else if (username.contains("testuser21@xleap")) {
					if (brand.equalsIgnoreCase("sea")) {
						WebElement logo = driver.findElement(By.xpath("//*[@id='ign-navbar']/a[3]"));
						Thread.sleep(2000);
						logo.click();

						waitFor("txtbx_Code", "5000", "Login Page");
						javaClick(custCodeProperty, " ", "Login", "Login");
						input(custCodeProperty, customerCode, "Login", "Login");
						Thread.sleep(2000);
						javaClick(selectBtnProperty, " ", "Login", "Login");
					}

					else {

						WebElement logo = driver.findElement(By.xpath("//*[@id='ign-navbar']/a[3]/img"));
						Thread.sleep(2000);
						logo.click();
						waitFor("txtbx_Code", "5000", "Login Page");
						javaClick(custCodeProperty, " ", "Login", "Login");
						input(custCodeProperty, customerCode, "Login", "Login");
						Thread.sleep(2000);
						javaClick(selectBtnProperty, " ", "Login", "Login");

					}
				}

				else if (username.contains("testuser22@xleap")) {
					if (brand.equalsIgnoreCase("sea")) {

						WebElement logo = driver.findElement(By.xpath("//*[@id='ign-navbar']/a[3]"));
						Thread.sleep(2000);
						logo.click();

						waitFor("txtbx_Code", "5000", "Login Page");
						input(custCodeProperty, customerCode, "Login", "Login");
						waitFor(" ", "3000", "Login Page");
						javaClick(selectBtnProperty, " ", "Login", "Login");
					} else {

						WebElement logo = driver.findElement(By.xpath("//*[@id='ign-navbar']/a[3]/img"));
						Thread.sleep(2000);
						logo.click();
						waitFor("txtbx_Code", "5000", "Login Page");
						input(custCodeProperty, customerCode, "Login", "Login");
						waitFor(" ", "3000", "Login Page");
						javaClick(selectBtnProperty, " ", "Login", "Login");
					}
				}

				else if (username.contains("mccuser21@xleap")) {

					WebElement logo = driver.findElement(By.xpath("//*[@id='ign-navbar']/a[3]"));
					Thread.sleep(2000);
					logo.click();
					waitFor("txtbx_Code", "5000", "Login Page");
					input(custCodeProperty, customerCode, "Login", "Login");
					Thread.sleep(2000);
					javaClick(selectBtnProperty, " ", "Login", "Login");

				}

				else if (username.contains("safuser21@xleap")) {
					WebElement logo = driver.findElement(By.xpath("//*[@id='ign-navbar']/a[3]"));
					Thread.sleep(2000);
					logo.click();
					waitFor("txtbx_Code", "5000", "Login Page");
					input(custCodeProperty, customerCode, "Login", "Login");
					Thread.sleep(2000);
					javaClick(selectBtnProperty, " ", "Login", "Login");

				}

				else if (username.contains("sgluser21@xleap")) {
					WebElement logo = driver.findElement(By.xpath("//*[@id='ign-navbar']/a[3]"));
					Thread.sleep(2000);
					logo.click();
					waitFor("txtbx_Code", "5000", "Login Page");
					input(custCodeProperty, customerCode, "Login", "Login");
					Thread.sleep(2000);
					javaClick(selectBtnProperty, " ", "Login", "Login");

				}

				else if (currentURLofthepage.equals(URLofthepage)) {
					WebElement logo = driver.findElement(By.xpath("//*[@id='logoImage']"));
					Thread.sleep(2000);
					logo.click();
					waitFor("txtbx_Code", "5000", "Login Page");
					input(custCodeProperty, customerCode, "Login", "Login");
					waitFor(" ", "3000", "Login Page");
					javaClick(selectBtnProperty, " ", "Login", "Login");

				}

				else if (username.contains("testuser201") || username.contains("spotusertst.in")) {

				}

				else if (username.contains("testuser10@xleap") || username.contains("mccuser10@xleap")
						|| username.contains("mccuser10@xleap") || username.contains("safuser10@xleap")
						|| username.contains("sgluser10@xleap")) {
					WebElement logo = driver.findElement(By.xpath("//*[@id='ign-navbar']/a[3]"));
					Thread.sleep(2000);
					logo.click();
				}

				else if (username.contains("ssibtestuser1")) {
					if (env.equals("PP")) {
						waitFor(" ", "5000", "customerCode");
						driver.findElement(By.xpath(
								"//td[div[contains(text(),'DK:" + customerCode + "')]]/following-sibling::td[2]"))
								.click();
					}

					else {
						driver.findElement(By.xpath(
								"//*[@id='GCSS BOOKED BY , PAKHUS D, 2ND FLOOR , DAMPFAERGEVEJ , COPENHAGEN , DK:10000007951']/a"))
								.click();
					}
				}

				else {
					waitFor("txtbx_Code", "5000", "Login Page");
					System.out.println("Navigate to the url: " + applicationUrl + " successfully");

					//input(custCodeProperty, customerCode, "Login", "Login");
					
					
					driver.findElement(By.xpath("//*[@id='customerCode']")).sendKeys(customerCode);
					
					Thread.sleep(2000);
					//javaClick(selectBtnProperty, " ", "Login", "Login");
					
					
					
				
					

//				WebElement CustomerElement = getObject(getObjectLocator(selectBtnProperty));
//				JavascriptExecutor executor = (JavascriptExecutor) driver;
//				executor.executeScript("arguments[0].click();", CustomerElement);
//					
					//getObject(getObjectLocator(selectBtnProperty)).click();
					
					
					driver.findElement(By.xpath("//*[@id='set-customer-code-form']/button")).click();
					
					WebDriverWait waitforpopup = new WebDriverWait(driver, 20);
					waitforpopup.until(ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//*[@class='nfd-modal__window__close']"))).click();

					//driver.switchTo().defaultContent();
					driver.navigate().refresh();
					System.out.println("Successfully navigate to " + driver.getCurrentUrl());
					
					
				}
				testRunner.testStepResult = true;
				
				
			} catch (Exception e) {
				log_testActions.error("Not able to login to " + env + " successfully. Exception =  " + e.getMessage());
				//testRunner.loginResult = false;
				testRunner.testStepResult = false;

			}
		}
	}*/
	
	
	/*
	 * *****************************************************************************
	 * ************************************** Method : changeToEnglish Description :
	 * Changing Application language to English Author: Ashok Date:
	 **********************************************************************************************************************
	 */

	public void changeToEnglish(String object, String data, String pageName, String stepName) {
		driver.navigate().to(driver.getCurrentUrl());
		try {
			log_testActions.info("Changing language to English");
			moveClick(object, data, pageName, stepName);

		} catch (Exception e) {
			testRunner.beforeTestException = e.getMessage();
			log_testActions.error("Not able to Changing language to English" + testRunner.beforeTestException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ************************************** Method : logout Description : Used to
	 * Logout from The Application Author: Ashok Date:
	 **********************************************************************************************************************
	 */

	public static void logout(String object, String data, String pageName, String stepName) {
		log_testActions.info("Logging out of the application");

		try {

			if (testRunner.testName.contains("_MML_")) {

				// IGNnavigateMenu("Maersk_SSP_IGN_lnk_loginlink;Maersk_SSP_IGN_lnk_logout","Logout","Booking_Page","Logout
				// from Application");

				String url = driver.getCurrentUrl();
				String[] split_URL = url.split("/");
				String brandName = split_URL[2];
				driver.navigate().to("https://" + brandName + "/logout");
				log_testActions.info("The application is logout");
				System.out.println("The application is Logout");

			} else {
				String url = driver.getCurrentUrl();
				String[] split_URL = url.split("/");
				String brandName = split_URL[2];
				driver.navigate().to("https://" + brandName + "/logout");
				log_testActions.info("The application is logout");
				System.out.println("The application is Logout");

				Thread.sleep(10000);

				String pageTitle = driver.getTitle();
				if (pageTitle.contains("Certificate") || pageTitle.contains("Certificate")) {
					javaClick("lnk_CertificateErrorIE", "", "", "Click on continue certificate error");
					Thread.sleep(10000);

				}
				if (pageTitle.contains("Acceptable Usage Policy")) {
					javaClick("btn_IAcceptError", "", "", "Click on I Accept Button");
					Thread.sleep(10000);

				}
				testRunner.testStepResult = true;
				System.out.println("Log out from the application successfully");
			}

//			 driver.manage().deleteAllCookies();
//			 System.out.println("clearing the cookies");

		} catch (Exception e) {
			
			testRunner.beforeTestException = e.getMessage();
			log_testActions.error("Not able to Logging out of the application" + testRunner.beforeTestException);
			testRunner.testStepResult = false;
		}

	}

	/*
	 ********************************************************************************************************************
	 * Method : waitForEnable Description : Wait for till Enable the WebElement
	 * Author: Ashok
	 **********************************************************************************************************************
	 */
	public static void waitForEnable(WebElement ele) {

		try {
			while (!ele.isEnabled()) {
				log_testActions.info("disabled");
				Thread.sleep(1000);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			log_testActions.info("Exception=" + e.getMessage());

		}

		log_testActions.info("Enabled");
	}

	/*
	 ********************************************************************************************************************
	 * Method : input Description : Enter the text value to the text box Author:
	 * Ashok
	 **********************************************************************************************************************
	 */

	public static void input(String object, String data, String pageName, String StepName) {

		String txtData = null;
		WebElement ele = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date todayDate = Calendar.getInstance().getTime();
		constants.Current_Date = formatter.format(todayDate);
		waitForVisible(object);
		try {
			log_testActions.info("Entering the text in " + object + " in " + pageName);
			System.out.println("Entered  value in " + object + "in" + pageName);

			txtData = data;
			if (object.equals("txtbx_Con1_length") || object.equals("txtbx_Con1_width")
					|| object.equals("txtbx_Con1_height")) {

				JavascriptExecutor executor1 = (JavascriptExecutor) driver;
				executor1.executeScript("document.getElementById('oversized-details-modal').style.display='block';");
				executor1.executeScript("document.getElementById('clone').style.display='block';");

			}

			if (object.equals("txtbx_Con2_heigth") || object.equals("txtbx_Con2_width")
					|| object.equals("txtbx_Con2_height")) {

				JavascriptExecutor executor2 = (JavascriptExecutor) driver;

				executor2.executeScript("document.getElementById('oversized-details-modal').style.display='block';");
				executor2.executeScript("document.getElementById('clone').style.display='block';");
				Thread.sleep(1000);

			}
			if (txtData.equals("currDate")) {
				txtData = constants.Current_Date;

			}
			if (txtData.equals("currDate+1")) {
				txtData = constants.Current_Date;
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();

			}
			if (txtData.equals("<<RTD_BookingNumber>>")) {
				txtData = constants.Booking_Number;

			}

			if (txtData.equals("<<GTD_TestData_B1>>")) {

			}

			waitForVisible(object);

			if (isTestElementPresent(object)) {

				log_testActions.info("Element Found. Entering value of " + object + " i= " + txtData);
				System.out.println("Element Found. Entering value of " + object + " i= " + txtData);

				ele = getObject(getObjectLocator(object));

				/* ele.clear(); */
				ele.sendKeys(txtData);
				// ele.sendKeys(Keys.TAB);
				Thread.sleep(1000);

				log_testActions.info("Entered " + txtData + " value in " + object);
				System.out.println("Entered " + txtData + " value in " + object);
				testRunner.testStepResult = true;
			} 
			
			else {
				log_testActions.info("Element Not Found for  " + object);
				System.out.println("Element Not Found for  " + object);
				testRunner.testStepResult = false;
			}
		
			
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to enter the text in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : setTextTab Description : Enter the text value to the text baox and
	 * handled down and Enter and TAB key events Author: Ashok
	 **********************************************************************************************************************
	 */
	public static void setTextTab(String object, String data, String pageName, String StepName) {

		String[] input = data.split("~");
		String inputValue = input[0];
		String tabEvent = input[1];
		String txtData = null;
		WebElement ele = null;

		try {
			log_testActions.info("Entering the text in " + object + " in " + pageName);
			System.out.println("Entering the text in " + object + " in " + pageName);

			txtData = inputValue;

			ele = getObject(getObjectLocator(object));
			waitForVisible(object);

			if (isTestElementPresent(object)) {

				log_testActions.info("Element Found. Entering value of " + object + " i= " + txtData);
				System.out.println("Element Found. Entering value of " + object + " i= " + txtData);

				ele = getObject(getObjectLocator(object));

				ele.clear();
				ele.sendKeys(txtData);
				Thread.sleep(2000);
				ele.sendKeys(Keys.TAB);
				Thread.sleep(1000);

				log_testActions.info("Entered " + txtData + " value in " + object);
				System.out.println("Entered " + txtData + " value in " + object);
				testRunner.testStepResult = true;
			} else {
				log_testActions.info("Element Not Found for  " + object);
				System.out.println("Element Not Found for  " + object);
				testRunner.testStepResult = false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to enter the text in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : getInnerTextValue Description : used to get the inner text value
	 * from DOM Author: Ashok
	 **********************************************************************************************************************
	 */
	public static String getInnerTextValue(String object, String data, String pageName, String StepName) {

		String eleValue = "";

		try {
			log_testActions.info("Getting  the value  of " + object + " in " + pageName);

			if (isTestElementPresent(object)) {

				log_testActions.info("Element Found. Getting value of " + object);
				eleValue = getObject(getObjectLocator(object)).getText();
				log_testActions.info("Element value = " + eleValue);
				testRunner.testStepResult = true;
			} else {
				log_testActions.info("Element Not Found for  " + object);
				testRunner.testStepResult = false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to enter the text in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;

		}
		return eleValue;
	}
	/*
	 ********************************************************************************************************************
	 * Method : getTextboxValue Description : Used to Get the Existing value in the
	 * text box Author: Ashok
	 **********************************************************************************************************************
	 */

	public static String getTextboxValue(String object, String data, String pageName, String StepName) {

		String eleValue = "";

		try {
			log_testActions.info("Getting  the value  of " + object + " in " + pageName);

			if (isTestElementPresent(object)) {

				log_testActions.info("Element Found. Getting value of " + object);
				eleValue = getObject(getObjectLocator(object)).getAttribute("value");
				log_testActions.info("Element value = " + eleValue);
				testRunner.testStepResult = true;
			} else {
				log_testActions.info("Element Not Found for  " + object);
				testRunner.testStepResult = false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to get the value in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;

		}
		return eleValue;
	}

	/*
	 ********************************************************************************************************************
	 * Method : getSelectedOption Description : Used to Get the Existing Selected
	 * value by the drop down Author: Ashok
	 **********************************************************************************************************************
	 */

	public static String getSelectedOption(String object, String data, String pageName, String StepName) {

		String eleValue = "";
		WebElement ele;

		try {
			log_testActions.info("Getting  the value  of " + object + " in " + pageName);

			if (isTestElementPresent(object)) {

				log_testActions.info("Element Found. Getting value of " + object);

				ele = getObject(getObjectLocator(object));
				Select dpdnSelect = new Select(ele);
				eleValue = dpdnSelect.getFirstSelectedOption().getText();
				log_testActions.info("Element value = " + eleValue);
				testRunner.testStepResult = true;
			} else {
				log_testActions.info("Element Not Found for  " + object);
				testRunner.testStepResult = false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to get the value in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;

		}
		return eleValue;
	}

	/*
	 ********************************************************************************************************************
	 * Method : getRadioValue Description : Used to Get the DOM value of the Radio
	 * value Author: Ashok
	 **********************************************************************************************************************
	 */

	public static String getRadioValue(String object, String data, String pageName, String StepName) {

		String eleValue = "";
		waitForVisible(object);

		try {
			log_testActions.info("Getting  the value  of " + object + " in " + pageName);

			if (isTestElementPresent(object)) {
				log_testActions.info("Element Found. Getting value of " + object);
				List<WebElement> eles = getObjects(getObjectLocator(object));

				for (int i = 0; i < eles.size(); i++) {
					WebElement ele = eles.get(i);
					if (ele.isSelected()) {
						eleValue = ele.getAttribute("value");
						constants.radioBtnAttributeValue = eleValue;
						testRunner.reportLogger.log(LogStatus.PASS,
								"The Radio button value is " + constants.radioBtnAttributeValue,
								constants.KEYWORD_PASS);

					}
				}

				log_testActions.info("Element value = " + eleValue);
				testRunner.testStepResult = true;
			} else {
				log_testActions.info("Element Not Found for  " + object);
				testRunner.testStepResult = false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to get the value in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}

		return eleValue;
	}

	/*
	 ********************************************************************************************************************
	 * Method : verifyElementExist Description : Used to verify the WebElement is
	 * Exist on the page Author: Ashok
	 **********************************************************************************************************************
	 */

	public static boolean verifyElementExist(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before Verifying " + object + " in " + pageName);
		try {
			if (isTestElementPresent(object)) {

				testRunner.testStepResult = true;
				log_testActions.info("Verifying " + object + " in " + pageName + " found");
				System.out.println("Verifying " + object + " in " + pageName + " found");
				return true;
			} else {
				
				log_testActions.info("Verifying " + object + " in " + pageName + " Not found");
				System.out.println("Verifying " + object + " in " + pageName + " Not found");
				testRunner.testStepResult = false;
				return false;
			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
			return false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : verifyElementExist Description : Used to verify the WebElement is
	 * not Exist on the page Author: Ashok
	 **********************************************************************************************************************
	 */

	public static boolean verifyElementNotExist(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before Verifying " + object + " in " + pageName);
		try {
			if (!isTestElementPresent(object)) {
				testRunner.testStepResult = true;
				log_testActions.info("Verifying " + object + " in " + pageName + " not found");
				System.out.println("Verifying " + object + " in " + pageName + " not found");
				return true;
			} else {
				
				log_testActions.info("Verifying " + object + " in " + pageName + " found");
				System.out.println("Verifying " + object + " in " + pageName + "  found");
				testRunner.testStepResult = false;
				return false;
			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
			return false;
		}
	}
	/*
	 ********************************************************************************************************************
	 * Method : comparePageText Description : Used to verify page text is displayed
	 * on the page Author: Ashok
	 **********************************************************************************************************************
	 */

	public static void comparePageText(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before compareValue " + object + " in " + pageName);
		System.out.println("Before compareValue " + object + " in " + pageName);
		try {
			String actual;
			WebElement ele = null;
			if (data.contains("~"))

			{
				String testdata[] = data.split("~");
				String referenceText = testdata[0];
				String expectedText = testdata[1];
				String ActualTextXapth = "(//*[contains(text(),'" + referenceText + "')]/following::*[contains(text(),'"
						+ expectedText + "')])[1]";
				String ActualText = driver.findElement(By.xpath(ActualTextXapth)).getText();

				if (ActualText.contains(expectedText)) {
					testRunner.testStepResult = true;
					log_testActions.info("Expected  =" + expectedText + " and Actual =" + ActualText + " values equal");
					System.out.println("Expected  =" + expectedText + " and Actual =" + ActualText + " values equal");

				}
			} else {
				String eleXpath = "//*[contains(text(),'" + data + "')]";
				ele = driver.findElement(By.xpath(eleXpath));
				actual = ele.getText();
				if (actual.contains(data)) {
					testRunner.testStepResult = true;
					log_testActions.info("Expected  =" + data + " and Actual =" + actual + " values equal");
					System.out.println("Expected  =" + data + " and Actual =" + actual + " values equal");

				}
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;

		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : compareValue Description : Used to compare page text is displayed on
	 * the page Author: Ashok
	 **********************************************************************************************************************
	 */
	public static boolean compareValue(String object, String data, String pageName, String StepName) {

		WebElement ele;
		int i, j = 1, k, l, m, n;
		boolean blnfound = true;
		String strTempValue[], strNewDatVal;
		String strTemp, strTemp1, strdate, strDate1, strDate2;
		log_testActions.info("Before compareValue " + object + " in " + pageName);
		System.out.println("Before compareValue " + object + " in " + pageName);

		try {
			if (!object.contains("")) 
			{
				ele = getObject(getObjectLocator(object));
				boolean expectedresult = ele.isDisplayed();
				testRunner.testStepResult = true;
				return expectedresult;
			}

			else {			
				if (data.contains("<<RTD_radioBtnAttributeValue>>")) {
					data = constants.radioBtnAttributeValue;
				}
				if (data.contains("currDate")) {
					data = constants.Current_Date;
				}
				
				
				String subValues[] = data.split(";");
				String strInnerHTML = driver.getPageSource();
				n = 0;
				m = 0;

				 //blnfound=true;
				for (i = 0; i < subValues.length; i++) {
					if (strInnerHTML.contains(subValues[i])) {
						blnfound = true;
						testRunner.testStepResult = true;
						log_testActions.info("Expected text " + subValues[i] + " is dispalyed in " + pageName);
						System.out.println("Expected text " + subValues[i] + " is dispalyed in " + pageName);
						//break;
					} else {
						
						testRunner.testStepResult = false;
						log_testActions.info("Expected text " + subValues[i] + " is not dispalyed in " + pageName);
						System.out.println("Expected text " + subValues[i] + " is not dispalyed in " + pageName);
						blnfound = false;
						break;
					}
				}
				
				return blnfound;	
			}
			
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;
			return false;
		}

	}

	/*
	 ********************************************************************************************************************
	 * Method : verify Description : Used to verify page text is displayed on the
	 * page Author: Ashok
	 **********************************************************************************************************************
	 */

	public static boolean verify(String object, String data, String pageName, String StepName) {
		WebElement ele = null;
		log_testActions.info("Before compareValue " + object + " in " + pageName);
		try {
			String actual;
			if (object.contains("error")) {
				String newErrorFieldXpath = "//lable[contains(" + data + ")]";
				ele = driver.findElement(By.xpath(newErrorFieldXpath));
				actual = ele.getText();

			} else {

				JavascriptExecutor jse = (JavascriptExecutor) driver;
				String script = "return   document.getElementByXPath(\"(//*/text()[preceding-sibling::br and following-sibling::br])[1]'\").getText();";

				actual = ((JavascriptExecutor) driver).executeScript(script).toString();

			}

			if (actual.contains(data)) {
				testRunner.testStepResult = true;
				log_testActions.info("Expected  =" + data + " and Actual =" + actual + " values equal");
				return true;
			} else {
				
				log_testActions.info("Expected  =" + data + " and Actual =" + actual + " values not equal");
				testRunner.testStepResult = false;
				return false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;
			return false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : verifyPageText Description : Used to verify multiple text values
	 * were displayed on the page Author: Ashok
	 **********************************************************************************************************************
	 */

	public static boolean verifyPageText(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before compareValue " + object + " in " + pageName);

		int i, j = 1, k, l, m, n;
		boolean blnfound = true;
		String strTempValue[], strNewDatVal;
		String strTemp, strTemp1, strdate, strDate1, strDate2;
		try {
			String subValues[] = data.split("~");
			String strInnerHTML = driver.getPageSource();
			n = 0;
			m = 0;

			for (i = 0; i < subValues.length; i++) {
				if (strInnerHTML.contains(subValues[i])) {
					blnfound = true;
					testRunner.testStepResult = true;
					// break;
				} else {
					blnfound = false;
					testRunner.testStepResult = false;
					break;
				}
			}

			return blnfound;
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;
			return false;
		}

	}

	/*
	 ********************************************************************************************************************
	 * Method : getText Description : Used to get text of the WebElement Author:
	 * Ashok
	 **********************************************************************************************************************
	 */
	public static String getText(String object, String data, String pageName, String StepName) {
		WebElement ele = null;
		log_testActions.info("Before compareValue " + object + " in " + pageName);
		try {
			String actual;

			actual = getObject(getObjectLocator(object)).getText();

			return actual;

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to compareValue");
			return "testRunner.stepException";
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : verifyErrorMsg Description : Used to verify the Error Message
	 * Author: Ashok
	 **********************************************************************************************************************
	 */
	public static boolean verifyErrorMsg(String object, String data, String pageName, String StepName) {
		WebElement ele = null;
		try {

			String expectedErrorText = data;
			String newErrorFieldXpath = "(//*[contains(text(),'" + data + "')])[1]";

			String actualerrorText = driver.findElement(By.xpath(newErrorFieldXpath)).getAttribute("innerHTML");

			if (actualerrorText.contains(expectedErrorText)) {
				testRunner.testStepResult = true;
				log_testActions
						.info("Expected  =" + expectedErrorText + " and Actual =" + actualerrorText + " values equal");
				return true;

			} else {
				
				log_testActions.info(
						"Expected  =" + expectedErrorText + " and Actual =" + actualerrorText + " values not equal");
				testRunner.testStepResult = false;
				return false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;
			return false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : verifySelectedElementText Description : Used to verify selected
	 * element text Author: Ashok
	 **********************************************************************************************************************
	 */

	public static boolean verifySelectedElementText(String object, String data, String pageName, String StepName) {
		WebElement ele = null;
		try {

			String actual;
			ele = getObject(getObjectLocator(object));

			String ElementText = ele.getAttribute("innerHTML");

			if (ElementText.contains(data)) {
				testRunner.testStepResult = true;
				log_testActions.info("Expected  =" + data + " and Actual =" + ElementText + " values equal");
				return true;

			} else {
				
				log_testActions.info("Expected  =" + data + " and Actual =" + ElementText + " values not equal");
				testRunner.testStepResult = false;
				return false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;
			return false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : verifySelectedElementText Description : Used to verify no error
	 * message Author: Ashok
	 **********************************************************************************************************************
	 */
	public static boolean verifyNoErrorMsg(String object, String data, String pageName, String StepName) {
		String input[] = data.split("~");
		String inputType = input[0];
		String errMsg = input[1];

		WebElement ele = null;
		try {
			String actual;
			String newErrorFieldXpath = "//label[contains(text(),'" + errMsg + "')]";
			ele = driver.findElement(By.xpath(newErrorFieldXpath));

			if (ele == (null)) {
				testRunner.testStepResult = true;
				log_testActions.info("Expected  =" + errMsg + "  Should not be exist");
				return true;

			} else {
				
				log_testActions.info("Expected step getting failed");
				testRunner.testStepResult = false;
				return false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;
			return false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : goToUrl Description :used to navigate to URL Author: Ashok
	 **********************************************************************************************************************
	 */

	public static void goToUrl(String object, String data, String pageName, String stepName) {
		log_testActions.info("Before goToURL in " + pageName);
		testRunner.testStepResult = false;
		try {
			
			
			driver.get(data);
			Thread.sleep(10000);
			String pageTitle = driver.getTitle();
			if (pageTitle.contains("Certificate") || pageTitle.contains("Certificate")) {
				javaClick("lnk_CertificateErrorIE", "", "", "Click on continue certificate error");
				Thread.sleep(5000);

			}
			if (pageTitle.contains("Acceptable Usage Policy")) {
				javaClick("btn_IAcceptError", "", "", "Click on I Accept Button");
				Thread.sleep(5000);

			}
			System.out.println("Navigate url : " + data + "    successfully");
			testRunner.testStepResult = true;
		} catch (Exception e) {
			
			testRunner.stepException = e.getMessage();
			log_testActions.info("Go to URL Function Failed " + object + " in " + pageName + "  --- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : goToUrl Description :used to navigate to URL Author: Ashok
	 **********************************************************************************************************************
	 */
	public static void navigateToUrl(String object, String data, String pageName, String stepName) {
		log_testActions.info("Before goToURL in " + pageName);
		testRunner.testStepResult = false;
		try {

			driver.get(data);
			String pageTitle = driver.getTitle();
			if (pageTitle.contains("Certificate") || pageTitle.contains("Certificate")) {
				javaClick("lnk_CertificateErrorIE", "", "", "Click on continue certificate error");
				Thread.sleep(10000);

			}
			testRunner.testStepResult = true;
		} catch (Exception e) {
			
			testRunner.stepException = e.getMessage();
			log_testActions.info("Go to URL Function Failed " + object + " in " + pageName + "  --- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	public static void navigateBack(String object, String data, String pageName, String stepName) {
		log_testActions.info("Before goToURL in " + pageName);
		testRunner.testStepResult = false;
		try {

			driver.navigate().back();
			Thread.sleep(1000);

			testRunner.testStepResult = true;
		} catch (Exception e) {
			
			testRunner.stepException = e.getMessage();
			log_testActions.info("Go to URL Function Failed " + object + " in " + pageName + "  --- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : isTestElementPresent Description :Verify is WebElement is present on
	 * WebPage Author: Ashok
	 **********************************************************************************************************************
	 */

	public static boolean isTestElementPresent(String object) throws Exception {

		if (getObject(getObjectLocator(object)) != null) {

			log_testActions.info("element found for  " + object);
			testRunner.testStepResult = true;
			return true;
		}

		else {
			log_testActions.info("element not found for  " + object);
			testRunner.testStepResult = false;
			return false;

		}

	}

	public static WebElement getObject(By Locator) throws Exception {
		WebElement ele = null;

		try {
			log_testActions.info("Locator in get object method = " + Locator.toString());

			ele = driver.findElement(Locator);

			log_testActions.info("Able to find the element for BY = " + Locator.toString());
			testRunner.testStepResult = true;
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			
			

			log_testActions.error("Not able to find the element for BY = " + Locator.toString() + "  Exception="
					+ testRunner.stepException);
			testRunner.testStepResult = false;
			

		}
		return ele;

	}

	public static List<WebElement> getObjects(By Locator) throws Exception {
		List<WebElement> ele = null;

		try {

			ele = driver.findElements(Locator);
		} catch (NoSuchElementException e) {
			
			testRunner.stepException = e.getMessage();

			log_testActions.error(
					"Not able to find the elements for BY = " + Locator + "  Exception=" + testRunner.stepException);

			testRunner.testStepResult = false;
		}
		return ele;

	}

	public static String[] getLocatorTypeValues(String objectName) throws Exception {

		log_testActions.info("Before locator values for object " + objectName);
		String[] locatorTypeValues = new String[2];
		String locatorTypeValuesString = ObjectReader.OR.getProperty(objectName);
		log_testActions.info("Locator Property=" + locatorTypeValuesString.toString());

		String locatorType = locatorTypeValuesString.split(";")[0];
		String locatorValues = locatorTypeValuesString.split(";")[1];
		locatorTypeValues[0] = locatorType;
		locatorTypeValues[1] = locatorValues;
		log_testActions.info("locatorType = " + locatorType);
		log_testActions.info("locatorValues = " + locatorValues);

		return locatorTypeValues;
	}

	public static By getObjectLocator(String locatorType, String locatorValue) throws Exception {

		By locator = null;
		switch (locatorType) {
		case "Id":
			locator = By.id(locatorValue);
			break;
		case "className":
			locator = By.className(locatorValue);
			break;
		case "Name":
			locator = By.name(locatorValue);
			break;
		case "CssSelector":
			locator = By.cssSelector(locatorValue);
			break;
		case "LinkText":
			locator = By.linkText(locatorValue);
			break;
		case "PartialLinkText":
			locator = By.partialLinkText(locatorValue);
			break;
		case "TagName":
			locator = By.tagName(locatorValue);
			break;
		case "Xpath":
			locator = By.xpath(locatorValue);
			break;
		}

		log_testActions.info("locator = " + locator);
		return locator;
	}

	public static By getObjectLocator(String objectName) throws Exception {
		String locatorProperty = null;
		if (!objectName.contains(";")) {
			locatorProperty = ObjectReader.OR.getProperty(objectName);
			log_testActions.info("Locator Property=" + locatorProperty);
		} else {
			locatorProperty = objectName;

		}

		String locatorType = locatorProperty.split(";")[0];
		String locatorValue = locatorProperty.split(";")[1];

		log_testActions.info("locatorType = " + locatorType);
		log_testActions.info("locatorValue = " + locatorValue);

		By locator = null;
		switch (locatorType) {
		case "Id":
			locator = By.id(locatorValue);
			break;
		case "Name":
			locator = By.name(locatorValue);
			break;
		case "CssSelector":
			locator = By.cssSelector(locatorValue);
			break;
		case "LinkText":
			locator = By.linkText(locatorValue);
			break;
		case "PartialLinkText":
			locator = By.partialLinkText(locatorValue);
			break;
		case "TagName":
			locator = By.tagName(locatorValue);
			break;
		case "Xpath":
			locator = By.xpath(locatorValue);
			break;
		}

		log_testActions.info("locator = " + locator);
		return locator;
	}

	/*
	 ********************************************************************************************************************
	 * Method : waitFor Description :Wait for specified time for presence web
	 * element Author: Ashok
	 **********************************************************************************************************************
	 */
	public static void waitFor(String object, String data, String pageName) throws Exception {
		try {
			if (data.contains("~")) {
				String[] testData = data.split("~");
				String waitTime = testData[1];
				Thread.sleep(Long.valueOf(waitTime));
				testRunner.testStepResult = true;
			} else {

				log_testActions.info("Wait for object " + object + " in " + pageName + " for " + data + "Seconds");
				Thread.sleep(Long.valueOf(data));
				testRunner.testStepResult = true;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to Wait --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	public void waitForElement(String object, String data, String StepName, String pageName) throws Exception {

		int actualWaitTime;
		try {

			String[] testData = data.split("~");
			String testdata = testData[0];
			String waitTime = testData[1];
			actualWaitTime = Integer.parseInt(waitTime) * 1000;

			if (object.contains("Maersk_SSP_PriceROE")) {

				WebDriverWait wait = new WebDriverWait(driver, 80);
				wait.until(ExpectedConditions.visibilityOfElementLocated(
						By.xpath("//*[contains(text(),'Rate of exchange used for calculation')]")));

			}

			else {
				System.out.println("object is not present");
				Thread.sleep(Long.valueOf(actualWaitTime));

			}

			testRunner.testStepResult = true;

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to Wait --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : waitForVisible Description :Wait for specified time for until the
	 * specified condition get satisfied(Condition wait) Author: Ashok
	 **********************************************************************************************************************
	 */

	public static boolean waitForVisible(String object) {

		boolean success = false;
		try {
			log_testActions.info("Waiting for the Element to visible...");
			//WebDriverWait wait = new WebDriverWait(driver, 65);
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOf(getObject(getObjectLocator(object))));
			success = true;
			testRunner.testStepResult = true;
		} catch (Exception e) {
			log_testActions.info("Unable to find Element to visible--- " + e);
			success = false;
			testRunner.testStepResult = false;
		}
		return success;
	}

	/*
	 ********************************************************************************************************************
	 * Method : selectOption Description :Used to select Dropdown option Author:
	 * Ashok
	 **********************************************************************************************************************
	 */

	public static void selectOption(String dpdn, String data, String pageName, String stepName) {

		try {

			if (data.equals("")) {
				log_testActions.info(stepName);

				WebElement dpdnEle = getObject(getObjectLocator(dpdn));

				Select selectDPDN = new Select(dpdnEle);
				List<WebElement> listOptions = selectDPDN.getOptions();

				selectDPDN.selectByIndex(1);

			} else {
				log_testActions.info(stepName);

				WebElement dpdnEle = getObject(getObjectLocator(dpdn));

				Select selectDPDN = new Select(dpdnEle);
				List<WebElement> listOptions = selectDPDN.getOptions();

				selectDPDN.selectByVisibleText(data);

			}

			testRunner.testStepResult = true;

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to select --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}
	/*
	 * *****************************************************************************
	 * ***************** Method: selectOptionByValue Author: Ashok Description:
	 * Select the dropdown value using value
	 * *****************************************************************************
	 * ****************
	 */

	public static void selectOptionByValue(String dpdn, String data, String pageName, String stepName) {

		try {

			log_testActions.info(stepName);

			WebElement dpdnEle = getObject(getObjectLocator(dpdn));

			Select selectDPDN = new Select(dpdnEle);
			List<WebElement> listOptions = selectDPDN.getOptions();
			selectDPDN.selectByValue(data);
			testRunner.testStepResult = true;

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to select --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: selectOptionUsingPartialText Author: Ashok
	 * Description: Select the drop down value using Partial text
	 * *****************************************************************************
	 * ****************
	 */
	public static void selectOptionUsingPartialText(String dpdn, String data, String pageName, String stepName) {

		String txtData = data;

		try {

			log_testActions.info(stepName);

			if (dpdn.contains("Maersk_ssp_imo_number")) {
				Thread.sleep(2000);
			}

			WebElement dpdnEle = getObject(getObjectLocator(dpdn));

			Select selectDPDN = new Select(dpdnEle);
			List<WebElement> listOptions = selectDPDN.getOptions();
			// List<WebElement> listOptions=selectDPDN.getAllSelectedOptions();

			System.out.println("Print otions:" + listOptions);
			for (int i = 0; i <= listOptions.size(); i++) {

				String sValue = listOptions.get(i).getText();
				if (sValue.contains(txtData)) {
					selectDPDN.selectByIndex(i);
					testRunner.testStepResult = true;
					break;

				}

			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to select --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : moveClick Description :Used to move cursor and click on web element
	 * Author: Santhosh
	 **********************************************************************************************************************
	 */
	public static void moveClick(String object, String data, String pageName, String stepName) {
		try {
			Thread.sleep(5000);
			log_testActions.info("Moving on main menu " + object);
			String[] locaotrsTypeValues = getLocatorTypeValues(object);
			String locatorType = locaotrsTypeValues[0];
			String locatorValue1 = locaotrsTypeValues[1].split("&")[0];
			String locatorValue2 = locaotrsTypeValues[1].split("&")[1];
			log_testActions.info("locatorValue1 " + locatorValue1);
			log_testActions.info("locatorValue2 " + locatorValue2);

			log_testActions.info("Before clicking main menu");
			WebElement menu = getObject(getObjectLocator(locatorType, locatorValue1));
			Actions action = new Actions(driver);
			action.moveToElement(menu).moveToElement(getObject(getObjectLocator(locatorType, locatorValue2))).click()
					.build().perform();

			testRunner.testStepResult = true;
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to move --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifyList Author: Ashok Description: verify the
	 * List items in Dropdown
	 * *****************************************************************************
	 * ****************
	 */

	public static void verifyList(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before Verifying " + object + " in " + pageName);
		ArrayList<String> inList;
		ArrayList<String> outList;
		ArrayList<WebElement> eleList;
		String[] inArray;
		try {
			if (isTestElementPresent(object)) {
				inArray = data.split(",");
				inList = new ArrayList<String>(Arrays.asList(inArray));
				eleList = (ArrayList<WebElement>) getObjects(getObjectLocator(object));
				outList = new ArrayList<String>(eleList.size());
				boolean success = true;
				for (WebElement ele : eleList) {
					outList.add(ele.getText());
				}

				int inListSize = inList.size();
				int outListSize = outList.size();

				if (inListSize == outListSize) {
					for (int i = 0; i < inListSize; i++) {

						if (inList.contains(outList.get(i))) {
							log_testActions.info("The inList item = " + inList.get(i) + " Equal to The outList item="
									+ outList.get(i));
							success = true;
							i++;
						} else {
							log_testActions.info("The inList item = " + inList.get(i)
									+ " Not Equal to The outList item=" + outList.get(i));
							success = false;
							break;
						}

					}
				} else {
					log_testActions.info("The size of inList=" + inListSize + " The size of outList=" + outListSize);
					success = false;
				}

				if (success) {
					testRunner.testStepResult = true;
					log_testActions.info("Verifying List of" + object + " in " + pageName + " Success");

				} else {

					log_testActions.info("Verifying List of" + object + " in " + pageName + " Fail");
					testRunner.testStepResult = false;

				}
			} else {
				testRunner.testStepResult = false;
				log_testActions.info("Verifying " + object + " in " + pageName + " Not found");

			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;

		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: getcustomerCode Author: Ashok Description: Getting
	 * custmer code
	 * *****************************************************************************
	 * ****************
	 */

	public static String getcustomerCode(String object) {

		log_testActions.info("Before getcustomerCode");

		String customerCode = "";

		try {
			customerCode = getInnerTextValue(object, "", "", "");
		} catch (Exception ex) {
			log_testActions.info("Exception=" + ex.getMessage());
		}

		return customerCode;
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: getbokingNumber Author: Ashok Description: Getting
	 * runtime booking number
	 * *****************************************************************************
	 * ****************
	 */

	public static String getbokingNumber(String object, String data, String pageName, String StepName) {

		log_testActions.info("Before getcustomerCode");

		String bookinNumber = "";
		try {

			/*
			 * bookingNumber =getInnerTextValue(object,"","","");
			 * 
			 * bookingNumber=constants.Booking_Number;
			 */
			bookinNumber = driver.findElement(By.xpath("//*[contains(text(),'Your booking has number')]")).getText();
			String partialBooking_Number = bookinNumber.replaceAll("\\D", "");

			String example = bookinNumber;
			String AllWords[] = example.split(" ");

			for (int i = 0; i < AllWords.length; i++) {
				if (AllWords[i].contains(partialBooking_Number)) {
					constants.Booking_Number = AllWords[i];
					constants.Booking_Number = constants.Booking_Number.replaceAll("[.]", "");

					// System.out.println("Booking number
					// "+constants.Booking_Number.replaceAll("[.]", ""));
					break;
				}

			}
			System.out.println(constants.Booking_Number);
			log_testActions.info(" Shipment Booking Number = " + constants.Booking_Number);
			log_testActions.info("napBooking Success ");
			testRunner.testStepResult = true;
			testRunner.reportLogger.log(LogStatus.PASS, "Your Booking Number is:  " + constants.Booking_Number,
					constants.KEYWORD_PASS);

		} catch (Exception ex) {

			log_testActions.info("Exception=" + ex.getMessage());
			testRunner.testStepResult = false;
		}

		return bookinNumber;

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: clearTextBox Author: Ashok Description: Clear the
	 * existing text value
	 * *****************************************************************************
	 * ****************
	 */

	public static void clearTextBox(String object, String data, String pageName, String StepName) {
		try {
			log_testActions.info("Clearing the text in " + object + " in " + pageName);

			waitForVisible(object);
			log_testActions.info("Element Found. Clearing value of " + object);
			getObject(getObjectLocator(object)).clear();

			testRunner.testStepResult = true;

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to Clear the text in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 ***********************************************************************************************
	 * Method: verifyCharCount Author: Ashok Description: Used to verify Character
	 * count
	 ***********************************************************************************************
	 */
	public static void verifyCharCount(String object, String data, String pageName, String StepName) {
		try {

			String WebElementText = getObject(getObjectLocator(object)).getAttribute("value");

			// String s = "...";
			int counter = 0;
			for (int i = 0; i < WebElementText.length(); i++) {
				if (Character.isLetter(WebElementText.charAt(i)))
					counter++;
			}
			System.out.println(counter);
			int expectedCount = Integer.parseInt(data);
			if (counter == expectedCount) {
				testRunner.testStepResult = true;
			} else {
				log_testActions.info("Character count accepted  " + object);
				testRunner.testStepResult = false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to Clear the text in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: selectDPDNValue Author: Ashok Description: Select
	 * list value
	 * *****************************************************************************
	 * ****************
	 */

	public static void selectDPDNValue(String dpdnElement, String dpdnTextBox, String invalue1, String inValue2,
			String pageName, String dpdnName) throws Exception {
		String dpdnListValue;
		String input = invalue1;

		try {
			if (dpdnName.equals("Commodity")) {
				dpdnListValue = "Xpath;//*[text()='" + input + "']/parent::div";

			} else if (dpdnName.equals("ContainerType")) {

				String conCategory = input.split("-")[0];
				String conSizeType = input.split("-")[1];
				String conSize = conSizeType.substring(0, 2);
				String conType = conSizeType.substring(4);
				input = conSizeType;
				if (conCategory.contains("Shipper")) {

					if (conType.contains("Reefer"))
						dpdnListValue = "Xpath;//div[contains(text(),'" + conCategory
								+ "')]/parent::li[contains(@class,'shippersownReefer')]";
					else
						dpdnListValue = "Xpath;//div[contains(text(),'" + conCategory
								+ "')]/parent::li[contains(@class,'shippersownOversized')]";
				} else {

					dpdnListValue = "Xpath;//div[contains(text(),'" + conCategory + "')]/parent::li";
				}

			} else {
				dpdnListValue = "Xpath;//*[text()='" + input + "']/parent::div[text()='" + inValue2 + "']";
			}

			// clickElement(dpdnElement, "", pageName, "Click on " + dpdnName + "
			// dropdown");
			// input(dpdnTextBox, input, pageName, "Enter value in " + dpdnName + "
			// textbox");
			// clickElement(dpdnListValue, "", pageName, "Select value " + dpdnName + "
			// dropdown");

			WebElement dropdown = getObject(getObjectLocator(dpdnElement));
			dropdown.click();
			input(dpdnTextBox, input, pageName, "Enter value in " + dpdnName + " textbox");
			WebElement lsitvalue = getObject(getObjectLocator(dpdnListValue));
			lsitvalue.click();

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" napBooking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: checkSelected Author: Ashok Description: Verify the
	 * Check box is Selected
	 * *****************************************************************************
	 * ****************
	 */

	public static String checkSelected(String object, String data, String pageName, String StepName) {
		log_testActions.info("checking checkbox selection " + object + " in " + pageName);

		WebElement ele = null;
		String selected = "";
		try {
			ele = getObject(getObjectLocator(object));
			if (ele.isSelected()) {
				testRunner.testStepResult = true;
				selected = "checked";
			} else {
				testRunner.testStepResult = false;
				selected = "notchecked";
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error(object + " Not able to Check. Exception =  " + testRunner.stepException);
			testRunner.testStepResult = false;
		}

		return selected;

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: checkDisabled Author: Ashok Description: Verify the
	 * Check Box is Disabled
	 * *****************************************************************************
	 * ****************
	 */

	public static String checkDisabled(String object, String data, String pageName, String StepName) {
		log_testActions.info("checking checkbox disabled " + object + " in " + pageName);

		WebElement ele = null;
		String disabled = "";
		try {
			ele = getObject(getObjectLocator(object));
			if (!ele.isEnabled()) {
				testRunner.testStepResult = true;
				disabled = "disabled";
			} else {
				testRunner.testStepResult = false;
				disabled = "enabled";
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error(object + " Not able to Check. Exception =  " + testRunner.stepException);
			testRunner.testStepResult = false;
		}

		return disabled;

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: OOGShipperownSingleCargoBooking Author: Ashok
	 * Description: Creating OOGShipperownSingleCargoBooking
	 * *****************************************************************************
	 * ****************
	 */

	public static void OOGShipperownSingleCargoBooking(String object, String data, String pageName, String StepName)
			throws Exception {

		try {
			String[] objectValue = object.split(";");
			String MainMenu = objectValue[0];
			String subMenu = objectValue[1];
			String[] testData = data.split(";");

			log_testActions.info("Before OOG Shipper own Single Cargo Booking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String contractTariff = testData[9];
			// String contactPerson = testData[10];

			String reference = testData[10];
			String exportType = testData[11];
			String importType = testData[12];
			String haulage_Instructions = testData[13];
			String loadAddress = testData[14];

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date todayDate = Calendar.getInstance().getTime();
			String todayDateString = formatter.format(todayDate);

			String[] cont1LoadDetails = testData[15].split("/");
			String cont1LoadFromDate = cont1LoadDetails[0].equals("today") ? todayDateString : cont1LoadDetails[0];
			String cont1LoadFromTime = cont1LoadDetails[1];
			String cont1LoadToDate = cont1LoadDetails[2].equals("today") ? todayDateString : cont1LoadDetails[2];
			String cont1LoadToTime = cont1LoadDetails[3];
			String cont1LoadRef = cont1LoadDetails[4];

			String[] cont1Dimention1 = testData[16].split("/");
			String length1 = cont1Dimention1[0];
			String width1 = cont1Dimention1[1];
			String height1 = cont1Dimention1[2];

			// String CargoDetails = testData[19];
			String additionalRefType = testData[17];
			String additionalRefValue = testData[18];
			String Temparature = testData[19];

			log_testActions.info("After getting input values");

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
			 * "navigated to new booking page");
			 */
			/*
			 * navigateMenu(
			 * "Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New BookShipment"
			 * ,"Booking_Page","Nvaigate to new Booking page");
			 * log_testActions.info("After Booking page");
			 */
			if (testRunner.testName.contains("_MML_")) {

				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			waitForVisible("txt_BookingPage1");

			compareValue("txt_BookingPage1", "Booked By", "Booking_Page", "Verify Booking Page");

			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");

			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");
			// New changes
			if (exportType.equals("SD")) {
				waitForVisible("radio_Export_SD");
				clickElement("radio_Export_SD", "", "Booking_Page2", "Select Export SD mode");
			}

			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");
			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");
			/*
			 * if(conType.contains("Oversized")){
			 * 
			 * clickElement("chk_cargoOversized", "", "Booking_Page",
			 * "Select Cargo Oversized");
			 * 
			 * }
			 */

			// Added by Ashok
			clickElement("Maersk_SSP_chk_Shippers1stContainer", "", "Booking_Page",
					"Select Shippers1stContainer checkbox");

			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");

			clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on iam price owner check box");

			// clickElement("chkbx_tariff", "", "Booking_Page", "Select Tariff");
			// input("txtbx_contract_BP", contractTariff, "Booking_Page", "Enter Contract
			// number");
			// New changes
			selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", "Gcss Booked By, Copenhagen", pageName, StepName);

			clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");

			waitForVisible("txt_BookingPage2");
			compareValue("txt_BookingPage2", "Configure your booking details below", "Booking_Page2",
					"Verify Booking Page2");

			input("txtbx_BookedbyReference", reference, "Booking_Page2", "Enter Booked by Reference");

			javaClick("Maersk_SSP_lnk_addHaulage1", "", "Booking page2", "Click on add haulage link");
			// selectOption("select_LoadAddress", loadAddress, "Booking_Page2", "Select Load
			// Address");
			if (conType.contains("Oversized")) {

				selectOptionUsingPartialText("dpdn_ReleaseDepotCity", loadAddress, "Booking_Page2",
						"Select Load Address");
				input("txtbx_releaseDate", "currDate", "Booking_Page2", "Enter Booked by Reference");
				input("txtbx_releaseTime", "1000", "Booking_Page2", "Enter Booked by Reference");

			} else {
				selectOptionUsingPartialText("select_LoadAddress", loadAddress, "Booking_Page2", "Select Load Address");
			}

			// input("txtbx_HaulageInstructions", haulage_Instructions, "Booking_Page2",
			// "Enter Booked by Reference");
			if (conTypeValue.contains("Reefer")) {

				input("txtbx_Temerature", Temparature, "Booking_Page2", "Enter Temparature value");
			}

			if (exportType.equals("SD")) {

				input("txtbx_Cont1LoadFromDate", cont1LoadFromDate, "Booking_Page2", "Enter Container1 Load From Date");
				input("txtbx_Cont1LoadFromTime", cont1LoadFromTime, "Booking_Page2", "Enter Container1 Load From Time");
				input("txtbx_Cont1LoadToDate", cont1LoadToDate, "Booking_Page2", "Enter Container1 Load To Date");
				input("txtbx_Cont1LoadToTime", cont1LoadToTime, "Booking_Page2", "Enter Container1 Load To Time");
				input("txtbx_Cont1LoadRef", cont1LoadRef, "Booking_Page2", "Enter Container1 Load To Time");
			}

			if (conType.contains("Oversized")) {
				javaClick("Maersk_SSP_lnk_AddOverSizedCargoDetails", "", "Booking_Page2", "Click on AddOversized link");
				input("Maersk_SSP_txtbx_QuoteReference", "oversized", "Booking_Page2", "Enter length of container1");
				input("Maersk_SSP_txtbx_Container1Length", length1, "Booking_Page2", "Enter length of container1");
				input("Maersk_SSP_txtbx_Container1Width", width1, "Booking_Page2", "Enter  width of container1");
				input("Maersk_SSP_txtbx_Container1Hight", height1, "Booking_Page2", "Enter height of container1");
				input("Maersk_SSP_txtbx_Container1Wieght", "500", "Booking_Page2", "Enter height of container1");
				javaClick("Maersk_SSP_lnk_ApplyAddOverSizedDetails", "", "Booking_Page2", "Click on Apply button");

			}
			selectOption("select_AdditionalReference", "Export License Received", "Booking_Page2",
					"Select Export License Received");
			input("select_YesNo", "Yes", "Booking_Page2", "Select Yes value");
			clickElement("lnk_AdditionalReferences", "", "Booking_Page2", "Click Add additional reference link");
			selectOption("select_AdditionalReference2", "OOG-BB Quote Reference Number", "Booking_Page2",
					"OOG-BB Quote Reference Number");
			input("txtbx_AdditionalReference2", "11111", "Booking_Page2", "enter value 11111");
			clickElement("lnk_AdditionalReferences", "", "Booking_Page2", "Click Add additional reference link");
			selectOption("select_AdditionalReference3", "UCR (Shipper Unique Consignment Ref)", "Booking_Page2",
					"UCR (Shipper Unique Consignment Ref)");
			input("txtbx_AdditionalReference3", "22222", "Booking_Page2", "enter value 22222");
			clickElement("btn_BookShipment", "", "Booking_Page2", "Click Book Shipment");

			javaClick("btn_PlaceBooking", "", "Booking_Page2", "Click Place Booking");

			compareValue("txt_BookingSuccessMessage", "Your booking has number", "Booking_Page",
					"Verify Booking Page2");
			String bookinNumber = driver.findElement(By.xpath("//*[contains(text(),'Your booking has number')]"))
					.getText();

			constants.Booking_Number = bookinNumber.replaceAll("\\D", "");

			if (testRunner.testName.contains("_SGL_")) {
				constants.Booking_Number = "SGL" + constants.Booking_Number;

			}
			if (testRunner.testName.contains("_SEA_")) {
				constants.Booking_Number = "SLD" + constants.Booking_Number;

			}
			if (testRunner.testName.contains("_MCC_")) {
				constants.Booking_Number = "MCB" + constants.Booking_Number;

			}
			System.out.println(constants.Booking_Number);
			log_testActions.info(" Shipment Booking Number = " + constants.Booking_Number);
			testRunner.reportLogger.log(LogStatus.PASS,
					"B5 OOGShipperownSingleCargoBooking number " + constants.Booking_Number, constants.KEYWORD_PASS);

			log_testActions.info("OOGShipperownSingleCargoBooking Booking Success ");

		}

		catch (Exception ex) {

			log_testActions.info(" OOGShipperownDGMultiCargo Booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}

	}
	/*
	 * *****************************************************************************
	 * ***************** Method: checkEnabled Author: Ashok Description: To Verify
	 * Check box enabled
	 * *****************************************************************************
	 * ****************
	 */

	public static String checkEnabled(String object, String data, String pageName, String StepName) {
		log_testActions.info("checking checkbox Enabled " + object + " in " + pageName);

		WebElement ele = null;
		String disabled = "";
		try {
			ele = getObject(getObjectLocator(object));
			if (ele.isEnabled()) {
				testRunner.testStepResult = true;
				disabled = "enabled";
			} else {
				testRunner.testStepResult = false;
				disabled = "disabled";
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error(object + " Not able to Check. Exception =  " + testRunner.stepException);
			testRunner.testStepResult = false;
		}

		return disabled;

	}
	/*
	 * *****************************************************************************
	 * ***************** Method: checkUnSelected Author: Ashok Description: To
	 * Verify Check box is unchecked
	 * *****************************************************************************
	 * ****************
	 */

	public static String checkUnSelected(String object, String data, String pageName, String StepName) {
		log_testActions.info("checking checkbox selection " + object + " in " + pageName);

		WebElement ele = null;
		String notSelected = "";

		try {
			ele = getObject(getObjectLocator(object));
			if (ele.isSelected()) {
				testRunner.testStepResult = false;
				notSelected = "no";
			} else {
				testRunner.testStepResult = true;
				notSelected = "yes";
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error(object + " Not able to Check. Exception =  " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
		return notSelected;

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: napBooking Author: Ashok Description: To create
	 * normal booking
	 * *****************************************************************************
	 * ****************
	 */

	public void napBooking(String object, String data, String pageName, String StepName) throws Exception {
		constants.Booking_Number = null;

		try {
			String[] testData = data.split(";");
			int length = testData.length;

			log_testActions.info("Before napBooking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String contractTariff = testData[9];
			String contactPerson = testData[10];
			String reference = testData[11];
			String exportType = testData[12];
			String importType = testData[13];
			String priceOwner = testData[14];

			// navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New
			// BookShipment","Booking_Page","Nvaigate to new Booking page");
			if (testRunner.testName.contains("_MML_")) {

				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			waitForVisible("txt_BookingPage1");
			compareValue("txt_BookingPage1", "Book New Shipments", "Booking_Page", "Verify Booking Page");
			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");
			log_testActions.info(" From Address Selected as  = " + fromValue1);
			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");
			log_testActions.info(" To Address Selected as  = " + toValue1);
			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");
			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");
			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
			clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on iam price owner check box");

			// functinality changed as R38
			// clickElement("chkbx_tariff", "", "Booking_Page", "Select Tariff");

			selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", priceOwner, pageName, StepName);
			clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");
			waitForVisible("txt_BookingPage2");
			compareValue("txt_BookingPage2", "Configure your booking details below", "Booking_Page2",
					"Verify Booking Page2");
			input("txtbx_BookedbyReference", reference, "Booking_Page2", "Enter Booked by Reference");

			javaClick("btn_BookShipment", "", "Booking_Page2", "Click Book Shipment");
			javaClick("btn_PlaceBooking", "", "Booking_Page2", "Click Place Booking");
			// driver.getPageSource().contains("Text which you looking for");
			compareValue("txt_BookingSuccessMessage", "Your booking has number", "Booking_Page",
					"Verify Booking Page2");
			String bookinNumber = driver.findElement(By.xpath("//*[contains(text(),'Your booking has number')]"))
					.getText();
			String partialBooking_Number = bookinNumber.replaceAll("\\D", "");

			String example = bookinNumber;
			String AllWords[] = example.split(" ");

			for (int i = 0; i < AllWords.length; i++) {
				if (AllWords[i].contains(partialBooking_Number)) {
					constants.Booking_Number = AllWords[i];
					constants.Booking_Number = constants.Booking_Number.replaceAll("[.]", "");

					// System.out.println("Booking number
					// "+constants.Booking_Number.replaceAll("[.]", ""));
					break;
				}

			}

			System.out.println(constants.Booking_Number);
			log_testActions.info(" Shipment Booking Number = " + constants.Booking_Number);
			log_testActions.info("napBooking Success ");
			testRunner.reportLogger.log(LogStatus.PASS, "B1 Napbooking Number " + constants.Booking_Number,
					constants.KEYWORD_PASS);

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" napBooking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	public void preRequisite(String object, String data, String pageName, String StepName) throws Exception {
		constants.Booking_Number = null;

		try {
			String[] testData = data.split(";");
			int length = testData.length;

			goToUrl("https://myt.apmoller.net/backoffice2/index", "", "Home_Page",
					"navigating to the back ofiice page");
			selectListValue("Maersk_SSP_dpdn_featureList;Maersk_SSP_txtbx_featureList", "DEADLINE_FEATURE;;Other", "",
					"");
			javaClick("Maersk_SSP_rdo_Enabled", "", "", "");
			javaClick("Maersk_SSP_lnk_SelectAll", "", "", "");
			javaClick("Maersk_SSP_rdo_SubmitBackOffice", "", "", "");

			selectListValue("Maersk_SSP_dpdn_featureList;Maersk_SSP_txtbx_featureList", "DANGEROUS_DETAILS;;Other", "",
					"");
			javaClick("Maersk_SSP_rdo_Disabled", "", "", "");
			javaClick("Maersk_SSP_lnk_SelectAll", "", "", "");
			javaClick("Maersk_SSP_rdo_SubmitBackOffice", "", "", "");

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" napBooking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: bookingFirstPage Author: Ashok Description: Enter
	 * All values for Booking first page
	 * *****************************************************************************
	 * ****************
	 */

	public static void bookingFirstPage(String object, String data, String pageName, String StepName) throws Exception {

		try {
			String[] testData = data.split(";");
			int length = testData.length;

			log_testActions.info("Before napBooking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String contractTariff = testData[9];
			// String contactPerson = testData[10];
			String reference = testData[10];
			String exportType = testData[11];
			String importType = testData[12];

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
			 * "navigated to new booking page");
			 */
			/*
			 * navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment",data,
			 * "Booking_Page","Nvaigate to new Booking page");
			 */
			if (testRunner.testName.contains("_MML_")) {

				// IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment","BOOK~New
				// BookShipment","Booking_Page","Nvaigate to new Booking page");

				clickElement("Maersk_SSP_lnk_BookNewShipments", "BOOK~New BookShipment", "Booking_Page",
						"Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				// navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New
				// BookShipment","Booking_Page","Nvaigate to new Booking page");

				clickElement("Maersk_SSP_lnk_BookNewShipments", "BOOK~New BookShipment", "Booking_Page",
						"Nvaigate to new Booking page");

			}

			waitForVisible("txt_BookingPage1");

			// javaClick("Maersk_btn_AcceptCookies","","Booking_Page","Click on Accept
			// cookies");

			compareValue("", "Book New Shipments", "Booking_Page", "Verify Booking Page");
			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");
			log_testActions.info(" From Address Selected as  = " + fromValue1);
			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");
			log_testActions.info(" To Address Selected as  = " + toValue1);

			selectRateLookUpRateCaluculationDate(
					"Maersk_SSP_txtbx_DepartureDate;Maersk_SSP_lst_DepartureMonth;Maersk_SSP_lst_DepartureYear",
					"days;1;No", "Booking_Page", "");

			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");
			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");
			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
			clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on iam price owner check box");
			selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", "Gcss Booked By, Copenhagen", pageName, StepName);
			// clickElement("chkbx_tariff", "", "Booking_Page", "Select Tariff");

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" napBooking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: reeferStarfreshSingleCargoBooking Author: Ashok
	 * Description: Creating reeferStarfreshSingleCargoBooking
	 * *****************************************************************************
	 * ****************
	 */

	public static void reeferStarfreshSingleCargoBooking(String object, String data, String pageName, String StepName)
			throws Exception {

		try {
			String[] testData = data.split(";");
			int length = testData.length;

			log_testActions.info("Before reefer Star fresh Single Cargo Booking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String contractTariff = testData[9];
			// String contactPerson = testData[10];
			String reference = testData[10];
			String exportType = testData[11];
			String importType = testData[12];
			String reeferConType = testData[13];
			String reeferTemp = testData[14];
			String additionalRefType = testData[15];
			String additionalRefValue = testData[16];

			log_testActions.info("After getting input values");

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
			 * "navigated to new booking page");
			 */
			/*
			 * navigateMenu(
			 * "Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New BookShipment"
			 * ,"Booking_Page","Nvaigate to new Booking page");
			 * log_testActions.info("After Booking page");
			 */
			if (testRunner.testName.contains("_MML_")) {

				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			waitForVisible("txt_BookingPage1");
			if (exportType.equals("SD") && importType.equals("CY")) {
				javaClick("radio_Export_SD", "", "Booking_page1", "Select export SD radio button");
				javaClick("radio_Import_CY", "", "Booking_page1", "Select export SD radio button");

			}

			compareValue("txt_BookingPage1", "Booked By", "Booking_Page", "Verify Booking Page");

			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");

			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");

			clickElement("chk_cargoTepControl", "", "Booking_Page", "Select Cargo Requires Temperature control");

			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");

			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");

			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
			clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on iam price owner check box");

			// clickElement("chkbx_tariff", "", "Booking_Page", "Select Tariff");
			selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", "Gcss Booked By, Copenhagen", pageName, StepName);

			clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");
			waitForVisible("txt_BookingPage2");
			compareValue("txt_BookingPage2", "Configure your booking details below", "Booking_Page2",
					"Verify Booking Page2");
			input("txtbx_BookedbyReference", reference, "Booking_Page2", "Enter Booked by Reference");
			if (exportType.equals("SD") && importType.equals("CY")) {

				// Added new Steps by Ashok
				selectOption("Maersk_SSP_lst_LoadAddressSDCY", "Kuehne + Nagel (ag & Co) Kg, Cologne",
						"Select Load address", "Booking Page2");
				input("txtbx_Cont1LoadFromDate", "currDate", "Enter Laoding from date", "Booking Page2");
				input("txtbx_Cont1LoadFromTime", "1000", "Enter Laoding from Time", "Booking Page2");
				input("txtbx_Cont1LoadToDate", "currDate", "Enter Laoding from date", "Booking Page2");
				input("txtbx_Cont1LoadToTime", "2300", "Enter Laoding to Time", "Booking Page2");
				input("txtbx_Cont1LoadRef", "1", "Enter Refrence number", "Booking Page2");
			}

			clickElement("link_showAdvancedConfigurations", "", "Booking_Page2",
					"Clicking Show Advanced configuration");
			clickElement("radio_starfresh", "", "Booking_Page2", "Select Star refresh");

			log_testActions.info("Select Starfresh passed");

			input("txtbx_Temerature", reeferTemp, "Booking_Page2", "Enter Temperature");

			log_testActions.info("Enter Temperature Passed");
			selectOption("select_AdditionalReference", additionalRefType, "Booking_Page2",
					"Select Additional Reference");
			selectOption("select_YesNo", additionalRefValue, "Booking_Page2", "Select Yes Or No");

			clickElement("btn_BookShipment", "", "Booking_Page2", "Click Book Shipment");

			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollBy(0,-250)", "");
			Thread.sleep(1000);
			javaClick("btn_PlaceBooking", "", "Booking_Page2", "Click Place Booking");
			compareValue("txt_BookingSuccessMessage", "Your booking has number", "Booking_Page",
					"Verify Booking Page2");
			String bookinNumber = driver.findElement(By.xpath("//*[contains(text(),'Your booking has number')]"))
					.getText();
			constants.Booking_Number = bookinNumber.replaceAll("\\D", "");
			System.out.println(constants.Booking_Number);
			log_testActions.info(" Shipment Booking Number = " + constants.Booking_Number);
			testRunner.reportLogger.log(LogStatus.PASS,
					"B10 reeferStarfreshSingleCargo Booking number " + constants.Booking_Number,
					constants.KEYWORD_PASS);

			if (testRunner.testStepResult == true) {
				log_testActions.info("reeferStarfreshSingleCargo Booking Success ");

			} else {
				log_testActions.info("reeferStarfreshSingleCargo Booking failed ");
			}

		} catch (Exception ex) {

			log_testActions.info(" reeferStarfreshSingleCargo Booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: OOGShipperownDGMultiCargoBooking Author: Ashok
	 * Description: Create OOGShipperownDGMultiCargoBooking booking
	 * *****************************************************************************
	 * ****************
	 */
	public static void OOGShipperownDGMultiCargoBooking(String object, String data, String pageName, String StepName)
			throws Exception {

		try {
			String[] testData = data.split(";");
			int length = testData.length;

			log_testActions.info("Before OOG Shipper own DG Multi Cargo Booking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String commValue2 = testData[9];
			String contCount2 = testData[10];
			String conTypeValue2 = testData[11];
			String weight2 = testData[12];
			String contractTariff = testData[13];
			// String contactPerson = testData[10];

			String reference = testData[14];
			String exportType = testData[15];
			String importType = testData[16];
			String[] cont1Dimention1 = testData[17].split("/");
			String length1 = cont1Dimention1[0];
			String width1 = cont1Dimention1[1];
			String height1 = cont1Dimention1[2];
			String[] cont1Dimention2 = testData[18].split("/");
			String length2 = cont1Dimention2[0];
			String width2 = cont1Dimention2[1];
			String height2 = cont1Dimention2[2];

			String CargoDetails = testData[19];
			String additionalRefType = testData[20];
			String additionalRefValue = testData[21];

			log_testActions.info("After getting input values");

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
			 * "navigated to new booking page");
			 */
			// navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New
			// BookShipment","Booking_Page","Nvaigate to new Booking page");
			if (testRunner.testName.contains("_MML_")) {

				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			log_testActions.info("After Booking page");

			waitForVisible("txt_BookingPage1");

			compareValue("txt_BookingPage1", "Booked By", "Booking_Page", "Verify Booking Page");

			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");

			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");

			clickElement("chk_cargoDangerous", "", "Booking_Page", "Select Cargo Dangerous");
			// clickElement("chk_cargoOversized", "", "Booking_Page", "Select Cargo
			// Oversized");

			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");

			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");

			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
			clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on iam price owner check box");
			// Added by Ashok
			clickElement("Maersk_SSP_chk_Shippers1stContainer", "", "Booking_Page", "Select ShippersOwnCheckBox");

			javaClick("lnk_AddConTypeComm", "", "Booking_Page", "click Add another Container/Commodity");

			selectDPDNValue("dpdn_Commodity2_BP", "txtbx_Commodity2_BP", commValue2, "", "Booking_Page", "Commodity");

			selectDPDNValue("dpdn_Container2_BP", "txtbx_Container2_BP", conTypeValue2, "", "Booking_Page",
					"ContainerType");

			input("txtbx_Weight2_BP", weight2, "Booking_Page", "Enter weight2");
			// Added by Ashok
			clickElement("Maersk_SSP_chk_ShippersOwn2ndContainer", "", "Booking_Page", "Select ShippersOwnCheckBox");

			// selectCheckbox("chkbx_tariff", "", "Booking_Page", "Select
			// Tariff");
			// input("txtbx_contract_BP", contractTariff, "Booking_Page", "Enter Contract
			// number");
			selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", "Gcss Booked By, Copenhagen", pageName, StepName);

			clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");

			waitForVisible("txt_BookingPage2");
			compareValue("txt_BookingPage2", "Configure your booking details below", "Booking_Page2",
					"Verify Booking Page2");

			input("txtbx_BookedbyReference", reference, "Booking_Page2", "Enter Booked by Reference");
			input("txtbx_contract_BP", contractTariff, "Booking_Page", "Enter Contract number");

			javaClick("lnk_EditContainerDetails", "", "Booking_Page2", "Click Edit container Details");
			comparePageText("", "Shipper~Yes", "", "");
			clickElement("btn_CancelEditContainerDetails", "", "Booking_Page2", "Click Edit container Details");
			// UnCheck("chk_container2Details", "", "Booking_Page2", "Selecting container2
			// details checkbox");
			javaClick("lnk_AddOversizedcargoDetails1", "", "Booking_Page2", "clicking Add Oversized Details link ");
			// Added by Ashok
			input("Maersk_SSP_txtbx_QuoteReference", "abcd", "Booking_Page2", "Enter length of container1");
			input("Maersk_SSP_txtbx_Container1Length", length1, "Booking_Page2", "Enter length of container1");
			input("Maersk_SSP_txtbx_Container1Width", width1, "Booking_Page2", "Enter  width of container1");
			input("Maersk_SSP_txtbx_Container1Hight", height1, "Booking_Page2", "Enter height of container1");
			input("Maersk_SSP_txtbx_Container1Wieght", "1000", "Booking_Page2", "Enter height of container1");
			javaClick("Maersk_SSP_lnk_AddOversizedDetailsApply", "", "Booking_Page2", "Click Update for container1");
			// clickElement("chk_container2Details", "", "Booking_Page2", "Selecting
			// container2 details checkbox");
			// UnCheck("chk_container1Details", "", "Booking_Page2", "Unselecting container1
			// details checkbox");
			clickElement("lnk_AddOversizedcargoDetails2", "", "Booking_Page2", "clicking Add Oversized Details link ");
			input("Maersk_SSP_txtbx_QuoteReference", "abcd", "Booking_Page2", "Enter length of container1");
			input("Maersk_SSP_txtbx_Container1Length", length1, "Booking_Page2", "Enter length of container1");
			input("Maersk_SSP_txtbx_Container1Width", width1, "Booking_Page2", "Enter  width of container1");
			input("Maersk_SSP_txtbx_Container1Hight", height1, "Booking_Page2", "Enter height of container1");
			input("Maersk_SSP_txtbx_Container1Wieght", "1000", "Booking_Page2", "Enter height of container1");
			clickElement("Maersk_SSP_lnk_AddOversizedDetailsApply", "", "Booking_Page2", "Click Update for container2");
			// clickElement("chk_container1Details", "", "Booking_Page2", "selecting
			// container1 details checkbox");
			input("txtbx_DangerousCargoDetails", CargoDetails, "Booking_Page2", "Enter Dangerous cargo details");
			selectOption("select_AdditionalReference", "OOG-BB Quote Reference Number", "Booking_Page2",
					"Select OOG-BB Quote Reference Number");

			input("txtbx_OOG-BBQuoteRefNum", "Yes", "Booking_Page2", "Enter Yes value");
			clickElement("btn_BookShipment", "", "Booking_Page2", "Click Book Shipment");
			javaClick("btn_PlaceBooking", "", "Booking_Page2", "Click Place Booking");
			compareValue("txt_BookingSuccessMessage", "Your booking has number", "Booking_Page",
					"Verify Booking Page2");
			String bookinNumber = driver.findElement(By.xpath("//*[contains(text(),'Your booking has number')]"))
					.getText();
			constants.Booking_Number = bookinNumber.replaceAll("\\D", "");
			System.out.println(constants.Booking_Number);
			log_testActions.info(" Shipment Booking Number = " + constants.Booking_Number);
			testRunner.reportLogger.log(LogStatus.PASS,
					"B2 OOGShipperownDGMultiCargoBooking number " + constants.Booking_Number, constants.KEYWORD_PASS);

			if (testRunner.testStepResult == true) {
				log_testActions.info("OOGShipperownDGMultiCargoBooking Booking Success ");

			} else {
				log_testActions.info("OOGShipperownDGMultiCargoBooking Booking failed ");

			}
		} catch (Exception ex) {

			log_testActions.info(" OOGShipperownDGMultiCargo Booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: DGReeferStandardMultiCargoBooking Author: Ashok
	 * Description: Create DGReeferStandardMultiCargoBooking booking
	 * *****************************************************************************
	 * ****************
	 */
	public static void DGReeferStandardMultiCargoBooking(String object, String data, String pageName, String StepName)
			throws Exception {

		try {
			String[] testData = data.split(";");

			log_testActions.info("Before DG Reefer Standard MultiCargo Booking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String commValue2 = testData[9];
			String contCount2 = testData[10];
			String conTypeValue2 = testData[11];
			String weight2 = testData[12];
			String contractTariff = testData[13];
			// String contactPerson = testData[10];

			String reference = testData[14];
			String exportType = testData[15];
			String importType = testData[16];
			String reeferConType = testData[17];
			String haulage_Instructions = testData[18];
			String loadAddress = testData[19];

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date todayDate = Calendar.getInstance().getTime();
			String todayDateString = formatter.format(todayDate);

			String[] cont1LoadDetails = testData[20].split("/");
			String cont1LoadFromDate = cont1LoadDetails[0].equals("today") ? todayDateString : cont1LoadDetails[0];
			String cont1LoadFromTime = cont1LoadDetails[1];
			String cont1LoadToDate = cont1LoadDetails[2].equals("today") ? todayDateString : cont1LoadDetails[2];
			String cont1LoadToTime = cont1LoadDetails[3];
			String cont1LoadRef = cont1LoadDetails[4];
			String[] cont2LoadDetails = testData[21].split("/");
			String cont2LoadFromDate = cont2LoadDetails[0].equals("today") ? todayDateString : cont2LoadDetails[0];
			String cont2LoadFromTime = cont2LoadDetails[1];
			String cont2LoadToDate = cont2LoadDetails[2].equals("today") ? todayDateString : cont2LoadDetails[2];
			String cont2LoadToTime = cont2LoadDetails[3];
			String cont2LoadRef = cont2LoadDetails[4];

			String CargoDetails = testData[22];
			String reeferTemp = testData[23];
			String additionalRefType = testData[24];
			String additionalRefValue = testData[25];

			log_testActions.info("After getting input values");

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
			 * "navigated to new booking page");
			 */
			// navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New
			// BookShipment","Booking_Page","Nvaigate to new Booking page");
			if (testRunner.testName.contains("_MML_")) {

				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			log_testActions.info("After Booking page");

			waitForVisible("txt_BookingPage1");

			compareValue("txt_BookingPage1", "Booked By", "Booking_Page", "Verify Booking Page");

			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");

			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");
			javaClick("radio_Export_SD", "", "Booking_Page2", "Select Export SD mode");

			clickElement("chk_cargoTepControl", "", "Booking_Page", "Select Cargo Requires Temperature control");
			clickElement("chk_cargoDangerous", "", "Booking_Page", "Select Dangerous control");

			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");

			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");

			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
			clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on iam price owner check box");
			javaClick("lnk_AddConTypeComm", "", "Booking_Page", "click Add another Container/Commodity");

			selectDPDNValue("dpdn_Commodity2_BP", "txtbx_Commodity2_BP", commValue2, "", "Booking_Page", "Commodity");
			selectDPDNValue("dpdn_Container2_BP", "txtbx_Container2_BP", conTypeValue2, "", "Booking_Page",
					"ContainerType");
			input("txtbx_Weight2_BP", weight2, "Booking_Page", "Enter weight2");

			// javaClick("chkbx_tariff", "", "Booking_Page", "Select Tariff");
			selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", "Gcss Booked By, Copenhagen", pageName, StepName);

			clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");

			waitForVisible("txt_BookingPage2");
			compareValue("txt_BookingPage2", "Configure your booking details below", "Booking_Page2",
					"Verify Booking Page2");

			input("txtbx_BookedbyReference", reference, "Booking_Page2", "Enter Booked by Reference");
			javaClick("Maersk_SSP_lnk_addHaulage1", "", "Booking_Page", "Click Add haulage container1");
			selectOption("select_LoadAddress", "Glock Gesmbh, Deutsch Wagram", "Booking_Page2", "Select Load Address");
			input("txtbx_HaulageInstructions", haulage_Instructions, "Booking_Page2", "Enter Booked by Reference");

			input("txtbx_Cont1LoadFromDate", cont1LoadFromDate, "Booking_Page2", "Enter Container1 Load From Date");
			input("txtbx_Cont1LoadFromTime", cont1LoadFromTime, "Booking_Page2", "Enter Container1 Load From Time");
			input("txtbx_Cont1LoadToDate", cont1LoadToDate, "Booking_Page2", "Enter Container1 Load To Date");
			input("txtbx_Cont1LoadToTime", cont1LoadToTime, "Booking_Page2", "Enter Container1 Load To Time");
			input("txtbx_Cont1LoadRef", cont1LoadRef, "Booking_Page2", "Enter Container1 Load To Time");

			javaClick("Maersk_SSP_lnk_addHaulage2", "", "Booking_Page", "Click Add haulage container2");
			selectOption("Maersk_SSP_lst_LoadAddressCnt2", "Glock Gesmbh, Deutsch Wagram", "Booking_Page2",
					"Select Load Address");
			input("Maersk_SSP_txtbx_HaulageInstructionsCnt2", haulage_Instructions, "Booking_Page2",
					"Enter Booked by Reference");

			clearTextBox("txtbx_Cont2LoadFromDate", cont2LoadFromDate, "Booking_Page2",
					"Clear Container2 Load From Date");
			clearTextBox("txtbx_Cont2LoadFromTime", cont2LoadFromTime, "Booking_Page2",
					"Clear Container2 Load From Time");
			clearTextBox("txtbx_Cont2LoadToDate", cont2LoadToDate, "Booking_Page2", "Clear Container2 Load To Date");
			clearTextBox("txtbx_Cont2LoadToTime", cont2LoadToTime, "Booking_Page2", "Clear Container2 Load To Date");
			clearTextBox("txtbx_Cont1LoadRef", cont2LoadRef, "Booking_Page2", "Enter Container1 Load To Time");

			input("txtbx_Cont2LoadFromDate", cont2LoadFromDate, "Booking_Page2", "Enter Container2 Load From Date");
			input("txtbx_Cont2LoadFromTime", cont2LoadFromTime, "Booking_Page2", "Enter Container2 Load From Time");
			input("txtbx_Cont2LoadToDate", cont2LoadToDate, "Booking_Page2", "Enter Container2 Load To Date");
			input("txtbx_Cont2LoadToTime", cont2LoadToTime, "Booking_Page2", "Enter Container2 Load To Time");
			input("txtbx_Cont2LoadRef", cont2LoadRef, "Booking_Page2", "Enter Container2 Load To Time");

			input("txtbx_DangerousCargoDetails", CargoDetails, "Booking_Page2", "Enter Temperature");
			input("txtbx_Temerature", reeferTemp, "Booking_Page2", "Enter Temperature");

			selectOption("select_AdditionalReference", additionalRefType, "Booking_Page2",
					"Select Additional Reference");

			input("txtbx_OOG-BBQuoteRefNum", additionalRefValue, "Booking_Page2", "Enter Yes");

			javaClick("btn_BookShipment", "", "Booking_Page2", "Click Book Shipment");

			javaClick("btn_PlaceBooking", "", "Booking_Page2", "Click Place Booking");

			compareValue("txt_BookingSuccessMessage", "Your booking has number", "Booking_Page",
					"Verify Booking Page2");

			if (testRunner.testStepResult == true) {
				log_testActions.info("B5 DGReeferStandardMultiCargoBooking Booking Success ");
				String bookinNumber = driver.findElement(By.xpath("//*[contains(text(),'Your booking has number')]"))
						.getText();
				constants.Booking_Number = bookinNumber.replaceAll("\\D", "");
				System.out.println(constants.Booking_Number);
				testRunner.reportLogger.log(LogStatus.PASS,
						"B6 DGReeferStandardMultiCargoBooking number " + constants.Booking_Number,
						constants.KEYWORD_PASS);
				log_testActions.info(" Shipment Booking Number = " + constants.Booking_Number);

			} else {
				log_testActions.info("DGReeferStandardMultiCargoBooking Booking failed ");
			}

		} catch (Exception ex) {

			log_testActions.info("DG Reefer Standard MultiCargo Booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}

	}
	/*
	 * *****************************************************************************
	 * ***************** Method: serviceContractAffiliateMultiCargoBooking Author:
	 * Ashok Description: Create serviceContractAffiliateMultiCargoBooking booking
	 * *****************************************************************************
	 * ****************
	 */

	public static void serviceContractAffiliateMultiCargoBooking(String object, String data, String pageName,
			String StepName) throws Exception {

		try {
			String[] testData = data.split(";");

			log_testActions.info("Before  Reefer serviceContractAffiliate MultiCargo Booking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String commValue2 = testData[9];
			String contCount2 = testData[10];
			String conTypeValue2 = testData[11];
			String weight2 = testData[12];
			String contractTariff = testData[13];
			// String contactPerson = testData[10];

			String reference = testData[14];
			String exportType = testData[15];
			String importType = testData[16];
			// String reeferConType = testData[17];

			String reeferTemp = testData[17];
			String priceOwner = testData[18];

			log_testActions.info("After getting input values");

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
			 * "navigated to new booking page");
			 */
			// navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New
			// BookShipment","Booking_Page","Nvaigate to new Booking page");
			if (testRunner.testName.contains("_MML_")) {

				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			log_testActions.info("After Booking page");

			waitForVisible("txt_BookingPage1");

			compareValue("txt_BookingPage1", "Booked By", "Booking_Page", "Verify Booking Page");

			if (testRunner.testStepResult == true) {
				selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");

				if (testRunner.testStepResult == true) {
					selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");

					if (testRunner.testStepResult == true) {
						if (conType.equals("Reefer/Dangerous")) {
							clickElement("chk_cargoTepControl", "", "Booking_Page",
									"Select Cargo Requires Temperature control");
							clickElement("chk_cargoDangerous", "", "Booking_Page", "Select Dangerous control");
						}

						if (testRunner.testStepResult == true) {
							selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page",
									"Commodity");

							if (testRunner.testStepResult == true) {
								selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "",
										"Booking_Page", "ContainerType");

								if (testRunner.testStepResult == true) {
									input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
									clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page",
											"Click on iam price owner check box");
									javaClick("lnk_AddConTypeComm", "", "Booking_Page",
											"click Add another Container/Commodity");

									if (testRunner.testStepResult == true) {

										selectDPDNValue("dpdn_Commodity2_BP", "txtbx_Commodity2_BP", commValue2, "",
												"Booking_Page", "Commodity");
										selectDPDNValue("dpdn_Container2_BP", "txtbx_Container2_BP", conTypeValue2, "",
												"Booking_Page", "ContainerType");
										input("txtbx_Weight2_BP", weight2, "Booking_Page", "Enter weight2");

										// javaClick("chkbx_tariff", "", "Booking_Page", "Select Tariff");
										selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", priceOwner, pageName,
												StepName);

										if (testRunner.testStepResult == true) {
											clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");

											if (testRunner.testStepResult == true) {
												waitForVisible("txt_BookingPage2");
												compareValue("txt_BookingPage2", "Configure your booking details below",
														"Booking_Page2", "Verify Booking Page2");

												if (testRunner.testStepResult == true) {
													input("txtbx_BookedbyReference", reference, "Booking_Page2",
															"Enter Booked by Reference");

													if (testRunner.testStepResult == true) {
														input("txtbx_Temerature", reeferTemp, "Booking_Page2",
																"Enter Temperature");

														if (testRunner.testStepResult == true) {
															clickElement("btn_BookShipment", "", "Booking_Page2",
																	"Click Book Shipment");

															if (testRunner.testStepResult == true) {
																javaClick("btn_PlaceBooking", "", "Booking_Page2",
																		"Click Place Booking");

																if (testRunner.testStepResult == true) {
																	compareValue("txt_BookingSuccessMessage",
																			"Your booking has number", "Booking_Page",
																			"Verify Booking Page2");

																	if (testRunner.testStepResult == true) {
																		log_testActions.info(
																				"ReeferServiceContractAffiliateMultiCargoBooking Booking Success ");

																		String bookinNumber = driver.findElement(By
																				.xpath("//*[contains(text(),'Your booking has number')]"))
																				.getText();
																		constants.Booking_Number = bookinNumber
																				.replaceAll("\\D", "");
																		System.out.println(constants.Booking_Number);
																		log_testActions
																				.info(" Shipment Booking Number = "
																						+ constants.Booking_Number);
																		testRunner.reportLogger.log(LogStatus.PASS,
																				"B4 serviceContractAffiliateMultiCargoBooking number "
																						+ constants.Booking_Number,
																				constants.KEYWORD_PASS);

																	} else {
																		log_testActions.info(
																				"ReeferServiceContractAffiliateMultiCargoBooking Booking failed ");
																	}
																}
															}
														}
													}

												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception ex) {

			log_testActions
					.info("Reefer serviceContractAffiliate MultiCargo Booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: reeferMagnumShippersOwnSingleCargoBooking Author:
	 * Ashok Description: Create reeferMagnumShippersOwnSingleCargoBooking booking
	 * *****************************************************************************
	 * ****************
	 */

	public static void reeferMagnumShippersOwnSingleCargoBooking(String object, String data, String pageName,
			String StepName) throws Exception {

		try {
			String[] testData = data.split(";");

			log_testActions.info("Before reefer Magnum Shippers Own Single Cargo Booking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String contractTariff = testData[9];
			// String contactPerson = testData[10];
			String reference = testData[10];
			String exportType = testData[11];
			String importType = testData[12];
			String reeferConType = testData[13];
			String reeferTemp = testData[14];
			String additionalRefType = testData[15];
			String additionalRefValue = testData[16];

			log_testActions.info("After getting input values");

			if (testRunner.testName.contains("_MML_")) {

				// goToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
				// "navigated to new booking page");
				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			log_testActions.info("After Booking page");

			waitForVisible("txt_BookingPage1");

			compareValue("txt_BookingPage1", "Booked By", "Booking_Page", "Verify Booking Page");
			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");
			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");
			clickElement("chk_cargoTepControl", "", "Booking_Page", "Select Cargo Requires Temperature control");
			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");
			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");
			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
			clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on iam price owner check box");
			// clickElement("chkbx_tariff", "", "Booking_Page", "Select Tariff");
			selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", "Gcss Booked By, Copenhagen", pageName, StepName);
			clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");
			waitForVisible("txt_BookingPage2");
			compareValue("txt_BookingPage2", "Configure your booking details below", "Booking_Page2",
					"Verify Booking Page2");
			input("txtbx_BookedbyReference", reference, "Booking_Page2", "Enter Booked by Reference");
			clickElement("link_showAdvancedConfigurations", "", "Booking_Page2",
					"Clicking Show Advanced configuration");
			clickElement("radio_magnum", "", "Booking_Page2", "Select Magnum");
			log_testActions.info("Select Starfresh passed");
			input("txtbx_Temerature", reeferTemp, "Booking_Page2", "Enter Temperature");
			selectOption("select_AdditionalReference", additionalRefType, "Booking_Page2",
					"Select Additional Reference");
			selectOption("select_YesNo", additionalRefValue, "Booking_Page2", "Select Yes Or No");
			clickElement("btn_BookShipment", "", "Booking_Page2", "Click Book Shipment");
			javaClick("btn_PlaceBooking", "", "Booking_Page2", "Click Place Booking");
			compareValue("txt_BookingSuccessMessage", "Your booking has number", "Booking_Page",
					"Verify Booking Page2");
			String bookinNumber = driver.findElement(By.xpath("//*[contains(text(),'Your booking has number')]"))
					.getText();
			constants.Booking_Number = bookinNumber.replaceAll("\\D", "");
			System.out.println(constants.Booking_Number);
			testRunner.reportLogger.log(LogStatus.PASS,
					"B7 reeferMagnumShippersOwnSingleCargoBooking " + constants.Booking_Number, constants.KEYWORD_PASS);
			log_testActions.info(" Shipment Booking Number = " + constants.Booking_Number);
			log_testActions.info("reeferMagnumShippersOwnSingleCargo Booking Success ");

		} catch (Exception ex) {

			log_testActions.info(" reeferMagnumShippersOwnSingleCargo Booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: ReeferStarCareMultiCargoBooking Author: Ashok
	 * Description: Create ReeferStarCareMultiCargoBooking booking
	 * *****************************************************************************
	 * ****************
	 */

	public static void ReeferStarCareMultiCargoBooking(String object, String data, String pageName, String StepName)
			throws Exception {

		try {
			String[] testData = data.split(";");

			log_testActions.info("Before Reefer StarCare MultiCargo Booking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String commValue2 = testData[9];
			String contCount2 = testData[10];
			String conTypeValue2 = testData[11];
			String weight2 = testData[12];
			String contractTariff = testData[13];
			// String contactPerson = testData[10];
			String reference = testData[14];
			String exportType = testData[15];
			String importType = testData[16];
			String reeferConType = testData[17];
			String reeferTemp = testData[18];
			String additionalRefType = testData[19];
			String additionalRefValue = testData[20];

			log_testActions.info("After getting input values");

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
			 * "navigated to new booking page");
			 */
			// navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New
			// BookShipment","Booking_Page","Nvaigate to new Booking page");
			if (testRunner.testName.contains("_MML_")) {

				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			log_testActions.info("After Booking page");

			waitForVisible("txt_BookingPage1");

			compareValue("txt_BookingPage1", "Booked By", "Booking_Page", "Verify Booking Page");
			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");

			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");

			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");

			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");

			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");

			selectRateLookUpRateCaluculationDate(
					"Maersk_SSP_txtbx_DepartureDate;Maersk_SSP_lst_DepartureMonth;Maersk_SSP_lst_DepartureYear",
					"days;1;No", "Booking_Page", "");

			Thread.sleep(2000);

			// clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on
			// iam price owner check box");
			javaClick("lnk_AddConTypeComm", "", "Booking_Page", "click Add another Container/Commodity");

			selectDPDNValue("dpdn_Commodity2_BP", "txtbx_Commodity2_BP", commValue2, "", "Booking_Page", "Commodity");
			selectDPDNValue("dpdn_Container2_BP", "txtbx_Container2_BP", conTypeValue2, "", "Booking_Page",
					"ContainerType");
			input("txtbx_Weight2_BP", weight2, "Booking_Page", "Enter weight2");

			// javaClick("chkbx_tariff", "", "Booking_Page", "Select Tariff");

			selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", "Gcss Booked By, Copenhagen", pageName, StepName);

			clickElement("chk_cargoTepControl", "", "Booking_Page", "Select Cargo Requires Temperature control");

			clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");

			waitForVisible("txt_BookingPage2");
			compareValue("txt_BookingPage2", "Configure your booking details below", "Booking_Page2",
					"Verify Booking Page2");

			input("txtbx_BookedbyReference", reference, "Booking_Page2", "Enter Booked by Reference");

			clickElement("link_showAdvancedConfigurations", "", "Booking_Page2",
					"Clicking Show Advanced configuration");

			clickElement("radio_starcare", "", "Booking_Page2", "Select Starcare");

			input("txtbx_Temerature", reeferTemp, "Booking_Page2", "Enter Temperature");

			selectOption("select_AdditionalReference", additionalRefType, "Booking_Page2",
					"Select Additional Reference");

			selectOption("select_YesNo", additionalRefValue, "Booking_Page2", "Select Additional Reference value");

			clickElement("btn_BookShipment", "", "Booking_Page2", "Click Book Shipment");

			javaClick("btn_PlaceBooking", "", "Booking_Page2", "Click Place Booking");

			compareValue("txt_BookingSuccessMessage", "Your booking has number", "Booking_Page",
					"Verify Booking Page2");
			String bookinNumber = driver.findElement(By.xpath("//*[contains(text(),'Your booking has number')]"))
					.getText();
			constants.Booking_Number = bookinNumber.replaceAll("\\D", "");
			System.out.println(constants.Booking_Number);
			log_testActions.info(" Shipment Booking Number = " + constants.Booking_Number);
			testRunner.reportLogger.log(LogStatus.PASS,
					"B9 ReeferStarCareMultiCargoBooking number " + constants.Booking_Number, constants.KEYWORD_PASS);

			log_testActions.info("ReeferStarcareMultiCargoBooking Booking Success ");

		}

		catch (Exception ex) {

			log_testActions.info("Reefer StarCare MultiCargo Booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: DGReeferSuperFreezerSingleCargoBooking Author:
	 * Ashok Description: Create DGReeferSuperFreezerSingleCargoBooking booking
	 * *****************************************************************************
	 * ****************
	 */
	public static void DGReeferSuperFreezerSingleCargoBooking(String object, String data, String pageName,
			String StepName) throws Exception {

		try {
			String[] testData = data.split(";");

			log_testActions.info("Before DG Reefer SuperFreezer Single Cargo Booking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String contractTariff = testData[9];
			// String contactPerson = testData[10];

			String reference = testData[10];
			String exportType = testData[11];
			String importType = testData[12];
			String reeferConType = testData[13];
			String CargoDetails = testData[14];
			String reeferTemp = testData[15];
			String additionalRefType = testData[16];
			String additionalRefValue = testData[17];

			log_testActions.info("After getting input values");

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
			 * "navigated to new booking page"); navigateMenu(
			 * "Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New BookShipment"
			 * ,"Booking_Page","Nvaigate to new Booking page");
			 * log_testActions.info("After Booking page");
			 */
			if (testRunner.testName.contains("_MML_")) {

				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			waitForVisible("txt_BookingPage1");

			compareValue("txt_BookingPage1", "Booked By", "Booking_Page", "Verify Booking Page");

			if (testRunner.testStepResult == true) {
				selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");

				if (testRunner.testStepResult == true) {
					selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");

					if (testRunner.testStepResult == true) {
						if (conType.equals("Reefer/Dangerous")) {
							clickElement("chk_cargoTepControl", "", "Booking_Page",
									"Select Cargo Requires Temperature control");
							clickElement("chk_cargoDangerous", "", "Booking_Page", "Select Dangerous control");
						}

						if (testRunner.testStepResult == true) {
							selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page",
									"Commodity");

							if (testRunner.testStepResult == true) {
								selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "",
										"Booking_Page", "ContainerType");

								if (testRunner.testStepResult == true) {
									input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
									clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page",
											"Click on iam price owner check box");

									if (testRunner.testStepResult == true) {

										// clickElement("chkbx_tariff", "", "Booking_Page", "Select Tariff");
										selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner",
												"Gcss Booked By, Copenhagen", pageName, StepName);

										if (testRunner.testStepResult == true) {
											clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");

											if (testRunner.testStepResult == true) {
												waitForVisible("txt_BookingPage2");
												compareValue("txt_BookingPage2", "Configure your booking details below",
														"Booking_Page2", "Verify Booking Page2");

												if (testRunner.testStepResult == true) {
													input("txtbx_BookedbyReference", reference, "Booking_Page2",
															"Enter Booked by Reference");

													if (testRunner.testStepResult == true) {
														input("txtbx_DangerousCargoDetails", CargoDetails,
																"Booking_Page2", "Enter Temperature");

														if (testRunner.testStepResult == true) {

															clickElement("link_showAdvancedConfigurations", "",
																	"Booking_Page2",
																	"Clicking Show Advanced configuration");
															clickElement("radio_superFreezer", "", "Booking_Page2",
																	"Select Super Freezer");

															if (testRunner.testStepResult == true) {
																clearTextBox("txtbx_Temerature", "", "Booking_Page2",
																		"Clear Temperatures");
																input("txtbx_Temerature", reeferTemp, "Booking_Page2",
																		"Enter Temperature");

																if (testRunner.testStepResult == true) {

																	selectOption("select_AdditionalReference",
																			additionalRefType, "Booking_Page2",
																			"Select Additional Reference");

																	if (testRunner.testStepResult == true) {

																		input("txtbx_OOG-BBQuoteRefNum",
																				additionalRefValue, "Booking_Page2",
																				"Enter Yes");

																		if (testRunner.testStepResult == true) {
																			clickElement("btn_BookShipment", "",
																					"Booking_Page2",
																					"Click Book Shipment");

																			if (testRunner.testStepResult == true) {
																				javaClick("btn_PlaceBooking", "",
																						"Booking_Page2",
																						"Click Place Booking");

																				if (testRunner.testStepResult == true) {
																					compareValue(
																							"txt_BookingSuccessMessage",
																							"Your booking has number",
																							"Booking_Page",
																							"Verify Booking Page2");

																					if (testRunner.testStepResult == true) {
																						log_testActions.info(
																								"DGReeferSuperFreezerSingleCargoBooking Booking Success ");
																						String bookinNumber = driver
																								.findElement(By.xpath(
																										"//*[contains(text(),'Your booking has number')]"))
																								.getText();
																						constants.Booking_Number = bookinNumber
																								.replaceAll("\\D", "");
																						System.out.println(
																								constants.Booking_Number);
																						log_testActions.info(
																								" Shipment Booking Number = "
																										+ constants.Booking_Number);
																						testRunner.reportLogger.log(
																								LogStatus.PASS,
																								"B8 DGReeferSuperFreezerSingleCargoBooking number "
																										+ constants.Booking_Number,
																								constants.KEYWORD_PASS);
																					} else {
																						log_testActions.info(
																								"DGReeferSuperFreezerSingleCargoBooking Booking failed ");
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception ex) {

			log_testActions.info("DG Reefer Standard MultiCargo Booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: DGReeferSingleCargoBookingSuperFreezer Author:
	 * Ashok Description: Create DGReeferSingleCargoBookingSuperFreezer booking
	 * *****************************************************************************
	 * ****************
	 */
	public static void DGReeferSingleCargoBookingSuperFreezer(String object, String data, String pageName,
			String StepName) throws Exception {

		try {
			String[] testData = data.split(";");

			log_testActions.info("Before DG Reefer SuperFreezer Single Cargo Booking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String contractTariff = testData[9];
			// String contactPerson = testData[10];

			String reference = testData[10];
			String exportType = testData[11];
			String importType = testData[12];
			String reeferConType = testData[13];
			String CargoDetails = testData[14];
			String reeferTemp = testData[15];
			String additionalRefType = testData[16];
			String additionalRefValue = testData[17];

			log_testActions.info("After getting input values");

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
			 * "navigated to new booking page"); navigateMenu(
			 * "Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New BookShipment"
			 * ,"Booking_Page","Nvaigate to new Booking page");
			 */

			if (testRunner.testName.contains("_MML_")) {

				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			log_testActions.info("After Booking page");

			waitForVisible("txt_BookingPage1");

			compareValue("txt_BookingPage1", "Booked By", "Booking_Page", "Verify Booking Page");

			if (testRunner.testStepResult == true) {
				selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");

				if (testRunner.testStepResult == true) {
					selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");

					if (testRunner.testStepResult == true) {
						if (conType.equals("Reefer/Dangerous")) {
							clickElement("chk_cargoTepControl", "", "Booking_Page",
									"Select Cargo Requires Temperature control");
							clickElement("chk_cargoDangerous", "", "Booking_Page", "Select Dangerous control");
						}

						if (testRunner.testStepResult == true) {
							selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page",
									"Commodity");

							if (testRunner.testStepResult == true) {
								selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "",
										"Booking_Page", "ContainerType");

								if (testRunner.testStepResult == true) {
									input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
									clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page",
											"Click on iam price owner check box");

									if (testRunner.testStepResult == true) {

										// clickElement("chkbx_tariff", "", "Booking_Page", "Select Tariff");
										selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner",
												"Gcss Booked By, Copenhagen", pageName, StepName);

										if (testRunner.testStepResult == true) {
											clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");

											if (testRunner.testStepResult == true) {
												waitForVisible("txt_BookingPage2");
												compareValue("txt_BookingPage2", "Configure your booking details below",
														"Booking_Page2", "Verify Booking Page2");

												if (testRunner.testStepResult == true) {
													input("txtbx_BookedbyReference", reference, "Booking_Page2",
															"Enter Booked by Reference");

													if (testRunner.testStepResult == true) {
														input("txtbx_DangerousCargoDetails", CargoDetails,
																"Booking_Page2", "Enter Temperature");

														if (testRunner.testStepResult == true) {

															clickElement("link_showAdvancedConfigurations", "",
																	"Booking_Page2",
																	"Clicking Show Advanced configuration");
															clickElement("radio_superFreezer", "", "Booking_Page2",
																	"Select Super Freezer");

															if (testRunner.testStepResult == true) {
																clearTextBox("txtbx_Temerature", "", "Booking_Page2",
																		"Clear Temperatures");
																input("txtbx_Temerature", reeferTemp, "Booking_Page2",
																		"Enter Temperature");

																if (testRunner.testStepResult == true) {

																	selectOption("select_AdditionalReference",
																			additionalRefType, "Booking_Page2",
																			"Select Additional Reference");

																	if (testRunner.testStepResult == true) {

																		input("txtbx_OOG-BBQuoteRefNum",
																				additionalRefValue, "Booking_Page2",
																				"Enter Yes");

																		if (testRunner.testStepResult == true) {
																			clickElement("btn_BookShipment", "",
																					"Booking_Page2",
																					"Click Book Shipment");

																			if (testRunner.testStepResult == true) {
																				javaClick("btn_PlaceBooking", "",
																						"Booking_Page2",
																						"Click Place Booking");

																				if (testRunner.testStepResult == true) {
																					compareValue(
																							"txt_BookingSuccessMessage",
																							"Your booking has number",
																							"Booking_Page",
																							"Verify Booking Page2");

																					if (testRunner.testStepResult == true) {
																						log_testActions.info(
																								"DGReeferSuperFreezerSingleCargoBooking Booking Success ");
																						String bookinNumber = driver
																								.findElement(By.xpath(
																										"//*[contains(text(),'Your booking has number')]"))
																								.getText();
																						constants.Booking_Number = bookinNumber
																								.replaceAll("\\D", "");
																						System.out.println(
																								constants.Booking_Number);
																						log_testActions.info(
																								" Shipment Booking Number = "
																										+ constants.Booking_Number);

																					} else {
																						log_testActions.info(
																								"DGReeferSuperFreezerSingleCargoBooking Booking failed ");
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

		} catch (Exception ex) {

			log_testActions.info("DG Reefer Standard MultiCargo Booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: trackBooking Author: Ashok Description: To Track
	 * existing booking
	 * *****************************************************************************
	 * ****************
	 */
	public static void trackBooking(String object, String data, String pageName, String StepName) throws Exception {

		try {

			log_testActions.info("Before Track cancel booking ");

			String bookingNumber = constants.Booking_Number;

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
			 * "navigated to new booking page");
			 */
			javaClick("Maersk_SSP_Menu_Track", "TRACK", "Booking_Page", "Nvaigate to Tracking Page");
			waitForVisible("txt_trackPage1");
			compareValue("txt_trackPage1", "Track", "Track_Page", "Verify Track Page");
			waitForVisible("txt_trackPage2");
			compareValue("txt_trackPage2", "To access a specific shipment", "Track_Page", "Verify Track Page");
			// input("txtbx_ShipmentNumber", bookingNumber, "Track_Page", "Enter
			// Booking Number");
			input("txbx_BLNumber", bookingNumber, "Track_Page", "Enter Booking Number");
			javaClick("btn_Retrieve", "", "Track_Page", "Click Retrive");
			clickElement("link_log_ShipmentBinder", "", "ShipmentBinder_Page", "Click Log");
			waitForVisible("txt_log_Cancellation");

			log_testActions.info("Track booking  Success ");

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" Trac booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: trackShipment Author: Ashok Description: To Track
	 * existing shipment
	 * *****************************************************************************
	 * ****************
	 */
	
	public static void trackShipment(String object, String data, String pageName, String StepName) {

		String[] objectProperties = object.split(";");
		String TxtTrackShipNo = objectProperties[0];
		String BtntrackShipNo = objectProperties[1];
		String binder = objectProperties[2];


		try {
			log_testActions.info("Entering the text in " + object + " in " + pageName);
			System.out.println("Entered  value in " + object + "in" + pageName);
			
			
			String url = driver.getCurrentUrl();		
			String[] split_URL = url.split("/");
			String brandName = split_URL[2];
			String Export_url = "https://" + brandName + "/shipmentoverview/export";	
			driver.navigate().to(Export_url);
			
			String bookingNumber = data;

			Thread.sleep(2000);		
			input(TxtTrackShipNo, bookingNumber, pageName, StepName);
			javaClick(BtntrackShipNo, "", pageName, StepName);
			testRunner.testStepResult = true;
			

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			System.out.println("Exception");

			log_testActions.error("Not able to enter the text in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	//public void trackShipment(String object, String data, String pageName, String StepName) throws Exception {

		
		//try {		
			
//			String trackObj[] = object.split(";");
//			String txtbx_trackShipNo = trackObj[0];
//			String btn_trackShipNo = trackObj[1];
//			String binder= trackObj[2];
			
			
			//driver.navigate().to("https://demo.maersk.com/shipmentoverview/export");
			
			//String bookingNumber = data;
/*			if (bookingNumber.contains("<<RTD_BookingNumber>>")) {

				bookingNumber = constants.Booking_Number;
			}
			
			System.out.println("In trackShipment Method");
				String url = driver.getCurrentUrl();
				System.out.println(url);
				
				String[] split_URL = url.split("/");
				String brandName = split_URL[2];
				System.out.println("brandName : "+brandName);
				String Export_url = "https://" + brandName + "/shipmentoverview/export";
				driver.navigate().to(Export_url);
				System.out.println(driver.getCurrentUrl());*/
				
				//Thread.sleep(2000);		
			
			

			//input(txtbx_trackShipNo, bookingNumber, pageName, stepName);
			//javaClick(btn_trackShipNo, "", pageName, stepName);

//			WebDriverWait wait = new WebDriverWait(driver, 5);
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='main']/div[1]/div[1]/h1")));
//
//			if (compareValue("", "Departing on", pageName, stepName)) {
//
//				testRunner.testStepResult = true;
//			} else {
//				testRunner.testStepResult = false;
//			}
//
//		} catch (Exception ex) {
//			
//			System.out.println("Move to track booking Exception");
//			//testRunner.stepException = ex.getMessage();
//			log_testActions.info(" Trac booking Failure. Exception = " + ex.getMessage());
//			testRunner.testStepResult = false;
//		}
//	}

	/*
	 * *****************************************************************************
	 * ***************** Method: gotoShipmentBinder Author: Ashok Description: Go to
	 * shipment binder page
	 * *****************************************************************************
	 * ****************
	 */
	public static void gotoShipmentBinder(String object, String data, String pageName, String StepName)
			throws Exception {

		try {

			log_testActions.info("Before Track booking number ");

			String bookingNumber = constants.Booking_Number;

			navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
					"navigated to new booking page");

			input("txbx_BLNumber", data, "Track_Page", "Enter Booking Number");
			javaClick("btn_Retrieve", "", "Track_Page", "Click Retrive");
			Thread.sleep(5000);
			log_testActions.info("Track booking  Success ");

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" Trac booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: clickElement Author: Ashok Description: to click on
	 * webelement
	 * *****************************************************************************
	 * ****************
	 */

	public static void clickElement(String object, String data, String pageName, String StepName) throws Exception
	// public static void clickElement(String object, String TrigerEvent, String
	// pageName, String StepName) throws Exception
	{

		log_testActions.info("Clicking on " + object + " in " + pageName);

		WebElement ele = null;
		 waitForVisible(object);

		try {
			if (isTestElementPresent(object)) {

				if ((pageName.equals("Booking_Page") || pageName.equals("Booking_Page2")
						|| pageName.contains("BookingSuccess_Page")) && object.contains(";"))

				{
					String ltype = object.split(";")[0];
					String lvalue = object.split(";")[1];

					log_testActions.info("locatorType = " + ltype);
					log_testActions.info("locatorValue = " + lvalue);

					ele = getObject(getObjectLocator(ltype, lvalue));
				} else {
					ele = getObject(getObjectLocator(object));
				}

				if (object.equals("lnk_CancelBooking") || object.equals("radio_export_SD"))

				{
					waitForEnable(ele);
					

				}
				
				
				if (object.equals("btn_DuplicateBooking_Binder"))
						
				{
					
					waitForEnable(ele);
					ScrollByPixel("", "down", "", "");
				}

				if (object.equals("link_showAdvancedConfigurations") || object.equals("radio_starfresh")
						|| object.equals("radio_magnum") || object.equals("radio_superFreezer")
						|| object.equals("radio_starcare") || object.equals("chk_container1Details")
						|| object.equals("chk_container2Details") || object.equals("lnk_AddOversizedDetails")
						|| object.equals("lnk_EditOversizedDetails") || object.equals("btn_Cancel")
						|| object.contains("lnk_CancelBooking")) {

					((JavascriptExecutor) driver).executeScript("arguments[0].click()", ele);
					Thread.sleep(10000);

				}

				else if (object.equals("btn_ContainerClear")) {

					ScrollByPixel("", "down", "", "");

					driver.findElement(By.xpath("(//abbr[contains(@class,'select2-search-choice-close')])[4]")).click();

				}

				else {
					
					
					WebElement element = getObject(getObjectLocator(object));
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].scrollIntoView();", element);
					executor.executeScript("arguments[0].click();", element);

					// ele.click();
				}

				log_testActions.info("Clicked on " + ele + " in " + pageName + "Success");

				testRunner.testStepResult = true;

			}
		} catch (StaleElementReferenceException se) {
			testRunner.stepException = se.getMessage();
			log_testActions.error(object + " Not able to click. Exception =  " + testRunner.stepException);
			ele = getObject(getObjectLocator(object));
			ele.click();
			testRunner.testStepResult = true;
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error(object + " Not able to click. Exception =  " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: serviceContractBooking Author: Ashok Description:
	 * Create serviceContractBooking
	 * *****************************************************************************
	 * ****************
	 */
	public static void serviceContractBooking(String object, String data, String pageName, String StepName)
			throws Exception {

		try {
			String[] testData = data.split(";");

			log_testActions.info("Before  Reefer serviceContractAffiliate MultiCargo Booking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String commValue2 = testData[9];
			String contCount2 = testData[10];
			String conTypeValue2 = testData[11];
			String weight2 = testData[12];
			String contractTariff = testData[13];
			// String contactPerson = testData[10];

			String reference = testData[14];
			String exportType = testData[15];
			String importType = testData[16];
			// String reeferConType = testData[17];

			String reeferTemp = testData[17];

			log_testActions.info("After getting input values");

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
			 * "navigated to new booking page");
			 */
			// navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New
			// BookShipment","Booking_Page","Nvaigate to new Booking page");
			if (testRunner.testName.contains("_MML_")) {

				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			log_testActions.info("After Booking page");

			waitForVisible("txt_BookingPage1");

			compareValue("txt_BookingPage1", "Booked By", "Booking_Page", "Verify Booking Page");

			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");

			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");

			clickElement("chk_cargoTepControl", "", "Booking_Page", "Select Cargo Requires Temperature control");
			clickElement("chk_cargoDangerous", "", "Booking_Page", "Select Dangerous control");

			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");

			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");
			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
			clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on iam price owner check box");
			/*
			 * javaClick("lnk_AddConTypeComm", "", "Booking_Page",
			 * "click Add another Container/Commodity");
			 * selectDPDNValue("dpdn_Commodity2_BP", "txtbx_Commodity2_BP", commValue2, "",
			 * "Booking_Page", "Commodity"); selectDPDNValue("dpdn_Container2_BP",
			 * "txtbx_Container2_BP", conTypeValue2, "", "Booking_Page", "ContainerType");
			 * input("txtbx_Weight2_BP", weight2, "Booking_Page", "Enter weight2");
			 */

			// javaClick("chkbx_tariff", "", "Booking_Page", "Select Tariff");
			selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", "Gcss Booked By, Copenhagen", pageName, StepName);
			clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");

			waitForVisible("txt_BookingPage2");

			compareValue("txt_BookingPage2", "Configure your booking details below", "Booking_Page2",
					"Verify Booking Page2");
			input("txtbx_BookedbyReference", reference, "Booking_Page2", "Enter Booked by Reference");
			input("txtbx_contract_BP", contractTariff, "Booking_Page", "enter contract number");

			input("txtbx_DangerousCargoDetails", "Dangerous", "Booking_Page2", "Enter Temperature");
			input("txtbx_Temerature", reeferTemp, "Booking_Page2", "Enter Temperature");
			javaClick("Maersk_SSP_lnk_ShowAdvanceConfig", "", "Booking page2", "Click on advance reefer config");
			clickElement("Maersk_SSP_rdo_reeferMagnum", "", "Select magnum radio button", "Booking Page 2");

			javaClick("btn_BookShipment", "", "Booking_Page2", "Click Book Shipment");
			javaClick("btn_PlaceBooking", "", "Booking_Page2", "Click Place Booking");

			compareValue("txt_BookingSuccessMessage", "Your booking has number", "Booking_Page",
					"Verify Booking Page2");

			if (testRunner.testStepResult == true) {
				log_testActions.info("ReeferServiceContractAffiliate single CargoBooking Booking Success ");

				String bookinNumber = driver.findElement(By.xpath("//*[contains(text(),'Your booking has number')]"))
						.getText();
				constants.Booking_Number = bookinNumber.replaceAll("\\D", "");
				System.out.println(constants.Booking_Number);
				log_testActions.info(" Shipment Booking Number = " + constants.Booking_Number);
				testRunner.reportLogger.log(LogStatus.PASS,
						"B3 ReeferServiceContractAffiliateMultiCargoBooking number " + constants.Booking_Number,
						constants.KEYWORD_PASS);

			} else {
				log_testActions.info("ReeferServiceContractAffiliateMultiCargoBooking Booking failed ");
			}

		} catch (Exception ex) {

			log_testActions
					.info("Reefer serviceContractAffiliate MultiCargo Booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}

	}

	/*----- Method Name: trackAmendmentBooking
	 * ---- Author Name: Ashok
	 * */

	/*
	 * *****************************************************************************
	 * ***************** Method: trackAmendmentBooking Author: Ashok Description:
	 * Teack amend booking
	 * *****************************************************************************
	 * ****************
	 */
	public void trackAmendmentBooking(String object, String data, String pageName, String StepName)
			throws Exception {

		String bookingNumber = constants.Booking_Number;
		bookingNumber = data;

		try {

			log_testActions.info("Before Track Amend booking ");

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
			 * "navigated to new booking page");
			 */
			navigateMenu("Maersk_SSP_Menu_Track", "TRACK", "Booking_Page", "Nvaigate to Tracking Page");
			Thread.sleep(5000);
			waitForVisible("txt_trackPage1");
			compareValue("txt_trackPage1", "Track", "Track_Page", "Verify Track Page");
			waitForVisible("txt_trackPage2");
			compareValue("txt_trackPage2", "To access a specific shipment", "Track_Page", "Verify Track Page");
			// input("txtbx_ShipmentNumber", bookingNumber, "Track_Page", "Enter
			// Booking Number");
			input("txbx_BLNumber", bookingNumber, "Track_Page", "Enter Booking Number");
			clickElement("btn_Retrieve", "", "Track_Page", "Click Retrive");
			clickElement("link_log_ShipmentBinder", "", "ShipmentBinder_Page", "Click Log");

			waitForVisible("txt_log_Amendment");

			log_testActions.info("Track Ammend booking  Success ");

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" Trac booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: changeCustomerCode Author: Ashok Description: To
	 * change customer code
	 * *****************************************************************************
	 * ****************
	 */
	public static void changeCustomerCode(String object, String data, String pageName, String StepName)
			throws Exception {

		try {

			log_testActions.info("Before changeCustomerCode ");

			String custmerCode = data;

			navigateToUrl("", "https://myt.apmoller.net/portaluser/#login/set-customer", "Booking_Page",
					"navigated to new to change custmer code page");
			Thread.sleep(5000);
			compareValue("Customer code", "Login_Page", "Login_Page", "Verify text");

			input("txtbx_Code", custmerCode, "Login_Page", "Enter custmer code Number");
			clickElement("btn_Continue", "", "Login_Page", "Click continue");
			log_testActions.info("change custmer code  Success ");

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" change custmer code Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: navigateMenu Author: Ashok Description: used to
	 * Navigate Menu
	 * *****************************************************************************
	 * ****************
	 */
	public static void IGNnavigateMenu(String object, String data, String pageName, String StepName) throws Exception {

		String strMenu = null;
		String strSubMenu = null;

		try {

			JavascriptExecutor js = (JavascriptExecutor) driver;
			Properties property = new Properties();

			String[] arrMenus = object.split(";");
			int menuLength = arrMenus.length;

			// clicking level 1
			/*
			 * strMenu = property.getProperty(strMenu); strMenu = strMenu.split(";")[1];
			 */

			if (arrMenus.length == 1) {
				strMenu = arrMenus[0];
				WebElement wlmMenu = getObject(getObjectLocator(strMenu));
				js.executeScript("arguments[0].focus();", wlmMenu);
				Actions builder = new Actions(driver);
				builder.moveToElement(wlmMenu).perform();

				wlmMenu.click();
				Thread.sleep(5000);
				driver.navigate().refresh();

				testRunner.testStepResult = true;
				log_testActions.info("Expected text " + strMenu + " is dispalyed in " + pageName);
				System.out.println("Expected text " + strMenu + " is dispalyed in " + pageName);

			}

			// level2

			if (arrMenus.length == 2) {
				/*
				 * strMenu = arrMenus[0]; strSubMenu = arrMenus[1]; WebElement wlmMenu =
				 * getObject(getObjectLocator(strMenu));
				 * js.executeScript("arguments[0].focus();", wlmMenu); Actions builder = new
				 * Actions(driver); builder.moveToElement(wlmMenu).perform(); wlmMenu.click();
				 * //strSubMenu = property.getProperty(strSubMenu); WebElement wlmSubMenu =
				 * getObject(getObjectLocator(strSubMenu));
				 * js.executeScript("arguments[0].focus();", wlmSubMenu);
				 * builder.moveToElement(wlmSubMenu).perform(); wlmSubMenu.click(); String
				 * pageTitle = driver.getTitle(); if (pageTitle.contains("Certificate") ||
				 * pageTitle.contains("Certificate")) { javaClick("lnk_CertificateErrorIE", "",
				 * "", "Click on continue certificate error");
				 * 
				 * } if (pageTitle.contains("Acceptable Usage Policy")) {
				 * javaClick("btn_IAcceptError", "", "", "Click on I Accept Button");
				 * 
				 * } log_testActions.info("Expected text " + strSubMenu + " is dispalyed in " +
				 * pageName); System.out.println("Expected text " + strSubMenu +
				 * " is dispalyed in " + pageName); testRunner.testStepResult = true;
				 * driver.navigate().refresh();
				 */

				// Edited One
				// ____________

				strMenu = arrMenus[0];
				strSubMenu = arrMenus[1];

				javaClick(strMenu, "", "", "Click on Main Menu");
				javaClick(strSubMenu, "", "", "Click on SubMenu");

				// List<WebElement> li = driver.findElements(By.xpath("//*[text()='Manage']"));
				// System.out.println(li.get(2).getText());
				// li.get(2).click();

				// driver.findElement(By.xpath("(//*[@class='ign-header__buttons__elem'])[2]")).click();

				TestActions.refreshWindow("", "", "", "");
				testRunner.testStepResult = true;

				// ____________

			}
		}

		catch (Exception e) {
			log_testActions.info(" reeferStarfreshSingleCargo Booking Failure. Exception = " + e.getMessage());
			testRunner.testStepResult = false;
			System.out.println(e.getMessage());
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: DGReeferShipperownMultiCargoBooking Author: Ashok
	 * Description: Create DGReeferShipperownMultiCargoBooking
	 * *****************************************************************************
	 * ****************
	 */
	public static void DGReeferShipperownMultiCargoBooking(String object, String data, String pageName, String StepName)
			throws Exception {

		try {
			String[] testData = data.split(";");

			log_testActions.info("Before DG Reefer Standard MultiCargo Booking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String commValue2 = testData[9];
			String contCount2 = testData[10];
			String conTypeValue2 = testData[11];
			String weight2 = testData[12];
			String contractTariff = testData[13];
			// String contactPerson = testData[10];

			String reference = testData[14];
			String exportType = testData[15];
			String importType = testData[16];
			String reeferConType = testData[17];
			String haulage_Instructions = testData[18];
			String loadAddress = testData[19];
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date todayDate = Calendar.getInstance().getTime();
			String todayDateString = formatter.format(todayDate);
			String[] cont1LoadDetails = testData[20].split("/");
			String cont1LoadFromDate = cont1LoadDetails[0].equals("today") ? todayDateString : cont1LoadDetails[0];
			String cont1LoadFromTime = cont1LoadDetails[1];
			String cont1LoadToDate = cont1LoadDetails[2].equals("today") ? todayDateString : cont1LoadDetails[2];
			String cont1LoadToTime = cont1LoadDetails[3];
			String cont1LoadRef = cont1LoadDetails[4];
			String[] cont2LoadDetails = testData[21].split("/");
			String cont2LoadFromDate = cont2LoadDetails[0].equals("today") ? todayDateString : cont2LoadDetails[0];
			String cont2LoadFromTime = cont2LoadDetails[1];
			String cont2LoadToDate = cont2LoadDetails[2].equals("today") ? todayDateString : cont2LoadDetails[2];
			String cont2LoadToTime = cont2LoadDetails[3];
			String cont2LoadRef = cont2LoadDetails[4];
			String CargoDetails = testData[22];
			String reeferTemp = testData[23];
			String additionalRefType = testData[24];
			String additionalRefValue = testData[25];

			log_testActions.info("After getting input values");

			/*
			 * navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
			 * "navigated to new booking page"); navigateMenu(
			 * "Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New BookShipment"
			 * ,"Booking_Page","Nvaigate to new Booking page");
			 * log_testActions.info("After Booking page");
			 */
			if (testRunner.testName.contains("_MML_")) {

				IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");

			} else {

				navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment", "BOOK~New BookShipment",
						"Booking_Page", "Nvaigate to new Booking page");
			}

			waitForVisible("txt_BookingPage1");

			compareValue("txt_BookingPage1", "Booked By", "Booking_Page", "Verify Booking Page");

			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");

			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");
			clickElement("radio_Export_SD", "", "Booking_Page1", "Select Export SD mode");

			clickElement("chk_cargoTepControl", "", "Booking_Page", "Select Cargo Requires Temperature control");
			clickElement("chk_cargoDangerous", "", "Booking_Page", "Select Dangerous control");

			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");

			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");

			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
			clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on iam price owner check box");
			javaClick("lnk_AddConTypeComm", "", "Booking_Page", "click Add another Container/Commodity");
			selectDPDNValue("dpdn_Commodity2_BP", "txtbx_Commodity2_BP", commValue2, "", "Booking_Page", "Commodity");
			selectDPDNValue("dpdn_Container2_BP", "txtbx_Container2_BP", conTypeValue2, "", "Booking_Page",
					"ContainerType");
			input("txtbx_Weight2_BP", weight2, "Booking_Page", "Enter weight2");
			// clickElement("chkbx_tariff", "", "Booking_Page", "Select Tariff");
			selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", "Gcss Booked By, Copenhagen", pageName, StepName);

			clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");

			waitForVisible("txt_BookingPage2");
			compareValue("txt_BookingPage2", "Configure your booking details below", "Booking_Page2",
					"Verify Booking Page2");

			input("txtbx_BookedbyReference", reference, "Booking_Page2", "Enter Booked by Reference");

			/*
			 * clickElement("radio_Export_SD", "", "Booking_Page1",
			 * "Select Export SD mode");
			 */

			input("txtbx_HaulageInstructions", haulage_Instructions, "Booking_Page2", "Enter Booked by Reference");
			selectOptionUsingPartialText("select_LoadAddress", "Gcss Booked By, Copenhagen", "Booking_Page2",
					"Select Load Address");

			input("txtbx_Cont1LoadFromDate", cont1LoadFromDate, "Booking_Page2", "Enter Container1 Load From Date");
			input("txtbx_Cont1LoadFromTime", cont1LoadFromTime, "Booking_Page2", "Enter Container1 Load From Time");
			input("txtbx_Cont1LoadToDate", cont1LoadToDate, "Booking_Page2", "Enter Container1 Load To Date");
			input("txtbx_Cont1LoadToTime", cont1LoadToTime, "Booking_Page2", "Enter Container1 Load To Time");
			input("txtbx_Cont1LoadRef", cont1LoadRef, "Booking_Page2", "Enter Container1 Load To Time");
			clearTextBox("txtbx_Cont2LoadFromDate", "", "Booking_Page2", "Clear Container2 Load From Date");
			clearTextBox("txtbx_Cont2LoadFromTime", "", "Booking_Page2", "Clear Container2 Load From Time");
			clearTextBox("txtbx_Cont2LoadToDate", "", "Booking_Page2", "Clear Container2 Load To Date");
			clearTextBox("txtbx_Cont2LoadToTime", "", "Booking_Page2", "Clear Container2 Load To Date");
			input("txtbx_Cont2LoadFromDate", cont2LoadFromDate, "Booking_Page2", "Enter Container2 Load From Date");
			input("txtbx_Cont2LoadFromTime", cont2LoadFromTime, "Booking_Page2", "Enter Container2 Load From Time");
			input("txtbx_Cont2LoadToDate", cont2LoadToDate, "Booking_Page2", "Enter Container2 Load To Date");
			input("txtbx_Cont2LoadToTime", cont2LoadToTime, "Booking_Page2", "Enter Container2 Load To Time");
			input("txtbx_Cont2LoadRef", cont2LoadRef, "Booking_Page2", "Enter Container2 Load To Time");

			input("txtbx_DangerousCargoDetails", CargoDetails, "Booking_Page2", "Enter Temperature");
			input("txtbx_Temerature", reeferTemp, "Booking_Page2", "Enter Temperature");

			selectOption("select_AdditionalReference", additionalRefType, "Booking_Page2",
					"Select Additional Reference");

			input("txtbx_OOG-BBQuoteRefNum", additionalRefValue, "Booking_Page2", "Enter Yes");

			clickElement("btn_BookShipment", "", "Booking_Page2", "Click Book Shipment");

			javaClick("btn_PlaceBooking", "", "Booking_Page2", "Click Place Booking");

			compareValue("txt_BookingSuccessMessage", "Your booking has number", "Booking_Page",
					"Verify Booking Page2");

			if (testRunner.testStepResult == true) {
				log_testActions.info("DGReeferStandardMultiCargoBooking Booking Success ");
				String bookinNumber = driver.findElement(By.xpath("//*[contains(text(),'Your booking has number')]"))
						.getText();
				constants.Booking_Number = bookinNumber.replaceAll("\\D", "");
				System.out.println(constants.Booking_Number);
				log_testActions.info(" Shipment Booking Number = " + constants.Booking_Number);

			} else {
				log_testActions.info("DGReeferStandardMultiCargoBooking Booking failed ");
			}

		} catch (Exception ex) {

			log_testActions.info("DG Reefer Standard MultiCargo Booking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: essentialTerms Author: Ashok Description: Essential
	 * terms page
	 * *****************************************************************************
	 * ****************
	 */
	public static void essentialTerms(String object, String data, String pageName, String StepName) throws Exception {

		try {
			String[] testData = data.split(";");
			int length = testData.length;

			log_testActions.info("Essential terms ");
			String TextMessage = testData[0];
			String ContractNumber = testData[1];

			/*
			 * goToUrl("", "https://myt.apmoller.net/essential/contractLookup",
			 * "SearchByContractNumber_Page", "Click EssentialTearms Link");
			 */
			// clickElement("btn_Search_EssentialTerms1", "",
			// "SearchByContractNumber_Page", "Click Search Button");
			navigateMenu("Maersk_SSP_Menu_LOOKUP;Maersk_SSP_subMenu_EssentialTerms", "LOOKUP~Essential Terms",
					"Home_Page", "Navigate To Essential Terms Page");

			waitForVisible("txt_BookingPage1");
			compareValue("txt_ContractPageSuccess", "Service Contract Essential Terms", "SearchByContractNumber_Page",
					"Verify SearchByContractNumber_Page");

			input("txtbx_contractNumber", ContractNumber, "SearchByContractNumber_Page", "Enter Contarct number");
			clickElement("btn_Search_EssentialTerms1", "", "SearchByContractNumber_Page", "Click Search Button");

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" napBooking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}
	/*
	 * *****************************************************************************
	 * ***************** Method: searchbyOriginDestination Author: Ashok
	 * Description: SearchbyOriginDestination origin page
	 * *****************************************************************************
	 * ****************
	 */

	public static void searchbyOriginDestination(String object, String data, String pageName, String StepName)
			throws Exception {

		try {
			String[] testData = data.split(";");
			int length = testData.length;

			log_testActions.info("Essential terms ");
			String fromValue1 = testData[0];
			// String fromValue2=testData[1];

			String toValue1 = testData[1];
			// String toValue2 = testData[3];
			String commValue = testData[2];
			clickElement("lnk_EssentialTearms", "", "SearchByContractNumber_Page", "Click lnk_EssentialTearms");
			compareValue("txt_ContractPageSuccess", "Service Contract Essential Terms", "SearchByContractNumber_Page",
					"Verify SearchByContractNumber_Page");
			clickElement("link_Essentials_Search_OriginDestination", "", "SearchByOriginDestination_Page",
					"Click link_Essentials_Search_OriginDestination");
			selectDPDNValue("select_Origin", "txtbx_Origin", fromValue1, "", "SearchByOriginDestination_Page",
					"Eneter Origin");
			log_testActions.info(" From Address Selected as  = " + fromValue1);
			selectDPDNValue("select_Destination", "txtbx_Destination", toValue1, "", "SearchByOriginDestination_Page",
					"Enter Destination");
			log_testActions.info(" To Address Selected as  = " + toValue1);
			selectDPDNValue("select_Commodity", "txtbx_Commodity", commValue, "", "SearchByOriginDestination_Page",
					"Enter Commodity");
			clickElement("btn_Search_EssentialTerms2", "", "SearchByContractNumber_Page", "Click lnk_EssentialTearms");
			compareValue("txt_ContractPageSuccess", "Service Contract Essential Terms", "SearchByContractNumber_Page",
					"Verify SearchByContractNumber_Page");
			Thread.sleep(30000);

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" napBooking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: javaClick Author: Ashok Description: Java Script
	 * click
	 * *****************************************************************************
	 * ****************
	 */
	public static void javaClick(String object, String data, String pageName, String StepName) throws Exception {
		log_testActions.info("Clicking on " + object + " in " + pageName);
		System.out.println("Clicking on " + object + " in " + pageName);
		WebElement element = null;

		waitForVisible(object);
		try {
			System.out.println("Element Present");
			

			if (object.contains("lnk_ShipmentBinder_AckPage") || object.contains("lnk_CancelBooking")
					|| object.contains("btn_AmendBooking_Binder")) {
				waitForVisible(object);
			}

			element = getObject(getObjectLocator(object));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);

			/*if (object.contains("btn_BookShipment") || object.contains("btn_RequestAmendment")) {
			
				Thread.sleep(2000);
				driver.findElement(By.xpath("//*[@id='confirm-button']")).click();

			}*/
			log_testActions.info("Clicked on " + object + " in " + pageName + " Success");
			System.out.println("Clicked on " + object + " in " + pageName + " Success");
			testRunner.testStepResult = true;

		} catch (Exception e) {
			
			//testRunner.stepException = e.getMessage();
			Log.error("Not able to click --- " + e.getMessage());
			System.out.println("Exception");
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** ----- Method Name: verifyFirstLetter ---- Author Name:
	 * Ashok ---- Description: verifyFirstLetter method is used to verify the first
	 * letter in the word
	 * *****************************************************************************
	 * *****************
	 */

	public static void verifyFirstLetter(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before Verifying " + object + " in " + pageName);
		boolean status = true;
		ArrayList<WebElement> listItems;

		// WebElement ele=null;
		try {

			listItems = (ArrayList<WebElement>) getObjects(getObjectLocator(object));

			for (int i = 0; i < listItems.size(); i++) {
				String Optiontext = listItems.get(0).getText().trim();
				if (Optiontext.startsWith(data)) {
					log_testActions.info("The first letter contains " + data + " in " + pageName + "Success");
					System.out.println("The first letter contains " + data + " in " + pageName + "Success");
					testRunner.testStepResult = true;
				}

			}

		} catch (Exception e) {
			Log.error("the first letter not contains --- " + e.getMessage());
			System.out.println("The first letter not contains " + data + " in " + pageName + "Success");
			testRunner.testStepResult = false;
		}

	}

	/*----- Method Name: verifyLetter
	 * ---- Author Name: Ashok
	 * ----	Description: verifyLetter method is used to verify the word contains letter in the word
	 */
	public static void verifyLetter(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before Verifying " + object + " in " + pageName);
		boolean status = true;
		ArrayList<WebElement> listItems;
		try {

			listItems = (ArrayList<WebElement>) getObjects(getObjectLocator(object));

			for (int i = 0; i < listItems.size(); i++) {
				String Optiontext = listItems.get(0).getText().trim();
				if (Optiontext.contains(data)) {
					log_testActions.info("The first letter contains " + data + " in " + pageName + "Success");
					System.out.println("The first letter contains " + data + " in " + pageName + "Success");
					testRunner.testStepResult = true;
				}

			}

		} catch (Exception e) {
			Log.error("the first letter not contains --- " + e.getMessage());
			System.out.println("The first letter not contains " + data + " in " + pageName + "Success");
			testRunner.testStepResult = false;
		}

	}

	/*----- Method Name: selectListOption
	 * ---- Author Name: Ashok
	 * ----	Description: selectListOption is used to select list item
	 */
	public static void selectListOption(String object, String data, String pageName, String StepName) {
		WebElement listIdentifier = null;
		try {
			listIdentifier = getObject(getObjectLocator(object));
			Select dropdown = new Select(listIdentifier);
			dropdown.selectByVisibleText(data);
			testRunner.testStepResult = true;

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			Log.error("the List not contains --- " + e.getMessage());
			System.out.println("The List not contains " + data + " in " + pageName + "Success");
			testRunner.testStepResult = false;
		}
	}

	/*----- Method Name: keyPressTab
	 * ---- Author Name: Ashok
	 * ----	Description: keyPressTab is used to press Tab keyboard button
	 */
	public static void keyPressTab(String object, String data, String pageName, String StepName) {
		WebElement ele = null;
		waitForVisible(object);

		try {
			Log.info("Clicking on Web Element " + object);
			ele = getObject(getObjectLocator(object));
			if (data.equalsIgnoreCase("down")) {
				ele.sendKeys(Keys.DOWN);
				// String press = Keys.chord(Keys.SHIFT, Keys.TAB);
				Thread.sleep(5000);
				testRunner.testStepResult = true;
			} else if (data.equalsIgnoreCase("enter")) {

				ele.sendKeys(Keys.ENTER);
				// String press = Keys.chord(Keys.SHIFT, Keys.TAB);

				testRunner.testStepResult = true;

			}

			else if (data.equalsIgnoreCase("F5")) {

				ele.sendKeys(Keys.F5);

				testRunner.testStepResult = true;

			}

			else {
				ele.sendKeys(Keys.TAB);
				// String press = Keys.chord(Keys.SHIFT, Keys.TAB);
				Thread.sleep(5000);
				testRunner.testStepResult = true;

			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.captureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");

			Log.error("Not able to press Tab key --- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*----- Method Name: verifyWord
	 * ---- Author Name: Ashok
	 * ----	Description: verifyWord is used to verify word
	 */
	public static void verifyWord(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before Verifying " + object + " in " + pageName);
		boolean status = true;
		ArrayList<WebElement> listItems;
		try {

			listItems = (ArrayList<WebElement>) getObjects(getObjectLocator(object));

			for (int i = 0; i < listItems.size(); i++) {
				String Optiontext = listItems.get(0).getText().trim();
				if (Optiontext.contains(data)) {
					log_testActions.info("The first letter contains " + data + " in " + pageName + "Success");
					System.out.println("The first letter contains " + data + " in " + pageName + "Success");
					testRunner.testStepResult = true;
				}

			}

		} catch (Exception e) {
			Log.error("the first letter not contains --- " + e.getMessage());
			System.out.println("The first letter not contains " + data + " in " + pageName + "Success");
			testRunner.testStepResult = false;
		}

	}

	/*----- Method Name: isEnabled
	 * ---- Author Name: Ashok
	 * ----	Description: isEnabled is used to verify the web element is Enabled or not
	 */
	public static void isEnabled(String object, String data, String pageName, String StepName) {
		WebElement ele = null;
		waitForVisible(object);
		try {

			if (getObject(getObjectLocator(object)).isEnabled())

			{
				testRunner.testStepResult = true;
				return;
			} else {
				testRunner.testStepResult = false;
				return;
			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			Log.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
		}
	}

	/*----- Method Name: isSelected
	 * ---- Author Name: Ashok
	 * ----	Description: isSelected is used to verify the web element is Selected or not
	 */
	public static void isSelected(String object, String data, String pageName, String StepName) {
		WebElement ele = null;
		waitForVisible(object);
		try {
			ele = getObject(getObjectLocator(object));
			if (ele.isSelected())

			{
				testRunner.testStepResult = true;
				return;
			} else {
				testRunner.testStepResult = false;
				return;
			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.captureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			Log.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
		}

	}

	/*----- Method Name: isDisabled
	 * ---- Author Name: Ashok
	 * ----	Description: isDisabled is used to verify the web element is Disabled or not
	 */
	public static void isDisabled(String object, String data, String pageName, String StepName) {
		WebElement ele = null;
		try {
			ele = getObject(getObjectLocator(object));

			if (!ele.isEnabled())

			{
				testRunner.testStepResult = true;
				return;
			} else {
				testRunner.testStepResult = false;
				return;
			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			Log.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
		}
	}

	/*----- Method Name: isNotSelected
	 * ---- Author Name: Ashok
	 * ----	Description: isNotSelected is used to verify the web element is Not Selected
	 */

	public static void isNotSelected(String object, String data, String pageName, String StepName) {
		WebElement ele = null;
		try {

			if (!(getObject(getObjectLocator(object)).isSelected()))

			// if(driver.findElement(By.xpath(OR.getProperty(object))).isEnabled())
			{
				testRunner.testStepResult = true;
				return;
			} else {
				testRunner.testStepResult = false;
				return;
			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			Log.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
		}

	}

	/*----- Method Name: UnCheck
	 * ---- Author Name: Ashok
	 * ----	Description: UnCheck is used to un check the check box
	 */

	public static void unCheck(String object, String data, String pageName, String StepName) {
		WebElement element = null;
		waitForVisible(object);
		try {
			element = getObject(getObjectLocator(object));

			if (element.isSelected())

			// if(driver.findElement(By.xpath(OR.getProperty(object))).isEnabled())
			{

				/* element.click(); */
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element);
				testRunner.testStepResult = true;
				return;
			} else {
				testRunner.testStepResult = false;
				return;
			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			Log.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: bookingSecondage Author: Ashok Description: Enter
	 * All values for Booking Second page
	 * *****************************************************************************
	 * ****************
	 */
	public static void bookingSecondage(String object, String data, String pageName, String StepName) throws Exception {

		try {
			String[] testData = data.split(";");
			int length = testData.length;

			log_testActions.info("Before napBooking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String contractTariff = testData[9];
			// String contactPerson = testData[10];
			String reference = testData[10];
			String exportType = testData[11];
			String importType = testData[12];

			driver.get("https://myt.apmoller.net/booking/new");

			waitForVisible("txt_BookingPage1");
			compareValue("txt_BookingPage1", "Book New Shipments", "Booking_Page", "Verify Booking Page");
			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");
			log_testActions.info(" From Address Selected as  = " + fromValue1);
			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");
			log_testActions.info(" To Address Selected as  = " + toValue1);
			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");
			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");
			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
			clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on iam price owner check box");
			input("txtbx_contract_BP", contractTariff, "Booking_Page", "Enter Contract number");
			clickElement("btn_Continue_Book", "", "Booking_Page", "Click Continue");

			testRunner.testStepResult = true;

		} catch (Exception ex) {
			testRunner.stepException = ex.getMessage();

			log_testActions.info(" napBooking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifyToolTip Author: Ashok Description: Verify too
	 * tip
	 * *****************************************************************************
	 * ****************
	 */

	public static boolean verifyToolTip(String object, String data, String pageName, String StepName) {
		WebElement ele = null;

		try {
			Actions ToolTip1 = new Actions(driver);
			ele = getObject(getObjectLocator(object));

			Thread.sleep(2000);
			ToolTip1.moveToElement(ele).perform();
			Thread.sleep(2000);
			String ToolTipText = ele.getAttribute("title");

			// Assert.assertEquals(ToolTipText, data);
			if (ToolTipText.contains(data)) {
				System.out.println("Tooltip value is: " + ToolTipText);

				testRunner.testStepResult = true;

			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.captureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			Log.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
		}
		return testRunner.testStepResult;
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifyGreaterThan4Rows Author: Ashok Description:
	 * verifyGreaterThan4Rows
	 * *****************************************************************************
	 * ****************
	 */
	public static void verifyGreaterThan4Rows(String object, String data, String pageName, String StepName) {
		ArrayList<WebElement> listItems;
		// Create an interface WebElement of the div under div with **class as
		// facetContainerDiv**

		try {

			List<WebElement> tableExist = driver.findElements(By.xpath("//div[@id='schedule-plan-1']"));
			// Get the count of check boxes
			List<WebElement> AllRadiobuttons = driver.findElements(
					By.xpath(".//input[@id='b-earliestDepartureDate' and @name='b.earliestDepartureDate']"));
			int RowCount = driver
					.findElements(
							By.xpath(".//input[@id='b-earliestDepartureDate' and @name='b.earliestDepartureDate']"))
					.size();
			if (RowCount > 4) {
				testRunner.testStepResult = true;
				return;

			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.captureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			Log.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifyEqualTo4Rows Author: Ashok Description:
	 * verifyEqualTo4Rows
	 * *****************************************************************************
	 * ****************
	 */
	public static void verifyEqualTo4Rows(String object, String data, String pageName, String StepName) {
		ArrayList<WebElement> listItems;

		try {

			List<WebElement> tableExist = driver.findElements(By.xpath("//div[@id='schedule-plan-1']"));
			// Get the count of check boxes
			List<WebElement> AllRadiobuttons = driver.findElements(
					By.xpath(".//input[@id='b-earliestDepartureDate' and @name='b.earliestDepartureDate']"));
			int RowCount = driver
					.findElements(
							By.xpath(".//input[@id='b-earliestDepartureDate' and @name='b.earliestDepartureDate']"))
					.size();
			if (RowCount == 4) {
				testRunner.testStepResult = true;
				return;

			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.captureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			Log.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifyNotSelected Author: Ashok Description:
	 * verifyNotSelected
	 * *****************************************************************************
	 * ****************
	 */
	public static void verifyNotSelected(String dpdn, String data, String pageName, String stepName) {

		try {
			log_testActions.info(stepName);

			WebElement dpdnEle = getObject(getObjectLocator(dpdn));

			Select selectDPDN = new Select(dpdnEle);
			String selectedOptiion = selectDPDN.getFirstSelectedOption().getText();
			if (!selectedOptiion.contains(data)) {
				testRunner.testStepResult = true;
			}

			else {
				testRunner.testStepResult = false;
			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to select --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifyNotExists Author: Ashok Description:
	 * verifyNotExists
	 * *****************************************************************************
	 * ****************
	 */
	public static void verifyNotExists(String object, String data, String pageName, String StepName) {
		log_testActions.info("checking  Not Exists " + object + " in " + pageName);

		WebElement ele = null;
		String actual = null;

		try {

			ele = getObject(getObjectLocator(object));

			if (!ele.isDisplayed()) {
				testRunner.testStepResult = true;

			} else {

				testRunner.testStepResult = false;

			}
		}

		// make pass if element not found
		catch (NoSuchElementException e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.CaptureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			log_testActions.error(object + " Not able to find. Exception =  " + testRunner.stepException);
			testRunner.testStepResult = true;
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.CaptureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			log_testActions.error(object + " Not able to Check. Exception =  " + testRunner.stepException);
			testRunner.testStepResult = false;
		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifySelected Author: Ashok Description:
	 * verifySelected
	 * *****************************************************************************
	 * ****************
	 */
	public static void verifySelected(String object, String data, String pageName, String StepName) {

		String eleValue = "";
		WebElement ele;

		String searchText = data;

		try {
			log_testActions.info("Getting  the value  of " + object + " in " + pageName);

			log_testActions.info("Element Found. Getting value of " + object);

			ele = getObject(getObjectLocator(object));

			String selectedOption = new Select(getObject(getObjectLocator(object))).getFirstSelectedOption().getText();

			log_testActions.info("Element value = " + selectedOption);
			if (selectedOption.contains(data)) {

				testRunner.testStepResult = true;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.CaptureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			log_testActions.error("Not able to get the value in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;

		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifyListContains Author: Ashok Description:
	 * verifyListContains
	 * *****************************************************************************
	 * ****************
	 */
	public static void verifyListContains(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before Verifying " + object + " in " + pageName);

		ArrayList<String> inList;
		ArrayList<String> outList;
		ArrayList<WebElement> eleList;
		String[] inArray;
		try {
			if (isTestElementPresent(object)) {
				inArray = data.split(",");
				inList = new ArrayList<String>(Arrays.asList(inArray));
				eleList = (ArrayList<WebElement>) getObjects(getObjectLocator(object));
				List<WebElement> Options = new Select(getObject(getObjectLocator(object))).getOptions();

				outList = new ArrayList<String>(Options.size());
				boolean success = true;
				for (WebElement ele : Options) {
					outList.add(ele.getText());
				}

				int inListSize = inList.size();
				int outListSize = outList.size();

				if (inListSize <= outListSize) {
					for (int i = 0; i < inListSize; i++) {

						if (outList.contains(inArray[i])) {
							log_testActions.info("The inList item = " + inList.get(i) + " Equal to The outList item="
									+ outList.get(i));
							success = true;
							i++;
						} else {
							log_testActions.info("The inList item = " + inList.get(i)
									+ " Not Equal to The outList item=" + outList.get(i));
							success = false;
							break;
						}

					}
				} else {
					log_testActions.info("The size of inList=" + inListSize + " The size of outList=" + outListSize);
					success = false;
				}

				if (success) {
					testRunner.testStepResult = true;
					log_testActions.info("Verifying List of" + object + " in " + pageName + " Success");

				} else {

					log_testActions.info("Verifying List of" + object + " in " + pageName + " Fail");
					testRunner.testStepResult = false;

				}
			} else {
				testRunner.testStepResult = false;
				log_testActions.info("Verifying " + object + " in " + pageName + " Not found");

			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.CaptureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			log_testActions.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;

		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifyListNotContains Author: Ashok Description:
	 * verifyListNotContains
	 * *****************************************************************************
	 * ****************
	 */
	public static void verifyListNotContains(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before Verifying " + object + " in " + pageName);

		ArrayList<String> inList;
		ArrayList<String> outList;
		ArrayList<WebElement> eleList;
		String[] inArray;
		try {
			if (isTestElementPresent(object)) {
				inArray = data.split(",");
				inList = new ArrayList<String>(Arrays.asList(inArray));
				eleList = (ArrayList<WebElement>) getObjects(getObjectLocator(object));
				List<WebElement> Options = new Select(getObject(getObjectLocator(object))).getOptions();

				outList = new ArrayList<String>(Options.size());
				boolean success = true;
				for (WebElement ele : Options) {
					outList.add(ele.getText());
				}

				int inListSize = inList.size();
				int outListSize = outList.size();

				if (inListSize <= outListSize) {
					for (int i = 0; i < inListSize; i++) {

						if (!outList.contains(inArray[i])) {
							log_testActions.info("The inList item = " + inList.get(i) + " Equal to The outList item="
									+ outList.get(i));
							success = true;
							i++;
						} else {
							log_testActions.info("The inList item = " + inList.get(i)
									+ " Not Equal to The outList item=" + outList.get(i));
							success = false;
							break;
						}

					}
				} else {
					log_testActions.info("The size of inList=" + inListSize + " The size of outList=" + outListSize);
					success = false;
				}

				if (success) {
					testRunner.testStepResult = true;
					log_testActions.info("Verifying List of" + object + " in " + pageName + " Success");

				} else {

					log_testActions.info("Verifying List of" + object + " in " + pageName + " Fail");
					testRunner.testStepResult = false;

				}
			} else {
				testRunner.testStepResult = false;
				log_testActions.info("Verifying " + object + " in " + pageName + " Not found");

			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.CaptureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			log_testActions.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;

		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: getCurrentDate Author: Ashok Description:
	 * getCurrentDate
	 * *****************************************************************************
	 * ****************
	 */
	public static void getCurrentDate(String object, String data, String pageName, String StepName) {
		log_testActions.info("Setting Current date " + object + " in " + pageName);

		WebElement ele = null;

		try {

			ele = getObject(getObjectLocator(object));
			if (data.equals("currdate")) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date todayDate = Calendar.getInstance().getTime();
				String todayDateString = formatter.format(todayDate);
				ele.sendKeys(todayDateString);
				Thread.sleep(1000);
				testRunner.testStepResult = true;

			} else {

				testRunner.testStepResult = false;

			}

		}
		// make pass if element not found
		catch (NoSuchElementException e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.CaptureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			log_testActions.error(object + " Not able to find. Exception =  " + testRunner.stepException);
			testRunner.testStepResult = true;
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.CaptureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			log_testActions.error(object + " Not able to Check. Exception =  " + testRunner.stepException);
			testRunner.testStepResult = false;
		}

	}

	private static String getOrdinal(int n) throws Exception {

		try {
			if (n >= 11 && n <= 13) {
				return n + "th";
			}
			switch (n % 10) {
			case 1:
				return n + "st";
			case 2:
				return n + "nd";
			case 3:
				return n + "rd";
			default:
				return n + "th";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "Fail";
		}

	}
	/*
	 * *****************************************************************************
	 * ***************** Method: selectlinkTypeDropdown Author: Ashok Description:
	 * selectlinkTypeDropdown
	 * *****************************************************************************
	 * ****************
	 */

	public void selectlinkTypeDropdown(String object, String data, String pageName, String StepName) {

		try {
			List<WebElement> options = (List<WebElement>) getObject(getObjectLocator(object));

			// Loop through the options and select the one that matches
			for (WebElement opt : options) {
				if (opt.getText().contains(data)) {
					opt.click();
					return;
				}
			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.CaptureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			log_testActions.error("Not able to enter the text in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}

		throw new NoSuchElementException("Can't find " + data + " in dropdown");
	}

	public static String getCurrentTimeStamp() {
		SimpleDateFormat formDate = new SimpleDateFormat("dd/MM/yyyy");

		String strDate = formDate.format(new Date()); // option 2
		constants.Current_Date = strDate;
		return constants.Current_Date;
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifyPageUrl Author: Ashok Description:
	 * verifyPageUrl
	 * *****************************************************************************
	 * ****************
	 */
	public static void verifyPageUrl(String object, String data, String pageName, String StepName) {
		String expPageUrl = data;

		try {
			String pageTitle = driver.getTitle();
			if (pageTitle.contains("Certificate") || pageTitle.contains("Certificate")) {
				javaClick("lnk_CertificateErrorIE", "", "", "Click on continue certificate error");
				Thread.sleep(5000);

			}
			if (pageTitle.contains("Acceptable Usage Policy")) {
				javaClick("btn_IAcceptError", "", "", "Click on I Accept Button");
				Thread.sleep(5000);

			}
			String actualpageUrl = driver.getCurrentUrl();
			if (actualpageUrl.contains(expPageUrl)) {
				Thread.sleep(2000);
				System.out.println("Current page Url is: " + actualpageUrl);
				testRunner.testStepResult = true;
				return;
			} else {
				testRunner.testStepResult = false;
				return;
			}

		} catch (Exception e) {
			//testRunner.stepException = e.getMessage();
			Log.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifyPageLink Author: Ashok Description:
	 * verifyPageLink
	 * *****************************************************************************
	 * ****************
	 */

	public static boolean verifyPageLink(String object, String data, String pageName, String StepName) {
		WebElement ele = null;
		try {

			String actual;
			String expectedPageLinkXpath = "//a[contains(text(),'" + data + "')]";

			boolean actualResult = driver.findElement(By.xpath(expectedPageLinkXpath)).isDisplayed();

			if (actualResult == true) {
				testRunner.testStepResult = true;
				log_testActions.info("Expected  =" + data + " link is displayed");
				return true;

			} else {
				testRunner.testStepResult = false;
				log_testActions.info("Expected  =" + data + " link is Not displayed");
				return false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.CaptureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;
			return false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: selectRateLookUpRateCaluculationDate Author: Ashok
	 * Description: selectRateLookUpRateCaluculationDate
	 * *****************************************************************************
	 * ****************
	 */
	public static void selectRateLookUpRateCaluculationDate(String object, String data, String pageName,
			String StepName) throws ParseException {
		WebElement ele = null;
		String newDateString = null;
		int NumberToBeincrease;
		String[] objectdata = object.split(";");
		String dateWidget = objectdata[0];
		String monthField = objectdata[1];
		String yearField = objectdata[2];
		String testData[] = data.split(";");
		String effectedMode = testData[0];
		if (!effectedMode.equalsIgnoreCase("specificdate")) {
			NumberToBeincrease = Integer.parseInt(testData[1]);
		} else {
			NumberToBeincrease = 0;
		}
		String dateTobeEntered = testData[2];
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
		Date todayDate = Calendar.getInstance().getTime();
		String todayDateString = formatter.format(todayDate);
		System.out.println(todayDateString);
		Calendar c = Calendar.getInstance();
		c.setTime(formatter.parse(todayDateString));
		if (effectedMode.contains("days")) {
			c.add(Calendar.DATE, NumberToBeincrease); // number of days to add
			newDateString = formatter.format(c.getTime());
		}
		if (effectedMode.contains("month"))// number of months to add

		{
			c.add(Calendar.MONTH, NumberToBeincrease); // number of years to add
			newDateString = formatter.format(c.getTime());
		}
		if (effectedMode.contains("year")) {
			c.add(Calendar.YEAR, NumberToBeincrease); // number of years to add

			newDateString = formatter.format(c.getTime());
		}
		if (effectedMode.contains("currDate")) {
			newDateString = todayDateString;

		}
		if (effectedMode.contains("specificdate")) {

			newDateString = dateTobeEntered;

		}
		System.out.println(newDateString);
		String dateformat[] = newDateString.split("/");
		int dateValue = Integer.parseInt(dateformat[0]);
		String monthValue = dateformat[1];
		String yearValue = dateformat[2];

		String newdatelinkXpath = "//a[text()='" + dateValue + "']";

		try {

			((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight, 0)");
			WebElement dropdown = getObject(getObjectLocator(dateWidget));
			dropdown.click();
			selectListOption(yearField, yearValue, pageName, StepName);
			selectListOption(monthField, monthValue, pageName, StepName);
			ele = driver.findElement(By.xpath(newdatelinkXpath));
			ele.click();
			log_testActions.error("We are able select cuurent date plus two weeks date in rate lookup page");

		}

		catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("We are not able select cuurent date plus two weeks date in rate lookup page"
					+ pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: VerifyCurrentDateTimeInPage Author: Ashok
	 * Description: VerifyCurrentDateTimeInPage
	 * *****************************************************************************
	 * ****************
	 */
	public static void verifyCurrentDateTimeInPage(String object, String data, String pageName, String StepName) {
		WebElement ele = null;
		int style = DateFormat.MEDIUM;
		String DateLanguageFormate = data;
		DateFormat formatter;
		String todayDate = null;
		String Language = data;
		Date now = new Date();

		driver.navigate().refresh();

		if (Language.contains("Japanese")) {

			// case "Japanese":

			formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
			todayDate = formatter.format(Calendar.getInstance().getTime());
			// break;

		}
		if (Language.contains("Chinese traditional")) {
			// case "Chinese traditional":

			formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
			todayDate = formatter.format(Calendar.getInstance().getTime());
			// break;
		}
		if (Language.contains("Chinese Simplified")) {
			// case "Chinese Simplified":

			formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
			todayDate = formatter.format(Calendar.getInstance().getTime());
			// break;

		}
		if (Language.contains("Korean")) {
			// case "Korean":

			formatter = new SimpleDateFormat("dd MMM yyyy", Locale.KOREAN);
			todayDate = formatter.format(Calendar.getInstance().getTime());
			// break;

		}
		if (Language.contains("Fran")) {

			formatter = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
			todayDate = formatter.format(Calendar.getInstance().getTime());
			// break;
		}
		if (Language.contains("Italiano")) {
			// case "Italiano":

			formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ITALIAN);
			todayDate = formatter.format(Calendar.getInstance().getTime());
			// break;
		}

		if (Language.contains("Espa")) {

			formatter = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
			todayDate = formatter.format(Calendar.getInstance().getTime());
			// break;
		}
		if (Language.contains("English")) {
			// case "English":
			formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
			todayDate = formatter.format(Calendar.getInstance().getTime());
			// break;

		}

		String dateFieldXpath = "//*[contains(text(),'" + todayDate + "')]";

		ele = driver.findElement(By.xpath(dateFieldXpath));

		String ActualDateDisplayed = ele.getText();

		if (ActualDateDisplayed.contains(todayDate)) {
			testRunner.testStepResult = true;
			testRunner.reportLogger.log(LogStatus.PASS, "Current Date  " + ActualDateDisplayed, constants.KEYWORD_PASS);
			log_testActions.info(
					"Expected  =" + todayDate + " and Actual date is =" + ActualDateDisplayed + "should not changed");
			testRunner.testStepResult = true;

		} else {
			testRunner.testStepResult = false;
			log_testActions
					.error("Expected  =" + todayDate + " and Actual =" + ActualDateDisplayed + " values Not equal");

		}
		return;
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: clickPageLinkContains Author: Ashok Description:
	 * clickPageLinkContains
	 * *****************************************************************************
	 * ****************
	 */
	public static void clickPageLinkContains(String object, String data, String pageName, String StepName)
			throws Exception {

		String linkText = data;
		log_testActions.info("Clicking on Page link " + data + " in " + pageName);
		System.out.println("Clicking on Page link" + data + " in " + pageName);
		WebElement element = null;
		String pageLinkXpath = "//a[contains(text(),'" + linkText + "')]";
		try {

			element = driver.findElement(By.xpath(pageLinkXpath));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			Thread.sleep(3000);
			log_testActions.info("Clicked on " + object + " in " + pageName + "Success");
			System.out.println("Clicked on " + object + " in " + pageName + "Success");
			testRunner.testStepResult = true;
		}

		catch (Exception e) {
			// testRunner.imagePath = ExtentManager.captureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			Log.error("Not able to click --- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: clickPageLink Author: Ashok Description:
	 * clickPageLink
	 * *****************************************************************************
	 * ****************
	 */
	public static void clickPageLink(String object, String data, String pageName, String StepName) throws Exception {

		String linkText = data;
		log_testActions.info("Clicking on Page link " + data + " in " + pageName);
		System.out.println("Clicking on Page link" + data + " in " + pageName);
		WebElement element = null;

		try {

			element = driver.findElement(By.xpath("//a[contains(text(),'" + linkText + "')]"));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
			Thread.sleep(3000);
			log_testActions.info("Clicked on " + object + " in " + pageName + "Success");
			System.out.println("Clicked on " + object + " in " + pageName + "Success");
			testRunner.testStepResult = true;
		}

		catch (Exception e) {
			// testRunner.imagePath = ExtentManager.captureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			Log.error("Not able to click --- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: doubleClick Author: Ashok Description: doubleClick
	 * *****************************************************************************
	 * ****************
	 */
	public static void doubleClick(String object, String data, String pageName, String StepName) throws Exception {

		log_testActions.info("Clicking on " + object + " in " + pageName);
		System.out.println("Clicking on " + object + " in " + pageName);

		WebElement element = null;
		element = getObject(getObjectLocator(object));
		try {
			Actions action = new Actions(driver).doubleClick(element);
			action.build().perform();
			testRunner.testStepResult = true;

			System.out.println("Double clicked the element");
		} catch (StaleElementReferenceException e) {
			System.out.println("Element is not attached to the page document " + e.getStackTrace());
			testRunner.testStepResult = false;
		} catch (NoSuchElementException e) {
			System.out.println("Element " + object + " was not found in DOM " + e.getStackTrace());
			testRunner.testStepResult = false;
		} catch (Exception e) {
			System.out.println("Element " + object + " was not clickable " + e.getStackTrace());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: getRowsOrColCount Author: Ashok Description:
	 * getRowsOrColCount
	 * *****************************************************************************
	 * ****************
	 */
	public static String getRowsOrColCount(String object, String data, String pageName, String StepName)
			throws Exception {

		String eleValue = "";
		List<WebElement> rows = null;
		List<WebElement> cols = null;
		String[] testdata = data.split("~");
		String countType = testdata[0];
		String expectedCount = testdata[1];
		int expectedCount1 = Integer.parseInt(expectedCount);
		int RowCount;
		int ColCount;
		WebElement htmltable = getObject(getObjectLocator(object));

		try {
			log_testActions.info("Getting  the value  of " + object + " in " + pageName);
			if (countType.contains("RowCount")) {
				rows = htmltable.findElements(By.tagName("tr"));

				RowCount = rows.size();
				if (RowCount <= expectedCount1)
					testRunner.testStepResult = true;
			}
			if (countType.contains("ColCount")) {
				rows = htmltable.findElements(By.tagName("td"));
				ColCount = cols.size();
				if (ColCount <= expectedCount1)
					testRunner.testStepResult = true;

			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			// testRunner.imagePath = ExtentManager.CaptureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			log_testActions.error("Not able to get the value in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;

		}
		return eleValue;
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: changeToLanguage Author: Ashok Description:
	 * changeToLanguage
	 * *****************************************************************************
	 * ****************
	 */
	public static void changeToLanguage(String object, String data, String pageName, String StepName) {
		WebElement ele = null;

		try {
			Actions ToolTip1 = new Actions(driver);
			ele = getObject(getObjectLocator(object));

			Thread.sleep(2000);

			/* ToolTip1.moveToElement(ele).perform(); */
			javaClick("Maersk_SSP_lnk_languages", "", "HomePage", "Click on nav Language");
			javaClick("Maersk_SSP_lnk_English", "", "HomePage", "Click on nav Language");

			/*
			 * String languageLinkXpath="//a[contains(text(),'" + data +"')]";
			 * 
			 * WebElement LanguageElement = driver.findElement(By.xpath(languageLinkXpath));
			 * 
			 * LanguageElement.click();
			 */

			Thread.sleep(2000);
			System.out.println("Language is changed to " + data);
			testRunner.testStepResult = true;
			testRunner.reportLogger.log(LogStatus.PASS, "Apllication language is changed to  " + data,
					constants.KEYWORD_PASS);
			return;

		} catch (Exception e) {
			// testRunner.imagePath = ExtentManager.captureScreen(driver,
			// System.getProperty("user.dir")+"\\Screenshots\\Failed\\");
			Log.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: selectListValue Author: Ashok Description:
	 * selectListValue
	 * *****************************************************************************
	 * ***************
	 */
	public static void selectListValue(String object, String data, String pageName, String stepName) throws Exception {

		String[] testData = data.split(";");
		String[] objectData = object.split(";");
		String dpdnElement = objectData[0];
		String dpdnTextBox = objectData[1];
		String invalue1 = testData[0];
		String inValue2 = testData[1];
		String dpdnName = testData[2]; // String dpdnElement,String
										// dpdnTextBox,String invalue1,String
										// inValue2,String pageName,String
										// dpdnName

		String dpdnListValue;
		String input = invalue1;

		/*
		 * if(inValue2.length()==0) {
		 * 
		 */
		try {

			ScrollByPixel("", "down", "", "");

			if (dpdnName.equals("Commodity") || dpdnName.equalsIgnoreCase("Other")
					|| dpdnName.equalsIgnoreCase("country")) {
				dpdnListValue = "Xpath;//*[text()='" + input + "']/parent::div";
			}

			else if (dpdnName.equalsIgnoreCase("ContractPerson")) {
				dpdnListValue = "Xpath;//*[@class='select2-match' and contains(text(),'" + input + "')]";

			} else if (dpdnName.equals("ContainerType")) {

				String conCategory = input.split("-")[0];
				String conSizeType = input.split("-")[1];
				String conSize = conSizeType.substring(0, 2);
				String conType = conSizeType.substring(4);
				input = conSizeType;
				if (conCategory.contains("Shipper")) {

					if (conType.contains("Reefer"))

						dpdnListValue = "Xpath;//optgroup[contains(@class,'shippersownReefer')]/option[contains(text(),'"
								+ conSize + "') and contains(text(),'" + conType + "')]";

					else

						dpdnListValue = "Xpath;//optgroup[contains(@class,'shippersown')]/option[contains(text(),'"
								+ conSize + "') and contains(text(),'" + conType + "')]";
				} else {

					dpdnListValue = "Xpath;//div[contains(text(),'" + conCategory + "')]/parent::li";
				}

			} else if (dpdnName.equalsIgnoreCase("country") || dpdnName.equalsIgnoreCase("featurelist")) {
				dpdnListValue = "Xpath;//*[text()='" + input + "']/parent::div";
			}

			else {
				dpdnListValue = "Xpath;//*[text()='" + input + "']/parent::div[text()='" + inValue2 + "']";
			}

			// clickElement(dpdnElement, "", pageName, "Click on " + dpdnName + "
			// dropdown");
			// input(dpdnTextBox, input, pageName, "Enter value in " + dpdnName + "
			// textbox");
			// clickElement(dpdnListValue, "", pageName, "Select value " + dpdnName + "
			// dropdown");

			// WebElement dropdown = getObject(getObjectLocator(dpdnElement));

			WebElement dropdown = getObject(getObjectLocator(dpdnElement));
			dropdown.click();
			Thread.sleep(2000);
			input(dpdnTextBox, input, pageName, "Enter value in " + dpdnName + " textbox");
			Thread.sleep(2000);
			WebElement lsitvalue = getObject(getObjectLocator(dpdnListValue));
			lsitvalue.click();

			// if (dpdnName.equals("From"))
			// {

			ScrollByPixel("", "down", "", "");

			// }

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;

		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: isDisplayed Author: Ashok Description: used to
	 * verify the WebElement is Dispalyed
	 * *****************************************************************************
	 * ****************
	 */
	public static boolean isDisplayed(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before Verifying " + object + " in " + pageName);
		WebElement element = null;
		String locator=null;
		String value=null;
		boolean result;

		//waitForVisible(object);

		try {

			String sspPropertyObject = ObjectReader.OR.getProperty(object);	
			String[] getPropertyObject = sspPropertyObject.split(";");
			String path = getPropertyObject[0];
			String locatorValue = getPropertyObject[1];
			//element = getObject(getObjectLocator(object));
			
			if(path.equalsIgnoreCase("Id"))
			{
				result = driver.findElement(By.id(locatorValue)).isDisplayed();
			}
			else
			{
				result = driver.findElement(By.xpath(locatorValue)).isDisplayed();
			}
			
			
			
			
			//result = element.isDisplayed();
			
			
			if (result == true) {
				
				System.out.println("Element is displayed");
				log_testActions.info("Verifying " + object + " in " + pageName + " is displayed");
				System.out.println("Verifying " + object + " in " + pageName + " is displayed");
				testRunner.testStepResult = true;
				return true;
			} else {
				System.out.println("Element is  Not displayed");
				log_testActions.info("Verifying " + object + " in " + pageName + " Not displayed");
				System.out.println("Verifying " + object + " in " + pageName + " Not displayed");
				testRunner.testStepResult = false;
				return false;
			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
			return false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: isNotDisplayed Author: Ashok Description: used to
	 * verify the WebElement is Not Dispalyed
	 * *****************************************************************************
	 * ****************
	 */
	public static void isNotDisplayed(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before Verifying " + object + " in " + pageName);
		List<WebElement> ListElement;
		boolean ElementLocation;
		int noOfElementsFound = 0;

		try {
			// element=getObject(getObjectLocator(object));
			if (!isTestElementPresent(object)) {
				// ElementLocation=driver.findElement(getObjectLocator(object));
				// noOfElementsFound=ListElement.size();
				// ElementLocation=driver.findElement(getObjectLocator(object)).isDisplayed();

				testRunner.testStepResult = true;
				log_testActions.info("Verifying " + object + " in " + pageName + " is Not displayed");
				System.out.println("Verifying " + object + " in " + pageName + " is Not displayed");

			}

			if (!data.isEmpty())

			{
				ListElement = driver.findElements(By.xpath("//*[contains(text(),'" + data + "')]"));
				noOfElementsFound = ListElement.size();

				if (noOfElementsFound == 0) {
					testRunner.testStepResult = true;
					log_testActions.info("Verifying " + object + " in " + pageName + " is Not displayed");
					System.out.println("Verifying " + object + " in " + pageName + " is Not displayed");
					// return true;
				} else {
					testRunner.testStepResult = false;
					log_testActions.info("Verifying " + object + " in " + pageName + " is displayed");
					System.out.println("Verifying " + object + " in " + pageName + " is displayed");
					// return false;
				}
			}
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;
			// return false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: bookingFirstPageMCC Author: Ashok Description:
	 * bookingFirstPageMCC
	 * *****************************************************************************
	 * ****************
	 */
	public static void bookingFirstPageMCC(String object, String data, String pageName, String StepName)
			throws Exception {

		try {
			String[] testData = data.split(";");
			String[] objectData = object.split(";");
			int length = testData.length;

			log_testActions.info("Before napBooking ");

			String fromValue1 = testData[0];
			String fromValue2 = testData[1];
			String toValue1 = testData[2];
			String toValue2 = testData[3];
			String conType = testData[4];
			String commValue = testData[5];
			String contCount = testData[6];
			String conTypeValue = testData[7];
			String weight = testData[8];
			String contractTariff = testData[9];
			// String contactPerson = testData[10];
			String reference = testData[10];
			String exportType = testData[11];
			String importType = testData[12];

			/*
			 * String menuBook=objectData[0]; String subBook=objectData[1]; String
			 * cityFrom=objectData[0]; String cityTo=objectData[0]; String
			 * menuBook=objectData[0]; String menuBook=objectData[0]; String
			 * menuBook=objectData[0]; String menuBook=objectData[0]; String
			 * menuBook=objectData[0]; String menuBook=objectData[0];
			 */
			// navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New
			// BookShipment","Booking_Page","Nvaigate to new Booking page");
			if (testRunner.testName.contains("_MML_")) {

				// IGNnavigateMenu("Maersk_SSP_IGN_btn_Book;Maersk_SSP_IGN_lnk_BookNewShipment","BOOK~New
				// BookShipment","Booking_Page","Nvaigate to new Booking page");

			} else {
				/*
				 * navigateToUrl("", "https://myt.apmoller.net/shipmentbinder/", "Booking_Page",
				 * "navigated to new booking page");
				 */

				// navigateMenu("Maersk_SSP_Menu_BOOK;Maersk_SSP_subMenu_NewBookShipment","BOOK~New
				// BookShipment","Booking_Page","Nvaigate to new Booking page");
			}

			waitForVisible("txt_BookingPage1");
			compareValue("txt_BookingPage1", "Book New Shipments", "Booking_Page", "Verify Booking Page");
			selectDPDNValue("dpdn_From_BP", "txtbx_From_BP", fromValue1, fromValue2, "Booking_Page", "From");
			log_testActions.info(" From Address Selected as  = " + fromValue1);
			selectDPDNValue("dpdn_To_BP", "txtbx_To_BP", toValue1, toValue2, "Booking_Page", "To");
			log_testActions.info(" To Address Selected as  = " + toValue1);

			selectRateLookUpRateCaluculationDate(
					"Maersk_SSP_txtbx_DepartureDate;Maersk_SSP_lst_DepartureMonth;Maersk_SSP_lst_DepartureYear",
					"days;1;No", "Booking_Page", "");

			selectDPDNValue("dpdn_Commodity_BP", "txtbx_Commodity_BP", commValue, "", "Booking_Page", "Commodity");
			selectDPDNValue("dpdn_Container_BP", "txtbx_Container_BP", conTypeValue, "", "Booking_Page",
					"ContainerType");
			input("txtbx_Weight_BP", weight, "Booking_Page", "Enter weight");
			clickElement("Maersk_SSP_chk_iamPriceOwner", "", "Booking_Page", "Click on iam price owner check box");
			// input("txtbx_contract_BP", contractTariff, pageName, StepName);
			selectOptionUsingPartialText("Maersk_SSP_lst_PriceOwner", "Gcss Booked By, Copenhagen", pageName, StepName);
			// javaClick("btn_Continue_Book", "", pageName, StepName);

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" napBooking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: verifyWebElementAttributeValue Author: Ashok
	 * Description: verifyWebElementAttributeValue
	 * *****************************************************************************
	 * ****************
	 */
	public static void verifyWebElementAttributeValue(String object, String data, String pageName, String StepName) {
		WebElement ele = null;

		String expectedAttributrValue = data;
		String ActualAttributeValue = null;
		log_testActions.info("Before compareValue " + object + " in " + pageName);
		try {

			ele = getObject(getObjectLocator(object));
			ActualAttributeValue = ele.getAttribute("value");

			/*
			 * if(data.equals("Empty")){ ActualAttributeValue.isEmpty();
			 * testRunner.testStepResult=true; }else if(!data.contains("Empty")){
			 * if(data.contains("currDate")){ expectedAttributrValue=constants.Current_Date;
			 * }
			 */
			if (ActualAttributeValue.contains(expectedAttributrValue)) {

				testRunner.testStepResult = true;

			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;

		}
	}

	/*
	 ***********************************************************************************************
	 * Method: SwitchToWindow Author: Ashok Description: SwitchToWindow
	 **********************************************************************************************
	 */

	public static void switchToWindow(String object, String data, String pageName, String StepName) {

		String expectedWindPageTitle = data;
		String SecondPageTitle = null;
		String SecondWindowPageUrl = null;
		String mainwindow = driver.getWindowHandle(); // get parent(current) window name
		Set<String> AllHandles = driver.getWindowHandles();

		; // switch back to main window & continue further execution
		try {

			for (String winHandle : AllHandles) // iterating on child windows

			{

				/* if (!winHandle.equals(mainwindow)) */
				if (driver.switchTo().window(winHandle).getTitle().contains(data)
						|| driver.switchTo().window(winHandle).getTitle().contains("Acceptable Usage Policy")) {

					// if (driver.switchTo().window(winHandle).getTitle().contains("Acceptable Usage
					// Policy")) {
					// javaClick("btn_IAcceptError", "", "", "Click on I Accept Button");
					//
					// }

					String current_url = driver.getCurrentUrl();

					if (current_url.contains("https://login.zscaler.net")) {
						Thread.sleep(5000);
						javaClick("btn_IAcceptError", "", "", "Click on I Accept Button");
						Thread.sleep(5000);

					}

					// System.out.println("Popup values: "+winHandle);
					// SecondPageTitle=driver.getTitle();
					//
					// if(SecondPageTitle.contains(expectedWindPageTitle)) {
					// System.out.println("Second Window title"+SecondPageTitle);
					//
					// testRunner.testStepResult=true;
					//
					// }
					// SecondWindowPageUrl=driver.getCurrentUrl();
					// System.out.println("Second window url"+SecondWindowPageUrl);
					driver.close();
					Thread.sleep(2000);
					driver.switchTo().window(mainwindow);
					String mainWindowPageTitle = driver.getTitle();
					System.out.println("main window url" + mainWindowPageTitle);
					testRunner.testStepResult = true;

				}

				// Perform any operations on child window
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;

		}
	}

	/*
	 ***********************************************************************************************
	 * Method: SwitchToSecondWindow Author: Ashok Description: SwitchToSecondWindow
	 **********************************************************************************************
	 */
	public static void switchToSecondWindow(String object, String data, String pageName, String StepName) {

		String expectedWindPageTitle = data;
		String SecondPageTitle = null;
		String SecondWindowPageUrl = null;
		String mainwindow = driver.getWindowHandle(); // get parent(current) window name
		Set<String> AllHandles = driver.getWindowHandles();

		; // switch back to main window & continue further execution
		try {

			for (String winHandle : AllHandles) // iterating on child windows

			{
				/* if (!winHandle.equals(mainwindow)) */
				if (driver.switchTo().window(winHandle).getTitle().contains(data)) {

					/* driver.switchTo().window(winHandle); */
					System.out.println("Popup values: " + winHandle);
					SecondPageTitle = driver.getTitle();
					System.out.println("Second Window title" + SecondPageTitle);
					SecondWindowPageUrl = driver.getCurrentUrl();
					System.out.println("Second window url" + SecondWindowPageUrl);

					testRunner.testStepResult = true;

				}

				// Perform any operations on child window
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;

		}
	}

	/*
	 ***********************************************************************************************
	 * Method: closeWindow Author: Ashok Description: closeWindow
	 **********************************************************************************************
	 */
	public static void closeWindow(String object, String data, String pageName, String StepName) {

		String expectedWindPageTitle = data;
		String SecondPageTitle = null;
		String SecondWindowPageUrl = null;
		String mainwindow = driver.getWindowHandle(); // get parent(current) window name
		Set<String> AllHandles = driver.getWindowHandles();

		; // switch back to main window & continue further execution
		try {

			for (String winHandle : AllHandles) // iterating on child windows

			{
				/* if (!winHandle.equals(mainwindow)) */
				if (driver.switchTo().window(winHandle).getTitle().contains(data)) {

					driver.close();

					driver.switchTo().window(mainwindow);
					/*
					 * String mainWindowPageTitle=driver.getTitle();
					 * System.out.println("main window url"+mainWindowPageTitle);
					 */

					testRunner.testStepResult = true;
				}

				// Perform any operations on child window
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;

		}
	}

	/*
	 ***********************************************************************************************
	 * Method: SelectTxtboxListValue Author: Ashok Description:
	 * SelectTxtboxListValue
	 **********************************************************************************************
	 */
	public static void selectTxtboxListValue(String object, String data, String pageName, String stepName)
			throws Exception {

		String testData = data;

		try {
			WebElement ListTextBoxLocation = getObject(getObjectLocator(object));

			javaClick(object, testData, pageName, "enter text on " + object + " text box");

			input(object, testData, pageName, "enter text on " + object + " text box");
			Thread.sleep(5000);
			keyPressTab(object, "tab", pageName, "Enter value in " + object + " textbox");
			Thread.sleep(2000);

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;

		}

	}
	/*
	 ***********************************************************************************************
	 * Method: addIMOdetails Author: Ashok Description: Enter Add IMO Details Page
	 * Values
	 **********************************************************************************************
	 */

	public static void addIMOdetails(String object, String data, String pageName, String StepName) throws Exception {

		try {
			String[] testData = data.split(";");
			String[] testObject = object.split(";");
			int lengthData = testData.length;
			int objectLength = testObject.length;
			log_testActions.info("Before napBooking ");
			String imoclasificationObject = testObject[0];
			String UNorNAnumberObject = testObject[1];
			String UNnumberObject = testObject[2];
			String NAnumberObject = testObject[3];
			String quantityObject = testObject[4];
			String packingTypeObject = testObject[5];
			String packingGroupObject = testObject[6];
			String packQuantityTypeObject = testObject[7];
			String grossWeightObject = testObject[8];
			String netWeightObject = testObject[9];
			// String contactPerson = testObject[10];
			String properShippingNameObject = testObject[10];
			String emergencyContactNameObject = testObject[11];
			String EmergencyContactNum1Object = testObject[12];
			String EmergencyContactNum2Object = testObject[13];
			String imoclasification = testData[0];
			String UNorNAnumber = testData[1];
			String UNnumber = testData[2];
			String NAnumber = testData[3];
			String quantity = testData[4];
			String packingType = testData[5];
			String packingGroup = testData[6];
			String packQuantityType = testData[7];
			String grossWeight = testData[8];
			String netWeight = testData[9];
			// String contactPerson = testData[10];
			String properShippingName = testData[10];
			String emergencyContactName = testData[11];
			String EmergencyContactNum1 = testData[12];
			String EmergencyContactNum2 = testData[13];

			selectOptionUsingPartialText(imoclasificationObject, imoclasification, "AddIMO_Page",
					"Select Imo clasification Type");
			selectOptionUsingPartialText(UNorNAnumberObject, UNorNAnumber, "AddIMO_Page",
					"Select Imo clasification Type");
			if (UNnumberObject.equalsIgnoreCase("yes")) {
				javaClick(UNnumberObject, "", "AddIMOPage", "Select UN number");
			} else {
				javaClick(NAnumberObject, "", "AddIMOPage", "Select UN number");
			}

			input(quantityObject, quantity, "AddIMOPage", "Enter Quantity");
			selectOptionUsingPartialText(packingTypeObject, packingType, "AddIMO_Page", "Select Imo packingType ");
			selectOption(packingGroupObject, packingGroup, "AddIMO_Page", "Select Imo packingGroup ");
			selectOption(packQuantityTypeObject, packQuantityType, "AddIMO_Page", "Select Imo packingGroup ");
			input(grossWeightObject, grossWeight, "AddIMOPage", "Enter Gross Weight");
			input(netWeightObject, netWeight, "AddIMOPage", "Enter Net Weight");
			input(properShippingNameObject, properShippingName, "AddIMOPage", "Enter properShippingName");
			input(emergencyContactNameObject, emergencyContactName, "AddIMOPage", "Enter emergencyContactName");
			input(EmergencyContactNum1Object, EmergencyContactNum1, "AddIMOPage", "Enter EmergencyContactNum1");
			input(EmergencyContactNum2Object, EmergencyContactNum2, "AddIMOPage", "Enter EmergencyContactNum2");

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" napBooking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: isFileDownloaded Author: Abhimanyu Ramanandan
	 * Description: Method is used to check the file is downloaded
	 * *****************************************************************************
	 * ***************
	 */
	public static void isFileDownloaded(String object, String data, String pageName, String StepName) {
		String home = System.getProperty("user.home");
		File fileDir = new File(home + "/Downloads/");
		File[] fileDirContents = fileDir.listFiles();

		testRunner.testStepResult = false;

		try {
			for (int i = 0; i < fileDirContents.length; i++) {
				if (fileDirContents[i].getName().equals(data)) {
					// File has been found, it can now be deleted:
					fileDirContents[i].delete();
					testRunner.testStepResult = true;
					log_testActions.info("Verifying the file " + data + "is available" + "Success");
					System.out.println("Verifying the file " + data + "is available" + "Success");
				}

			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error(" Not able to verify the downloaded file");
			testRunner.testStepResult = false;
		}
	}

	public static void navigateMenu(String object, String data, String pageName, String StepName) throws Exception {

		String strMenu = null;
		String strSubMenu = null;
		try {
			if (testRunner.testName.contains("_MML_") && object.contains("Maersk_SSP_subMenu_NewBookShipment")) {

				navigateToUrl("", "https://myt.apmoller.net/booking/new", "Booking_Page",
						"navigated to new booking page");

			} else {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				Properties property = new Properties();

				String[] arrMenus = object.split(";");
				int menuLength = arrMenus.length;

				// clicking level 1
				/*
				 * strMenu = property.getProperty(strMenu); strMenu = strMenu.split(";")[1];
				 */

				if (arrMenus.length == 1) {
					strMenu = arrMenus[0];
					WebElement wlmMenu = getObject(getObjectLocator(strMenu));
					js.executeScript("arguments[0].focus();", wlmMenu);
					Actions builder = new Actions(driver);
					builder.moveToElement(wlmMenu).perform();

					wlmMenu.click();
					Thread.sleep(5000);
					driver.navigate().refresh();

					testRunner.testStepResult = true;
					log_testActions.info("Expected text " + strMenu + " is dispalyed in " + pageName);
					System.out.println("Expected text " + strMenu + " is dispalyed in " + pageName);

				}

				// level2

				if (arrMenus.length == 2) {
					strMenu = arrMenus[0];
					strSubMenu = arrMenus[1];
					WebElement wlmMenu = getObject(getObjectLocator(strMenu));
					Thread.sleep(2000);
					js.executeScript("arguments[0].focus();", wlmMenu);
					Thread.sleep(2000);
					Actions builder = new Actions(driver);
					builder.moveToElement(wlmMenu).click().perform();

					/*
					 * wlmMenu.click(); /* strSubMenu = property.getProperty(strSubMenu);
					 */
					WebElement wlmSubMenu = getObject(getObjectLocator(strSubMenu));
					Thread.sleep(2000);
					js.executeScript("arguments[0].focus();", wlmSubMenu);
					builder.moveToElement(wlmSubMenu).perform();
					Thread.sleep(2000);
					wlmSubMenu.click();
					Thread.sleep(3000);
					String pageTitle = driver.getTitle();
					if (pageTitle.contains("Certificate") || pageTitle.contains("Certificate")) {
						javaClick("lnk_CertificateErrorIE", "", "", "Click on continue certificate error");
						Thread.sleep(5000);

					}
					if (pageTitle.contains("Acceptable Usage Policy")) {
						javaClick("btn_IAcceptError", "", "", "Click on I Accept Button");
						Thread.sleep(5000);

					}
					log_testActions.info("Expected text " + strSubMenu + " is dispalyed in " + pageName);
					System.out.println("Expected text " + strSubMenu + " is dispalyed in " + pageName);
					testRunner.testStepResult = true;
					Thread.sleep(1000);
					driver.navigate().refresh();

				}

			}
		} catch (Exception e) {
			log_testActions.info(" reeferStarfreshSingleCargo Booking Failure. Exception = " + e.getMessage());
			testRunner.testStepResult = false;
			System.out.println(e.getMessage());
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : selectOptiontext Description :Used to text select Dropdown option
	 * Author: Razim Meeran
	 **********************************************************************************************************************
	 */

	public static void selectOptiontext(String object, String data, String pageName, String StepName) {

		String[] testData = data.split(";");
		String[] objectData = object.split(";");
		String dpdnElement = objectData[0];
		String dpdnTextBox = objectData[1];
		String invalue1 = testData[0];
		String selection = testData[1];
		String dpdnListValue;
		int selectvalue = Integer.parseInt(selection);

		try {

			// clickElement(dpdnElement, "", pageName, "Click on dropdown");
			// List<WebElement> listOptions=
			// driver.findElements(getObjectLocator(dpdnTextBox));
			// listOptions.get(selectvalue).sendKeys(invalue1);
			// dpdnListValue = "Xpath;//*[text()='" + invalue1 + "']";
			// clickElement(dpdnListValue, "", pageName, "Select value from dropdown");

			WebElement dropdown = getObject(getObjectLocator(dpdnElement));
			dropdown.click();
			Thread.sleep(2000);
			List<WebElement> listOptions = driver.findElements(getObjectLocator(dpdnTextBox));
			listOptions.get(selectvalue).sendKeys(invalue1);
			dpdnListValue = "Xpath;//*[text()='" + invalue1 + "']";
			WebElement lsitvalue = getObject(getObjectLocator(dpdnListValue));
			lsitvalue.click();

			testRunner.testStepResult = true;

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to select --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : selectDropdown Description :Used to select value from Dropdown OD3CP
	 * Flag ON Author: Razim Meeran
	 **********************************************************************************************************************
	 */

	public static void selectDropdown(String object, String data, String pageName, String StepName) {

		String[] testData = data.split(";");
		String[] objectData = object.split(";");
		String textbox = objectData[0];
		String testdata = testData[0];
		try {
			input(textbox, data, "Booking", "CreateBooking");
			Thread.sleep(3000);

			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
					By.xpath("//*[contains(@class,'tt-highlight') and contains(text(),'" + testdata + "')]")));
			List<WebElement> list = driver.findElements(
					By.xpath("//*[contains(@class,'tt-highlight') and contains(text(),'" + testdata + "')]"));

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getText().equals(data)) {
					WebElement dValue = list.get(i);
					wait.until(ExpectedConditions.elementToBeClickable(dValue)).click();

					// list.get(i).click();
					Thread.sleep(3000);
					break;
				}
			}
			testRunner.testStepResult = true;
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to select --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: datePicker Author: Razim Description:
	 * Calander_Caluculation
	 * *****************************************************************************
	 * ****************
	 */

	public static void datePicker(String object, String data, String pageName, String StepName) throws ParseException {

		String newDateString;

		String[] objectdata = object.split(";");
		String dateWidget = objectdata[0];
		String monthField = objectdata[1];
		String yearField = objectdata[2];

		String testData[] = data.split(";");
		String effectedMode = testData[0];

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
		Date todayDate = Calendar.getInstance().getTime();
		String todayDateString = formatter.format(todayDate);
		System.out.println(todayDateString);

		String currentdateformat[] = todayDateString.split("/");
		String dateValue_cur = currentdateformat[0];

		String dateValue_split[] = dateValue_cur.split("");
		String dateFirstUnit = dateValue_split[0];
		String datesecondUnit = dateValue_split[1];
		String date_txtbx_shortvalue = "";

		if (dateFirstUnit.equals("0")) {
			date_txtbx_shortvalue = datesecondUnit;

		} else {
			date_txtbx_shortvalue = dateValue_cur;
		}

		String monthValue_cur = currentdateformat[1];
		String yearValue_cur = currentdateformat[2];

		try {

			TestActions.ScrollByPixel(object, "down", pageName, StepName);

			WebElement datePicker = driver.findElement(By.xpath("//*[@id='pseudo-date-input-shipmentDate']"));
			Actions act = new Actions(driver);
			act.moveToElement(datePicker).click().build().perform();

			WebElement datePicker_frame1 = driver.findElement(
					By.xpath("//*[@id='ui-datepicker-div']//following::*[text()='" + date_txtbx_shortvalue + "']"));
			datePicker_frame1.click();

			log_testActions.error("We are able select cuurent date plus two weeks date in rate lookup page");
		}

		catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("We are not able select cuurent date plus two weeks date in rate lookup page"
					+ pageName + "--- " + e.getMessage());

			testRunner.testStepResult = false;

		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: Calander_Caluculation Author: Razim Description:
	 * Calander_Caluculation
	 * *****************************************************************************
	 * ****************
	 */

	public static void Calander_Caluculation(String object, String data, String pageName,

			String StepName) throws ParseException {

		WebElement ele = null;
		String newDateString = null;
		int NumberToBeincrease;
		String[] objectdata = object.split(";");
		String dateWidget = objectdata[0];
		String monthField = objectdata[1];
		String yearField = objectdata[2];
		String testData[] = data.split(";");
		String effectedMode = testData[0];

		if (!effectedMode.equalsIgnoreCase("specificdate")) {
			NumberToBeincrease = Integer.parseInt(testData[1]);
		} else {
			NumberToBeincrease = 0;
		}

		String dateTobeEntered = testData[2];
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy");
		Date todayDate = Calendar.getInstance().getTime();
		String todayDateString = formatter.format(todayDate);
		System.out.println(todayDateString);

		String currentdateformat[] = todayDateString.split("/");

		int dateValue_cur = Integer.parseInt(currentdateformat[0]);
		String monthValue_cur = currentdateformat[1];
		String yearValue_cur = currentdateformat[2];

		Calendar c = Calendar.getInstance();
		c.setTime(formatter.parse(todayDateString));

		if (effectedMode.contains("days")) {
			c.add(Calendar.DATE, NumberToBeincrease); // number of days to add
			newDateString = formatter.format(c.getTime());
		}
		if (effectedMode.contains("month"))// number of months to add

		{
			c.add(Calendar.MONTH, NumberToBeincrease); // number of years to add
			newDateString = formatter.format(c.getTime());
		}
		if (effectedMode.contains("year")) {
			c.add(Calendar.YEAR, NumberToBeincrease); // number of years to add

			newDateString = formatter.format(c.getTime());
		}
		if (effectedMode.contains("currDate")) {
			newDateString = todayDateString;

		}
		if (effectedMode.contains("specificdate")) {

			newDateString = dateTobeEntered;
		}

		System.out.println(newDateString);
		String dateformat[] = newDateString.split("/");

		int dateValue = Integer.parseInt(dateformat[0]);
		String monthValue = dateformat[1];
		String yearValue = dateformat[2];

		try {

			// javaClick("Maersk_btn_AcceptCookies","","Home_Page","Click on Accept
			// cookies");

			WebElement datePicker = driver.findElement(By.id("pseudo-date-input-shipmentDate"));
			Actions act = new Actions(driver);
			act.moveToElement(datePicker).click().build().perform();

			WebElement nextLink = driver.findElement(
					By.xpath("//*[@id='ui-datepicker-div']//a[@class='ui-datepicker-next ui-corner-all']"));

			for (;;) {
				WebElement month_txtbx = driver.findElement(By.xpath("//*[@class='ui-datepicker-month']"));
				String month_txtbxvalue = month_txtbx.getText();
				System.out.println("Text of webelement: " + month_txtbxvalue);

				String month_txtbx_shortvalue = ""; // substring containing first 4 characters

				if (month_txtbxvalue.length() > 3) {
					month_txtbx_shortvalue = month_txtbxvalue.substring(0, 3);
				} else {
					month_txtbx_shortvalue = month_txtbxvalue;
				}

				if (monthValue.equalsIgnoreCase(month_txtbx_shortvalue)) {
					WebElement datePicker_frame1 = driver.findElement(
							By.xpath("//*[@id='ui-datepicker-div']//following::*[text()='" + dateValue + "']"));
					datePicker_frame1.click();
					break;

				} else {
					WebElement nextLink1 = driver.findElement(By.xpath("//*[@id='ui-datepicker-div']/div/a[2]"));
					nextLink1.click();
					WebElement datePicker_frame1 = driver.findElement(
							By.xpath("//*[@id='ui-datepicker-div']//following::*[text()='" + dateValue + "']"));
					datePicker_frame1.click();

				}

			}

			log_testActions.error("We are able select cuurent date plus two weeks date in rate lookup page");

		}

		catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("We are not able select cuurent date plus two weeks date in rate lookup page"
					+ pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;

		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: OD3CPbookingFirstPage Author: Razim Description:
	 * Enter All values for Booking on OD3CP Flag on first page
	 * *****************************************************************************
	 * ****************
	 */

	public static void OD3CPbookingFirstPage(String object, String data, String pageName, String StepName)
			throws Exception {

		try {
			String[] testData = data.split(";");
			int length = testData.length;

			log_testActions.info("Before napBooking ");

			String fromValue1 = testData[0];
			String toValue1 = testData[1];
			String date = testData[2];
			String commodity = testData[3];
			String conType = testData[4];
			String contCount = testData[5];
			javaClick("Maersk_btn_AcceptCookies", "", "Booking_Page", "Click on Accept cookies");

			selectDropdown("txtbx_From_new_BP", fromValue1, "Booking_Page", "From");
			selectDropdown("txtbx_To_new_BP", toValue1, "Booking_Page", "To");
			javaClick("Maersk_SSIB_PriceOwner", "", "Booking_Page", "PriceOwner");
			javaClick("Maersk_SSIB_Add_PriceOwner", "", "Booking_Page", "PriceOwner");

			String loginname = driver
					.findElement(By.xpath("//*[@id='nav-login-btn']/.//span[@class='ign-nav__buttons__item__text']"))
					.getAttribute("innerHTML");
			System.out.println("login name: " + loginname);

			if (loginname.equals("SSIB Portal")) {

			} else {

				javaClick("Maersk_SSIB_Contract", "", "Booking_Page", "contract");
				javaClick("Maersk_SSIB_Add_Contract", "", "Booking_Page", "contract");

			}

			datePicker("Maersk_SSP_txtbx_DepartureDate;Maersk_SSP_lst_DepartureMonth;Maersk_SSP_lst_DepartureYear",
					date, "Booking_Page", "date");
			selectDropdown("txtbx_Commodity_new_BP", commodity, "Booking_Page", "Commodity");
			waitFor("", "2000", "Booking_Page");
			selectDropdown("txtbx_Containertype_new_BP", conType, "Booking_Page", "ContainerType");
			waitFor("", "2000", "Booking_Page");
			clearTextBox("Maersk_SSIB_Drop_NO_Container", "", "Booking_Page", "Containercount");
			input("Maersk_SSIB_Drop_NO_Container", contCount, "Booking_Page", "Containercount");

			if (fromValue1.equalsIgnoreCase("Jawaharlal Nehru, India")) {
				clearTextBox("txtbx_Containerweight", "", "Booking_Page", "Containerwaight");
				input("txtbx_Containerweight", "500", "Booking_Page", "Containerwaight");

			}

			else {
				javaClick("Maersk_chk_Shipersowncontainer", "", "Booking_Page", "shiperscontainer");

			}

			waitFor("", "2000", "Booking_Page");

			// datePicker("Maersk_SSP_txtbx_DepartureDate;Maersk_SSP_lst_DepartureMonth;Maersk_SSP_lst_DepartureYear",
			// date, "Booking_Page", "date");
			clickElement("Maersk_SSIB_Continue_Button", "", "Booking_Page", "Continue");
			javaClick("Maersk_SSIB_popup", "", "Booking_Page", "popup");
			javaClick("Maersk_SSIB_popup_continue", "", "Booking_Page", "popup");

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" napBooking Failure. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : mouseHoverText Description :Used to verify the mouse hover text of
	 * the element Author: Razim
	 **********************************************************************************************************************
	 */
	public static void mouseHoverText(String object, String data, String pageName, String stepName) {

		try {

			Actions builder = new Actions(driver);
			WebElement tool_tip = getObject(getObjectLocator(object));
			builder.moveToElement(tool_tip).perform();
			String tool_tip_msg = tool_tip.getText();

			if (tool_tip_msg.equals(data)) {
				testRunner.testStepResult = true;
			}

			// getObject(getObjectLocator(object)).getAttribute("value");

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Mouse hover text is not matched" + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 ***********************************************************************************************
	 * Method: SwitchToWindow Author: Razim Description: SwitchToWindow
	 **********************************************************************************************
	 */

	public static void switchToWindow2(String object, String data, String pageName, String StepName) {

		String expectedWindPageTitle = data;
		String SecondPageTitle = null;
		String SecondWindowPageUrl = null;
		String mainwindow = driver.getWindowHandle();
		Set<String> AllHandles = driver.getWindowHandles();

		try {

			driver.close();
			driver.switchTo().window(mainwindow);
			testRunner.testStepResult = true;

			// for(String winHandle :AllHandles)
			// {
			// if
			// (driver.switchTo().window(winHandle).getTitle().contains(data)||driver.switchTo().window(winHandle).getTitle().contains("Acceptable
			// Usage Policy")){
			//
			// String current_url = driver.getCurrentUrl();
			//
			//
			//
			// //
			// testRunner.testStepResult=true;
			// }
			// }

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;
		}
	}
	/*
	 ***********************************************************************************************
	 * Method: cargoconfirmation Author: Razim Description: cargoconfirmation
	 **********************************************************************************************
	 */

	public static void cargoconfirmation(String object, String data, String pageName, String StepName) {

		try {
			Thread.sleep(1000);
			driver.findElement(By.xpath("//*[@id='confirm-button']")).click();
			testRunner.testStepResult = true;

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;
		}
	}
	/*
	 * *****************************************************************************
	 * ***************** Method: captureBooking Author: Razim Description: Capture
	 * booking number
	 * *****************************************************************************
	 * ****************
	 */

	public void captureBooking(String object, String data, String pageName, String StepName) throws Exception {
		constants.Booking_Number = null;

		try {

			log_testActions.info("Capture Booking number");

			String bookinNumber = driver.findElement(By.xpath("//*[contains(text(),'Your booking has number')]"))
					.getText();
			String partialBooking_Number = bookinNumber.replaceAll("\\D", "");

			String bookno = bookinNumber;
			String AllWords[] = bookno.split(" ");

			for (int i = 0; i < AllWords.length; i++) {
				if (AllWords[i].contains(partialBooking_Number)) {
					constants.Booking_Number = AllWords[i];
					constants.Booking_Number = constants.Booking_Number.replaceAll("[.]", "");
					break;
				}

			}

			System.out.println(constants.Booking_Number);
			log_testActions.info(" Shipment Booking Number = " + constants.Booking_Number);
			log_testActions.info("CaptureBooking Success ");
			testRunner.reportLogger.log(LogStatus.PASS, "B1 Napbooking Number " + constants.Booking_Number,
					constants.KEYWORD_PASS);

			testRunner.testStepResult = true;

		} catch (Exception ex) {

			log_testActions.info(" Capture Booking. Exception = " + ex.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : mouseHover Description :Used to move cursor on web element Author:
	 * Razim
	 **********************************************************************************************************************
	 */
	public static void mouseHover(String object, String data, String pageName, String stepName) {
		try {

			log_testActions.info("Before clicking main menu");
			WebElement menu = getObject(getObjectLocator(object));
			Actions action = new Actions(driver);
			action.moveToElement(menu).perform();

			testRunner.testStepResult = true;
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to move --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : refreshWindow Description :Used to refresh the window Author: Razim
	 **********************************************************************************************************************
	 */
	public static void refreshWindow(String object, String data, String pageName, String stepName) {
		try {
			driver.navigate().refresh();
			log_testActions.info("window get refreshed");
			testRunner.testStepResult = true;
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to refresh --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : cardFavouriteSelection Description :select Dropdown option from card
	 * Author: Razim
	 **********************************************************************************************************************
	 */
	public static void cardFavouriteSelection(String object, String data, String pageName, String StepName) {
		try {

			String[] objectProperties = object.split(";");

			String btn_SearchORAdd = objectProperties[0];

			javaClick(btn_SearchORAdd, " ", "SelectionTab", "SelectionButton");
			Thread.sleep(2000);
			// javaClick("parties_searchtab", " ", "SelectionTab", "SelectionTab");

			driver.findElement(By.xpath("//*[@id='cmd-search-party-tab']")).click();

			javaClick("parties_customerCodeTab", " ", "SelectionTab", "Customercode");
			input("parties_txt_custCode", data, "SelectionTab", "Customercode");
			javaClick("parties_btnSearch", " ", "SelectionTab", "SelectionTab");

			Thread.sleep(3000);

			javaClick("confirmParty_sonfirmbtm", " ", "SelectionTab", "SelectionTab");

			testRunner.testStepResult = true;

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to select --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : cardFavouriteADDNewParty Description :Adding New party from card
	 * Author: Razim
	 **********************************************************************************************************************
	 */
	public static void cardFavouriteADDNewParty(String object, String data, String pageName, String StepName) {
		try {

			String[] objectProperties = object.split(";");

			String btn_SearchORAdd = objectProperties[0];

			javaClick(btn_SearchORAdd, " ", "SelectionTab", "SelectionButton");
			Thread.sleep(2000);
			// javaClick("parties_searchtab", " ", "SelectionTab", "SelectionTab");

			driver.findElement(By.xpath("//*[@id='cmd-search-party-tab']")).click();

			javaClick("parties_customerCodeTab", " ", "SelectionTab", "Customercode");
			input("parties_txt_custCode", data, "SelectionTab", "Customercode");
			javaClick("parties_btnSearch", " ", "SelectionTab", "SelectionTab");

			Thread.sleep(3000);

			javaClick("confirmParty_sonfirmbtm", " ", "SelectionTab", "SelectionTab");

			testRunner.testStepResult = true;

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to select --- " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: javaTabClick Author: Razim Description: Java Script
	 * click
	 * *****************************************************************************
	 * ****************
	 */
	public static void tabClick(String object, String data, String pageName, String StepName) throws Exception {

		try {

			Thread.sleep(2000);
			driver.findElement(By.xpath("//*[contains(@id,'vas-cmd-trading-name-tab')]/a")).click();

			log_testActions.info("Clicked on " + object + " in " + pageName + "Success");
			System.out.println("Clicked on " + object + " in " + pageName + "Success");
			testRunner.testStepResult = true;
		}

		catch (Exception e) {
			testRunner.stepException = e.getMessage();
			Log.error("Not able to click --- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: ScrollByPixel Author: Razim Description: Scroll By
	 * Pixel
	 * *****************************************************************************
	 * ****************
	 */
	public static void ScrollByPixel(String object, String data, String pageName, String StepName) throws Exception {
		try {

			JavascriptExecutor js = (JavascriptExecutor) driver;

			if (data.contains("down")) {
				js.executeScript("window.scrollBy(0,-300)");
			} else {
				js.executeScript("window.scrollBy(0,300)");
			}

			Thread.sleep(2000);
			log_testActions.info("Scrolled Successfully");
			// System.out.println("Scrolled Successfully");
			testRunner.testStepResult = true;
		}

		catch (Exception e) {
			testRunner.stepException = e.getMessage();
			Log.error("Not able to click --- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : VerifyTextboxValue Description : Used to Get the Existing value in
	 * the text box Author: Razim
	 **********************************************************************************************************************
	 */

	public static String VerifyTextboxValue(String object, String data, String pageName, String StepName) {

		String eleValue = "";
		String textBoxdata = data;

		try {
			log_testActions.info("Getting  the value  of " + object + " in " + pageName);

			if (isTestElementPresent(object)) {

				log_testActions.info("Element Found. Getting value of " + object);
				eleValue = getObject(getObjectLocator(object)).getText();

				if (eleValue.equals(textBoxdata)) {

					log_testActions.info("Element value = " + eleValue);
					System.out.println("");
					testRunner.testStepResult = true;
				} else {
					System.out.println("Expected Text not present");
					log_testActions.info("Expected value is not matched with  " + eleValue);
					testRunner.testStepResult = false;
				}

			} else {
				log_testActions.info("Element Not Found for  " + object);
				testRunner.testStepResult = false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to get the value in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}
		return eleValue;
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: dataisNotDisplayed Author: Razim Description: used
	 * to verify the given data is Not Displayed
	 * *****************************************************************************
	 * ****************
	 */
	public static void dataisNotDisplayed(String object, String data, String pageName, String StepName) {

		try {
			boolean Error = driver.getPageSource().contains(data);
			if (Error == false) {
				testRunner.testStepResult = true;
				log_testActions.error("The expected data " + data + " is not present");
			} else {
				testRunner.testStepResult = false;
				log_testActions.error("The expected data " + data + "  is present");
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to verify the presence of the data");
			testRunner.testStepResult = false;
			// return false;
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : AcceptCookies Description : Accept the coolies policies Author:
	 * Razim
	 **********************************************************************************************************************
	 */
	public static void AcceptCookies(String object, String data, String pageName, String stepName) {

		boolean success = false;

		try {
			log_testActions.info("Waiting for the Element to visible...");

			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='ign-accept-cookie']"))).click();

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.info("Unable to find Element to visible--- " + e);

		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : cargoconfirmationin_Auto Description : Auto Cargo confirmation in
	 * Booking page 2 Author: Razim
	 **********************************************************************************************************************
	 */
	public static void cargoconfirmationin_Auto(String object, String data, String pageName, String stepName) {

		boolean success = false;

		try {
			log_testActions.info("Waiting for the Element to visible...");

			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='confirm-button']"))).click();

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.info("Unable to find Element to visible--- " + e);
		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : twillWindow Description : Accept the twillWindow Author: Razim
	 **********************************************************************************************************************
	 */
	public static void twillWindow(String object, String data, String pageName, String stepName) {

		boolean success = false;

		try {
			log_testActions.info("Waiting for the Element to visible...");

			// if(testRunner.testStepResult==false)
			// {
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//*[@href='https://demo.maersk.com/dashboard/']"))).click();

			// }
			// else
			// {
			// testRunner.testStepResult = false;
			// }

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.info("Unable to find Element to visible--- " + e);
			// testRunner.testStepResult=true;
		}

	}

	/*
	 ********************************************************************************************************************
	 * Method : deleteCookies Description : delete the cookies Author: Razim
	 **********************************************************************************************************************
	 */
	public static void deleteCookies(String object, String data, String pageName, String stepName) {

		try {
			log_testActions.info("Waiting for the Element to visible...");
			driver.manage().deleteAllCookies();

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.info("Unable to find Element to visible--- " + e);
			// testRunner.testStepResult=true;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: ScrollToElementVisibility Author: Razim
	 * Description: ScrollToElementVisibility
	 * *****************************************************************************
	 * ****************
	 */
	public static void ScrollToElementVisibility(String object, String data, String pageName, String StepName)
			throws Exception {
		try {

			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("window.scrollBy(0,-300)");

			// log_testActions.info("Clicked on " + object + " in " + pageName + "Success");
			// System.out.println("Move to" + object + " in " + pageName + "Success");
			Thread.sleep(3000);
			testRunner.testStepResult = true;
		}

		catch (Exception e) {
			testRunner.stepException = e.getMessage();
			Log.error("Not able to click --- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * Method: menuclickElement Author: Razim Description: To click on Menu and Sub
	 * Menu Modified Date: 24/02/2021
	 * *****************************************************************************
	 */

	public static void menuclickElement(String object, String data, String pageName, String StepName) throws Exception {

		
		WebElement ele = null;
		Actions actions = new Actions(driver);
		String objectProperty[] = object.split(";");
		
		try {
			
			if(objectProperty.length==1)
			{
				
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				WebElement M1 = getObject(getObjectLocator(object));
				if (M1 != null)
				{
					actions.moveToElement(M1).perform();
					M1.click();
					System.out.println("Parent menu "+ M1 +"is Menu  clicked");
				}
				else
				{
					testRunner.testStepResult = true;
					
				}
				
			}
			else
			{
				
				String menu1 = objectProperty[0];
				String menu2 = objectProperty[1];
				WebElement M1 = getObject(getObjectLocator(menu1));
				WebElement M2 = getObject(getObjectLocator(menu2));
				Thread.sleep(5000);
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				if (M1 != null)
				{
					actions.moveToElement(M1).perform();
					System.out.println("Parent menu "+ M1 +"is Menu  clicked");

					if (M2 != null) {
						actions.moveToElement(M2).perform();
						M2.click();
						log_testActions.info("Menu clicked");	
						System.out.println("Child menu "+ M2 +"is Menu  clicked");
						testRunner.testStepResult = true;
					} else {
						testRunner.testStepResult = false;
					}
				} else {
					testRunner.testStepResult = false;
				}
				
			}	
		}

		catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to click. Exception =  " + testRunner.stepException);
			testRunner.testStepResult = false;
		}
	}

	/*
	 * *****************************************************************************
	 * ***************** Method: siteCannotBeReached Author: Razim Description:
	 * Refresh the window
	 * *****************************************************************************
	 * ****************
	 */

	public static void siteCannotBeReached(String object, String data, String pageName, String StepName)
			throws Exception

	{

		try {

			WebDriverWait wait = new WebDriverWait(driver, 0);
			boolean present = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='main-content']/div[1]")))
					.isDisplayed();

			if (present == true) {
				System.out.println("Present");
				log_testActions.info("Site cannot be reached window displayed");
				System.out.println("temporarily down is present");
				driver.navigate().to(driver.getCurrentUrl());
			}

		} catch (Exception e) {
			log_testActions.info("Unable to find Element to visible--- " + e);
		}

	}

	/*
	 * *****************************************************************************
	 * ***************** Method: VerifyListValueElementNotPresent Author: Razim
	 * Description: To verify the list element is not present
	 * *****************************************************************************
	 * ***************
	 */
	public static void VerifyListValueElementNotPresent(String object, String data, String pageName, String stepName)
			throws Exception {

		String[] testData = data.split(";");
		String[] objectData = object.split(";");
		String dpdnElement = objectData[0];
		String dpdnTextBox = objectData[1];
		String invalue1 = testData[0];
		String inValue2 = testData[1];
		String dpdnName = testData[2]; // String dpdnElement,String
										// dpdnTextBox,String invalue1,String
										// inValue2,String pageName,String
										// dpdnName

		String dpdnListValue;
		String input = invalue1;

		/*
		 * if(inValue2.length()==0) {
		 * 
		 */
		try {

			ScrollByPixel("", "down", "", "");

			if (dpdnName.equalsIgnoreCase("ContractPerson")) {
				dpdnListValue = "Xpath;//*[@class='select2-match' and contains(text(),'" + input + "')]";

			}

			else {
				dpdnListValue = "Xpath;//*[text()='" + input + "']/parent::div[text()='" + inValue2 + "']";
			}

			WebElement dropdown = getObject(getObjectLocator(dpdnElement));
			dropdown.click();
			Thread.sleep(2000);
			input(dpdnTextBox, input, pageName, "Enter value in " + dpdnName + " textbox");
			Thread.sleep(2000);

			Boolean isPresent = driver
					.findElements(By.xpath("//*[@class='select2-match' and contains(text(),'" + input + "')]"))
					.size() > 0;

			if (isPresent = true) {

				System.out.println("Not available for the contact");

			} else {
				System.out.println("Available for the contact");
				getObject(getObjectLocator(dpdnListValue)).click();
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to verify the presence of the element");
			testRunner.testStepResult = false;

		}

	}

	/*
	 ********************************************************************************************************************
	 * Method : comparePageTextnotPresent Description : Used to verify page text is
	 * not displayed on the page Author: Ashok
	 **********************************************************************************************************************
	 */

	public static void comparePageTextnotPresent(String object, String data, String pageName, String StepName) {

		try {

			WebElement ele = null;

			String eleXpath = "//*[contains(text(),'" + data + "')]";

			if (driver.getPageSource().contains(data)) {

				testRunner.testStepResult = false;
				log_testActions.info("Expected = Element presnt in the page");
				System.out.println("Expected = Element presnt in the page");

			} else {

				testRunner.testStepResult = true;
				log_testActions.info("Expected = Element not presnt in the page");
				System.out.println("Expected = Element not presnt in the page");
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Element presetnt in the page");
			testRunner.testStepResult = false;

		}
	}

	/*
	 ********************************************************************************************************************
	 * Method : verifyBookingPage1Details Description : Used to verify the booking
	 * page 1 data Date: 16/02/2021 Author: Razim
	 **********************************************************************************************************************
	 */

	public static void verifyBookingPage1Details(String object, String data, String pageName, String StepName) {

		try {
			log_testActions.info("Getting  the value  of " + object + " in " + pageName);

			String[] objectProperties = object.split(";");
			String fromProperty = objectProperties[0];
			String toProperty = objectProperties[1];
			String commodityProperty = objectProperties[2];
			String containerProperty = objectProperties[3];
			String containernoProperty = objectProperties[4];

			String[] testData = data.split(";");
			String fromdata = testData[0];
			String todata = testData[1];
			String comoditydata = testData[2];
			String containerdata = testData[3];
			String containernodata = testData[4];

				waitForVisible(fromProperty);

			VerifyTextboxattribute(fromProperty, fromdata, pageName, StepName);
			VerifyTextboxattribute(toProperty, todata, pageName, StepName);
			VerifyTextboxattribute(commodityProperty, comoditydata, pageName, StepName);
			VerifyTextboxattribute(containerProperty, containerdata, pageName, StepName);
			VerifyTextboxattribute(containernoProperty, containernodata, pageName, StepName);

			testRunner.testStepResult = true;

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to get the value in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}
		// return eleValue;
	}

	/*
	 ********************************************************************************************************************
	 * Method : VerifyTextboxattribute Description : Used to verify the Existing
	 * attribute of the text box Date : 16/02/2021 Author: Razim
	 **********************************************************************************************************************
	 */
	public static String VerifyTextboxattribute(String object, String data, String pageName, String StepName) {

		String attribute = "";
		String textBoxdata = data;

		try {
			log_testActions.info("Getting  the value  of " + object + " in " + pageName);

			if (isTestElementPresent(object)) {

				log_testActions.info("Element Found. Getting value of " + object);
				WebElement eleValue = getObject(getObjectLocator(object));
				attribute = eleValue.getAttribute("data-text");

				if (attribute.equals(textBoxdata)) {
					log_testActions.info("Element value = " + eleValue);
					System.out.println("Verified booking page data: " + attribute);
					testRunner.testStepResult = true;
				} else {
					System.out.println("Expected Text not present");
					log_testActions.info("Expected value is not matched with  " + eleValue);
					testRunner.testStepResult = false;
				}

			} else {
				log_testActions.info("Element Not Found for  " + object);
				testRunner.testStepResult = false;
			}

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to get the value in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}
		return attribute;
	}
	
	
	/*
	 ********************************************************************************************************************
	 * Method : isElementPresent Description :Verify is WebElement is present on
	 * WebPage Author: Ashok
	 **********************************************************************************************************************
	 */

	public static boolean isElementPresent(String object) throws Exception {

		if (getObject(getObjectLocator(object)) != null) {

			log_testActions.info("element found for  " + object);
			testRunner.testStepResult = true;
			return true;
		}

		else {
			log_testActions.info("element not found for  " + object);
			testRunner.testStepResult = false;
			return false;

		}

	}
	/*
	 ********************************************************************************************************************
	 * Method : comparetextinPage Description : Used to compare page text is displayed on
	 * the page 
	 * Author: Razim
	 * Date:01/03/2021
	 **********************************************************************************************************************
	 */
	public static boolean comparetextinPage(String object, String data, String pageName, String StepName) {

		WebElement ele;
		boolean blnfound = true;
		String strTempValue[], strNewDatVal;
		String strTemp, strTemp1, strdate, strDate1, strDate2;
		log_testActions.info("Before compareValue " + object + " in " + pageName);
		System.out.println("Before compareValue " + object + " in " + pageName);

		try {
			
						
			if (driver.getPageSource().contains(data)) 
			{
				blnfound = true;
				log_testActions.info("Expected text " + data + " is dispalyed in " + pageName);
				System.out.println("Expected text " + data + " is dispalyed in " + pageName);
				testRunner.testStepResult = true;
			}
			else
			{
				blnfound = false;
				testRunner.testStepResult = false;
			}
				
				return blnfound;
				
		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			log_testActions.error("Not able to compareValue");
			testRunner.testStepResult = false;
			return false;
		}

	}
	
	

	/*
	 ********************************************************************************************************************
	 * Method : shipmentSearch Description : Go to Shipment overview export and search the booking Author:
	 * Razim Date : 04/03/2021 
	 **********************************************************************************************************************
	 */

	public static void shipmentSearch(String object, String data, String pageName, String StepName)throws Exception {

		String[] objectProperties = object.split(";");
		String TxtTrackShipNo = objectProperties[0];
		String BtntrackShipNo = objectProperties[1];
		String binder = objectProperties[2];


		try {
			log_testActions.info("Entering the text in " + object + " in " + pageName);
			System.out.println("Entered  value in " + object + "in" + pageName);
			
			
			String url = driver.getCurrentUrl();		
			String[] split_URL = url.split("/");
			String brandName = split_URL[2];
			String Export_url = "https://" + brandName + "/shipmentoverview/export";	
			driver.navigate().to(Export_url);
			
			String bookingNumber = data;

			Thread.sleep(2000);		
			input(TxtTrackShipNo, bookingNumber, pageName, StepName);
			javaClick(BtntrackShipNo, "", pageName, StepName);
			
			WebDriverWait wait_forPageLoad = new WebDriverWait(driver, 5);
			wait_forPageLoad.until(ExpectedConditions.urlContains("/shipmentbinder"));

			String BinderURL= driver.getCurrentUrl();
			
			if(BinderURL.contains("/shipmentbinder"))
			{
				testRunner.testStepResult = true;
			}
			else
			{
				testRunner.testStepResult = false;
			}
			
			
			
			

		} catch (Exception e) {
			testRunner.stepException = e.getMessage();
			System.out.println("Exception");

			log_testActions.error("Not able to enter the text in " + pageName + "--- " + e.getMessage());
			testRunner.testStepResult = false;
		}
	}
	
	
	
	
	/*
	 * *****************************************************************************
	 * ***************** Method: HandeleAddWindow Author: Razim Description: uHandling 
	 * Add Window from Home Screen for MML PP Date: 04/03/2021
	 * *****************************************************************************
	 * ****************
	 */
	public static boolean HandeleAddWindow(String object, String data, String pageName, String StepName) {
		log_testActions.info("Before Verifying " + object + " in " + pageName);
		WebElement element = null;
		boolean result;
		JavascriptExecutor js;

		try {
//			element = getObject(getObjectLocator(object));
//			result = element.isDisplayed();
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class='nfd-modal__window']")));

			
			if (driver.findElement(By.xpath("//*[@class='nfd-modal__window']")).isDisplayed() == true) {
				
				//System.out.println("True");
				//log_testActions.info("Verifying " + object + " in " + pageName + " is displayed");
				//System.out.println("Verifying " + object + " in " + pageName + " is displayed");
				
				
				if (driver instanceof JavascriptExecutor) {
				    js = (JavascriptExecutor) driver;
				
				js.executeScript("return document.getElementsByClassName('nfd-modal__window')[0].remove();");
				}
				
				return true;
			} else {
				//System.out.println("False");
				log_testActions.info("Verifying " + object + " in " + pageName + " Not displayed");
				System.out.println("Verifying " + object + " in " + pageName + " Not displayed");
				return true;
			}
		} catch (Exception e) {
			//testRunner.stepException = e.getMessage();

			log_testActions.error("Not able to verify the presence of the element");
			//testRunner.testStepResult = true;
			return true;
		}
	}

	
	/*
	 * *****************************************************************************
	 * ***************** Method: HandeleAddWindow Author: Razim Description: uHandling 
	 * Add Window from Home Screen for MML PP Date: 04/03/2021
	 * *****************************************************************************
	 * ****************
	 */
	public static void printDropdownList(String object, String data, String pageName, String StepName) {
		

		try {		
			
			//Select select = new Select(driver. findElement(By.xpath("//select[@id='imoType']")));
			

			Select select = new Select(driver. findElement(By.xpath("//select[@id='imoUnNumber']")));
			
			
			List<WebElement> list = select.getOptions();
			
			for(int i=0;i<list.size();i++){
				
//				String unNaNumber=list.get(i).getText();				
//				System.out.println(unNaNumber);
//				
//				String replacevalue = unNaNumber.replaceAll(") ", "#");
//				String[] wholeunNaNumber = replacevalue.split("#");
//				String Number=wholeunNaNumber[0];
//				String replaceNumber= Number.replaceAll("(", "");
//				
//				String un=wholeunNaNumber[1];
				
				
				//System.out.println(replaceNumber);
				//System.out.println(un);
				
				System.out.println(list.get(i).getText());
			}
			
			 

			}
		 catch (Exception e) {
			
		}
	}


}
