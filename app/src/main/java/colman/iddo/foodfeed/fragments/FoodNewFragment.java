package colman.iddo.foodfeed.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import colman.iddo.foodfeed.R;

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
        final EditText foodName = (EditText) contentView.findViewById(R.id.edit_name);
        final EditText foodDescription = (EditText) contentView.findViewById(R.id.edit_description);

        Button saveBtn = (Button) contentView.findViewById(R.id.edit_btn_save);
        Button cancelBtn = (Button) contentView.findViewById(R.id.edit_btn_cancel);
        Button deleteBtn = (Button) contentView.findViewById(R.id.edit_btn_delete);
        deleteBtn.setVisibility(View.INVISIBLE);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NEW", "Save button was clicked!");
                    foodDataScreensListener.onSaveButtonClick(true);
                }

        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EDIT", "Cancel button was clicked!");
                foodDataScreensListener.onCancelButtonClick();
            }
        });
        return contentView;
    }
}