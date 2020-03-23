package com.example.nirvoy;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.example.nirvoy.directionhelpers.FetchURL;
import com.example.nirvoy.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.mancj.materialsearchbar.MaterialSearchBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private MarkerOptions source, destination;
    Button getDirection;
    private Polyline currentPolyline;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    public static String stotal,sthisLoc;
   // public static float total,thisLoc;
    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private TextView listTitle;
    private Button btnHelp;
    private FloatingActionButton infoButton;
    private ImageButton cancelBtn;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout linearLayoutBSheet;
    private ToggleButton tbUpDown;
    private ProgressBar progressBar,graphProgress;
    private ProgressDialog progressDialog;
    private final float DEFAULT_ZOOM = 15;

    private RecyclerView placeRecyclerView;
    private PlaceAdapter placeAdapter;
    private RecyclerView.LayoutManager placeLayoutManager;

    double current_latitude, current_longitude;

    DatabaseReference databaseReference, databaseReference2,databaseReference3;
    FirebaseUser user;
    String uid;

    String address,msg;

    String dataresult, numberData;

    public ArrayList<PlaceDetails> placeDetailsArrayList;
    public ArrayList<CurrentLocation> locationList;
    public ArrayList<ContactData> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        init();

        msg = getIntent().getStringExtra("msg");


        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("ContactDatas").child(uid);
        databaseReference3 = FirebaseDatabase.getInstance().getReference("Username").child(uid);
        databaseReference2 = FirebaseDatabase.getInstance().getReference("currentLocation") ;

        collectContact();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        progressBar.setVisibility(View.INVISIBLE);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);
        Places.initialize(MapActivity.this, "AIzaSyAkw-8rEH2FHzG54RPa5oX4HEHbiE9mkwo");

        placesClient = Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectContact();
                sendMessage();
                collectAllInfo();
                buildRecyclerView();
                linearLayoutBSheet.setVisibility(View.VISIBLE);
                saveCurrentLocation();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetBehavior.getState()== BottomSheetBehavior.STATE_EXPANDED || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    linearLayoutBSheet.setVisibility(View.GONE);
                }
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cntOfReq = 0;

                loadPastLocation();

                for(int i = 0; i < locationList.size(); i++)
                {
                    double end_lat = locationList.get(i).getLatitude();
                    double end_lng = locationList.get(i).getLongitude();
                    float results[] = new float[10];

                    Location.distanceBetween(current_latitude, current_longitude, end_lat, end_lng, results);

                    if(results[0] <= 1000)
                    {
                        cntOfReq++;
                    }

                    Log.d("loadLocation", "onClick: "+ locationList.get(i).getLatitude() + " " + locationList.get(i).getLongitude() + "Distance: " + results[0]);
                }

                String cur_lat  = String.valueOf(current_latitude);
                String cur_lng  = String.valueOf(current_longitude);
                String cnt_req = String.valueOf(cntOfReq);
                String total  = String.valueOf(locationList.size());
                Log.d("loadLocation", "Total: " + cur_lat+" That location "+cur_lng);

                Intent intent = new Intent(getApplicationContext(), ShowGraphActivity.class);
                intent.putExtra("lat",cur_lat);
                intent.putExtra("lng",cur_lng);

                intent.putExtra("cnt_req",cnt_req);
                intent.putExtra("tot",total);
