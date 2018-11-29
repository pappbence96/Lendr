package pappbence.bme.hu.lendr.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;
import pappbence.bme.hu.lendr.R;
import pappbence.bme.hu.lendr.data.Category;

public class NewCategoryDialogFragment extends SupportBlurDialogFragment {
    public static final String TAG = "NewItemDialogFragment";
    private EditText nameEditText;
    private CheckBox setParentCheckBox;
    private Spinner categorySpinner;
    private Category startCategory;

    public interface NewCategoryDialogListener {
        void onCategoryCreated(Category newCategory);
    }

    private NewCategoryDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewCategoryDialogListener) {
            listener = (NewCategoryDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!");
        }

        Bundle args = getArguments();
        if(args == null){
            startCategory = null;
        }
        else{
            long categoryId = args.getLong("categoryId", -1);
            if(categoryId == -1 ){
                startCategory = null;
            } else{
                startCategory = Category.findById(Category.class, categoryId);
            }
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
                .setTitle("Add new Category")
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
                            listener.onCategoryCreated(getCategory());
                            dismiss();
                        }
                    }
                });
            }
        });
        return dialog;
    }

    private Category getCategory(){
        Category category = new Category();
        category.Name = nameEditText.getText().toString();
        category.ParentCategory = setParentCheckBox.isChecked() ? (Category)categorySpinner.getSelectedItem() : null;
        return category;
    }

    private View getContentView() {
        final View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_category, null);
        nameEditText = contentView.findViewById(R.id.CategoryNameEditText);
        setParentCheckBox = contentView.findViewById(R.id.setParentCb);
        setParentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && Category.listAll(Category.class).size() == 0){
                    Snackbar.make(contentView, "Can't add Parent because there are no categories.", Snackbar.LENGTH_LONG).show();
                    setParentCheckBox.setChecked(false);
                    return;
                }
                categorySpinner.setEnabled(isChecked);
            }
        });
        categorySpinner = contentView.findViewById(R.id.ParentCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, Category.listAll(Category.class)));
        if(startCategory != null){
            nameEditText.setText(startCategory.Name);
        }
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
