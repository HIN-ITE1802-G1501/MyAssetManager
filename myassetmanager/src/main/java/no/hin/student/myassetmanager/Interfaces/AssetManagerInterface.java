/**
* This is the AssetManager interface that represents an the interface for our items.
* @author Kurt-Erik Karlsen and Aleksander V. Grunnvoll
* @version 1.1
*/

package no.hin.student.myassetmanager.Interfaces;

import android.view.View;

public interface AssetManagerInterface {
    int getId();                            // Define method for getting the Id of the object
    String getListItemTitle();              // Define method for getting the listitem title
    String getListItemSubTitle(View view);  // Define method for getting the listitem subtitle
    int getListItemImage();                 // Define method for getting the listitem image
}
