package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.menu_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.layout.activity_score;

/**
 * Activité d'affichage des scores
 */
public final class ScoreActivity extends AppCompatActivity {

    /**
     * Initialisation des attributs de l'instance de {@link ScoreActivity}
     *
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_score);

        findViewById(menu_btn).setOnClickListener(view -> finish());
    }
}
