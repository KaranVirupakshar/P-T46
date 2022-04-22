package edu.neu.madcourse.studybuddy.groupArtifacts;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.studybuddy.R;

public class GroupCardViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView dateTime;
    public TextView location;

    public GroupCardViewHolder(@NonNull View itemView, final AdapterView.OnItemClickListener onItemClickListener) {
        super(itemView);
        title = itemView.findViewById(R.id.group_title);
        dateTime = itemView.findViewById(R.id.group_datetime);
        location = itemView.findViewById(R.id.location);

        //Go to the chat activity for the group from here once its done
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
