package com.example.myapplication.bottomMenu.settingsTab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.example.myapplication.R;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String PREF_FILE_NAME = "PREFERENCIAS";
    private SwitchPreferenceCompat themePref;
    private DropDownPreference languagePref;

    private Preference aboutusButton;
    private Preference contactButton;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        PreferenceManager manager = getPreferenceManager();
        manager.setSharedPreferencesName(PREF_FILE_NAME);
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        languagePref = findPreference("language");
        Locale current = getResources().getConfiguration().locale;
        if (current.getLanguage().contains("es")) {
            languagePref.setValue("spanish");
        } else if (current.getLanguage().contains("en")) {
            languagePref.setValue("english");
        } else if (current.getLanguage().contains("fr")) {
            languagePref.setValue("french");
        } else if (current.getLanguage().contains("it")) {
            languagePref.setValue("italian");
        }

        aboutusButton = findPreference("about_us");
        aboutusButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(i);
                return true;
            }
        });
        contactButton = findPreference("contact");
        contactButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(i);
                return true;
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("theme")) {
            themePref = findPreference(key);
            if (themePref.isChecked()) {
                int nightModeFlags = getActivity().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        }

        if (key.equals("language")) {
            languagePref = findPreference(key);
            String language = languagePref.getValue();
            Locale current = getResources().getConfiguration().locale;
            if (current.getLanguage().contains("es") && language != "spanish") {
                ShowchangeLang();
            } else if (current.getLanguage().contains("en") && language != "english") {
                ShowchangeLang();
            } else if (current.getLanguage().contains("fr") && language != "french") {
                ShowchangeLang();
            } else if (current.getLanguage().contains("it") && language != "italian") {
                ShowchangeLang();
            }
        }
    }

    public void ShowchangeLang() {
        Boolean clickeado = Boolean.FALSE;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.change_language);
        alertDialog.setMessage(R.string.change_language2);
        alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                Boolean clickeado = Boolean.TRUE;
            }
        });
        alertDialog.show();
        if (!clickeado) {
            setLanguagePref();
        }
    }

    public void setLanguagePref() {
        languagePref = findPreference("language");
        Locale current = getResources().getConfiguration().locale;
        if (current.getLanguage().contains("es")) {
            languagePref.setValue("spanish");
        } else if (current.getLanguage().contains("en")) {
            languagePref.setValue("english");
        } else if (current.getLanguage().contains("fr")) {
            languagePref.setValue("french");
        } else if (current.getLanguage().contains("it")) {
            languagePref.setValue("italian");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }
}