package pappbence.bme.hu.lendr;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import pappbence.bme.hu.lendr.adapter.LendrItemAdapter;
import pappbence.bme.hu.lendr.data.LendrDatabase;
import pappbence.bme.hu.lendr.data.LendrItem;

public class MainActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private LendrItemAdapter adapter;

    private LendrDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO implement shopping item creation
            }
        });

        database = Room.databaseBuilder(
                getApplicationContext(),
                LendrDatabase.class,
                "lendr-item-list"
        ).build();

        initRecyclerView();
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

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new LendrItemAdapter();
        adapter.addItem(new LendrItem(0, "Valami", "Nem tudom", 0));
        adapter.addItem(new LendrItem(1, "Aasd", "", 0));
        adapter.addItem(new LendrItem(2, "gsdgsf", "Ez egy komment", 1));
        adapter.addItem(new LendrItem(3, "Vaasd355lami", "Nem tudom", 1))   ;
        adapter.addItem(new LendrItem(4, "4213", "Nem tudom", 0));
        adapter.addItem(new LendrItem(5, "ASDasF", "Nem tudom", 2));


        //loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<LendrItem>>() {

            @Override
            protected List<LendrItem> doInBackground(Void... voids) {
                return database.lendrItemDao().getAll();
            }

            @Override
            protected void onPostExecute(List<LendrItem> items) {
                adapter.update(items);
            }
        }.execute();
    }

}
