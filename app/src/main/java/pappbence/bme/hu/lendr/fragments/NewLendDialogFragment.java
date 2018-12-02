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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;
import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.data.Lend;
import pappbence.bme.hu.lendr.data.LendrItem;

public class NewLendDialogFragment extends SupportBlurDialogFragment {
    public static final String TAG = "NewLendDialogFragment";
    private Spinner itemSpinner;
    private EditText lendeeEditText;
    private EditText startDateEditText;
    private EditText endDateEditText;

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
    }

    private Boolean isValid(){
        if(TextUtils.isEmpty(lendeeEditText.getText())){
            lendeeEditText.setError("Lendee name cannot be empty");
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Add new Lend")
                .setView(getContentView())
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null)
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

        DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        try{
            lend.StartDate = format.parse(startDateEditText.getText().toString());
            lend.EndDate = format.parse(endDateEditText.getText().toString());
        } catch(Exception e){
            // TODO: 2018. 12. 01. : exception handling
        }

        return lend;
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_lend, null);
        itemSpinner = contentView.findViewById(R.id.LendrItemSpinner);
        itemSpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, LendrItem.listAll(LendrItem.class)));
        lendeeEditText = contentView.findViewById(R.id.LendeeNameEditText);

        startDateEditText = contentView.findViewById(R.id.StartDateEditText);
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        int s=monthOfYear+1;
                        startDateEditText.setText(dayOfMonth+"/"+s+"/"+year);
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

                        int s=monthOfYear+1;
                        endDateEditText.setText(dayOfMonth+"/"+s+"/"+year);
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
