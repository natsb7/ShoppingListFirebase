package com.nataliasep.listacomprafirebase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nataliasep.listacomprafirebase.Models.Category;
import com.nataliasep.listacomprafirebase.Models.Item;
import com.nataliasep.listacomprafirebase.Models.ShoppingList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser; //esto es para obtener el usuario que está logueado
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //esto es para obtener el usuario que está logueado
        if (firebaseUser != null) {
            Toast.makeText(this, "Bienvenido: " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
            //esto es para obtener la instancia de la base de datos

            Intent i = new Intent(this, ListsActivity.class);
            db.collection("myLists")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            // Si hay algún error, lo mostramos y salimos
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    // No hay listas, mostrar la actividad "No hay listas creadas"
                                    setContentView(R.layout.empty_layout);
                                    TextView tvEmpty = findViewById(R.id.tvEmpty);
                                    tvEmpty.setText("No hay listas creadas, dale al botón 'Añadir' de abajo para crear una nueva lista :)");
                                    Button bAction = findViewById(R.id.btAdd);
                                    bAction.setOnClickListener(v -> {
                                        showDialog(i);
                                    });
                                } else {
                                    // Hay listas, mostrar la actividad de shoppinglist
                                    startActivity(i);
                                }
                            }else {
                                Log.e(MainActivity.class.getSimpleName(), "Error getting documents (products): ", task.getException());
                            }
                        }
                    });

            /*CREACIÓN DE CATEGORIAS Y SUS PRODUCTOS

            List<Item> productsCategoryFrutas = new ArrayList<>();
            productsCategoryFrutas.add(new Item("Manzana", false, "manzana.jpg"));
            productsCategoryFrutas.add(new Item("Pera", false, "pera.jpg"));
            productsCategoryFrutas.add(new Item("Plátano", false, "platano.jpg"));
            productsCategoryFrutas.add(new Item("Naranjas", false, "naranjas.jpg"));
            Category category1 = new Category("Frutas", "frutas.jpg");

            ArrayList<Item> productsCategoryVerduras = new ArrayList<>();
            productsCategoryVerduras .add(new Item("Zanahoria", false, "zanahora.jpg"));
            productsCategoryVerduras .add(new Item("Lechuga", false, "lechuga.jpg"));
            productsCategoryVerduras .add(new Item("Col lombarga", false, "col_lombarda.jpg"));
            productsCategoryVerduras .add(new Item("Pepino", false, "pepino.jpg"));
            Category category2 = new Category("Verduras", "verduras.jpg");

            ArrayList<Item> productsCategoryProteinas = new ArrayList<>();
            productsCategoryProteinas.add(new Item("Pollo", false, "pollo.jpg"));
            productsCategoryProteinas.add(new Item("Salmón", false, "salmon.jpg"));
            productsCategoryProteinas.add(new Item("Leche", false, "leche.jpg"));
            productsCategoryProteinas.add(new Item("Huevos", false, "huevos.jpg"));
            Category category3 = new Category("Proteinas", "proteinas.jpg");

            ArrayList<Item> productsCategoryCereales = new ArrayList<>();
            productsCategoryCereales .add(new Item("Pan", false, "pan.jpg"));
            productsCategoryCereales .add(new Item("Arroz", false, "arroz.jpg"));
            productsCategoryCereales .add(new Item("Pasta", false, "pasta.jpg"));
            productsCategoryCereales .add(new Item("Avena", false, "avena.jpg"));
            Category category4 = new Category("Cereales", "cereales.jpg");



            db.collection("categorias").add(category4) //esto es para añadir el producto a la base de datos (en la colección "producto"
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            for (Item item : productsCategoryCereales) {
                                documentReference.collection("items").add(item)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(MainActivity.class.getSimpleName(), "Producto añadido con ID: " + documentReference.getId());
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(MainActivity.class.getSimpleName(), "Error añadiendo el producto");
                                                Log.e(MainActivity.class.getSimpleName(), e.getMessage());

                                            }
                                        });
                                Log.d(MainActivity.class.getSimpleName(), "Categoría añadida con ID: " + documentReference.getId());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(MainActivity.class.getSimpleName(), "Error añadiendo la categoría");
                            Log.e(MainActivity.class.getSimpleName(), e.getMessage());
                        }
                    });*/






        } else {
            Log.d(MainActivity.class.getSimpleName(), "No hay usuario logueado");
            Toast.makeText(this, "Usuario desconocido", Toast.LENGTH_SHORT).show();
            ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if (o.getResultCode() == RESULT_OK) {
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                Toast.makeText(MainActivity.this, "Bienvenido: " + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Acceso denegado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

            );

            resultLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build()); //esto sirve para lanzar la actividad de login
        }
    }

    private void showDialog(Intent i) {
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
                                startActivity(i);
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(MainActivity.class.getSimpleName(), "Error añadiendo la lista");
                        Log.e(MainActivity.class.getSimpleName(), e.getMessage());
                        Toast.makeText(MainActivity.this, "Error añadiendo la lista", Toast.LENGTH_SHORT).show();
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