package edu.gzu.image;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Objects;

class polyFunk {

    static int OFF = 0;
    static int PIX = SdssConstants.getOutlinePix();
  
    static final  int X   = 11;		//random references
    static final  int Y   = 22;

    /**
     * given a span, will return an arraylist of line segments <br/>
     * that form a bounding polygon(s)
     * 通过参数span，返回一个元素为线段的arraylist
     * @param spans
     */
    public static ArrayList<Line> getPoly(String spans)
    {
        ArrayList<Line> hSegments = new ArrayList<Line>();
        ArrayList<Line> vSegments = new ArrayList<Line>();
        loadLines(spans, /*ref*/ hSegments, /*ref*/ vSegments);
        horizontalScan(/*ref*/ hSegments);
        vSegments.addAll(hSegments);
        return vSegments;
    }


    /**
     * reads lines into an ArrayList of lines
     * 读取lines到存放到列表中
     */
    private static void loadLines(String spans,
        /*ref*/ ArrayList<Line> hSegments, /*ref*/ ArrayList<Line> vSegments)
    {
        String[] span = spans.split(";");
        for (int x = 0; x < span.length; x++)
        {
            String[] pieces = span[x].split(",");
            int top_realY = Integer.parseInt(pieces[0]) * PIX;
            int bottom_realY = top_realY + PIX;
            Point tL = new Point(Integer.parseInt(pieces[1]) * PIX, top_realY);
            Point tR = new Point(Integer.parseInt(pieces[2]) * PIX - OFF, top_realY);
            Point bL = new Point(tL.x, bottom_realY - OFF);			//shift up
            Point bR = new Point(tR.x, bottom_realY - OFF);			//tR is shifted left
            Line top = new Line(tL, tR, top_realY);
            Line bottom = new Line(bL, bR, bottom_realY);
            Line left = new Line(tL, bL, -1);
            Line right = new Line(tR, bR, -1);
            hSegments.add(top);
            hSegments.add(bottom);
            vSegments.add(left);
            vSegments.add(right);
        }
    }


    /**
     *  groups same rows together, then calls cleanUpRow to remove overlap
     */
    private static void horizontalScan(/*ref*/ ArrayList<Line> segments)
    {
        ArrayList<Line> result = new ArrayList<Line>();
        ArrayList<Line> lines = new ArrayList<Line>();
        lineSort(/*ref*/ segments, Y);
        for (int a = 0; a < segments.size(); a++)
        {
            Line current = (Line)segments.get(a);
            lines.add(current);
            int currentRow = ((Line)segments.get(a)).realY;
            if (a + 1 >= segments.size())			//last element
            {
                cleanUpRow(/*ref*/ lines);
                result.addAll(lines);
                break;
            }
            else
            {
                int nextRow = ((Line)segments.get(a + 1)).realY;
                if (nextRow != currentRow || (a + 1) >= segments.size())
                {
                    cleanUpRow(/*ref*/ lines);
                    result.addAll(lines);
                    lines.clear();
                }
            }
        }
        segments = result;
    }

    
    /**
     * only cleans up items on same row
     * 仅仅用于对同一行的items进行清理
     */
    private static void cleanUpRow(/*ref*/ ArrayList<Line> lines)
    {
        if (lines.size() < 2) return;
        lineSort(/*ref*/ lines, X);					//leftmost line at index 0
        for (int a = 0; a < lines.size(); a++)
        {
            Line current = (Line)lines.get(a);
            for (int b = a; b < lines.size(); b++)
            {
                Line compare = (Line)lines.get(b);
                if (compare.equals(current))
                    continue;					//same line
                if (current.p2.getX() > compare.p1.getX())
                {
                    //Console.WriteLine("******");
                    //dumpSegments(lines);
                    int leftChunk = (int) (compare.p1.getX() - current.p1.getX());
                    int rightChunk = (int) (compare.p2.getX() - current.p2.getX());
                    //Console.WriteLine(current.p2.X+" "+compare.p1.X);
                    //Console.WriteLine("******");
                    lines.remove(current);		//take out old lines
                    lines.remove(compare);
                    if (leftChunk != 0)			//left piece
                    {
                        Point p1 = new Point((int)current.p1.getX(), (int)current.p1.getY());
                        Point p2 = new Point((int)current.p1.getX() + leftChunk,(int) current.p1.getY());
                        if (!equal(p1, p2)) lines.add(new Line(p1, p2));
                    }
                    if (rightChunk < 0)			//right piece of current
                    {
                        Point p1 = new Point((int)current.p2.getX() + rightChunk,(int) current.p1.getY());
                        Point p2 = new Point((int)current.p2.getX(),(int) current.p1.getY());
                        if (!Objects.equals(p1, p2)) lines.add(new Line(p1, p2));
                    }
                    if (rightChunk > 0)			//right piece of compare
                    {
                        Point p1 = new Point((int)current.p2.getX(),(int) compare.p1.getY());
                        Point p2 = new Point((int)current.p2.getX() + rightChunk, (int)compare.p1.getY());
                        if (!Objects.equals(p1, p2)) lines.add(new Line(p1, p2));
                    }
                    //Console.WriteLine("\t***");
                    //dumpSegments(lines);
                    cleanUpRow(/*ref*/ lines);
                    return;
                }
            }
        }
    }


