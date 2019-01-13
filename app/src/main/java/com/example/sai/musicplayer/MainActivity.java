package com.example.sai.musicplayer;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    MediaPlayer mediaPlayer;  // It will handle the functions related to our audio files.
    ListView listView;       // ListView -> To view the items/elements in a linearly ordered fashion.
    SeekBar seekBar;   // used to control the timeline of the audio file. Can be scrolled to get ahead or back of the currently playing position
    TextView name;
    Animation animation;
    ImageView playpausebtn;
    ImageView forward;
    ImageView rewind;
    private Handler handler;  //To schedule messages and runnables to be executed as some point in the future. In other words perform action on same thread in future.
    private Runnable runnable;  // A Runnable is basically a type of class (Runnable is an Interface) that can be put into a thread, describing what the thread is supposed to do.

    @Override
    public void onBackPressed() {  // playing audio should stop after pressing back button.
        super.onBackPressed();
        mediaPlayer.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animation = AnimationUtils.loadAnimation(this,R.anim.fadein);
        mediaPlayer= new MediaPlayer();
        seekBar = findViewById(R.id.seekbar);
        playpausebtn = findViewById(R.id.play);
        forward = findViewById(R.id.forward);
        rewind = findViewById(R.id.rewind);
        name=findViewById(R.id.songname);
        listView = findViewById(R.id.listnames);

        handler = new Handler();  // handler object to access the functionalities.

        playpausebtn.setVisibility(View.INVISIBLE);
        forward.setVisibility(View.INVISIBLE);
        rewind.setVisibility(View.INVISIBLE);    // these imageviews are kept invisible at the start.
        seekBar.setVisibility(View.INVISIBLE);

        ArrayList<String> arrayList = new ArrayList<>();  // this adds the songs in the arraylist.
        arrayList.add("Castle of Glass - Linkin Park");
        arrayList.add("Spectre - Alan Walker");
        arrayList.add("I Walk Alone - Green Day");
        arrayList.add("Hero - Skillet");
        arrayList.add("Blank Page - Taylor Swift");
        arrayList.add("Bring Me To Life - Evanescence");
        arrayList.add("ksjdcbkjdsc");

        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.spectre);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        //ArrayAdapter uses a TextView to display each item within it.
        listView.setAdapter(arrayAdapter); // setting the arrayadapter to our listview.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // handling clicks on the songs.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){ //listview items starts from position 0 to n...(0,1,2....n).
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();   // if any song is playing, we want to stop it to play the selected song.
                        playpausebtn.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.play));
                    }
                    playpausebtn.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.VISIBLE);
                    rewind.setVisibility(View.VISIBLE);  // you must have guessed it why we set it to visible :)
                    seekBar.setVisibility(View.VISIBLE);

                    name.startAnimation(animation);
                    name.setText("Castle of Glass");
                    mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.castle);  // creating the mediaplayer , passing our audio file to it.
                }
                else if(position == 1){
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        playpausebtn.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.play));
                    }
                    playpausebtn.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.VISIBLE);
                    rewind.setVisibility(View.VISIBLE);
                    seekBar.setVisibility(View.VISIBLE);
                    name.startAnimation(animation);
                    name.setText("Spectre");
                    mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.spectre);  // creating the mediaplayer,adding our audio file to it.
                }
            }
        });



        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {  //Register a callback to be invoked when the media source is ready for playback.
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mediaPlayer.getDuration()); //setting the seekbar's duration according to the song duration.
                new AsyncCaller().execute(); // executing the asynctask actions ( that is 'ChangeSeekBar' method).
            }
        });


        playpausebtn.setOnClickListener(new View.OnClickListener() {  //play , pause handling.
            @Override
            public void onClick(View v) {
              if(mediaPlayer.isPlaying()){
                  mediaPlayer.pause();
                  playpausebtn.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.play));

              }else {
                  mediaPlayer.start();
                  playpausebtn.setImageDrawable(ContextCompat.getDrawable(MainActivity.this,R.drawable.pause));
                  new AsyncCaller().execute();

              }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               if(fromUser){
                   mediaPlayer.seekTo(progress);  // if ay input from the user eg. if user wants to forward the song usings seekbar, it should progress acc to the input.
               }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(seekBar.getProgress());
            }  // getting the progress of the seekbar pointer.
        });



    }

    public void Action(View view){
            if(view.getId() == R.id.forward){
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000); // forwarding the song by 5 seconds
            }else if(view.getId() == R.id.rewind){
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000); // rewinding the sing by 5 seconds.
            }
    }

    private class AsyncCaller extends AsyncTask<Void,Void,Void>{  //AsyncTask in Android is used to perform heavy task in background.

        @Override
        protected void onPreExecute() {  // what we action want to perform? it goes here.
            super.onPreExecute();
            ChangeSeekBar();

        }

        @Override
        protected Void doInBackground(Void... voids) { // all the background actions goes here.Dont update the UI here.
            return null;
        }

    }
  public void ChangeSeekBar(){
        seekBar.setProgress(mediaPlayer.getCurrentPosition()); //Get the Current Playback Position in an Audio File
        if(mediaPlayer.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                        ChangeSeekBar();  //we need to keep it running on a separate thread rather than our main thread for better performance.
                }
            };
        }
        handler.postDelayed(runnable,1000); // the seekbar pointer psoition should progress forward after 1 sec (1000ms -> 1s).
  }

}
