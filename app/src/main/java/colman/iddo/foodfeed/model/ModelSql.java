package colman.iddo.foodfeed.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Tomer on 01/06/2017.
 */

/**
 * The SQLiteOpenHelper class handles the DB versions.
 */
public class ModelSql extends SQLiteOpenHelper {

    /**
     * @param context Used to let the helper class go to the application's memory to
     *                check the app version. The Context is the object that manages all the run
     *                in the background.
     */
    ModelSql(Context context) {
        /**
         * @param context described above.
         * @param name The name of the app's DB file that will be created by the helper.
         * @param factory Not in use
         * @param version The DB's version. We must increment the DB number each time it is
         *                changed, otherwise the DB scheme won't be changed.
         */
        super(context, "database.db", null, 11);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FoodSql.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FoodSql.onUpgrade(db, oldVersion, newVersion);

    }
}
