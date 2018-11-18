package pappbence.bme.hu.lendr.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(
        entities = {Category.class, Lend.class, LendrItem.class, Person.class, ItemImage.class},
        version=1
)

public abstract class LendrDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract ItemImageDao itemImageDao();
    public abstract LendDao lendDao();
    public abstract LendrItemDao lendrItemDao();
    public abstract PersonDao personDao();
}
