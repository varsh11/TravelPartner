package com.androidbroadcast.reciever;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Shish on 4/8/2019.
 */

public class GooglePlacesExample extends ListActivity {
        ArrayList<GooglePlace> venuesList;
        public static String placeId;
        public static final int REQ_VOICE = 1;


    // the google key

        // ============== YOU SHOULD MAKE NEW KEYS ====================//
        final static String GOOGLE_KEY = "AIzaSyB2NP1cNPXmksIfAvBVioGmatLilKdIxn8";

        // we will need to take the latitude and the logntitude from a certain point
        // this is the center of New York
        //final String query = "napa+valley+point+of+interest";
       // final String longtitude = "-73.9852992";

        ArrayAdapter<String> myAdapter;
        ArrayList<GooglePlace> outputObtained;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);

            //execute the async task
            new googleplaces(new AsyncResponse() {
                public void processFinish(ArrayList output) {
                   // Log.d("", "processFinish: "+output);
                   outputObtained = new ArrayList<GooglePlace>(output);
                   // Log.d("Output", "processFinish: "+outputObtained.get(0).getPlaceId());

                }
            }).execute();



            ListView lv = getListView();
            //Log.d("ListView", "onCreate: "+lv.get);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                        long arg3)
                {
                   // String value = (String)adapter.getItemAtPosition(position);
                    Log.d("Value", "onItemClick: "+outputObtained.get(position).getPlaceId());
                    Log.d("Value PlaceId", "onItemClick: "+outputObtained.get(position).getLatitude());
                    Intent i = new Intent(GooglePlacesExample.this,PlaceDetail.class);
                    i.putExtra("placeId",outputObtained.get(position).getPlaceId());
                    i.putExtra("photoReferenceId",outputObtained.get(position).getPhotoRefId());
                    i.putExtra("name",outputObtained.get(position).getName());
                    i.putExtra("location",outputObtained.get(position).getFormatted_address());
                    i.putExtra("rating",outputObtained.get(position).getRating());
                    i.putExtra("category",outputObtained.get(position).getCategory());
                    i.putExtra("openNow",outputObtained.get(position).getOpenNow());
                    i.putExtra("lat",outputObtained.get(position).getLatitude());
                    i.putExtra("lon",outputObtained.get(position).getLongitude());
                    i.putExtra("openNow",outputObtained.get(position).getOpenNow());
                    startActivity(i);
                    // assuming string and if you want to get the value on click of list item
                    // do what you intend to do on click of listview row
                }
            });
            }

            public void go(View view){
                new googleplaces(new AsyncResponse() {
                    public void processFinish(ArrayList output) {
                        // Log.d("", "processFinish: "+output);
                        outputObtained = new ArrayList<GooglePlace>(output);
                        // Log.d("Output", "processFinish: "+outputObtained.get(0).getPlaceId());

                    }
                }).execute();

            }

        private class googleplaces extends AsyncTask<View, Void, ArrayList> {

            String temp;
            public AsyncResponse delegate = null;

            public googleplaces(AsyncResponse asyncResponse) {
                delegate = asyncResponse;//Assigning call back interfacethrough constructor
            }


            @Override
            protected ArrayList doInBackground(View... urls) {
                EditText query = (EditText) findViewById(R.id.searchEdit);

                @SuppressLint("WrongThread") String q=query.getText().toString();
                // make Call to the url
                temp = Util.makeCall("https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + Util.queryModifier(q) + "+point+of+interest+&language=en&key=" + GOOGLE_KEY);
                //Log.d("temp", "doInBackground: "+temp);
                return new ArrayList();
            }

            @Override
            protected void onPreExecute() {
                // we can start a progress bar here
            }


            @Override
            protected void onPostExecute(ArrayList result) {
               if (temp == null) {
                   /*TextView msg=(TextView)findViewById(R.);
                   msg.setTextSize(30);
                   msg.setText("No serach result available");
                   */ // we have an error to the call
                    // we can also stop the progress bar
                   Toast.makeText(getApplicationContext(), "No serach result available", Toast.LENGTH_SHORT).show();
                } else {
                    // all things went right

                    // parse Google places search result
                    venuesList = (ArrayList<GooglePlace>) Util.parseGoogleParse(temp);

                   Collections.sort(venuesList, new Comparator<GooglePlace>() {
                       public int compare(GooglePlace lhs, GooglePlace rhs) {
                           Float a = Float.parseFloat(lhs.getRating());
                           Float b = Float.parseFloat(rhs.getRating());
                           return b.compareTo(a);
                       }
                   });

                    List<String> listTitle = new ArrayList<String>();

                    for (int i = 0; i < venuesList.size(); i++) {
                        // make a list of the venus that are loaded in the list.
                        // show the name, the category and the city
                        Log.d("Array", "onPostExecute: "+venuesList.get(i).getCategory());
                        listTitle.add(i, venuesList.get(i).getName() + "\n" + venuesList.get(i).getRating() + "\n" + venuesList.get(i).getFormatted_address());

                    }

                    // set the results to the list
                    // and show them in the xml
                    Log.d("Output is : ", "onPostExecute: "+listTitle.toString());
                   ListView simpleList = getListView();

                   MyAdapter myAdapter=new MyAdapter(getBaseContext(),R.layout.row_layout,venuesList);
                   simpleList.setAdapter(myAdapter);

                    /*myAdapter = new ArrayAdapter<String>(GooglePlacesExample.this, R.layout.row_layout, R.id.Name, listTitle);
                    setListAdapter(myAdapter);
                    */
                    delegate.processFinish(venuesList);
                }
            }


        }

    public void voice(View view) {
        Log.d("Inside voice", "voice: "+"started");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            Log.d("Inside try", "voice: "+"started");
            startActivityForResult(intent, REQ_VOICE);
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && null != data)
        {
            // handles voice results
            if (requestCode == GooglePlacesExample.REQ_VOICE) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String query = result.get(0);
                Log.d("Recognizer result", "onActivityResult: "+query);
                Util.queryModifier(query);
                EditText actv = (EditText) findViewById(R.id.searchEdit);
                actv.setText(query);
            }

        }

    }

}

