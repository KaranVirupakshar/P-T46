package edu.neu.madcourse.studybuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import util.MainActivityTabAdapter;


//TODO Redirect to register page on create group button
//TODO profile click register page
//On guest login find groups/home page -->
public class MainActivity extends AppCompatActivity implements
        MainActivityProfileFragment.OnFragmentInteractionListener,
        Profile_ConnectionListFragment.OnListFragmentInteractionListener,
        Profile_AddConnectionFragment.OnFragmentInteractionListener,
        Profile_ConnectionRequestsFragment.OnListFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.mainToolbar);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.group));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.person));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final MainActivityTabAdapter myAdapter = new MainActivityTabAdapter(
                getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(myAdapter);

        int color = Color.parseColor("#000000");
        tabLayout.getTabAt(viewPager.getCurrentItem()).getIcon()
                .setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(2);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                int color = Color.parseColor("#000000");
                tab.getIcon().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int color = Color.parseColor("#FFFFFF");
                tab.getIcon().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

}
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}