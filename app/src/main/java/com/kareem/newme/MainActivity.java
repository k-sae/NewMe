package com.kareem.newme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kareem.newme.Chatting.Messages.Message;
import com.kareem.newme.Chatting.UserMessage.UserMessage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(this, NavigationActivity.class);

//        UserMessage userMessage = new UserMessage("bor3ey");
//        FirebaseDatabase.getInstance()
//                .getReference("Users").child("1").child("messages")
//                .push()
//                .setValue(new Message(1, "hello", "some date"));


        startActivity(intent);
    }


}
