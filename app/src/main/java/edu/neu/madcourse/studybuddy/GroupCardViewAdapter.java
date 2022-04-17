package edu.neu.madcourse.studybuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * This class represents the adapter for the recycler view shic will be called by the main page
 */
public class GroupCardViewAdapter extends RecyclerView.Adapter<GroupCardViewHolder> {

    private List<GroupCard> groupCards;
    private AdapterView.OnItemClickListener listener;

    public GroupCardViewAdapter(List<GroupCard> groupCards) {
        this.groupCards = groupCards;
    }

    @NonNull
    @Override
    public GroupCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_card, parent, false);
        return new GroupCardViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupCardViewHolder holder, int position) {
        GroupCard currentItem = groupCards.get(position);

        holder.title.setText(currentItem.title);
        holder.dateTime.setText(currentItem.date);
        holder.location.setText(currentItem.location);
    }

    @Override
    public int getItemCount() {
        return groupCards.size();
    }
}
