package colman.iddo.foodfeed.model;

import java.util.List;

import colman.iddo.foodfeed.activities.MainActivity;

/**
 * Created by Iddo on 30-Aug-17.
 */

public class FoodItemModel {

    public interface GetAllStudentsAndObserveCallback {
        void onComplete(List<FoodItem> list);
        void onCancel();
    }

    public static FoodItemModel instance = new FoodItemModel();

    private ModelSql modelSql;

    private FoodItemModel() {
        modelSql = new ModelSql(MainActivity.getMyContext());
    }

    public void getAllFoodItemsAndObserve(GetAllStudentsAndObserveCallback callback) {
        List<FoodItem> list = FoodSql.getAllFoodItems(modelSql.getReadableDatabase());

        callback.onComplete(list);
    }
}
