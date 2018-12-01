package pappbence.bme.hu.lendr.data;

import com.orm.SugarRecord;

public class Lend extends SugarRecord<Lend> {
    public LendrItem Item;
    public String Lendee;
}
