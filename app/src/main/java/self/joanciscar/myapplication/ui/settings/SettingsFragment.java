package self.joanciscar.myapplication.ui.settings;

import android.os.Bundle;
import android.view.View;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import self.joanciscar.myapplication.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    private FirebaseAuth mAuth;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        EditTextPreference signature = findPreference("signature");
        ListPreference reply = findPreference("reply");
        SwitchPreferenceCompat authenticated = findPreference("authenticated");
        authenticated.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mAuth.getCurrentUser();
                return false;
            }
        });
        SwitchPreferenceCompat sync = findPreference("sync");
        SwitchPreferenceCompat sync_pictures = findPreference("sync_pictures");
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

    }

    @Override
    public void onStart() {
        super.onStart();

    }
}