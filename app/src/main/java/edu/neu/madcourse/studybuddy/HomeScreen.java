package edu.neu.madcourse.studybuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.neu.madcourse.studybuddy.groupArtificats.GroupCard;
import edu.neu.madcourse.studybuddy.groupArtificats.GroupCardViewAdapter;

/**
 * A class that represents the home screen for the application.
 */
public class HomeScreen extends AppCompatActivity {
    private List<GroupCard> groupCards;

    private RecyclerView recyclerView;
    private GroupCardViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //Only appears when the user is logged in/ the page redirects to the register /login page
    private FloatingActionButton floatingActionButton;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
}
