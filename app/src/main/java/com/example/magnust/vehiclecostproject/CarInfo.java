package com.example.magnust.vehiclecostproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.preference.EditTextPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class CarInfo extends AppCompatActivity {
    private Button btnGetInfo;
    private TextView txtRes, txtRegNr;
    private EditText edtxtRegNr;
    private DownloadRegNr myTask;
    private URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        btnGetInfo = (Button) findViewById(R.id.btnGetInfo);
        txtRegNr = (TextView) findViewById(R.id.txtRegNr);
        txtRes = (TextView) findViewById(R.id.txtResult);
        edtxtRegNr = (EditText) findViewById(R.id.edtxtRegNr);

    }

    public void getCarInfo(View view) {
        String regNr = edtxtRegNr.getText().toString();
        txtRegNr.setText(regNr);
        myTask = new DownloadRegNr(regNr, txtRes);
        myTask.execute();
    }

    // New class
    public class DownloadRegNr extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "DOWNLOAD_REG";
        private String regNr;
        private TextView txtResult;
        private String resultString;

        public DownloadRegNr(String regNr, TextView txtResult){
            Log.e(TAG, regNr);
            this.regNr = regNr;
            this.txtResult = txtResult;
        }
        @Override
        protected void onPreExecute() {
        /*
         *    do things before doInBackground() code runs
         *    such as preparing and showing a Dialog or ProgressBar
        */
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        /*
         *    updating data
         *    such a Dialog or ProgressBar
        */

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                url = new URL("https://www.vegvesen.no/Kjoretoy/Kjop+og+salg/Kj%C3%B8ret%C3%B8yopplysninger?registreringsnummer=" + regNr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        if(line.contains("CO2")){
                            total.append(line).append('\n');
                        }
                    }
                    resultString = total.toString();
                    Log.i(TAG, resultString);
                    resultString = resultString.trim();
                    /*
                    resultString = resultString.replace("<","");
                    resultString = resultString.replace(">","");
                    resultString = resultString.replace("/","");
                    resultString = resultString.replace("d","");
                    resultString = resultString.replace("t"," ");
                    */
                    resultString = resultString.replaceFirst("</dd>","");
                    resultString = resultString.replaceFirst("<dt>","");
                    resultString = resultString.replaceFirst("</dt>"," ");
                    resultString = resultString.replaceFirst("<dd>","");

                } finally {
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        /*
         *    do something with data here
         *    display it or send to mainactivity
         *    close any dialogs/ProgressBars/etc...
        */
            txtResult.setText(resultString);
        }
    }
}
