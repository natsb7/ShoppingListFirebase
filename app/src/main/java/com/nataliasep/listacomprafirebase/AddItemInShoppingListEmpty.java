package com.nataliasep.listacomprafirebase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.nataliasep.listacomprafirebase.Adapters.CategoryAdapter;
import com.nataliasep.listacomprafirebase.Models.Category;

public class AddItemInShoppingListEmpty extends AppCompatActivity {
    private Button bAction;
    private TextView tvEmpty;
    private FirebaseFirestore db;
    private CategoryAdapter categoryAdapter;
    private RecyclerView rvList;
    private String listClickedId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout);
        listClickedId = getIntent().getStringExtra("listClickedId");
        tvEmpty = findViewById(R.id.tvEmpty);
        tvEmpty.setText("No hay elementos en la lista, dale al botón 'Añadir' de abajo para escoger una categoria :)");
        bAction = findViewById(R.id.btAdd);
        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCategorylist();
            }
        });

    }

    private void loadCategorylist() {
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        rvList = findViewById(R.id.recView);
        CollectionReference ref = db.collection("categorias");
        Query query = ref.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>().setQuery(query, Category.class).build();
        categoryAdapter = new CategoryAdapter(options, listClickedId);
        rvList.setAdapter(categoryAdapter);
        rvList.setLayoutManager(new GridLayoutManager(AddItemInShoppingListEmpty.this, 3));
        rvList.setHasFixedSize(true);
        categoryAdapter.startListening();

    }
}
