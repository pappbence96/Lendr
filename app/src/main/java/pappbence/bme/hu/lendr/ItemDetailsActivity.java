package pappbence.bme.hu.lendr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import pappbence.bme.hu.lendr.data.LendrItem;

public class ItemDetailsActivity extends AppCompatActivity {

    LendrItem item;

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
}
