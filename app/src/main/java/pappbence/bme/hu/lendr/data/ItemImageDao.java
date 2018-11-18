package pappbence.bme.hu.lendr.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

public interface ItemImageDao {
    @Query("SELECT * FROM category")
    List<ItemImage> getAll();

    @Insert
    long Insert(ItemImage image);

    @Update
    void Update(ItemImage imagecategory);

    @Delete
    void Delete(ItemImage image);
}
