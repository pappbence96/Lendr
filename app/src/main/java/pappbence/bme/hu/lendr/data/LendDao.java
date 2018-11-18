package pappbence.bme.hu.lendr.data;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

public interface LendDao {
    @Query("SELECT * FROM lend")
    List<Lend> getAll();

    @Insert
    long Insert(Lend lend);

    @Update
    void Update(Lend lend);

    @Delete
    void Delete(Lend lend);
}
