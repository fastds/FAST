package org.fastds.dbutil;

import java.util.UUID;

public class UUIDUtil
{
	public static String generateUUID()
	{
		String[] nums = UUID.randomUUID().toString().toUpperCase().split("-");
		String res = "";
		for(String temp: nums)
		{
			
			res += temp;
		}
		return res;
	}
}