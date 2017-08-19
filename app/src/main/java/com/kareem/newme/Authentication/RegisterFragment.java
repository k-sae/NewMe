package com.kareem.newme.Authentication;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.kareem.newme.ApiRespond;
import com.kareem.newme.Connections.VolleyRequest;
import com.kareem.newme.Constants;
import com.kareem.newme.Model.User;
import com.kareem.newme.R;
import com.kareem.newme.Utils;
import com.kareem.newme.ViewPagerFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends ViewPagerFragment implements View.OnClickListener, View.OnFocusChangeListener {

    private View view;
    private EditText fullName, emailId, mobileNumber, userName,
            password, confirmPassword;
    private TextView errorTextView;
    private Button signUpButton;
    private boolean isValidUserName;
    private CountryCodePicker countryCodePicker;
    private User user;
    private Activity parent;
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        setLayout();
        return view;
    }

    private void setLayout() {
        userName = (EditText) view.findViewById(R.id.userName);
        fullName = (EditText) view.findViewById(R.id.fullName);
        emailId = (EditText) view.findViewById(R.id.userEmailId);
        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        errorTextView = (TextView) view.findViewById(R.id.error_text_already_exists);
        countryCodePicker = (CountryCodePicker) view.findViewById(R.id.ccp);
        setListeners();
    }

    private void setListeners() {
        signUpButton.setOnClickListener(this);
        userName.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (validate())
        {
            //TODO
            //2-send to server
            final Map<String, String> params = new HashMap<>();
            params.put("req", "registerUser");
            params.put("user", new Gson().toJson(user));
            if (parent == null) return;
            VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,params,parent) {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (parent != null)
                    Toast.makeText(parent,"Network Error please Try again", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String response) {
                    if (response.contains("added"))
                    {
                        if (parent != null)
                        new CustomToast().Show_Toast(parent, view,
                                "Registered");
                    }
                    else
                    {
                        if (parent != null)
                        new CustomToast().Show_Toast(parent, view,
                                new Gson().fromJson(response, ApiRespond.class).getApiRespond());
                    }
                }
            };
            volleyRequest.start();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus)
        {
            Map<String, String> params = new HashMap<>();
            params.put("req", "validUsername");
            params.put("username", userName.getText().toString());
            if (parent == null) return;
            VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,params,parent) {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (parent != null)
                    Toast.makeText(parent,"Network Error please Try again", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String response) {
                    if (response.contains("true"))
                    {
                        errorTextView.setVisibility(View.GONE);
                        isValidUserName = true;
                    }
                    else
                    {
                        errorTextView.setVisibility(View.VISIBLE);
                        isValidUserName = false;
                    }
                }
            };
            volleyRequest.start();
        }
    }
    private boolean validate() {
        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();
        String country = countryCodePicker.getSelectedCountryName();
        String phonePrefix = countryCodePicker.getFullNumber();
        String userName = this.userName.getText().toString();
        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0
                || userName.equals("")
                || userName.length() == 0
                ) {
            if (parent != null) return false;
            new CustomToast().Show_Toast(parent, view,
                    "All fields are required.");
        }
            // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(parent, view,
                    "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(parent, view,
                    "Both password doesn't match.");
            // Make sure user should check Terms and Conditions checkbox

            // Else do signup or do your stuff
        else {
            user = new User();
            user.setUserType("1");
            user.setName(getFullName);
            user.setEmail(getEmailId);
            user.setPhone(phonePrefix + getMobileNumber);
            user.setCountry(country);
            user.setGender("0");
            user.setPass(getPassword);
            user.setUsername(userName);
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = activity;
    }
}

