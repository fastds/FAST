package edu.gzu.image.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import edu.gzu.image.Functions;
import edu.gzu.image.sphericalhtm.Pair;

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
	@Test
	public void testfHMSbase()
	{
		double ra =  57.50890;
		System.out.println(Functions.fHMSbase(ra,1,2));
		assertEquals("testfHMSbase测试不通过", "03:50:02.13", Functions.fHMSbase(ra, 1,2));
	}
	@Test
	public void testfDMSbase()
	{
		double dec =   0.06656;
		System.out.println(Functions.fDMSbase(dec,1,1));
		assertEquals("testfDMSbase测试不通过", "+00:03:59.6", Functions.fDMSbase(dec,1,1));
	}
	@Test
	public void testFIAUFromEq()
	{
		double ra =  57.50890;
		double dec =   0.06656;
		System.out.println(Functions.fIAUFromEq(ra, dec));
		assertEquals("testFIAUFromEq测试不通过", "SDSS J035002.13+000359.6", Functions.fIAUFromEq(ra, dec));
	}
	@Test
	public void test()
	{
		double nx=0.5371682317808762;
		double ny = 0.8434743275522294;
		double nz = 0.0011616908888386443;
		double radius = 13.166842132769187;
		List<Pair> list = Functions.htmCoverCircleXyz(nx,ny,nz,radius);
		System.out.println("listSize:"+list.size());
		for(Pair p : list)
		{
			System.out.println(p.getLo()+","+p.getHi());
		}
	}

}
