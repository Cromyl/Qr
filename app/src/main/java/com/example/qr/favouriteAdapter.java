package com.example.qr;

import static com.example.qr.PostScanningFragment.history;
import static com.example.qr.PostScanningFragment.like;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.zxing.Result;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class favouriteAdapter extends RecyclerView.Adapter<favouriteAdapter.myviewholder> {
    ArrayList<Result> dataholder;
    private OnNoteListener mOnNoteListener;

    public favouriteAdapter(ArrayList<Result> dataholder, OnNoteListener onNoteListener) {
        this.dataholder = dataholder;
        this.mOnNoteListener=onNoteListener;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row,parent,false);
        return new myviewholder(view,mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        boolean ans=false;
        if(check_URL(like.get(position).getText())) {
            holder.img.setImageResource(R.drawable.ic_baseline_link_24);
            holder.header.setText("URL");
        }
        else {
            holder.header.setText("TEXT");
            holder.img.setImageResource(R.drawable.ic_baseline_text_fields_24);
        }

        holder.desc.setText("Upload Time - "+new Date(dataholder.get(position).getTimestamp())+" Link/Text -  "+dataholder.get(position).getText());


    }


    @Override
    public int getItemCount() {
        return like.size();
    }
    public static boolean check_URL(String str) {
        try {
            new URL(str).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView img;
        TextView header,desc;
        ImageButton imgButton;
        OnNoteListener onNoteListener;
        public myviewholder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            img=itemView.findViewById(R.id.row_image);
            header=itemView.findViewById(R.id.row_heading);
            desc=itemView.findViewById(R.id.row_details);
            imgButton=itemView.findViewById(R.id.row_delete);
            this.onNoteListener= onNoteListener;

            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //history.remove(getAdapterPosition());
                    like.remove(getAdapterPosition());
                    saveData();
                    notifyDataSetChanged();
                    //history.remove(getAdapterPosition());
                    Toast.makeText(itemView.getContext(), "Item Removed from liked list", Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            ScannerFragment.scan=like.get(getAdapterPosition());
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public void saveData()
    {
        SharedPreferences sharedPreferences= HistoryFragment.activity.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(history);
        editor.putString("task list",json);
        editor.apply();

        SharedPreferences sharedPreferences1= HistoryFragment.activity.getSharedPreferences("shared preferences1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1= sharedPreferences1.edit();
        Gson gson1=new Gson();
        String json1=gson1.toJson(like);
        editor1.putString("task list1",json1);
        editor1.apply();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
