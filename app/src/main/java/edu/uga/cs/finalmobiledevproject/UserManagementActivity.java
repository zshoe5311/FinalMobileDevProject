package edu.uga.cs.finalmobiledevproject;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class UserManagementActivity extends AppCompatActivity {
    private static final String DEBUG_TAG = "UserManagementActivity";
    private TextView signedInTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_management_activity);
        Log.d( DEBUG_TAG, "UserManagementActivity.onCreate()" );
        Button viewListButton = findViewById(R.id.button4);
        Button reviewLeadsButton = findViewById(R.id.button5);
        signedInTextView = findViewById( R.id.textView4);
        viewListButton.setOnClickListener( new ViewListButtonClickListener() );
        reviewLeadsButton.setOnClickListener( new ReviewLeadsButtonClickListener() );
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if( currentUser != null ) {
                    // User is signed in
                    Log.d(DEBUG_TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
                    String userEmail = currentUser.getEmail();
                    signedInTextView.setText( "Roommate Status: Signed in as " + userEmail );
                } else {
                    // User is signed out
                    Log.d( DEBUG_TAG, "onAuthStateChanged:signed_out" );
                    signedInTextView.setText( "Roommate Status: Roommate not signed in" );
                }
            }
        });
    }
    private class ViewListButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            /*Intent intent = new Intent(view.getContext(), ViewListActivity.class);
            view.getContext().startActivity( intent );*/
        }
    }
    private class ReviewLeadsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            /*Intent intent = new Intent(view.getContext(), LogoutUserActivity.class);
            view.getContext().startActivity(intent);*/
        }
    }
    @Override
    protected void onStart() {
        Log.d( DEBUG_TAG, "UserManagementActivity.onStart()" );
        super.onStart();
    }
    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "UserManagementActivity.onResume()" );
        super.onResume();
    }
    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "UserManagementActivity.onPause()" );
        super.onPause();
    }
    @Override
    protected void onStop() {
        Log.d( DEBUG_TAG, "UserManagementActivity.onStop()" );
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.d( DEBUG_TAG, "UserManagementActivity.onDestroy()" );
        super.onDestroy();
    }
    @Override
    protected void onRestart() {
        Log.d( DEBUG_TAG, "UserManagementActivity.onRestart()" );
        super.onRestart();
    }
}