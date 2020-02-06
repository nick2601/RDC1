package com.rdc.qrlogin;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class final_submit_page extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView textViewName, textViewAddress , txtchalanno,textCustomerName, txtchalan_date, txtchalan_time;
    String email , phone , qr_key_id , token , challan_no;
    UserSessionManager session;
    Button submit ;
    json j;
    Linkpage l1;
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    PreferenceHelper_coco pf;
    RadioGroup radioGroup2, radioGroup1 , radioGroup3 ;
    RadioButton radioButton1 , radioButton2 , radioButton3, plant_btn, site_btn ;


    String str_sampling_loc = null , str_plant_slum , str_Sampling_time , str_sampling_slum ,
            str_cast_time , str_cast_slump  ,  str_water, str_admix , str_Spiceman_size = null, str_placement = null
             , str_temp1 , str_temp2 , str_humidity_field ;

    EditText e_str_plant_slum , e_str_sampling_slum , e_str_cast_slump , e_str_water, e_str_admix , e_temp1 , e_temp2  , e_str_humidity_field;

    Button b_Sampling_time , b_casting_time ;
    TextView t_Sampling_time , t_casting_time ;

    Spinner spinner_cast , spinner_structure;
    String s_cast , s_structure, production_time, production_date ;
    int sp_id_1 , sp_id_2, timerFlag=2, location_status ;

TextView temp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_submit_page);

        temp = (TextView)findViewById(R.id.temp);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        txtchalanno = (TextView)findViewById(R.id.chalanno);
        txtchalan_date = (TextView)findViewById(R.id.chalan_date);
        txtchalan_time = (TextView)findViewById(R.id.chalan_time);
        submit  = (Button)findViewById(R.id.submit);

        b_Sampling_time = (Button) findViewById(R.id.clock_Sampling_time);
        t_Sampling_time = (TextView)findViewById(R.id.Sampling_time) ;
        b_casting_time = (Button) findViewById(R.id.clock_casting_time);
        t_casting_time = (TextView)findViewById(R.id.casting_time) ;

        e_str_plant_slum = (EditText)findViewById(R.id.e_str_plant_slum);
        e_str_sampling_slum = (EditText)findViewById(R.id.e_str_sampling_slum);
        e_str_cast_slump = (EditText)findViewById(R.id.e_str_cast_slump);
        e_str_water = (EditText)findViewById(R.id.e_str_water);
        e_str_admix = (EditText)findViewById(R.id.e_str_admix);
        e_temp1 = (EditText)findViewById(R.id.temp1);
        e_temp2 = (EditText)findViewById(R.id.temp2);
        e_str_humidity_field = (EditText)findViewById(R.id.e_str_humidity_field);
        spinner_cast = (Spinner) findViewById(R.id.spinner1);
        spinner_structure = (Spinner) findViewById(R.id.spinner2);
        textCustomerName=(TextView)findViewById(R.id.customer_name);


        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radioGroup3 = (RadioGroup) findViewById(R.id.radioGroup3);

        plant_btn = (RadioButton) findViewById(R.id.plant_button);
        site_btn = (RadioButton) findViewById(R.id.site_button);

        session = new UserSessionManager(getApplicationContext());
        pf = new PreferenceHelper_coco();
        token = pf.getPreferences(final_submit_page.this,"um_id");

        final Intent i = getIntent();

        challan_no = i.getStringExtra("challan_no");
        System.out.println(challan_no);
        str_temp1 = i.getStringExtra("temp1");
        str_temp2 = i.getStringExtra("temp2");
        str_humidity_field = i.getStringExtra("humidity_field");
        production_time = i.getStringExtra("production_time");
        production_date = i.getStringExtra("production_date");
        location_status = i.getIntExtra("location", 0);

        textViewAddress.setText(challan_no);
        e_temp1.setText(str_temp1);
        e_temp2.setText(str_temp2);
        e_str_humidity_field.setText(str_humidity_field);

        String s=challan_no;
//        System.out.println(s+"\n");
       String[] split = s.split("\"");
