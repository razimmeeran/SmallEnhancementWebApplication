package net.apmoller.crb.portal.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



import net.apmoller.crb.portal.config.Constants;

public class ObjectReader {
		
	public static Properties OR;
	
	public static Properties readObjRepository() throws IOException
	{
		String Path_OR = Constants.Path_ObjectRepo;
		FileInputStream fs = new FileInputStream(Path_OR);
		OR= new Properties();
		OR.load(fs);
		return OR;
	}	


}
