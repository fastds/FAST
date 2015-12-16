package edu.gzu.image;

import java.util.ArrayList;

public class Tuple {
        public boolean retval;
        public ArrayList<Line> segments;
        public int compareBy;
        public Tuple(boolean _r, /*ref*/ ArrayList<Line> _s, int _c)
        {
            retval = _r;
            segments = _s;
            compareBy = _c;
        }
}
