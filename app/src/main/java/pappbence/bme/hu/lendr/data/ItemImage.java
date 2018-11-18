package pappbence.bme.hu.lendr.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "itemimage",
        foreignKeys = @ForeignKey(
                entity = LendrItem.class, parentColumns = "Id", childColumns = "ItemId"
        )
)
class ItemImage {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    private int ItemId;
    //TODO: image handling
    //private Bitmap Image;

}
