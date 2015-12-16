/**
 * @author jamesmarva jamesmarva@163.com
 * @date Sep 1, 2015 4:42:41 AM
 */
package edu.gzu.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ControlSpaceImpl {

	/**
	 * This method is run images to make a new container
	 * @param username
	 * @param ip
	 * return 0 or 1 
	 */
	public int addNew(final String username, final String ip) {
		
		String startCommand =  "sudo sh /opt/fastdb/initSciDB.sh " + username + " " + ip;
		System.out.println("AddNewSpace.java: username " + username);
		System.out.println("AddNewSpace.java: ip " + ip);
		Process mProcess = null;
		int mExitValue = -1;
		try {
			mProcess = Runtime.getRuntime().exec(startCommand);
			BufferedReader output = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
			String line = "";
			while ((line = output.readLine()) != null) {
				System.out.println(line);
			}
			output.close();
			
			mExitValue = mProcess.waitFor();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if (0 != mExitValue) {
			return 0;
		} else if (mExitValue == 0) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * The method is to stop container
	 * @param username
	 * @return int 
	 */
	public int stopSpace(String username) {
		String mStopCommand = "sudo sh /opt/fastdb/stopSciDB.sh " + username;
//		Process mProcess = null;
		int mExitValue = -1;
		try {
			Process mProcess = Runtime.getRuntime().exec(mStopCommand);
			mExitValue = mProcess.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (0 != mExitValue) {
			return 0;
		} else if (mExitValue == 0) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * The method is to start container
	 * @param username
	 * @param ip
	 * @return int 0 or 1
	 */
	public int startSpace(String username, String ip) {
		String mStartCommand = "sudo sh /opt/fastdb/startSciDB.sh " + username + " "+ ip; 
		int mExitValue = -1;
		try {
			Process mProcess = Runtime.getRuntime().exec(mStartCommand);
			mExitValue = mProcess.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (0 != mExitValue) {
			return 0;
		} else if (mExitValue == 0) {
			return 1;
		} else {
			return 0;
		}
	}
}
