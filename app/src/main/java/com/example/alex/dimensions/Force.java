package com.example.alex.dimensions;

/**
 * Created by alex on 3/8/18.
 */

public class Force extends Dimension
{
    static String centralConversionUnit = "N";

    public Force(String u, double m)
    {
        super(u,m);
    }

    public String getType()
    {
        return "Force";
    }
}
