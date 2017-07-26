package com.kareem.newme.Fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kareem.newme.Model.News;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a
 * TODO: Replace the implementation with code for your data type.
 */
public class NewsAdapter extends BaseAdapter{

    private  List<News> mValues;

    public NewsAdapter() {
        mValues = new ArrayList<>();
    }


    @Override
    public int getCount() {
       return mValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public List<News> getmValues() {
        return mValues;
    }
}
