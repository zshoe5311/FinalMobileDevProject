package edu.uga.cs.finalmobiledevproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.finalmobiledevproject.databinding.ActivityRecentPurchaseBinding;

public class RecentPurchaseActivity extends NavDrawerBaseActivity {

    ActivityRecentPurchaseBinding activityRecentPurchaseBinding;

    private RecyclerView recyclerView;
    private BasketRecyclerAdapter recyclerAdapter;
    private ShoppingItemRecyclerAdapter sRecyclerAdapter;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private List<Basket> basketsList;

    private FirebaseDatabase database;

    private ArrayList<Float> totals;
    private float totalSCost;
    private float userSCost;
    private Basket selectedBasket;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRecentPurchaseBinding = ActivityRecentPurchaseBinding.inflate(getLayoutInflater());
        setContentView(activityRecentPurchaseBinding.getRoot());
        allocateActivityTitle("Previous Purchases");

        Bundle extras = getIntent().getExtras();
        user = (FirebaseUser) extras.get("currentUser");

        recyclerView = findViewById(R.id.rRecyclerView);

        FloatingActionButton floatingButton = findViewById(R.id.rFloatingActionButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settleCost();
            }
        });

        basketsList = getBaskets();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new BasketRecyclerAdapter(basketsList, RecentPurchaseActivity.this);
        recyclerView.setAdapter(recyclerAdapter);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppingitems");

        ArrayList<Basket> baskets = new ArrayList<Basket>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                basketsList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    ShoppingItem si = postSnapshot.getValue(ShoppingItem.class);
                    si.setKey(postSnapshot.getKey());
                    if (si.getPurchasedBasketId() >= 0) {
                        while (si.getPurchasedBasketId() >= baskets.size()) {
                            baskets.add(new Basket());
                            System.out.println("TEST " + baskets.size());
                        }
                        Basket b = baskets.get(si.getPurchasedBasketId());
                        b.addItem(si);
                        b.setId(si.getPurchasedBasketId());
                        b.setUser(si.getBasketingUser());
                        b.recalculateTotalPrice();
                    }
                }
                for (int i = 0; i < baskets.size(); i++) {
                    if (baskets.get(i).getUser() != null) {
                        basketsList.add(baskets.get(i));
                        System.out.println("TEST " + basketsList.size());
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

    @Override
    public void onBackPressed() {
        if (recyclerView.getAdapter() instanceof ShoppingItemRecyclerAdapter) {
            Intent intent = new Intent(RecentPurchaseActivity.this, RecentPurchaseActivity.class);
            intent.putExtra("currentUser", user);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }

    private List<Basket> getBaskets() {
        List<Basket> list = new ArrayList<Basket>();
        for (int i = 0; i < 4; i++) {
            List<ShoppingItem> sList = new ArrayList<ShoppingItem>();
            /*sList.add(new ShoppingItem("0","testItem4", "0.01", "0", "testdesc", "user"));
            sList.add(new ShoppingItem("1","testItem5", "0.02", "0", "testdesc2", "user"));
            sList.add(new ShoppingItem("2","testItem6", "0.03", "0", "testdesc3", "user"));
            sList.add(new ShoppingItem("3","testItem4", "0.01", "0", "testdesc", "user"));
            sList.add(new ShoppingItem("4","testItem5", "0.02", "0", "testdesc2", "user"));
            sList.add(new ShoppingItem("5","testItem6", "0.03", "0", "testdesc3", "user"));*/
            list.add(new Basket("user", i+1, sList));
        }

        return list;
    }

    private void refreshRecyclerView() {
        recyclerAdapter = new BasketRecyclerAdapter(basketsList, RecentPurchaseActivity.this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void refreshSRecyclerAdapter(Basket bas) {
        sRecyclerAdapter = new ShoppingItemRecyclerAdapter(bas.getItems(), RecentPurchaseActivity.this);
        selectedBasket = bas;
        sRecyclerAdapter.setPurchasedBasketId(selectedBasket.getId());
        recyclerView.setAdapter(sRecyclerAdapter);
    }

    public void settleCost() {
        totalSCost = 0;
        DatabaseReference ref = database
                .getReference()
                .child("shoppingitems");
        for (int b = basketsList.size()-1; b >= 0; b--) {
            totalSCost = totalSCost + basketsList.get(b).getTotalPrice();
            for (int j = 0; j < basketsList.get(b).getItems().size(); j++) {
                ShoppingItem s = basketsList.get(b).getItems().get(0);
                basketsList.get(b).removeItem(0);
                ref = ref.child(s.getKey());
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            basketsList.remove(b);
        }
        recyclerAdapter.notifyDataSetChanged();
        userSCost = totalSCost/4;
        Toast toast= Toast.makeText(getApplicationContext(),"Cost Settled! Total Cost: " + df.format(totalSCost) + ", You owe $" + df.format(userSCost) ,Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
    }

    public void viewBasketItems(Basket basket) {
        refreshSRecyclerAdapter(basket);
    }

    public void cancelSelectedBItem(ShoppingItem item, int p) {
        item.setBasketingUser("");
        item.setPurchasedBasketId(-1);
        sRecyclerAdapter.notifyItemRemoved(p);
        DatabaseReference ref = database
                .getReference()
                .child("shoppingitems")
                .child(item.getKey());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().setValue(item);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*selectedBasket.cancelItem(item.getKey());
        refreshSRecyclerAdapter(selectedBasket);*/
    }

    public FirebaseUser getUser() {
        return user;
    }
}