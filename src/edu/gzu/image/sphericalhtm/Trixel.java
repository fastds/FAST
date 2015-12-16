package edu.gzu.image.sphericalhtm;

import edu.gzu.image.sphericallib.Cartesian;
import edu.gzu.image.sphericallib.Constant;

public class Trixel {
	 // Fields
    public static  double DblTolerance = (2.0 * Constant.DoublePrecision);
    private final int eHIDBits = 0x40;
    public static final double Epsilon = 1E-14;
    public static final double Epsilon2 = 2.5E-08;
    private final long IDHIGHBIT = 0x2000000000000000L;
    private final long IDHIGHBIT2 = 0x1000000000000000L;
    private final long N0 = 12L;
    private final long N1 = 13L;
    private final long N2 = 14L;
    private final long N3 = 15L;
    private final long S0 = 8L;
    private final long S1 = 9L;
    private final long S2 = 10L;
    private final long S3 = 11L;
    public final int TrixelNameLength = 0x20;

    // Methods
    private Trixel()
    {
    }

//    public static long CartesianToHid(double x, double y, double z, int depth)
//    {
//        int num;
//        return XyzToNameOrHid(x, y, z, depth, null, out num);
//    }

    public static long CartesianToHid20(Cartesian v)
    {
        int num = 0;
        if (Cartesian.IsNaN(v))
        {
            return 0L;
        }
//        return XyzToNameOrHid(v.get_X(), v.get_Y(), v.get_Z(), 20, null, out num);
        return XyzToNameOrHid(v.getX(), v.getY(), v.getZ(), 20, null, num);
    }

     static boolean Contains(Cartesian p, Cartesian v1, Cartesian v2, Cartesian v3)
    {
        if (Cartesian.TripleProduct(v1, v2, p) < -DblTolerance)
        {
            return false;
        }
        if (Cartesian.TripleProduct(v2, v3, p) < -DblTolerance)
        {
            return false;
        }
        if (Cartesian.TripleProduct(v3, v1, p) < -DblTolerance)
        {
            return false;
        }
        return true;
    }

    public static Pair Extend(long htmid, int level)
    {
        long num;
        long num2;
        int num4;
        int num3 = LevelOfHid(htmid);
        if (level > num3)
        {
            num4 = 2 * (level - num3);
            num = htmid << num4;
            long num5 = ((long) 1L) << num4;
            num5 -= 1L;
            num2 = num + num5;
        }
        else
        {
            num4 = 2 * (num3 - level);
            num = htmid >> num4;
            num2 = num;
        }
        return new Pair(num, num2);
    }

    public static boolean IsAncestor(long grandpa, long hid)
    {
        int num = LevelOfHid(hid) - LevelOfHid(grandpa);
        if (num < 0)
        {
            return false;
        }
        long num2 = hid >> (2 * num);
        return (num2 == grandpa);
    }

    public static boolean IsValid(long hid)
    {
        if (hid < 8L)
        {
            return false;
        }
        for (int i = 0; i < 0x40; i += 2)
        {
            if (0L != ((hid << i) & 0x2000000000000000L))
            {
                break;
            }
            if (0L != ((hid << i) & 0x1000000000000000L))
            {
                return false;
            }
        }
        return true;
    }

    public static int LevelOfHid(long htmid)
    {
        int num = 0;
        if (htmid < 0L)
        {
            return -1;
        }
        int num2 = 2;
        while (num2 < 0x40)
        {
            if (0L != ((htmid << (num2 - 2)) & 0x2000000000000000L))
            {
                break;
            }
            num2 += 2;
        }
        num = (0x40 - num2) / 2;
        return (num - 2);
    }

    public static long NameToHid(String sname)
    {
        if (sname != null)
        {
            int length = sname.length();
            return NameToHid(sname.toCharArray(), length);
        }
        return 0L;
    }

