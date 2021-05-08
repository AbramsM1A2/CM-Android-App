package com.example.myapplication.bottomMenu.settingsTab;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class ContactUsActivity extends AppCompatActivity {

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
                SharedPreferences preferencias = getSharedPreferences("PREFERENCIAS", MODE_PRIVATE);
                SharedPreferences.Editor edit = preferencias.edit();
                edit.putBoolean("activar_home", Boolean.TRUE);
                edit.apply();
                startActivity(i);
            }
        });
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.contact_us_image)
                .setDescription(getText(R.string.contactus_description))
                .addGroup(getString(R.string.connect))
                .addEmail("rdelmar00@gmail.com")
                .addFacebook("Google España")
                .addTwitter("GoogleES")
                .addYoutube("Android Developers")
                .addPlayStore("Goat Simulator")
                .addInstagram("android")
                .addGitHub("CM-Android-App")
                .addItem(volver)
                .create();
        setContentView(aboutPage);
    }

}
