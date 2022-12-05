package edu.uga.cs.finalmobiledevproject;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import java.util.Arrays;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode( AppCompatDelegate.MODE_NIGHT_YES );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d( DEBUG_TAG, "FinalMobileDevProject: MainActivity.onCreate()" );
        Button signInButton = findViewById( R.id.button1 );
        Button registerButton = findViewById( R.id.button2 );
        signInButton.setOnClickListener( new SignInButtonClickListener() );
        registerButton.setOnClickListener( new RegisterButtonClickListener() );
    }
    // A button listener class to start a Firebase sign-in process
    private class SignInButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick( View v ) {
            // This is an example of how to use the AuthUI activity for signing in to Firebase.
            // Here, we are just using email/password sign in.
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build()
            );
            Log.d( DEBUG_TAG, "MainActivity.SignInButtonClickListener: Signing in started" );
            // Create an Intent to singin to Firebese.
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        }
    }
    // The ActivityResultLauncher class provides a new way to invoke an activity
    // for some result.  It is a replacement for the deprecated method startActivityForResult.
    //
    // The signInLauncher variable is a launcher to start the AuthUI's logging in process that
    // should return to the MainActivity when completed.  The overridden onActivityResult
    // is then called when the Firebase logging-in process is finished.
    private ActivityResultLauncher<Intent> signInLauncher =
            registerForActivityResult(
                    new FirebaseAuthUIActivityResultContract(),
                    new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                        @Override
                        public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                            onSignInResult(result);
                        }
                    }
            );
    private void onSignInResult( FirebaseAuthUIAuthenticationResult result ) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            if( response != null ) {
                Log.d( DEBUG_TAG, "MainActivity.onSignInResult: response.getEmail(): " + response.getEmail() );
            }
            // after a successful sign in, start the user(roommate) management activity
            Intent intent = new Intent( this, UserManagementActivity.class );
            startActivity( intent );
        }
        else {
            Log.d( DEBUG_TAG, "MainActivity.onSignInResult: Failed to sign in" );
            // Sign in failed. If response is null the user canceled the
            Toast.makeText( getApplicationContext(),
                    "Sign in failed",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private class RegisterButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // start the user registration activity
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            view.getContext().startActivity(intent);
        }
    }
    @Override
    protected void onStart() {
        Log.d( DEBUG_TAG, "FinalMobileDevProject: MainActivity.onStart()" );
        super.onStart();
    }
    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "FinalMobileDevProject: MainActivity.onResume()" );
        super.onResume();
    }
    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "FinalMobileDevProject: MainActivity.onPause()" );
        super.onPause();
    }
    @Override
    protected void onStop() {
        Log.d( DEBUG_TAG, "FinalMobileDevProject: MainActivity.onStop()" );
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.d( DEBUG_TAG, "FinalMobileDevProject: MainActivity.onDestroy()" );
        super.onDestroy();
    }
    @Override
    protected void onRestart() {
        Log.d( DEBUG_TAG, "FinalMobileDevProject: MainActivity.onRestart()" );
        super.onRestart();
    }
}