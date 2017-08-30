package colman.iddo.foodfeed.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import colman.iddo.foodfeed.R;
import colman.iddo.foodfeed.model.FoodItem;
import colman.iddo.foodfeed.model.Model;

public class FoodEditFragment extends android.app.Fragment {

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface FoodDataScreensListener {

        /**
         * Use the save button and pass if the id validation check passed (true) or failed.
         * If passed, Student details will be changed / created.
         * Otherwise, an error alert message will be displayed about the existing ID.
         * @param doesIdExists a boolean that represent if the id validation check passed/failed.
         */
        void onSaveButtonClick(boolean doesIdExists);
        void onDeleteButtonClick();
        void onCancelButtonClick();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_add).setVisible(false).setEnabled(false);
        menu.findItem(R.id.menu_edit).setVisible(false).setEnabled(false);
    }

    // the fragment initialization parameters
    private static final String FOOD_ID = "foodIdString";

    // fragment's main variables
    protected FoodDataScreensListener foodDataScreensListener;
    private FoodItem food;
    private String foodIdString;
    private int foodRow;

    public FoodEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static FoodEditFragment newInstance(String foodItemId) {
        FoodEditFragment fragment = new FoodEditFragment();
        Bundle args = new Bundle();
        args.putString(FOOD_ID, foodItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            foodIdString = getArguments().getString(FOOD_ID);
            foodRow = Model.instance.getRowIndex(foodIdString);
            food = Model.instance.getFoodItem("" + foodRow);
            Log.d("EDIT", "(onCreate) foodIdString got from FOOD_ID = " + foodIdString);
            Log.d("EDIT", "(onCreate) food object index in DB = " + foodRow);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_food_data, container, false);
        final EditText foodItemName = (EditText) contentView.findViewById(R.id.edit_name);
        final EditText foodItemDescription = (EditText) contentView.findViewById(R.id.edit_description);

        foodItemName.setText(food.getName());
        foodItemDescription.setText(food.getDescription());

        Button saveBtn = (Button) contentView.findViewById(R.id.edit_btn_save);
        Button deleteBtn = (Button) contentView.findViewById(R.id.edit_btn_delete);
        Button cancelBtn = (Button) contentView.findViewById(R.id.edit_btn_cancel);
        deleteBtn.setVisibility(View.VISIBLE);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EDIT", "Save button was clicked!");

                    food.setName(String.valueOf(foodItemName.getText()));
                    food.setDescription(String.valueOf(foodItemDescription.getText()));
                    Model.instance.editFoodItem(food, Integer.toString(foodRow));
                    foodDataScreensListener.onSaveButtonClick(true);
                }
            }
        );

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EDIT", "Cancel button was clicked!");
                foodDataScreensListener.onCancelButtonClick();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodDataScreensListener.onDeleteButtonClick();
                Log.d("EDIT", "Delete button was clicked!");
                Model.instance.deleteFoodItem(food.getId());

            }
        });
        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FoodDataScreensListener) {
            foodDataScreensListener = (FoodDataScreensListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FoodDataScreensListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        foodDataScreensListener = null;
    }
}
