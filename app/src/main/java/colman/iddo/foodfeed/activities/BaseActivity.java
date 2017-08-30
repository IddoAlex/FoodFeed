package colman.iddo.foodfeed.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import colman.iddo.foodfeed.R;

import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends Activity {

    private final String OPTIONS_MENU_TAG = "OptionsMenuTag";

    private FirebaseAuth mAuth;
    // TODO?: Remove ProgressDialog
    private ProgressDialog mProgressDialog;
    private String _loadingString = "Loading...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
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
        Log.d(OPTIONS_MENU_TAG,"menu item selcted");
        switch (item.getItemId()){
            case R.id.addFoodItemBtn:
                Log.d(OPTIONS_MENU_TAG,"Add button pressed");
                /*
                // TODO: Change accordingly
                NewStudentFragment newStudentFragment = new NewStudentFragment();
                getFragmentManager().beginTransaction()
                        .replace(((ViewGroup) getView().getParent()).getId(), newStudentFragment).addToBackStack(null).commit();
                */
                break;
            default:
                return false;
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

    protected FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }
}
