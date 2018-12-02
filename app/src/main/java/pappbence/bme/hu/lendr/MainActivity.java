package pappbence.bme.hu.lendr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import pappbence.bme.hu.lendr.adapter.CategoryAdapter;
import pappbence.bme.hu.lendr.adapter.LendAdapter;
import pappbence.bme.hu.lendr.adapter.LendrItemAdapter;
import pappbence.bme.hu.lendr.data.Category;
import pappbence.bme.hu.lendr.data.Lend;
import pappbence.bme.hu.lendr.data.LendrItem;
import pappbence.bme.hu.lendr.fragments.CategoriesFragment;
import pappbence.bme.hu.lendr.fragments.ItemsFragment;
import pappbence.bme.hu.lendr.fragments.LendsFragment;
import pappbence.bme.hu.lendr.fragments.MenuPagerAdapter;
import pappbence.bme.hu.lendr.fragments.NewCategoryDialogFragment;
import pappbence.bme.hu.lendr.fragments.NewItemDialogFragment;
import pappbence.bme.hu.lendr.fragments.NewLendDialogFragment;

public class MainActivity extends AppCompatActivity implements NewItemDialogFragment.NewItemDialogListener, NewCategoryDialogFragment.NewCategoryDialogListener, NewLendDialogFragment.NewLendDialogListener {

    public LendrItemAdapter itemAdapter;
    public CategoryAdapter categoryAdapter;
    public LendAdapter lendAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        TabLayout tl = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);
        MenuPagerAdapter menuAdapter = new MenuPagerAdapter(getSupportFragmentManager());
        menuAdapter.AddFragment(new ItemsFragment(), getString(R.string.pager_items));
        menuAdapter.AddFragment(new CategoriesFragment(), getString(R.string.pager_categories));
        menuAdapter.AddFragment(new LendsFragment(), getString(R.string.pager_lends));
        viewPager.setAdapter(menuAdapter);
        tl.setupWithViewPager(viewPager);

        InitAddButtons();
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
                if(Category.listAll(Category.class).size() == 0){
                    Snackbar.make(findViewById(android.R.id.content), R.string.no_category_error, Snackbar.LENGTH_LONG).show();
                    return;
                }
                new NewItemDialogFragment().show(getSupportFragmentManager(), NewItemDialogFragment.TAG);
            }
        });

        categoryAddBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                new NewCategoryDialogFragment().show(getSupportFragmentManager(), NewCategoryDialogFragment.TAG);
            }
        });

        lendAddBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
                new NewLendDialogFragment().show(getSupportFragmentManager(), NewLendDialogFragment.TAG);
            }
        });
    }

    @Override
    public void onItemCreated(LendrItem newItem) {
        newItem.save();
        itemAdapter.addItem(newItem);
        categoryAdapter.categoryChanged();
    }

    @Override
    public void onCategoryCreated(Category newCategory) {
        categoryAdapter.addOrSaveCategory(newCategory);
    }

    @Override
    public void onLendCreated(Lend newLend) {
        if(newLend == null){
            return;
        }

        lendAdapter.addLend(newLend);
    }
}