//        for(String spl: split)
//        {
//            System.out.println(spl);
//        }

        textCustomerName.setText(split[7]);
  //      challan_no = split[3];
        txtchalanno.setText(challan_no);
        txtchalan_time.setText(production_time);
        txtchalan_date.setText(production_date);
        if(location_status == 0){
            Toast.makeText(this,"Something went wrong, please try scanning Qr code again", Toast.LENGTH_LONG).show();
            Intent it = new Intent(final_submit_page.this, MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
            finish();
        }else if(location_status == 2){
            site_btn.setChecked(true);
            plant_btn.setEnabled(false);
        }

        j = new json();
        l1 = new Linkpage();

        List<String> categories = new ArrayList<String>();
        categories.add("-");
        categories.add("1");
        categories.add("2");
        categories.add("3");
        categories.add("4");
        categories.add("5");
        categories.add("6");
        categories.add("7");
        categories.add("8");
        categories.add("9");
        categories.add("10");

        List<String> categories2 = new ArrayList<String>();
        categories2.add("-");
        categories2.add("Column");
        categories2.add("Slab");
        categories2.add("Footing");
        categories2.add("Raft");
        categories2.add("Aluminium");
        categories2.add("Formwork");
        categories2.add("Sheerwall");
        categories2.add("Corewall");
        categories2.add("Retaining");
        categories2.add("PCC");
        categories2.add("Pile");
        categories2.add("Screed");
        categories2.add("PQC");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cast.setAdapter(dataAdapter);
        //spinner_cast.setOnItemSelectedListener(this);
        spinner_cast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                sp_id_1 = spinner_cast.getSelectedItemPosition();
                s_cast=spinner_cast.getSelectedItem().toString();

                //Toast.makeText(final_submit_page.this, ""+spinner_position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_structure.setAdapter(dataAdapter2);
        //spinner_structure.setOnItemSelectedListener(this);
        spinner_structure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                sp_id_2 = spinner_cast.getSelectedItemPosition();
                s_structure=spinner_structure.getSelectedItem().toString();

                //Toast.makeText(final_submit_page.this, ""+spinner_position, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        // Toast.makeText(this, "token : "+token, Toast.LENGTH_SHORT).show();

        b_Sampling_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertdialog_box();
            }
        });

        b_casting_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertdialog_box2();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_plant_slum = e_str_plant_slum.getText().toString();
                str_sampling_slum = e_str_sampling_slum.getText().toString();
                str_cast_slump = e_str_cast_slump.getText().toString();
                str_water = e_str_water.getText().toString();
                str_admix = e_str_admix.getText().toString();

