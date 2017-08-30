package colman.iddo.foodfeed.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import colman.iddo.foodfeed.R;
import colman.iddo.foodfeed.model.FoodItem;
import colman.iddo.foodfeed.model.Model;

public class FoodDetailsFragment extends Fragment {

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface DetailsFragmentListener {
        /**
         * updateStudentId intended to update the foodId string in the Activity when resuming
         * to this fragment after editing, to make sure the Edit button in the ActionBar will
         * have the updated ID string.
         * @param studentId
         */
        void updateStudentId(String studentId);
    }

    // the fragment initialization parameters
    private static final String FOOD_ID = "foodId";

    // fragment's main variables
    private FoodItem foodItem;
    private String foodId;
    private int foodRow;
    TextView name;
    TextView id;
    TextView description;

    private DetailsFragmentListener detailsFragmentListener;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_add).setVisible(false).setEnabled(false);
        menu.findItem(R.id.menu_edit).setVisible(true).setEnabled(true);
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
            foodRow = Model.instance.getRowIndex(foodId);
            foodItem = Model.instance.getFoodItem("" + foodRow);
            Log.d("DETAILS", "(onCreate) foodId got from FOOD_ID = " + foodId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DETAILS", "StudentDetailsFragment onCreateView");
        Log.d("DETAILS", "Passed foodItem id = " + foodItem.getId());

        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_food_details, container, false);
        name = (TextView) contentView.findViewById(R.id.details_name);
        description = (TextView) contentView.findViewById(R.id.details_description);

        name.setText(foodItem.getName());
        id.setText(foodItem.getId());
        description.setText(foodItem.getDescription());

        return contentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailsFragmentListener) {
            detailsFragmentListener = (DetailsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DetailsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        detailsFragmentListener = null;
    }

    @Override
    public void onPause() {
        Log.d("DETAILS", "onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        foodItem = Model.instance.getFoodItem("" + foodRow);
        if (foodItem != null){
            name.setText(foodItem.getName());
            id.setText(foodItem.getId());
            description.setText(foodItem.getDescription());
            detailsFragmentListener.updateStudentId(foodId);
            Log.d("DETAILS", "foodId now = " + foodId);
        }
        Log.d("DETAILS", "onResume");

    }
}
