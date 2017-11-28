package fr.fcomte.univ.iut.martin.florent.topquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static fr.fcomte.univ.iut.martin.florent.topquiz.R.layout.activity_game;

public final class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_game);
    }
}
