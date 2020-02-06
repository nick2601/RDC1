package com.rdc.qrlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.rdc.qrlogin.Activities.LoginResponse;
import com.rdc.qrlogin.RetrofitClient.RetClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {


    EditText e1, e2;
    Button btnlogin;
    ArrayList<HashMap<String, String>> data;
    String sttrresponce;
    json js;
    Linkpage l1;
    ProgressDialog pd;
    String sname, spass, id, token;

    UserSessionManager session;
    PreferenceHelper_coco pf;
    TextView error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSessionManager(getApplicationContext());
        pf = new PreferenceHelper_coco();

        e1 = (EditText) findViewById(R.id.email);
        e2 = (EditText) findViewById(R.id.pass);
        btnlogin = (Button) findViewById(R.id.login);
        error = (TextView) findViewById(R.id.error);

        final Intent in = getIntent();
        if (in.getBooleanExtra("session_expired", false) == true) {
            Toast.makeText(this, "Session expired", Toast.LENGTH_LONG).show();
        }

        if (session.isUserLoggedIn()) {
            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            finish();
        }


        js = new json();
        l1 = new Linkpage();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sname = e1.getText().toString();
                Log.d("username", sname);
                spass = e2.getText().toString();
                if (sname.matches("")) {
                    e1.setError("Id should not be null ");
                    return;
                }
                if (spass.matches("")) {
                    e2.setError("password should not be null");
                    return;
                }
                /*session.createUserLoginSession("User Session ", "a" );
                pf.SavePreferences(Login.this,"um_id","a");*/

                /*SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Login.this);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("userName",sname);
                editor.commit();*/

                //Login_class log = new Login_class();
                //log.execute();

                //...................................
                Call<LoginResponse> call = RetClient
                        .getInstance()
                        .getApi()
                        .login(sname, spass);

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        LoginResponse loginresponse = response.body();
                        Log.d("Login", "onResponse: " + new Gson().toJson(loginresponse));
                        if (response.isSuccessful()) {
                            session.createUserLoginSession("User Session ", id );
                            pf.SavePreferences(Login.this,"um_id",token);
                            pf.SavePreferences(Login.this,"username",sname);
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            Toast.makeText(Login.this, loginresponse.getToken(), Toast.LENGTH_LONG).show();
                            String token = loginresponse.getToken();
                            Log.d("token", token);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {

                            Toast.makeText(Login.this, loginresponse.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

  /*  public class Login_class extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pd = new ProgressDialog(Login.this );
            pd.setMessage("Authenticating Device......");
            pd.setIndeterminate(true);
//            pd.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            List<NameValuePair> param = new ArrayList<NameValuePair>();
            data = new ArrayList<HashMap<String, String>>();

            param.add(new BasicNameValuePair("ft_login", sname));
            param.add(new BasicNameValuePair("ft_password", spass));
        //    sttrresponce = js.getJSONFromUrl(l1.URLLink+"/demologin.php",param);
            //sttrresponce = js.getJSONFromUrl("http://34.93.199.150/api/master/ft_login",param);
//            Log.d("json data->", String.valueOf(param));
            JSONObject jobjResponse ;

            try {
                jobjResponse = new JSONObject(sttrresponce);
               // JSONArray jArrayProdList = new JSONArray();
                //jArrayProdList = jobjResponse.getJSONArray("user");
                for (int i = 0; i < jobjResponse.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                  //  JSONObject jobj = jobjResponse.getJSONObject(i);

                    id = jobjResponse.getString("success");
                    token = jobjResponse.getString("token");
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!id.equals("true"))
            {
                pd.dismiss();
               // Toast.makeText(Login.this, "invalid Email and Password", Toast.LENGTH_SHORT).show();
                error.setVisibility(View.VISIBLE);
                e1.setText("");
                e2.setText("");
                e1.setFocusable(true);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "successfully logged in", Toast.LENGTH_SHORT).show();

                session.createUserLoginSession("User Session ", id );
                pf.SavePreferences(Login.this,"um_id",token);
                pf.SavePreferences(Login.this,"username",sname);

                Intent i = new Intent(Login.this,MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        }
    }*/
        });
    }
}