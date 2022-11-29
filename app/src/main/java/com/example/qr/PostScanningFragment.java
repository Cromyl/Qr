package com.example.qr;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import android.content.ClipboardManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;

public class PostScanningFragment extends Fragment {
    public static int location;
    public static ArrayList<Result> history;
    public static ArrayList<Result> like;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        TextView t1=getActivity().findViewById(R.id.test);
        t1.setText(ScannerFragment.scan.getText());
        TextView heading=getActivity().findViewById(R.id.heading);
        ImageView image=getActivity().findViewById(R.id.headingImg);
        loadData();
        loadData();
        ImageButton liked=getActivity().findViewById(R.id.like_button);
        boolean check=false;
        if(like!=null) {
            for (Result k1 : like) {
                if (k1.getText().equals(ScannerFragment.scan.toString()) && k1.getTimestamp() == ScannerFragment.scan.getTimestamp()) {
                    check = true;
                    liked.setImageResource(R.drawable.ic_baseline_favorite_24);
                    break;
                }
            }
        }
        if(check_URL(t1.getText().toString()))
        {
            image.setImageResource(R.drawable.ic_baseline_link_24);
            heading.setText("URL");
        }
        else
        {
            image.setImageResource(R.drawable.ic_baseline_text_fields_24);
            heading.setText("TEXT");
        }
        if(location==1)
            history.add(0,ScannerFragment.scan);
        saveData();

        ImageButton copy=getActivity().findViewById(R.id.copy_button);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager=(ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip=ClipData.newPlainText("editText",t1.getText());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Copied To ClipBoard !!", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton search=getActivity().findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(getContext(), "Searching...", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY,t1.getText().toString());
                    startActivity(intent);
                }catch (Exception e){}


            }
        });

        ImageButton share=getActivity().findViewById(R.id.share_button);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"");
                intent.putExtra(intent.EXTRA_TEXT,t1.getText().toString());
                startActivity(Intent.createChooser(intent,"Share Via.."));

            }
        });

        Log.d("Check karo", "onActivityCreated: "+like);

        Button camera=getActivity().findViewById(R.id.camera_button);
        Button back=getActivity().findViewById(R.id.back_button);
        if(location==1)
        {
            camera.setVisibility(View.VISIBLE);
        }
        else
        {
            back.setVisibility(View.VISIBLE);
        }

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
                replaceFragment(new ScannerFragment());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(location==2)
                    replaceFragment(new HistoryFragment());
                else
                    replaceFragment(new FavouriteFragment());
            }
        });



        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check=false;
                Result temp=null;
                for(Result k1:like)
                {
                    if(k1.getText().equals(ScannerFragment.scan.toString())&&k1.getTimestamp()==ScannerFragment.scan.getTimestamp())
                    {
                        check=true;
                        temp=k1;
                        break;
                    }
                }
                if(check)
                {
                    liked.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    Toast.makeText(getContext(), "Removed from liked list", Toast.LENGTH_SHORT).show();
                    like.remove(like.indexOf(temp));
                }
                else {
                    liked.setImageResource(R.drawable.ic_baseline_favorite_24);
                    Toast.makeText(getContext(), "Added to your liked list", Toast.LENGTH_SHORT).show();
                    like.add(0, ScannerFragment.scan);
                }
                saveData();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment





        return inflater.inflate(R.layout.fragment_post_scanning, container, false);
    }

    public static boolean check_URL(String str) {
        try {
            new URL(str).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void saveData()
    {
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(history);
        editor.putString("task list",json);
        editor.apply();

        SharedPreferences sharedPreferences1= getActivity().getSharedPreferences("shared preferences1",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        Gson gson1=new Gson();
        String json1=gson1.toJson(like);
        editor1.putString("task list1",json1);
        editor1.apply();


    }

    public  void loadData()
    {

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString("task list",null);
        Type type=new TypeToken<ArrayList<Result>>(){}.getType();
        history=gson.fromJson(json,type);

        if(history==null)
        {
            history=new ArrayList<>();
        }


        SharedPreferences sharedPreferences1=getActivity().getSharedPreferences("shared preferences1",Context.MODE_PRIVATE);
        Gson gson1=new Gson();
        String json1=sharedPreferences1.getString("task list1",null);
        Type type1=new TypeToken<ArrayList<Result>>(){}.getType();
        like=gson1.fromJson(json1,type1);

        if(like==null)
        {
            like=new ArrayList<>();
        }
    }

    public void replaceFragment(Fragment fragment)
    {
        PostScanningFragment.location=1;
        FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

}