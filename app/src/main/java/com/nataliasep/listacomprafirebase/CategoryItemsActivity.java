package com.nataliasep.listacomprafirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nataliasep.listacomprafirebase.Adapters.ItemAdapter;
import com.nataliasep.listacomprafirebase.Models.Category;
import com.nataliasep.listacomprafirebase.Models.Item;

import java.util.List;

public class CategoryItemsActivity extends AppCompatActivity {

    private String categoryId;
    private String selectedListId;
    private FirebaseFirestore db;
    private ItemAdapter itemAdapter;
    private RecyclerView rvList;
    private FirestoreRecyclerOptions<Item> options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null) {
            categoryId = intent.getStringExtra("categoryId");
            selectedListId = intent.getStringExtra("selectedListId");
        }

        db = FirebaseFirestore.getInstance();
        rvList = findViewById(R.id.recView);
        DocumentReference categoriaDocRef = db.collection("categorias").document(categoryId);
        CollectionReference productosRef = categoriaDocRef.collection("items");
        options = new FirestoreRecyclerOptions.Builder<Item>().setQuery(productosRef, Item.class).build();
        itemAdapter = new ItemAdapter(options, selectedListId, categoryId);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.setAdapter(itemAdapter);
        rvList.setHasFixedSize(true);
        itemAdapter.startListening();

    }
}