//                str_Sampling_time = t_Sampling_time.getText().toString();
//                str_cast_time = t_casting_time.getText().toString();
//                Log.i("cast time: ", str_cast_time);
//                Log.i("samp time: ", str_Sampling_time);
//
//                DateFormat outputformat = new SimpleDateFormat("HH:mm:ss");
//                //7:40 PM
//
//                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm aa");
////                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//                Date castTime= null, samplingTime = null, productionTime = null;
//                try {
//                    Date output = outputformat.parse(production_time);
//                    String date = formatter.format(output);
//                    castTime = formatter.parse(str_cast_time);
//                    samplingTime=formatter.parse(str_Sampling_time);
//                    productionTime= formatter.parse(date);
//                    Log.d("castt-",String.valueOf(castTime));
//                    Log.d("sampt-",String.valueOf(samplingTime));
//                    Log.d("prot-",String.valueOf(productionTime));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                if (str_plant_slum.matches("")) {
                    e_str_plant_slum.setError("required");
                    return;
                }
                if (str_sampling_slum.matches("")) {
                    e_str_sampling_slum.setError("required");
                    return;
                }
                if (str_cast_slump.matches("")) {
                    e_str_cast_slump.setError("required");
                    return;
                }
                if (str_water.matches("")) {
                    e_str_water.setError("required");
                    return;
                }
                if (str_admix.matches("")) {
                    e_str_admix.setError("required");
                    return;
                }
                if (str_Sampling_time.matches("")) {
                    Toast.makeText(final_submit_page.this, "select sampling time", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if(samplingTime.before(productionTime)){
//                    Toast.makeText(final_submit_page.this, "Sampling time can't be before production time.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (str_cast_time.matches("")) {
                    Toast.makeText(final_submit_page.this, "select casting time", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if(castTime.after(samplingTime)){
//                    Toast.makeText(final_submit_page.this, "Casting time can't be before sampling time.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                int selectedId = 0;
                if(location_status == 1) {
                    if(plant_btn.isChecked() == true)
                    {
                        str_sampling_loc = "Plant";
                    }
                    if(site_btn.isChecked() == true){
                        str_sampling_loc = "Site";
                    }
                    if(plant_btn.isChecked() == false && site_btn.isChecked() == false){
                        Toast.makeText(final_submit_page.this, "select sampling location", Toast.LENGTH_SHORT).show();
                    }

//                    selectedId = radioGroup1.getCheckedRadioButtonId();
//                    radioButton1 = (RadioButton) findViewById(selectedId);
//                    if (selectedId != -1) {
//                        str_sampling_loc = radioButton1.getText().toString();
//                    }
//                    if (selectedId != -1) {
//                        str_sampling_loc = radioButton1.getText().toString();
//                    }
//                    if (selectedId == -1) {
//                    }
                }else if(location_status == 2){
                    str_sampling_loc = "Site";
                }
                int selectedId2 = radioGroup2.getCheckedRadioButtonId();
                radioButton2 = (RadioButton) findViewById(selectedId2);
                if (selectedId2 != -1)
                { str_Spiceman_size = radioButton2.getText().toString(); }
                if (selectedId2 != -1)
                { str_Spiceman_size = radioButton2.getText().toString(); }
                if(selectedId2 == -1  )
                { Toast.makeText(final_submit_page.this, "select specimen size", Toast.LENGTH_SHORT).show(); }

                int selectedId3 = radioGroup3.getCheckedRadioButtonId();
                radioButton3 = (RadioButton) findViewById(selectedId3);
                if (selectedId3 != -1)
                { str_placement = radioButton3.getText().toString(); }
                if (selectedId3 != -1)
                { str_placement = radioButton3.getText().toString(); }
                if(selectedId3 == -1  )
                { Toast.makeText(final_submit_page.this, "Select Placement", Toast.LENGTH_SHORT).show(); }

                if (sp_id_1 == 0)
                {
                    Toast.makeText(final_submit_page.this, "Select Cube Number ", Toast.LENGTH_SHORT).show();
                }
                if (sp_id_2 == 0)
                {
                    Toast.makeText(final_submit_page.this, "Select Structure Type", Toast.LENGTH_SHORT).show();
                }

                if ( (selectedId2 != -1 )  && ( selectedId3 != -1) && (sp_id_1 != 0) && (sp_id_2 != 0) ) {
                    insertdata();
                    //Toast.makeText(final_submit_page.this, "insert", Toast.LENGTH_SHORT).show();
                }

            }
        });

//
//        private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
//    }

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        Long item = parent.getItemIdAtPosition(position);

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public  void insertdata() {

        // as a header x-access-token

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
            JSONObject student1 = new JSONObject();

            student1.put("qr_key_id", challan_no);
            student1.put("sampling_loc", str_sampling_loc);
            student1.put("plant_slump", str_plant_slum);
            student1.put("sampling_time", str_Sampling_time);
            student1.put("sampling_slump", str_sampling_slum);
            student1.put("casting_time", str_cast_time);
            student1.put("casting_slump", str_cast_slump);
            student1.put("size", str_Spiceman_size);
            student1.put("addition", str_water);
            student1.put("admixture", str_admix);
            student1.put("placement", str_placement);
            //student1.put("id", "id");
            student1.put("temp", str_temp1);
            student1.put("weather", str_temp2);
            student1.put("humidity", str_humidity_field);
            student1.put("cube_numbers", s_cast);
            student1.put("structure_type", s_structure);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(student1);

            Log.d("jsonarry" , jsonArray+"");

             params.add(new BasicNameValuePair("rf7", jsonArray+"")) ;

            StrictMode.setThreadPolicy(policy);
            String str = j.getJSONFromUrl("http://34.93.199.150/api/cube/ft_rf7", params  , token);
            Log.d("api response - ",str);
            if(str.contains("Already Present & Entered by someone")){
                Toast.makeText(getApplicationContext(), "" + str, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(final_submit_page.this,Scanner.class);
                finish();
                startActivity(intent);

            }else {
                Toast.makeText(getApplicationContext(), "" + str, Toast.LENGTH_LONG).show();
                Intent i = new Intent(final_submit_page.this, blank.class);
                finish();
                startActivity(i);
                Log.e("eeeeeeeeeeeeeee", str);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

       /* StrictMode.setThreadPolicy(policy);
        String str = j.getJSONFromUrl("http://35.200.166.14/api/cube/ft_rf7", params  , token);
        Toast.makeText(getApplicationContext(), "submit : "+str , Toast.LENGTH_LONG).show();
        Log.e("eeeeeeeeeeeeeee" , str);*/

    }


    public void alertdialog_box(){
        int hour = Integer.parseInt(production_time.substring(0,2));
        int minute = Integer.parseInt(production_time.substring(3,5));
        timerFlag = 0;
        RangeTimePickerDialog tpd = new RangeTimePickerDialog(final_submit_page.this, new RangeTimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            }
        }, hour, minute+1, true);
        tpd.setMin(hour, minute+1);
//        if(hour >= 18){
//            tpd.setMax((hour+6)-24, minute);
//        }else {
//            tpd.setMax(hour + 6, minute);         //Change the numeric ie:6 to increase or decrease the time limit
//        }
        tpd.setMax(hour + 6, minute);         //Change the numeric ie:6 to increase or decrease the time limit
        tpd.show();
    }
//08:45
    public void alertdialog_box2() {
        int hour, minute;
        if(str_Sampling_time != null) {
            timerFlag = 1;
            if(str_Sampling_time.split(":")[0].length() == 1) {
                hour = Integer.parseInt("0"+str_Sampling_time.substring(0, 1));
                minute = Integer.parseInt(str_Sampling_time.substring(2,4));
            }else{
                hour = Integer.parseInt(str_Sampling_time.substring(0, 2));
                minute = Integer.parseInt(str_Sampling_time.substring(3,5));
            }
            RangeTimePickerDialog tpd = new RangeTimePickerDialog(final_submit_page.this, new RangeTimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                }
            }, hour, minute + 1, true);
            tpd.setMin(hour, minute + 1);
            tpd.setMax(hour + 6, minute);
            tpd.show();
        }else{
            Toast.makeText(this, "Please set production time first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout)
        {
            session.logoutUser();
            Intent i = new Intent(final_submit_page.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(final_submit_page.this , MainActivity.class);
        startActivity(i);
    }

//    -----------------------------------------------------

    public class RangeTimePickerDialog extends TimePickerDialog {

        private int minHour = -1;
        private int minMinute = -1;

        private int maxHour = 25;
        private int maxMinute = 25;

        private int currentHour = 0;
        private int currentMinute = 0;

        private Calendar calendar = Calendar.getInstance();
        private DateFormat dateFormat;


        public RangeTimePickerDialog(Context context, TimePickerDialog.OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
            super(context, callBack, hourOfDay, minute, is24HourView);
            currentHour = hourOfDay;
            currentMinute = minute;
            dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
            fixSpinner(context, hourOfDay, minute, is24HourView);

            try {
                Class<?> superclass = getClass().getSuperclass();
                Field mTimePickerField = superclass.getDeclaredField("mTimePicker");
                mTimePickerField.setAccessible(true);
                TimePicker mTimePicker = (TimePicker) mTimePickerField.get(this);
                mTimePicker.setOnTimeChangedListener(this);
            } catch (NoSuchFieldException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            }
        }

        public void setMin(int hour, int minute) {
            minHour = hour;
            minMinute = minute;
        }

        public void setMax(int hour, int minute) {
            maxHour = hour;
            maxMinute = minute;
        }

        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            boolean validTime = true;
            if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)){
                validTime = false;
            }

            if (hourOfDay  > maxHour || (hourOfDay == maxHour && minute > maxMinute)){
                validTime = false;
            }

            if(validTime){
                currentHour = hourOfDay;
                currentMinute = minute;
            }

            updateTime(currentHour, currentMinute);
            updateDialogTitle(view, currentHour, currentMinute);
        }


//        @Override
//        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//
//            boolean validTime = true;
//            if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)){
//                validTime = false;
//            }
//
//            if (hourOfDay  > maxHour || (hourOfDay == maxHour && minute > maxMinute)){
//                validTime = false;
//            }
//
//            if(validTime && (hourOfDay < 18)){
//                currentHour = hourOfDay;
//                currentMinute = minute;
//            }
//
//            if (!validTime && minHour >= 18) {
//                switch(hourOfDay){
//                    case 23: if(hourOfDay == maxHour && minute > maxMinute){ currentHour = maxHour; currentMinute = maxMinute;
//                    }else currentHour = 0; currentMinute = minute;
//                        break;
//                    case 0: if(hourOfDay == maxHour && minute > maxMinute){ currentHour = maxHour; currentMinute = maxMinute;
//                    }else currentHour = hourOfDay; currentMinute = minute;
//                        break;
//                    case 1: if(hourOfDay == maxHour && minute > maxMinute){ currentHour = maxHour; currentMinute = maxMinute;
//                    }else currentHour = hourOfDay; currentMinute = minute;
//                        break;
//                    case 2: if(hourOfDay == maxHour && minute > maxMinute){ currentHour = maxHour; currentMinute = maxMinute;
//                    }else currentHour = hourOfDay; currentMinute = minute;
//                        break;
//                    case 3: if(hourOfDay == maxHour && minute > maxMinute){ currentHour = maxHour; currentMinute = maxMinute;
//                    }else currentHour = hourOfDay; currentMinute = minute;
//                        break;
//                    case 4: if(hourOfDay == maxHour && minute > maxMinute){ currentHour = maxHour; currentMinute = maxMinute;
//                    }else currentHour = hourOfDay; currentMinute = minute;
//                        break;
//                    case 5: if(hourOfDay == maxHour && minute > maxMinute){ currentHour = maxHour; currentMinute = maxMinute;
//                    }else currentHour = 0; currentMinute = minute;
//                        break;
//                }
////                currentHour = (hourOfDay+6)-24;
//                currentMinute = minute;
//            }
//
//
//            updateTime(currentHour, currentMinute);
//            updateDialogTitle(view, currentHour, currentMinute);
//        }

        private void updateDialogTitle(TimePicker timePicker, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            String m, h;
            if(hourOfDay<10) {
                h = "0" + String.valueOf(hourOfDay);
            }else{
                h = String.valueOf(hourOfDay);
            }
            if(minute<10) {
                m = "0" + String.valueOf(minute);
            }else{
                m = String.valueOf(minute);
            }
            String s = String.valueOf(calendar.SECOND);
//            DateFormat df = new SimpleDateFormat("hh:mm:ss");
            if(timerFlag == 0){
                str_Sampling_time = h+":"+m+":"+s;
                t_Sampling_time.setText(str_Sampling_time);
            }
            if(timerFlag == 1){
                str_cast_time = h+":"+m+":"+s;
                t_casting_time.setText(str_cast_time);
            }
        }


        private void fixSpinner(Context context, int hourOfDay, int minute, boolean is24HourView) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // android:timePickerMode spinner and clock began in Lollipop
                try {
                    // Get the theme's android:timePickerMode
                    //two modes are available clock mode and spinner mode ... selecting spinner mode for latest versions
                    final int MODE_SPINNER = 2;
                    Class<?> styleableClass = Class.forName("com.android.internal.R$styleable");
                    Field timePickerStyleableField = styleableClass.getField("TimePicker");
                    int[] timePickerStyleable = (int[]) timePickerStyleableField.get(null);
                    final TypedArray a = context.obtainStyledAttributes(null, timePickerStyleable, android.R.attr.timePickerStyle, 0);
                    Field timePickerModeStyleableField = styleableClass.getField("TimePicker_timePickerMode");
                    int timePickerModeStyleable = timePickerModeStyleableField.getInt(null);
                    final int mode = a.getInt(timePickerModeStyleable, MODE_SPINNER);
                    a.recycle();
                    if (mode == MODE_SPINNER) {
                        TimePicker timePicker = (TimePicker) findField(TimePickerDialog.class, TimePicker.class, "mTimePicker").get(this);
                        Class<?> delegateClass = Class.forName("android.widget.TimePicker$TimePickerDelegate");
                        Field delegateField = findField(TimePicker.class, delegateClass, "mDelegate");
                        Object delegate = delegateField.get(timePicker);
                        Class<?> spinnerDelegateClass;
                        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
                            spinnerDelegateClass = Class.forName("android.widget.TimePickerSpinnerDelegate");
                        } else {

                            spinnerDelegateClass = Class.forName("android.widget.TimePickerClockDelegate");
                        }
                        if (delegate.getClass() != spinnerDelegateClass) {
                            delegateField.set(timePicker, null); // throw out the TimePickerClockDelegate!
                            timePicker.removeAllViews(); // remove the TimePickerClockDelegate views
                            Constructor spinnerDelegateConstructor = spinnerDelegateClass.getConstructor(TimePicker.class, Context.class, AttributeSet.class, int.class, int.class);
                            spinnerDelegateConstructor.setAccessible(true);
                            // Instantiate a TimePickerSpinnerDelegate
                            delegate = spinnerDelegateConstructor.newInstance(timePicker, context, null, android.R.attr.timePickerStyle, 0);
                            delegateField.set(timePicker, delegate); // set the TimePicker.mDelegate to the spinner delegate
                            // Set up the TimePicker again, with the TimePickerSpinnerDelegate
                            timePicker.setIs24HourView(is24HourView);
                            timePicker.setCurrentHour(hourOfDay);
                            timePicker.setCurrentMinute(minute);
                            timePicker.setOnTimeChangedListener(this);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        private Field findField(Class objectClass, Class fieldClass, String expectedName) {
            try {
                Field field = objectClass.getDeclaredField(expectedName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {} // ignore
            // search for it if it wasn't found under the expected ivar name
            for (Field searchField : objectClass.getDeclaredFields()) {
                if (searchField.getType() == fieldClass) {
                    searchField.setAccessible(true);
                    return searchField;
                }
            }
            return null;
        }
    }


}
