package colman.iddo.foodfeed.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import colman.iddo.foodfeed.R;
import colman.iddo.foodfeed.model.FoodItem;
import colman.iddo.foodfeed.model.FoodItemModel;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class FoodEditFragment extends Fragment {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public final static int RESULT_SUCCESS = 0;
    public final static int RESULT_FAIL = 1;

    // fragment's main variables
    protected FoodItem foodItem;
    protected ProgressBar progressBar;

    protected TextView foodName;
    protected ImageView foodImage;
    protected TextView foodType;
    protected CheckBox vegetarian;
    protected TextView description;

    protected Bitmap imageBitmap;
    protected String foodIdString;

    // the fragment initialization parameters
    private static final String FOOD_ID = "fid";

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_add_food).setVisible(false).setEnabled(false);
        menu.findItem(R.id.menu_edit_food).setVisible(false).setEnabled(false);
    }

    public FoodEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static FoodEditFragment newInstance(String foodId) {
        FoodEditFragment fragment = new FoodEditFragment();
        Bundle args = new Bundle();
        args.putString(FOOD_ID, foodId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            foodIdString = getArguments().getString(FOOD_ID);
            foodItem = FoodItemModel.instance.getFoodItem(foodIdString);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_food_data, container, false);

        Bundle myBundle = this.getArguments();
        if (myBundle != null){
            this.foodIdString = myBundle.getString(FOOD_ID);
            foodItem = FoodItemModel.instance.getFoodItem(foodIdString);
            foodName = (EditText) contentView.findViewById(R.id.edit_name);
            foodImage = (ImageView) contentView.findViewById(R.id.edit_image);
            foodType = (EditText) contentView.findViewById(R.id.edit_type);
            vegetarian = (CheckBox) contentView.findViewById(R.id.edit_vegetarian);
            description = (EditText) contentView.findViewById(R.id.edit_description);

            progressBar = (ProgressBar) contentView.findViewById(R.id.edit_progressbar);
            progressBar.setVisibility(GONE);

            foodName.setText(foodItem.getName());
            foodType.setText(foodItem.getType());
            vegetarian.setChecked(foodItem.getVegetarian());
            description.setText(foodItem.getDescription());

            foodImage.setTag(foodItem.getImageUrl());
            foodImage.setImageDrawable(getActivity().getDrawable(R.drawable.avatar));

            if (foodItem.getImageUrl() != null && !foodItem.getImageUrl().isEmpty() && !foodItem.getImageUrl().equals("")){
                FoodItemModel.instance.getImage(foodItem.getImageUrl(), new FoodItemModel.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        String tagUrl = foodImage.getTag().toString();
                        if (tagUrl.equals(foodItem.getImageUrl())) {
                            foodImage.setImageBitmap(image);
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        }
        Button saveBtn = (Button) contentView.findViewById(R.id.edit_btn_save);
        Button deleteBtn = (Button) contentView.findViewById(R.id.edit_btn_delete);
        Button cancelBtn = (Button) contentView.findViewById(R.id.edit_btn_cancel);
        deleteBtn.setVisibility(View.VISIBLE);

        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           saveFoodItem(foodIdString);
                                       }
                                   }
        );

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToList();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFoodItem(foodIdString);
            }
        });
        return contentView;
    }

    protected void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            foodImage.setImageBitmap(imageBitmap);
        }
    }

    public void deleteFoodItem(String foodId){
        progressBar.setVisibility(View.VISIBLE);
        FoodItemModel.instance.deleteFoodItem(foodId);
        backToList();
    }

    public void saveFoodItem(String fid){
        String fidNew = fid;
        String nameNew = ((EditText) getView().findViewById(R.id.edit_name)).getText().toString();
        String typeNew = ((EditText)getView().findViewById(R.id.edit_type)).getText().toString();
        String descriptionNew = ((EditText)getView().findViewById(R.id.edit_description)).getText().toString();
        Boolean vegetarianNew = ((CheckBox)getView().findViewById(R.id.edit_vegetarian)).isChecked();

        if(!validateFoodItemFields(nameNew, typeNew, descriptionNew, vegetarianNew)) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        final FoodItem foodItemNew = new FoodItem(fidNew, nameNew, typeNew, descriptionNew, vegetarianNew, foodItem.getImageUrl(), foodItem.getUserId());

        if (imageBitmap != null) {
            FoodItemModel.instance.saveImage(imageBitmap, foodItemNew.getFid() + ".jpeg", new FoodItemModel.SaveImageListener() {
                @Override
                public void complete(String url) {
                    foodItemNew.setImageUrl(url);
                    FoodItemModel.instance.updateFoodItem(foodItemNew);
                    progressBar.setVisibility(GONE);
                    showMessage("Edit Food Details", "Food updated successfully");
                    backToList();
                }

                @Override
                public void fail() {
                    progressBar.setVisibility(GONE);
                    showMessage("Edit Food Details", "Failed editing food details");
                }
            });
        }else{
            FoodItemModel.instance.updateFoodItem(foodItemNew);
            progressBar.setVisibility(GONE);
            showMessage("Edit Food Details", "Food updated successfully");
            backToList();
        }
    }

    protected void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("TAG", "OK");
            }
        });
        builder.show();
    }

    protected void backToList(){
        getFragmentManager().popBackStack(0,  FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    protected boolean validateFoodItemFields(String name,String type, String description, boolean vegeratian ) {
        boolean isValid = true;
        String errorMsg = "";

        if(TextUtils.isEmpty(name)) {
            isValid = false;
            errorMsg += "Name can not be empty.\n";
        }

        if(TextUtils.isEmpty(type)) {
            isValid = false;
            errorMsg += "Type can not be empty.\n";
        }

        if(TextUtils.isEmpty(description)) {
            isValid = false;
            errorMsg += "Description can not be empty.\n";
        }

        if(!isValid) {
            showMessage("Invalid input", errorMsg);
        }

        return isValid;
    }

}
