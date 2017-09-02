package colman.iddo.foodfeed.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tomer on 01/06/2017.
 */

public class FoodSql {
    static final String FOOD_TABLE = "foodItems";
    static final String ROWID = "ID";
    static final String FOOD_ID = "foodId";
    static final String NAME = "foodName";
    static final String TYPE = "foodType";
    static final String DESCRIPTION = "description";
    static final String PRICE = "price";
    static final String DISCOUNT = "discount";
    static final String IMAGE_URL = "imageURL";

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
        values.put(NAME, foodItem.getFoodName());
        values.put(TYPE, foodItem.getFoodType());
        values.put(DESCRIPTION, foodItem.getDescription());
        values.put(PRICE, foodItem.getPrice());
        if (foodItem.getDiscount())
            values.put(DISCOUNT, 1);
        else
            values.put(DISCOUNT, 0);
        values.put(IMAGE_URL, foodItem.getImageUrl());

        db.insert(FOOD_TABLE, FOOD_ID, values);
    }

    static void updateFoodItem(SQLiteDatabase db, FoodItem foodItem){
        ContentValues values = new ContentValues();
        values.put(FOOD_ID, foodItem.getId());
        values.put(NAME, foodItem.getFoodName());
        values.put(TYPE, foodItem.getFoodType());
        values.put(DESCRIPTION, foodItem.getDescription());
        values.put(PRICE, foodItem.getPrice());
        if (foodItem.getDiscount())
            values.put(DISCOUNT, 1);
        else
            values.put(DISCOUNT, 0);
        values.put(IMAGE_URL, foodItem.getImageUrl());

        db.update(FOOD_TABLE, values, ROWID + "= ?", new String[] { Integer.toString(getRowIndex(db, foodItem.getId())) });
    }

    static FoodItem getFoodItem(SQLiteDatabase db, String foodItemId) {
        String[] selectionArgs = {foodItemId};
        Cursor cursor = db.query(FOOD_TABLE, null, FOOD_ID + " = ? ", selectionArgs, null, null, null);
        /**
         * If we can move the cursor to the first row, it means we have at least one student in the
         * list, that we would return. Otherwise, we'll return just null.
         */
        if (cursor.moveToFirst()) {
            return createFoodItemFromCursor(cursor);
        }
        return null;
    }

    static protected void deleteFoodItem(SQLiteDatabase db, String foodItemId) {
        db.delete(FOOD_TABLE, FOOD_ID + "=" + foodItemId, null);
    }

    static boolean checkIfIdAlreadyExists(SQLiteDatabase db, String foodItemId) {
        String query = "Select * from " + FOOD_TABLE + " where " + FOOD_ID + " = " + foodItemId;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    static int getRowIndex(SQLiteDatabase db, String studentId) {
        String[] selectionArgs = { studentId };
        Cursor cursor = db.query(FOOD_TABLE, null, FOOD_ID + " = ?", selectionArgs, null, null, null);
        cursor.moveToFirst();

        /**
         * The row Index is the row's auto increment integer primary key value
         */
        int foodRowIndex = cursor.getColumnIndex(ROWID);
        String foodRow = cursor.getString(foodRowIndex);
        return Integer.parseInt(foodRow);
    }

    static void toggleChecked(SQLiteDatabase db, FoodItem foodItem){
        ContentValues values = new ContentValues();
        if (foodItem.getDiscount())
            values.put(DISCOUNT, 1);
        else
            values.put(DISCOUNT, 0);

        db.update(FOOD_TABLE, values, ROWID + "=" + getRowIndex(db, foodItem.getId()), null);
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
                NAME + " TEXT, " +
                TYPE + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                PRICE + " NUMBER, " +
                DISCOUNT + " NUMBER, " +
                IMAGE_URL + " TEXT " +
                ")");
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
        int idIndex = cursor.getColumnIndex(FOOD_ID);
        int nameIndex = cursor.getColumnIndex(NAME);
        int typeIndex = cursor.getColumnIndex(TYPE);
        int descriptionIndex = cursor.getColumnIndex(DESCRIPTION);
        int priceIndex = cursor.getColumnIndex(PRICE);
        int discountIndex = cursor.getColumnIndex(DISCOUNT);
        int imageUrlIndex = cursor.getColumnIndex(IMAGE_URL);

        FoodItem foodItem = new FoodItem();
        foodItem.setId(cursor.getString(idIndex));
        foodItem.setFoodName(cursor.getString(nameIndex));
        foodItem.setFoodType(cursor.getString(typeIndex));
        foodItem.setDescription(cursor.getString(descriptionIndex));
        foodItem.setPrice(cursor.getInt(priceIndex));
        foodItem.setDiscount(cursor.getInt(discountIndex) == 1);
        foodItem.setImageUrl(cursor.getString(imageUrlIndex));

        return foodItem;
    }
}
