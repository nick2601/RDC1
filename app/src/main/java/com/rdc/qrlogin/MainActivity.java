package com.rdc.qrlogin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements android.location.LocationListener{
    Button buttonScan ;
    String username;
    IntentIntegrator qrScan;
    UserSessionManager session;
    ProgressDialog pd;
    LocationManager locationManager;

    String city;
    String OPEN_WEATHER_MAP_API = "d608c2fcf4eb9f3d7a0fa92acc3c1527";

    String temp2 , temp1 , humidity_field  ;
    String challan_no, token, pro_time, pro_date ;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    int flag=5, location_status=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonScan = (Button) findViewById(R.id.buttonScan);
//        username = (TextView)findViewById(R.id.username);
//        username.setText(user);

        PreferenceHelper_coco pf = new PreferenceHelper_coco();
        String pref = pf.getPreferences(MainActivity.this,"username");
        Log.i("pref",pref);
//        username.setText(pref);
        username = pref;

        token = pf.getPreferences(MainActivity.this,"um_id");


        qrScan = new IntentIntegrator(this);
        session = new UserSessionManager(getApplicationContext());

        //_______________________________________________________________________________________________

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
/*        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initiating the qr code scan
                qrScan.initiateScan();
            }
        });*/

        //Scan Button
     /*   Button buttonBarCodeScan = (Button)findViewById(R.id.buttonScan);
        buttonBarCodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initiate scan with our custom scan activity
                new IntentIntegrator(MainActivity.this).setCaptureActivity(Scanner.class).initiateScan();
            }
        });*/
        //Button button=(Button)findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MainActivity.this).setCaptureActivity(Scanner.class).initiateScan();
            }
        });
    }

    protected int test(String challan_no){

        /*
        *   FLAG STATUS:
        *   0: Everything goes well
        *   1: Data already present
        *   2: Internet connectivity issue
        *   3: Server crashed
        *   4: Token expired
        *   999: Data 1 not present
        * */

        try {

            StrictMode.setThreadPolicy(policy);
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://34.93.199.150/api/cube/submitted");

            // add header
            Log.d("MainActivity", "test: "+token);
            post.setHeader("x-access-token", token);

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("qr_key_id", challan_no));
            Log.d("qr_key_id", challan_no);

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);

            Log.d("api response - ", String.valueOf(response.getStatusLine().getStatusCode()));

            if(response.getStatusLine().getStatusCode() == 200) {

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                String resString = result.toString();
                Log.d("resstring=>",resString);
                if(!resString.substring(resString.indexOf("data1") + 7, resString.indexOf("data1") + 9).equals("[]")) {
                    String res = result.toString().substring(8, 10);
                    int t_temp = resString.indexOf("PRODUCTION_TIME") + 18;
                    pro_time = resString.substring(t_temp, t_temp + 8);
                    int d_temp = resString.indexOf("PRODUCTION_DATE") + 18;
                    String t_date = resString.substring(d_temp, d_temp + 10);
                    pro_date = t_date.split("-")[2] + "-" + t_date.split("-")[1] + "-" + t_date.split("-")[0];
                    Log.d("pro-time - ", pro_time);
                    Log.d("pro-date - ", pro_date);

                    if (res.equals("[]")) {
                        flag = 0;
                        location_status = 1;
                    } else if (!res.equals("[]")) {
                        String meta = resString.substring(0,resString.indexOf("data1")-2);
                        if(meta.contains("Plant")){
                            if(!meta.contains("Site")){
                                flag = 0;
                                location_status = 2;
                            }else if(meta.contains("Site")){
                                flag = 1;
                            }
                        }if(meta.contains("Site") && !meta.contains("Plant")){
                            flag = 1;
                        }
                    }
                }else{
                    flag = 999;
                }
            }else if(response.getStatusLine().getStatusCode() == 502){
                flag = 3;
            }else if(response.getStatusLine().getStatusCode() == 401){
                flag = 4;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d("code-result: ", String.valueOf(result));
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
//_____________________________________________________
/*                JSONObject obj = null;
                try {
                    obj = new JSONObject(result.getContents());
                    String email = obj.getString("s_email");
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
//____________________________________________________________
                Log.d("qr-code-result", result.getContents());
                challan_no =  result.getContents(); // get challan code from qr code
                String[] test = challan_no.split(",",2);
                String t = test[0];
                String[] tq = t.split(":",2);
                String excise = tq[1].substring(1,(tq[1].length()-1));

                if (Function.isNetworkAvailable(getApplicationContext())) {
                    int temp = test(excise);
                    if(temp == 0){
                        taskLoadUp("mumbai");
                    }else if(temp == 1){
                        Toast.makeText(this, "Sample details already present", Toast.LENGTH_LONG).show();
                    }else if(temp == 2){
                        Toast.makeText(this,"Please try scanning QR code again", Toast.LENGTH_LONG).show();
                    }else if(temp == 3){
                        Toast.makeText(this,"Server crashed! Please contact your senior", Toast.LENGTH_LONG).show();
                    }else if(temp == 4){
//                        Toast.makeText(this,"Please try logging again",Toast.LENGTH_LONG).show();
                        session.logoutUser();
                        Intent sessend = new Intent(MainActivity.this, Login.class);
                        sessend.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        sessend.putExtra("session_expired" , true );
                        startActivity(sessend);
                        finish();
                    }else if(temp == 999){
                        Toast.makeText(this,"Please try again after 5 minutes else contact your senior", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Internet connectivity lost", Toast.LENGTH_LONG).show();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem logoutItem = menu.findItem(R.id.logout);
        logoutItem.setTitle("Logout "+username);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout)
        {
            session.logoutUser();
            Intent i = new Intent(MainActivity.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    void getLocation() {


        pd = new ProgressDialog(MainActivity.this );
        pd.setMessage("Wait! Current location Fetching.....");
        pd.setIndeterminate(true);
        pd.show();

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000000, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onLocationChanged(Location location) {

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            pd.dismiss();

           // Toast.makeText(this, ""+addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();
            city = addresses.get(0).getLocality();
            taskLoadUp(city);

            Intent i = new Intent(MainActivity.this,final_submit_page.class);
//            System.out.println(challan_no);
            i.putExtra("challan_no" , challan_no );
            i.putExtra("temp1" , temp1 );
            i.putExtra("temp2" , temp2);
            i.putExtra("humidity_field" , humidity_field);
            i.putExtra("production_time" , pro_time);
            i.putExtra("production_date" , pro_date);
            i.putExtra("location", location_status);


            startActivity(i);
            finish();

        } catch (Exception e)
        {

        }
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }
    @Override
    public void onProviderEnabled(String provider) { }
    @Override
    public void onProviderDisabled(String provider) { Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show(); }


    public void taskLoadUp(String query) {
        if (Function.isNetworkAvailable(getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    class DownloadWeather extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // loader.setVisibility(View.VISIBLE);

        }
        protected String doInBackground(String...args) {
            String xml = Function.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                   // cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
                     temp2 = details.getString("description").toUpperCase(Locale.US);
                     temp1 = String.format("%.2f", main.getDouble("temp")) + "°";
                    humidity_field = main.getString("humidity") + "%" ;
                    //pressure_field.setText("Pressure: " + main.getString("pressure") + " hPa");
                    //updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
                    //weatherIcon.setText(Html.fromHtml(Function.setWeatherIcon(details.getInt("id"),
                      //      json.getJSONObject("sys").getLong("sunrise") * 1000,
                        //    json.getJSONObject("sys").getLong("sunset") * 1000)));

                   // loader.setVisibility(View.GONE);

                    Intent i = new Intent(MainActivity.this,final_submit_page.class);

                    i.putExtra("challan_no" , challan_no );
                    i.putExtra("temp1" , temp1 );
                    i.putExtra("temp2" , temp2);
                    i.putExtra("humidity_field" , humidity_field);
                    i.putExtra("production_time" , pro_time);
                    i.putExtra("production_date" , pro_date);
                    i.putExtra("location", location_status);

                    startActivity(i);
                    finish();

                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
