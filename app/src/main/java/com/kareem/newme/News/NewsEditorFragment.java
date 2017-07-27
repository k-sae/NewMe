package com.kareem.newme.News;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.kareem.newme.Constants;
import com.kareem.newme.Model.News;
import com.kareem.newme.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsEditorFragment extends Fragment {
    private News news;
    public NewsEditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO
        // check the Intent if an already existed news is passed
        String jsonNews = getActivity().getIntent().getStringExtra(Constants.NEWS_DATA);
        if(jsonNews != null)
           news = new Gson().fromJson(jsonNews, News.class);
        else news = new News();

        View view = inflater.inflate(R.layout.fragment_news_editor, container, false);
        setLayout(view);
        return view;
    }
    private void setLayout(View view)
    {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        view.findViewById(R)
    }
}
