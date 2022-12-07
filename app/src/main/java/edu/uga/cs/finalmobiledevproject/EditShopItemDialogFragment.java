package edu.uga.cs.finalmobiledevproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditShopItemDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditShopItemDialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int SAVE = 1;

    private EditText itemNameView;
    private EditText itemPriceView;
    private EditText descriptionView;

    String key;
    int position;
    String itemName;
    float itemPrice;
    int purchasedBasketId;
    String inBasketOf;
    String description;

    public EditShopItemDialogFragment() {
        // Required empty public constructor
    }

    public interface EditShopItemDialogListener {
        void updateShopItem(int position, ShoppingItem shopItem, int action);
    }
    public static EditShopItemDialogFragment newInstance(String key, int pos, String nam, float pri, int pur, String inb, String des) {
        EditShopItemDialogFragment dialog = new EditShopItemDialogFragment();

        Bundle args = new Bundle();
        args.putString("key", key);
        args.putInt("position", pos);
        args.putString("itemName", nam);
        args.putFloat("itemPrice", pri);
        args.putInt("purchasedBasketId", pur);
        args.putString("inBasketOf", inb);
        args.putString("description", des);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        key = getArguments().getString("key");
        position = getArguments().getInt("position");
        itemName = getArguments().getString("itemName");
        itemPrice = getArguments().getFloat("itemPrice");
        purchasedBasketId = getArguments().getInt("purchasedBasketId");
        inBasketOf = getArguments().getString("inBasketOf");
        description = getArguments().getString("description");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.add_shop_item_dialog, getActivity().findViewById(R.id.root));

        itemNameView = layout.findViewById(R.id.itemNameET);
        itemPriceView = layout.findViewById(R.id.itemPriceET);
        descriptionView = layout.findViewById(R.id.itemDescriptionET);

        itemNameView.setText(itemName);
        itemPriceView.setText(itemPrice + "");
        descriptionView.setText(description);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setView(layout);

        builder.setTitle("Edit Shop Item");

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("SAVE", new SaveButtonClickListener());
        //builder.setNeutralButton("DELETE", new DeleteButtonClickListener());
        return builder.create();
    }

    private class SaveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            String iName = itemNameView.getText().toString();
            String iPrice = itemPriceView.getText().toString();
            String iDesc = descriptionView.getText().toString();
            ShoppingItem shopItem = new ShoppingItem(iName, iPrice, purchasedBasketId + "", iDesc, inBasketOf);
            shopItem.setKey(key);

            EditShopItemDialogListener listener = (EditShopItemDialogFragment.EditShopItemDialogListener) getActivity();
            listener.updateShopItem(position, shopItem, SAVE);
        }
    }

    /*private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    }*/
}