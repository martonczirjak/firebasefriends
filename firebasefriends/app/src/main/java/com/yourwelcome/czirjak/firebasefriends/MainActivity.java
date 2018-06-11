package com.yourwelcome.czirjak.firebasefriends;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private List<User> userList = new ArrayList<User>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Here we go", document.getId() + " => " + document.getData());
                                User user = new User(document.get("name").toString(), document.get("profession").toString());
                                userList.add(user);
                            }
                            UserAdapter userAdapter = new UserAdapter(userList);
                            recyclerView.setAdapter(userAdapter);
                            Toast.makeText(getApplicationContext(), String.valueOf(userList.size()).toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.w("Nope", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
