package com.example.inclass04;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    ExecutorService threadPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.asynctaskbtn);
        imageView = findViewById(R.id.defaultImage);
        progressBar = findViewById(R.id.progressBar);
        threadPool = Executors.newFixedThreadPool(10);

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
                threadPool.execute(new ImageUsingThread());
            }
        });
    }
    public class ImageUsingThread implements Runnable {
        @Override
        public void run() {
            newurl = "https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg";
            Bitmap newbitmap = getImageBitmap(newurl);
            imageView.setImageBitmap(newbitmap);
            imageView.setVisibility(View.VISIBLE);
              progressBar.setVisibility(View.INVISIBLE);
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

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = getImageBitmap(strings);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
           imageView.setVisibility(View.VISIBLE);
           imageView.setImageBitmap(bitmap);
           progressBar.setVisibility(View.INVISIBLE);
        }


    }


}


