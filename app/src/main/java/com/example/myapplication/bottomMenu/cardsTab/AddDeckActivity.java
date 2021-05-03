package com.example.myapplication.bottomMenu.cardsTab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.database.Card.CardViewModel;
import com.example.myapplication.database.Deck.Deck;
import com.example.myapplication.database.Deck.DeckViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddDeckActivity extends AppCompatActivity {
    private DeckViewModel mDeckViewModel;
    private String mMsg_snack_add_deck;
    private Button add_deck_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deck);
        getSupportActionBar().setTitle(getApplicationContext().getString(R.string.fab_add_deck));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Msg to the user when he adds a deck to the database
        mMsg_snack_add_deck = getApplicationContext().getString(R.string.msg_snack_add_deck);
        //Viewmodel de BD
        mDeckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        //Botones y UI
        add_deck_button = (Button) findViewById(R.id.buttonAddDeck);
        EditText mEditTextDeckName = (EditText) findViewById(R.id.editTextDeckName);
        add_deck_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String deckName = mEditTextDeckName.getText().toString();
                //Para que no se añadan nombres en blanco

                if (!deckName.isEmpty()) {

                    //Msgs to user in alert dialog
                    String msg_to_user = getApplicationContext().getString(R.string.msg_add_deck) + " " + deckName;
                    String title_to_user = getApplicationContext().getString(R.string.add_deck) + "?";

                    new AlertDialog.Builder(v.getContext())
                            .setTitle(title_to_user)
                            .setMessage(msg_to_user)
                            .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Deck deck = new Deck(deckName, new Date());
                                    mDeckViewModel.insert(deck);
                                    //Si no funciona la view v poner mejor findViewById(android.R.id.content)
                                    Snackbar.make(v, mMsg_snack_add_deck, Snackbar.LENGTH_LONG).show();
                                    //Para vaciar el campo despues de añadir el mazo
                                    mEditTextDeckName.getText().clear();
                                }
                            })
                            .setNegativeButton(R.string.btn_cancel, null)
                            .show();

                } else {
                    //En el caso de que se deje en blanco el nombre del mazo
                    Toast.makeText(getApplicationContext(), R.string.msg_toast_empty, Toast.LENGTH_LONG).show();
                }
            }
        });
        setTheme();
    }

    public boolean onSupportNavigateUp() {
        //Metodo para el boton de atras del actionbar
        finish();
        return true;
    }

    private void setTheme() {

        int nightModeFlags = this.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            //claro
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cp_light_blue_700)));
            add_deck_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_blue_700)));


        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            //oscuro
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cp_light_blue_200)));
            add_deck_button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_blue_200)));


        }
    }
}