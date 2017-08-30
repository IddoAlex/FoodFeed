package colman.iddo.foodfeed.model;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tomer on 01/06/2017.
 */

/**
 * This class extends the original Application class, and it's use is to have a static Context
 * variable that can be referenced for the ModelSql's CTOR, as it needs a reference for the app's
 * context in order to handle it's versions.
 * In order to use this class, I also edited the Manifest file and defined the Application's name
 * as this class.
 */
public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }
}
