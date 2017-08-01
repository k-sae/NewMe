package com.kareem.newme.News;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kareem.newme.Connections.VolleyRequest;
import com.kareem.newme.Constants;
import com.kareem.newme.Model.Like;
import com.kareem.newme.Model.News;
import com.kareem.newme.R;
import com.kareem.newme.RunTimeItems;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kareem on 7/31/17.
 */

public class NewsDetailsAdapter extends BaseAdapter {
    private News news;
    private Context context;
    private String newsId;
    Boolean isLiked = false;
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
        ImageView imageView = (ImageView) v.findViewById(R.id.news_details_image_view);
        // Set their text
        newsTitle.setText(news.getTitle());
        newsDetails.setText(news.getContent());
        Picasso.with(context).load(news.getImage_url()).error(R.mipmap.default_image_news).into(imageView);
        setNewsButtonsLayoutAndListeners(v);
        return v;
    }
    private void setNewsButtonsLayoutAndListeners(View view)
    {
        //like button
        //edit
        //delete
        ImageView imageView = (ImageView) view.findViewById(R.id.news_details_image_view_like);

        if (RunTimeItems.loggedUser != null)
        for (Like like: news.getLikes()
             ) {
            if (RunTimeItems.loggedUser.getId().equals(like.getUserId().toString()))
            {
                isLiked = true;
            }
        }
        if (isLiked)
            imageView.setImageResource(R.drawable.like_active);
        else imageView.setImageResource(R.drawable.like);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                // change ImageLayout
                sendLikeRequest();
            }
        });
        ImageView delete_button = (ImageView) view.findViewById(R.id.news_details_image_view_delete);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String , String> stringMap = new HashMap<>();
                stringMap.put("req", "deleteNew");
                stringMap.put("newId", newsId);
                VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,stringMap,context) {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                    @Override
                    public void onResponse(String response) {
                    }
                };
                volleyRequest.start();
            }
        });
    }

    private void sendLikeRequest()
    {
        Map<String , String> stringMap = new HashMap<>();
        if (isLiked) stringMap.put("req", "dislikeNew");
        else
        stringMap.put("req", "likeNew");
        stringMap.put("newId", newsId);
        //TODO fix this
        if (isLiked) stringMap.put("likeId", "dislikeNew");
        else
            stringMap.put("like", new Gson().toJson(new Like(Integer.valueOf(RunTimeItems.loggedUser.getId()))));
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,stringMap,context) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(String response) {

            }
        };
        volleyRequest.start();
    }


    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }
}
