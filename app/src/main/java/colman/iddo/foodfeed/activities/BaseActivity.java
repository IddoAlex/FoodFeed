package colman.iddo.foodfeed.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import colman.iddo.foodfeed.R;
import colman.iddo.foodfeed.fragments.FoodEditFragment;
import colman.iddo.foodfeed.fragments.FoodNewFragment;

public class BaseActivity extends Activity {

    private final String OPTIONS_MENU_TAG = "OptionsMenuTag";
    String foodIdString;

    public interface ImageListener{
        void sendImageOnResult(Bundle extras);
    }


    // TODO?: Remove ProgressDialog
    private ProgressDialog mProgressDialog;
    private String _loadingString = "Loading...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(OPTIONS_MENU_TAG,"menu item selected");
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
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(_loadingString);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    //Override the onBackPressed in order to return to previous fragment on back button press
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getFragmentManager().popBackStack();
        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
