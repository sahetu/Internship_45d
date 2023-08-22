package com.internship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class DashboardActivity extends AppCompatActivity {

    MeowBottomNavigation mBottomNavigation;

    int HOME_MENU = 1;
    int CART_MENU = 2;
    int WISHLIST_MENU = 3;
    int PROFILE_MENU = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mBottomNavigation = findViewById(R.id.dashboard_bottom);

        mBottomNavigation.add(new MeowBottomNavigation.Model(HOME_MENU, R.drawable.ic_home));
        mBottomNavigation.add(new MeowBottomNavigation.Model(CART_MENU, R.drawable.ic_cart));
        mBottomNavigation.add(new MeowBottomNavigation.Model(WISHLIST_MENU, R.drawable.ic_wishlist));
        mBottomNavigation.add(new MeowBottomNavigation.Model(PROFILE_MENU, R.drawable.ic_user));

        mBottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
                if(item.getId()==HOME_MENU){
                    mBottomNavigation.show(HOME_MENU,true);
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_relative,new HomeFragment()).commit();
                }
                else if(item.getId()==CART_MENU){
                    mBottomNavigation.show(CART_MENU,true);
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_relative,new CartFragment()).commit();
                }
                else if(item.getId()==WISHLIST_MENU){
                    mBottomNavigation.show(WISHLIST_MENU,true);
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_relative,new WishlistFragment()).commit();
                }
                else if(item.getId()==PROFILE_MENU){
                    mBottomNavigation.show(PROFILE_MENU,true);
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_relative,new ProfileFragment()).commit();
                }
                else{

                }
            }
        });

        mBottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        mBottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        mBottomNavigation.show(HOME_MENU,true);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.dashboard_relative,new HomeFragment()).commit();

    }
}