    public static long NameToHid(char[] name, int effectivelength)
    {
        long num2;
        long num5;
        int num6;
        long num = 0L;
        int length = 0;
        length = name.length;
        if (name.length < 2)
        {
            return 0L;
        }
        if ((name[0] != 'N') && (name[0] != 'S'))
        {
            return num;
        }
        if (length > 0x20)
        {
            return 0L;
        }
        length = effectivelength;
        for (int i = length - 1; i > 0; i--)
        {
            if ((name[i] > '3') || (name[i] < '0'))
            {
                return 0L;
            }
            num5 = name[i] - '0';
            num6 = 2 * ((length - i) - 1);
            num2 = num5 << num6;
            num += num2;
        }
        num5 = 2L;
        if (name[0] == 'N')
        {
            num5 += 1L;
        }
        num6 = (2 * length) - 2;
        num2 = num5 << num6;
        return (num + num2);
    }

     static long NameToHid(char[][] names, int select, int len)
    {
        return NameToHid(names[select], len);
    }

//    public static boolean NameToTriangle(char[] name, out Cartesian c0, out Cartesian c1, out Cartesian c2)
//    {
//        boolean flag = false;
//        int index = name[1] - '0';
//        c0 = Cartesian.NaN;
//        c1 = Cartesian.NaN;
//        c2 = Cartesian.NaN;
//        if ((index < 0) || (index > 3))
//        {
//            return flag;
//        }
//        if ((name[0] != 'N') && (name[0] != 'S'))
//        {
//            return flag;
//        }
//        if (name[0] == 'N')
//        {
//            index += 4;
//        }
//        HtmState instance = HtmState.Instance;
//        Cartesian cartesian = new Cartesian(instance.originalPoints[instance.faces[index].vi0], false);
//        Cartesian cartesian2 = new Cartesian(instance.originalPoints[instance.faces[index].vi1], false);
//        Cartesian cartesian3 = new Cartesian(instance.originalPoints[instance.faces[index].vi2], false);
//        Cartesian naN = Cartesian.NaN;
//        Cartesian cartesian5 = Cartesian.NaN;
//        Cartesian cartesian6 = Cartesian.NaN;
//        for (index = 2; name[index] != '\0'; index++)
//        {
//            cartesian6.SetMiddlePoint(cartesian, cartesian2, true);
//            naN.SetMiddlePoint(cartesian2, cartesian3, true);
//            cartesian5.SetMiddlePoint(cartesian3, cartesian, true);
//            switch (name[index])
//            {
//                case '0':
//                    cartesian2.Set(cartesian6, false);
//                    cartesian3.Set(cartesian5, false);
//                    break;
//
//                case '1':
//                    cartesian.Set(cartesian2, false);
//                    cartesian2.Set(naN, false);
//                    cartesian3.Set(cartesian6, false);
//                    break;
//
//                case '2':
//                    cartesian.Set(cartesian3, false);
//                    cartesian2.Set(cartesian5, false);
//                    cartesian3.Set(naN, false);
//                    break;
//
//                case '3':
//                    cartesian.Set(naN, false);
//                    cartesian2.Set(cartesian5, false);
//                    cartesian3.Set(cartesian6, false);
//                    break;
//            }
//        }
//        c0 = new Cartesian(cartesian, false);
//        c1 = new Cartesian(cartesian2, false);
//        c2 = new Cartesian(cartesian3, false);
//        return true;
//    }

//    public static long Parse(String in_string)
//    {
//        long num;
//        boolean flag = false;
//        if (in_string.length() < 1)
//        {
//            return 0L;
//        }
//        try
//        {
//            flag = (in_string[0] == 'S') || (in_string[0] == 'N');
//        }
//        catch
//        {
//        }
//        if (flag)
//        {
//            return NameToHid(in_string);
//        }
//        try
//        {
//            num = long.Parse(in_string);
//            if (!IsValid(num))
//            {
//                num = 0L;
//            }
//        }
//        catch
//        {
//            num = 0L;
//        }
//        return num;
//    }
//
    private static long startpane(/*out*/ Cartesian[] v0, /*out*/ Cartesian[] v1, /*out*/ Cartesian[] v2, double xin, double yin, double zin, char[] name)
    {
        v0[0] = Cartesian.NaN;
        v1[0] = Cartesian.NaN;
        v2[0] = Cartesian.NaN;
        long num = 0L;
        if ((xin > 0.0) && (yin >= 0.0))
        {
            num = (zin >= 0.0) ? 15L : 8L;
        }
        else if ((xin <= 0.0) && (yin > 0.0))
        {
            num = (zin >= 0.0) ? 14L : 9L;
        }
        else if ((xin < 0.0) && (yin <= 0.0))
        {
            num = (zin >= 0.0) ? 13L : 10L;
        }
        else if ((xin >= 0.0) && (yin < 0.0))
        {
            num = (zin >= 0.0) ? 12L : 11L;
        }
        else
        {
            num = (zin >= 0.0) ? 15L : 8L;
        }
        if (num <= 0L)
        {
            return -1L;
        }
        HtmState instance = HtmState.getInstance();
        int index = (int) (num - 8L);
        v0[0] = new Cartesian(instance.originalPoints[instance.faces[index].vi0], false);
        v1[0] = new Cartesian(instance.originalPoints[instance.faces[index].vi1], false);
        v2[0] = new Cartesian(instance.originalPoints[instance.faces[index].vi2], false);
        if (name != null)
        {
            name[0] = instance.faces[index].name[0];
            name[1] = instance.faces[index].name[1];
            name[2] = '\0';
        }
        return num;
    }
//
//    private static int ToIndexArray(int[] name, long hid)
//    {
//        long num4;
//        int index = 0;
//        if (hid < 0L)
//        {
//            return -2;
//        }
//        if (hid < 8L)
//        {
//            return -1;
//        }
//        int num2 = 2;
//        while (num2 < 0x40)
//        {
//            long num5 = hid << (num2 - 2);
//            num4 = num5 & 0x2000000000000000L;
//            if (num4 != 0L)
//            {
//                break;
//            }
//            if ((num5 & 0x1000000000000000L) != 0L)
//            {
//                return -1;
//            }
//            num2 += 2;
//        }
//        index = (0x40 - num2) >> 1;
//        for (num2 = 0; num2 < (index - 1); num2++)
//        {
//            int num3 = (int) ((hid >> (num2 * 2)) & 3L);
//            name[(index - num2) - 1] = num3;
//        }
//        num4 = (hid >> ((index * 2) - 2)) & 1L;
//        if (num4 != 0L)
//        {
//            name[1] += 4;
//            name[0] = 0x65;
//        }
//        else
//        {
//            name[0] = 100;
//        }
//        name[index] = 0x63;
//        return index;
//    }

