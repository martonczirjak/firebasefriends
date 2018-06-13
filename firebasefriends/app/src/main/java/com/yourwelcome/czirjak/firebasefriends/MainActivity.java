package com.yourwelcome.czirjak.firebasefriends;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

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

    private Toolbar mToolbar;
    private Button mainMenu;

    FirebaseAuth mAuth;

    //pushnotihoz

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
//
//        mToolbar = findViewById(R.id.mainBar);
        getSupportActionBar().setTitle("BeerApp");

        mainMenu = findViewById(R.id.main_logout);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("push")){
                    String message = intent.getStringExtra("message");
                    showNotification("BeerApp", message);
                }
            }
        };


        if( currentUser == null){
            Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(startIntent);
            finish();
        }




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

    private void showNotification(String title, String message) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,b.build());
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
                            userList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Here we go", document.getId() + " => " + document.getData());
                                User user = new User(document.get("name").toString(), document.get("profession").toString(), mAuth.getCurrentUser().getUid().toString());

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
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("regComplete"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("push"));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if(item.getItemId() == R.id.main_logout){

            FirebaseAuth.getInstance().signOut();
            sendToStart();
        }
        return true;
    }
}
