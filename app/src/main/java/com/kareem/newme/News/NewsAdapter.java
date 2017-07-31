package com.kareem.newme.News;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.kareem.newme.Model.News;
import com.kareem.newme.R;

import java.util.ArrayList;

/**
 * Created by kareem on 7/31/17.
 */

public class NewsAdapter extends BaseAdapter {
    private ArrayList<DataSnapshot> dataSnapshots;
    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
        dataSnapshots = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return dataSnapshots.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSnapshots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        //TODO dont forget the image
        if (v ==null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.news_list_item,parent,false);
        }
        TextView newsTitle = (TextView)v.findViewById(R.id.news_list_item_title);
        TextView newsDetails = (TextView)v.findViewById(R.id.news_list_item_details);

        // Set their text
        News news = dataSnapshots.get(position).getValue(News.class);
        newsTitle.setText(news.getTitle());
        newsDetails.setText(news.getContent());
        return v;
    }

    public ArrayList<DataSnapshot> getDataSnapshots() {
        return dataSnapshots;
    }
}
