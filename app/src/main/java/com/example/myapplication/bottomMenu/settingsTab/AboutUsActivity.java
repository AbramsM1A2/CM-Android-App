package com.example.myapplication.bottomMenu.settingsTab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.BuildConfig;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element versionElement = new Element();
        versionElement.setTitle(getString(R.string.versiontitle));

        Element volver = new Element();
        volver.setTitle(getString(R.string.backHome));
        volver.setIconDrawable(R.drawable.baseline_home_24);
        volver.setIconTint(R.color.purple_500);
        volver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                SharedPreferences preferencias = getSharedPreferences("PREFERENCIAS",MODE_PRIVATE);
                SharedPreferences.Editor edit = preferencias.edit();
                edit.putBoolean("activar_home",Boolean.TRUE);
                edit.apply();
                startActivity(i);
            }
        });

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.itc_launcher_flashcards)
                .setDescription(getText(R.string.aboutus_description))
                .addGroup(getString(R.string.find_us))
                .addGitHub("PRUEBA")
                .addPlayStore("PRUEBA")
                .addItem(versionElement)
                .addItem(new Element(getText(R.string.version)+" " + BuildConfig.VERSION_NAME, R.drawable.baseline_aod_24))
                .addItem(volver)
                .create();
        setContentView(aboutPage);
    }
}

