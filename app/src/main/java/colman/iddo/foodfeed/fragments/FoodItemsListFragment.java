package colman.iddo.foodfeed.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import colman.iddo.foodfeed.R;
import colman.iddo.foodfeed.model.FoodItemModel;
import colman.iddo.foodfeed.model.FoodItem;

public class FoodItemsListFragment extends Fragment {

    static final int REQUEST_ADD_ID = 1;
    static final int REQUEST_WRITE_STORAGE = 2;
    private static final String FOOD_ID = "FOOD_ID";

    ListView list;
    private List<FoodItem> data = new LinkedList<>();
    FoodItemsAdapter adapter;

    public FoodItemsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();

        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_food_list, container, false);

        list = (ListView) contentView.findViewById(R.id.foodItemsList);
        adapter = new FoodItemsAdapter(getActivity());

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodDetailsFragment studentDetailsFragment = new FoodDetailsFragment();
                Bundle myBundle = new Bundle();
                myBundle.putString(FOOD_ID, data.get(position).getId());
                studentDetailsFragment.setArguments(myBundle);
                getFragmentManager().beginTransaction()
                        .replace(((ViewGroup) getView().getParent()).getId(), studentDetailsFragment).addToBackStack(null).commit();
            }
        });

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading Food Items...");
        pd.show();

        FoodItemModel.instance.getAllFoodItemsAndObserve(new FoodItemModel.GetAllStudentsAndObserveCallback() {
            @Override
            public void onComplete(List<FoodItem> list) {
                data = list;
                adapter.notifyDataSetChanged();
                pd.dismiss();
            }

            @Override
            public void onCancel() {
                pd.dismiss();
            }
        });

        // App permissions
        boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }

        return contentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CHECK", "FoodItemsListFragment: onActivityResult");

        if (requestCode == REQUEST_ADD_ID){
            if (resultCode == FoodNewFragment.RESULT_SUCCESS){
                //operation success
                Log.d("TAG","operation success");
            }else{
                Log.d("TAG","operation fail");
            }
        }else{
            if (resultCode == REQUEST_WRITE_STORAGE){
                Log.d("TAG", "REQUEST_WRITE_STORAGE");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getFragmentManager().getBackStackEntryCount() == 0){
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    class FoodItemsAdapter extends BaseAdapter {

        private Context context;

        public FoodItemsAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.food_items_row, null);
            }

            final TextView foodName = (TextView)convertView.findViewById(R.id.row_food_name);
            final TextView foodId = (TextView)convertView.findViewById(R.id.row_food_id);
            final ImageView discount = (ImageView)convertView.findViewById(R.id.row_food_discount);
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.row_food_image);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.row_food_progressbar);
            final TextView foodType = (TextView) convertView.findViewById(R.id.row_food_type);
            final TextView price = (TextView) convertView.findViewById(R.id.row_food_price);
            final TextView description = (TextView) convertView.findViewById(R.id.row_food_description);

            final FoodItem foodItem = data.get(position);
            foodName.setText(foodItem.getFoodName());
            foodId.setText(foodItem.getId());
            if (foodItem.getDiscount())
                discount.setVisibility(View.VISIBLE);
            else
                discount.setVisibility(View.INVISIBLE);
            foodType.setText(foodItem.getFoodType());
            price.setText(foodItem.getPrice());
            description.setText(foodItem.getDescription());

            imageView.setTag(foodItem.getImageUrl());
            imageView.setImageDrawable(getActivity().getDrawable(R.drawable.avatar));

            if (foodItem.getImageUrl() != null && !foodItem.getImageUrl().isEmpty() && !foodItem.getImageUrl().equals("")){
                progressBar.setVisibility(View.VISIBLE);
                FoodItemModel.instance.getImage(foodItem.getImageUrl(), new FoodItemModel.GetImageListener() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        String tagUrl = imageView.getTag().toString();
                        if (tagUrl.equals(foodItem.getImageUrl())) {
                            imageView.setImageBitmap(image);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFail() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            return convertView;
        }
    }
}
