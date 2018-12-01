package pappbence.bme.hu.lendr.data;

import com.orm.SugarRecord;

import java.util.Date;

public class Lend extends SugarRecord<Lend> {
    public LendrItem Item;
    public String Lendee;
    public Date StartDate;
    public Date EndDate;
    public Boolean Closed = false;
}
