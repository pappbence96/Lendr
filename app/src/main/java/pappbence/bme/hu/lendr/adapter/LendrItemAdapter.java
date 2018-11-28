package pappbence.bme.hu.lendr.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pappbence.bme.hu.lendr.MainActivity;
import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.data.LendrItem;

public class LendrItemAdapter extends RecyclerView.Adapter<LendrItemAdapter.LendrItemViewHolder> implements Filterable {

    private final List<LendrItem> items;
    private ItemFilter filter;
    private MainActivity mainActivity;

    private View.OnClickListener onItemClickListener;

    public void setItemClickListener(View.OnClickListener clickListener) {
        onItemClickListener = clickListener;
    }

    public LendrItemAdapter() {
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public LendrItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.lendr_item_list, parent, false);
        return new LendrItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LendrItemViewHolder holder, int position) {
        LendrItem item = items.get(position);
        holder.nameTextView.setText(item.Name);
        holder.descriptionTextView.setText(item.Description);
        holder.categoryNameTextView.setText(item.Category.Name);
        holder.item = item;
    }

    public void SortByItemName(){
        Sort(new Comparator<LendrItem>() {
            @Override
            public int compare(LendrItem o1, LendrItem o2) {
                return o1.Name.compareTo(o2.Name);
            }
        });
    }

    public void SortByCategoryName(){
        Sort(new Comparator<LendrItem>() {
            @Override
            public int compare(LendrItem o1, LendrItem o2) {
                return o1.Category.compareTo(o2.Category);
            }
        });
    }

    private void Sort(Comparator<LendrItem> comparator){
        Collections.sort(items, comparator);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(LendrItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void update(List<LendrItem> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new ItemFilter(this, items);
        }
        return filter;
    }

    public LendrItem getItem(int position) {
        return items.get(position);
    }

    public class ItemFilter extends Filter{

        private final LendrItemAdapter adapter;
        private final List<LendrItem> originalItems;
        private final List<LendrItem> filteredItems;

        public ItemFilter(LendrItemAdapter adapter, List<LendrItem> items){
            this.originalItems = items;
            this.adapter = adapter;
            filteredItems = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredItems.clear();
            final FilterResults results = new FilterResults();

            if(constraint.length() == 0){
                filteredItems.addAll(originalItems);
            } else{
                final String filterString = constraint.toString();
                for(LendrItem item : originalItems){
                    if(item.Name.contains(filterString)){
                        filteredItems.add(item);
                    }
                }
            }
            results.values = filteredItems;
            results.count = filteredItems.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items.clear();
            items.addAll(filteredItems);
            adapter.notifyDataSetChanged();
        }
    }

    public class LendrItemViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        TextView descriptionTextView;
        TextView categoryNameTextView;

        LendrItem item;

        public LendrItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(onItemClickListener);
            nameTextView = itemView.findViewById(R.id.LendrItemNameTextView);
            descriptionTextView = itemView.findViewById(R.id.LendrItemDescriptionTextView);
            categoryNameTextView = itemView.findViewById(R.id.LendrItemCategoryNameTextView);
        }
    }
}
