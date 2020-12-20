package self.joanciscar.myapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

import self.joanciscar.myapplication.R;

public class LogInFirebase extends AppCompatActivity {
    private static final int SIGN_IN_CODE = 1;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    private static final String TAG ="msg";
    public static final String ORDER_TAG = "ORDER"; // True log on  or false revoke
    public static final String USER_TAG = "user";
    public static final String ACCOUND_TAG = "user";
    public static final int RESULT_REVOKED = -2;
    public static final int RESULT_ERROR = 2;
    public Bundle returningExtras;
    private GoogleSignInAccount account;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_log_in_firebase);
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mAuth = FirebaseAuth.getInstance();
        googleSignInClient = GoogleSignIn.getClient(this,gso);
        Bundle b = this.getIntent().getExtras();
        returningExtras = new Bundle();
        if(b.getBoolean(ORDER_TAG)) {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, SIGN_IN_CODE);
        } else {
            revokeAccess();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SIGN_IN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(LogInFirebase.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NotNull Task<AuthResult> task) {
                                Intent i = new Intent();
                                if (task.isSuccessful()) {
                                    returningExtras.putParcelable("account",account);
                                    returningExtras.putParcelable("user",mAuth.getCurrentUser());
                                    i.putExtras(returningExtras);
                                    setResult(RESULT_OK,i);
                                } else {
                                    returningExtras.putSerializable("user",null);
                                    returningExtras.putSerializable("account",null);
                                    i.putExtras(returningExtras);
                                    setResult(RESULT_ERROR,i);
                                }
                                finish();
                            }
                        });
            } catch (ApiException e) {
                returningExtras.putSerializable("user",null);
                returningExtras.putSerializable("account",null);
                setResult(RESULT_ERROR,new Intent());
                Intent i = new Intent();
                i.putExtras(returningExtras);
                finish();
            }
        }
    }
    private void revokeAccess() {
        mAuth.signOut();
        googleSignInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                returningExtras.putSerializable("user",null);
                returningExtras.putSerializable("account",null);
                setResult(RESULT_REVOKED,new Intent());
                finish();
            }
        });
    }
}