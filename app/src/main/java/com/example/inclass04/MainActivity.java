package com.example.inclass04;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//Shikha Bhattarai
//Teena Xiong
//InClass 04

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    ProgressBar progressBar;
    Button button;
    Button threadbutton;
    String url = null;
    String newurl = null;
    Handler handler;
    Handler postHandler;

    int progressStatus = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.asynctaskbtn);
        imageView = findViewById(R.id.defaultImage);
        progressBar = findViewById(R.id.progressBar);
        threadbutton = findViewById(R.id.threadbtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg";
                new DownloadImage().execute(url);
            }
        });
        threadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        imageView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(message.what);
                        return false;
                    }
                });
                postHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        progressBar.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageBitmap((Bitmap) message.obj);
                        return false;
                    }
                });
                new Thread(new ImageUsingThread()).start();
            }
        });
    }


    public class ImageUsingThread implements Runnable {
        @Override
        public void run() {
            while(progressStatus<100){
                progressStatus++;
                Message m = new Message();
                m.what = progressStatus;
                handler.sendMessage(m);
                try {
                    // Sleep for 30 milliseconds.
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            progressStatus =0;


            new Thread(new Runnable() {
                @Override
                public void run() {
                    newurl = "https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg";
                    Bitmap newbitmap = getImageBitmap(newurl);
                    Message imageMessage = new Message();
                    imageMessage.obj = newbitmap;
                    postHandler.sendMessage(imageMessage);
                }
            }).start();
        }
    }

    public Bitmap getImageBitmap(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class DownloadImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = getImageBitmap(strings);
            for (int x = 0; x <= 100; x++) {
                for (int j = 0; j < 10000000; j++) {
                }
                publishProgress(x);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
    }


}