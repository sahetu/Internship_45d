package com.internship;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    Context context;
    String[] nameArray;
    String[] priceArray;
    String[] descArray;
    int[] imageArray;
    SharedPreferences sp;

    public ProductAdapter(Context context, String[] nameArray, int[] imageArray, String[] priceArray, String[] descArray) {
        this.context = context;
        this.imageArray = imageArray;
        this.nameArray = nameArray;
        this.priceArray = priceArray;
        this.descArray = descArray;
        sp = context.getSharedPreferences(ConstantSp.PREF,Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,price;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.custom_product_image);
            name = itemView.findViewById(R.id.custom_product_name);
            price = itemView.findViewById(R.id.custom_product_price);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.imageView.setImageResource(imageArray[position]);
        holder.name.setText(nameArray[position]);
        holder.price.setText(ConstantSp.PRICE_SYMBOL+priceArray[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.PRODUCT_NAME,nameArray[position]).commit();
                sp.edit().putString(ConstantSp.PRODUCT_IMAGE,String.valueOf(imageArray[position])).commit();
                sp.edit().putString(ConstantSp.PRODUCT_PRICE,priceArray[position]).commit();
                sp.edit().putString(ConstantSp.PRODUCT_DESCRIPTION,descArray[position]).commit();
                new CommonMethod(context, ProductDetailActivity.class);
            }
        });


    }

    @Override
    public int getItemCount() {
        return nameArray.length;
    }

}
