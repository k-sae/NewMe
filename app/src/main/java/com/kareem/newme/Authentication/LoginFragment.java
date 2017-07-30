package com.kareem.newme.Authentication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kareem.newme.R;
import com.kareem.newme.ViewPagerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends ViewPagerFragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //TODO
        //add data base
        //add listener

        return view;
    }
}
