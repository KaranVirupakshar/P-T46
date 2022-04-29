package edu.neu.madcourse.studybuddy.groupArtifacts;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.neu.madcourse.studybuddy.R;
import edu.neu.madcourse.studybuddy.models.UserGroups;

public class GroupCardViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView subject;
    public TextView location;
    public Button cardButton;

    private String groupId;

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    private FirebaseDatabase firebaseDatabase;

    public GroupCardViewHolder(@NonNull View itemView, final AdapterView.OnItemClickListener onItemClickListener) {
        super(itemView);
        title = itemView.findViewById(R.id.group_title);
        subject = itemView.findViewById(R.id.group_subject);
        location = itemView.findViewById(R.id.group_location);
        cardButton = itemView.findViewById(R.id.groupCardButton);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userAndGroups = db.collection("userGroups");
        Query query;
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        query = userAndGroups.whereEqualTo("user", userId);

        //The groups the user belongs to.
        final UserGroups[] userGroup = new UserGroups[1];
        final String[] documentId = new String[1];
        //fetch all the group ids the user belongs to
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        documentId[0] = documentSnapshot.getId();
                        userGroup[0] = documentSnapshot.toObject(UserGroups.class);
                    }
                }
            }
        });

        //Obtain all the groupIds here
        Set<String> groupIds = new HashSet<String>(userGroup[0].getGroups());

        //Make db calls for the user to join the group
        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference= userAndGroups.document(documentId[0]);

                //If groupId is not present add it to the users groups -> which will form a new group.
                if(!groupIds.contains(groupId)){
                    documentReference.update("groups", FieldValue.arrayUnion(groupId));
                }
                else{
                    documentReference.update("groups", FieldValue.arrayRemove(groupId));
                }
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
