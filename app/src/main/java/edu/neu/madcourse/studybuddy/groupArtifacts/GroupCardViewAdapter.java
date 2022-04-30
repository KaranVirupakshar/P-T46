package edu.neu.madcourse.studybuddy.groupArtifacts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.neu.madcourse.studybuddy.MainActivityHomeFragment;
import edu.neu.madcourse.studybuddy.R;
import edu.neu.madcourse.studybuddy.models.UserGroups;


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

        //Store the group Id so that it can be added to the user
        holder.setGroupId(currentItem.groupId);
        Log.i("Holder log", "The group id is " + currentItem.groupId );
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userAndGroups = db.collection("userGroups");
        Query query;
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        query = userAndGroups.whereEqualTo("user", userId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    UserGroups userGroup;
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        userGroup = documentSnapshot.toObject(UserGroups.class);
                        helper(holder,userGroup,currentItem,currentItem.groupId, position);
                    }
                }
            }
        });

    }


    void helper(GroupCardViewHolder holder, UserGroups userGroup, GroupCard currentItem, String groupId, int position){
        Log.i("helper", groupId);
        Set<String> groupIds = new HashSet<String>(userGroup.getGroups());
        if(groupIds.contains(groupId)){
            holder.cardButton.setText("Leave!");
        }
        else {
            holder.cardButton.setText("Join");
        }
        refreshCalls(holder, groupId, position);
        holder.title.setText(currentItem.title);
        holder.subject.setText(currentItem.subject);
        holder.location.setText(currentItem.location);
    }

    void buttonLogicHelper(String documentId, UserGroups userGroups, CollectionReference userAndGroups, GroupCardViewHolder holder,String groupId, int position){
        //Make db calls for the user to join the group
        Log.i("buttonHelper", groupId);
        Set<String> groupIds = new HashSet<>(userGroups.getGroups());
        holder.cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentReference= userAndGroups.document(documentId);
                //If groupId is not present add it to the users groups -> which will form a new group.
                if(!groupIds.contains(holder.getGroupId())){
                    documentReference.update("groups", FieldValue.arrayUnion(groupId));
                }
                else{
                    documentReference.update("groups", FieldValue.arrayRemove(groupId));
                    for (int i = 0; i < groupCards.size(); i++) {
                        GroupCard currentCard = groupCards.get(i);
                        if (currentCard.groupId.equals(groupId)) {
                            groupCards.remove(i);
                            break;
                        }
                    }
                }
                notifyAdapter(position);
            }
        });
    }

    void notifyAdapter(int position){
        Log.i("notifyAdapter", "This called!");
        this.notifyDataSetChanged();
    }




    void refreshCalls(GroupCardViewHolder holder, String groupId, int position){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userAndGroups = db.collection("userGroups");
        Query query;
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        query = userAndGroups.whereEqualTo("user", userId);


        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String documentGroupId = "";
                UserGroups userGroups;
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        documentGroupId = documentSnapshot.getId();
                        userGroups = documentSnapshot.toObject(UserGroups.class);
                        buttonLogicHelper(documentGroupId, userGroups, userAndGroups, holder, groupId, position);
                    }
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return groupCards!=null ? groupCards.size() : 0;
    }
}