    private static boolean equal(Point p1, Point p2)
    {
        if (p1.x == p2.x)
            return p1.y == p2.y;
        else
            return false;
    }


    // I don't know why unwinding the tail recursion makes it stop breaking
    // on every machine except Deoyani's desktop, where it works either way,
    // but it does. - RCE 1/10/2011
    private static boolean lineSort(/*ref*/ ArrayList<Line> segments, int compareBy)
    {
        Tuple ret = new Tuple(false, /*ref*/ segments, compareBy);
        do
        {
            ret = lineSort2(/*ref*/ ret.segments, ret.compareBy);
        } while (ret.retval != true);
        return ret.retval;
    }
    /**
     * sorts line segments, highest to lowest.  pass X or Y to specify axis
     * 将线段从高到低排序，通过指定X Y轴
     */
    private static Tuple lineSort2(/*ref*/ ArrayList<Line> segments, int compareBy)
    {
        boolean switched = false;
        if (compareBy == Y)
        {
            int last = (int) ((Line)segments.get(0)).p1.getY();
            for (int x = 1; x < segments.size(); x++)
            {
                int current = (int) ((Line)segments.get(x)).p1.getY();
                if (current < last)
                {
                    switchItem(/*ref*/ segments, x, x - 1);
                    switched = true;
                }
                last = current;
            }
        }
        else if (compareBy == X)
        {
            int last = (int) ((Line)segments.get(0)).p1.getX();
            for (int x = 1; x < segments.size(); x++)
            {
                int current = (int) ((Line)segments.get(x)).p1.getX();
                if (current < last)
                {
                    switchItem(/*ref*/ segments, x, x - 1);
                    switched = true;
                }
                last = current;
            }
        }
        if (switched)
            return new Tuple(false, /*ref*/ segments, compareBy);
        ArrayList<Line> vo = new ArrayList<Line>();
        return new Tuple(true, /*ref*/ vo, -1);
    }


    private static void switchItem(/*ref*/ ArrayList<Line> list, int i1, int i2)
    {
        Object temp = list.get(i1);
        list.set(i1,list.get(i2)) ;
        list.set(i2,(Line) temp);
    }


    //===============================================
    // Debug stuff
    //===============================================
    //For Debug.  Will return straight translation.
    public static ArrayList<Line> getAll(String spans)
    {
        ArrayList<Line> hSegments = new ArrayList<Line>();
        ArrayList<Line> vSegments = new ArrayList<Line>();
        loadLines(spans, /*ref*/ hSegments, /*ref*/ vSegments);
        vSegments.addAll(hSegments);
        return vSegments;
    }


    //For Debug. prints lists of line segments
    public static void dumpSegments(ArrayList<Line> list)
    {
        for (int x = 0; x < list.size(); x++)
        {
            Line line = (Line)list.get(x);
            System.out.println("(" + line.p1.getX() + "," + line.p1.getY()
                + "), (" + line.p2.getX() + "," + line.p2.getY() + ")");
        }
    }


//zoe    // For Debug. Draw the outline on an image
//    public static void drawSegments(ArrayList<Line> list)
//    {
//        Pen pen = new Pen(Color.GREEN, 1);
//        Bitmap bMap = new Bitmap(2000, 500);
//        Graphics g = Graphics.FromImage(bMap);
//        for (int x = 0; x < list.size(); x++)
//        {
//            g.DrawLine(pen, ((Line)list[x]).p1, ((Line)list[x]).p2);
//        }
//        FileStream fs = File.Create("h:\\out.bmp");
//        bMap.Save(fs, ImageFormat.Bmp);
//        fs.Close();
//    }


    /*
        public static void Main(){
        //String TEST = "53,489,491;54,489,492;55,488,492;56,488,492;57,488,492;58,489,491";
        String TEST = "70,415,416;71,412,419;72,412,420;73,411,422;74,410,423;75,405,406;75,409,423;76,404,423;77,404,423;78,403,424;79,403,424;80,404,424;81,404,424;82,404,424;83,403,424;84,403,423;85,404,423;86,403,423;87,403,423;88,402,424;89,403,423;90,403,423;91,403,423;92,402,422;93,402,422;94,403,421;95,404,421;96,404,421;97, 405,420;98,406,419;99,406,419;100,407,414;100,417,418;101,409,412;102,410,411";
        ArrayList al = getPoly(TEST);
        drawSegments(al);
        dumpSegments(al);
        }
    */
}
