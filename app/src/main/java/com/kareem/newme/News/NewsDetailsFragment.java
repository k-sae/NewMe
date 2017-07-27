package com.kareem.newme.News;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kareem.newme.Connections.PostConnector;
import com.kareem.newme.Constants;
import com.kareem.newme.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsDetailsFragment extends Fragment {

    public NewsDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String jsonNews = getActivity().getIntent().getStringExtra(Constants.NEWS_DATA);
        View view = inflater.inflate(R.layout.fragment_news_details, container, false);
        try {
            PostConnector postConnector = new PostConnector(new URL("http://drhanadi.com/newmemobile/classes/MobileApi.php")) {
                @Override
                public void onFinish(String data) {
                    Log.e("asd", "onFinish: "+ data);
                }
            };
            Map<String,String> params = new HashMap<>();
//            params.put("req","addNew");
//            params.put("new", jsonNews);
            params.put("req", "deleteNew");
            params.put("newId", "-Kq5Br7peEFznzrrj2OY");
            postConnector.setParams(params);
            postConnector.execute();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return view;
    }
}
