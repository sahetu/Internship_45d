package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailActivity extends AppCompatActivity {

    TextView name,price,desc;
    ImageView imageView;

    Button buyNow;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        imageView = findViewById(R.id.product_detail_image);
        name = findViewById(R.id.product_detail_name);
        price = findViewById(R.id.product_detail_price);
        desc = findViewById(R.id.product_detail_desc);

        buyNow = findViewById(R.id.product_detail_buy_now);

        name.setText(sp.getString(ConstantSp.PRODUCT_NAME,""));
        price.setText(ConstantSp.PRICE_SYMBOL+sp.getString(ConstantSp.PRODUCT_PRICE,""));

        desc.setText(sp.getString(ConstantSp.PRODUCT_DESCRIPTION,""));

        imageView.setImageResource(Integer.parseInt(sp.getString(ConstantSp.PRODUCT_IMAGE,"")));

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}