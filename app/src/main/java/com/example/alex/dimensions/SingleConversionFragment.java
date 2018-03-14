package com.example.alex.dimensions;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;


public class SingleConversionFragment extends Fragment
{
    private static final String SPINNER_1_POS = "position1";
    private static final String SPINNER_2_POS = "position2";
    private static final String EDIT_TEXT_FIELD = "input";

    private Spinner spinner_1;
    private Spinner spinner_2;
    private ArrayAdapter<String> mStringArrayAdapter1;
    private ArrayAdapter<String> mStringArrayAdapter2;
    private EditText mEditText;
    private Button mButton;
    private String button_text;
    private String spinner_1_choice;
    private String spinner_2_choice;
    private String edit_text_string;
    private Button mAllButton;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SPINNER_1_POS, spinner_1_choice);
        savedInstanceState.putString(SPINNER_2_POS, spinner_2_choice);
        savedInstanceState.putString(EDIT_TEXT_FIELD, mEditText.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            super.onCreate(savedInstanceState);
            spinner_1_choice = savedInstanceState.getString(SPINNER_1_POS);
            spinner_2_choice = savedInstanceState.getString(SPINNER_2_POS);
            edit_text_string = savedInstanceState.getString(EDIT_TEXT_FIELD);
        }
    }

    public static SingleConversionFragment newInstance (Context c)
    {
        return new SingleConversionFragment();
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.conversion_layout, container, false);

        Database.getInstance();
        try
        {
            Database.load(new File(getContext().getFilesDir(), Database.getDatabaseFile()));
        }
        catch (Exception e)
        {
            Database.getInstance();
        }

        spinner_1 = v.findViewById(R.id.spinner_1);
        spinner_1.setOnItemSelectedListener(new Spinner1Listener());
        mStringArrayAdapter1 = new ArrayAdapter<>(this.getContext(), R.layout.spinner_textview, Database.getInstance().getDimensionList(true));
        mStringArrayAdapter1.setDropDownViewResource(R.layout.spinner_textview);
        spinner_1.setAdapter(mStringArrayAdapter1);

        spinner_2 = v.findViewById(R.id.spinner_2);
        spinner_2.setOnItemSelectedListener(new Spinner2Listener());
        mStringArrayAdapter2 = new ArrayAdapter<>(this.getContext(), R.layout.spinner_textview, Database.getInstance().getDimensionList(true));
        mStringArrayAdapter2.setDropDownViewResource(R.layout.spinner_textview);
        spinner_2.setAdapter(mStringArrayAdapter2);

        mButton = v.findViewById(R.id.number_output);
        try
        {
            button_text = Convert.value(Double.parseDouble(edit_text_string)).from(spinner_1_choice).to(Database.getInstance().getDimensionsOfSameType(spinner_1_choice).get(0))+"";
        }
        catch (Exception e)
        {
            button_text = "0";
        }
        mButton.setText(button_text);

        mEditText = v.findViewById(R.id.number_input);
        if (!(edit_text_string == null || edit_text_string.equals("")))
        {
            mEditText.setText(edit_text_string);
        }

        mEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s)
            {
                try
                {
                    button_text = Convert.value(Double.parseDouble(edit_text_string)).from(spinner_1_choice).to(Database.getInstance().getDimensionsOfSameType(spinner_1_choice).get(0))+"";
                }
                catch (Exception e)
                {
                    button_text = "0";
                }
                mButton.setText(button_text);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        mAllButton = v.findViewById(R.id.all_button);
        mAllButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view)
            {
                double input;
                try
                {
                    input = Double.parseDouble(mEditText.getText().toString());
                }
                catch (Exception e)
                {
                    input = 0;
                }
                SingleConversionActivity s = (SingleConversionActivity)getActivity();
                Intent intent = MultiConversionActivity.newIntent(SingleConversionFragment.this.getContext(), spinner_1_choice, input);
                startActivity(intent);
            }
        });

        return v;
    }

    public class Spinner1Listener implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected (AdapterView<?> adapterView, View view, int i, long l)
        {   spinner_1_choice = (String)adapterView.getItemAtPosition(i);
            spinner_2_choice = "";
            mStringArrayAdapter2.clear();
            mStringArrayAdapter2.addAll(Database.getInstance().getDimensionsOfSameType(spinner_1_choice));
            mStringArrayAdapter2.notifyDataSetChanged();
            Log.d("onItemSelected", "adapter1");
            Resources res = getResources();
            try
            {
                if (!(edit_text_string == null || edit_text_string.equals("")))
                {
                    button_text = Convert.value(Double.parseDouble(mEditText.getText().toString())).from(spinner_1_choice).to(Database.getInstance().getDimensionsOfSameType(spinner_1_choice).get(0)) + "";
                }
                else
                {
                    button_text = Convert.value(Double.parseDouble(mEditText.getText().toString())).from(spinner_1_choice).to(spinner_2_choice) + "";
                }
            }
            catch (Exception e)
            {
                button_text = "0";
            }
            mButton.setText(button_text);
        }

        @Override
        public void onNothingSelected (AdapterView<?> adapterView)
        {

        }
    }

    public class Spinner2Listener implements AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected (AdapterView<?> adapterView, View view, int i, long l)
        {
            spinner_2_choice = (String)adapterView.getItemAtPosition(i);
            Log.d("onItemSelected", "adapter2");
            Resources res = getResources();
            try
            {
                button_text = Convert.value(Double.parseDouble(mEditText.getText().toString())).from(spinner_1_choice).to(spinner_2_choice) + "";
            }
            catch (Exception e)
            {
                button_text = "0";
            }
            mButton.setText(button_text);
        }

        @Override
        public void onNothingSelected (AdapterView<?> adapterView)
        {

        }
    }

    void setSpinner1(String value)
    {
        if (value != null) {
            int spinnerPosition = mStringArrayAdapter1.getPosition(value);
            spinner_1.setSelection(spinnerPosition);
        }
    }

    void setSpinner2(String value)
    {
        if (value != null) {
            int spinnerPosition = mStringArrayAdapter2.getPosition(value);
            spinner_2.setSelection(spinnerPosition);
        }
    }
}
