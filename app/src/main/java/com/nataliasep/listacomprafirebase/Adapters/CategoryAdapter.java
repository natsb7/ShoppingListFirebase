package com.nataliasep.listacomprafirebase.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.nataliasep.listacomprafirebase.CategoryItemsActivity;
import com.nataliasep.listacomprafirebase.Models.Category;
import com.nataliasep.listacomprafirebase.R;

public class CategoryAdapter extends FirestoreRecyclerAdapter<Category, CategoryAdapter.CategoryViewHolder> {

    private String selectedListId;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CategoryAdapter(@NonNull FirestoreRecyclerOptions<Category> options, String selectedListId) {
        super(options);
        this.selectedListId = selectedListId;
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position, @NonNull Category model) {
        holder.onBindCategory(model);
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.categoria_item, parent, false));
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivProduct;
        private TextView tvCategoryName;
        private final Context  context;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvCategoryName = itemView.findViewById(R.id.tvProductName);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            String categoryClickedId = getSnapshots().getSnapshot(getBindingAdapterPosition()).getId();
            Intent intent = new Intent(v.getContext(), CategoryItemsActivity.class);
            intent.putExtra("categoryId", categoryClickedId);
            intent.putExtra("selectedListId", selectedListId);
            v.getContext().startActivity(intent);
        }

        public void onBindCategory(Category category) {
            tvCategoryName.setText(category.getName());
            String productImagePath = category.getImg();
            String imageNameWithoutExtension = productImagePath.replace(".jpg", "");
            int drawableID = context.getResources().getIdentifier(imageNameWithoutExtension, "drawable", context.getPackageName());
            Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), drawableID);
            ivProduct.setImageBitmap(imageBitmap);

    }
}




}
