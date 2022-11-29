package com.example.qr;

import static com.example.qr.PostScanningFragment.history;
import static com.example.qr.PostScanningFragment.like;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Objects;


public class FavouriteFragment extends Fragment implements favouriteAdapter.OnNoteListener
{

    public static Activity activity;
    RecyclerView recyclerView;
    TextView t1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_favourite, container, false);
        t1=view.findViewById(R.id.no_favourite);
        if(like.size()==0)
            t1.setVisibility(View.VISIBLE);
        else
            t1.setVisibility(View.INVISIBLE);
        recyclerView=view.findViewById(R.id.favview);
        activity=getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new favouriteAdapter(like,  this));
        saveData();
        return view;


    }
    public void saveData()
    {
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(history);
        editor.putString("task list",json);
        editor.apply();

        SharedPreferences sharedPreferences1= getActivity().getSharedPreferences("shared preferences1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        Gson gson1=new Gson();
        String json1=gson1.toJson(like);
        editor1.putString("task list1",json1);
        editor1.apply();
    }

    @Override
    public void onNoteClick(int position) {
        FragmentManager fragmentManager= Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        PostScanningFragment.location=3;
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction().addToBackStack(null);
        fragmentTransaction.replace(R.id.frame_layout,new PostScanningFragment());
        fragmentTransaction.commit();
    }
}