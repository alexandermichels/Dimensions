package com.example.alex.dimensions;

import android.provider.ContactsContract;


public class Dimension
{
    private String unit;
    private double magnitude;
    private static String centralConversionUnit = "none";
    private static String [] metricMods = { "p", "n", "u", "m", "c", "d", "da", "h", "k", "M", "G" };
    private static double [] metricMulti = { 0.000000000001, 0.000000001, 0.000001, 0.001, 0.01, 0.1, 10, 100, 1000, 1000000, 1000000000 };

    static Dimension makeDimension(String u, double m)
    {
        for (int i = 0; i < Database.getDimensionsSize(); i++)
        {
            if (Database.getDimension(i).equalsIgnoreCase(u))
            {
                if (i < Database.getNumDims("Length"))
                {
                    return new Length(u,m);
                }
                else if (i < Database.getNumDims("Time"))
                {
                    return new Time(u,m);
                }
                else if (i < Database.getNumDims("Energy"))
                {
                    return new Energy(u,m);
                }
                else if (i < Database.getNumDims("Power"))
                {
                    return new Power(u,m);
                }
                else if (i < Database.getNumDims("Mass"))
                {
                    return new Mass(u,m);
                }
                else if (i < Database.getNumDims("Force"))
                {
                    return new Force(u,m);
                }
                else if (i < Database.getNumDims("Volume"))
                {
                    return new Volume(u,m);
                }
                else if (i < Database.getNumDims("Area"))
                {
                    return new Area(u,m);
                }
            }
        }

        String metricPrefix2 = u.substring(0,2);
        if (metricPrefix2.equalsIgnoreCase("da"))
        {
            Dimension d = makeDimension(u.substring(2),m*10);
            if (d instanceof Area)
            {
                d.setMagnitude(d.getMagnitude()*10);
            }
            else if (d instanceof Volume)
            {
                d.setMagnitude(d.getMagnitude()*100);
            }
            return d;
        }

        String metricPrefix1 = u.substring(0,1);
        for (int i = 0; i < metricMods.length; i++)
        {
            if (metricPrefix1.equalsIgnoreCase(metricMods[i]))
            {
                Dimension d = makeDimension(u.substring(1),m*metricMulti[i]);
                if (d instanceof Area)
                {
                    d.setMagnitude(d.getMagnitude()*metricMulti[i]);
                }
                else if (d instanceof Volume)
                {
                    d.setMagnitude(d.getMagnitude()*metricMulti[i]*metricMulti[i]);
                }
                return d;
            }

        }

        return new Dimension(u,m);
    }

    public Dimension(String u, double m)
    {
        unit = u;
        magnitude = m;
    }

    public double to(String otherDim) throws IncompatibleUnits
    {
        if (makeDimension(otherDim,0).getType().equalsIgnoreCase(this.getType()))
        {
            return (this.getMagnitude() * Database.getConversionRate(this.getUnit()) / Database.getConversionRate(otherDim));
        }
        else
        {
            throw new IncompatibleUnits(this.getUnit(), otherDim);
        }
    }

    String getUnit ()
    {
        return unit;
    }

    double getMagnitude ()
    {
        return magnitude;
    }

    private void setMagnitude (double magnitude)
    {
        this.magnitude = magnitude;
    }

    public static String getCentralConversionUnit ()
    {
        return centralConversionUnit;
    }

    public String getType()
    {
        return "";
    }

    class IncompatibleUnits extends NumberFormatException
    {
        IncompatibleUnits(String from, String to)
        {
            super("You cannot convert from " + from + " to " + to);
        }
    }
}
