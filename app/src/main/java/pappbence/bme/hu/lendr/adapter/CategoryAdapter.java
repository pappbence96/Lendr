package pappbence.bme.hu.lendr.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pappbence.bme.hu.lendr.MainActivity;
import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.data.Category;
import pappbence.bme.hu.lendr.data.LendrItem;
import pappbence.bme.hu.lendr.fragments.NewCategoryDialogFragment;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>  {

    private final List<Category> categories;
    private MainActivity activity;
    private FragmentManager fragmentManager;
    private Category editedCategory = null;

    public CategoryAdapter() {
        categories = new ArrayList<>();
    }

    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
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

    public void addOrSaveCategory(Category category) {
        if(editedCategory == null){
            category.save();
            categories.add(category);
            notifyItemInserted(categories.size() - 1);
            return;
        }
        editedCategory.Name = category.Name;
        editedCategory.ParentCategory = category.ParentCategory;
        editedCategory.save();
        notifyDataSetChanged();
    }

    public void update(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public void categoryChanged(Category category) {
        notifyDataSetChanged();
    }

    public void setActivity(MainActivity activity){
        this.activity = activity;
    }

    public void promptDeleteCategory(final Category category){
        new AlertDialog.Builder(activity)
                .setTitle("Delete category")
                .setMessage("Do you really want to delete this category?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteCategory(category);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void deleteCategory(Category category) {
        String errorMessage = "";
        if (category.getItems().size() != 0){
            errorMessage = "Can't delete this category because it has items assigned to it.";
        } else if(category.getChildren().size() != 0){
            errorMessage = "Can't delete this category because it has child categories.";
        }
        if(errorMessage.equals("")){
            categories.remove(category);
            category.delete();
            notifyDataSetChanged();
            return;
        }
        new AlertDialog.Builder(activity)
                .setTitle("Can't delete category")
                .setMessage(errorMessage)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setNegativeButton(android.R.string.no, null).show();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void SortByCategoryName() {
        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.compareTo(o2);
            }
        });
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        TextView parentTextView;
        TextView itemCountTextView;

        Category category;

        public CategoryViewHolder(@NonNull final View categoryView) {
            super(categoryView);
            categoryView.setTag(this);
            nameTextView = categoryView.findViewById(R.id.CategoryNameTextView);
            parentTextView = categoryView.findViewById(R.id.ParentCategoryTextView);
            itemCountTextView = categoryView.findViewById(R.id.ItemCountTextView);
            categoryView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(activity, categoryView);
                    popupMenu.getMenuInflater().inflate(R.menu.category_click_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.action_edit_category:
                                    editedCategory = category;
                                    Bundle args = new Bundle();
                                    args.putLong("categoryId", category.getId());
                                    NewCategoryDialogFragment f = new NewCategoryDialogFragment();
                                    f.setArguments(args);
                                    f.show(fragmentManager, NewCategoryDialogFragment.TAG);
                                    break;
                                case R.id.action_category_delete:
                                    promptDeleteCategory(category);
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });


        }
    }
}
