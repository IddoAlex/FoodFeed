package colman.iddo.foodfeed.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import colman.iddo.foodfeed.R;
import colman.iddo.foodfeed.model.FoodItem;
import colman.iddo.foodfeed.model.FoodItemModel;

public class FoodDetailsFragment extends Fragment {

    // the fragment initialization parameters
    private static final String FOOD_ID = "FOOD_ID";

    // fragment's main variables
    protected FoodItem foodItem;
    protected String foodId;
    protected TextView id;
    protected TextView foodName;
    protected ImageView foodImage;
    protected TextView foodType;
    protected ImageView vegetarian;
    protected TextView description;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.addFoodItemBtn).setVisible(false).setEnabled(false);
        menu.findItem(R.id.editFoodItemBtn).setVisible(true).setEnabled(true);
    }

    public FoodDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static FoodDetailsFragment newInstance(String foodId) {
        FoodDetailsFragment fragment = new FoodDetailsFragment();
        Bundle args = new Bundle();
        args.putString(FOOD_ID, foodId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu (true);

        if (getArguments() != null) {
            foodId = getArguments().getString(FOOD_ID);
            foodItem = FoodItemModel.instance.getFoodItem(foodId);
            Log.d("DETAILS", "(onCreate) foodId got from FOOD_ID = " + foodId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DETAILS", "FoodDetailsFragment onCreateView");
        Log.d("DETAILS", "Passed foodItem id = " + foodItem.getId());

        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_food_details, container, false);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle myBundle = this.getArguments();

        if (myBundle != null){
            foodId = myBundle.getString(FOOD_ID);
            foodItem = FoodItemModel.instance.getFoodItem(foodId);
            foodType = contentView.findViewById(R.id.details_food_type);
            vegetarian = (ImageView) contentView.findViewById(R.id.details_food_vegetarian);
            description = (TextView) contentView.findViewById(R.id.details_food_description);
            foodImage = (ImageView) contentView.findViewById(R.id.details_food_image);

            foodName.setText(foodItem.getName());
            foodType.setText(foodItem.getType());
            if (foodItem.getVegetarian())
                vegetarian.setVisibility(View.VISIBLE);
            else
                vegetarian.setVisibility(View.INVISIBLE);
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

        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        foodItem = FoodItemModel.instance.getFoodItem(foodId);
        if (foodItem != null){
            description.setText(foodItem.getDescription());
        }
        Log.d("DETAILS", "onResume");

    }
}
