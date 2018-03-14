package com.example.alex.dimensions;

/**
 * Created by alex on 3/8/18.
 */

public class Power extends Dimension
{
    static String centralConversionUnit = "w";

    public Power(String u, double m)
    {
        super(u,m);
    }

    public String getType()
    {
        return "Power";
    }
}
