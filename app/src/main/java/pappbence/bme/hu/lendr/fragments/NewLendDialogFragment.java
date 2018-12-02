package pappbence.bme.hu.lendr.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;
import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.data.Lend;
import pappbence.bme.hu.lendr.data.LendrItem;

public class NewLendDialogFragment extends SupportBlurDialogFragment{
    public static final String TAG = "NewLendDialogFragment";
    private Spinner itemSpinner;
    private EditText lendeeEditText;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private LendrItem startItem;

    private DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    public interface NewLendDialogListener {
        void onLendCreated(Lend newLend);
    }

    private NewLendDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewLendDialogListener) {
            listener = (NewLendDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!");
        }

        Bundle args = getArguments();
        if(args == null){
            startItem = null;
        }
        else{
            long itemId = args.getLong("itemId", -1);
            if(itemId == -1 ){
                startItem = null;
            } else{
                startItem = LendrItem.findById(LendrItem.class, itemId);
            }
        }
    }

    private Boolean isValid(){
        if(TextUtils.isEmpty(lendeeEditText.getText())){
            lendeeEditText.setError(getString(R.string.new_lend_lendee_empty));
            return false;
        }
        if(TextUtils.isEmpty(startDateEditText.getText())){
            startDateEditText.setError(getString(R.string.new_lend_start_empty));
            return false;
        }
        if(TextUtils.isEmpty(endDateEditText.getText())){
            endDateEditText.setError(getString(R.string.new_lend_end_empty));
            return false;
        }
        Lend tmpLend = getLend();
        if(tmpLend.StartDate.after(tmpLend.EndDate)){
            endDateEditText.setError(getString(R.string.new_lend_date_preceed));
            return false;
        }
        List<Lend> potentialConflicts = tmpLend.Item.getLends(false);
        for(Lend l : potentialConflicts){
            if(l.conflictsWith(tmpLend)){
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.new_lend_conflict_title)
                        .setMessage(R.string.new_lend_conflict_msg)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, null)
                        .setNegativeButton(android.R.string.no, null).show();
                return false;
            }
        }
        return true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_lend_title)
                .setView(getContentView())
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button okButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isValid()){
                            listener.onLendCreated(getLend());
                            dismiss();
                        }
                    }
                });
            }
        });
        return dialog;
    }

    private Lend getLend(){
        Lend lend = new Lend();
        lend.Item = (LendrItem)itemSpinner.getSelectedItem();
        lend.Lendee = lendeeEditText.getText().toString();

        try{
            lend.StartDate = format.parse(startDateEditText.getText().toString());
            lend.EndDate = format.parse(endDateEditText.getText().toString());
        } catch(Exception e){
            Toast.makeText(getContext(), R.string.new_lend_parse_error, Toast.LENGTH_LONG).show();
            return null;
        }

        return lend;
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_lend, null);
        itemSpinner = contentView.findViewById(R.id.LendrItemSpinner);
        itemSpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, LendrItem.listAll(LendrItem.class)));
        if(startItem != null){
            int pos;
            for(pos = 0; pos < itemSpinner.getCount(); pos++){
                LendrItem tmpItem = (LendrItem)itemSpinner.getItemAtPosition(pos);
                if(tmpItem.Name.equals(startItem.Name)){
                    itemSpinner.setSelection(pos);
                    break;
                }
            }
        }
        lendeeEditText = contentView.findViewById(R.id.LendeeNameEditText);

        startDateEditText = contentView.findViewById(R.id.StartDateEditText);
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        startDateEditText.setText(String.format("%1$s/%2$s/%3$s", dayOfMonth, monthOfYear + 1, year));
                    }
                };

                Calendar c = Calendar.getInstance();
                DatePickerDialog d = new DatePickerDialog(getActivity(), dpd, c.get(Calendar.YEAR) ,c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        });
        endDateEditText = contentView.findViewById(R.id.EndDateEditText);
        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        endDateEditText.setText(String.format("%1$s/%2$s/%3$s", dayOfMonth, monthOfYear + 1, year));
                    }
                };

                Calendar c = Calendar.getInstance();
                DatePickerDialog d = new DatePickerDialog(getActivity(), dpd, c.get(Calendar.YEAR) ,c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        });
        return contentView;
    }

    @Override
    protected boolean isDebugEnable() {
        return false;
    }

    @Override
    protected float getDownScaleFactor() {
        return 5;
    }

    @Override
    protected int getBlurRadius() {
        return 7;
    }

    @Override
    protected boolean isDimmingEnable() {
        return true;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return true;
    }

    @Override
    protected boolean isRenderScriptEnable() {
        return true;
    }
}
