package pappbence.bme.hu.lendr.data;

import android.graphics.Bitmap;

import com.orm.SugarRecord;

import pappbence.bme.hu.lendr.BitmapUtil;

public class ItemImage extends SugarRecord<ItemImage>{
    public LendrItem Item;
    private String BitmapString;

    public Bitmap getImage(){
        return BitmapUtil.StringToBitmap(BitmapString);
    }

    public void setImage(Bitmap image){
        BitmapString = BitmapUtil.BitmapToString(image);
    }
}