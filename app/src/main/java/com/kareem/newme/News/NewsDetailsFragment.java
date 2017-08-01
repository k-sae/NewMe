package com.kareem.newme.News;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kareem.newme.Connections.PostConnector;
import com.kareem.newme.Constants;
import com.kareem.newme.Model.News;
import com.kareem.newme.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsDetailsFragment extends Fragment {

    private NewsDetailsAdapter newsDetailsAdapter;
    public NewsDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String jsonNews = getActivity().getIntent().getStringExtra(Constants.NEWS_DATA);
        View view = inflater.inflate(R.layout.fragment_news_details, container, false);
        Log.e( "onCreateView: ", getActivity().getIntent().getStringExtra(Constants.NEWS_ID) );
        Log.e( "onCreateView: ", getActivity().getIntent().getStringExtra(Constants.NEWS_DATA) );
        ListView listView = (ListView) view.findViewById(R.id.news_details_listView);
        setNewsDetailsAdapter(listView);
        return view;
    }
    private void setNewsDetailsAdapter(ListView listView)
    {
        newsDetailsAdapter = new NewsDetailsAdapter(getActivity());
        newsDetailsAdapter.setNews(new Gson().fromJson(getActivity().getIntent().getStringExtra(Constants.NEWS_DATA), News.class));
        newsDetailsAdapter.setNewsId(getActivity().getIntent().getStringExtra(Constants.NEWS_ID));
        FirebaseDatabase.getInstance().getReference("News-2").child(newsDetailsAdapter.getNewsId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                News news = dataSnapshot.getValue(News.class);
                if (news == null) getActivity().finish();
                else {
                    newsDetailsAdapter.setNews(news);
                    newsDetailsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setAdapter(newsDetailsAdapter);

    }
}
