package com.example.ale.myapplicatio;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {

    private AutoCompleteTextView cerca;
    private Button bottone;
    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<String> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        DataBase db = new DataBase(this);
        db.insertViaggio();
        ButtonListener buttonListener = new ButtonListener();
        cityList = new ArrayList<>();
        bottone = (Button) findViewById(R.id.bottone);
        cerca = (AutoCompleteTextView) findViewById(R.id.cerca);
        final TextWatcher passwordWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() >2) {
                    new GetCity().execute(s.toString());
                }
            }

            public void afterTextChanged(Editable s) {
                cityList.clear();
            }
        };
        cerca.addTextChangedListener(passwordWatcher);
        bottone.setOnClickListener(buttonListener);
        cerca.setOnEditorActionListener(editTextListener);



    }



    private class GetCity extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // Toast.makeText(MainActivity.this, "Json Data is downloading " , Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            String url1 = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";
            String url2 ="&types=(cities)&language=it&key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";
            //String url2 ="&types=(cities)&key=AIzaSyBieTKI8Lmg7TuF2MgUUtK93bjpWylxLBM";
            String url= url1 + arg0[0] + url2 ;
            Log.e(TAG, "url  " + url);
            String jsonStr = sh.makeServiceCall(url);
                       if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray predictions = jsonObj.getJSONArray("predictions");

                    // looping through All Contacts
                    for (int i = 0; i < predictions.length(); i++) {
                        JSONObject c = predictions.getJSONObject(i);
                        String description = c.getString("description");
                        //String id = c.getString("id");
                        /*JSONArray matched_substrings = c.getJSONArray("matched_substrings");
                        for (int j = 0; j < matched_substrings.length(); j++) {
                            JSONObject m = matched_substrings.getJSONObject(j);
                            String length = m.getString("length");
                            String offset = m.getString("offset");
                        }*/
                        //String placeid = c.getString("place_id");
                        //String reference = c.getString("reference");
                        //JSONObject structured_formatting = c.getJSONObject("structured_formatting");
                        //String main_text = structured_formatting.getString("main_text");
                        /*JSONArray main_text_matched_substrings = structured_formatting.getJSONArray("main_text_matched_substrings");
                        for (int k = 0; k < main_text_matched_substrings.length(); k++) {
                            JSONObject mt = main_text_matched_substrings.getJSONObject(k);
                            String length1 = mt.getString("length");
                            String offset1 = mt.getString("offset");
                        }*/
                        //String secondary_text = structured_formatting.getString("secondary_text");
                        /*JSONArray terms = c.getJSONArray("terms");
                        for (int p = 0; p < terms.length(); p++) {
                            JSONObject t = terms.getJSONObject(p);
                            String offset = t.getString("offset");
                            String value = t.getString("value");
                        }*/
                        //String types = c.getString("types");
                        cityList.add(description);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                //Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(getApplicationContext(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, cityList);
            adapter.notifyDataSetChanged();
            cerca.setAdapter(adapter);
            if (!cerca.isPopupShowing()) {
                cerca.showDropDown();
            }

        }
    }

    public class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String ricerca = cerca.getText().toString();
            if(!ricerca.equals("")){
                Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                intent.putExtra("citta", ricerca);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),"Metti una city pezzo di negro bastardo infame",Toast.LENGTH_LONG).show();
            }


        }


    }
    private TextView.OnEditorActionListener editTextListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                String ricerca = cerca.getText().toString();
                if(!ricerca.equals("")){
                    Intent intent = new Intent(MainActivity.this, RicercaActivity.class);
                    intent.putExtra("citta", ricerca);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Metti una city pezzo di negro bastardo infame",Toast.LENGTH_LONG).show();
                }
            }
            return false;
        }
    };

}


