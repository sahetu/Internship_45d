package com.internship;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;

    String[] idArray = {"1","2","3","4","5"};
    String[] nameArray = {"Bread","Cloth","Makeup Kit","Bread","Cloth","Makeup Kit"};
    int[] imageArray = {R.drawable.bread,R.drawable.cloth,R.drawable.makup_kit,R.drawable.bread,R.drawable.cloth,R.drawable.makup_kit};

    String[] priceArray = {"50","2000","4000","50","2000","4000"};
    String[] descArray = {
            "Bread, baked food product made of flour or meal that is moistened, kneaded, and sometimes fermented. A major food since prehistoric times, it has been made in various forms using a variety of ingredients and methods throughout the world.",
            "Piece of cloth - a separate part consisting of fabric. piece of material. bib - top part of an apron; covering the chest. chamois cloth - a piece of chamois used for washing windows or cars. dishcloth, dishrag - a cloth for washing dishes.",
            "What does a makeup kit consist of? Ideally, a complete makeup kit consists of a moisturizer, skin tint (like foundation or tinted primer), concealer, lip product, bronzer, blush, and mascara",
            "Bread, baked food product made of flour or meal that is moistened, kneaded, and sometimes fermented. A major food since prehistoric times, it has been made in various forms using a variety of ingredients and methods throughout the world.",
            "Piece of cloth - a separate part consisting of fabric. piece of material. bib - top part of an apron; covering the chest. chamois cloth - a piece of chamois used for washing windows or cars. dishcloth, dishrag - a cloth for washing dishes.",
            "What does a makeup kit consist of? Ideally, a complete makeup kit consists of a moisturizer, skin tint (like foundation or tinted primer), concealer, lip product, bronzer, blush, and mascara"
    };

    RecyclerView categoryRecyclerview;
    String[] categoryNameArray = {"Travel","Fashion","Health","Beauty","Food Drink"};
    int[] categoryImageArray = {R.drawable.travel,R.drawable.fashion,R.drawable.health,R.drawable.beauty,R.drawable.food_drink};

    ArrayList<CategoryList> arrayList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.home_recyclerview);

        //Display Data In List
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Display Data In Grid
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        ProductAdapter adapter = new ProductAdapter(getActivity(),nameArray,imageArray,priceArray,descArray,idArray);
        recyclerView.setAdapter(adapter);

        categoryRecyclerview = view.findViewById(R.id.home_recyclerview_category);
        categoryRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        categoryRecyclerview.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerview.setNestedScrollingEnabled(false);

        arrayList = new ArrayList<>();
        for(int i=0;i<categoryNameArray.length;i++){
            CategoryList  list = new CategoryList();
            list.setName(categoryNameArray[i]);
            list.setImage(categoryImageArray[i]);
            arrayList.add(list);
        }
        CategoryAdapter catAdapter = new CategoryAdapter(getActivity(),arrayList);
        categoryRecyclerview.setAdapter(catAdapter);
        return view;
    }
}