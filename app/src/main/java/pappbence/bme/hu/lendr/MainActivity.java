package pappbence.bme.hu.lendr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import pappbence.bme.hu.lendr.adapter.CategoryAdapter;
import pappbence.bme.hu.lendr.adapter.LendrItemAdapter;
import pappbence.bme.hu.lendr.data.Category;
import pappbence.bme.hu.lendr.data.LendrItem;
import pappbence.bme.hu.lendr.fragments.CategoriesFragment;
import pappbence.bme.hu.lendr.fragments.ItemsFragment;
import pappbence.bme.hu.lendr.fragments.LendsFragment;
import pappbence.bme.hu.lendr.fragments.MenuPagerAdapter;
import pappbence.bme.hu.lendr.fragments.NewCategoryDialogFragment;
import pappbence.bme.hu.lendr.fragments.NewItemDialogFragment;

public class MainActivity extends AppCompatActivity implements NewItemDialogFragment.NewItemDialogListener, NewCategoryDialogFragment.NewCategoryDialogListener{

    public LendrItemAdapter itemAdapter;
    public CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tl = findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        MenuPagerAdapter menuAdapter = new MenuPagerAdapter(getSupportFragmentManager());
        menuAdapter.AddFragment(new ItemsFragment(), "Items");
        menuAdapter.AddFragment(new CategoriesFragment(), "Categories");
        menuAdapter.AddFragment(new LendsFragment(), "Lends");
        viewPager.setAdapter(menuAdapter);
        tl.setupWithViewPager(viewPager);

        InitAddButtons();
        InitTestData();
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
                new NewCategoryDialogFragment().show(getSupportFragmentManager(), NewCategoryDialogFragment.TAG);
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
            itemAdapter.SortByItemName();
            return true;
        }
        if(id == R.id.action_sort_category){
            itemAdapter.SortByCategoryName();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemCreated(LendrItem newItem) {
        newItem.save();
        itemAdapter.addItem(newItem);
        categoryAdapter.categoryChanged(newItem.Category);
    }

    @Override
    public void onCategoryCreated(Category newCategory) {
        newCategory.save();
        categoryAdapter.addCategory(newCategory);
    }

}
