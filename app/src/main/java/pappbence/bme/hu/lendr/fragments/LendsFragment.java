package pappbence.bme.hu.lendr.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pappbence.bme.hu.lendr.MainActivity;
import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.adapter.LendAdapter;
import pappbence.bme.hu.lendr.data.Lend;

public class LendsFragment extends Fragment {

    LendAdapter adapter;
    View view;
    RecyclerView recyclerView;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lends_screen, container, false);
        initRecyclerView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity)getActivity();
    }

    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.LendRecyclerView);
        adapter = new LendAdapter();
        adapter.update(Lend.listAll(Lend.class));

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
        mainActivity.lendAdapter = this.adapter;
    }
}
