package pappbence.bme.hu.lendr.data;

import com.orm.SugarRecord;

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
}