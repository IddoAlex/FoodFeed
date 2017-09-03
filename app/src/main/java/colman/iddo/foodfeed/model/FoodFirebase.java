package colman.iddo.foodfeed.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Iddo on 31-Aug-17.
 */

public class FoodFirebase {

    public interface GetFoodItemCallback {
        void onComplete(FoodItem food);
        void onCancel();
    }

    public interface GetAllFoodItemsAndObserveCallback {
        void onComplete(List<FoodItem> list);
        void onCancel();
    }

    private final String TAG = "FoodFirebase";

    static final String FOOD_TABLE = "foodItems";
    static final String FOOD_ID = "fid";
    static final String NAME = "name";
    static final String TYPE = "type";
    static final String DESCRIPTION = "description";
    static final String VEGETARIAN = "vegetarian";
    static final String IMAGE_URL = "imageURL";
    static final String USER_ID = "userId";
    static final String FOOD_LAST_UPDATE_DATE = "lastUpdateDate";

    public void addOrUpdateFoodItem(FoodItem foodItem) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FOOD_TABLE);

        Map<String, Object> value = new HashMap<>();
        value.put(FOOD_ID, foodItem.getFid());
        value.put(NAME, foodItem.getName());
        value.put(TYPE, foodItem.getType());
        value.put(DESCRIPTION, foodItem.getDescription());
        value.put(VEGETARIAN, foodItem.getVegetarian());
        value.put(IMAGE_URL, foodItem.getImageUrl());
        value.put(USER_ID, foodItem.getUserId());
        value.put(FOOD_LAST_UPDATE_DATE, ServerValue.TIMESTAMP);

        myRef.child(foodItem.getFid()).setValue(value);
    }

    public void getFoodItem(String id, final GetFoodItemCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FOOD_TABLE);
        myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FoodItem food = dataSnapshot.getValue(FoodItem.class);
                callback.onComplete(food);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel();
            }
        });
    }

    public void deleteFoodItem(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FOOD_TABLE);
        myRef.child(id).removeValue();
    }

    public void getAllFoodItemsAndObserve(double lastUpdateDate,
                                          final GetAllFoodItemsAndObserveCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(FOOD_TABLE);

        myRef.orderByChild(FOOD_LAST_UPDATE_DATE).startAt(lastUpdateDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<FoodItem> list = new LinkedList<>();
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            FoodItem food = snap.getValue(FoodItem.class);
                            list.add(food);
                        }
                        callback.onComplete(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "DatabaseError occurred: " + databaseError.getMessage());
                        callback.onCancel();
                    }
                });
    }

    public void saveImage(Bitmap imageBmp, String name, final FoodItemModel.SaveImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference imagesRef = storage.getReference().child("images").child(name);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        });
    }

    public void getImage(String url, final FoodItemModel.GetImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);
        final long ONE_MEGABYTE = 1024 * 1024;

        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Log.d(TAG,exception.getMessage());
                listener.onFail();
            }
        });
    }
}