package pappbence.bme.hu.lendr.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "lend", foreignKeys = {
        @ForeignKey(entity = Person.class, parentColumns = "Id", childColumns = "PersonId"),
        @ForeignKey(entity = LendrItem.class, parentColumns = "Id", childColumns = "ItemId")
    }
)
public class Lend {
    @PrimaryKey(autoGenerate = true)
        private int Id;
    private int ItemId;
    private int PersonId;
}
