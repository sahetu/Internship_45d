package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    SharedPreferences sp;

    Button editProfile,updateProfile;
    EditText name, email, contact, dob;
    TextView logout;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    RadioButton male,female;

    RadioGroup gender;

    Spinner city;
    ArrayList<String> arrayList;

    Calendar calendar;
    String sCity = "";
    String sGender;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        /*String sSharedId = sp.getString(ConstantSp.ID,"");
        String sSharedName = sp.getString(ConstantSp.NAME,"");
        String sSharedEmail = sp.getString(ConstantSp.EMAIL,"");
        String sSharedContact = sp.getString(ConstantSp.CONTACT,"");
        String sSharedGender = sp.getString(ConstantSp.GENDER,"");
        String sSharedCity = sp.getString(ConstantSp.CITY,"");
        String sSharedDob = sp.getString(ConstantSp.DOB,"");*/

        db = openOrCreateDatabase("Internship", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        logout = findViewById(R.id.home_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                new CommonMethod(HomeActivity.this,MainActivity.class);
            }
        });

        name = findViewById(R.id.home_name);
        email = findViewById(R.id.home_email);
        contact = findViewById(R.id.home_contact);

        city = findViewById(R.id.home_city);
        dob = findViewById(R.id.home_dob);

        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                dob.setText(sdf.format(calendar.getTime()));

            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this, dateClick, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

        arrayList = new ArrayList<>();

        arrayList.add("Ahmedabad");
        arrayList.add("Surat");
        arrayList.add("Rajkot");
        arrayList.add("Test");
        arrayList.add("Demo");
        arrayList.add("Gandhinagar");

        arrayList.remove(3);
        arrayList.set(3, "Vadodara");

        arrayList.add(0, "Jamnagar");
        arrayList.add(0, "Select City");

        ArrayAdapter adapter = new ArrayAdapter(HomeActivity.this, android.R.layout.simple_list_item_1, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        city.setAdapter(adapter);

        //city.setSelection(2);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sCity = "";
                    Log.d("City", "Selected City = " + sCity);
                } else {
                    sCity = arrayList.get(i);
                    new CommonMethod(HomeActivity.this, sCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        male = findViewById(R.id.home_male);
        female = findViewById(R.id.home_female);
        gender = findViewById(R.id.home_gender);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i); //R.id.home_male,R.id.home_female;
                sGender = radioButton.getText().toString();
                new CommonMethod(HomeActivity.this, sGender);
            }
        });

        updateProfile = findViewById(R.id.home_update);

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equals("")) {
                    name.setError("Name Required");
                } else if (email.getText().toString().trim().equals("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Valid Email Id Required");
                } else if (contact.getText().toString().trim().equals("")) {
                    contact.setError("Contact No. Required");
                } else if (contact.getText().toString().trim().length() < 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(HomeActivity.this, "Please Select Gender");
                } else if (sCity.equals("")) {
                    new CommonMethod(HomeActivity.this, "Please Select City");
                } else if (dob.getText().toString().trim().equals("")) {
                    dob.setError("Please Select Date Of Birth");
                } else {

                    String selectQuery = "SELECT * FROM USERS WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        String updateQuery = "UPDATE USERS SET NAME='"+name.getText().toString()+"',EMAIL='"+email.getText().toString()+"',CONTACT='"+contact.getText().toString()+"',GENDER='"+sGender+"',CITY='"+sCity+"' WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"'";
                        db.execSQL(updateQuery);
                        new CommonMethod(HomeActivity.this,"Profile Update Successfully");

                        sp.edit().putString(ConstantSp.NAME,name.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL,email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT,contact.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                        sp.edit().putString(ConstantSp.CITY,sCity).commit();
                        sp.edit().putString(ConstantSp.DOB,dob.getText().toString()).commit();

                        setData(false);
                    }
                    else{
                        new CommonMethod(HomeActivity.this, "UserId Does Not Exists");
                    }
                }
            }
        });

        editProfile = findViewById(R.id.home_edit);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });

        setData(false);

    }

    private void setData(boolean isEnable) {
        name.setEnabled(isEnable);
        email.setEnabled(isEnable);
        contact.setEnabled(isEnable);
        dob.setEnabled(isEnable);

        male.setEnabled(isEnable);
        female.setEnabled(isEnable);

        city.setEnabled(isEnable);

        if(isEnable){
            editProfile.setVisibility(View.GONE);
            updateProfile.setVisibility(View.VISIBLE);
        }
        else{
            editProfile.setVisibility(View.VISIBLE);
            updateProfile.setVisibility(View.GONE);
        }

        name.setText(sp.getString(ConstantSp.NAME, ""));
        email.setText(sp.getString(ConstantSp.EMAIL, ""));
        contact.setText(sp.getString(ConstantSp.CONTACT, ""));
        dob.setText(sp.getString(ConstantSp.DOB, ""));

        //female.setChecked(true);
        sGender = sp.getString(ConstantSp.GENDER,"");
        if(sGender.equalsIgnoreCase("Male")){
            male.setChecked(true);
        }
        else if(sGender.equalsIgnoreCase("Female")){
            female.setChecked(true);
        }
        else{

        }

        sCity = sp.getString(ConstantSp.CITY,"");
        int iCityPosition = 0;
        for(int i=0;i<arrayList.size();i++){
            if(sCity.equalsIgnoreCase(arrayList.get(i))){
                iCityPosition = i;
                break;
            }
        }
        city.setSelection(iCityPosition);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finishAffinity();
    }
}