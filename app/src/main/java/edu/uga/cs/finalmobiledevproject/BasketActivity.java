package edu.uga.cs.finalmobiledevproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.finalmobiledevproject.databinding.ActivityBasketBinding;

public class BasketActivity extends NavDrawerBaseActivity {

    ActivityBasketBinding activityBasketBinding;

    private RecyclerView recyclerView;
    private ShoppingItemRecyclerAdapter recyclerAdapter;

    private List<ShoppingItem> basketItemsList;

    private FirebaseDatabase database;
    private String username;
    private int basketId;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBasketBinding = ActivityBasketBinding.inflate(getLayoutInflater());
        setContentView(activityBasketBinding.getRoot());
        allocateActivityTitle("Your Basket");

        Bundle extras = getIntent().getExtras();
        user = (FirebaseUser) extras.get("currentUser");

        recyclerView = findViewById(R.id.bRecyclerView);


        FloatingActionButton floatingButton = findViewById(R.id.bFloatingActionButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (basketItemsList.size() > 0) {
                    DatabaseReference ref = database
                            .getReference()
                            .child("shoppingitems");
                    for (int i = basketItemsList.size() - 1; i >= 0; i--) {
                        ShoppingItem basItem = basketItemsList.get(i);
                        basItem.setPurchasedBasketId(basketId);
                        ref = ref.child(basItem.getKey());
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().setValue(basItem);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        basketItemsList.remove(i);
                        ref = database.getReference().child("shoppingitems");
                    }
                    recyclerAdapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(getApplicationContext(), "Your Basket is purchased!", Toast.LENGTH_SHORT);
                    toast.setMargin(50, 50);
                    toast.show();
                }
            }
        });

        basketItemsList = getBasketItems(username);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new ShoppingItemRecyclerAdapter(basketItemsList, BasketActivity.this);
        recyclerView.setAdapter(recyclerAdapter);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppingitems");

        basketId = 0;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                basketItemsList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    ShoppingItem si = postSnapshot.getValue(ShoppingItem.class);
                    if (si.getPurchasedBasketId() >= basketId)
                        basketId = si.getPurchasedBasketId() + 1;
                    si.setKey(postSnapshot.getKey());
                    if (si.getBasketingUser().equals(user.getUid()) && si.getPurchasedBasketId() < 0) {
                        basketItemsList.add(si);
                    }
                }
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("ValueEventListner: reading failed");
            }
        });
    }

    private void refreshRecyclerView() {
        recyclerAdapter = new ShoppingItemRecyclerAdapter(basketItemsList, BasketActivity.this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private List<ShoppingItem> getBasketItems(String u) {
        List<ShoppingItem> list = new ArrayList<ShoppingItem>();
        /*list.add(new ShoppingItem("0","testItem4", "0.01", "0", "testdesc", u));
        list.add(new ShoppingItem("1","testItem5", "0.02", "0", "testdesc2", u));
        list.add(new ShoppingItem("2","testItem6", "0.03", "0", "testdesc3", u));
        list.add(new ShoppingItem("3","testItem4", "0.01", "0", "testdesc", u));
        list.add(new ShoppingItem("4","testItem5", "0.02", "0", "testdesc2", u));
        list.add(new ShoppingItem("5","testItem6", "0.03", "0", "testdesc3", u));*/
        return list;
    }

    public void returnItemToList(ShoppingItem s, int p) {
        s.setBasketingUser("");
        recyclerAdapter.notifyItemRemoved(p);
        DatabaseReference ref = database
                .getReference()
                .child("shoppingitems")
                .child(s.getKey());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().setValue(s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*s.setBasketingUser("");
        basketItemsList.remove(s);
        refreshRecyclerView();*/
        Toast toast= Toast.makeText(getApplicationContext(),"Item Returned to the List",Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
    }

    public FirebaseUser getUser() {
        return user;
    }
}