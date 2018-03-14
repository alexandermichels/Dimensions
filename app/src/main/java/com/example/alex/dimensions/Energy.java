package com.example.alex.dimensions;

/**
 * Created by alex on 3/8/18.
 */

public class Energy extends Dimension
{
    static String centralConversionUnit = "j";

    public Energy(String u, double m)
    {
        super(u,m);
    }

    public String getType()
    {
        return "Energy";
    }
}
