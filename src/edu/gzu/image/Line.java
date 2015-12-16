package edu.gzu.image;

import java.awt.Point;

public class Line {
	 public Point p1;
     public Point  p2;
     public int realY;
     public Line(Point  p1_, Point  p2_, int realY_)
     {
         p1 = p1_;
         p2 = p2_;
         realY = realY_;
     }
     public Line(Point  p1_, Point  p2_)
     {
         p1 = p1_;
         p2 = p2_;
         realY =(int) p2.getY();
     }
     public void shift(int x, int y)
     {
         p1.x += x;
         p2.x += x;
         p1.y += y;
         p2.y += y;
     }
}
