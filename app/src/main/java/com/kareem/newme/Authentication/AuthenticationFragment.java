package com.kareem.newme.Authentication;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kareem.newme.Adapters.FragmentAdapter;
import com.kareem.newme.R;
import com.kareem.newme.ViewPagerFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class AuthenticationFragment extends ViewPagerFragment {

    public AuthenticationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.authentication_ViewPager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager());
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setFragmentTitle(getString(R.string.login));
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setFragmentTitle(getString(R.string.register));
        fragmentAdapter.getFragments().add(loginFragment);
        fragmentAdapter.getFragments().add(registerFragment);
        viewPager.setAdapter(fragmentAdapter);
        return view;
    }
}
