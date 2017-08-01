package com.kareem.newme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.kareem.newme.Authentication.AuthenticationActivity;
import com.kareem.newme.Model.RealmObjects.RealmUserUtils;
import com.kareem.newme.Model.User;
import com.kareem.newme.News.NewsFragment;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private NewsFragment newsFragment;
    private final int LOGIN_ACTIVITY_RESULT_CODE = 3521;
    private TextView navigationBarHeader;
    private Menu menu;
    private UserRoleFragment activeUserRoleFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationBarHeader = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navigation_header_text);
        menu = navigationView.getMenu();
        if (savedInstanceState == null) {
            newsFragment = new NewsFragment();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInAnonymously();
            navigationView.setCheckedItem(R.id.nav_news);
            navigate(newsFragment);
        }
        RealmUserUtils realmUserUtils = new RealmUserUtils(this);
        RunTimeItems.loggedUser = realmUserUtils.getLoggedUserFromDataBase();
        if(RunTimeItems.loggedUser != null) updateLoginStatus();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            navigate(newsFragment);
        }
        else if (id == R.id.nav_login){
            if (RunTimeItems.loggedUser == null)
            navigate(AuthenticationActivity.class);
            else logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigate(Fragment fragment)
    {

        getSupportFragmentManager().beginTransaction()
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
        navigationBarHeader.setText(RunTimeItems.loggedUser.getName());
        menu.findItem(R.id.nav_login).setTitle(getString(R.string.logout));
        triggerUserRoleChanged();
    }
    private void logout() {
        navigationBarHeader.setText(getString(R.string.anonymous));
        menu.findItem(R.id.nav_login).setTitle(getString(R.string.login));
        RunTimeItems.loggedUser = null;
        RealmUserUtils realmUserUtils = new RealmUserUtils(this);
        realmUserUtils.clearRealmItems();
        triggerUserRoleChanged();
    }
    private void triggerUserRoleChanged()
    {
        if (activeUserRoleFragment != null)
        activeUserRoleFragment.onUserRoleChanged();
    }
}
