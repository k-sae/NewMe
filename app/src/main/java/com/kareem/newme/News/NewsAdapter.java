package com.kareem.newme.News;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.kareem.newme.Model.News;
import com.kareem.newme.R;
import com.squareup.picasso.Picasso;

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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View v, ViewGroup parent) {

        //TODO dont forget the image
        if (v ==null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.news_list_item,parent,false);
        }
        ImageView imageView = (ImageView) v.findViewById(R.id.news_list_item_imageView);

        TextView newsTitle = (TextView)v.findViewById(R.id.news_list_item_title);
        TextView newsDetails = (TextView)v.findViewById(R.id.news_list_item_details);
        TextView likesCount = (TextView) v.findViewById(R.id.likes_count_news_list);
        TextView commentsCount = (TextView) v.findViewById(R.id.comments_count_news_list);
        // Set their text
        News news = dataSnapshots.get(position).getValue(News.class);
        newsTitle.setText(news.getTitle());
        newsDetails.setText(news.getContent());
        likesCount.setText(news.getLikes().size() +"");
        commentsCount.setText(news.getComments().size() + "");
        if (news.getImage_url().contains("/")) {
            String filename = news.getImage_url().substring(news.getImage_url().lastIndexOf("/") + 1);
            Picasso.with(context).load("http://www.drhanadi.com/newmemobile/view/upload/thumb/" + filename)
                    .error(R.mipmap.default_image_news)
                    .resize(300,300).onlyScaleDown()
                    .into(imageView);
        }
        else Picasso.with(context)
                .load(R.mipmap.default_image_news)
                .resize(300,300).onlyScaleDown()
                .error(R.mipmap.default_image_news)
                .into(imageView);
        return v;
    }

    public ArrayList<DataSnapshot> getDataSnapshots() {
        return dataSnapshots;
    }
}
