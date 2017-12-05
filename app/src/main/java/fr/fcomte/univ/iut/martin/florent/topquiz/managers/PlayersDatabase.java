package fr.fcomte.univ.iut.martin.florent.topquiz.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.fcomte.univ.iut.martin.florent.topquiz.models.Player;
import lombok.experimental.ExtensionMethod;
import lombok.experimental.FieldDefaults;
import lombok.experimental.PackagePrivate;

import static lombok.AccessLevel.PRIVATE;

/**
 * Gestion de la table {@value TABLE_PLAYERS} dans la base de données
 */
@FieldDefaults(level = PRIVATE, makeFinal = true)
@ExtensionMethod(Collections.class)
public final class PlayersDatabase extends Database {

    @PackagePrivate static String TABLE_PLAYERS = "players";
    @PackagePrivate static String KEY_NAME      = "name";
    @PackagePrivate static String KEY_SCORE     = "score";
    static                 byte   NB_SCORES     = 5;
    List<Player> players = new ArrayList<>();

    /**
     * Constructeur <br/>
     *
     * @param context activité où est instanciée la classe
     * @see Database#Database(Context)
     */
    public PlayersDatabase(final Context context) {
        super(context);
    }

    /**
     * Modifie les scores en base de données en faisant en sorte qu'il n'y ait :
     * <ul>
     * <li>que {@value NB_SCORES} en base de données ;</li>
     * <li>qu'un pseudonyme par joueur enregistré (si un pseudonyme est utilisé plusieurs fois, le score sera mis à jour).</li>
     * </ul>
     *
     * @param player joueur courant à ajouter dans la base de données si son score est supérieur à ceux déjà présents
     */
    public void setScores(final Player player) {
        final List<Player> players = getPlayers();
        players.add(player);
        players.sort();
        while (players.size() > NB_SCORES)
            players.remove(players.size() - 1);
        final SQLiteDatabase db = getWritableDatabase();
        for (final Player p : players)
            db.execSQL(
                    "INSERT OR REPLACE INTO " + TABLE_PLAYERS + "(" + KEY_NAME + "," + KEY_SCORE +
                    ")VALUES (:name, :score)",
                    new Object[]{":name = " + p.name(), ":score=" + p.score()}
            );
        db.close();
    }

    /**
     * @return liste des joueurs présents en base de données
     */
    private List<Player> getPlayers() {
        final SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = db.query(TABLE_PLAYERS,
                                       new String[]{KEY_NAME, KEY_SCORE},
                                       null, null, null, null, KEY_SCORE
        );
        players.clear();
        if (cursor.moveToFirst())
            do {
                players.add(new Player(
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        (byte) cursor.getShort(cursor.getColumnIndex(KEY_SCORE))
                ));
            } while (cursor.moveToNext());
        cursor.close();
        db.close();
        return players;
    }
}
