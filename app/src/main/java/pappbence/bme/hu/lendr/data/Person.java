package pappbence.bme.hu.lendr.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "person")
public class Person {
    @PrimaryKey(autoGenerate = true)
    public int Id;
    public String Name;
    public String Address;
    public String Phone;
}
