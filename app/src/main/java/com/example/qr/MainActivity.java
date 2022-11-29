package com.example.qr;

import static com.example.qr.PostScanningFragment.history;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    static int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        history=new ArrayList<>();
        PostScanningFragment.like=new ArrayList<>();
        loadData();


        nav=(NavigationView) findViewById(R.id.navmenu);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        replaceFragment(new ScannerFragment());
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {

                    case R.id.scan:
                        menuItem.setIcon(R.drawable.ic_baseline_qr_code_241);

                        toolbar.setTitle("Scanner");
                        replaceFragment(new ScannerFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.history:
                        menuItem.setIcon(R.drawable.ic_baseline_history_24);
                        toolbar.setTitle("History");
                        replaceFragment(new HistoryFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.favourite:
                        menuItem.setIcon(R.drawable.ic_baseline_favorite_24);
                        toolbar.setTitle("Favourite");
                        replaceFragment(new FavouriteFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.share:
                        menuItem.setIcon(R.drawable.ic_baseline_share_24);
                        //toolbar.setTitle("Share");
                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT,"QR Scanner App ");
                        intent.putExtra(intent.EXTRA_TEXT,"Url of my app");
                        startActivity(Intent.createChooser(intent,"Share Via.."));
                        //replaceFragment(new ShareFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.about:
                        menuItem.setIcon(R.drawable.ic_baseline_info_24);
                        toolbar.setTitle("Information");
                        replaceFragment(new InformationFragment());
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });



    }
    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
    public  void loadData()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString("task list",null);
        Type type=new TypeToken<ArrayList<Result>>(){}.getType();
        history=gson.fromJson(json,type);

        if(history==null)
        {
            history=new ArrayList<>();
        }

        SharedPreferences sharedPreferences1=getSharedPreferences("shared preferences1", Context.MODE_PRIVATE);
        Gson gson1=new Gson();
        String json1=sharedPreferences1.getString("task list1",null);
        Type type1=new TypeToken<ArrayList<Result>>(){}.getType();
        PostScanningFragment.like=gson1.fromJson(json1,type1);

        if(PostScanningFragment.like==null)
        {
            PostScanningFragment.like=new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
       // super.onBackPressed();
    }
}