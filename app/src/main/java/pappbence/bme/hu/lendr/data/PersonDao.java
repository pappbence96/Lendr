package pappbence.bme.hu.lendr.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PersonDao {
    @Query("SELECT * FROM person")
    List<Person> getAll();

    @Insert
    long Insert(Person person);

    @Update
    void Update(Person person);

    @Delete
    void Delete(Person person);
}
