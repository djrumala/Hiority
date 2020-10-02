package com.common_id.hiority;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.common_id.hiority.UserLoginActivity.MyPreferences;
import static com.common_id.hiority.UserLoginActivity.mEmail;

/**
 * Created by icol on 08/02/2017.
 */

public class splashscreen extends AppCompatActivity {

    //Set waktu lama splashscreen
    private static int splashInterval = 3000;
    ArrayList<Double> nodelatitude;
    ArrayList<Double> nodelongitude;
    ArrayList<String> nodename;
    ArrayList<Integer> nodeid;
    ArrayList<String> nodea;
    ArrayList<String> nodeb;
    JSONObject jsonobject;
    JSONArray jsonarray, edgeArray;
    String resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashlayout);

        SharedPreferences pref = getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
        String EmailHolder = pref.getString(mEmail, "0");

//        if (!EmailHolder.equals("0")) {

        if (!EmailHolder.equals("0")) {
//        new SaveDataClass(this).execute();
//        new DownloadJSON().execute();
            new GetJSONNode().execute();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    Intent i = new Intent(splashscreen.this, MapsActivity.class);
                    i.putExtra("nolat", nodelatitude);
                    i.putExtra("nolong", nodelongitude);
                    i.putExtra("noid", nodeid);
                    i.putExtra("noname", nodename);
                    i.putExtra("node_a", nodea);
                    i.putExtra("node_b", nodeb);
                    if (nodelatitude != null) {
                        startActivity(i); // menghubungkan activity splashscren ke main activity dengan intent
                    } else {

                    }
                    //jeda selesai Splashscreen
                    this.finish();
                }

                private void finish() {
                    // TODO Auto-generated method stub
                }
            }, splashInterval);
        }

        else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    Intent i = new Intent(splashscreen.this, UserLoginActivity.class);
                    startActivity(i); // menghubungkan activity splashscren ke main activity dengan intent
                    this.finish();
                }

                private void finish() {
                    // TODO Auto-generated method stub
                }
            }, splashInterval);
        }

    }

//    private class DownloadJSON extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//            nodelatitude = new ArrayList<Double>();
//            nodelongitude = new ArrayList<Double>();
//            nodename = new ArrayList<String>();
//            nodeid = new ArrayList<Integer>();
//            nodea = new ArrayList<String>();
//            nodeb = new ArrayList<String>();
//            try {
////                jsonobject = JSONParser.getJSONfromURL("http://www.computer-its.com/TA/Faishol/parsing_db.php");
//                jsonobject = JSONParser.getJSONfromURL("http://common-id.com/hiority/parse.php");
//                jsonarray = jsonobject.getJSONArray("edge_result");
//                for (int i = 0; i < jsonarray.length(); i++) {
//                    jsonobject = jsonarray.getJSONObject(i);
//                    nodea.add(jsonobject.optString("latlng_a"));
//                    nodeb.add(jsonobject.optString("latlng_b"));
//                }
//
//            } catch (Exception e) {
//            }
//            return null;
//        }
//
//        protected void onPostExecute(Void args) {
//            // Locate the spinner in activity_main.xml
//        }
//    }

    private class GetJSONNode extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            nodelatitude = new ArrayList<Double>();
            nodelongitude = new ArrayList<Double>();
            nodename = new ArrayList<String>();
            nodeid = new ArrayList<Integer>();
            nodea = new ArrayList<String>();
            nodeb = new ArrayList<String>();
            try {
//                jsonobject = JSONParser.getJSONfromURL("http://www.computer-its.com/TA/Faishol/parsing_db.php");
                jsonobject = JSONParser.getJSONfromURL("http://common-id.com/hiority/parse.php");
                jsonarray = jsonobject.getJSONArray("node_result");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    nodename.add(jsonobject.optString("node_name"));
                    nodeid.add(jsonobject.optInt("node_id"));
                    nodelatitude.add(jsonobject.optDouble("latitude"));
                    nodelongitude.add(jsonobject.optDouble("longitude"));
                    Log.e("Res", String.valueOf(jsonobject));
                }
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml
            Log.e("Node", String.valueOf(jsonobject));
            Toast.makeText(splashscreen.this, String.valueOf(jsonobject), Toast.LENGTH_LONG).show();
        }
    }

    private class SaveDataClass extends AsyncTask<Void, Void, Void> {
        public Context context;
        String FinalResult;
        ProgressDialog progressDialog;

        public SaveDataClass(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(HiorityDirectionActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setTitle("CampusGuide");
//            progressDialog.setMessage("Loading Data...");
//            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpServiceClass httpServiceClass = null;

            httpServiceClass = new HttpServiceClass("http://common-id.com/hiority/parse2.php");
//            httpServiceClass = new HttpServiceClass("http://www.computer-its.com/TA/Faishol/parsing_db.php\"");
//            httpServiceClass.AddParam("html_instructions", HtmlIns);

            try {
                httpServiceClass.ExecuteGetRequest();
                if (httpServiceClass.getResponseCode() == 200) {
                    FinalResult = httpServiceClass.getResponse().trim();
                    JSONObject Jobject = new JSONObject(FinalResult);

                    try {
//                        JSONArray Jarray = new JSONArray(jsonArray);

                        JSONArray Jarray = Jobject.getJSONArray("node_result");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object = Jarray.getJSONObject(i);
                            nodename.add(object.optString("node_name"));
                            nodeid.add(object.optInt("node_id"));
                            nodelatitude.add(object.optDouble("latitude"));
                            nodelongitude.add(object.optDouble("longitude"));
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, httpServiceClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            progressDialog.dismiss();
            Toast.makeText(splashscreen.this, FinalResult, Toast.LENGTH_LONG).show();
        }
    }

}
