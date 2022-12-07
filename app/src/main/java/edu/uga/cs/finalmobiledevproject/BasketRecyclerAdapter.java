package edu.uga.cs.finalmobiledevproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class BasketRecyclerAdapter extends RecyclerView.Adapter<BasketRecyclerAdapter.BasketHolder>{

    private List<Basket> basketList;
    private Context context;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public BasketRecyclerAdapter (List<Basket> l, Context c) {
        basketList = l;
        context = c;
    }

    class BasketHolder extends RecyclerView.ViewHolder {
        TextView basketHeader;
        TextView itemsText;
        TextView costTextView;
        Button editItemButton;

        public BasketHolder(@NonNull View itemView) {
            super(itemView);

            basketHeader = itemView.findViewById(R.id.basketHeader);
            itemsText = itemView.findViewById(R.id.itemsText);
            costTextView = itemView.findViewById(R.id.costTV);
            editItemButton = itemView.findViewById(R.id.editPurchasedItemB);
        }
    }

    @NonNull
    @Override
    public BasketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new BasketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketHolder holder, int position) {
        Basket basket = basketList.get(position);
        RecentPurchaseActivity act = (RecentPurchaseActivity) context;

        holder.editItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.viewBasketItems(basket);
            }
        });

        holder.basketHeader.setText("Basket " + basket.getId());
        String items = "Items:\n";
        for (ShoppingItem s : basket.getItems()) {
            items += (s.getItemName() + "\n");
        }
        holder.itemsText.setText(items);
        holder.costTextView.setText("Total Cost: $" + df.format(basket.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return basketList.size();
    }
}
