package com.example.alex.dimensions;

/**
 * Created by alex on 3/7/18.
 */

public class Value
{
    double val;

    public Dimension from(String unit)
    {
        return Dimension.makeDimension(unit, this.val);
    }

    public Value(double v)
    {
        val = v;
    }

    public double getVal ()
    {
        return val;
    }

    public void setVal (double val)
    {
        this.val = val;
    }
}
