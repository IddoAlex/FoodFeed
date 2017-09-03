package colman.iddo.foodfeed.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import colman.iddo.foodfeed.R;
import colman.iddo.foodfeed.model.AuthFirebase;

public class LoginActivity extends  BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        mEmailField = (EditText) findViewById(R.id.login_email_field);
        mPasswordField = (EditText) findViewById(R.id.login_password_field);

        // Buttons
        findViewById(R.id.login_login_btn).setOnClickListener(this);
        findViewById(R.id.login_register_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.login_login_btn) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.login_register_btn) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        if(AuthFirebase.instance.getIsLoggedOn()) {
            onLoginSuccess();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return false;
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        AuthFirebase.instance.signUserIn(this, email, password, new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "signInWithEmail:success");
                hideProgressDialog();
                onLoginSuccess();
            }
        }, new Runnable() {
            @Override
            public void run() {
                // If sign in fails, display a message to the user.
                hideProgressDialog();
                Toast.makeText(LoginActivity.this, "Failed to log in",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        AuthFirebase.instance.registerUser(this, email, password, new Runnable() {
            @Override
            public void run() {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success");
                hideProgressDialog();
                onLoginSuccess();
            }
        }, new Runnable() {
            @Override
            public void run() {
                // If sign in fails, display a message to the user.
                hideProgressDialog();
                Toast.makeText(LoginActivity.this, "Failed to register",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onLoginSuccess() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Toast.makeText(LoginActivity.this, "LOGIN SUCCESSFUL!",
                Toast.LENGTH_LONG).show();

        startActivity(i);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }
}