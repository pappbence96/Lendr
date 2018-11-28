package pappbence.bme.hu.lendr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import pappbence.bme.hu.lendr.adapter.LendrItemAdapter;
import pappbence.bme.hu.lendr.data.LendrItem;

public class ItemDetailsActivity extends AppCompatActivity {

    LendrItem item;
    LendrItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

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
