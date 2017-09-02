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
import colman.iddo.foodfeed.fragments.FoodEditFragment;
import colman.iddo.foodfeed.fragments.FoodItemsListFragment;
import colman.iddo.foodfeed.model.AuthFirebase;
import colman.iddo.foodfeed.fragments.FoodNewFragment;

public class MainActivity extends BaseActivity {

    private static Context context;
    private final String TAG = "MainActivity";
    private final String OPTIONS_MENU_TAG = "OptionsMenuTag";
    FoodItemsListFragment foodItemsListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(false);

        foodItemsListFragment = new FoodItemsListFragment();

        FragmentTransaction tran = getFragmentManager().beginTransaction();
        tran.add(R.id.main_fragment_container, foodItemsListFragment, "tag");
        tran.show(foodItemsListFragment);
        tran.commit();
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
            case R.id.addFoodItemBtn:
                Log.d(OPTIONS_MENU_TAG,"Add button pressed");
                getActionBar().setDisplayHomeAsUpEnabled(true);
                FoodNewFragment foodNewFragment = FoodNewFragment.newInstance();
                FragmentTransaction tranAdd = getFragmentManager().beginTransaction();
                tranAdd.replace(R.id.main_fragment_container, foodNewFragment);
                tranAdd.addToBackStack(null); //add current fragment to stack
                tranAdd.commit();
                break;
            case R.id.editFoodItemBtn:
                Log.d(OPTIONS_MENU_TAG,"Edit button pressed");
                getActionBar().setDisplayHomeAsUpEnabled(true);
                FoodEditFragment foodEditFragment = FoodEditFragment.newInstance(foodIdString);
                FragmentTransaction tranEdit = getFragmentManager().beginTransaction();
                tranEdit.replace(R.id.main_fragment_container, foodEditFragment );
                tranEdit.addToBackStack("foodDetailsFragment"); //add current fragment to stack
                tranEdit.commit();
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
}
