package edu.uga.cs.finalmobiledevproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.finalmobiledevproject.databinding.ActivityViewShopListBinding;

public class ViewShopListActivity extends NavDrawerBaseActivity
        implements AddShopItemDialogFragment.AddShopItemDialogListener,
        EditShopItemDialogFragment.EditShopItemDialogListener {

    ActivityViewShopListBinding activityViewShopListBinding;

    public static final String DEBUG_TAG = "ViewShopListActivity";

    private RecyclerView recyclerView;
    private ShoppingItemRecyclerAdapter recyclerAdapter;

    private List<ShoppingItem> shopItemsList;

    private FirebaseDatabase database;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        activityViewShopListBinding = ActivityViewShopListBinding.inflate(getLayoutInflater());
        setContentView(activityViewShopListBinding.getRoot());
        allocateActivityTitle("Shopping List");

        Bundle extras = getIntent().getExtras();
        user = (FirebaseUser) extras.get("currentUser");

        recyclerView = findViewById(R.id.recyclerView);

        FloatingActionButton floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new AddShopItemDialogFragment();
                newFragment.show(getSupportFragmentManager(), null);
            }
        });

        shopItemsList = new ArrayList<ShoppingItem>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new ShoppingItemRecyclerAdapter(shopItemsList, ViewShopListActivity.this);
        recyclerView.setAdapter(recyclerAdapter);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppingitems");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int previousListSize = shopItemsList.size();
                shopItemsList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    ShoppingItem si = postSnapshot.getValue(ShoppingItem.class);
                    si.setKey(postSnapshot.getKey());
                    if ((si.getBasketingUser() == null || si.getBasketingUser().isEmpty()) && si.getPurchasedBasketId() < 0) {
                        shopItemsList.add(si);
                    }
                }
                String updateMessage = "List Updated";
                System.out.println(previousListSize + ", " + shopItemsList.size());
                if (previousListSize > shopItemsList.size()) {
                    updateMessage = updateMessage + ": " + (previousListSize - shopItemsList.size()) + " Item(s) Deleted.";
                } else if (previousListSize == shopItemsList.size() - 1) {
                    updateMessage = updateMessage + ": " + (shopItemsList.size() - previousListSize) + " Item(s) Added.";
                }

                recyclerAdapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(getApplicationContext(),updateMessage,Toast. LENGTH_SHORT);
                //toast.setMargin(50,50);
                toast.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("ValueEventListner: reading failed");
            }
        });

    }

    private void refreshRecyclerView() {
        recyclerAdapter = new ShoppingItemRecyclerAdapter(shopItemsList, ViewShopListActivity.this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public List<ShoppingItem> getShopItems () {
        List<ShoppingItem> list = new ArrayList<ShoppingItem>();
        /*list.add(new ShoppingItem("0","testItem", "0.01", "0", "testdesc", null));
        list.add(new ShoppingItem("1","testItem2", "0.02", "0", "testdesc2", null));
        list.add(new ShoppingItem("2","testItem3", "0.03", "0", "testdesc3", null));
        list.add(new ShoppingItem("3","testItem", "0.01", "0", "testdesc", null));
        list.add(new ShoppingItem("4","testItem2", "0.02", "0", "testdesc2", null));
        list.add(new ShoppingItem("5","testItem3", "0.03", "0", "testdesc3", null));*/
        return list;
    }

    public void deleteShopItem(String k, int pos) {
        shopItemsList.remove(pos);
        recyclerAdapter.notifyItemRemoved(pos);
        DatabaseReference ref = database
                .getReference()
                .child("shoppingitems")
                .child(k);

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

    public void editShopItem(ShoppingItem s, int p) {
        EditShopItemDialogFragment editItemFragment =
                EditShopItemDialogFragment.newInstance(s.getKey(), p, s.getItemName(), s.getItemPrice(),
                        s.getPurchasedBasketId(), s.getBasketingUser(), s.getDescription());
        editItemFragment.show(getSupportFragmentManager(), null);
        /*ShoppingItem toEdi = new ShoppingItem();
        for (ShoppingItem s: shopItemsList) {
            if (s.getId() == index) {
                toEdi = s;
            }
        }*/
        /* editItemFragment = EditShopItemDialogFragment.newInstance(
                p, toEdi.getId(), toEdi.getItemName(), toEdi.getItemPrice(),
                toEdi.getPurchasedBasketId(), toEdi.getBasketingUser(), toEdi.getDescription());
        editItemFragment.show(getSupportFragmentManager(), null);*/
    }

    public void addShopItem(ShoppingItem item, int i) {
        //Code about moving item to basket will appear here
        deleteShopItem(item.getKey(), i);
        item.setBasketingUser(user.getUid());
        createShopItem(item);
    }

    public void updateShopItem(int position, ShoppingItem shopItem, int action) {
        /*recyclerAdapter.notifyItemChanged(position);
        shopItemsList.set(position, shopItem);*/

        recyclerAdapter.notifyItemChanged(position);
        DatabaseReference ref = database
                .getReference()
                .child("shoppingitems")
                .child(shopItem.getKey());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().setValue(shopItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void createShopItem(ShoppingItem shopItem) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shoppingitems");

        myRef.push().setValue(shopItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                if (shopItemsList.size() > 0)
                                    recyclerView.smoothScrollToPosition(shopItemsList.size()-1);
                            }
                        });
                        /*Toast toast= Toast.makeText(getApplicationContext(),"Item Added to the List!",Toast. LENGTH_SHORT);
                        toast.setMargin(50,50);
                        toast.show();*/
                    }
                });

        /*shopItemsList.add(shopItem);
        refreshRecyclerView();
        Toast toast= Toast.makeText(getApplicationContext(),"Item Added to the List!",Toast. LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();*/
    }

    public FirebaseUser getUser() {
        return user;
    }

}