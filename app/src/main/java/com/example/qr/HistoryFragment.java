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

import org.w3c.dom.Text;

import java.util.Objects;


public class HistoryFragment extends Fragment implements historyAdapter.OnNoteListener {

    public static Activity activity;
    RecyclerView recyclerView;
    View view;
    TextView t1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        view= inflater.inflate(R.layout.fragment_history, container, false);

        t1= view.findViewById(R.id.no_history);
        if(history.size()==0)
            t1.setVisibility(View.VISIBLE);
        else
            t1.setVisibility(View.INVISIBLE);
        recyclerView=view.findViewById(R.id.hisview);
        activity=getActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(new historyAdapter(history,this));
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
        PostScanningFragment.location=2;
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction().addToBackStack(null);
        fragmentTransaction.replace(R.id.frame_layout,new PostScanningFragment());
        fragmentTransaction.commit();
    }


}