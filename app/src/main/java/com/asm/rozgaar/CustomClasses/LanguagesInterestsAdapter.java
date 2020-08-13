package com.asm.rozgaar.CustomClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asm.rozgaar.R;

import java.util.ArrayList;

public class LanguagesInterestsAdapter extends RecyclerView.Adapter<LanguagesInterestsAdapter.ListViewHolder> {

    ArrayList<String> list;
    LanguagesInterestsAdapter adapter;
    RecyclerView rv;

    public LanguagesInterestsAdapter(ArrayList<String> list, RecyclerView rv) {
        this.list = list;
        this.rv = rv;
    }
    public void getAdapter(LanguagesInterestsAdapter adapter){
        this.adapter=adapter;
    }

    @NonNull
    @Override
    public LanguagesInterestsAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.interest__recyclerview_item,parent,false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
        holder.itemText.setText(list.get(position));
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView itemText;
        ImageButton cross;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemText=itemView.findViewById(R.id.item_text);
            cross=itemView.findViewById(R.id.clear_button);
        }
    }
}
