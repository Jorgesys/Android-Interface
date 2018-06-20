package com.jorgesys.aninterface;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;


public class JsonTask extends AsyncTask<String, String, String> {


    private final static String TAG = "JsonTask";
    private Communique communique;
    private AppCompatActivity context;

    JsonTask(Communique communique){
        this.communique = communique;
    }

    protected void onPreExecute() {
        super.onPreExecute();

    }

    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            Log.e(TAG, "JsonTask " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "JsonTask " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "JsonTask " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        parseJson(result);
    }


    private void parseJson(String data){
        JSONObject jsonData = null;
        try {
            jsonData = new JSONObject(data);
            JSONArray jsonRoutes = null;

            //Obtiene el JSONArray de routes.
            jsonRoutes = jsonData.getJSONArray("routes");
            for (int i = 0; i < jsonRoutes.length(); i++) {
                JSONObject jsonRoute = jsonRoutes.getJSONObject(i);

                //Obtiene objeto overview_polyline.
                JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
                //Obtiene JSONArray legs.
                JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
                //Obtiene los objetos de array legs,
                for (int j = 0; j < jsonLegs.length(); j++) {

                    JSONObject jsonLeg = jsonLegs.getJSONObject(j);
                    //Obtiene el JSONObject de distance.
                    JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                    //Obtiene el JSONArray de traffic_speed_entry.
                    JSONArray jsonTrafficSpeedEntry = jsonLeg.getJSONArray("traffic_speed_entry");
                    //Obtiene el valor de end_address.
                    String jsonEndAddress = jsonLeg.getString("end_address");
                    //Obtiene el JSONArray de via_waypoint.
                    JSONArray jsonViaWayPoint = jsonLeg.getJSONArray("via_waypoint");
                    //Obtiene el valor de start_address.
                    String jsonStartAddress = jsonLeg.getString("start_address");
                    //Obtiene el JSONObject de start_location.
                    JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

                    //Obtiene el Array steps.
                    JSONArray jsonSteps = jsonLeg.getJSONArray("steps");
                    //Obtiene elementos de steps
                    String information = "";
                    for (int k = 0; k < jsonSteps.length(); k++) {
                        JSONObject jsonObjectInstruction = jsonSteps.getJSONObject(k);
                        //Obtiene el valor de html_instructions
                        String jsonInstruction = jsonObjectInstruction.optString("html_instructions");
                        Log.i(TAG, "jsonInstruction: " + jsonInstruction);

                        //Add navigation instructions!
                        information +=Html.fromHtml(jsonInstruction+"<br>");

                        JSONObject jsonStepDistance = jsonObjectInstruction.getJSONObject("distance");
                        Log.i(TAG, "jsonStepDistance: " + jsonStepDistance);
                        String jsonTravelMode = jsonObjectInstruction.optString("travel_mode");
                        Log.i(TAG, "jsonTravelMode: " + jsonTravelMode);
                        String jsonManeuver = jsonObjectInstruction.optString("maneuver");
                        Log.i(TAG, "jsonManeuver: " + jsonManeuver);
                        JSONObject jsonStepStartLocation = jsonObjectInstruction.getJSONObject("start_location");
                        Log.i(TAG, "jsonStepStartLocation: " + jsonStepStartLocation);
                        JSONObject jsonStepPolyline = jsonObjectInstruction.getJSONObject("polyline");
                        Log.i(TAG, "jsonStepPolyline: " + jsonStepPolyline);
                        JSONObject jsonStepDuration = jsonObjectInstruction.getJSONObject("duration");
                        Log.i(TAG, "jsonStepDuration: " + jsonStepDuration);
                        JSONObject jsonEndLocation = jsonObjectInstruction.getJSONObject("end_location");
                        Log.i(TAG, "jsonEndLocation: " + jsonEndLocation);
                    }
                    //Obtiene el objeto duration.
                    JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
                    //Obtiene el objeto end_location.
                    JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");

                    Log.i(TAG, "jsonDistance: "+ jsonDistance );
                    Log.i(TAG, "jsonTrafficSpeedEntry: "+ jsonTrafficSpeedEntry );
                    Log.i(TAG, "jsonEndAddress: "+ jsonEndAddress );
                    Log.i(TAG, "jsonViaWayPoint: "+ jsonViaWayPoint );
                    Log.i(TAG, "jsonStartAddress: "+ jsonStartAddress );
                    Log.i(TAG, "jsonStartLocation: "+ jsonStartLocation );
                    Log.i(TAG, "jsonDuration: " + jsonDuration);
                    Log.i(TAG, "jsonEndLocation: " + jsonEndLocation);

                    //Print navigation instructions.
                    communique.respond(information);
                }

                Log.i(TAG, "TERMINA PARSER!");

            }

        } catch (JSONException e) {
            Log.e(TAG, "parseJson() " + e.getMessage());
            communique.respond("JSONException : " + Html.fromHtml("<font color=red>ERROR!" + e.getMessage() +"</font>"));
        }catch (Exception e) {
            Log.e(TAG, "parseJson() " + e.getMessage());
            communique.respond("Exception : " + Html.fromHtml("<font color=red>ERROR!" + e.getMessage() +"</font>"));
        }
    }





}