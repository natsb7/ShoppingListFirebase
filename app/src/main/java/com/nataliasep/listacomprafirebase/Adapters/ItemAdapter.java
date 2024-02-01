package com.nataliasep.listacomprafirebase.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nataliasep.listacomprafirebase.ItemsInShoppingList;
import com.nataliasep.listacomprafirebase.Models.Item;
import com.nataliasep.listacomprafirebase.R;

public class ItemAdapter extends FirestoreRecyclerAdapter<Item, ItemAdapter.ProductoViewHolder> {
    private String shoppingListSelectedId;
    private String categoryId;
    private FirebaseFirestore db;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ItemAdapter(@NonNull FirestoreRecyclerOptions<Item> options, String shoppingListSelectedId, String categoryId) {
        super(options);
        this.shoppingListSelectedId = shoppingListSelectedId;
        this.categoryId = categoryId;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductoViewHolder holder, int position, @NonNull Item model) {
        holder.bindProducto(model);
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_item, parent, false);
        return new ProductoViewHolder(v);
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvNombre;
        private ImageView ivProductImg;
        private final Context context;


        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvNombre = itemView.findViewById(R.id.tvNombre);
            ivProductImg = itemView.findViewById(R.id.ivProductImg);
            itemView.setOnClickListener(this);
        }

        public void bindProducto(Item item){
            tvNombre.setText(item.getName());
            String productImagePath = item.getImg();
            String imageNameWithoutExtension = productImagePath.replace(".jpg", "");
            int drawableID = context.getResources().getIdentifier(imageNameWithoutExtension, "drawable", context.getPackageName());
            Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), drawableID);
            ivProductImg.setImageBitmap(imageBitmap);
        }

        @Override
        public void onClick(View v) {
            String selectedProductId = getSnapshots().getSnapshot(getBindingAdapterPosition()).getId();
            addSelectedItemInSelectedList(selectedProductId, shoppingListSelectedId);
            Intent intent = new Intent(context, ItemsInShoppingList.class);
            intent.putExtra("shoppingListId", shoppingListSelectedId);
            intent.putExtra("categoryId", categoryId);
            intent.putExtra("productId", selectedProductId);
            context.startActivity(intent);
        }
    }

    public void addSelectedItemInSelectedList(String selectedItemId, String selectedListId){
        db = FirebaseFirestore.getInstance();
        DocumentReference listRef = db.collection("myLists").document(selectedListId);
        DocumentReference productsInCategoryRef = db.collection("categorias")
                .document(categoryId)
                .collection("items")
                .document(selectedItemId);

        Log.d("TAG SELECTED", "Product selected ID: " + selectedItemId);
        Log.d("TAG SELECTED", "List selected ID: " + selectedListId);

        productsInCategoryRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Item selectedItem = documentSnapshot.toObject(Item.class);
                    Log.d("PRODUCT SELECTED", "Product selected: " + selectedItem.getName());
                    if (selectedItem != null) {
                        DocumentReference productInListRef = listRef.collection("productsInList")
                                .document(selectedItemId);

                        productInListRef.set(selectedItem)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("ADDING PRODUCT", "Product added to list successfully, product ID: " + selectedItemId);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("MI LOG", "Error adding product to list: " + e.getMessage());
                                    }
                                });
                    }
                }
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Error al recuperar el producto");
            }
        });



    }
}
