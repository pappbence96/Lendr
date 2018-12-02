package pappbence.bme.hu.lendr.data;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Date;
import java.util.List;

public class Lend extends SugarRecord<Lend> {
    public LendrItem Item;
    public String Lendee;
    public Date StartDate;
    public Date EndDate;
    public Boolean Closed ;

    public Lend(){
        Closed = false;
    }

    public static List<Lend> findByClosed(Boolean closed){
        return Select.from(Lend.class)
                .where(Condition.prop("closed").eq(closed ? "1" : "0"))
                .list();
    }
}
