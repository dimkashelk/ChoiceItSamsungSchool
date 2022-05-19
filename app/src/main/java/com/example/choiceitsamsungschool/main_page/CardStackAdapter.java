package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.choiceitsamsungschool.R;
import com.google.android.material.card.MaterialCardView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder>{
    private List<Spot> spots = new ArrayList<>();

    public CardStackAdapter(List<Spot> spots) {
        this.spots = spots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_spot, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Spot spot = spots.get(position);
        holder.title.setText(spot.getTitle());
        holder.image.setImageDrawable(spot.getDrawable());
        holder.card.setOnClickListener(v -> Toast.makeText(v.getContext(), "Что-то случилось", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    public Spot getItem(int position) {
        return spots.get(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addSpot(Spot spot) {
        spots.add(spot);
        notifyItemInserted(spots.size() - 1);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAllSpot(List<Spot> list) {
        spots.addAll(list);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        spots.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RoundedImageView image;
        public MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_spot_title);
            image = itemView.findViewById(R.id.item_spot_image);
            card = itemView.findViewById(R.id.item_spot_card);
        }
    }
}
