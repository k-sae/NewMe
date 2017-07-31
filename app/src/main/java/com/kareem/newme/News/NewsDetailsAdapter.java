package com.kareem.newme.News;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kareem.newme.Model.News;
import com.kareem.newme.R;

import java.util.ArrayList;

/**
 * Created by kareem on 7/31/17.
 */

public class NewsDetailsAdapter extends BaseAdapter {
    private News news;
    private Context context;

    public NewsDetailsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return news.getComments().size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return position == 0? news : news.getComments().get(position-1) ;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        //TODO dont forget the image
        if (v ==null )
        {
            if (position == 0)
            v = LayoutInflater.from(context).inflate(R.layout.news_details_list_item,parent,false);
            //TODO
            else v = LayoutInflater.from(context).inflate(R.layout.news_details_list_item,parent,false);
        }
        if ((position == 0 && v.findViewById(R.id.news_details_textView_title) == null) )
        {
            v = LayoutInflater.from(context).inflate(R.layout.news_details_list_item,parent,false);
        }
        TextView newsTitle = (TextView)v.findViewById(R.id.news_details_textView_title);
        TextView newsDetails = (TextView)v.findViewById(R.id.news_details_textView_content);

        // Set their text
        newsTitle.setText(news.getTitle());
        newsDetails.setText(news.getContent());
        return v;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
