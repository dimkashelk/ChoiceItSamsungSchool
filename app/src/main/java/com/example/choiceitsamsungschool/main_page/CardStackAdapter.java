package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Что-то случилось", Toast.LENGTH_SHORT).show();
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RoundedImageView image;
        public MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
            View item_spot = inflater.inflate(R.layout.item_spot, null);

            title = item_spot.findViewById(R.id.item_spot_title);
            image = item_spot.findViewById(R.id.item_spot_image);
            card = item_spot.findViewById(R.id.item_spot_card);
        }
    }
}
