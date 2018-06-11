package com.yourwelcome.czirjak.firebasefriends;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.name.setText(users.get(position).getName());
        holder.profession.setText(users.get(position).getProfession());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView profession;

        public UserViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.txt_name);
            this.profession = itemView.findViewById(R.id.txt_profession);

        }


    }

}
