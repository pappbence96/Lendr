package pappbence.bme.hu.lendr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.paracamera.Camera;

import pappbence.bme.hu.lendr.adapter.LendrItemAdapter;
import pappbence.bme.hu.lendr.data.LendrItem;

public class ItemDetailsActivity extends AppCompatActivity {

    final static int REQUEST_IMAGE_CAPTURE = 1;
    LendrItem item;
    LinearLayout imageBar;
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(800)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);

        imageBar = findViewById(R.id.ImageBar);

        long itemId = getIntent().getLongExtra("ItemId", 0);
        item = LendrItem.findById(LendrItem.class, itemId);

        TextView name = findViewById(R.id.ItemDetailsName);
        TextView desc = findViewById(R.id.ItemDetailsDescription);
        TextView category = findViewById(R.id.ItemDetailsCategory);

        name.setText(item.Name);
        desc.setText(item.Description);
        category.setText(item.Category.Name);
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
            return true;
        }
        if(id == R.id.action_delete_item){
            promptDeleteItem();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void takeItemPhoto() {
        /*
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
        try {
            camera.takePicture();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.d("imagedbg", "W: " + String.valueOf(imageBitmap.getWidth()));
            Log.d("imagedbg", "H: " + String.valueOf(imageBitmap.getHeight()));*/

        Bitmap imageBitmap = null;

        if(requestCode == Camera.REQUEST_TAKE_PHOTO){
            imageBitmap = camera.getCameraBitmap();
        }

        if(imageBitmap == null){
            Toast.makeText(this.getApplicationContext(),"Picture not taken!",Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("imagedbg", String.valueOf(imageBitmap.toString()));
        ImageView imageView = new ImageView(
                this);
        imageView.setImageBitmap(imageBitmap);
        ViewGroup.MarginLayoutParams imageViewParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.MATCH_PARENT);
        imageView.setPadding(4, 4, 4,  4);
        imageView.setLayoutParams(imageViewParams);

        //TODO: Save image to DB

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
