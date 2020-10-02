package com.common_id.hiority;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, TextToSpeech.OnInitListener {
    private GoogleMap mMap;
    private RelativeLayout inf, actionbar;
    private LatLng latLong;
    private LinearLayout infoatas;
    private Marker mark, marker2, markawal, marksource;
    private View mapView;
    private TextView jarak, index, startpoint, endpoint, l, lati, lngi, angel;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private ImageButton currr, but_tujuan, navigate, set;
    private ImageView lurus, kanan, kiri, steplurus, stepkanan, stepkiri, ispu;
    double lats, prevdist, longs, lat = 0, lng, latatata, longitata, latini, longini, lalo, lola;
    LatLng koordinat, koordinat2;
    int mapclickflag = 0, complete = 0, min;
    private MarkerOptions markerOptions;
    final LatLng ITS = new LatLng(-7.276407, 112.79036);
    ProgressBar pbar;
    AlertDialog levelDialog;
    BitmapDescriptor marks, markd, markess;
    ArrayList<Double> nodelatitude;
    ArrayList<Double> nodelongitude, distance;
    ArrayList<String> nodename;
    ArrayList<String> nodea;
    ArrayList<String> nodeb;
    ArrayList<Integer> nodeid;
    ArrayList<LatLng> way, waycoba;
    ArrayList<Integer> route, reverseroute, routecoba, velocity;
    Polyline lineback;
    List<Polyline> line;
    JSONObject jsonobject, jalurobject, jarakobject, velobject;
    int noawal, notuj, minakhir, cost, position = 0;
    double dst, jaraktemp = 0, prevtemp = 0;
    boolean prioritymode = false, normalmode=false;
    //    GPSTracker gps;
    private TextToSpeech tts;

//    // LogCat tag
//    private static final String TAG = MapsActivity.class.getSimpleName();
//    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
//    private Location mLastLocation;
//    // Google client to interact with Google API
//    private GoogleApiClient mGoogleApiClient;
//
//    // boolean flag to toggle periodic location updates
//    private boolean mRequestingLocationUpdates = false;
//
//    private LocationRequest mLocationRequest;
//
//    // Location updates intervals in sec
//    private int UPDATE_INTERVAL = 5000; // 10 sec
//    private static int FATEST_INTERVAL = 5000; // 5 sec
//    private static float DISPLACEMENT = 0.25F; // 1 meters

    private boolean mLocationPermissionGranted = false;
    private Integer PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 111;

    private static final String TAG = MapsActivity.class.getSimpleName();

    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;
    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    // UI elements
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private LocationManager mlocManager;
    private LocationListener mlocListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        nodename = (ArrayList<String>) getIntent().getSerializableExtra("noname");
        nodea = (ArrayList<String>) getIntent().getSerializableExtra("node_a");
        nodeb = (ArrayList<String>) getIntent().getSerializableExtra("node_b");
        nodelatitude = (ArrayList<Double>) getIntent().getSerializableExtra("nolat");
        nodelongitude = (ArrayList<Double>) getIntent().getSerializableExtra("nolong");
        nodeid = (ArrayList<Integer>) getIntent().getSerializableExtra("noid");
        tts = new TextToSpeech(this, this);

        Log.e("Line", String.valueOf(nodea.size()));

//        if (checkPlayServices()) {
//
//            // Building the GoogleApi client
//            buildGoogleApiClient();
//
//            createLocationRequest();
//        }

//        GPS();
//        loadlocation();

        Toast.makeText(this, "Sentuh Peta", Toast.LENGTH_SHORT).show();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapView = mapFragment.getView();

        init(); //Needed for GPS init to get current place
        // restore the values from saved instance state
        restoreValuesFromBundle(savedInstanceState);
        startLocationButtonClick();
//        new ParseJSonDataClass(this).execute();
        getLocationPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Get the bundle
        inisialisasi();
        complete = 0;

        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(ITS, 13);
        mMap.moveCamera(cameraPosition);
        mMap.animateCamera(cameraPosition);

        //waycoba = new ArrayList<LatLng>();
//        for (int i = 0; i < nodelatitude.size(); i++) {
//            waycoba.add(new LatLng(nodelatitude.get(i), nodelongitude.get(i)));
//            mMap.addMarker(new MarkerOptions().position(new LatLng(nodelatitude.get(i), nodelongitude.get(i)))
//                    .title(String.valueOf(nodeid.get(i)))
//                    .snippet("Pilih")
//                    .icon(markd));
//        }

//        PolylineOptions rectLine = new PolylineOptions().width(10).color(
//                Color.RED);
//        LatLng aLoc = null, bLoc = null;
//        for (int i = 0; i < nodea.size(); i++) {
//            String[] Alatlong = nodea.get(i).split(",");
//            double aLat = Double.parseDouble(Alatlong[0]);
//            double aLon = Double.parseDouble(Alatlong[1]);
//            aLoc = new LatLng(aLat, aLon);
//            String[] Blatlong = nodeb.get(i).split(",");
//            double bLat = Double.parseDouble(Blatlong[0]);
//            double bLon = Double.parseDouble(Blatlong[1]);
//            bLoc = new LatLng(bLat, bLon);
//            rectLine.add(aLoc, bLoc);
//        }
//        mMap.addPolyline(rectLine);

//        PolylineOptions lineOptions=new PolylineOptions();
//        lineOptions.addAll(waycoba);
//        lineOptions.width(10);
//        lineOptions.color(Color.rgb(69, 134, 71));
//        line= Collections.singletonList(mMap.addPolyline(lineOptions));

        Mapclick(markd);
        // loadlocation();
        //GPS();
//        curloc();
        if (latLong != null) {
            markeroption(latLong, marks, "Lokasi Anda");
            markawal = mMap.addMarker(markerOptions);
        } else {
            Log.e("TTS", "Get Location Failed!");
        }

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
//            new HiorityDirectionActivity.ParseJSonDataClass(MapsActivity.this).execute();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Anda menyetujui izin lokasi", Toast.LENGTH_SHORT).show();
                mLocationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setCompassEnabled(false);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
        //move the position of direction
        mMap.setPadding(0, 0, 30, 175);
        //Set button position of google maps on right bottom
        if (mMap != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 185);

        }

        if (mCurrentLocation != null) { //GET CURRENT LOCATION BY GPS
            latLong = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
//            mMap.animateCamera(cameraUpdate);

            min = dekat(latLong.latitude, latLong.longitude);
            if (min != 0 && latLong != null) {
                noawal = nodeid.get(min);
            }

            if (marksource != null)
                marksource.remove(); //remove last marker of current location
            Bitmap bitmap = resizeMapIcons("mark_src", 48, 48);
            marksource = mMap.addMarker(new MarkerOptions().position(latLong).title("Current Location").
                    icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

            if (complete == 2) {
                for (Polyline polyline : line) {
                    polyline.remove();
                }
                line.clear();
                lineback.remove();

//            markawal.remove();
//            markeroption(latLong, marks, "Lokasi Anda");
//            markawal = mMap.addMarker(markerOptions);

//                way.remove(way.size() - 1);

                float bear = (float) bear(latLong, way.get(1));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
                // Zoom in the Google Map
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
// Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(70), 2000, null);
// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(mMap.getCameraPosition())
                        .zoom(17)                   // Sets the zoom
                        .bearing(bear)                // Sets the orientation of the camera to east
                        .tilt(80)                   // Sets the tilt of the camera to 30 degrees
                        .build()
                ));

                drawroute(latLong);
            }
