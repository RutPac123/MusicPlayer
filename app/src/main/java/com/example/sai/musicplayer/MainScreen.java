package com.example.sai.musicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {
    TextView titletext;
    TextView enter;
    Animation myAnime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        titletext = findViewById(R.id.welcometitle);
        myAnime = AnimationUtils.loadAnimation(this,R.anim.fadein);
        titletext.startAnimation(myAnime);

        enter= findViewById(R.id.enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainScreen.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
