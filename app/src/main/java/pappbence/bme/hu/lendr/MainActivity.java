package pappbence.bme.hu.lendr;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;


import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.orm.SugarApp;
import com.rengwuxian.materialedittext.MaterialEditText;

import pappbence.bme.hu.lendr.adapter.LendrItemAdapter;
import pappbence.bme.hu.lendr.data.Category;
import pappbence.bme.hu.lendr.data.LendrItem;
import pappbence.bme.hu.lendr.fragments.NewItemDialogFragment;

public class MainActivity extends AppCompatActivity implements NewItemDialogFragment.NewItemDialogListener{
    private RecyclerView recyclerView;
    private LendrItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SearchView sw = findViewById(R.id.item_search_view);
        sw.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        InitAddButtons();
        InitTestData();

        initRecyclerView();
    }

    private void InitTestData() {
        Category.deleteAll(Category.class);
        LendrItem.deleteAll(LendrItem.class);

        Category cat1 = new Category("Butor", null);
        cat1.save();
        Category cat2 = new Category("Jatek", null);
        cat2.save();
        Category cat3 = new Category("Evoeszk.", null);
        cat3.save();
        LendrItem i2 = new LendrItem("Asztal", "Ez meg egy masik butor, nagyon nagyon hosszu leirassal, ami valszeg nem fog kiferni a kepernyore", cat1);
        i2.save();
        LendrItem i1 = new LendrItem("Szek", "Ez egy butor", cat1);
        i1.save();
    }

    private void InitAddButtons() {
        FloatingActionButton fab = findViewById(R.id.fab);

        int floatingButtonSize = 180;
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this).setLayoutParams(new FrameLayout.LayoutParams(floatingButtonSize, floatingButtonSize));

        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageDrawable(getResources().getDrawable(R.drawable.round_add_black_48));
        SubActionButton itemAddBtn = itemBuilder.setContentView(itemIcon1).build();

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageDrawable(getResources().getDrawable(R.drawable.round_category_black_48));
        SubActionButton categoryAddBtn = itemBuilder.setContentView(itemIcon2).build();

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageDrawable(getResources().getDrawable(R.drawable.round_calendar_today_black_48));
        SubActionButton lendAddBtn = itemBuilder.setContentView(itemIcon3).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(categoryAddBtn)
                .addSubActionView(lendAddBtn)
                .addSubActionView(itemAddBtn)
                .attachTo(fab)
                .build();

        itemAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NewItemDialogFragment().show(getSupportFragmentManager(), NewItemDialogFragment.TAG);
            }
        });

        categoryAddBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content), "Adding categories is not yet supported", Snackbar.LENGTH_LONG).show();
            }
        });

        lendAddBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content), "Adding lend intervals is not yet supported", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_sort_name){
            adapter.SortByItemName();
            return true;
        }
        if(id == R.id.action_sort_category){
            adapter.SortByCategoryName();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new LendrItemAdapter();

        adapter.update(LendrItem.listAll(LendrItem.class));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemCreated(LendrItem newItem) {
        newItem.save();
        adapter.addItem(newItem);
    }
}
