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
public class RegisterFragment extends ViewPagerFragment{


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

}
