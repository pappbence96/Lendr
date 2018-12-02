package pappbence.bme.hu.lendr.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import pappbence.bme.hu.lendr.MainActivity;
import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.data.Lend;

public class LendAdapter extends RecyclerView.Adapter<LendAdapter.LendViewHolder>  {

    private final List<Lend> lends;
    private MainActivity activity;
    private FragmentManager fragmentManager;
    DateFormat dateFormat;

    public LendAdapter(){
        lends = new ArrayList<>();
        dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
    }

    @NonNull
    @Override
    public LendAdapter.LendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.lend_list, parent, false);
        return new LendAdapter.LendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LendAdapter.LendViewHolder lendViewHolder, int i) {
        Lend lend = lends.get(i);
        lendViewHolder.itemTextView.setText(lend.Item.Name);
        lendViewHolder.lendeeTextView.setText(lend.Lendee);
        lendViewHolder.startDateTextView.setText(dateFormat.format(lend.StartDate));
        lendViewHolder.endDateTextView.setText(dateFormat.format(lend.EndDate));
        lendViewHolder.closedTextBox.setChecked(lend.Closed);
        lendViewHolder.lend = lend;
    }

    public void update(List<Lend> lends) {
        this.lends.clear();
        this.lends.addAll(lends);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lends.size();
    }


    public void setActivity(MainActivity activity){
        this.activity = activity;
    }

    public void addLend(Lend newLend) {
        lends.add(newLend);
        notifyDataSetChanged();
    }


    public class LendViewHolder extends RecyclerView.ViewHolder{
        TextView itemTextView;
        TextView lendeeTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        CheckBox closedTextBox;

        Lend lend;

        public LendViewHolder(@NonNull final View categoryView) {
            super(categoryView);
            categoryView.setTag(this);
            itemTextView = categoryView.findViewById(R.id.LendItem);
            lendeeTextView = categoryView.findViewById(R.id.LendLendee);
            startDateTextView = categoryView.findViewById(R.id.LendStartDate);
            endDateTextView = categoryView.findViewById(R.id.LendEndDate);
            closedTextBox = categoryView.findViewById(R.id.LendClosedCheckBox);
            closedTextBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    lend.Closed = isChecked;
                }
            });
        }
    }
}
