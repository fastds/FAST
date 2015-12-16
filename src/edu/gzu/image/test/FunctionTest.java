package edu.gzu.image.test;

import org.junit.Test;

import edu.gzu.image.Functions;

public class FunctionTest {
	@Test
	public void testFGetNearestObjEq()
	{
		double ra = 336.746;
		double dec = -1.005;
		double radius = 10.790221168704853;
		String res = Functions.fGetNearestObjEq(ra, dec, radius);
		System.out.println("aql:"+res);//skyserver(objID):1237654669203013744
	}
	@Test
	public void testFGetNearbyFrameEq()
	{
//		fGetNearbyFrameEq(159.815, -0.655,8.96020779435244,0);
		System.out.println(Functions.fGetNearbyFrameEq(337.138,-0.951,17.960885819228594,0));
		
	}

}
