package com.kareem.newme.Authentication;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kareem.newme.Connections.VolleyRequest;
import com.kareem.newme.Constants;
import com.kareem.newme.Model.User;
import com.kareem.newme.R;
import com.kareem.newme.ViewPagerFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends ViewPagerFragment implements View.OnClickListener {

    private EditText userName;
    private EditText password;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        setFragmentTitle(getString(R.string.login));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //TODO
        //add data base
        //add listener
        userName = (EditText) view.findViewById(R.id.login_username);
        password = (EditText) view.findViewById(R.id.login_password);
        Button loginButton = (Button) view.findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Logging in ...","Please wait...",false,false);
        loading.show();
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,getParams(),getActivity()) {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getActivity(),"Network Error please Try again", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                loading.dismiss();
                User user = new Gson().fromJson(response, User.class);
                if (user == null)
                {
                    Toast.makeText(getActivity(),"Wrong User Name or Password", Toast.LENGTH_LONG).show();
                }
                else
                {
                    //TODO database stuff
                }
            }
        };
        volleyRequest.start();
    }

    private Map<String, String> getParams()
    {
        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("req", "login");
        stringStringMap.put("username", userName.getText().toString());
        stringStringMap.put("password", password.getText().toString() );
        return stringStringMap;
    }
}
