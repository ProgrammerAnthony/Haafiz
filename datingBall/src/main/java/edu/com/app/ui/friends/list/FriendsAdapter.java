package edu.com.app.ui.friends.list;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.com.app.R;
import edu.com.app.data.bean.Friends;


public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private List<Friends> mFriends;

    @Inject
    public FriendsAdapter() {
        mFriends = new ArrayList<>();
    }

    public void setFriends(List<Friends> friends) {
        mFriends = friends;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ribot, parent, false);
        return new FriendsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        Friends friends = mFriends.get(position);
        holder.hexColorView.setBackgroundColor(Color.parseColor(friends.profile.hexColor));
        holder.nameTextView.setText(String.format("%s %s",
                friends.profile.name.first, friends.profile.name.last));
        holder.emailTextView.setText(friends.profile.email);
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.view_hex_color)
        View hexColorView;
        @Bind(R.id.text_name)
        TextView nameTextView;
        @Bind(R.id.text_email)
        TextView emailTextView;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
