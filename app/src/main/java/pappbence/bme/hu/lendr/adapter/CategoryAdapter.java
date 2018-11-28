package pappbence.bme.hu.lendr.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.data.Category;
import pappbence.bme.hu.lendr.data.LendrItem;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private final List<Category> categories;

    public CategoryAdapter() {
        categories = new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_list, parent, false);
        return new CategoryAdapter.CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.nameTextView.setText(category.Name);
        holder.parentTextView.setText(category.ParentCategory != null ? "\u2192" + category.ParentCategory.Name : ""  );
        List<LendrItem> items = category.getItems();
        holder.itemCountTextView.setText(String.valueOf(items.size()));
        holder.category = category;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void addCategory(Category category) {
        categories.add(category);
        notifyItemInserted(categories.size() - 1);
    }

    public void update(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public void categoryChanged(Category category) {
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        TextView parentTextView;
        TextView itemCountTextView;

        Category category;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.CategoryNameTextView);
            parentTextView = itemView.findViewById(R.id.ParentCategoryTextView);
            itemCountTextView = itemView.findViewById(R.id.ItemCountTextView);
        }
    }
}
