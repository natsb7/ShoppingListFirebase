package com.nataliasep.listacomprafirebase.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nataliasep.listacomprafirebase.AddItemInShoppingListEmpty;
import com.nataliasep.listacomprafirebase.Models.ItemList;
import com.nataliasep.listacomprafirebase.Models.ShoppingList;
import com.nataliasep.listacomprafirebase.ItemsInShoppingList;
import com.nataliasep.listacomprafirebase.R;

import java.util.ArrayList;

public class ShoppingListAdapter extends FirestoreRecyclerAdapter<ShoppingList, ShoppingListAdapter.ShoppingListViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private FirebaseFirestore db;
    public ShoppingListAdapter(@NonNull FirestoreRecyclerOptions<ShoppingList> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShoppingListAdapter.ShoppingListViewHolder holder, int position, @NonNull ShoppingList model) {
        holder.bindShoppingList(model);
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ShoppingListAdapter.ShoppingListViewHolder(v);
    }

    public void refresh() {
        notifyDataSetChanged();

    }

    class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvListName, tvDate;
        private final Context  context;

        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvListName = itemView.findViewById(R.id.tvListName);
            this.tvDate = itemView.findViewById(R.id.tvDate);
            this.context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bindShoppingList(ShoppingList shoppingList) {
            tvListName.setText(shoppingList.getName());
            tvDate.setText(shoppingList.getDate());
        }

        @Override
        public void onClick(View v) {
            String listClickedId = getSnapshots().getSnapshot(getBindingAdapterPosition()).getId();
            checkProductsInList(listClickedId, context);

        }
    }

    private void checkProductsInList(String listClickedId, Context context) {
        db = FirebaseFirestore.getInstance();
        DocumentReference myListsRef = db.collection("myLists").document(listClickedId);
        myListsRef.collection("productsInList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot productsSnapshot) {
                if (!productsSnapshot.isEmpty()) {
                    Log.d("LIST ADAPTER", "HAY PRODUCTOS");
                    Intent intent = new Intent(context, ItemsInShoppingList.class);
                    intent.putExtra("shoppingListId", listClickedId);
                    context.startActivity(intent);
                } else {
                    Log.d("LIST ADAPTER", "NO HAY PRODUCTOS");
                    Intent intent = new Intent(context, AddItemInShoppingListEmpty.class);
                    intent.putExtra("listClickedId", listClickedId);
                    context.startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Maneja el error al obtener la colección productsInList.
                Toast.makeText(context, "Error al obtener la colección productsInList: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
