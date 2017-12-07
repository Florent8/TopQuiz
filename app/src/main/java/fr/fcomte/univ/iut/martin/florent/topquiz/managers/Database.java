package fr.fcomte.univ.iut.martin.florent.topquiz.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;

import fr.fcomte.univ.iut.martin.florent.topquiz.models.Question;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.PackagePrivate;

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
import static lombok.AccessLevel.PRIVATE;

/**
 * Gère la base de données de l'application
 */
@FieldDefaults(level = PRIVATE, makeFinal = true)
abstract class Database extends SQLiteOpenHelper {

    @PackagePrivate static String        KEY_ID              = "id";
    static                 String        DATABASE_NAME       = "top_quiz_db";
    static                 byte          DATABASE_VERSION    = 1;
    static                 String        QUESTIONS_JSON_FILE = "questions.json";
    @PackagePrivate        ContentValues values              = new ContentValues();
    Context context;

    /**
     * Constructeur
     *
     * @param context activité où est instanciée la classe
     */
    Database(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * Création des tables
     * <ul>
     * <li>{@link QuestionBank#TABLE_QUESTIONS}</li>
     * <li>initialise les différentes questions stockées au format json
     * dans le fichier {@value QUESTIONS_JSON_FILE}</li>
     * <li>{@link PlayersDatabase#TABLE_PLAYERS}</li>
     * </ul>
     *
     * @param db database
     */
    @SneakyThrows(IOException.class)
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

        final Question[] questions = new Gson().fromJson(
                new InputStreamReader(context.getAssets().open(QUESTIONS_JSON_FILE)),
                Question[].class
        );

        for (final Question question : questions) {
            values.clear();
            values.put(KEY_QUESTION, question.question());
            values.put(KEY_ANSWER1, question.answer1());
            values.put(KEY_ANSWER2, question.answer2());
            values.put(KEY_ANSWER3, question.answer3());
            values.put(KEY_ANSWER4, question.answer4());
            values.put(KEY_GOOD_ANSWER, question.goodAnswer());
            db.insert(TABLE_QUESTIONS, null, values);
        }

        db.execSQL("CREATE TABLE " + TABLE_PLAYERS + "("
                   + KEY_NAME + " TEXT PRIMARY KEY,"
                   + KEY_SCORE + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }
}
