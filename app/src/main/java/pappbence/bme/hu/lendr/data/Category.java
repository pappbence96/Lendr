package pappbence.bme.hu.lendr.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "category",
        foreignKeys = @ForeignKey(
                entity = Category.class, parentColumns = "Id", childColumns = "ParentCategory"
        )
)
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    private int Name;
    private Category ParentCategory;
}
