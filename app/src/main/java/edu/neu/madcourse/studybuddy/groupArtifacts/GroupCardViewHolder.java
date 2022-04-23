package edu.neu.madcourse.studybuddy.groupArtifacts;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.studybuddy.R;

public class GroupCardViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView subject;
    public TextView location;
    public Button cardButton;
    private FirebaseDatabase firebaseDatabase;

    public GroupCardViewHolder(@NonNull View itemView, final AdapterView.OnItemClickListener onItemClickListener) {
        super(itemView);
        title = itemView.findViewById(R.id.group_title);
        subject = itemView.findViewById(R.id.group_subject);
        location = itemView.findViewById(R.id.location);
        cardButton = itemView.findViewById(R.id.groupCardButton);

        //Make db calls for the user to join the group
        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //Go to the chat activity for the group from here once its done
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
