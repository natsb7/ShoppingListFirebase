package com.nataliasep.listacomprafirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nataliasep.listacomprafirebase.Adapters.CategoryAdapter;
import com.nataliasep.listacomprafirebase.Adapters.ItemAdapter;
import com.nataliasep.listacomprafirebase.Models.Category;
import com.nataliasep.listacomprafirebase.Models.Item;
import com.nataliasep.listacomprafirebase.Models.ItemList;

public class ItemsInShoppingList extends AppCompatActivity {
    private Button bAction;
    private FirebaseFirestore db;
    private CategoryAdapter categoryAdapter;
    private RecyclerView rvList;
    private String shoppingListId;
    private String categoryId;
    private String productId;
    private ItemAdapter itemAdapter;

    private FirestoreRecyclerOptions<Item> options;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        shoppingListId = intent.getStringExtra("shoppingListId");
        categoryId = intent.getStringExtra("categoryId");
        productId = intent.getStringExtra("productId");

        Log.d("ItemsInShoppingList", "Shopping List ID: " + shoppingListId);
        Log.d("ItemsInShoppingList", "Category ID: " + categoryId);
        Log.d("ItemsInShoppingList", "Product ID: " + productId);

        loadShoppingList(shoppingListId);

        bAction = findViewById(R.id.btAdd);
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategories();
            }
        });

    }

    private void loadShoppingList(String shoppingListId) {
        db = FirebaseFirestore.getInstance();
        rvList = findViewById(R.id.recView);
        CollectionReference productsRef = db.collection("myLists")
                .document(shoppingListId)
                .collection("productsInList");

        options = new FirestoreRecyclerOptions.Builder<Item>().setQuery(productsRef, Item.class).build();
        itemAdapter = new ItemAdapter(options, shoppingListId, categoryId);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.setAdapter(itemAdapter);
        rvList.setHasFixedSize(true);
        itemAdapter.startListening();


    }

    private void loadCategories() {
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        rvList = findViewById(R.id.recView);
        CollectionReference ref = db.collection("categorias");
        Query query = ref.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>().setQuery(query, Category.class).build();
        categoryAdapter = new CategoryAdapter(options, shoppingListId);
        rvList.setAdapter(categoryAdapter);
        rvList.setLayoutManager(new GridLayoutManager(this, 3));
        rvList.setHasFixedSize(true);
        categoryAdapter.startListening();
    }
}
