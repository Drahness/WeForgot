package self.joanciscar.myapplication.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import self.joanciscar.myapplication.MainActivity;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 1;
    String TAG = "msg";
    private TextView googleUser, firebaseUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing);
        // Views
        googleUser = findViewById(R.id.googleUser);
        firebaseUID = findViewById(R.id.firebaseUID);
        // Button listeners
        findViewById(R.id.signIn).setOnClickListener(this);
        findViewById(R.id.signOut).setOnClickListener(this);
        findViewById(R.id.disconnect).setOnClickListener(this);
        // Configure Google Sign In
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signInButton)         {signIn();      }
        else if (i == R.id.signOutButton)   {signOut();     }
        else { if (i == R.id.disconnectButton) {revokeAccess();}
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }
    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();
        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        updateUI(null);       }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            googleUser.setText(getString(R.string.google_user, user.getEmail()));
            firebaseUID.setText(getString(R.string.firebase_uid, user.getUid()));
            googleUser.setVisibility(View.VISIBLE);
            firebaseUID.setVisibility(View.VISIBLE);
            MainActivity.myUser = getString(R.string.google_user, user.getEmail());
            findViewById(R.id.signIn).setVisibility(View.GONE);    findViewById(R.id.signOut).setVisibility(View.VISIBLE);
            findViewById(R.id.disconnect).setVisibility(View.VISIBLE);
        } else {
            googleUser.setText(R.string.signed_out);
            MainActivity.myUser = getString(R.string.sign_out);
            firebaseUID.setText(null);
            findViewById(R.id.signIn).setVisibility(View.VISIBLE);     findViewById(R.id.signOut).setVisibility(View.GONE);
            findViewById(R.id.disconnect).setVisibility(View.GONE);
        }
    }

}
