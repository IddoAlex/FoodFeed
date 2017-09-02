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
import colman.iddo.foodfeed.fragments.FoodItemsListFragment;

public class MainActivity extends BaseActivity{

    private static Context context;
    private final String TAG = "MainActivity";
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
            case android.R.id.home:
                Log.d(TAG,"android.R.id.home" + getFragmentManager().getBackStackEntryCount());
                getFragmentManager().popBackStack();
                Log.d(TAG,"android.R.id.home" + getFragmentManager().getBackStackEntryCount());
                break;
            case R.id.sign_out_btn:
                signOut();
                break;
            default:
                return false;
        }
        return true;
    }

    private void signOut() {
        getFirebaseAuth().signOut();

        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_LONG).show();

        startActivity(i);
    }
}
