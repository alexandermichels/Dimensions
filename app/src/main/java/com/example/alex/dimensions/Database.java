package com.example.alex.dimensions;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alex on 2/7/18.
 */

public class Database implements Serializable
{
    private static Database instance;
    private static final String DATABASE_FILE = "com_example_alex_dimensions_database";
    private HashMap<String, Integer> numDims;
    private ArrayList<String> dimensions;
    private HashMap<String, Double> conversionRates;
    private HashMap<String, Double> metrics;
    private HashMap<String, Boolean> isMetric;
    private static String [] metricMods = { "p", "n", "u", "m", "c", "d", "", "da", "h", "k", "M", "G" };
    private static double [] metricMulti = { 0.000000000001, 0.000000001, 0.000001, 0.001, 0.01, 0.1, 1, 10, 100, 1000, 1000000, 1000000000 };

    private Database()
    {
        dimensions = new ArrayList<>();
        numDims = new HashMap<>();
        conversionRates = new HashMap<>();
        isMetric = new HashMap<>();

        metrics = new HashMap<>();
        for (int i = 0;  i < metricMods.length; i++)
        {
            metrics.put(metricMods[i], metricMulti[i]);
        }


        instance = this;
    }

    public void populateDatabase()
    {
        String [] dims = { "m", "in", "ft", "yd", "mi", "ly", "s", "min", "hr", "day", "wk", "fortnight", "j", "eV", "btu", "boe", "w", "hp", "j/s", "g", "slug", "N", "lb", "m^3", "li", "oz", "gallon", "pint", "hoppus", "ft^2", "m^2", "acre", "hectacre" };
        boolean [] m_or_no = {true, false, false, false, false, false, true, false, false, false, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, false, false, true, false, false };
        String [] ds = { "Length", "Time", "Energy", "Power", "Mass", "Force", "Volume", "Area"};
        int [] nDims = { 6, 12, 16, 19, 21, 24, 29, 34 };
        double [] rates = { 1, 0.0254, 0.3048, 0.9144, 1609.34, 0.0000000000000001057, 1, 0.016667, 0.000277778, 0.000011574, 0.0000016534, 0.00000082672, 1, 6242000000000000000., 0.000947817, 0.00000000016456, 1, 0.00134102, 1, 1, 0.0000685218, 1, 4.44822, 3.6, 1, 1000, 264.172, 2113.38, 1.8027, 1, 0.092903, 0.000022957, 0.092903 };
        //central conversion units per unit


        for (int i = 0; i < dims.length; i++)
        {
            dimensions.add(dims[i]);
            conversionRates.put(dims[i], rates[i]);
            isMetric.put(dims[i], m_or_no[i]);
        }

        for (int i = 0; i < ds.length; i++)
        {
            numDims.put(ds[i], nDims[i]);
        }
    }

    public static Database getInstance ()
    {
        if (instance == null)
        {
            instance = new Database();
            instance.populateDatabase();
        }
        return instance;
    }

    public static void load(File f) throws IOException, ClassNotFoundException
    {
        FileInputStream fileInputStream = new FileInputStream(f);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        instance = (Database) objectInputStream.readObject();

        objectInputStream.close();
        fileInputStream.close();
    }


    public void save(File f) throws IOException
    {
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(this);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    public static String getDatabaseFile()
    {
        return DATABASE_FILE;
    }

    public static HashMap<String, Integer> getNumDims ()
    {
        return getInstance().numDims;
    }

    public static int getNumDims(String key)
    {
        return getInstance().numDims.get(key);
    }

    public static ArrayList<String> getDimensions ()
    {
        return getInstance().dimensions;
    }

    public static int getDimensionsSize()
    {
        return getInstance().dimensions.size();
    }

    public static String getDimension(int i)
    {
        return getDimensions().get(i);
    }

    public ArrayList<String> getDimensionList(boolean metrics)
    {
        if (!metrics)
        {
            return getDimensions();
        }
        else
        {
            ArrayList<String> list = new ArrayList<>(dimensions.size());
            for(int i = 0; i < dimensions.size(); i++)
            {
                if (isMetric(getDimension(i)))
                {
                    for (int j = 0; j < metricMods.length; j++)
                    {
                        list.add(metricMods[j] + getDimension(i));
                    }
                }
                else
                {
                    list.add(getDimension(i));
                }
            }

            return list;
        }
    }

    public static HashMap<String, Double> getConversionRates ()
    {
        return getInstance().conversionRates;
    }

    public static double getConversionRate(String key)
    {
        if (getConversionRates().get(key) != null)
        {
            return getConversionRates().get(key);
        }
        else if (getConversionRates().get(key.substring(1)) != null)
        {
            return getConversionRates().get(key.substring(1)) * getMetric(key.substring(0,1));
        }
        else if (getConversionRates().get(key.substring(2)) != null)
        {
            return getConversionRates().get(key.substring(2)) * getMetric(key.substring(0,2));
        }
        return 1;
    }

    public static HashMap<String, Double> getMetrics ()
    {
        return getInstance().metrics;
    }

    public static double getMetric(String key)
    {
        return getMetrics().get(key);
    }

    public static boolean isMetric(String key)
    {
        if (Database.getInstance().isMetric.get(key) != null)
        {
            return Database.getInstance().isMetric.get(key);
        }
        else
        {
            for (int i = 1; i < 3; i++)
            {
                if (Database.getInstance().isMetric.get(key.substring(i)) != null)
                {
                    return Database.getInstance().isMetric.get(key.substring(i));
                }
            }
            return false;
        }
    }

    public ArrayList<String> getDimensionsOfSameType(String key)
    {
        Dimension key_dimension = Dimension.makeDimension(key, 0);
        String type = key_dimension.getType();
        ArrayList<String> list = new ArrayList<>();
        for (int h = 0; h < dimensions.size(); h++)
        {
            Dimension tmp = Dimension.makeDimension(getDimension(h),0);
            Log.d("Database: ",tmp.getType() + "  is of the same type as  " + type + " : " + tmp.getType().equalsIgnoreCase(type) + "");
            if (tmp.getType().equalsIgnoreCase(type))
            {
                    if (isMetric(getDimension(h)))
                    {
                        for (int j = 0; j < metricMods.length; j++)
                        {
                            list.add(metricMods[j] + getDimension(h));
                            Log.d("Database: ", "added " + metricMods[j] + getDimension(h) + " to getDimensionsOfSameType(" + key + ")");
                        }
                    } else
                    {
                        list.add(getDimension(h));
                        Log.d("Database: ", "added "  + getDimension(h) + " to getDimensionsOfSameType(" + key + ")");
                    }

            }
        }
        return list;
    }
}
