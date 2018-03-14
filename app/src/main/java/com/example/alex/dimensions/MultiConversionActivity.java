package com.example.alex.dimensions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by alex on 3/13/18.
 */

public class MultiConversionActivity extends SingleFragmentActivity
{
    private static final String EXTRA_DIM = "com.example.alex.dimensions.dim";
    private static final String EXTRA_VAL = "com.example.alex.dim.val";
    private String dimension;
    private double value;


    public static Intent newIntent(Context packageContext, String dimension, double value)
    {
        Intent i = new Intent(packageContext, MultiConversionActivity.class);
        i.putExtra(EXTRA_DIM, dimension);
        i.putExtra(EXTRA_VAL, value);
        return i;
    }

    @Override
    protected Fragment createFragment()
    {
        return MultiConversionFragment.newInstance(this, getIntent().getStringExtra(EXTRA_DIM), getIntent().getDoubleExtra(EXTRA_VAL, value));
    }

}
