package no.hin.student.myassetmanager.Interfaces;


import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

public interface MyInterface {
    int getId();
    String getListItemTitle();
    String getListItemSubTitle(View view);
    int getListItemImage();
}
