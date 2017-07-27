package com.kareem.newme.News;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kareem.newme.Constants;
import com.kareem.newme.Model.News;
import com.kareem.newme.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsEditorFragment extends Fragment {
    private News news;
    private TextView titleTextView;
    private TextView contentTextView;
    private ImageView newsImageView;

    public NewsEditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO
        // check the Intent if an already existed news is passed
        String jsonNews = getActivity().getIntent().getStringExtra(Constants.NEWS_DATA);
        View view = inflater.inflate(R.layout.fragment_news_editor, container, false);
        setLayout(view);
        if(jsonNews != null) {
            news = new Gson().fromJson(jsonNews, News.class);
            //TODO
        }
        else news = new News();
        return view;
    }
    private void setLayout(View view)
    {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = contentTextView.getText().toString();
                String title =  titleTextView.getText().toString();
                if (content.equals("") || title.equals(""))
                Snackbar.make(view, "please fill all text fields", Snackbar.LENGTH_LONG).show();
                else{
                    //TODO image check
                    news.setTitle(title);
                    news.setContent(content);
                }
            }
        });
        titleTextView = (TextView) view.findViewById(R.id.news_title_edit_text);
        contentTextView = (TextView) view.findViewById(R.id.news_content_edit_text);
        newsImageView = (ImageView) view.findViewById(R.id.news_edit_imageView);
        newsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream profileImageStream = getActivity().getContentResolver().openInputStream(data.getData());
                assert profileImageStream != null;
                byte[] profileImageByteArray = new byte[profileImageStream.available()];
                //noinspection ResultOfMethodCallIgnored
                profileImageStream.read(profileImageByteArray);
                newsImageView.setImageBitmap(BitmapFactory.decodeByteArray(profileImageByteArray,0,profileImageByteArray.length));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
