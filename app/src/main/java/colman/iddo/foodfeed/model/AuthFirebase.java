package colman.iddo.foodfeed.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by Iddo on 03-Sep-17.
 */

public class AuthFirebase {
    public static AuthFirebase instance = new AuthFirebase();
    private FirebaseAuth mAuth;
    private final String TAG = "AuthFirebase";

    private AuthFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    public boolean getIsLoggedOn() {
        return getFirebaseAuth().getCurrentUser() != null;
    }

    public void signUserIn(Activity caller, String email, String password, final Runnable onSuccessTask, final Runnable onFailTask){
        instance.getFirebaseAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(caller, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onSuccessTask.run();
                        } else {

                            onFailTask.run();

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    public void registerUser(Activity caller, String email, String password, final Runnable onSuccessTask, final Runnable onFailTask) {
        instance.getFirebaseAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(caller, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onSuccessTask.run();
                        } else {

                            onFailTask.run();

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    public String getCurrentFirebaseUserId() {
        FirebaseUser user = mAuth.getCurrentUser();
        return user != null? user.getUid() : null;
    }
}