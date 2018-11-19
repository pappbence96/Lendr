package pappbence.bme.hu.lendr.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ItemImageDao {
    @Query("SELECT * FROM itemimage")
    List<ItemImage> getAll();

    @Insert
    long Insert(ItemImage image);

    @Update
    void Update(ItemImage image);

    @Delete
    void Delete(ItemImage image);
}
