package edu.uga.cs.finalmobiledevproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddShopItemDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddShopItemDialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int SAVE = 1;

    private EditText itemNameView;
    private EditText itemPriceView;
    private EditText descriptionView;

    /*int position;
    int id;
    String itemName;
    float itemPrice;
    int purchasedBasketId;
    String inBasketOf;
    String description;*/

    public AddShopItemDialogFragment() {
        // Required empty public constructor
    }

    public interface AddShopItemDialogListener {
        void createShopItem(ShoppingItem shopItem);
    }
    /*public static AddShopItemDialogFragment newInstance(int pos, int id, String nam, float pri, int pur, String inb, String des) {
        AddShopItemDialogFragment dialog = new AddShopItemDialogFragment();

        Bundle args = new Bundle();
        args.putInt("position", pos);
        args.putInt("id", id);
        args.putString("itemName", nam);
        args.putFloat("itemPrice", pri);
        args.putInt("purchasedBasketId", pur);
        args.putString("inBasketOf", inb);
        args.putString("description", des);
        dialog.setArguments(args);
        return dialog;
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*position = getArguments().getInt("position");
        id = getArguments().getInt("id");
        itemName = getArguments().getString("itemName");
        itemPrice = getArguments().getFloat("itemPrice");
        purchasedBasketId = getArguments().getInt("purchasedBasketId");
        inBasketOf = getArguments().getString("inBasketOf");
        description = getArguments().getString("description");*/

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_shop_item_dialog, getActivity().findViewById(R.id.root));

        itemNameView = layout.findViewById(R.id.itemNameET);
        itemPriceView = layout.findViewById(R.id.itemPriceET);
        descriptionView = layout.findViewById(R.id.itemDescriptionET);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setView(layout);

        builder.setTitle("Add Item to Shopping List");

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton(android.R.string.ok, new AddShopItemListener());

        return builder.create();
    }

    private class AddShopItemListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            String iName = itemNameView.getText().toString();
            String iPrice = itemPriceView.getText().toString();
            String iDesc = descriptionView.getText().toString();

            ShoppingItem shopItem = new ShoppingItem(iName, iPrice, "-1", iDesc, "");

            AddShopItemDialogListener listener = (AddShopItemDialogListener) getActivity();

            listener.createShopItem(shopItem);

            dismiss();
        }
    }

    /*private class SaveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            String iName = itemNameView.getText().toString();
            String iPrice = itemPriceView.getText().toString();
            String iDesc = descriptionView.getText().toString();
            ShoppingItem shopItem = new ShoppingItem(id + "", iName, iPrice, purchasedBasketId + "", iDesc, inBasketOf);

            EditShopItemDialogListener listener = (AddShopItemDialogFragment.EditShopItemDialogListener) getActivity();
            listener.updateShopItem(position, shopItem, SAVE);
        }
    }*/

    /*private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    }*/
}