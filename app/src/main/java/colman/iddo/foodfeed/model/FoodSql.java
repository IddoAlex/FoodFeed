package colman.iddo.foodfeed.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tomer on 01/06/2017.
 */

public class FoodSql {
    static final String FOOD_TABLE = "foodItems";
    static final String ROWID = "ID";
    static final String FOOD_ID = "foodid";
    static final String FOOD_NAME = "name";
    static final String FOOD_IMAGE_URL = "imageURL";
    static final String FOOD_DESCRIPTION = "description";

    static protected List<FoodItem> getAllFoodItems(SQLiteDatabase db) {
        /**
         * in the following query we'll use "null" as the columns, to mimic SELECT * (get all cols)
         * We'll use null for the selection and selectionArgs as we don't have any WHERE statement.
         */
        Cursor cursor = db.query(FOOD_TABLE, null, null, null, null, null, null);
        List<FoodItem> list = new LinkedList<FoodItem>();
        /**
         * If we can move the cursor to the first row, it means we have at least one student in the
         * list. Otherwise, we'll return just an empty list.
         */
        if (cursor.moveToFirst()) {
            int foodIdIndex = cursor.getColumnIndex(FOOD_ID);
            int nameIndex = cursor.getColumnIndex(FOOD_NAME);
            int imageUrlIndex = cursor.getColumnIndex(FOOD_IMAGE_URL);
            int descriptionIndex = cursor.getColumnIndex(FOOD_DESCRIPTION);

            do {
                FoodItem food = new FoodItem();
                food.id = cursor.getString(foodIdIndex);
                food.name = cursor.getString(nameIndex);
                food.imageUrl = cursor.getString(imageUrlIndex);
                food.description = cursor.getString(descriptionIndex);
                list.add(food);
            } while (cursor.moveToNext());
        }
        return list;
    }

    static void addFoodItem(SQLiteDatabase db, FoodItem foodItem) {
        ContentValues values = new ContentValues();
        values.put(FOOD_ID, foodItem.id);
        values.put(FOOD_NAME, foodItem.name);
        values.put(FOOD_IMAGE_URL, foodItem.imageUrl);
        values.put(FOOD_DESCRIPTION, foodItem.description);

        db.insert(FOOD_TABLE, FOOD_ID, values);

    }

    static protected void deleteFoodItem(SQLiteDatabase db, String foodItemId) {
        db.delete(FOOD_TABLE, ROWID + "=" + getRowIndex(db, foodItemId), null);
    }

    static FoodItem getFoodItem(SQLiteDatabase db, String foodItemRow) {
        String[] selectionArgs = {foodItemRow};
        Cursor cursor = db.query(FOOD_TABLE, null, ROWID + " = ?", selectionArgs, null, null, null);
        FoodItem foodItem = new FoodItem();
        /**
         * If we can move the cursor to the first row, it means we have at least one student in the
         * list, that we would return. Otherwise, we'll return just null.
         */
        if (cursor.moveToFirst()) {
            foodItem.id = cursor.getString(cursor.getColumnIndex(FOOD_ID));
            foodItem.name = cursor.getString(cursor.getColumnIndex(FOOD_NAME));
            foodItem.imageUrl = cursor.getString(cursor.getColumnIndex(FOOD_IMAGE_URL));
            foodItem.description = cursor.getString(cursor.getColumnIndex(FOOD_DESCRIPTION));
            return foodItem;
        }
        return null;
    }

    static int getRowIndex(SQLiteDatabase db, String foodItemId) {
        String[] selectionArgs = { foodItemId };
        Cursor cursor = db.query(FOOD_TABLE, null, FOOD_ID + " = ?", selectionArgs, null, null, null);
        cursor.moveToFirst();

        /**
         * The row Index is the row's auto increment integer primary key value
         */
        int foodItemRowIndex = cursor.getColumnIndex(ROWID);
        String foodItemRow = cursor.getString(foodItemRowIndex);
        Log.d("FoodItemsSQL", "SQL FoodItem Row ID = " + foodItemRow);
        return Integer.parseInt(foodItemRow);
    }

    static void editFoodItem(SQLiteDatabase db, FoodItem foodItem, String foodItemRow){
        ContentValues values = new ContentValues();
        values.put(FOOD_ID, foodItem.id);
        values.put(FOOD_NAME, foodItem.name);
        values.put(FOOD_IMAGE_URL, foodItem.imageUrl);
        values.put(FOOD_DESCRIPTION, foodItem.description);

        db.update(FOOD_TABLE, values, ROWID + "=" + foodItemRow, null);
    }

    static boolean checkIfIdAlreadyExists(SQLiteDatabase db, String foodItemId, int index) {
        String[] selectionArgs = {foodItemId};
        Cursor cursor = db.query(FOOD_TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int sqlRowIdIndex = cursor.getColumnIndex(ROWID);
            int foodItemIdIndex = cursor.getColumnIndex(FOOD_ID);
            do {
                if (cursor.getString(foodItemIdIndex).equals(foodItemId))
                    if (Integer.parseInt(cursor.getString(sqlRowIdIndex)) != index)
                        return true;
            } while (cursor.moveToNext());
        }
        return false;
    }

    /**
     * onCreate is created once we still don't have tables in our DB.
     * We use method to initially create the DB.
     * It was originally from the SQLiteOpenHelper class, although we don't override it.
     */
    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FOOD_TABLE +
                " (" +
                ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FOOD_ID + " TEXT, " +
                FOOD_NAME + " TEXT, " +
                FOOD_IMAGE_URL + " TEXT, " +
                FOOD_DESCRIPTION + " TEXT)");
    }

    /**
     * In onUpgrade scenario we'll just delete the recreate the DB.
     * It was originally from the SQLiteOpenHelper class, although we don't override it.
     */
    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + FOOD_TABLE);
        onCreate(db);
    }
}
