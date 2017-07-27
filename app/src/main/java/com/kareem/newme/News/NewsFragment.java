package com.kareem.newme.News;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.kareem.newme.BasicNotifier;
import com.kareem.newme.Constants;
import com.kareem.newme.DataSetListener;
import com.kareem.newme.Model.News;
import com.kareem.newme.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class NewsFragment extends Fragment implements DataSetListener {

    private BasicNotifier basicNotifier;
    private NewsAdapter newsAdapter;
    private FirebaseListAdapter<News> newsFirebaseListAdapter;
    private Gson parser;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // Set the adapter
        parser = new Gson();
        ListView listView = (ListView) view.findViewById(R.id.news_container_listView);
        populateNews(listView);
        newsAdapter = new NewsAdapter();
        basicNotifier = new BasicNotifier(this);
//        listView.setAdapter(newsAdapter);
        basicNotifier.execute(Constants.NEWS_URL);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        basicNotifier.close();
    }

    private void populateNews(ListView newsListView)
    {
        newsFirebaseListAdapter = new FirebaseListAdapter<News>(getActivity(), News.class,
                R.layout.news_list_item, FirebaseDatabase.getInstance().getReference("News-2")) {
            @Override
            protected void populateView(View v, News model, int position) {
                // Get references to the views of message.xml
                //TODO dont forget the image
                TextView newsTitle = (TextView)v.findViewById(R.id.news_list_item_title);
                TextView newsDetails = (TextView)v.findViewById(R.id.news_list_item_details);

                // Set their text
                newsTitle.setText(model.getTitle());
                newsDetails.setText(model.getContent());

            }
        };
        newsFirebaseListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

            }
        });
        newsListView.setAdapter(newsFirebaseListAdapter);
    }

    @Override
    public void onDataSetChanged(String data) {

//        newsAdapter.getmValues().addAll(parser.fromJson(data, NewsArray.class).getNews());
//        FirebaseDatabase.getInstance()
//                .getReference("News")
//                .push()
//                .setValue(newsAdapter.getmValues().get(0)
//                );

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                newsAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
