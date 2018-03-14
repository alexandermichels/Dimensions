package com.example.alex.dimensions;

/**
 * Created by alex on 3/7/18.
 */

public class Time extends Dimension
{
    static String centralConversionUnit = "s";

    public Time(String u, double m)
    {
        super(u,m);
    }

    public String getType()
    {
        return "Time";
    }
}
