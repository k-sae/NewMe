package com.kareem.newme.News;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kareem.newme.Connections.VolleyRequest;
import com.kareem.newme.Constants;
import com.kareem.newme.Model.News;
import com.kareem.newme.R;
import com.kareem.newme.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 * imageApi
 *  req = addNewPic
    newId = (capital i)
    image =
 */
//            params.put("req", "deleteNew");
//            params.put("newId", "-Kq5Br7peEFznzrrj2OY");
public class NewsEditorFragment extends Fragment {
    private News news;
    private TextView titleTextView;
    private TextView contentTextView;
    private ImageView newsImageView;
    private Bitmap bitmap;
    private boolean isEditing = false;
    //    private byte[] profileImageByteArray;
    public NewsEditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TODO
        // check the Intent if an already existed news is passed
        String jsonNews = getActivity().getIntent().getStringExtra(Constants.NEWS_DATA);
        View view = inflater.inflate(R.layout.fragment_news_editor, container, false);
        if(jsonNews != null) {
            news = new Gson().fromJson(jsonNews, News.class);
            //TODO
            isEditing = true;
        }
        else news = new News();
        setLayout(view);

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
                    uploadNews();
                }
            }
        });
        titleTextView = (TextView) view.findViewById(R.id.news_title_edit_text);
        contentTextView = (TextView) view.findViewById(R.id.news_content_edit_text);
        newsImageView = (ImageView) view.findViewById(R.id.news_edit_imageView);
        titleTextView.setText(news.getTitle());
        contentTextView.setText(news.getContent());
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

    private void updateNews() {
        final String id = getActivity().getIntent().getStringExtra(Constants.NEWS_DATA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                 bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                newsImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadNews()
    {
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Uploading...","Please wait...",false,false);
        loading.show();
        Map<String,String> params = new HashMap<>();
        if (isEditing) {
            params.put("req", "editNew");
            params.put("newId", getActivity().getIntent().getStringExtra(Constants.NEWS_ID));
            params.put("editedNew", new Gson().toJson(news));
        }
        else {
            params.put("req", "addNew");
            params.put("new", new Gson().toJson(news));
            if (bitmap != null)
                params.put("imagebase64", Utils.getStringImage(bitmap));
        }
            final VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL, params, getActivity()) {
                @Override
                public void onResponse(String response) {
                    loading.dismiss();
                    getActivity().finish();
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(),"Error occure will uploading please try again", Toast.LENGTH_LONG).show();
                    loading.setMessage("retrying");
                  start();
                }
            };
            volleyRequest.start();

    }
}
