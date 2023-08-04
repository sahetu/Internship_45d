package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

public class SignupActivity extends AppCompatActivity {

    Button signup;
    EditText name, email, contact, password, confirmPassword,dob;
    TextView login;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //RadioButton male,female;

    RadioGroup gender;

    Spinner city;
    //String[] cityArray = {"Ahmedabad","Gandhinagar","Vadodara","Surat","Rajkot","Patan","Modasa","Mehsana","Bharuch","Ahmedabad","Gandhinagar","Vadodara","Surat","Rajkot","Patan","Modasa","Mehsana","Bharuch"};

    ArrayList<String> arrayList;

    Calendar calendar;
    String sCity = "";
    String sGender;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        db = openOrCreateDatabase("Internship",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        login = findViewById(R.id.signup_login);

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        contact = findViewById(R.id.signup_contact);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirm_password);

        city = findViewById(R.id.signup_city);
        dob = findViewById(R.id.signup_dob);

        calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                dob.setText(sdf.format(calendar.getTime()));

            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this,dateClick,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
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
        arrayList.set(3,"Vadodara");

        arrayList.add(0,"Jamnagar");
        arrayList.add(0,"Select City");

        ArrayAdapter adapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_list_item_1,arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    sCity= "";
                    Log.d("City","Selected City = "+sCity);
                }
                else{
                    sCity = arrayList.get(i);
                    new CommonMethod(SignupActivity.this,sCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*male = findViewById(R.id.signup_male);
        female = findViewById(R.id.signup_female);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(SignupActivity.this,male.getText().toString());
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(SignupActivity.this,female.getText().toString());
            }
        });*/

        gender = findViewById(R.id.signup_gender);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i); //R.id.signup_male,R.id.signup_female;
                sGender = radioButton.getText().toString();
                new CommonMethod(SignupActivity.this,sGender);
            }
        });

        signup = findViewById(R.id.signup_signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
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
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                } else if (password.getText().toString().trim().length() < 6) {
                    password.setError("Min. 6 Char Password Required");
                } else if (confirmPassword.getText().toString().trim().equals("")) {
                    confirmPassword.setError("Confirm Password Required");
                } else if (confirmPassword.getText().toString().trim().length() < 6) {
                    confirmPassword.setError("Min. 6 Char Confirm Password Required");
                }
                else if(!confirmPassword.getText().toString().trim().matches(password.getText().toString())){
                    confirmPassword.setError("Password Does Not Match");
                }
                else if(gender.getCheckedRadioButtonId() == -1){
                    new CommonMethod(SignupActivity.this,"Please Select Gender");
                }
                else if(sCity.equals("")){
                    new CommonMethod(SignupActivity.this,"Please Select City");
                }
                else if(dob.getText().toString().trim().equals("")){
                    dob.setError("Please Select Date Of Birth");
                }
                else {

                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+email.getText().toString()+"' OR CONTACT='"+contact.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        new CommonMethod(SignupActivity.this, "Email Id/Contact No. Already Exists");
                    }
                    else{
                        String insertQuery = "INSERT INTO USERS VALUES(NULL,'"+name.getText().toString()+"','"+email.getText().toString()+"','"+contact.getText().toString()+"','"+password.getText().toString()+"','"+sGender+"','"+sCity+"','"+dob.getText().toString()+"')";
                        db.execSQL(insertQuery);
                        new CommonMethod(SignupActivity.this, "Signup Successfully");
                        new CommonMethod(view, "Signup Successfully");
                        onBackPressed();
                    }
                }
            }
        });
    }
}