package com.nataliasep.listacomprafirebase;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nataliasep.listacomprafirebase.Adapters.ShoppingListAdapter;
import com.nataliasep.listacomprafirebase.Models.ShoppingList;

public class ListsActivity extends AppCompatActivity {

    private Button bAction;
    private FirebaseFirestore db;
    private CollectionReference myListsCollection;
    private FirestoreRecyclerOptions<ShoppingList> options;
    private RecyclerView rvList;
    private ShoppingListAdapter shoppingListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAction = findViewById(R.id.btAdd);
        rvList = findViewById(R.id.recView);
        db = FirebaseFirestore.getInstance();
        myListsCollection = db.collection("myLists");
        options = new FirestoreRecyclerOptions.Builder<ShoppingList>()
                .setQuery(myListsCollection, ShoppingList.class)
                .build();

        shoppingListAdapter = new ShoppingListAdapter(options);
        rvList.setAdapter(shoppingListAdapter);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setHasFixedSize(true);
        shoppingListAdapter.startListening();

        bAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.editText);
        Button btnCrear = dialogView.findViewById(R.id.btnCrear);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelar);

        final AlertDialog dialog = builder.create();

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = editText.getText().toString();
                String date = DateTimeHelper.getCurrentDateTime();
                ShoppingList newList = new ShoppingList(listName, date);
                db.collection("myLists").add(newList)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(MainActivity.class.getSimpleName(), "Lista añadida con ID: " + documentReference.getId());
                                dialog.dismiss();
                                shoppingListAdapter.refresh();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(MainActivity.class.getSimpleName(), "Error añadiendo la lista");
                                Log.e(MainActivity.class.getSimpleName(), e.getMessage());
                                Toast.makeText(ListsActivity.this, "Error añadiendo la lista", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
