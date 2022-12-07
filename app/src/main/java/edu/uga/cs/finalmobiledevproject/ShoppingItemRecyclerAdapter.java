package edu.uga.cs.finalmobiledevproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingItemRecyclerAdapter extends RecyclerView.Adapter<ShoppingItemRecyclerAdapter.ShopItemHolder> {

    public static final String DEBUG_TAG = "ShopItemRecyclerAdapter";

    private List<ShoppingItem> shopItemList;
    private Context context;
    private int purchasedBasketId;

    public ShoppingItemRecyclerAdapter(List<ShoppingItem> sil, Context c) {
        shopItemList = sil;
        context = c;
        purchasedBasketId = -1;
    }

    class ShopItemHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        TextView itemDescription;
        Button addButton;
        Button editButton;
        Button deleteButton;
        Button returnButton;
        Button ccButton;
        Button ciButton;

        LinearLayout shopListLL;
        LinearLayout purchdLL;

        public ShopItemHolder (View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.basketHeader);
            itemPrice = itemView.findViewById(R.id.itemsText);
            itemDescription = itemView.findViewById(R.id.itemDescription);

            shopListLL = itemView.findViewById(R.id.shopListLL);
            addButton = itemView.findViewById(R.id.addButton);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            returnButton = itemView.findViewById(R.id.returnButton);

            ccButton = itemView.findViewById(R.id.changeCostButton);
            ciButton = itemView.findViewById(R.id.canItemButton);
            purchdLL = itemView.findViewById(R.id.purchasedLL);
            if (context instanceof RecentPurchaseActivity) {
                returnButton.setVisibility(Button.GONE);
                shopListLL.setVisibility(Button.GONE);
            } else {
                purchdLL.setVisibility(LinearLayout.GONE);
                if (context instanceof ViewShopListActivity) {
                    returnButton.setVisibility(Button.GONE);
                } else {
                    shopListLL.setVisibility(LinearLayout.GONE);
                }
            }
        }
    }

    @NonNull
    @Override
    public ShopItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        return new ShopItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopItemHolder holder, int position) {
        ShoppingItem shopItem = shopItemList.get(position);
        Log.d(DEBUG_TAG, "onBindViewHolder: " + shopItem);

        /*
        String id = shopItem.getId() + "";
        String itemName = shopItem.getItemName();
        String itemPrice = shopItem.getItemPrice() + "";
        String itemDescription = shopItem.getDescription();
        */

        if (context instanceof ViewShopListActivity) {
            ViewShopListActivity act = (ViewShopListActivity) context;
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    act.deleteShopItem(shopItem.getKey(), holder.getAdapterPosition());
                    /*Toast toast = Toast.makeText(act.getApplicationContext(), "Item Deleted", Toast.LENGTH_SHORT);
                    toast.setMargin(50, 50);
                    toast.show();*/
                }
            });
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    act.editShopItem(shopItem, holder.getAdapterPosition());
                    /*EditShopItemDialogFragment editItemFragment =
                            EditShopItemDialogFragment.newInstance(holder.getAdapterPosition(), shopItem.getId(), shopItem.getItemName(), shopItem.getItemPrice(),
                                    shopItem.getPurchasedBasketId(), shopItem.getBasketingUser(), shopItem.getDescription());
                    editItemFragment.show(((AppCompatActivity)context).getSupportFragmentManager(), null);*/

                }
            });
            holder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    act.addShopItem(shopItem, holder.getAdapterPosition());
                    Toast toast = Toast.makeText(act.getApplicationContext(), "Item Added to Your Basket!", Toast.LENGTH_SHORT);
                    toast.setMargin(50, 50);
                    toast.show();
                }
            });
        } else if (context instanceof RecentPurchaseActivity) {
            RecentPurchaseActivity act = (RecentPurchaseActivity) context;
            holder.ccButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.ciButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    act.cancelSelectedBItem(shopItem, holder.getAdapterPosition());
                }
            });
        } else {
            BasketActivity act = (BasketActivity) context;
            holder.returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    act.returnItemToList(shopItem, holder.getAdapterPosition());
                }
            });
        }

        holder.itemName.setText(shopItem.getItemName());
        holder.itemPrice.setText("$" + shopItem.getItemPrice() + "");
        holder.itemDescription.setText(shopItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return shopItemList.size();
    }

    public void setPurchasedBasketId(int i) {
        purchasedBasketId = i;
    }

}