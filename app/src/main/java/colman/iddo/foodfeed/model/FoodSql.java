package colman.iddo.foodfeed.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tomer on 01/06/2017.
 */

public class FoodSql {
    static final String FOOD_TABLE = "foodItems";
    static final String FOOD_ID = "foodId";
    static final String FOOD_NAME = "name";
    static final String FOOD_IMAGE_URL = "imageURL";
    static final String FOOD_DESCRIPTION = "description";
    static final String FOOD_USER_ID = "userId";

    static List<FoodItem> getAllFoodItems(SQLiteDatabase db) {
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
            do {
                list.add(createFoodItemFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return list;
    }

    static void addFoodItem(SQLiteDatabase db, FoodItem foodItem) {
        ContentValues values = new ContentValues();
        values.put(FOOD_ID, foodItem.getId());
        values.put(FOOD_NAME, foodItem.getName());
        values.put(FOOD_IMAGE_URL, foodItem.getImageUrl());
        values.put(FOOD_DESCRIPTION, foodItem.getDescription());
        values.put(FOOD_USER_ID, foodItem.getUserId());

        db.insert(FOOD_TABLE, FOOD_ID, values);

    }

    static protected void deleteFoodItem(SQLiteDatabase db, String foodItemId) {
        db.delete(FOOD_TABLE, FOOD_ID + "=" + foodItemId, null);
    }

    static FoodItem getFoodItem(SQLiteDatabase db, String foodItemId) {
        String[] selectionArgs = {foodItemId};
        Cursor cursor = db.query(FOOD_TABLE, null, FOOD_ID + " = ?", selectionArgs, null, null, null);

        /**
         * If we can move the cursor to the first row, it means we have at least one student in the
         * list, that we would return. Otherwise, we'll return just null.
         */
        if (cursor.moveToFirst()) {
            return createFoodItemFromCursor(cursor);
        }
        return null;
    }

    static void editFoodItem(SQLiteDatabase db, FoodItem foodItem){
        ContentValues values = new ContentValues();
        values.put(FOOD_ID, foodItem.getId());
        values.put(FOOD_NAME, foodItem.getName());
        values.put(FOOD_IMAGE_URL, foodItem.getImageUrl());
        values.put(FOOD_DESCRIPTION, foodItem.getDescription());
        values.put(FOOD_USER_ID, foodItem.getUserId());

        db.update(FOOD_TABLE, values, FOOD_ID + "=" + foodItem.getId(), null);
    }

    static boolean checkIfIdAlreadyExists(SQLiteDatabase db, String foodItemId) {
        /*
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
        */
        String query = "Select * from " + FOOD_TABLE + " where " + FOOD_ID + " = " + foodItemId;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    /**
     * onCreate is created once we still don't have tables in our DB.
     * We use method to initially create the DB.
     * It was originally from the SQLiteOpenHelper class, although we don't override it.
     */
    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FOOD_TABLE +
                " (" +
                FOOD_ID + " TEXT PRIMARY KEY , " +
                FOOD_NAME + " TEXT, " +
                FOOD_IMAGE_URL + " TEXT, " +
                FOOD_DESCRIPTION + " TEXT, " +
                FOOD_USER_ID + " TEXT)");
    }

    /**
     * In onUpgrade scenario we'll just delete the recreate the DB.
     * It was originally from the SQLiteOpenHelper class, although we don't override it.
     */
    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + FOOD_TABLE);
        onCreate(db);
    }

    @NonNull
    private static FoodItem createFoodItemFromCursor(Cursor cursor) {
        FoodItem fi = new FoodItem();

        fi.setId(cursor.getString(cursor.getColumnIndex(FOOD_ID)));
        fi.setName(cursor.getString(cursor.getColumnIndex(FOOD_NAME)));
        fi.setImageUrl(cursor.getString(cursor.getColumnIndex(FOOD_IMAGE_URL)));
        fi.setDescription(cursor.getString(cursor.getColumnIndex(FOOD_DESCRIPTION)));
        fi.setUserId(cursor.getString(cursor.getColumnIndex(FOOD_USER_ID)));

        return fi;
    }
}
