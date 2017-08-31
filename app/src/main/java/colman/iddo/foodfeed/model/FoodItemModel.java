package colman.iddo.foodfeed.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.URLUtil;

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

    public interface GetImageListener{
        void onSuccess(Bitmap image);
        void onFail();
    }

    public interface SaveImageListener {
        void complete(String url);
        void fail();
    }

    public static FoodItemModel instance = new FoodItemModel();

    private final String TAG = "FoodItemModel";
    private static final String LastFoodItemsUpdateDateString = "LastUpdateDate";

    private ModelSql modelSql;
    private FoodFirebase foodFirebase;

    private FoodItemModel() {
        modelSql = new ModelSql(MainActivity.getMyContext());
        foodFirebase = new FoodFirebase();
    }

    public void getAllFoodItemsAndObserve(final GetAllStudentsAndObserveCallback callback) {
        //1. get local lastUpdateTade
        SharedPreferences pref = MainActivity.getMyContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        final double lastUpdateDate = pref.getFloat(LastFoodItemsUpdateDateString,0);
        Log.d(TAG,"lastUpdateDate: " + lastUpdateDate);

        //2. get updated records from FB
        foodFirebase.getAllFoodItemsAndObserve(lastUpdateDate, new FoodFirebase.GetAllFoodItemsAndObserveCallback() {
            @Override
            public void onComplete(List<FoodItem> list) {
                double newLastUpdateDate = lastUpdateDate;
                Log.d(TAG, "FB detch:" + list.size());
                for (FoodItem foodItem: list) {
                    //3. update the local db
                    FoodSql.addFoodItem(modelSql.getWritableDatabase(),foodItem);

                    //4. update the lastUpdateDate
                    double currUpdateDate = foodItem.getLastUpdateDate();
                    if (newLastUpdateDate < currUpdateDate){
                        newLastUpdateDate = currUpdateDate;
                    }
                }

                SharedPreferences.Editor prefEd = MainActivity.getMyContext().getSharedPreferences(TAG,
                        Context.MODE_PRIVATE).edit();
                prefEd.putFloat(LastFoodItemsUpdateDateString, (float) newLastUpdateDate);
                prefEd.commit();
                Log.d(TAG,LastFoodItemsUpdateDateString + " " + newLastUpdateDate);


                //5. read from local db
                List<FoodItem> data = FoodSql.getAllFoodItems(modelSql.getReadableDatabase());

                //6. return list of students
                callback.onComplete(data);
            }
            @Override
            public void onCancel() {

            }
        });
    }

    public FoodItem getFoodItem(String fId){
        return FoodSql.getFoodItem(modelSql.getReadableDatabase(), fId);
    }

    public void addFoodItem(FoodItem foodItem) {
        // Because observing addition to Firebase, foodItem will be added to local SQL db too
        foodFirebase.addOrUpdateFoodItem(foodItem);
    }

    public void deleteFoodItem(FoodItem f){
        Log.d(TAG, "foodId to delete:" + f.getId());
        FoodSql.deleteFoodItem(modelSql.getWritableDatabase(), f.getId());
        foodFirebase.deleteFoodItem(f.getId());
    }

    public void updateFoodItem(FoodItem foodItem) {
        foodFirebase.addOrUpdateFoodItem(foodItem);

        FoodSql.editFoodItem(modelSql.getWritableDatabase(), foodItem);

        Log.d(TAG, "FoodItem updated");
    }

    /*
        IMAGES
         */
    public void saveImage(final Bitmap imageBmp, final String name, final SaveImageListener listener) {
        foodFirebase.saveImage(imageBmp, name, new SaveImageListener() {
            @Override
            public void complete(String url) {
                String fileName = URLUtil.guessFileName(url, null, null);
                ModelFiles.saveImageToFile(imageBmp,fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });
    }

    public void getImage(final String url, final GetImageListener listener) {
        //check if image exsist localy
        final String fileName = URLUtil.guessFileName(url, null, null);
        ModelFiles.loadImageFromFileAsynch(fileName, new ModelFiles.LoadImageFromFileAsynch() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (bitmap != null){
                    Log.d(TAG,"getImage from local success " + fileName);
                    listener.onSuccess(bitmap);
                }else {
                    foodFirebase.getImage(url, new GetImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            String fileName = URLUtil.guessFileName(url, null, null);
                            Log.d(TAG,"getImage from FB success " + fileName);
                            ModelFiles.saveImageToFile(image,fileName);
                            listener.onSuccess(image);
                        }

                        @Override
                        public void onFail() {
                            Log.d(TAG,"getImage from FB fail ");
                            listener.onFail();
                        }
                    });
                }
            }
        });
    }
}
