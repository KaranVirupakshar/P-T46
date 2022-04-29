package edu.neu.madcourse.studybuddy.groupArtifacts;

import android.util.Log;
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

        refreshCalls();

        //Go to the chat activity for the group from here once its done
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    void buttonLogicHelper(String documentId, UserGroups userGroups, CollectionReference userAndGroups){
        //Make db calls for the user to join the group
        Set<String> groupIds = new HashSet<>(userGroups.getGroups());
        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference= userAndGroups.document(documentId);
                //If groupId is not present add it to the users groups -> which will form a new group.
                if(!groupIds.contains(groupId)){
                    documentReference.update("groups", FieldValue.arrayUnion(groupId));
                }
                else{
                    documentReference.update("groups", FieldValue.arrayRemove(groupId));
                }
            }
        });
    }

    void refreshCalls(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userAndGroups = db.collection("userGroups");
        Query query;
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        query = userAndGroups.whereEqualTo("user", userId);


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String groupId = "";
                UserGroups userGroups;
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        groupId = documentSnapshot.getId();
                        userGroups = documentSnapshot.toObject(UserGroups.class);
                        buttonLogicHelper(groupId, userGroups, userAndGroups);
                    }
                }
            }
        });
    }

}
