package colman.iddo.foodfeed.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import colman.iddo.foodfeed.R;
import colman.iddo.foodfeed.model.FoodItem;
import colman.iddo.foodfeed.model.FoodItemModel;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class FoodNewFragment extends FoodEditFragment {

    public FoodNewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static FoodNewFragment newInstance() {
        FoodNewFragment fragment = new FoodNewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("NEW", "(OnCreate) New Food");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_food_data, container, false);

        id = (EditText) contentView.findViewById(R.id.edit_id);
        foodName = (EditText) contentView.findViewById(R.id.edit_name);
        foodImage = (ImageView) contentView.findViewById(R.id.edit_image);
        foodType = (EditText) contentView.findViewById(R.id.edit_type);
        discount = (CheckBox) contentView.findViewById(R.id.edit_discount);
        price = (EditText) contentView.findViewById(R.id.edit_price);
        description = (EditText) contentView.findViewById(R.id.edit_description);

        Button saveBtn = (Button) contentView.findViewById(R.id.edit_btn_save);
        Button cancelBtn = (Button) contentView.findViewById(R.id.edit_btn_cancel);
        Button deleteBtn = (Button) contentView.findViewById(R.id.edit_btn_delete);

        deleteBtn.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar) contentView.findViewById(R.id.edit_progressbar);
        progressBar.setVisibility(GONE);

        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImageSelectionMenu();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodId = ((EditText)getView().findViewById(R.id.edit_id)).getText().toString();
                if(FoodItemModel.instance.checkIfIdAlreadyExists(foodId))
                    showMessage("Add New Student", "Id Already Exist");
                else
                    createFoodItem();
                }

        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToList();
            }
        });

        return contentView;
    }

    private void createFoodItem(){
        String idNew = ((EditText)getView().findViewById(R.id.edit_id)).getText().toString();
        String nameNew = ((EditText) getView().findViewById(R.id.edit_name)).getText().toString();
        String typeNew = ((EditText)getView().findViewById(R.id.edit_type)).getText().toString();
        String descriptionNew = ((EditText)getView().findViewById(R.id.edit_description)).getText().toString();
        String priceNew = ((EditText)getView().findViewById(R.id.edit_price)).getText().toString();
        Boolean discountNew = ((CheckBox)getView().findViewById(R.id.edit_discount)).isChecked();

        progressBar.setVisibility(View.VISIBLE);
        final FoodItem foodItem = new FoodItem(idNew, nameNew, typeNew, descriptionNew, Integer.parseInt(priceNew), discountNew);

        if (imageBitmap != null) {
            FoodItemModel.instance.saveImage(imageBitmap, foodItem.getId() + ".jpeg", new FoodItemModel.SaveImageListener() {
                @Override
                public void complete(String url) {
                    foodItem.setImageUrl(url);
                    FoodItemModel.instance.addFoodItem(foodItem);
                    progressBar.setVisibility(GONE);
                    showMessage("Add New Student", "New student added successfully");
                    backToList();
                }

                @Override
                public void fail() {
                    progressBar.setVisibility(GONE);
                    showMessage("Add New Student", "Failed adding new student");
                }
            });
        }else{
            FoodItemModel.instance.addFoodItem(foodItem);
            progressBar.setVisibility(GONE);
            showMessage("Add New Student", "New student added successfully");
            backToList();
        }
    }



}