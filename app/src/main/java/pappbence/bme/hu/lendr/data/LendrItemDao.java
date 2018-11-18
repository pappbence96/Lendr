package pappbence.bme.hu.lendr.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface LendrItemDao {
    @Query("SELECT * FROM lendritem")
    List<LendrItem> getAll();

    @Insert
    long Insert(LendrItem item);

    @Update
    void Update(LendrItem item);

    @Delete
    void Delete(LendrItem item);
}
