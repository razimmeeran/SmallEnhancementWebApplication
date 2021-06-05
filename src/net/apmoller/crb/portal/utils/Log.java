package net.apmoller.crb.portal.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Log {

// Initialise Log4j logs
//	private static Logger Log = Logger.getLogger(Log.class.getName());
	
		private static Logger log ;
		
		public static void loadLog4j(String file)
		{
			DOMConfigurator.configure(file); 
			
		}

		
		public static Logger getLog(String className)
		{	
			return log = Logger.getLogger(className);
		}


// Marks the beginning of the Test Case
	public static void startTestCase(String sTestCaseName) {

		log.info("****************************************************************************************");
		log.info("****************************************************************************************");
		log.info("-------------------------- " + sTestCaseName+ "---------------------------");
		log.info("****************************************************************************************");
		log.info("****************************************************************************************");
		
		System.out.println("-------------------------- " + sTestCaseName+ "---------------------------");
		

	}

// End of Test Case
	public static void endTestCase(String sTestCaseName) {
		log.info("XXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D-"	+ "             XXXXXXXXXXXXXXXXXXXXXX");
		log.info("X");
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXX             " + sTestCaseName+"---E---N---D---"	+ "             XXXXXXXXXXXXXXXXXXXXXX");

	}

	// Need to create these methods, so that they can be called
	public static void info(String message) {
		log.info(message);
	}

	public static void warn(String message) {
		log.warn(message);
	}

	public static void error(String message) {
		log.error(message);
	}

	public static void fatal(String message) {
		log.fatal(message);
	}

	public static void debug(String message) {
		log.debug(message);
	}

}