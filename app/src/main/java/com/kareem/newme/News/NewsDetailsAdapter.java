package com.kareem.newme.News;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kareem.newme.Connections.VolleyRequest;
import com.kareem.newme.Constants;
import com.kareem.newme.Model.Comment;
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
    private Boolean isLiked = false;
    private int likeId;
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
            else v = LayoutInflater.from(context).inflate(R.layout.comments_list_items,parent,false);
        }
        if ((position == 0 && v.findViewById(R.id.news_details_textView_title) == null) )
        {
            v = LayoutInflater.from(context).inflate(R.layout.news_details_list_item,parent,false);
        }
        else if (v.findViewById(R.id.comments_list_content) == null) v = LayoutInflater.from(context).inflate(R.layout.comments_list_items,parent,false);
        if (position == 0) setNewsLayout(v);
        else setCommentsLayout(v,position);
        return v;
    }

    private void setCommentsLayout(View v, int position) {
        EditText content = (EditText) v.findViewById(R.id.comments_list_content);
        TextView name = (TextView) v.findViewById(R.id.comments_list_name);
        Comment comment = news.getComments().get(position -1);
        content.setText(comment.getContent());
        name.setText(comment.getUserName());
        //TODO
        // 1- set listeners
        //idk just it seems i forgot something :)
    }

    private void setNewsLayout(View view) {
        TextView newsTitle = (TextView)view.findViewById(R.id.news_details_textView_title);
        TextView newsDetails = (TextView)view.findViewById(R.id.news_details_textView_content);
        ImageView imageView = (ImageView) view.findViewById(R.id.news_details_image_view);
        // Set their text
        newsTitle.setText(news.getTitle());
        newsDetails.setText(news.getContent());
        Picasso.with(context).load(news.getImage_url()).error(R.mipmap.default_image_news).into(imageView);
        setNewsButtonsLayoutAndListeners(view);
    }

    private void setNewsButtonsLayoutAndListeners(View view)
    {
        //like button
        //edit
        //delete
        ImageView likeButton = (ImageView) view.findViewById(R.id.news_details_image_view_like);

        if (RunTimeItems.loggedUser != null) {
            isLiked = false;
            for (int i = 0; i < news.getLikes().size(); i++) {
                Like like = news.getLikes().get(i);
                if (RunTimeItems.loggedUser.getId().equals(like.getUserId().toString())) {
                    isLiked = true;
                    likeId = i;
                    break;
                }

            }
        }
        else likeButton.setVisibility(View.GONE);
        if (isLiked)
            likeButton.setImageResource(R.drawable.like_active);
        else likeButton.setImageResource(R.drawable.like);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLikeRequest();
            }
        });
        //delete button
        ImageView delete_button = (ImageView) view.findViewById(R.id.news_details_image_view_delete);
        setDeleteButton(delete_button);

        //edit button
        ImageView editButton = (ImageView) view.findViewById(R.id.news_details_image_view_edit);
        setEditButton(editButton);
    }

    private void sendLikeRequest(){
        Map<String , String> stringMap = new HashMap<>();
        if (isLiked) stringMap.put("req", "dislikeNew");
        else
        stringMap.put("req", "likeNew");
        stringMap.put("newId", newsId);
        //TODO fix this
        if (isLiked) stringMap.put("likeId", String.valueOf(likeId));
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

    private void setDeleteButton(View deleteButton){
        if (RunTimeItems.loggedUser != null && RunTimeItems.loggedUser.getUserType().equals(Constants.ADMIN_TYPE))
            deleteButton.setOnClickListener(new View.OnClickListener() {
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
        else deleteButton.setVisibility(View.GONE);
    }

    private void setEditButton(View editButton)
    {
        if (RunTimeItems.loggedUser != null && RunTimeItems.loggedUser.getUserType().equals(Constants.ADMIN_TYPE))
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(context,NewsEditor.class);
                    intent.putExtra(Constants.NEWS_DATA, new Gson().toJson(news));
                    intent.putExtra(Constants.NEWS_ID, newsId);
                    context.startActivity(intent);
                }
            });
        else editButton.setVisibility(View.GONE);
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
