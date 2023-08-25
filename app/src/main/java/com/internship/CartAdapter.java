package com.internship;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    Context context;
    ArrayList<CartList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;

    public CartAdapter(Context context, ArrayList<CartList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantSp.PREF, Context.MODE_PRIVATE);

        db = context.openOrCreateDatabase("Internship", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tableQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INTEGER(100),USERID INTEGER(100),PRODUCTID INTEGER(100),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTDESCRIPTION TEXT,PRODUCTPRICE VARCHAR(20),PRODUCTQTY VARCHAR(20),TOTALPRICE VARCHAR(50))";
        db.execSQL(cartTableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INTEGER(100),PRODUCTID INTEGER(100),PRODUCTNAME VARCHAR(100),PRODUCTIMAGE VARCHAR(100),PRODUCTDESCRIPTION TEXT,PRODUCTPRICE VARCHAR(20))";
        db.execSQL(wishlistTableQuery);

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart, parent, false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView, removeCart,add,minus;
        TextView name, price, total,qty;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            removeCart = itemView.findViewById(R.id.custom_cart_remove);
            imageView = itemView.findViewById(R.id.custom_cart_image);
            name = itemView.findViewById(R.id.custom_cart_name);
            price = itemView.findViewById(R.id.custom_cart_price);
            total = itemView.findViewById(R.id.custom_cart_total);
            qty = itemView.findViewById(R.id.custom_cart_qty);
            add = itemView.findViewById(R.id.custom_cart_plus);
            minus = itemView.findViewById(R.id.custom_cart_minus);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getProductImage()));
        holder.name.setText(arrayList.get(position).getProductName());
        holder.price.setText(ConstantSp.PRICE_SYMBOL + arrayList.get(position).getProductPrice());
        holder.qty.setText(arrayList.get(position).getProductQty());

        holder.total.setText(ConstantSp.PRICE_SYMBOL + arrayList.get(position).getTotalPrice());

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iQty = Integer.parseInt(arrayList.get(position).getProductQty())+1;
                //int iTotalPrice = Integer.parseInt(arrayList.get(position).getTotalPrice())+Integer.parseInt(arrayList.get(position).getProductPrice());
                int iTotalPrice = iQty * Integer.parseInt(arrayList.get(position).getProductPrice());

                holder.qty.setText(String.valueOf(iQty));
                holder.total.setText(ConstantSp.PRICE_SYMBOL + iTotalPrice);

                CartFragment.iCartTotal += Integer.parseInt(arrayList.get(position).getProductPrice());
                CartFragment.checkout.setText("Checkout "+ConstantSp.PRICE_SYMBOL+CartFragment.iCartTotal);

                String updateQuery = "UPDATE CART SET PRODUCTQTY='"+iQty+"',TOTALPRICE='"+iTotalPrice+"' WHERE CARTID='"+arrayList.get(position).getCartId()+"'";
                db.execSQL(updateQuery);

                CartList list = new CartList();
                list.setCartId(arrayList.get(position).getCartId());
                list.setProductId(arrayList.get(position).getProductId());
                list.setProductName(arrayList.get(position).getProductName());
                list.setProductImage(arrayList.get(position).getProductImage());
                list.setProductPrice(arrayList.get(position).getProductPrice());
                list.setProductDesc(arrayList.get(position).getProductDesc());
                list.setProductQty(String.valueOf(iQty));
                list.setTotalPrice(String.valueOf(iTotalPrice));
                arrayList.set(position,list);
                notifyDataSetChanged();

                if(CartFragment.iCartTotal>0){
                    CartFragment.checkout.setVisibility(View.VISIBLE);
                }
                else{
                    CartFragment.checkout.setVisibility(View.GONE);
                }

            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iQty = Integer.parseInt(arrayList.get(position).getProductQty())-1;
                //int iTotalPrice = Integer.parseInt(arrayList.get(position).getTotalPrice())+Integer.parseInt(arrayList.get(position).getProductPrice());
                int iTotalPrice = iQty * Integer.parseInt(arrayList.get(position).getProductPrice());

                holder.qty.setText(String.valueOf(iQty));
                holder.total.setText(ConstantSp.PRICE_SYMBOL + iTotalPrice);

                CartFragment.iCartTotal -= Integer.parseInt(arrayList.get(position).getProductPrice());
                CartFragment.checkout.setText("Checkout "+ConstantSp.PRICE_SYMBOL+CartFragment.iCartTotal);

                if(iQty>0) {
                    String updateQuery = "UPDATE CART SET PRODUCTQTY='"+iQty+"',TOTALPRICE='"+iTotalPrice+"' WHERE CARTID='"+arrayList.get(position).getCartId()+"'";
                    db.execSQL(updateQuery);

                    CartList list = new CartList();
                    list.setCartId(arrayList.get(position).getCartId());
                    list.setProductId(arrayList.get(position).getProductId());
                    list.setProductName(arrayList.get(position).getProductName());
                    list.setProductImage(arrayList.get(position).getProductImage());
                    list.setProductPrice(arrayList.get(position).getProductPrice());
                    list.setProductDesc(arrayList.get(position).getProductDesc());
                    list.setProductQty(String.valueOf(iQty));
                    list.setTotalPrice(String.valueOf(iTotalPrice));
                    arrayList.set(position, list);
                    notifyDataSetChanged();
                }
                else{
                    String deleteQuery = "DELETE FROM CART WHERE CARTID='"+arrayList.get(position).getCartId()+"'";
                    db.execSQL(deleteQuery);

                    arrayList.remove(position);
                    notifyDataSetChanged();
                }

                if(CartFragment.iCartTotal>0){
                    CartFragment.checkout.setVisibility(View.VISIBLE);
                }
                else{
                    CartFragment.checkout.setVisibility(View.GONE);
                }

            }
        });

        holder.removeCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteQuery = "DELETE FROM CART WHERE CARTID='"+arrayList.get(position).getCartId()+"'";
                db.execSQL(deleteQuery);
                new CommonMethod(context,"Product Remove From Cart Successfully");

                int iProductTotal = Integer.parseInt(arrayList.get(position).getTotalPrice());
                CartFragment.iCartTotal -= iProductTotal;
                CartFragment.checkout.setText("Checkout "+ConstantSp.PRICE_SYMBOL+CartFragment.iCartTotal);

                arrayList.remove(position);
                notifyDataSetChanged();

                if(CartFragment.iCartTotal>0){
                    CartFragment.checkout.setVisibility(View.VISIBLE);
                }
                else{
                    CartFragment.checkout.setVisibility(View.GONE);
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.PRODUCT_ID, arrayList.get(position).getProductId()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_NAME, arrayList.get(position).getProductName()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_IMAGE, String.valueOf(arrayList.get(position).getProductImage())).commit();
                sp.edit().putString(ConstantSp.PRODUCT_PRICE, arrayList.get(position).getProductPrice()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_DESCRIPTION, arrayList.get(position).getProductDesc()).commit();
                new CommonMethod(context, ProductDetailActivity.class);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
