package com.example.bigblackbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bigblackbox.activity.IndexActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Login(View view){
        Intent intent = new Intent(this,login.class);
        startActivity(intent);
    }

    public void Register(View v){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}