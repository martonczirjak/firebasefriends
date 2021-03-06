package com.yourwelcome.czirjak.firebasefriends;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseFirestore db;


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);

//        db = FirebaseFirestore.getInstance();
//        db.collection("friends")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("Here we go", document.getId() + " => " + document.getData());
//                                User user = new User(document.get("name").toString(), document.get("profession").toString());
//                                userList.add(user);
//                            }
//                            UserAdapter userAdapter = new UserAdapter(userList);
//                            recyclerView.setAdapter(userAdapter);
//                            Toast.makeText(getApplicationContext(), String.valueOf(userList.size()).toString(), Toast.LENGTH_LONG).show();
//                        } else {
//                            Log.w("Nope", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
    }

    @Override
    public void onStart() {
        super.onStart();
//         Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(currentUser == null){
            sendToStart();
        }
    }


    public void sendToStart(){
        Intent startInent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startInent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = FirebaseFirestore.getInstance();
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
