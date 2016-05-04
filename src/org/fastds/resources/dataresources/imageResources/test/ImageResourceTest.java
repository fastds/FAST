package org.fastds.resources.dataresources.imageResources.test;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class ImageResourceTest {
	@Test
	public void testEncoding()
	{
		String str = "鍐呭";
		try {
			System.out.println(new String(str.getBytes("GBK"),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
