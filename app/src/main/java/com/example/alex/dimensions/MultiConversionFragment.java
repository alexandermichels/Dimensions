package com.example.alex.dimensions;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class MultiConversionFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private DimensionAdapter mDimensionAdapter;
    private static final String DIM = "DIM";
    private String dimension;
    private static final String VAL = "VAL";
    private double value;
    private Spinner spinner_1;
    private ArrayList<String> sameDimList;
    private ArrayAdapter<String> mStringArrayAdapter1;
    private EditText mEditText;
    private String spinner_1_choice;
    private String edit_text_string;

    private static final String SPINNER_1_POS = "position1";
    private static final String EDIT_TEXT_FIELD = "input";

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SPINNER_1_POS, spinner_1_choice);
        savedInstanceState.putString(EDIT_TEXT_FIELD, mEditText.getText().toString());
    }

    public static MultiConversionFragment newInstance(Context c, String dimension, double value)
    {
        MultiConversionFragment result = new MultiConversionFragment();
        Bundle b = new Bundle();
        b.putString(DIM, dimension);
        b.putDouble(VAL, value);
        result.setArguments(b);
        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        dimension = b.getString(DIM);
        value = b.getDouble(VAL);

        if (savedInstanceState != null)
        {
            super.onCreate(savedInstanceState);
            spinner_1_choice = savedInstanceState.getString(SPINNER_1_POS);
            edit_text_string = savedInstanceState.getString(EDIT_TEXT_FIELD);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.multi_conversion_layout, container, false);

        sameDimList = Database.getInstance().getDimensionsOfSameType(dimension);

        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        spinner_1 = v.findViewById(R.id.spinner_1);
        spinner_1 = v.findViewById(R.id.spinner_1);
        spinner_1.setOnItemSelectedListener(new MultiConversionFragment.Spinner1Listener());
        mStringArrayAdapter1 = new ArrayAdapter<>(this.getContext(), R.layout.spinner_textview, Database.getInstance().getDimensionList(true));
        mStringArrayAdapter1.setDropDownViewResource(R.layout.spinner_textview);
        spinner_1.setAdapter(mStringArrayAdapter1);
        setSpinner1(dimension);

        mEditText = v.findViewById(R.id.number_input);
        if (!(edit_text_string == null || edit_text_string.equals("")))
        {
            mEditText.setText(edit_text_string);
        }
        else if (value != 0)
        {
            String s = value+"";
            mEditText.setText(s);
        }

        mEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s)
            {
                try
                {
                    value = Double.parseDouble(mEditText.getText().toString());
                }
                catch(Exception e)
                {
                    value = 0;
                }
                updateUI();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        updateUI();
        return v;
    }

    class DimensionHolder extends RecyclerView.ViewHolder
    {
        private TextView mTextView;
        private int mIndex;

        DimensionHolder (LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.spinner_textview, parent, false));
            mTextView = itemView.findViewById(R.id.webpage);
        }

        void bind(int index)
        {
            mIndex = index;
            double d = Convert.value(value).from(dimension).to(sameDimList.get(mIndex));
            String s = d + " " + sameDimList.get(mIndex);
            mTextView.setText(s);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public class DimensionAdapter extends  RecyclerView.Adapter<DimensionHolder>
    {

        @Override
        public DimensionHolder onCreateViewHolder (ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new DimensionHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder (DimensionHolder holder, int position)
        {
            holder.bind(position);
        }

        @Override
        public int getItemCount ()
        {
            return sameDimList.size();
        }
    }

    private void updateUI()
    {
        sameDimList = Database.getInstance().getDimensionsOfSameType(dimension);
        if (mDimensionAdapter == null) {
            mDimensionAdapter = new DimensionAdapter();
            mRecyclerView.setAdapter(mDimensionAdapter);
        } else {
            mDimensionAdapter.notifyDataSetChanged();
        }
    }

    public class Spinner1Listener implements AdapterView.OnItemSelectedListener
    {double input;
        @Override
        public void onItemSelected (AdapterView<?> adapterView, View view, int i, long l)
        {
            dimension = (String)adapterView.getItemAtPosition(i);
            Log.d("MultConversionFragment", "onItemSelected");
            updateUI();
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
}
