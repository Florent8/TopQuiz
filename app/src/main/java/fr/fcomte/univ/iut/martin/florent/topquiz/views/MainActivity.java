package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.content.Intent;
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

public final class MainActivity extends AppCompatActivity {

    private static final byte GAME_ACTIVITY_REQUEST_CODE = 1;
    private Player          player;
    private PlayersDatabase playersDatabase;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        playersDatabase = new PlayersDatabase(this);

        final Button button = findViewById(start_btn);
        final EditText nameInput = findViewById(name_input);

        button.setEnabled(false);
        button.setOnClickListener(view -> {
            player = new Player(nameInput.getText().toString(), (byte) 0);
            startActivityForResult(new Intent(this, GameActivity.class), GAME_ACTIVITY_REQUEST_CODE);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAME_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
            player.setScore(data.getByteExtra(BUNDLE_EXTRA_SCORE, (byte) 0));
        playersDatabase.setScores(player);
    }
}
