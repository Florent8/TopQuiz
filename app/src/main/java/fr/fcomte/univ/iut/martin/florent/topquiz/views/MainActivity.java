package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import fr.fcomte.univ.iut.martin.florent.topquiz.managers.PlayersDatabase;
import fr.fcomte.univ.iut.martin.florent.topquiz.models.Player;

import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.name_input;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.start_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.layout.activity_main;
import static fr.fcomte.univ.iut.martin.florent.topquiz.views.GameActivity.BUNDLE_EXTRA_SCORE;

/**
 * Activité principale lancée au chargement de l'application <br/>
 * Hérite de {@link AppCompatActivity}
 */
public final class MainActivity extends AppCompatActivity {

    private static final byte   GAME_ACTIVITY_REQUEST_CODE = 1;
    private static final String PLAYER_PREFERENCE          = "player";
    private static final String SCORE_PREFERENCE           = "score";
    private SharedPreferences preferences;
    private Player            player;
    private PlayersDatabase   playersDatabase;

    /**
     * Initialisation des attributs de l'instance de {@link MainActivity} <br/>
     * Gestion de la récupération du nom du joueur <br/>
     * Bouton de lancement d'une instance de {@link GameActivity}
     *
     * @param savedInstanceState {@link Bundle}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        playersDatabase = new PlayersDatabase(this);
        preferences = getPreferences(MODE_PRIVATE);

        player = new Player(preferences.getString(PLAYER_PREFERENCE, ""));

        final Button button = findViewById(start_btn);
        final EditText nameInput = findViewById(name_input);

        button.setEnabled(false);
        button.setOnClickListener(view -> {
            final String name = nameInput.getText().toString();
            if (!player.getName().equals(name))
                player = new Player(name);
            startActivityForResult(new Intent(this, GameActivity.class),
                                   GAME_ACTIVITY_REQUEST_CODE
            );
        });

        nameInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(final CharSequence s, final int i, final int i1,
                                          final int i2
            ) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int i, final int i1, final int i2
            ) {
                button.setEnabled(s.length() != 0);
            }

            @Override
            public void afterTextChanged(final Editable editable) {
            }
        });
    }

    /**
     * Récupère le score du joueur à la fin de {@link GameActivity}
     *
     * @param requestCode code de la requête
     * @param resultCode  {@value GAME_ACTIVITY_REQUEST_CODE}
     * @param data        {@link Intent}
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAME_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
            player.setScore(data.getByteExtra(BUNDLE_EXTRA_SCORE, (byte) 0));
        playersDatabase.setScores(player);
        preferences.edit()
                   .putString(PLAYER_PREFERENCE, player.getName())
                   .putInt(SCORE_PREFERENCE, player.getScore())
                   .apply();
    }
}
