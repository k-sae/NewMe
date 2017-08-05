package com.kareem.newme.FAQ;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kareem.newme.Connections.VolleyRequest;
import com.kareem.newme.Constants;
import com.kareem.newme.Model.News;
import com.kareem.newme.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class FAQEditorFragment extends Fragment {

    private TextView titleTextView;
    private TextView contentTextView;
    private FAQ faq;
    public FAQEditorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        faq = new FAQ();
        View view = inflater.inflate(R.layout.fragment_faq_editor, container, false);
        setLayout(view);
        return view;
    }
    private void setLayout(View view)
    {
        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = contentTextView.getText().toString();
                String title =  titleTextView.getText().toString();
                if (content.equals("") || title.equals(""))
                    Snackbar.make(view, "please fill all text fields", Snackbar.LENGTH_LONG).show();
                else{
                    faq.setQuestion(title);
                    faq.setAnswer(content);
                    uploadData();
                }
            }
        });
        titleTextView = (TextView) view.findViewById(R.id.news_title_edit_text);
        contentTextView = (TextView) view.findViewById(R.id.news_content_edit_text);
        titleTextView.setText(faq.getQuestion());
        contentTextView.setText(faq.getAnswer());
    }

    private void uploadData() {
        Map<String , String> stringMap = new HashMap<>();
        stringMap.put("req", "addFaq");
        stringMap.put("faq", new Gson().toJson(faq));
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,stringMap,getActivity()) {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(String response) {
                getActivity().finish();
            }
        };
        volleyRequest.start();
    }

}
