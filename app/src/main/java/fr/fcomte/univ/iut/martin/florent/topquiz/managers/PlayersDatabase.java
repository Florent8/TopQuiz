package fr.fcomte.univ.iut.martin.florent.topquiz.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import fr.fcomte.univ.iut.martin.florent.topquiz.models.Player;

import static java.util.Collections.sort;

public final class PlayersDatabase extends Database {

    private static final String        TABLE_PLAYERS = "players";
    private static final String        KEY_NAME      = "name";
    private static final String        KEY_SCORE     = "score";
    private static final byte          NB_SCORES     = 5;
    private final        ContentValues values        = new ContentValues();

    public PlayersDatabase(final Context context) {
        super(context);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PLAYERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT NOT NULL,"
                + KEY_SCORE + " INTEGER NOT NULL)");
    }

    public void setScores(final Player player) {
        final List<Player> players = getPlayers();
        players.add(player);
        sort(players);

        while (players.size() != NB_SCORES)
            players.remove(players.size() - 1);

        final SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PLAYERS, null, null);
        values.clear();
        values.put("seq", 0);
        db.update("SQLITE_SEQUENCE", values, "name = ?", new String[]{TABLE_PLAYERS});

        for (Player p : players) {
            values.clear();
            values.put(KEY_NAME, p.getName());
            values.put(KEY_SCORE, p.getScore());
            db.insert(TABLE_PLAYERS, null, values);
        }

        db.close();
    }

    private List<Player> getPlayers() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLAYERS,
                new String[]{KEY_ID, KEY_NAME, KEY_SCORE},
                null, null, null, null, KEY_SCORE);
        final List<Player> players = new ArrayList<>();
        if (cursor.moveToFirst())
            do {
                players.add(new Player(
                        (byte) cursor.getShort(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        (byte) cursor.getShort(cursor.getColumnIndex(KEY_SCORE))
                ));
            } while (cursor.moveToNext());
        cursor.close();
        db.close();
        return players;
    }
}
