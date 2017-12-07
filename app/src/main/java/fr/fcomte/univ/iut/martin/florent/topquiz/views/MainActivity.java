package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.fcomte.univ.iut.martin.florent.topquiz.managers.PlayersDatabase;
import fr.fcomte.univ.iut.martin.florent.topquiz.models.Player;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.name_input;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.name_text_view;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.scores_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.start_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.layout.activity_main;
import static fr.fcomte.univ.iut.martin.florent.topquiz.views.GameActivity.BUNDLE_EXTRA_SCORE;
import static lombok.AccessLevel.PRIVATE;

/**
 * Activité principale lancée au chargement de l'application
 */
@FieldDefaults(level = PRIVATE, makeFinal = true)
public final class MainActivity extends AppCompatActivity {

    static byte   GAME_ACTIVITY_REQUEST_CODE = 1;
    static String PLAYER_PREFERENCE          = "player";
    static String SCORE_PREFERENCE           = "score";
    StringBuilder stringBuilder = new StringBuilder();
    @NonFinal SharedPreferences preferences;
    @NonFinal Player            player;
    @NonFinal PlayersDatabase   playersDatabase;
    @NonFinal EditText          nameInput;
    @NonFinal Button            scoreButton;

    /**
     * Initialisation des attributs de l'instance de {@link MainActivity} <br/>
     * Gestion de la récupération du nom du joueur <br/>
     * Bouton de lancement d'une instance de {@link GameActivity} <br/>
     * Bouton de lancement d'une instance de {@link ScoreActivity}
     *
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        playersDatabase = new PlayersDatabase(this);
        preferences = getPreferences(MODE_PRIVATE);

        player = new Player(preferences.getString(PLAYER_PREFERENCE, ""),
                            (byte) preferences.getInt(SCORE_PREFERENCE, 0)
        );

        final Button button = findViewById(start_btn);
        nameInput = findViewById(name_input);

        button.setEnabled(false);
        button.setOnClickListener(view -> {
            final String name = nameInput.getText().toString();
            if (!player.name().equals(name))
                player = new Player(name);
            startActivityForResult(new Intent(this, GameActivity.class),
                                   GAME_ACTIVITY_REQUEST_CODE
            );
        });

        nameInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(final CharSequence s, final int i, final int i1, final int i2) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int i, final int i1, final int i2) {
                button.setEnabled(s.length() != 0);
            }

            @Override
            public void afterTextChanged(final Editable editable) {
            }
        });

        scoreButton = findViewById(scores_btn);
        scoreButton
                .setOnClickListener(view -> startActivity(new Intent(this, ScoreActivity.class)));
        scoreButton.setEnabled(false);

        setPlayerMessage();
    }

    /**
     * Récupère le score du joueur à la fin de {@link GameActivity}
     *
     * @param requestCode code de la requête
     * @param resultCode  {@value GAME_ACTIVITY_REQUEST_CODE}
     * @param data        intent
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAME_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
            player.score(data.getByteExtra(BUNDLE_EXTRA_SCORE, (byte) 0));
        playersDatabase.setScores(player);
        preferences.edit()
                   .putString(PLAYER_PREFERENCE, player.name())
                   .putInt(SCORE_PREFERENCE, player.score())
                   .apply();
        setPlayerMessage();
    }

    /**
     * Mise à jour du message d'accueil
     */
    private void setPlayerMessage() {
        stringBuilder.setLength(0);
        if (!player.name().equals("")) {
            scoreButton.setEnabled(true);
            ((TextView) findViewById(name_text_view)).setText(
                    stringBuilder.append("Bonjour ").append(player.name())
                                 .append(" ! Ravi de vous revoir.\nVotre précédent score était de ")
                                 .append(player.score()).append(".").toString());
            nameInput.setText(player.name());
            nameInput.setSelection(player.name().length());
        }
    }
}
