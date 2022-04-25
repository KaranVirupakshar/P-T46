package util;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;

import edu.neu.madcourse.studybuddy.Profile_ConnectionListFragment.OnListFragmentInteractionListener;
import edu.neu.madcourse.studybuddy.R;

import java.util.ArrayList;
import java.util.List;

public class ConnectionRecyclerViewAdapter extends RecyclerView.Adapter<ConnectionRecyclerViewAdapter.ViewHolder> implements SectionIndexer {

    private List<Connection> connectionsList;
    private final OnListFragmentInteractionListener mListener;
    private ArrayList<Integer> mSectionPositions;

    public ConnectionRecyclerViewAdapter(List<Connection> items, OnListFragmentInteractionListener listener) {
        connectionsList = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.connection_creation_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Connection connection = connectionsList.get(position);
        holder.connectionUsername.setText(connection.getUserName());
        holder.connectionFullName.setText(connection.getFullName());
        holder.connectionLogo.setText(connection.getLogo());
        holder.connectionCheckBox.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return connectionsList.size();
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>();
        mSectionPositions = new ArrayList<>();
        for (int i = 0, size = connectionsList.size(); i < size; i++) {
            String section = String.valueOf(connectionsList.get(i).firstName.charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int i) {
        return mSectionPositions.get(i);
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView connectionUsername;
        public final TextView connectionFullName;
        public final TextView connectionLogo;
        public final CheckBox connectionCheckBox;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            connectionUsername = view.findViewById(R.id.ConnectionCheckboxItem_username);
            connectionFullName = view.findViewById(R.id.ConnectionCheckboxItem_fullName);
            connectionLogo = view.findViewById(R.id.ConnectionCheckboxItem_Logo);
            connectionCheckBox = view.findViewById(R.id.ConnectionCheckboxItem_checkBox);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + connectionUsername.getText() + "'";
        }
    }
}
