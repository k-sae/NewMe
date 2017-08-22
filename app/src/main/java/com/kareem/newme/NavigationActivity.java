package com.kareem.newme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.kareem.newme.Authentication.AuthenticationActivity;
import com.kareem.newme.Chatting.Messages.MessagesFragment;
import com.kareem.newme.Chatting.UserMessage.UserMessagesFragment;
import com.kareem.newme.Connections.VolleyRequest;
import com.kareem.newme.FAQ.FAQFragment;
import com.kareem.newme.Model.RealmObjects.RealmTokenUtils;
import com.kareem.newme.Model.RealmObjects.RealmUserUtils;
import com.kareem.newme.Model.User;
import com.kareem.newme.News.NewsFragment;
import com.kareem.newme.homePage.HomePageFragment;

import io.fabric.sdk.android.Fabric;
import java.util.HashMap;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationActivityCallBack {
    private NewsFragment newsFragment;
    private MessagesFragment messagesFragment;
    private UserMessagesFragment userMessagesFragment;
    private final int LOGIN_ACTIVITY_RESULT_CODE = 3521;
    private TextView navigationBarHeader;
    private Menu menu;
    private UserRoleFragment activeUserRoleFragment;
    private FAQFragment faqFragment;
    private HomePageFragment homePageFragment;
    private NavigationView navigationView;
    private TextView titlebar;
    private int selectedItemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.TRANSPARENT);
         titlebar = (TextView) findViewById(R.id.toolbar_title);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        initFragments();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationBarHeader = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navigation_header_text);
        menu = navigationView.getMenu();
        if (savedInstanceState == null) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInAnonymously();
            setActive(R.id.nav_home);
        }
        RealmUserUtils realmUserUtils = new RealmUserUtils(this);
        RunTimeItems.loggedUser = realmUserUtils.getLoggedUserFromDataBase();
        if(RunTimeItems.loggedUser != null) forceLogin();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (selectedItemId != R.id.nav_home)
            {
                setActive(R.id.nav_home);
            }
            else
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        onNavigationItemSelected(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onNavigationItemSelected(int id) {
        navigationView.setCheckedItem(id);
        if (id == R.id.nav_news) {
            navigate(newsFragment);
            titlebar.setText(getString(R.string.news));
        }
        else if (id == R.id.nav_login){
            if (RunTimeItems.loggedUser == null)
                navigate(AuthenticationActivity.class);
            else logout();
        }
        else if (id == R.id.nav_contact_us)
        {
            if (RunTimeItems.loggedUser == null) onNavigationItemSelected(R.id.nav_login);
            else if (RunTimeItems.loggedUser.getUserType().equals(Constants.ADMIN_TYPE))
                navigate(userMessagesFragment);
            else navigate(messagesFragment);
            titlebar.setText(getString(R.string.contact_us));
        }
        else if (id == R.id.nav_FAQََ) {
            navigate(faqFragment);
            titlebar.setText(getString(R.string.faq));
        }
        else if (id == R.id.nav_home)
        {
            navigate(homePageFragment);
            titlebar.setText(getString(R.string.home));
        }
        selectedItemId = id;
    }

    private void navigate(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null) return;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment == null) initFragments();

        if (fragmentTransaction == null) return;
        fragmentTransaction
                .replace(R.id.frame_content,
                        fragment)
                .commit();
        if (fragment instanceof UserRoleFragment)
        {
            activeUserRoleFragment = (UserRoleFragment) fragment;
        }
    }
    private void navigate(Class<?> cls)
    {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent,LOGIN_ACTIVITY_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_ACTIVITY_RESULT_CODE && resultCode == Activity.RESULT_OK)
        {
            String userData = data.getStringExtra(Constants.USER_DATA);
            RunTimeItems.loggedUser = new Gson().fromJson(userData, User.class);
            new RealmUserUtils(this).save(RunTimeItems.loggedUser);
            updateLoginStatus();
        }
    }
    private void updateLoginStatus()
    {
        Map<String , String> stringMap = new HashMap<>();
        stringMap.put("req", "setUserDevice");
        String token = new RealmTokenUtils(this).getTokenFromDataBase().getId();
        if (token == null) return;
        stringMap.put("deviceToken", token);
        stringMap.put("userId", RunTimeItems.loggedUser.getId());
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,stringMap,this) {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast("Connection Error", NavigationActivity.this);
                forceLogout();
            }

            @Override
            public void onResponse(String response) {
                forceLogin();
            }
        };
        volleyRequest.start();

    }
    private void forceLogin()
    {
        navigationBarHeader.setText(RunTimeItems.loggedUser.getName());
        menu.findItem(R.id.nav_login).setTitle(getString(R.string.logout));
        triggerUserRoleChanged();
    }
    private void logout() {
        Map<String , String> stringMap = new HashMap<>();
        stringMap.put("req", "unSetUserDevice");
        stringMap.put("userId", RunTimeItems.loggedUser.getId());
        VolleyRequest volleyRequest = new VolleyRequest(Constants.BASE_URL,stringMap,this) {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast("Connection Error", NavigationActivity.this);
            }

            @Override
            public void onResponse(String response) {
                forceLogout();
            }
        };
        volleyRequest.start();
    }
    private void forceLogout()
    {
        navigationBarHeader.setText(getString(R.string.guest));
        menu.findItem(R.id.nav_login).setTitle(getString(R.string.login));
        RunTimeItems.loggedUser = null;
        RealmUserUtils realmUserUtils = new RealmUserUtils(this);
        realmUserUtils.clearRealmItems();
        triggerUserRoleChanged();

    }
    private void triggerUserRoleChanged()
    {
//        if (activeUserRoleFragment != null)
//        activeUserRoleFragment.onUserRoleChanged();
        setActive(R.id.nav_home);
    }

    @Override
    public void setActive(int id) {

        onNavigationItemSelected(id);
    }

    private void initFragments()
    {
        newsFragment = new NewsFragment();
        messagesFragment = new MessagesFragment();
        userMessagesFragment = new UserMessagesFragment();
        faqFragment  = new FAQFragment();
        homePageFragment = new HomePageFragment();
    }
}
