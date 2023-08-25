package com.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    TextView name, price, desc;
    ImageView imageView;

    Button buyNow,addCart,removeCart,addWishlist,removeWishlist;

    SharedPreferences sp;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = openOrCreateDatabase("Internship",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INTEGER(100),USERID INTEGER(100),PRODUCTID INTEGER(100),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTDESCRIPTION TEXT,PRODUCTPRICE VARCHAR(20),PRODUCTQTY VARCHAR(20),TOTALPRICE VARCHAR(50))";
        db.execSQL(cartTableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INTEGER(100),PRODUCTID INTEGER(100),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTDESCRIPTION TEXT,PRODUCTPRICE VARCHAR(20))";
        db.execSQL(wishlistTableQuery);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        imageView = findViewById(R.id.product_detail_image);
        name = findViewById(R.id.product_detail_name);
        price = findViewById(R.id.product_detail_price);
        desc = findViewById(R.id.product_detail_desc);

        addWishlist = findViewById(R.id.product_detail_add_wishlist);
        removeWishlist = findViewById(R.id.product_detail_remove_wishlist);

        addCart = findViewById(R.id.product_detail_add_cart);
        removeCart = findViewById(R.id.product_detail_remove_cart);

        buyNow = findViewById(R.id.product_detail_buy_now);

        name.setText(sp.getString(ConstantSp.PRODUCT_NAME, ""));
        price.setText(ConstantSp.PRICE_SYMBOL + sp.getString(ConstantSp.PRODUCT_PRICE, ""));

        desc.setText(sp.getString(ConstantSp.PRODUCT_DESCRIPTION, ""));

        imageView.setImageResource(Integer.parseInt(sp.getString(ConstantSp.PRODUCT_IMAGE, "")));

        Checkout.preload(getApplicationContext());

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });

        String selectCartQuery = "SELECT * FROM CART WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"' AND PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"'";
        Cursor cursorCart = db.rawQuery(selectCartQuery,null);
        if(cursorCart.getCount()>0){
            removeCart.setVisibility(View.VISIBLE);
            addCart.setVisibility(View.GONE);
        }
        else {
            removeCart.setVisibility(View.GONE);
            addCart.setVisibility(View.VISIBLE);
        }

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectQuery = "SELECT * FROM CART WHERE ORDERID='0' AND USERID='"+sp.getString(ConstantSp.ID,"")+"' AND PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"'";
                Cursor cursor = db.rawQuery(selectQuery,null);
                if(cursor.getCount()>0){
                    new CommonMethod(ProductDetailActivity.this,"Product Already Added In Cart");
                }
                else {
                    int iQty = 1;
                    int iTotalPrice = Integer.parseInt(sp.getString(ConstantSp.PRODUCT_PRICE, "")) * iQty;
                    String insertQuery = "INSERT INTO CART VALUES(NULL,'0','" + sp.getString(ConstantSp.ID, "") + "','" + sp.getString(ConstantSp.PRODUCT_ID, "") + "','" + sp.getString(ConstantSp.PRODUCT_NAME, "") + "','" + sp.getString(ConstantSp.PRODUCT_IMAGE, "") + "','" + sp.getString(ConstantSp.PRODUCT_DESCRIPTION, "") + "','" + sp.getString(ConstantSp.PRODUCT_PRICE, "") + "','" + iQty + "','" + iTotalPrice + "')";
                    db.execSQL(insertQuery);
                    new CommonMethod(ProductDetailActivity.this, "Product Added In Cart Successfully");

                    addCart.setVisibility(View.GONE);
                    removeCart.setVisibility(View.VISIBLE);
                }
            }
        });

        removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteQuery = "DELETE FROM CART WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"' AND PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"'";
                db.execSQL(deleteQuery);
                new CommonMethod(ProductDetailActivity.this,"Product Remove From Cart Successfully");
                removeCart.setVisibility(View.GONE);
                addCart.setVisibility(View.VISIBLE);
            }
        });

        String selectQuery = "SELECT * FROM WISHLIST WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"' AND PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            removeWishlist.setVisibility(View.VISIBLE);
            addWishlist.setVisibility(View.GONE);
        }
        else {
            removeWishlist.setVisibility(View.GONE);
            addWishlist.setVisibility(View.VISIBLE);
        }

        removeWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteQuery = "DELETE FROM WISHLIST WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"' AND PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"'";
                db.execSQL(deleteQuery);
                new CommonMethod(ProductDetailActivity.this,"Product Remove From Wishlist Successfully");
                removeWishlist.setVisibility(View.GONE);
                addWishlist.setVisibility(View.VISIBLE);
            }
        });

        addWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectQuery = "SELECT * FROM WISHLIST WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"' AND PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"'";
                Cursor cursor = db.rawQuery(selectQuery,null);
                if(cursor.getCount()>0){
                    new CommonMethod(ProductDetailActivity.this,"Product Already Added In Wishlist");
                }
                else {
                    String insertQuery = "INSERT INTO WISHLIST VALUES(NULL,'" + sp.getString(ConstantSp.ID, "") + "','" + sp.getString(ConstantSp.PRODUCT_ID, "") + "','" + sp.getString(ConstantSp.PRODUCT_NAME, "") + "','" + sp.getString(ConstantSp.PRODUCT_IMAGE, "") + "','" + sp.getString(ConstantSp.PRODUCT_DESCRIPTION, "") + "','" + sp.getString(ConstantSp.PRODUCT_PRICE, "") + "')";
                    db.execSQL(insertQuery);
                    new CommonMethod(ProductDetailActivity.this, "Product Added In Wishlist Successfully");

                    addWishlist.setVisibility(View.GONE);
                    removeWishlist.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        co.setKeyID("rzp_test_xsiOz9lYtWKHgF");

        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Online Order Purchase");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://emojiisland.com/cdn/shop/products/Flushed_Face_Emoji_grande.png?v=1571606037");
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(sp.getString(ConstantSp.PRODUCT_PRICE, ""))*100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", sp.getString(ConstantSp.EMAIL,""));
            preFill.put("contact", sp.getString(ConstantSp.CONTACT,""));

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            Toast.makeText(this, "Payment Successful :\nPayment ID: " + s + "\nPayment Data: " + paymentData.getData(), Toast.LENGTH_SHORT).show();
            Log.d("RESPONSE_SUCCESS","Payment Successful :\nPayment ID: " + s + "\nPayment Data: " + paymentData.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            Toast.makeText(this, "Payment Failed:\nPayment Data: " + paymentData.getData(), Toast.LENGTH_SHORT).show();
            Log.d("RESPONSE_FAIL","Payment Failed:\nPayment Data: " + paymentData.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}