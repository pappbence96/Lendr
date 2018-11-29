package pappbence.bme.hu.lendr.fragments;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Spinner;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;
import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.data.Category;
import pappbence.bme.hu.lendr.data.LendrItem;

public class NewItemDialogFragment extends SupportBlurDialogFragment {
    public static final String TAG = "NewItemDialogFragment";
    private EditText nameEditText;
    private EditText descriptionEditText;
    private Spinner categorySpinner;

    public interface NewItemDialogListener {
        void onItemCreated(LendrItem newItem);
    }

    private NewItemDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewItemDialogListener) {
            listener = (NewItemDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!");
        }
    }

    private Boolean isValid(){
        if(TextUtils.isEmpty(nameEditText.getText())){
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Add new Item")
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
                        if(!isValid()){
                            nameEditText.setError("Name can't be empty");
                        } else {
                            listener.onItemCreated(getItem());
                            dismiss();
                        }
                    }
                });
            }
        });
        return dialog;
    }

    private LendrItem getItem(){
        LendrItem item = new LendrItem();
        item.Name = nameEditText.getText().toString();
        item.Description = descriptionEditText.getText().toString();
        item.Category = (Category)categorySpinner.getSelectedItem();
        return item;
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_item, null);
        nameEditText = contentView.findViewById(R.id.ItemNameEditText);
        descriptionEditText = contentView.findViewById(R.id.ItemDescriptionEditText);
        categorySpinner = contentView.findViewById(R.id.ItemCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, Category.listAll(Category.class)));
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
