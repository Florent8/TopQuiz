package fr.fcomte.univ.iut.martin.florent.topquiz.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;

import java.util.List;

import fr.fcomte.univ.iut.martin.florent.topquiz.managers.PlayersDatabase;
import fr.fcomte.univ.iut.martin.florent.topquiz.models.Player;
import fr.fcomte.univ.iut.martin.florent.topquiz.models.Player.PlayerNameComparator;
import lombok.experimental.FieldDefaults;

import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.menu_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.sort_name_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.sort_score_btn;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.id.table_layout;
import static fr.fcomte.univ.iut.martin.florent.topquiz.R.layout.activity_score;
import static java.util.Collections.sort;
import static lombok.AccessLevel.PRIVATE;

/**
 * Activité d'affichage des scores
 */
@FieldDefaults(level = PRIVATE)
public final class ScoreActivity extends AppCompatActivity {

    List<Player>    players;
    ScoreTableRow[] tableRow;
    TableLayout     tableLayout;

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
        findViewById(sort_name_btn).setOnClickListener(view -> sortPlayersByName());
        findViewById(sort_score_btn).setOnClickListener(view -> sortPlayersByScore());

        tableLayout = findViewById(table_layout);

        players = new PlayersDatabase(this).getPlayers();
        sortPlayersByScore();
    }

    /**
     * Trie par score {@link ScoreActivity#players}
     */
    private void sortPlayersByScore() {
        sort(players);
        setPlayersToTableLayout();
    }

    /**
     * Trie par noms {@link ScoreActivity#players}
     */
    private void sortPlayersByName() {
        sort(players, new PlayerNameComparator());
        setPlayersToTableLayout();
    }

    /**
     * Vide le TableLayout et y ajoute les joueurs
     */
    private void setPlayersToTableLayout() {
        if (tableRow != null)
            for (final ScoreTableRow row : tableRow)
                tableLayout.removeView(row);

        tableRow = new ScoreTableRow[players.size()];
        for (int i = 0; i < tableRow.length; i++) {
            final Player p = players.get(i);
            tableRow[i] = new ScoreTableRow(this, p.name(), p.score());
            tableLayout.addView(tableRow[i]);
        }
    }
}
