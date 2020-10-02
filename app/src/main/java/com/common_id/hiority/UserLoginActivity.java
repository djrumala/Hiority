package com.common_id.hiority;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class UserLoginActivity extends AppCompatActivity {
    EditText Email, Password;
    Button LogIn, Register, ForgotPass;

    String PasswordHolder, EmailHolder, RegHolder, ApiTokenHolder;
    String EmailShared; //taken from sharedpreference
    ProgressDialog progressDialog;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    String finalResult, resultForgot;
    private String IPAddress = "common-id.com";
    private String HttpURL = "http://" + IPAddress + "/hiority";

    public static final String MyPreferences = "Myprefs";
    //    public static final String GeneralPref = "Generalprefs";
    public static final String mId = "idKey";
    public static final String mName = "nameKey";
    public static final String mEmail = "emailKey";
    public static final String mPassword = "passwordKey";
    public static final String mStatus = "statusKey";

    private static final String TAG = UserLoginActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver; //FOR PUSH NOTIF

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
        setContentView(R.layout.activity_user_login);

        ScrollView view = findViewById(R.id.scrollView);

        Email = (AutoCompleteTextView) findViewById(R.id.email);
        Password = findViewById(R.id.password);
        LogIn = (Button) findViewById(R.id.login);

//        ForgotPass = findViewById(R.id.forgot_pass);

//        SharedPreferences pref = getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
//        String EmailHolder = pref.getString(mEmail, "0");
//        String StatusUserHolder = pref.getString(mStatus, "Not yet");

//        if (!EmailHolder.equals("0")) {
//            if (StatusUserHolder.equals("admin")) {
//                //User has filled everything, waiting for admin confirmation
//                finish();
//                Intent intent = new Intent(UserLoginActivity.this, AdminClassActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//            } else if (StatusUserHolder.equals("hiority")) {
//                finish();
//                Intent intent = new Intent(UserLoginActivity.this, HiorityDirectionActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//            } else if (StatusUserHolder.equalsIgnoreCase("smartpole")) {
//                finish();
//                Intent intent = new Intent(UserLoginActivity.this, HiorityDirectionActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//            } else {
//                //User has verified its email, allowed to login to fill user data completely
//                finish();
//                Intent intent = new Intent(UserLoginActivity.this, UserMainActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//            }
//        }

        //After clicking go on keyboard, user will be directed to home dashboard
        Password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
//                    InputMethodManager inputManager = (InputMethodManager)
//                            getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
                    CheckEditTextIsEmptyOrNot();
                    return true;
                }
                return false;
            }
        });

        //FOCUS AFTER CLICKED
        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To hide keyboard after clicked Login
//                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);

                CheckEditTextIsEmptyOrNot(); //Checking texts on each edittext
//                finish();
//                Intent intent = new Intent(UserLoginActivity.this, SupplierMainActivity.class);
//                startActivity(intent);

            }
        });

//        Register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //To hide keyboard after clicked Login
////                Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
////                startActivity(intent);
////                overridePendingTransition(0, 0);
//            }
//        });

//        ForgotPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //To hide keyboard after clicked Login
////                ForgotPassDialog();
//                Toast.makeText(UserLoginActivity.this,"This feature will be available soon", Toast.LENGTH_SHORT).show();
//            }
//        });

//        //TODO: FIREBASE NOTIFICATION
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                // checking for type intent filter
//                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
//                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
////                    displayFirebaseRegId();
//
//                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                    // new push notification is received
////                    String message = intent.getStringExtra("message");
////                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//                }
//            }
//        };

//        displayFirebaseRegId();
    }

//    private void displayFirebaseRegId() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        String regId = pref.getString("regId", null);
//
//        Log.e(TAG, "Firebase reg id: " + regId);
//
//        if (!TextUtils.isEmpty(regId))
////            Toast.makeText(UserLoginActivity.this, "Firebase Reg Id: " + regId, Toast.LENGTH_LONG).show();
//            RegHolder = regId;
//        else
//            Toast.makeText(UserLoginActivity.this, "Firebase Reg Id is not received yet!", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // register GCM registration complete receiver
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(Config.REGISTRATION_COMPLETE));
//
//        // register new push message receiver
//        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(Config.PUSH_NOTIFICATION));
//
//        // clear the notification area when the app is opened
//        NotificationUtils.clearNotifications(getApplicationContext());
//    }
//
//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        super.onPause();
//    }

