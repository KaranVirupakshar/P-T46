package util;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.neu.madcourse.studybuddy.Profile_ConnectionRequestsFragment.OnListFragmentInteractionListener;
import edu.neu.madcourse.studybuddy.R;

import java.lang.ref.WeakReference;
import java.util.List;


public class ConnectionRequestRecyclerViewAdapter extends RecyclerView.Adapter<ConnectionRequestRecyclerViewAdapter.ViewHolder> {

    private List<Connection> pendingConnectionsList;
    private final OnListFragmentInteractionListener mListener;
    private AcceptRejectButtonListener acceptRejectButtonListener;

    public ConnectionRequestRecyclerViewAdapter(List<Connection> items, OnListFragmentInteractionListener listener, AcceptRejectButtonListener acceptRejectButtonListener) {
        this.pendingConnectionsList = items;
        this.mListener = listener;
        this.acceptRejectButtonListener = acceptRejectButtonListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_connection_request, parent, false);
        return new ViewHolder(view, this.acceptRejectButtonListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Connection connection = pendingConnectionsList.get(position);

        holder.userName.setText(connection.getUserName());
        holder.fullName.setText(connection.getFullName());
        holder.personLogo.setText(connection.getLogo());

    }

    @Override
    public int getItemCount() {
        return pendingConnectionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View mView;
        public final TextView personLogo;
        public final TextView userName;
        public final TextView fullName;
        public final Button acceptButton;
        public final Button rejectButton;

        public WeakReference<AcceptRejectButtonListener> listener;

        public ViewHolder(View view, AcceptRejectButtonListener acceptRejectButtonListener) {
            super(view);

            this.mView = view;
            this.personLogo = view.findViewById(R.id.pendingConnectionLogo);
            this.userName = view.findViewById(R.id.pendingConnectionNameRequest);
            this.fullName = view.findViewById(R.id.pendingConnectionFullName);
            this.acceptButton = view.findViewById(R.id.acceptButton);
            this.rejectButton = view.findViewById(R.id.rejectButton);
            this.listener = new WeakReference<>(acceptRejectButtonListener);
            this.acceptButton.setOnClickListener(this);
            this.rejectButton.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + userName.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.acceptButton:
                    this.listener.get().onAccept(getLayoutPosition());
                    break;
                case R.id.rejectButton:
                    this.listener.get().onReject(getLayoutPosition());
                    break;
                default:
                    break;
            }
        }
    }
}