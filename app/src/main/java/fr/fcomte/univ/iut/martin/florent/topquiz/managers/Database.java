package fr.fcomte.univ.iut.martin.florent.topquiz.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static fr.fcomte.univ.iut.martin.florent.topquiz.managers.PlayersDatabase.KEY_NAME;
import static fr.fcomte.univ.iut.martin.florent.topquiz.managers.PlayersDatabase.KEY_SCORE;
import static fr.fcomte.univ.iut.martin.florent.topquiz.managers.PlayersDatabase.TABLE_PLAYERS;
import static fr.fcomte.univ.iut.martin.florent.topquiz.managers.QuestionBank.KEY_ANSWER1;
import static fr.fcomte.univ.iut.martin.florent.topquiz.managers.QuestionBank.KEY_ANSWER2;
import static fr.fcomte.univ.iut.martin.florent.topquiz.managers.QuestionBank.KEY_ANSWER3;
import static fr.fcomte.univ.iut.martin.florent.topquiz.managers.QuestionBank.KEY_ANSWER4;
import static fr.fcomte.univ.iut.martin.florent.topquiz.managers.QuestionBank.KEY_GOOD_ANSWER;
import static fr.fcomte.univ.iut.martin.florent.topquiz.managers.QuestionBank.KEY_QUESTION;
import static fr.fcomte.univ.iut.martin.florent.topquiz.managers.QuestionBank.TABLE_QUESTIONS;

/**
 * Gère la base de données de l'application <br/>
 * Hérite de {@link SQLiteOpenHelper}
 */
abstract class Database extends SQLiteOpenHelper {

    static final         String        KEY_ID           = "id";
    private static final String        DATABASE_NAME    = "top_quiz_db";
    private static final byte          DATABASE_VERSION = 1;
    final                ContentValues values           = new ContentValues();

    /**
     * Constructeur
     *
     * @param context {@link Context}
     */
    Database(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Création des tables
     * <ul>
     * <li>{@link QuestionBank#TABLE_QUESTIONS}</li>
     * <li>{@link PlayersDatabase#TABLE_PLAYERS}</li>
     * </ul>
     *
     * @param db {@link SQLiteDatabase}
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_QUESTIONS + "("
                   + KEY_ID + " INTEGER PRIMARY KEY,"
                   + KEY_QUESTION + " TEXT NOT NULL,"
                   + KEY_ANSWER1 + " TEXT NOT NULL,"
                   + KEY_ANSWER2 + " TEXT NOT NULL,"
                   + KEY_ANSWER3 + " TEXT NOT NULL,"
                   + KEY_ANSWER4 + " TEXT NOT NULL,"
                   + KEY_GOOD_ANSWER + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_PLAYERS + "("
                   + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + KEY_NAME + " TEXT NOT NULL,"
                   + KEY_SCORE + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }
}
