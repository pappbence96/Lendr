package pappbence.bme.hu.lendr.data;

import com.orm.SugarRecord;

public class Category extends SugarRecord<Category>{
    public String Name;
    public Category ParentCategory;

    public Category() {
    }

    public Category(String name, Category parentCategory) {

        Name = name;
        ParentCategory = parentCategory;
    }
}