package pappbence.bme.hu.lendr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.List;

import pappbence.bme.hu.lendr.data.ItemImage;
import pappbence.bme.hu.lendr.data.LendrItem;
import pappbence.bme.hu.lendr.fragments.ImagePreviewFragment;

public class ItemDetailsActivity extends AppCompatActivity {

    LendrItem item;
    LinearLayout imageBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        imageBar = findViewById(R.id.ImageBar);

        long itemId = getIntent().getLongExtra("ItemId", 0);
        item = LendrItem.findById(LendrItem.class, itemId);

        TextView name = findViewById(R.id.ItemDetailsName);
        TextView desc = findViewById(R.id.ItemDetailsDescription);
        TextView category = findViewById(R.id.ItemDetailsCategory);

        name.setText(item.Name);
        desc.setText(item.Description);
        category.setText(item.Category.Name);

        List<ItemImage> itemImages = item.getImages();
        for(ItemImage ii : itemImages){
            final Bitmap bmp = ii.getImage();
            Log.d("imagedbg", "W: " + bmp.getWidth() + " H: " + bmp.getHeight());
            imageBar.post(new Runnable() {
                @Override
                public void run() {
                    addImageToBar(bmp);
                }
            });
        }
        imageBar.invalidate();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.item_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_image) {
            takeItemPhoto();
            return true;
        }
        if(id == R.id.action_edit_item){
            Snackbar.make(findViewById(android.R.id.content), "Items can not be edited. Delete it and record it again.", Snackbar.LENGTH_LONG).show();
            return true;
        }
        if(id == R.id.action_delete_item){
            promptDeleteItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void takeItemPhoto() {
        if(item.getImages().size() >= 4){
            Snackbar.make(findViewById(android.R.id.content), "An item can only store 4 photos.", Snackbar.LENGTH_LONG).show();
            return;
        }
        PickImageDialog.build(new PickSetup().setMaxSize(400)
                .setButtonOrientation(LinearLayoutCompat.HORIZONTAL))
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        recordTakenImage(r.getBitmap());
                    }
                })
                .show(getSupportFragmentManager());
    }

    public void recordTakenImage(final Bitmap imageBitmap){
        ItemImage persistentImage = new ItemImage();
        persistentImage.Item = item;
        persistentImage.setImage(imageBitmap);
        persistentImage.save();

        Log.d("imagedbg", "W: " + imageBitmap.getWidth() + " H: " + imageBitmap.getHeight());
        addImageToBar(imageBitmap);

    }

    public void addImageToBar(final Bitmap imageBitmap){
        ImageView imageView = new ImageView(
                this);
        imageView.setImageBitmap(imageBitmap);
        ViewGroup.MarginLayoutParams imageViewParams = new ViewGroup.MarginLayoutParams(
                imageBitmap.getWidth() * imageBar.getHeight() / imageBitmap.getHeight(),
                imageBar.getHeight());
        imageView.setPadding(4, 4, 4, 4);
        imageView.setLayoutParams(imageViewParams);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ImagePreviewFragment f = new ImagePreviewFragment();
                Bundle args = new Bundle();
                args.putByteArray("bytearray", BitmapUtil.BitmapToByteArray(imageBitmap));
                f.setArguments(args);
                f.show(getSupportFragmentManager(), ImagePreviewFragment.TAG);
                return true;
            }
        });
        imageBar.addView(imageView);
    }

    public void promptDeleteItem(){
        new AlertDialog.Builder(this)
                .setTitle("Delete item")
                .setMessage("Do you really want to delete this item?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteItem();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void deleteItem(){
        String itemName = item.Name;
        item.delete();

        //TODO: delete images belonging to this Item

        Toast.makeText(getApplicationContext(), String.format("%1$s deleted", itemName), Toast.LENGTH_LONG).show();
        this.finish();
    }
}
