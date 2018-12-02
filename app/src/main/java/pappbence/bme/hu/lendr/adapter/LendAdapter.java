package pappbence.bme.hu.lendr.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pappbence.bme.hu.lendr.MainActivity;
import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.data.Lend;

public class LendAdapter extends RecyclerView.Adapter<LendAdapter.LendViewHolder>  {

    private final List<Lend> lends;
    private MainActivity activity;
    private DateFormat dateFormat;

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
    public void onBindViewHolder(@NonNull final LendAdapter.LendViewHolder lendViewHolder, int i) {
        Lend lend = lends.get(i);
        lendViewHolder.itemTextView.setText(lend.Item.Name);
        lendViewHolder.lendeeTextView.setText(lend.Lendee);
        lendViewHolder.startDateTextView.setText(dateFormat.format(lend.StartDate));
        lendViewHolder.endDateTextView.setText(dateFormat.format(lend.EndDate));
        lendViewHolder.closedCheckBox.setChecked(lend.Closed);
        lendViewHolder.lend = lend;
        lendViewHolder.itemView.setBackgroundResource(lend.Closed ? R.drawable.list_bg_closed : R.drawable.list_bg);
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
        newLend.save();
        notifyDataSetChanged();
    }

    public void sortByEndDate() {
        sort(new Comparator<Lend>() {
            @Override
            public int compare(Lend o1, Lend o2) {
                if(o1.EndDate.after(o2.EndDate)){
                    return 1;
                } else if(o1.EndDate.before(o2.EndDate)){
                    return -1;
                }
                return 0;
            }
        });
    }

    public void sortByStartDate() {
        sort(new Comparator<Lend>() {
            @Override
            public int compare(Lend o1, Lend o2) {
                if(o1.StartDate.after(o2.StartDate)){
                    return 1;
                } else if(o1.StartDate.before(o2.StartDate)){
                    return -1;
                }
                return 0;
            }
        });
    }

    public void sortByLendee() {
        sort(new Comparator<Lend>() {
            @Override
            public int compare(Lend o1, Lend o2) {
                return o1.Lendee.compareTo(o2.Lendee);
            }
        });
    }

    public void sortByItemName() {
        sort(new Comparator<Lend>() {
            @Override
            public int compare(Lend o1, Lend o2) {
                return o1.Item.compareTo(o2.Item);
            }
        });
    }

    private void sort(Comparator<Lend> comp){
        Collections.sort(lends, comp);
        notifyDataSetChanged();
    }

    public void deleteClosedLends() {
        for(Lend l : Lend.findByClosed(true)){
            for(int i = 0; i < lends.size(); i++){
                if(l.getId() == lends.get(i).getId()){
                    lends.remove(i);
                    l.delete();
                }
            }
        }
        notifyDataSetChanged();
    }

    private void promptDeleteLend(final Lend lend){
        new AlertDialog.Builder(activity)
                .setTitle(R.string.delete_lend_title)
                .setMessage(R.string.delete_lend_msg)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteLend(lend);
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void deleteLend(Lend lend) {
        lends.remove(lend);
        lend.delete();
        notifyDataSetChanged();
    }

    public class LendViewHolder extends RecyclerView.ViewHolder{

        TextView itemTextView;
        TextView lendeeTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        CheckBox closedCheckBox;

        Lend lend;

        LendViewHolder(@NonNull final View lendView) {
            super(lendView);
            lendView.setTag(this);
            itemTextView = lendView.findViewById(R.id.LendItem);
            lendeeTextView = lendView.findViewById(R.id.LendLendee);
            startDateTextView = lendView.findViewById(R.id.LendStartDate);
            endDateTextView = lendView.findViewById(R.id.LendEndDate);
            closedCheckBox = lendView.findViewById(R.id.LendClosedCheckBox);
            closedCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lend.Closed = closedCheckBox.isChecked();
                    lend.save();
                    itemView.setBackgroundResource(lend.Closed ? R.drawable.list_bg_closed : R.drawable.list_bg);
                    itemView.invalidate();
                }
            });
            lendView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(activity, lendView);
                    popupMenu.getMenuInflater().inflate(R.menu.lend_click_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.action_lend_delete:
                                    promptDeleteLend(lend);
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
