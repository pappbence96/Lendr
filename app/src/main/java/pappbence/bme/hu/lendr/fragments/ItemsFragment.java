package pappbence.bme.hu.lendr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import pappbence.bme.hu.lendr.ItemDetailsActivity;
import pappbence.bme.hu.lendr.MainActivity;
import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.adapter.LendrItemAdapter;
import pappbence.bme.hu.lendr.data.LendrItem;

public class ItemsFragment extends Fragment {

    LendrItemAdapter adapter;
    RecyclerView recyclerView;
    MainActivity activity;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.items_screen, container, false);
        initRecyclerView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.items_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_sort_name:
                adapter.SortByItemName();
                break;
            case R.id.action_sort_category:
                adapter.SortByItemName();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.ItemRecyclerView);
        adapter = new LendrItemAdapter();

        adapter.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
                int position = viewHolder.getAdapterPosition();
                Intent itemDetailsIntent = new Intent(getActivity(), ItemDetailsActivity.class);
                long itemId = adapter.getItem(position).getId();
                itemDetailsIntent.putExtra("ItemId", itemId);
                startActivity(itemDetailsIntent);
            }
        });

        adapter.update(LendrItem.listAll(LendrItem.class));

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
        activity.itemAdapter = this.adapter;
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.update(LendrItem.listAll(LendrItem.class));
    }

}