//    private void InternetConnetion () {
//        //Cheking internet connection
//        new CheckNetworkConnection(this, new CheckNetworkConnection.OnConnectionCallback() {
//            @Override
//            public void onConnectionSuccess() {
//                new ParseJSonDataClass(UserLoginActivity.this).execute();
//            }
//
//            @Override
//            public void onConnectionFail(String msg) {
//                Toast.makeText(HistoryActivity.this, "Tidak ditemukan koneksi internet", Toast.LENGTH_LONG).show();
//
//            }
//        }).execute();
//    }


    //   CHECKING NETWORK CONNECTION, CREDIT: https://stackoverflow.com/questions/9570237/android-check-internet-connection
    public static class CheckNetworkConnection extends AsyncTask<Void, Void, Boolean> {
        private OnConnectionCallback onConnectionCallback;
        private Context context;

        public CheckNetworkConnection(Context con, OnConnectionCallback onConnectionCallback) {
            super();
            this.onConnectionCallback = onConnectionCallback;
            this.context = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (context == null)
                return false;

            boolean isConnected = new NetWorkInfoUtility().isNetWorkAvailableNow(context);
            return isConnected;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);

            if (b) {
                onConnectionCallback.onConnectionSuccess();
            } else {
                String msg = "No Internet Connection";
                if (context == null)
                    msg = "Context is null";
                onConnectionCallback.onConnectionFail(msg);
            }

        }

        public interface OnConnectionCallback {
            void onConnectionSuccess();

            void onConnectionFail(String errorMsg);
        }
    }

    static class NetWorkInfoUtility {

        public boolean isWifiEnable() {
            return isWifiEnable;
        }

        public void setIsWifiEnable(boolean isWifiEnable) {
            this.isWifiEnable = isWifiEnable;
        }

        public boolean isMobileNetworkAvailable() {
            return isMobileNetworkAvailable;
        }

        public void setIsMobileNetworkAvailable(boolean isMobileNetworkAvailable) {
            this.isMobileNetworkAvailable = isMobileNetworkAvailable;
        }

        private boolean isWifiEnable = false;
        private boolean isMobileNetworkAvailable = false;

        public boolean isNetWorkAvailableNow(Context context) {
            boolean isNetworkAvailable = false;

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            setIsWifiEnable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
            setIsMobileNetworkAvailable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());

            if (isWifiEnable() || isMobileNetworkAvailable()) {
        /*Sometime wifi is connected but service provider never connected to internet
        so cross check one more time*/
                if (isOnline())
                    isNetworkAvailable = true;
            }

            return isNetworkAvailable;
        }

        public boolean isOnline() {
            /*Just to check Time delay*/
            long t = Calendar.getInstance().getTimeInMillis();

            Runtime runtime = Runtime.getRuntime();
            try {
                /*Pinging to Google server*/
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                long t2 = Calendar.getInstance().getTimeInMillis();
                Log.i("NetWork check Time", (t2 - t) + "");
            }
            return false;
        }
    }


    public void CheckEditTextIsEmptyOrNot() {
        boolean cancel = false;
        View focusView = null;

        Email.setError(null);//reset error
        Password.setError(null);

        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();

//        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        String regId = pref.getString("regId", null);
//
//        Log.e(TAG, "Firebase reg id: " + regId);

//        RegHolder = regId;

        if (TextUtils.isEmpty(PasswordHolder) || PasswordHolder.length() < 6) {
            Password.setError("Password sekurangnya 6 karakter");
            focusView = Password;
            cancel = true;
        }
        if (TextUtils.isEmpty(EmailHolder)) {
            Email.setError("Email tidak boleh kosong");
            focusView = Email;
            cancel = true;
        }
        if (cancel == true) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        if (cancel == false) {

            ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
//                UserLoginFunction(EmailHolder, PasswordHolder); //memanggil fungsi login
                new SaveDataClass(UserLoginActivity.this).execute();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myAlertDialogTheme));
                builder.setTitle("Error");
                builder.setMessage("Koneksi Internet Tidak ditemukan");
                builder.setCancelable(true);

                builder.setNegativeButton("Coba Lagi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        }
    }

    private class SaveDataClass extends AsyncTask<Void, Void, Void> {
        public Context context;
        String FinalResult, UserStatus = "No Status";
        ProgressDialog progressDialog;

        public SaveDataClass(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UserLoginActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Hiority");
            progressDialog.setMessage("Loading Data...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpServiceClass httpServiceClass = null;

            httpServiceClass = new HttpServiceClass(HttpURL + "/login.php");
            httpServiceClass.AddParam("email", EmailHolder);
            httpServiceClass.AddParam("password", PasswordHolder);

            try {
                httpServiceClass.ExecutePostRequest();
                if (httpServiceClass.getResponseCode() == 200) {
                    FinalResult = httpServiceClass.getResponse().trim();

                    SharedPreferences pref = getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    try {
                        JSONArray Jarray = new JSONArray(FinalResult);

                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object = Jarray.getJSONObject(i);
                            UserStatus = object.getString("status");
                            editor.putString(mEmail, EmailHolder);
                            editor.putString(mPassword, PasswordHolder);
                            editor.putString(mId, object.getString("id"));
                            editor.putString(mName, object.getString("nama"));
                            editor.putString(mStatus, UserStatus);
                            editor.apply();
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
            progressDialog.dismiss();
//            Toast.makeText(context, UserStatus, Toast.LENGTH_SHORT).show();

//            finish();
//            Intent intent = new Intent(UserLoginActivity.this, MapsActivity.class);
//            startActivity(intent);
//            overridePendingTransition(0, 0);
            if (FinalResult.equals("Email atau password salah. Silakan coba lagi"))
                Toast.makeText(context, FinalResult, Toast.LENGTH_SHORT).show();
            else
                new GetJSONNode().execute();
        }
    }

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
//            Log.e("Node", String.valueOf(jsonobject));
//            Toast.makeText(splashscreen.this, String.valueOf(jsonobject), Toast.LENGTH_LONG).show();
            finish();
            Intent i = new Intent(UserLoginActivity.this, MapsActivity.class);
            i.putExtra("nolat", nodelatitude);
            i.putExtra("nolong", nodelongitude);
            i.putExtra("noid", nodeid);
            i.putExtra("noname", nodename);
            i.putExtra("node_a", nodea);
            i.putExtra("node_b", nodeb);
            if (nodelatitude != null) {
                startActivity(i); // menghubungkan activity splashscren ke main activity dengan intent
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        overridePendingTransition(0, 0);//go back without any history and when you enter the apps again, you can go this activity again not profile

    }
}
