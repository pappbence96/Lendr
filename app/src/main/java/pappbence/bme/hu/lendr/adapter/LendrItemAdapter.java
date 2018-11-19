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
import pappbence.bme.hu.lendr.data.LendrItem;

public class LendrItemAdapter extends RecyclerView.Adapter<LendrItemAdapter.LendrItemViewHolder> {

    private final List<LendrItem> items;

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
        holder.item = item;
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

    public class LendrItemViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        TextView descriptionTextView;

        LendrItem item;

        public LendrItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.LendrItemNameTextView);
            descriptionTextView = itemView.findViewById(R.id.LendrItemDescriptionTextView);
        }
    }
}
