package colman.iddo.foodfeed.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import colman.iddo.foodfeed.R;
import colman.iddo.foodfeed.fragments.FoodDetailsFragment;
import colman.iddo.foodfeed.fragments.FoodEditFragment;
import colman.iddo.foodfeed.fragments.FoodItemsListFragment;
import colman.iddo.foodfeed.model.AuthFirebase;
import colman.iddo.foodfeed.fragments.FoodNewFragment;
import colman.iddo.foodfeed.model.FoodItem;
import colman.iddo.foodfeed.model.FoodItemModel;

public class MainActivity extends BaseActivity implements FoodDetailsFragment.FoodIdUpdateListener, FoodItemsListFragment.ListFragmentListener {

    private static Context context;
    private String foodIdString;
    private final String TAG = "MainActivity";
    private final String OPTIONS_MENU_TAG = "OptionsMenuTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        if (savedInstanceState == null) {
            FoodItemsListFragment foodItemsListFragment = FoodItemsListFragment.newInstance();
            FragmentTransaction tran = getFragmentManager().beginTransaction();
            tran.add(R.id.main_fragment_container, foodItemsListFragment);
            tran.commit();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.sign_out_btn).setVisible(true);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    public static Context getMyContext() {
        return context;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"item selected");
        switch (item.getItemId()){
            case R.id.menu_add_food:
                Log.d(OPTIONS_MENU_TAG,"Add button pressed");
                getActionBar().setDisplayHomeAsUpEnabled(true);
                FoodNewFragment foodNewFragment = FoodNewFragment.newInstance();
                FragmentTransaction tranAdd = getFragmentManager().beginTransaction();
                tranAdd.replace(R.id.main_fragment_container, foodNewFragment);
                tranAdd.addToBackStack(null); //add current fragment to stack
                tranAdd.commit();
                break;
            case R.id.menu_edit_food:
                Log.d(OPTIONS_MENU_TAG,"Edit button pressed");
                if (checkUserPermission(foodIdString)) {
                    getActionBar().setDisplayHomeAsUpEnabled(true);
                    FoodEditFragment foodEditFragment = FoodEditFragment.newInstance(foodIdString);
                    FragmentTransaction tranEdit = getFragmentManager().beginTransaction();
                    tranEdit.replace(R.id.main_fragment_container, foodEditFragment);
                    tranEdit.addToBackStack("foodDetailsFragment"); //add current fragment to stack
                    tranEdit.commit();
                }
                else
                    Toast.makeText(MainActivity.this, "Error: You are permitted to edit only your own posts.",
                            Toast.LENGTH_LONG).show();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.sign_out_btn:
                signOut();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void signOut() {
        AuthFirebase.instance.getFirebaseAuth().signOut();

        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_LONG).show();

        startActivity(i);
    }

    private boolean checkUserPermission(String foodItemId){
        String currentUser = AuthFirebase.instance.getCurrentFirebaseUserId();
        FoodItem foodItem = FoodItemModel.instance.getFoodItem(foodItemId);
        if (currentUser.equals(foodItem.getUserId()))
            return true;
        else
            return false;
    }

    @Override
    public void updateFoodIdInActivity(String foodId) {
        foodIdString = foodId;
    }

    @Override
    public void onFoodItemSelected(String foodId) {
        Log.d("MAIN", "FoodId selected " + foodId);
        foodIdString = foodId;

        FoodDetailsFragment foodDetailsFragment = FoodDetailsFragment.newInstance(foodId);
        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.replace(R.id.main_fragment_container, foodDetailsFragment);
        tran.addToBackStack("foodListFragment");
        tran.commit();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
