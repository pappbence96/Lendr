package pappbence.bme.hu.lendr.fragments;

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

import pappbence.bme.hu.lendr.MainActivity;
import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.adapter.CategoryAdapter;
import pappbence.bme.hu.lendr.data.Category;

public class CategoriesFragment extends Fragment {

    private CategoryAdapter adapter;
    private RecyclerView recyclerView;
    private MainActivity activity;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.categories_screen, container, false);
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
    public void onResume() {
        super.onResume();
        adapter.update(Category.listAll(Category.class));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.categories_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_sort_category_name:
                adapter.SortByCategoryName();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.CategoryRecyclerView);

        adapter = new CategoryAdapter();
        adapter.update(Category.listAll(Category.class));
        adapter.setActivity(activity);
        adapter.setFragmentManager(getActivity().getSupportFragmentManager());

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
        activity.categoryAdapter = this.adapter;
    }

}
