package com.example.hhj73.fix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MatchingActivity extends AppCompatActivity {

    ListView matchList;
    ArrayList<String> users;
    ArrayAdapter arrayAdapter;
    DatabaseReference databaseReference;
    String curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        init();
    }

    public void init() {
        Intent intent = getIntent();
        curUser = intent.getStringExtra("curUser");
        matchList = (ListView) findViewById(R.id.matchList);
        users = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);
        matchList.setAdapter(arrayAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                while(child.hasNext()) {
                    String id = child.next().getKey();
                    if(!id.equals(curUser)) {
                        users.add(id);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(MatchingActivity.this, str, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("myID", curUser);
                intent.putExtra("urID", str);
                startActivity(intent);
            }
        });

    }
}