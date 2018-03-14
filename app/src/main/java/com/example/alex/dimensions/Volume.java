package com.example.alex.dimensions;

/**
 * Created by alex on 3/8/18.
 */

public class Volume extends Dimension
{
    static String centralConversionUnit = "m^3";

    public Volume(String u, double m)
    {
        super(u,m);
    }

    public String getType()
    {
        return "Volume";
    }
}
