package self.joanciscar.myapplication.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import self.joanciscar.myapplication.R;
import self.joanciscar.myapplication.ui.LogInFirebase;

public class SettingsFragment extends PreferenceFragmentCompat {
    private FirebaseAuth mAuth;
    private static final int SIGN_IN_CODE = 1;
    private static final String TAG ="msg";

    // For the log in you need to generate in the web a fingerprint. The gradle task android/signinReport will generate your fingerprint
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        EditTextPreference signature = findPreference("signature");
        ListPreference reply = findPreference("reply");
        SwitchPreferenceCompat authenticated = findPreference("authenticated");
        SwitchPreferenceCompat sync = findPreference("sync");
        SwitchPreferenceCompat sync_pictures = findPreference("sync_pictures");
        /*GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this.getContext(), gso);*/
        mAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        authenticated.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SwitchPreferenceCompat p = (SwitchPreferenceCompat) preference;
                if(p.isChecked()) {
                    Intent i = new Intent();
                    i.putExtra(LogInFirebase.ORDER_TAG,true);
                    i.setClass(SettingsFragment.this.getContext(),LogInFirebase.class);
                    startActivityForResult(i,2);
                } else {
                    Intent i = new Intent();
                    i.putExtra(LogInFirebase.ORDER_TAG,false);
                    i.setClass(SettingsFragment.this.getContext(),LogInFirebase.class);
                    startActivityForResult(i,2);
                }
                return true;
            }
        });
        sync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SwitchPreferenceCompat p = (SwitchPreferenceCompat) preference;
                if(p.isChecked()) {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("email",mAuth.getCurrentUser().getEmail());
                    map.put("displayName",mAuth.getCurrentUser().getDisplayName());
                    FirebaseDatabase.getInstance().getReference(mAuth.getUid()).setValue(map);
                } else {
                    FirebaseDatabase.getInstance().getReference(mAuth.getUid()).removeValue();
                }
                return true;
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
       /* if (requestCode == SIGN_IN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NotNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    ((CurrentUserViewModel) new ViewModelProvider(SettingsFragment.this.getActivity()).get(CurrentUserViewModel.class)).setUser(user);
                                } else {
                                    authenticated.setChecked(false);
                                }
                            }
                        });
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                authenticated.setChecked(false);
            }
        } */
        if(requestCode == 2) {
            CurrentUserViewModel vm = ((CurrentUserViewModel) new ViewModelProvider(SettingsFragment.this.getActivity()).get(CurrentUserViewModel.class));
            if(resultCode == LogInFirebase.RESULT_OK) {
                vm.setUser(FirebaseAuth.getInstance().getCurrentUser());
            } else {
                vm.setUser(null);
            }
        }
    }

}