package pappbence.bme.hu.lendr.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "lendritem",
        foreignKeys = @ForeignKey(
                entity = Category.class, parentColumns = "Id", childColumns = "CategoryId"
        )
)
public class LendrItem {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    private String Name;
    private String Description;
    private int CategoryId;
}
