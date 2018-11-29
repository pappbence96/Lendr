package pappbence.bme.hu.lendr.data;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class Category extends SugarRecord<Category> implements Comparable<Category>{
    public String Name;
    public Category ParentCategory;

    public Category() {
    }

    public Category(String name, Category parentCategory) {
        Name = name;
        ParentCategory = parentCategory;
    }

    @Override
    public String toString() {
        return Name;
    }

    @Override
    public int compareTo(Category o) {
        return this.Name.compareTo(o.Name);
    }

    public List<LendrItem> getItems(){
        return Select.from(LendrItem.class)
                .where(Condition.prop("category").eq(this.getId()))
                .list();
    }

    public List<Category> getChildren(){
        return Select.from(Category.class)
                .where(Condition.prop("parent_category").eq(this.getId()))
                .list();
    }
}