package com.example.choiceitsamsungschool.main_page;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

public class SpotDiffCallback extends DiffUtil.Callback {
    private List<Spot> old_spots = new ArrayList<>();
    private List<Spot> new_spots = new ArrayList<>();

    @Override
    public int getOldListSize() {
        return old_spots.size();
    }

    @Override
    public int getNewListSize() {
        return new_spots.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old_spots.get(oldItemPosition).id == new_spots.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old_spots.get(oldItemPosition) == new_spots.get(newItemPosition);
    }
}