//                    icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_src)));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

//            String address = null, locality = null, subLocality = null, province = null, city = null, country, postalCode = null, knownName;
//            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//            List<Address> addresses;
//            try {
//                addresses = gcd.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation
//                        .getLongitude(), 1);
//                if (addresses.size() > 0)
//                    System.out.println(addresses.get(0).getLocality());
//                //cityName=addresses.get(0).getLocality();
//                //address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                locality = addresses.get(0).getLocality();
//                subLocality = addresses.get(0).getSubLocality();
//                province = addresses.get(0).getAdminArea();
//                address = addresses.get(0).getThoroughfare();
//                city = addresses.get(0).getSubAdminArea();
//                country = addresses.get(0).getCountryName();
//                postalCode = addresses.get(0).getPostalCode();
//                knownName = addresses.get(0).getFeatureName();
////                if (subLocality != null) {
////                    locality = locality + "," + subLocality;
////                } else {
////                    locality = locality;
////                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            String s = "Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude();
//            String s = "Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude()+
//                    "\nMy Currrent City is: " + address + ", " + subLocality + ", " + locality + ", " + province + city;
            Log.e(TAG, s);
//            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

//                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(MapsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }

    public void stopLocationButtonClick() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
//                        toggleButtons();
                    }
                });
    }

    public void showLastKnownLocation() {
        if (mCurrentLocation != null) {
            Toast.makeText(getApplicationContext(), "Lat: " + mCurrentLocation.getLatitude()
                    + ", Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Last known location is not available!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }

        updateLocationUI();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

//    private void loadlocation() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLastLocation = LocationServices.FusedLocationApi
//                .getLastLocation(mGoogleApiClient);
//
//        if (mLastLocation != null) {
//            double latitude = mLastLocation.getLatitude();
//            double longitude = mLastLocation.getLongitude();
//            if (latitude != 0) {
//                latLong = new LatLng(latitude, longitude);
//            } else {
//                // loadlocation();
//            }
//        } else {
//
//        }
//    }

//    public void GPS() {
//        gps = new GPSTracker(MapsActivity.this);
//
////        ActionBar abar= getSupportActionBar();
////        abar.setHomeButtonEnabled(true);
////        abar.setDisplayHomeAsUpEnabled(true);
//        // check if GPS enabled
//        if (gps.canGetLocation()) {
//            if (gps.getLatitude() != 0) {
//                latLong = new LatLng(gps.getLatitude(), gps.getLongitude());
//                min = dekat(latLong.latitude, latLong.longitude);
//                if (min != 0 && latLong != null) {
//                    noawal = nodeid.get(min);
//                }
//            } else {
//                // GPS();
//
//                Toast.makeText(MapsActivity.this, "Gangguan Koneksi", Toast.LENGTH_SHORT).show();
//            }
//            // \n is for new line
//        } else {
//            // can't get location
//            // GPS or Network is not enabled
//            // Ask user to enable GPS/network in settings
//            gps.showSettingsAlert();
//        }
//    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut() {

    }

    private class Pathing extends AsyncTask<Void, Void, Void> {
        String TimeHolder;

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the WorldPopulation Class
            route = new ArrayList<Integer>();
            way = new ArrayList<LatLng>();
            distance = new ArrayList<Double>();
            velocity = new ArrayList<Integer>();
            // Create an array to populate the spinner
            // JSON file URL address
            String url = "?src=" + noawal + "&dst=" + notuj;
//            jsonobject = JSONParser.getJSONfromURL("http://computer-its.com/TA/Faishol/pathfinding.php" + url);
//            jsonobject = JSONParser.getJSONfromURL("http://computer-its.com/kecepatan/SmartNavigation/pathfinding.php" + url);
            jsonobject = JSONParser.getJSONfromURL("http://common-id.com/hiority/newtime.php" + url);
//            jsonobject = JSONParser.getJSONfromURL("http://common-id.com/hiority/pathfinding.php" + url);
//            jsonobject = JSONParser.getJSONfromURL("http://computer-its.com/TA/SmartNavigation/pathfinding.php" + url);
            try {
//                // Locate the NodeList name
//                cost.add(jsonobject.optInt("cost"));
//                jsonarray = jsonobject.getJSONArray("path");

                way.add(latLong);

                jalurobject = jsonobject.getJSONObject("jalur");
//                velobject = jsonobject.getJSONObject("index");
                velobject = jsonobject.getJSONObject("kecepatan");
                jarakobject = jsonobject.getJSONObject("jarak");
                TimeHolder = jsonobject.getString("total waktu");
                JSONObject latobject = jsonobject.getJSONObject("latitude");
                JSONObject lngobject = jsonobject.getJSONObject("longitude");
                //way.add(new LatLng(nodelatitude.get(minakhir), nodelongitude.get(minakhir)));
                for (int a = 0; a < jarakobject.length(); a++) {
                    distance.add(jarakobject.optDouble(String.valueOf(a)));
                    velocity.add(velobject.optInt(String.valueOf(a)));
                }
                for (int i = 0; i < jalurobject.length(); i++) {
                    route.add(jalurobject.optInt(String.valueOf(i)));
                }
                for (int i = 0; i < route.size(); i++) {
                    way.add(new LatLng(latobject.optDouble(String.valueOf(i)), lngobject.optDouble(String.valueOf(i))));
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {
//        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
//        mySpinner.setAdapter(new ArrayAdapter<LatLng>(MapsActivity.this,
//                android.R.layout.simple_spinner_dropdown_item,
//                way));

            waycoba = new ArrayList<LatLng>(way);
            routecoba = new ArrayList<Integer>(route);
            drawroute(latLong);
            index.setText(TimeHolder);
        }
    }

//    public void upin() {
//        final CharSequence[] items = {" 1 detik ", " 2 detik", " 3 detik ", " 4 detik", " 5 detik"};
//
//        // Creating and Building the Dialog
//        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
//        builder.setTitle("Update Interval");
//        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//
//                switch (item) {
//                    case 0:
//                        UPDATE_INTERVAL = 1000;
//                        FATEST_INTERVAL = 1000;
//                        break;
//                    case 1:
//                        UPDATE_INTERVAL = 2000;
//                        FATEST_INTERVAL = 2000;
//                        break;
//                    case 2:
//                        UPDATE_INTERVAL = 3000;
//                        FATEST_INTERVAL = 3000;
//                        break;
//                    case 3:
//                        UPDATE_INTERVAL = 4000;
//                        FATEST_INTERVAL = 4000;
//                        break;
//                    case 4:
//                        UPDATE_INTERVAL = 5000;
//                        FATEST_INTERVAL = 5000;
//                        break;
//
//                }
//                levelDialog.dismiss();
//            }
//        });
//        levelDialog = builder.create();
//        levelDialog.show();
//    }


//    public void setting_button(View view) {
//        PopupMenu popup = new PopupMenu(MapsActivity.this, set);
//        //Inflating the Popup using xml file
//        popup.getMenuInflater().inflate(R.menu.menu_setting, popup.getMenu());
//
//        //registering popup with OnMenuItemClickListener
//        //registering popup with OnMenuItemClickListener
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.tentang:
//                        Intent aboutIntent = new Intent(MapsActivity.this, about.class);
//                        startActivity(aboutIntent);
//                        return true;
//                    case R.id.upin:
//                        upin();
//                        return true;
//                    default:
//                        return true;
//                }
//            }
//        });
//
//        popup.show();//showing popup menu
//    }

    public void drawroute(LatLng latLong) {
        way.remove(0);
        way.add(0, latLong);

        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.addAll(way);
        lineOptions.width(15);
        lineOptions.color(Color.rgb(69, 134, 71));
        lineback = mMap.addPolyline(lineOptions);

//        mySpinner2.setAdapter(new ArrayAdapter<LatLng>(MapsActivity.this,
//                android.R.layout.simple_spinner_dropdown_item,
//                way));
//        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
//
//        mySpinner.setAdapter(new ArrayAdapter<LatLng>(MapsActivity.this,
//                android.R.layout.simple_spinner_dropdown_item,
//                way));

        double km = 0;
        line = new ArrayList<Polyline>();
        int color = Color.rgb(69, 134, 71);
        for (int i = 0; i < distance.size(); i++) {
            int in = velocity.get(i);
            km = km + distance.get(i) / 1000;
            if (in > 0 && in <= 15) {
                color = Color.rgb(204, 0, 0);
            }
            if (in > 15 & in <= 25) {
                color = Color.rgb(242, 30, 4);
            }
            if (in > 25 && in <= 30) {
                color = Color.rgb(242, 191, 0);
            }
            if (in > 30 && in <= 40) {
                color = Color.rgb(72, 198, 13);
            }
            if (in >= 40) {
                color = Color.rgb(13, 198, 20);
            }

            if (way.size() > 2) {
                line.add(mMap.addPolyline(new PolylineOptions()
                        .add(way.get(i + 1), way.get(i + 2))
                        .width(5)
                        .color(color)));
            } else {
                line.add(mMap.addPolyline(new PolylineOptions()
                        .add(latLong, way.get(0))
                        .width(5)
                        .color(color)));
            }
        }

//        index.setText(String.valueOf(ppm));
        jarak.setText(String.valueOf(String.format("%.2f", km)));
        if (complete == 2) {
            voicenav();
        }
    }

    public void voicenav() {
        LatLng prevnode;
        LatLng nextnode = way.get(1); //persimpangan pertama, way(0) adalah lokasi saat ini lihat way.add(0, latlong)

        double jarak1 = haversine(way.get(0).latitude, way.get(0).longitude, nextnode.latitude, nextnode.longitude);

        Toast.makeText(this, route.get(0) + "Jarak: " + String.valueOf(jarak1), Toast.LENGTH_LONG).show();
        String header = "";
        kiri.setVisibility(View.GONE);
        kanan.setVisibility(View.GONE);
        lurus.setVisibility(View.VISIBLE);

        if (position > 0) {
            if(!normalmode) {
                prevnode = waycoba.get(position);
                prevdist = haversine(latLong.latitude, latLong.longitude, prevnode.latitude, prevnode.longitude);

                if (prevtemp < prevdist && prevtemp != 0) {
                    SendData("0", "0", String.valueOf(routecoba.get(position - 1)));
                    normalmode=true;
                }
            }
        }

        double angle = 99999; //Initialize angle value

        if (way.size() > 2)
            angle = sudut(latLong, way.get(1), way.get(2));

        if (jaraktemp < jarak1 && jaraktemp != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak("Wrong way", TextToSpeech.QUEUE_FLUSH, null, "id2");
            }
            //SendData("N", "0", String.valueOf(route.get(position)));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak("Go Straight", TextToSpeech.QUEUE_FLUSH, null, "id2");
            }
        }

        jaraktemp = jarak1;
        prevtemp = prevdist;

        if (angle == 99999) {
            header = "You will reach destination soon";
        } else {
            if (angle < 0) {
                header = "TURN LEFT";
                stepkiri.setVisibility(View.VISIBLE);
                stepkanan.setVisibility(View.GONE);
            }
            if (angle > 0) {
                header = "TURN RIGHT";
                stepkiri.setVisibility(View.GONE);
                stepkanan.setVisibility(View.VISIBLE);
            }
        }


        if (jarak1 < 200) {
//            SEND SIGNAL PRIORITY MODE TO TRAFFIC LIGHT
            String Direction = vehicleBearing(latLong, way.get(1));

            if (jarak1 > 50) {
                if(!prioritymode) {
                    if (position > 0) {
                        if (angle == 99999) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        tts.speak(header, TextToSpeech.QUEUE_FLUSH, null, "id1");
//                    }
//                    complete = complete - 1;
                        } else
                            Toast.makeText(this, route.get(0).toString() + Direction +
                                    String.valueOf(position) + " jarak: " + String.valueOf(jarak1)
                                    + "prevdist: " + String.valueOf(prevdist), Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(this, route.get(0).toString() + Direction + String.valueOf(position) + " jarak: " + String.valueOf(jarak1), Toast.LENGTH_LONG).show();

                    SendData(Direction, "1", route.get(0).toString());
                    prioritymode=true;
                }
            }

            else if (jarak1 < 50) {// jika jarak pengguna dengan titik kurang dari 45 meter ()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(header, TextToSpeech.QUEUE_FLUSH, null, "id1");
                }

                position++; //pointing to the next index

                if (angle < 0) {
                    kiri.setVisibility(View.VISIBLE);
                    kanan.setVisibility(View.GONE);
                    lurus.setVisibility(View.GONE);
                }

                if (angle > 0) {
                    kiri.setVisibility(View.GONE);
                    kanan.setVisibility(View.VISIBLE);
                    lurus.setVisibility(View.GONE);
                }

                if (angle == 99999) //Reaching last node
                {
//                stepkiri.setVisibility(View.GONE);
//                stepkanan.setVisibility(View.GONE);
//                steplurus.setVisibility(View.GONE);
//                kiri.setVisibility(View.GONE);
//                kanan.setVisibility(View.GONE);
//                lurus.setVisibility(View.GONE);
//                actionbar.setVisibility(View.VISIBLE);
//                navigate.setVisibility(View.VISIBLE);
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
//                // Zoom in the Google Map
//                mMap.animateCamera(CameraUpdateFactory.zoomIn());
//// Zoom out to zoom level 10, animating with a duration of 2 seconds.
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(100), 2000, null);
//// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
//                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(mMap.getCameraPosition())
//                        .zoom(13)                   // Sets the zoom
//                        .bearing(0)                // Sets the orientation of the camera to east
//                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
//                        .build()));
                    complete = complete - 1;
                } else {
                    way.remove(1);
                    distance.remove(0);
                    velocity.remove(0);
                    route.remove(0); //added on 21/05/2019
                    prioritymode=false;
                    normalmode=false;
                }
            }
        }


    }

    public void SendData(final String Direction, final String Status, final String Node) {
        class SendStatClass extends AsyncTask<Void, Void, Void> {
            public Context context;
            String FinalResult;
            ProgressDialog progressDialog;

            public SendStatClass(Context context) {
                this.context = context;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                HttpServiceClass httpServiceClass = null;

                httpServiceClass = new HttpServiceClass("http://common-id.com/hiority/control.php");
                httpServiceClass.AddParam("node", Node);
                httpServiceClass.AddParam("stat", Status);
                httpServiceClass.AddParam("dir", Direction);

                try {
                    httpServiceClass.ExecuteGetRequest();
                    if (httpServiceClass.getResponseCode() == 200) {
                        FinalResult = httpServiceClass.getResponse().trim();

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
//                progressDialog.dismiss();
                if (Status.equals("1"))
                    Toast.makeText(MapsActivity.this, "Node " + Node + " Priority Mode " + FinalResult, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MapsActivity.this, "Node " + Node + " Normal Mode " + FinalResult, Toast.LENGTH_LONG).show();
            }
        }
        new SendStatClass(MapsActivity.this).execute();
    }


//    private void updatelocation() {
////        GPS();
////        loadlocation();
//        if (mCurrentLocation != null) {
//            for (Polyline polyline : line) {
//                polyline.remove();
//            }
//            line.clear();
//            lineback.remove();
//
////            markawal.remove();
////            markeroption(latLong, marks, "Lokasi Anda");
////            markawal = mMap.addMarker(markerOptions);
//
//            // way.remove(way.size() - 1);
//
//            float bear = (float) bear(latLong, way.get(1));
//
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
//            // Zoom in the Google Map
//            mMap.animateCamera(CameraUpdateFactory.zoomIn());
//// Zoom out to zoom level 10, animating with a duration of 2 seconds.
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(70), 2000, null);
//// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(mMap.getCameraPosition())
//                    .zoom(17)                   // Sets the zoom
//                    .bearing(bear)                // Sets the orientation of the camera to east
//                    .tilt(80)                   // Sets the tilt of the camera to 30 degrees
//                    .build()
//            ));
//
//            drawroute(latLong);
//        } else {
//
//        }
//    }

    public double bear(LatLng startpoint, LatLng endpoint) {
        double x1 = endpoint.latitude;
        double y1 = endpoint.longitude;
        double x2 = startpoint.latitude;
        double y2 = startpoint.longitude;
        double y = y1 - y2;
        double x = x1 - x2;
        double radians = Math.atan2(y, x);
        double compassReading = radians * (180 / Math.PI);

        return compassReading; //angle
    }

    public String vehicleBearing(LatLng startpoint, LatLng endpoint) {
        double x1 = endpoint.latitude;
        double y1 = endpoint.longitude;
        double x2 = startpoint.latitude;
        double y2 = startpoint.longitude;
        double y = y1 - y2;
        double x = x1 - x2;
        double radians = Math.atan2(y, x);

        double compassReading = radians * (180 / Math.PI);

        String coordNames[] = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

        double coordIndex = Math.round(compassReading / 45);
        if (coordIndex < 0) {
            coordIndex = coordIndex + 8;
        }

        if (coordIndex == 8) {
            coordIndex = 0;
        }

        return coordNames[(int) coordIndex]; // returns the coordinate value
    }

    private double GetAngle(LatLng p1, LatLng p2) {
        double p1x = p1.latitude;
        double p1y = p1.longitude;
        double p2x = p2.latitude;
        double p2y = p2.longitude;
        double m = Math.atan((p2y - p1y) / (p2x - p1x)); // where y = m * x + K
        return p2x - p1x < 0 ? m + Math.PI : m; // The will go to the correct Quadrant
    }

    private double NormalizeAngle(double angle) {
        return angle < 0 ? angle + 2 * Math.PI : angle; //This will make sure angle is [0..2PI]
    }

    private double sudut(LatLng current, LatLng center, LatLng previous) {
//        double theta1 = GetAngle(current, center);
//        double theta2 = GetAngle(center, previous);
//        double delta = NormalizeAngle(theta2 - theta1);
        double x1 = current.latitude;
        double y1 = current.longitude;
        double x2 = center.latitude;
        double y2 = center.longitude;
        double x3 = previous.latitude;
        double y3 = previous.longitude;
        double v1x = x2 - x1;
        double v1y = y2 - y1;
        double v2x = x3 - x2;
        double v2y = y3 - y2;
        double hasil = (v1x * v2y) - (v1y * v2x);
        return hasil;

//        double P2X= current.latitude;
//        double P2Y = current.longitude;
//        double P1X=center.latitude;
//        double P1Y=center.longitude;
//        double P3X=previous.latitude;
//        double P3Y=previous.longitude;
//
//        double numerator = P2Y*(P1X-P3X) + P1Y*(P3X-P2X) + P3Y*(P2X-P1X);
//        double denominator = (P2X-P1X)*(P1X-P3X) + (P2Y-P1Y)*(P1Y-P3Y);
//        double ratio = numerator/denominator;
//
//        double angleRad = Math.atan(ratio);
//        double angleDeg = (angleRad*180)/Math.PI;
//
//        if(angleDeg<0){
//            angleDeg = 360+angleDeg;
//        }
//        return angleDeg;

        //2
//        double dy = center.longitude - previous.longitude;
//        double dx = center.latitude - previous.latitude;
//        double theta = Math.atan2(dy, dx); // range (-PI, PI]
//        theta *= 180 / Math.PI; // rads to degs, range (-180, 180]
//        if (theta < 0) theta = 360 + theta; // range [0, 360)
//        return theta;
    }

    public void set_dest(View view) {
        if (koordinat2 != null) {

            actionbar.setVisibility(View.VISIBLE);
            notuj = nodeid.get(minakhir);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
            startpoint.setText(String.valueOf(nodename.get(minakhir)));
            endpoint.setText(String.valueOf(nodename.get(min)));
            new Pathing().execute();
            but_tujuan.setVisibility(View.GONE);
            inf.setVisibility(View.VISIBLE);
            infoatas.setVisibility(View.VISIBLE);
            navigate.setVisibility(View.VISIBLE);
            pbar.setVisibility(View.GONE);

            mapclickflag = 0;
            Mapclick(markd);
            markeroption(koordinat2, markd, nodename.get(minakhir));
            marker2 = mMap.addMarker(markerOptions);

            complete++;


        } else {
            Toast.makeText(this, "Pilih Lokasi Tujuan", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Sentuh Peta", Toast.LENGTH_SHORT).show();
        }
    }

    private void markeroption(LatLng koor, BitmapDescriptor marker, String nama) {
        markerOptions = new MarkerOptions().position(koor)
                .title(nama)
                .snippet("Pilih")
                .icon(marker);
    }

    public static double haversine(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);
        return dist;
    }

    public int findMinIdx(int[] numbers) {
        if (numbers == null || numbers.length == 0) return -1; // Saves time for empty array
        // As pointed out by ZouZou, you can save an iteration by assuming the first index is the smallest
        int minVal = numbers[0]; // Keeps a running count of the smallest value so far
        int minIdx = 0; // Will store the index of minVal
        for (int idx = 1; idx < numbers.length; idx++) {
            if (numbers[idx] < minVal) {
                minVal = numbers[idx];
                minIdx = idx;
            }
        }
        return minIdx;
    }

    public int dekat(double lats, double longs) {
        int min;
        int[] dists = new int[nodelatitude.size()];
        for (int i = 0; i < nodelatitude.size(); i++) {
            latatata = nodelatitude.get(i);
            longitata = nodelongitude.get(i);
            dists[i] = (int) haversine(lats, longs, latatata, longitata);
        }
        min = findMinIdx(dists);
        return min;
    }

    protected void Mapclick(final BitmapDescriptor markers) {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                if (nodelatitude != null) {
                    lats = point.latitude;
                    longs = point.longitude;
                    minakhir = dekat(lats, longs);
                    latini = nodelatitude.get(minakhir);
                    longini = nodelongitude.get(minakhir);
                    if (latini != 0) {
                        if (complete == 0) {
                            koordinat2 = new LatLng(latini, longini);
                            markeroption(koordinat2, markers, nodename.get(minakhir));
                        }
                        if (complete != 1) {
                            if (mapclickflag == 0) {
                                if (mark != null) {
                                    mark.remove();
                                    mark = mMap.addMarker(markerOptions);
                                    mapclickflag++;
                                } else {
                                    mark = mMap.addMarker(markerOptions);
                                    mapclickflag++;
                                }
                            } else {
                                mark.remove();
                                mark = mMap.addMarker(markerOptions);
                                mapclickflag++;
                            }
                        } else {
                        }
                    } else {
                        Toast.makeText(MapsActivity.this, "Tidak Dapat Sambung ke Database", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MapsActivity.this, "Gangguan Koneksi", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void inisialisasi() {
//         ispu=(ImageView)findViewById(R.id.ispu);
//        l=(TextView)findViewById(R.id.sas);
        steplurus = (ImageView) findViewById(R.id.steplurus);
        stepkanan = (ImageView) findViewById(R.id.stepkanan);
        stepkiri = (ImageView) findViewById(R.id.stepkiri);
        lurus = (ImageView) findViewById(R.id.lurus);
        kanan = (ImageView) findViewById(R.id.kanan);
        kiri = (ImageView) findViewById(R.id.kiri);
        // angel = (TextView) findViewById(R.id.sudut);
        marks = BitmapDescriptorFactory.fromResource(R.drawable.mark_src);
        markd = BitmapDescriptorFactory.fromResource(R.drawable.mark_dest);
        set = (ImageButton) findViewById(R.id.set_button);
        actionbar = (RelativeLayout) findViewById(R.id.action_bar);
        navigate = (ImageButton) findViewById(R.id.navigate);
        pbar = (ProgressBar) findViewById(R.id.pbar);
        markess = BitmapDescriptorFactory.fromResource(R.drawable.marker);
        inf = (RelativeLayout) findViewById(R.id.info);
        infoatas = (LinearLayout) findViewById(R.id.infatas);
        currr = (ImageButton) findViewById(R.id.curloc);
        but_tujuan = (ImageButton) findViewById(R.id.but_tujuan);
        index = (TextView) findViewById(R.id.kadar);
        jarak = (TextView) findViewById(R.id.jarak);
        startpoint = (TextView) findViewById(R.id.endpoint);
        endpoint = (TextView) findViewById(R.id.startpoint);

        stepkanan.setVisibility(View.GONE);
        steplurus.setVisibility(View.GONE);
        stepkiri.setVisibility(View.GONE);
        kanan.setVisibility(View.GONE);
        lurus.setVisibility(View.GONE);
        kiri.setVisibility(View.GONE);
        infoatas.setVisibility(View.GONE);
        actionbar.setVisibility(View.GONE);
        inf.setVisibility(View.GONE);
        navigate.setVisibility(View.GONE);
        but_tujuan.setVisibility(View.VISIBLE);
        pbar.setVisibility(View.GONE);
        // ispu.setVisibility(View.GONE);
    }

    //
    public void navigate(View view) {
        complete = complete + 1;
        view.startAnimation(buttonClick);
        inf.setVisibility(View.VISIBLE);
        navigate.setVisibility(View.GONE);
        currr.setVisibility(View.GONE);

        // Show the current location in Google Map
//        togglePeriodicLocationUpdates();

//        updatelocation();
    }

    //
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        checkPlayServices();
//
//        // Resuming the periodic location updates
//        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        stopLocationUpdates();
//    }
//
//    /**
//     * Method to display the location on UI
//     */
//
//


    //    /**
//     * Method to toggle periodic location updates
//     */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }
//
//    /**
//     * Creating google api client object
//     */
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//    }
//
//    /**
//     * Creating location request object
//     */
//    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
//    }
//
//    /**
//     * Method to verify google play services on the device
//     */
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "This device is not supported.", Toast.LENGTH_LONG)
//                        .show();
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * Starting the location updates
//     */
//    protected void startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
//
//    }
//
//    /**
//     * Stopping location updates
//     */
//    protected void stopLocationUpdates() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(
//                mGoogleApiClient, this);
//    }
//
//    /**
//     * Google api callback methods
//     */
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
//                + result.getErrorCode());
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//
//        startLocationUpdates();
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int arg0) {
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        // Assign the new location
//        mLastLocation = location;
//
//        Toast.makeText(getApplicationContext(), "Location changed!",
//                Toast.LENGTH_SHORT).show();
//
//        // Displaying the new location on UI
//        if (complete >= 2)
//            updatelocation();
//    }
//
//    public void curloc() {
//        if (latLong != null) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
//            // Zoom in the Google Map
//            mMap.animateCamera(CameraUpdateFactory.zoomIn());
//// Zoom out to zoom level 10, animating with a duration of 2 seconds.
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
//        } else {
//            Toast.makeText(getApplicationContext(), "Tidak Mendapatkan Lokasi",
//                    Toast.LENGTH_SHORT).show();
//
//            if (markawal != null) {
//                markeroption(latLong, marks, "Lokasi Anda");
//                markawal = mMap.addMarker(markerOptions);
//            }
//
//        }
//    }
//
//    public void curr(View view) {
//        if (latLong != null && latLong.latitude != 0 && markawal == null) {
//            markeroption(latLong, marks, "Lokasi Anda");
//            markawal = mMap.addMarker(markerOptions);
//        } else {
//            Log.e("TTS", "Get Location Failed!");
//        }
//        curloc();
//
//    }

    //    @Override
//    public void onConnected(Bundle bundle) {
////        Toast.makeText(this, "Sentuh Peta", Toast.LENGTH_SHORT).show();
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
////            return;
////        }
////        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
////                mGoogleApiClient);
////        if (mLastLocation != null) {
////            //place marker at current position
////            //mGoogleMap.clear();
////            lastlocation = new LatLng( mLastLocation.getLatitude(), mLastLocation.getLongitude());
////        }
////        locationRequest = new LocationRequest();
////        locationRequest.setInterval(5000); //5 seconds
////        locationRequest.setFastestInterval(3000); //3 seconds
////        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
////        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
////        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
//
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // your code
            if (complete == 0) {
//                this.finish();
                this.finishAffinity();
            } else if (complete == 1) {
//                topbar.setVisibility(View.VISIBLE);
//                topbar1.setVisibility(View.VISIBLE);
                //  text_tujuan.setVisibility(View.VISIBLE);

                actionbar.setVisibility(View.GONE);
                infoatas.setVisibility(View.GONE);
                inf.setVisibility(View.GONE);
                currr.setVisibility(View.VISIBLE);
                but_tujuan.setVisibility(View.VISIBLE);
                navigate.setVisibility(View.GONE);
                complete = complete - 1;
                marker2.remove();
                for (Polyline polyline : line) {
                    polyline.remove();
                }
                line.clear();
                lineback.remove();
                //lattex.setText(String.valueOf(complete));
            } else if (complete == 2) {
                stepkiri.setVisibility(View.GONE);
                stepkanan.setVisibility(View.GONE);
                steplurus.setVisibility(View.GONE);
                kiri.setVisibility(View.GONE);
                kanan.setVisibility(View.GONE);
                lurus.setVisibility(View.GONE);
                actionbar.setVisibility(View.VISIBLE);
                navigate.setVisibility(View.VISIBLE);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
                // Zoom in the Google Map
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
// Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(100), 2000, null);
// Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(mMap.getCameraPosition())
                        .zoom(13)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build()));
                complete = complete - 1;
                //lattex.setText(String.valueOf(complete));
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
