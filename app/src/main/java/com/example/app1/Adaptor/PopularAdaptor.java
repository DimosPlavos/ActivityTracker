package com.example.app1.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app1.Activity.SelectListener;
import com.example.app1.Domain.PopularDomain;
import com.example.app1.R;

import java.util.ArrayList;

public class PopularAdaptor extends RecyclerView.Adapter<PopularAdaptor.ViewHolder> {
    ArrayList<PopularDomain> popularDomains;
    private SelectListener listener;

    public PopularAdaptor(ArrayList<PopularDomain> popularDomains, SelectListener listener){
        this.popularDomains = popularDomains;
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdaptor.ViewHolder holder, int position) {
        holder.popularName.setText(popularDomains.get(position).getName());
        holder.popularRating.setText(String.valueOf(popularDomains.get(position).getRating())); //thetw to pedio rating tou frontend na ginei oso to rating tpu backend

        String picUrl = "";
        holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.cat_background));
        picUrl = popularDomains.get(position).getPic();

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(picUrl,"drawable",holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.popularPic);

    }

    @Override
    public int getItemCount() {
        return popularDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView popularName;
        TextView popularRating;
        ImageView popularPic;
        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            popularName = itemView.findViewById(R.id.popularName);
            popularRating = itemView.findViewById(R.id.popularRating);
            popularPic = itemView.findViewById(R.id.popularPic);
            mainLayout = itemView.findViewById(R.id.mainLayout2); //na to tsekarw mainlayout-mainlayout2
        }
    }
}