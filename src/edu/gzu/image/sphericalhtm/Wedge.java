package edu.gzu.image.sphericalhtm;

import edu.gzu.image.sphericallib.Constant;

class Wedge {
	// Fields
    public static final double Epsilon = (3.0 * Constant.DoublePrecision);
    private double incoming;
    private double outgoing;

    // Methods
    Wedge()
    {
        this.incoming = 0.0;
        this.outgoing = 6.2831853071795862;
    }

    public Wedge(double outangle, double inangle)
    {
        this.incoming = outangle;
        if (inangle < outangle)
        {
            inangle += 6.2831853071795862;
        }
        this.outgoing = inangle;
    }

    public Markup Compare(Wedge other)
    {
        double incoming = this.incoming;
        double outgoing = this.outgoing;
        double num3 = other.incoming;
        double num4 = other.outgoing;
        double num5 = (incoming < num3) ? incoming : num3;
        incoming -= num5;
        outgoing -= num5;
        num3 -= num5;
        num4 -= num5;
        if (outgoing > 6.2831853071795862)
        {
            outgoing -= 6.2831853071795862;
            incoming -= 6.2831853071795862;
        }
        if ((incoming > (num3 + Epsilon)) && (incoming < (num4 - Epsilon)))
        {
            return Markup.Partial;
        }
        if ((outgoing > (num3 + Epsilon)) && (outgoing < (num4 - Epsilon)))
        {
            return Markup.Partial;
        }
        if ((incoming <= (num3 + Epsilon)) && (outgoing >= (num4 - Epsilon)))
        {
            return Markup.Inner;
        }
        return Markup.Undefined;
    }

    public  String toString()
    {
        return "We(in "+this.incoming+", out "+this.outgoing+")" ;
    }


}
