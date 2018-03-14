package com.example.alex.dimensions;

/**
 * Created by alex on 3/7/18.
 */

public class Length extends Dimension
{
    static String centralConversionUnit = "m";

    public Length (String u, double m)
    {
        super(u,m);
    }

    public String getType()
    {
        return "Length";
    }
}
