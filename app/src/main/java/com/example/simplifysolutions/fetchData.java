package com.example.simplifysolutions;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class fetchData extends AsyncTask<Void, Void, Void> {
    String data = "";
    String dataParsed = "";
    String singleParsed = "";
    String x = RateCalc.p.getText().toString();
    String y = RateCalc.d.getText().toString();





    @Override
    protected Void doInBackground(Void... voids) {
        try {
            String from=x, to=y, forg="ontario", tord="ontario";
            final String key="AIzaSyD2jqrZINgV2OAKEHKQdXdD79mcWE0yRJ0";
            URL url;
            url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?"
             +"units=imperial&origins="+from+","+forg+"&destinations="+to+","+tord+"&key="+key+"");


            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data = data + line;
            }
            JSONArray JA = null;
            JSONObject JO = new JSONObject(data);
            JA = JO.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
            JA = JO.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
            singleParsed = JA.getJSONObject(0).getJSONObject("distance").getString("text");

            String []temp = singleParsed.split(" ");
            float dist_kl= (float) (Float.parseFloat(temp[0])/0.62137);
            dataParsed = Float.toString(dist_kl);//temp[0];
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        RateCalc.data.setText(this.dataParsed);

    }
}
