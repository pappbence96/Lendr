package pappbence.bme.hu.lendr.data;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class LendrItem extends SugarRecord<LendrItem> implements Comparable<LendrItem>{
    public String Name;
    public String Description;
    public Category Category;

    public LendrItem(String name, String description, pappbence.bme.hu.lendr.data.Category category) {
        Name = name;
        Description = description;
        Category = category;
    }

    public LendrItem() {
    }

    @Override
    public int compareTo(LendrItem o) {
        return this.Name.compareTo(o.Name);
    }

    public List<ItemImage> getImages(){
        return Select.from(ItemImage.class)
                .where(Condition.prop("item").eq(this.getId()))
                .list();
    }
}