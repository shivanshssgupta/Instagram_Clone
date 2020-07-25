package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {
    LinearLayout linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        Intent intent= getIntent();
        String username= intent.getStringExtra("username");
        linear= findViewById(R.id.linear);
        ParseQuery<ParseObject> query= new ParseQuery<ParseObject>("Images");
        query.whereEqualTo("username",username);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null && objects.size()>0)
                {
                    for (ParseObject object:objects)
                    {
                        ParseFile file= (ParseFile) object.get("image");

                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                Log.i("Info",data.toString());
                                if(e==null && data!=null)
                                {
                                    Log.i("Info","Check");
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView imageView= new ImageView(getApplicationContext());
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                                    ));
                                    imageView.setImageBitmap(bitmap);
                                    linear.addView(imageView);
                                }
                                else
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
                else
                {
                    e.printStackTrace();
                }
            }
        });





    }
}