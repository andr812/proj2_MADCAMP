package com.example.user.project2;

/**
 * Created by user on 2017-12-29.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ImageGridActivity extends Activity {

    ArrayList<String> DATALIST = new ArrayList<String>();
    ArrayList<String> imageIDs = new ArrayList<String>();

    Button getbtn;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smain); //xml파일...

        try {
            DATALIST = new HttpCall.galleryGET().execute("http://52.79.128.200:3000/api/pics").get();
            for(int i=0;i<DATALIST.size();i++){
                imageIDs.add (DATALIST.get(i));
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        getbtn = findViewById(R.id.getbtn);
        getbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                GridView gridViewImages = (GridView)findViewById(R.id.gridViewImages);
                ImageGridAdapter imageGridAdapter = new ImageGridAdapter(view.getContext(), imageIDs);
                gridViewImages.setAdapter(imageGridAdapter);
            }
        });
    }
    private Bitmap getBitmapFromString(String jsonString) {
/*
* This Function converts the String back to Bitmap
* */
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }


}