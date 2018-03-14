package com.example.alex.dimensions;

/**
 * Created by alex on 3/8/18.
 */

public class Mass extends Dimension
{
    static String centralConversionUnit = "g";

    public Mass(String u, double m)
    {
        super(u,m);
    }

    public String getType()
    {
        return "Mass";
    }
}