//                graphProgress.setVisibility(View.VISIBLE);
                startActivity(intent);

            }
        });

        tbUpDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View view, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    tbUpDown.setChecked(true);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    tbUpDown.setChecked(false);
                }
            }

            @Override
            public void onSlide(View view, float v) {
                int height = bottomSheetBehavior.getPeekHeight();
                height = height+(int)v;
                bottomSheetBehavior.setPeekHeight(height);
            }
        });

    }

    private void loadPastLocation() {

        databaseReference2 = FirebaseDatabase.getInstance().getReference("currentLocation");

        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                locationList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    CurrentLocation currentLocation = dataSnapshot1.getValue(CurrentLocation.class);
                    locationList.add(currentLocation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void saveCurrentLocation() {

        String key = databaseReference2.push().getKey();

        CurrentLocation currentLocation = new CurrentLocation(current_latitude, current_longitude,address);
        databaseReference2.child(key).setValue(currentLocation);
    }


    public void collectAllInfo(){

        Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
        placeDetailsArrayList.clear();

        if(msg.equals("medical")){
            getNearbyHospitals();
            listTitle.setText("Hospitals");
        }
        else if(msg.equals("police")){
            getNearbyPolice();
            listTitle.setText("Police Stations");
        }
    }


    private void init() {
//        materialSearchBar = findViewById(R.id.searchBar);
        btnHelp = findViewById(R.id.btn_help);
        infoButton = findViewById(R.id.info);
        cancelBtn = findViewById(R.id.cancelbtn);
        this.linearLayoutBSheet = findViewById(R.id.bottomSheet);
        this.bottomSheetBehavior = BottomSheetBehavior.from(linearLayoutBSheet);
        this.tbUpDown = findViewById(R.id.toggleButton);
        placeDetailsArrayList =new ArrayList<>();
        contactList = new ArrayList<>();
        locationList = new ArrayList<>();
        placeRecyclerView = findViewById(R.id.recyclerView);
        placeRecyclerView.setHasFixedSize(true);
        placeLayoutManager = new LinearLayoutManager(this);
        listTitle = findViewById(R.id.list_title);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if(mapView != null && mapView.findViewById(Integer.parseInt("1")) != null){
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 280);
        }

        //check if gps is enabled

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
                getNearbyHospitals();
            }
        });

        task.addOnFailureListener(MapActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException resolvable = (ResolvableApiException) e;

                    try {
                        resolvable.startResolutionForResult(MapActivity.this, 51);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 51){
            if(resultCode == RESULT_OK){
                getDeviceLocation();
                getNearbyHospitals();
            }
        }
    }

    private void getDeviceLocation(){
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()){
                            mLastKnownLocation = task.getResult();

                            current_latitude = mLastKnownLocation.getLatitude();
                            current_longitude = mLastKnownLocation.getLongitude();
                            address = getAddress(current_latitude, current_longitude);

                            Log.d("cor", "This is latitude " + current_latitude + " longitude " + current_longitude);
                            if(mLastKnownLocation != null){
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            }else{
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback(){
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if(locationResult == null){
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };

                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        } else{
                            Toast.makeText(MapActivity.this, "Unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getNearbyPolice(){

        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("location="+String.valueOf(current_latitude)+","+String.valueOf(current_longitude));

        if(!SettingsActivity.radius.isEmpty() && SettingsActivity.radius != "")
        {
            stringBuilder.append("&radius="+SettingsActivity.radius);
        }
        else
        {
            stringBuilder.append("&radius=5000");
        }
        stringBuilder.append("&type=police");
        stringBuilder.append("&key=AIzaSyAkw-8rEH2FHzG54RPa5oX4HEHbiE9mkwo");

        PlaceDetails placeDetails;

        String url = stringBuilder.toString();

        Log.d("nearbyplaces", url);

        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        try {
            dataresult = getNearbyPlacesData.execute(dataTransfer).get().toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject parentObject = new JSONObject(dataresult);
            JSONArray resultArray = parentObject.getJSONArray("results");


            for(int i = 0; i < resultArray.length(); i++){
                JSONObject jsonObject = resultArray.getJSONObject(i);

                JSONObject geometry = jsonObject.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                String lat = location.getString("lat");
                String lng = location.getString("lng");

                double place_lat = Double.parseDouble(lat);
                double place_lng = Double.parseDouble(lng);

                String place_id = jsonObject.getString("place_id");
                String place_name = jsonObject.getString("name");
                String place_rating = jsonObject.getString("rating");

                String phoneNumber = getPhoneNumber(place_id);
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    placeDetails = new PlaceDetails(R.drawable.ic_home,place_id,place_name,phoneNumber,"police",place_lat,place_lng);
                    placeDetailsArrayList.add(placeDetails);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getNearbyHospitals() {

        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("location="+String.valueOf(current_latitude)+","+String.valueOf(current_longitude));

        if(!SettingsActivity.radius.isEmpty() && SettingsActivity.radius != "")
        {
            stringBuilder.append("&radius="+SettingsActivity.radius);
        }
        else
        {
            stringBuilder.append("&radius=2000");
        }

        stringBuilder.append("&type=hospital");
        stringBuilder.append("&key=AIzaSyAkw-8rEH2FHzG54RPa5oX4HEHbiE9mkwo");

        PlaceDetails placeDetails;

        String url = stringBuilder.toString();

        Log.d("nearbyplaces", url);

        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();

        try {
            dataresult = getNearbyPlacesData.execute(dataTransfer).get().toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject parentObject = new JSONObject(dataresult);
            JSONArray resultArray = parentObject.getJSONArray("results");


            for(int i = 0; i < resultArray.length(); i++){
                JSONObject jsonObject = resultArray.getJSONObject(i);

                JSONObject geometry = jsonObject.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                String lat = location.getString("lat");
                String lng = location.getString("lng");

                double place_lat = Double.parseDouble(lat);
                double place_lng = Double.parseDouble(lng);

                String place_id = jsonObject.getString("place_id");
                String place_name = jsonObject.getString("name");
//                String place_rating = jsonObject.getString("rating");

                String phoneNumber = getPhoneNumber(place_id);
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    placeDetails = new PlaceDetails(R.drawable.ic_hospital, place_id,place_name,phoneNumber,"hospital",place_lat ,place_lng);
                    placeDetailsArrayList.add(placeDetails);
                }

                Log.d("lat_lng", "getNearbyHospitals: lat "+lat+"   long: "+lng);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void buildRecyclerView(){
        placeAdapter = new PlaceAdapter(placeDetailsArrayList);
        placeRecyclerView.setLayoutManager(placeLayoutManager);
        placeRecyclerView.setAdapter(placeAdapter);
        placeAdapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showDirection(position);
            }

            @Override
            public void onCallClick(int position){
                callItem(position);
            }
        });
    }

    public String getPhoneNumber(String place_id){
        String formatted_phone_number = "";
        String international_phone_number= "";
        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        stringBuilder.append("place_id=" + place_id);
        stringBuilder.append("&fields=name,formatted_phone_number,international_phone_number");
        stringBuilder.append("&key=AIzaSyAkw-8rEH2FHzG54RPa5oX4HEHbiE9mkwo");

        String url = stringBuilder.toString();

        Log.d("nearbyplacesphone", "url : "+url);

        Object dataTransfer[] = new Object[2];
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;

        GetNearbyPlacesNumber getNearbyPlacesNumber = new GetNearbyPlacesNumber();
        try {
            numberData = getNearbyPlacesNumber.execute(dataTransfer).get().toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("nearbyplacesnumber", "getPhoneNumber: "+numberData);

        try {
            JSONObject parentObject = new JSONObject(numberData);
            JSONObject jsonObject = parentObject.getJSONObject("result");

            formatted_phone_number = jsonObject.getString("formatted_phone_number");
            international_phone_number = jsonObject.getString("international_phone_number");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (international_phone_number != null && !international_phone_number.isEmpty()) {
            return international_phone_number;
        }
        else if (formatted_phone_number != null && !formatted_phone_number.isEmpty()) {
            return formatted_phone_number;
        }
        else{
            return "";
        }
    }

    //to retrieve the current address from latitude and longitude
    String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        String current_address = "";

        List<Address> addresses = null;
        try {
            Log.e("latitude", "inside latitude--" + latitude);
            addresses = geocoder.getFromLocation(latitude, longitude, 1);


            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                current_address = address;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return current_address;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void callItem(int position){
        String number = placeDetailsArrayList.get(position).getPlaceNumber();
        number=number.replace("-", "");
        number=number.replace(" ", "");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));

        if (ActivityCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }


    public void showDirection(int position){
        PlaceDetails placeDetails = placeDetailsArrayList.get(position);
        double lat = placeDetails.getPlaceLat();
        double lng = placeDetails.getPlaceLng();
        String name = placeDetails.getPlaceName();
        source = new MarkerOptions().position(new LatLng(current_latitude, current_longitude)).title("Your Device Location");
        destination = new MarkerOptions().position(new LatLng(lat, lng)).title(name);
        new FetchURL(MapActivity.this).execute(getUrl(source.getPosition(), destination.getPosition(), "walking"), "walking");
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    void collectContact(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    ContactData contactData = dataSnapshot1.getValue(ContactData.class);
                    Log.d("contacts", contactData.getNumber()+"\n");
                    String number = contactData.getNumber();
                    Log.d("message", "onDataChange: "+number);
                    contactList.add(contactData);
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void sendMessage(){
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                if(key.equals(uid)){
                    String name = dataSnapshot.getValue(String.class);
                    if(msg.equals("medical")){
                        String message ="I am "+name+". I need medical help at "+address+". Please help me.";
                        for(int i=0;i<contactList.size();i++){
                            ContactData contactData = contactList.get(i);
                            send(message,contactData.getNumber());
                            Log.d("Message", "number: "+contactData.getNumber()+" message:"+message);
                        }
                    }
                    else{
                        String message ="I am "+name+". I am not feeling safe at "+address+". Please help me.";
                        for(int i=0;i<contactList.size();i++){
                            ContactData contactData = contactList.get(i);
                            send(message,contactData.getNumber());
                            Log.d("Message", "number: "+contactData.getNumber()+" message:"+message);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void send(String msg ,String number){


        String sms = msg;
        String phoneNum = number;
        if(!TextUtils.isEmpty(sms) && !TextUtils.isEmpty(phoneNum)) {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNum, null, sms, null, null);
        }

        //String message = "01723346238";

        //Getting intent and PendingIntent instance
        //Intent intent = new Intent(MapActivity.this,);
        //PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,intent, 0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        ////SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(number, null, msg, pi, null);

        Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                Toast.LENGTH_LONG).show();
    }

}
