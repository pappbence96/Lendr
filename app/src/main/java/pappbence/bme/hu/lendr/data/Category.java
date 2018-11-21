package pappbence.bme.hu.lendr.data;

import com.orm.SugarRecord;

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
}