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
