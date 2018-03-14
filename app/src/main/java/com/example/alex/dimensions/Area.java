package com.example.alex.dimensions;

/**
 * Created by alex on 3/8/18.
 */

public class Area extends Dimension
{
    static String centralConversionUnit = "ft^2";

    public Area(String u, double m)
    {
        super(u,m);
    }

    public String getType()
    {
        return "Area";
    }
}
