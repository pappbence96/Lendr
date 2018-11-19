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
    public int Id;
    public String Name;
    public String Description;
    public int CategoryId;

    public LendrItem(){}
    public LendrItem(int id, String name, String description, int categoryId) {
        Id = id;
        Name = name;
        Description = description;
        CategoryId = categoryId;
    }
}
