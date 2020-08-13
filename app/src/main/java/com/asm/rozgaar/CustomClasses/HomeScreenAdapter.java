package com.asm.rozgaar.CustomClasses;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asm.rozgaar.R;

import java.util.ArrayList;

public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.JobViewHolder> {
    ArrayList<AvailableJob> job_list;
    Context context;

    public HomeScreenAdapter(ArrayList<AvailableJob> job_list, Context context) {
        this.job_list = job_list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeScreenAdapter.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.available_jobs_row,parent,false);
        return new JobViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        AvailableJob job=job_list.get(position);
        if(job.getLogo_URI()==null)
            holder.logo.setImageResource(R.drawable.zomato_logo_vector);
        else
            holder.logo.setImageURI(Uri.parse(job.getLogo_URI()));
        holder.description.setText(job.getDescription());
        holder.address.setText(job.getAddress());
        holder.salary.setText(job.getSalary());
    }
    @Override
    public int getItemCount() {
        return job_list.size();
    }
    public static class JobViewHolder extends RecyclerView.ViewHolder{
        ImageView logo;
        TextView description,address,salary;
        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            logo=itemView.findViewById(R.id.logo_container);
            description=itemView.findViewById(R.id.job_description);
            address=itemView.findViewById(R.id.job_address);
            salary=itemView.findViewById(R.id.job_salary);
        }

    }
}
