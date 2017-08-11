package com.kareem.newme.News;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kareem.newme.Connections.PostConnector;
import com.kareem.newme.Connections.VolleyRequest;
import com.kareem.newme.Constants;
import com.kareem.newme.Model.Comment;
import com.kareem.newme.Model.News;
import com.kareem.newme.R;
import com.kareem.newme.RunTimeItems;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsDetailsFragment extends Fragment {

//    private NewsDetailsAdapter newsDetailsAdapter;
    private NewsDetailsRecyclerAdapter newsDetailsRecyclerAdapter;
    public NewsDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_details, container, false);
        Log.e( "onCreateView: ", getActivity().getIntent().getStringExtra(Constants.NEWS_ID) );
        Log.e( "onCreateView: ", getActivity().getIntent().getStringExtra(Constants.NEWS_DATA) );
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.news_details_listView);
        setNewsDetailsAdapter(listView);
        if (RunTimeItems.loggedUser == null) view.findViewById(R.id.comments_writer_holder).setVisibility(View.GONE);
        View send_button = view.findViewById(R.id.send_button);
        final EditText comment_editText = (EditText) view.findViewById(R.id.commentWriter_EditText);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!comment_editText.getText().toString().equals(""))
                sendComment(comment_editText.getText().toString());
                comment_editText.setText("");
            }
        });
        return view;
    }
    private void sendComment(String text)
    {
        Comment comment = new Comment();
        comment.setContent(text);
        comment.setUserId(Integer.valueOf(RunTimeItems.loggedUser.getId()));
        comment.setUserName(RunTimeItems.loggedUser.getName());
        Map<String , String> stringMap = new HashMap<>();
        stringMap.put("req", "addComment");
        stringMap.put("comment", new Gson().toJson(comment));
        stringMap.put("newId", getActivity().getIntent().getStringExtra(Constants.NEWS_ID));
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,stringMap,getActivity()) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(String response) {

            }
        };
        volleyRequest.start();
    }
    private void setNewsDetailsAdapter(RecyclerView listView)
    {
        newsDetailsRecyclerAdapter = new NewsDetailsRecyclerAdapter(getActivity());
        newsDetailsRecyclerAdapter.setNews(new Gson().fromJson(getActivity().getIntent().getStringExtra(Constants.NEWS_DATA), News.class));
        newsDetailsRecyclerAdapter.setNewsId(getActivity().getIntent().getStringExtra(Constants.NEWS_ID));
        FirebaseDatabase.getInstance().getReference("News-2").child(newsDetailsRecyclerAdapter.getNewsId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                News news = dataSnapshot.getValue(News.class);
                if (news == null) NewsDetailsFragment.this.getActivity().finish();
                else {
                    newsDetailsRecyclerAdapter.setNews(news);
                    newsDetailsRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setAdapter(newsDetailsRecyclerAdapter);

    }
}
