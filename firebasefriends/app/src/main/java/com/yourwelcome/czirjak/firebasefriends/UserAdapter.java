package com.yourwelcome.czirjak.firebasefriends;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private final boolean[] checkBoxes;


    public UserAdapter(List<User> users) {
        this.users = users;
        checkBoxes = new boolean[users.size()];
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, final int position) {
        holder.profession.setText(users.get(position).getProfession());

        User user = users.get(position);
        holder.name.setText(users.get(position).getName());

        holder.checkBox.setChecked(false);

        if(checkBoxes[position]){
            holder.checkBox.setChecked(true);
        }
        else{
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                checkBoxes[position] = false;
                user.setSelected(false);
            }
            else{
                checkBoxes[position] = true;
                user.setSelected(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView profession;

        private CheckBox checkBox ;

        public UserViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.txt_name);
            this.profession = itemView.findViewById(R.id.txt_profession);

            this.checkBox =(CheckBox)itemView.findViewById(R.id.checkBox);
        }


    }


}
