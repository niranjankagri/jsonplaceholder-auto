package com.typicode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Defines all the Utility functions shared by the framework:
 * reading application data and logging.
 */
public class Util {

	// Created on first use; Log4j2 reads src/main/resources/log4j2.properties from the classpath
	private static Logger logger;
	// Loaded once from application-data.properties on first read
	private static Properties applicationProperties;
	
	/**
	 * Reads the values as per defined key-value pairs in application data properties file.
	 * The file is read from the working directory (the project root when run through Maven).
	 * @param propKey : Keys as per defined in properties file
	 * @return : Respective Value of Property Key, or null if the key (or the file) is missing
	 */
	public static String readApplicationData(String propKey) {
		if(applicationProperties == null) {
			applicationProperties = new Properties();
			try(InputStream input = new FileInputStream("application-data.properties")) {
				applicationProperties.load(input);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return applicationProperties.getProperty(propKey);
	}
	
	/**
	 * Returns logger to print messages to the console and log4j-application.log
	 * @return : {@link Logger}
	 */
	public static Logger getLogger() {
        if(logger == null) {
            logger = LogManager.getLogger(Util.class);
        }
        
        return logger;
    }
}