    public static int ToName(char[] name, long hid)
    {
        long num4;
        int index = 0;
        if (hid < 0L)
        {
            return -2;
        }
        if (hid < 8L)
        {
            return -1;
        }
        int num2 = 2;
        while (num2 < 0x40)
        {
            long num5 = hid << (num2 - 2);
            num4 = num5 & 0x2000000000000000L;
            if (num4 != 0L)
            {
                break;
            }
            if ((num5 & 0x1000000000000000L) != 0L)
            {
                return -1;
            }
            num2 += 2;
        }
        index = (0x40 - num2) >> 1;
        for (num2 = 0; num2 < (index - 1); num2++)
        {
            int num3 = 0x30 + ((int) ((hid >> (num2 * 2)) & 3L));
            name[(index - num2) - 1] = (char) num3;
        }
        num4 = (hid >> ((index * 2) - 2)) & 1L;
        if (num4 != 0L)
        {
            name[0] = 'N';
        }
        else
        {
            name[0] = 'S';
        }
        name[index] = '\0';
        return index;
    }

    public static String ToString(long hid)
    {
        char[] name = new char[0x20];
        int length = ToName(name, hid);
        if (length > 0)
        {
            return new String(name, 0, length);
        }
        return "NaHID";
    }

//    public static void ToTriangle(long hid, out Cartesian a, out Cartesian b, out Cartesian c)
//    {
//        char[] name = new char[0x20];
//        ToName(name, hid);
//        NameToTriangle(name, out a, out b, out c);
//    }

