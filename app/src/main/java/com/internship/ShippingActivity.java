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

public class ShippingActivity extends AppCompatActivity {

    Button payNow;
    EditText name, email, contact, address;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    RadioGroup paymentMethod;

    Spinner city;
    ArrayList<String> arrayList;
    String sCity = "";
    String sGender;

    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("Internship", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        name = findViewById(R.id.shipping_name);
        email = findViewById(R.id.shipping_email);
        contact = findViewById(R.id.shipping_contact);
        address = findViewById(R.id.shipping_address);

        name.setText(sp.getString(ConstantSp.NAME,""));
        email.setText(sp.getString(ConstantSp.EMAIL,""));
        contact.setText(sp.getString(ConstantSp.CONTACT,""));

        city = findViewById(R.id.shipping_city);
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

        ArrayAdapter adapter = new ArrayAdapter(ShippingActivity.this, android.R.layout.simple_list_item_1, arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sCity = "";
                    Log.d("City", "Selected City = " + sCity);
                } else {
                    sCity = arrayList.get(i);
                    new CommonMethod(ShippingActivity.this, sCity);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        paymentMethod = findViewById(R.id.shipping_payment);

        paymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i); //R.id.shipping_male,R.id.shipping_female;
                sGender = radioButton.getText().toString();
                new CommonMethod(ShippingActivity.this, sGender);
            }
        });

        payNow = findViewById(R.id.shipping_pay_now);
        payNow.setOnClickListener(new View.OnClickListener() {
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
                } else if (address.getText().toString().trim().equals("")) {
                    address.setError("Address Required");
                } else if (sCity.equals("")) {
                    new CommonMethod(ShippingActivity.this, "Please Select City");
                } else if (paymentMethod.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(ShippingActivity.this, "Please Select Payment Method");
                } else {

                }
            }
        });
    }
}