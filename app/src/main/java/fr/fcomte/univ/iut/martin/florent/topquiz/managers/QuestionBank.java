package fr.fcomte.univ.iut.martin.florent.topquiz.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import fr.fcomte.univ.iut.martin.florent.topquiz.models.Question;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.experimental.PackagePrivate;

import static lombok.AccessLevel.PRIVATE;

/**
 * Gestion de la table {@value TABLE_QUESTIONS} dans la base de données <br/>
 * Hérite de {@link Database}
 */
@Accessors(fluent = true)
@FieldDefaults(makeFinal = true, level = PRIVATE)
public final class QuestionBank extends Database {

    @PackagePrivate static String TABLE_QUESTIONS     = "questions";
    @PackagePrivate static String KEY_QUESTION        = "question";
    @PackagePrivate static String KEY_ANSWER1         = "answer1";
    @PackagePrivate static String KEY_ANSWER2         = "answer2";
    @PackagePrivate static String KEY_ANSWER3         = "answer3";
    @PackagePrivate static String KEY_ANSWER4         = "answer4";
    @PackagePrivate static String KEY_GOOD_ANSWER     = "good_answer";
    static                 String QUESTIONS_JSON_FILE = "questions.json";
    static                 String RANDOM_ORDER_BY     = "random()";
    Context context;
    String[] columns = new String[]{KEY_ID,
                                    KEY_QUESTION,
                                    KEY_ANSWER1,
                                    KEY_ANSWER2,
                                    KEY_ANSWER3,
                                    KEY_ANSWER4,
                                    KEY_GOOD_ANSWER};
    @NonFinal @Getter @Setter ArrayList<String> idsList   = new ArrayList<>();
    @NonFinal @Getter @Setter StringBuilder     idsString = new StringBuilder();

    /**
     * Constructeur
     *
     * @param context {@link Context}
     * @see Database#Database(Context)
     */
    public QuestionBank(final Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Après la création des tables, initialise les différentes questions stockées au format json
     * dans le fichier {@value QUESTIONS_JSON_FILE}
     *
     * @param db {@link SQLiteDatabase}
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {
        super.onCreate(db);

        Question[] questions;
        try {
            questions = new Gson().fromJson(
                    new InputStreamReader(context.getAssets().open(QUESTIONS_JSON_FILE)),
                    Question[].class
            );
        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        for (final Question question : questions) {
            values.clear();
            values.put(KEY_QUESTION, question.getQuestion());
            values.put(KEY_ANSWER1, question.getAnswer1());
            values.put(KEY_ANSWER2, question.getAnswer2());
            values.put(KEY_ANSWER3, question.getAnswer3());
            values.put(KEY_ANSWER4, question.getAnswer4());
            values.put(KEY_GOOD_ANSWER, question.getGoodAnswer());
            db.insert(TABLE_QUESTIONS, null, values);
        }
    }

    /**
     * Retourne une question sélectionnée au hasard dans la base et qui n'est pas déjà sortie
     *
     * @return {@link Question}
     */
    public Question getQuestion() {
        final SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = db.query(TABLE_QUESTIONS,
                                       columns,
                                       idsString.toString(),
                                       idsList.toArray(new String[0]),
                                       null, null, RANDOM_ORDER_BY
        );
        cursor.moveToFirst();
        final Question question = new Question(
                cursor.getString(cursor.getColumnIndex(KEY_QUESTION)),
                cursor.getString(cursor.getColumnIndex(KEY_ANSWER1)),
                cursor.getString(cursor.getColumnIndex(KEY_ANSWER2)),
                cursor.getString(cursor.getColumnIndex(KEY_ANSWER3)),
                cursor.getString(cursor.getColumnIndex(KEY_ANSWER4)),
                (byte) cursor.getShort(cursor.getColumnIndex(KEY_GOOD_ANSWER))
        );

        if (idsString.length() != 0)
            idsString.append(" AND ");
        idsString.append(KEY_ID + "!= ? ");
        idsList.add(cursor.getString(cursor.getColumnIndex(KEY_ID)));
        cursor.close();
        db.close();
        return question;
    }
}
