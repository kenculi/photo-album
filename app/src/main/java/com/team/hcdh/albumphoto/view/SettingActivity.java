package com.team.hcdh.albumphoto.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.team.hcdh.albumphoto.R;

import java.util.ArrayList;


public class SettingActivity extends AppCompatActivity {
    ContextThemeWrapper contextThemeWrapper;

    static void restartApp(Activity activity) {
        Intent newIntent = new Intent(activity, MainActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(newIntent);
        activity.overridePendingTransition(0, 0);
        activity.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        contextThemeWrapper = new ContextThemeWrapper(getBaseContext(), this.getTheme());
        Preferences.applyTheme(contextThemeWrapper, getBaseContext());

        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(0, 0);
            }
        });
        if (getFragmentManager().findFragmentById(R.id.content_frame) == null) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
        }

    }

    public static class SettingsFragment extends PreferenceFragment {

        private SharedPreferences.OnSharedPreferenceChangeListener mListenerOptions;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.prefers);

            /*PreferenceScreen preferenceScreen = getPreferenceScreen();

            Preference hiddenFolders = findPreference("hiddenFolders");

            SQLiteDatabase hiddenFoldersDB = getActivity().openOrCreateDatabase("HIDDEN", MODE_PRIVATE, null);

            final ArrayList<String> mHiddenFolders = new ArrayList<>();

            Cursor cursor = hiddenFoldersDB.rawQuery("SELECT * FROM foldersList;", null);

            if (cursor != null && cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {

                    mHiddenFolders.add(cursor.getString(cursor.getColumnIndex("folder")));
                    cursor.moveToNext();
                }
                cursor.close();
            }


            if (mHiddenFolders.size() != 0) {

                preferenceScreen.addPreference(hiddenFolders);

                hiddenFolders.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {

                        HiddenFoldersDialog.show(getActivity(), mHiddenFolders);

                        return false;
                    }
                });
            } else {
                preferenceScreen.removePreference(hiddenFolders);
            }*/


            mListenerOptions = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences preftheme, String key) {

                    if (key.equals(getResources().getString(R.string.pref_theme)) | key.equals(getResources().getString(R.string.pref_grid))) {

                        restartApp(getActivity());
                    }
                }
            };



        }

        @Override
        public void onResume() {
            super.onResume();

            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListenerOptions);
        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListenerOptions);
            super.onPause();
        }
    }
}
