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
import pappbence.bme.hu.lendr.data.Lend;
import pappbence.bme.hu.lendr.data.LendrItem;
import pappbence.bme.hu.lendr.fragments.ImagePreviewFragment;
import pappbence.bme.hu.lendr.fragments.NewItemDialogFragment;
import pappbence.bme.hu.lendr.fragments.NewLendDialogFragment;

public class ItemDetailsActivity extends AppCompatActivity implements NewLendDialogFragment.NewLendDialogListener {

    LendrItem lendrItem;
    LinearLayout imageBar;
    TextView openLends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        imageBar = findViewById(R.id.ImageBar);

        long itemId = getIntent().getLongExtra("ItemId", 0);
        lendrItem = LendrItem.findById(LendrItem.class, itemId);

        TextView name = findViewById(R.id.ItemDetailsName);
        TextView desc = findViewById(R.id.ItemDetailsDescription);
        TextView category = findViewById(R.id.ItemDetailsCategory);
        openLends = findViewById(R.id.ItemDetailsOpenLends);
        TextView closed = findViewById(R.id.ItemDetailsClosedLends);

        name.setText(lendrItem.Name);
        desc.setText(lendrItem.Description);
        category.setText(lendrItem.Category.Name);
        openLends.setText(String.valueOf(lendrItem.getLends(false).size()));
        closed.setText(String.valueOf(lendrItem.getLends(true).size()));

        List<ItemImage> itemImages = lendrItem.getImages();
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
        // Handle action bar lendrItem clicks here. The action bar will
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
        if(id == R.id.action_add_lend){
            Bundle args = new Bundle();
            args.putLong("itemId", lendrItem.getId());
            NewLendDialogFragment f = new NewLendDialogFragment();
            f.setArguments(args);
            f.show(getSupportFragmentManager(), NewItemDialogFragment.TAG);
            return true;
        }
        if(id == R.id.action_delete_item){
            promptDeleteItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void takeItemPhoto() {
        if(lendrItem.getImages().size() >= 4){
            Snackbar.make(findViewById(android.R.id.content), "An lendrItem can only store 4 photos.", Snackbar.LENGTH_LONG).show();
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
        persistentImage.Item = lendrItem;
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
                .setTitle("Delete lendrItem")
                .setMessage("Do you really want to delete this lendrItem?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        promtDeleteItem();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void promtDeleteItem(){
        List<Lend> lends = lendrItem.getLends();
        if(lends.size() > 0){
            new AlertDialog.Builder(this)
                    .setTitle("Delete lendrItem")
                    .setMessage("Lends on this lendrItem will also be deleted. Do you want to continue?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            deleteItem();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        } else {
            deleteItem();
        }
    }

    private void deleteItem(){
        for(ItemImage ii : lendrItem.getImages()){
            ii.delete();
        }
        for(Lend l : lendrItem.getLends()){
            l.delete();
        }
        String itemName = lendrItem.Name;
        lendrItem.delete();
        Toast.makeText(getApplicationContext(), String.format("%1$s deleted", itemName), Toast.LENGTH_LONG).show();
        this.finish();
    }

    @Override
    public void onLendCreated(Lend newLend) {
        newLend.save();
        int open = Integer.valueOf(openLends.getText().toString());
        openLends.setText(String.valueOf(open + 1));
    }
}
