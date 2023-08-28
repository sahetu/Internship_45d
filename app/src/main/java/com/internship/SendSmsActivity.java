package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class SendSmsActivity extends AppCompatActivity {

    EditText contact,message;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        contact = findViewById(R.id.send_sms_contact);
        message = findViewById(R.id.send_sms_message);
        submit = findViewById(R.id.send_sms_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contact.getText().toString().trim().equalsIgnoreCase("")){
                    contact.setError("Contact No. Required");
                }
                else if(contact.getText().toString().trim().length()<10){
                    contact.setError("Valid Contact No. Required");
                }
                else if(message.getText().toString().trim().equalsIgnoreCase("")){
                    message.setError("Message Required");
                }
                else{
                    SmsManager smsManager = SmsManager.getDefault();
                    ArrayList<String> parts = smsManager.divideMessage(message.getText().toString());
                    smsManager.sendMultipartTextMessage(contact.getText().toString(), null, parts, null, null);
                    new CommonMethod(SendSmsActivity.this,"Sms Send Successfully");
                }
            }
        });

    }
}