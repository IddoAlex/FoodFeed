package colman.iddo.foodfeed.model;

import java.util.List;

/**
 * This class represents the Students model data, it is designed in singleton pattern,
 * so only one instance of it will be created.
 */
public class Model {
    public final static Model instance = new Model();
    private ModelSql modelSql;

    private Model(){
        modelSql = new ModelSql(MyApplication.context);
    }

    public List<FoodItem> getAllFoodItems(){
        return FoodSql.getAllFoodItems(modelSql.getReadableDatabase());
    }

    public void addFoodItem(FoodItem foodItem){
        FoodSql.addFoodItem(modelSql.getWritableDatabase(), foodItem);
    }

    public void deleteFoodItem(String foodItemId){ FoodSql.deleteFoodItem(modelSql.getWritableDatabase(), foodItemId); }

    public FoodItem getFoodItem(String foodItemId) { return FoodSql.getFoodItem(modelSql.getReadableDatabase(), foodItemId); }

    public int getRowIndex(String foodItemId){ return FoodSql.getRowIndex(modelSql.getReadableDatabase(), foodItemId); }

    //public int getRowIndex(FoodItem foodItem) { return modelMem.getRowIndex(foodItem); }

    /**
     * We have to use edit methods for the foodItem object and for the checkbox toggle,
     * as we have to send query to the DB in order edit them, and not just edit the FoodItem object
     * which is located in the app's memory (as in memModel).
     * @param foodItem
     */
    public void editFoodItem(FoodItem foodItem, String foodItemRow) { FoodSql.editFoodItem(modelSql.getWritableDatabase(), foodItem, foodItemRow); }

    /**
     * FoodItemID Input validation method - check if ID entered as input already exists in other
     * FoodItem in DB. If it doesn't exist, return false (validation succeeded).
     * Otherwise, return true.
     * @param foodItemId the given foodItemId to check if already exists
     * @param index the index of an existing foodItem object in the DB list. Used only in case of
     *              doing the validation in case of existing student.
     *              In case of new student - the index will be -1.
     */
    public boolean checkIfIdAlreadyExists(String foodItemId, int index) {
        return FoodSql.checkIfIdAlreadyExists(modelSql.getReadableDatabase(), foodItemId, index); }
}