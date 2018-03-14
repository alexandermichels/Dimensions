package com.example.alex.dimensions;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SingleConversionActivity extends SingleFragmentActivity
{

    @Override
    protected Fragment createFragment ()
    {
        return SingleConversionFragment.newInstance(this);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

}