    public static long Truncate(long htmid, int level)
    {
        long num = htmid;
        int num2 = LevelOfHid(htmid);
        if (level < num2)
        {
            num = htmid >> (2 * (num2 - level));
        }
        return num;
    }


//    public static String XyzToHidName(double x, double y, double z, int depth)
//    {
//        int num;
//        char[] name = new char[0x20];
//        XyzToNameOrHid(x, y, z, depth, name, out num);
//        return new String(name, 0, num);
//    }
//
    private static long XyzToNameOrHid(double x, double y, double z, int depth, char[] name,/* out*/ int len)
    {
        long num2;
        Cartesian[] cartesian = new Cartesian[1];
        Cartesian[] cartesian2 = new Cartesian[1];
        Cartesian[] cartesian3 = new Cartesian[1];
        Cartesian p = new Cartesian(x, y, z, true);
        Cartesian naN = Cartesian.NaN;
        Cartesian cartesian5 = Cartesian.NaN;
        Cartesian cartesian6 = Cartesian.NaN;
        long num = startpane(/*out*/ cartesian, /*out*/ cartesian2, /*out*/ cartesian3, x, y, z, name);
        len = 2;
        if (name != null)
        {
            if (num < 8L)
            {
                name[0] = 'X';
                name[1] = 'X';
                name[2] = '\0';
            }
            num2 = num;
            while (depth-- > 0)
            {
                cartesian6.SetMiddlePoint(cartesian[0], cartesian2[0], true);
                naN.SetMiddlePoint(cartesian2[0], cartesian3[0], true);
                cartesian5.SetMiddlePoint(cartesian3[0], cartesian[0], true);
                if (!Contains(p, cartesian[0], cartesian6, cartesian5))
                {
                    if (!Contains(p, cartesian2[0], naN, cartesian6))
                    {
                        if (!Contains(p, cartesian3[0], cartesian5, naN))
                        {
                            if (!Contains(p, naN, cartesian5, cartesian6))
                            {
                                throw new RuntimeException("Panic in Cartesian2hid");
                            }
                            name[len++] = '3';
                            cartesian[0].Set(naN, false);
                            cartesian2[0].Set(cartesian5, false);
                            cartesian3[0].Set(cartesian6, false);
                        }
                        else
                        {
                            name[len++] = '2';
                            cartesian[0].Set(cartesian3[0], false);
                            cartesian2[0].Set(cartesian5, false);
                            cartesian3[0].Set(naN, false);
                        }
                        continue;
                    }
                    name[len++] = '1';
                    cartesian[0].Set(cartesian2[0], false);
                    cartesian2[0].Set(naN, false);
                    cartesian3[0].Set(cartesian6, false);
                }
                else
                {
                    name[len++] = '0';
                    cartesian2[0].Set(cartesian6, false);
                    cartesian3[0].Set(cartesian5, false);
                    continue;
                }
            }
            name[len] = '\0';
            return num2;
        }
        if (num < 8L)
        {
            return 1L;
        }
        num2 = num;
        while (depth-- > 0)
        {
            num2 = num2 << 2;
            cartesian6.SetMiddlePoint(cartesian[0], cartesian2[0], true);
            naN.SetMiddlePoint(cartesian2[0], cartesian3[0], true);
            cartesian5.SetMiddlePoint(cartesian3[0], cartesian[0], true);
            if (!Contains(p, cartesian[0], cartesian6, cartesian5))
            {
                if (!Contains(p, cartesian2[0], naN, cartesian6))
                {
                    if (!Contains(p, cartesian3[0], cartesian5, naN))
                    {
                        if (!Contains(p, naN, cartesian5, cartesian6))
                        {
                            throw new RuntimeException("Panic in Cartesian2hid");
                        }
                        num2 |= 3L;
                        cartesian[0].Set(naN, false);
                        cartesian2[0].Set(cartesian5, false);
                        cartesian3[0].Set(cartesian6, false);
                    }
                    else
                    {
                        num2 |= 2L;
                        cartesian[0].Set(cartesian3[0], false);
                        cartesian2[0].Set(cartesian5, false);
                        cartesian3[0].Set(naN, false);
                    }
                    continue;
                }
                num2 |= 1L;
                cartesian[0].Set(cartesian2[0], false);
                cartesian2[0].Set(naN, false);
                cartesian3[0].Set(cartesian6, false);
            }
            else
            {
                cartesian2[0].Set(cartesian6, false);
                cartesian3[0].Set(cartesian5, false);
                continue;
            }
        }
        return num2;
    }



}
