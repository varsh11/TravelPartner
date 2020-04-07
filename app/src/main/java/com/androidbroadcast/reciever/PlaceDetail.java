package com.androidbroadcast.reciever;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


/**
 * Created by Shish on 4/9/2019.
 */

public class PlaceDetail extends Activity {
    ImageView image;
    String placeId;
    String photoId;
    Bitmap temp1;
    TextView rating;
    TextView location;
    TextView name;
    TextView openNow;
    public static final String Openweather_API_KEY = "30daf9cdfa363cfee658e3ae167e6a1c";
    String temp2;
    ArrayList<WeatherModel> venuesList;
    TextView weather;
    float sensorValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);
        image = (ImageView) findViewById(R.id.imageView);
        rating = (TextView) findViewById(R.id.ratingView);
        location = (TextView) findViewById(R.id.locView);
        name = (TextView) findViewById(R.id.nameView);
        openNow = (TextView) findViewById(R.id.openView);
        weather = (TextView) findViewById(R.id.weatherView);

        /*TextView link = (TextView) findViewById(R.id.nearbyView);
        link.setMovementMethod(LinkMovementMethod.getInstance());*/


        final Bundle extras = getIntent().getExtras();
        // Extract data using passed keys
        placeId = extras.getString("placeId");
        photoId = extras.getString("photoReferenceId");
        //temp1 = Util.makePhotoCall("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoId + "&language=en&key=" + GooglePlacesExample.GOOGLE_KEY );
        image.setImageBitmap(temp1);
        rating.setText("Rating : " + extras.getString("rating") + "/5.0");
        location.setText(extras.getString("location"));
        name.setText(extras.getString("name"));
        openNow.setText("Open now : " + extras.getString("openNow"));
        Log.d("Bundle Object", "onCreate: " + photoId);


        try {
            temp1 = new googlephoto(new AsyncResponse() {
                public void processFinish(ArrayList output) {
                }
            }).execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            temp2 = new weatherUpdate(new AsyncResponse() {
                public void processFinish(ArrayList output) {


                }
            }).execute(Float.parseFloat(extras.getString("lat")), Float.parseFloat(extras.getString("lon"))).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.d("checking", "onCreate: " + "called");
        Button clickButton = (Button) findViewById(R.id.mapOpen);
        clickButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%s", Float.parseFloat(extras.getString("lat")), Float.parseFloat(extras.getString("lon")), extras.getString("name"));
                Log.d("Uri", "onClick: " + uri);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);


            }
        });
    }


    private class googlephoto extends AsyncTask<View, Bitmap, Bitmap> {

        Bitmap temp;
        public AsyncResponse delegate = null;

        public googlephoto(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Bitmap doInBackground(View... urls) {
            // make Call to the url BitmapFactory.decodeResource(getResources(),R.drawable.android);
            temp = Util.makePhotoCall("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoId + "&key=" + GooglePlacesExample.GOOGLE_KEY);
            //Log.d("temp", "doInBackground: " + temp.toString());
            return temp;
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }


        protected void onPostExecute(Bitmap tempResult) {
            if (temp == null) {
                // we have an error to the call
                // we can also stop the progress bar
                // Log.d("temp+1", "onPostExecute: " + temp);
            } else {
                // all things went right
                image.setImageBitmap(temp);

            }

        }
    }

    private class weatherUpdate extends AsyncTask<Float, String, String> {

        String temp;
        public AsyncResponse delegate = null;

        public weatherUpdate(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected String doInBackground(Float... urls) {
            // make Call to the url

            temp = Util.makeCall("https://api.openweathermap.org/data/2.5/weather?lat=" + urls[0] + "&lon=" + urls[1] + "&units=metric&appid=" + Openweather_API_KEY);
            //Log.d("temp", "doInBackground: " + temp.toString() + urls[0] + urls[1]);
            return temp;
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }


        protected void onPostExecute(String tempResult) {
            if (temp == null) {
                //Log.d("temp+2", "onPostExecute: " + temp);
            } else {



                //Log.d("temp+2", "doInBackground: " + temp.toString());
                venuesList = (ArrayList<WeatherModel>) Util.parseWeather(temp);
                //Log.d("Temp output", "onPostExecute: " + temp);
                if (venuesList.size() > 0) {
                    weather.setText(venuesList.get(0).getDescription() + "\n" + venuesList.get(0).getTemperature() + "\u00B0" + "C\n" + venuesList.get(0).getWind() + "mps");
                }
                delegate.processFinish(venuesList);
            }

        }

    }

    public void share(View view) {
        TextView name = (TextView) findViewById(R.id.nameView);
        final Bundle extras = getIntent().getExtras();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Tourist Place");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, name.getText());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Name : "+extras.getString("name")+"\n"+"https://maps.google.com/?q="+ Float.parseFloat(extras.getString("lat")) + "," + Float.parseFloat(extras.getString("lon")));
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


}
