package fr.fcomte.univ.iut.martin.florent.topquiz.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.fcomte.univ.iut.martin.florent.topquiz.models.Question;

public final class QuestionBank extends Database {

    private static final String TABLE_QUESTIONS = "questions";
    private static final String KEY_QUESTION    = "question";
    private static final String KEY_ANSWER1     = "answer1";
    private static final String KEY_ANSWER2     = "answer2";
    private static final String KEY_ANSWER3     = "answer3";
    private static final String KEY_ANSWER4     = "answer4";
    private static final String KEY_GOOD_ANSWER = "good_answer";
    private final Context context;
    private final String[]      columns   = new String[]{KEY_ID, KEY_QUESTION, KEY_ANSWER1, KEY_ANSWER2, KEY_ANSWER3, KEY_ANSWER4, KEY_GOOD_ANSWER};
    private final List<String>  idsList   = new ArrayList<>();
    private final StringBuilder idsString = new StringBuilder();

    public QuestionBank(final Context context) {
        super(context);
        this.context = context;
    }

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

        Question[] questions;
        try {
            questions = new Gson().fromJson(
                    new InputStreamReader(context.getAssets().open("questions.json")),
                    Question[].class
            );
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        ContentValues values = new ContentValues();
        for (Question question : questions) {
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

    public Question getQuestion() {
        final SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = db.query(TABLE_QUESTIONS,
                columns,
                idsString.toString(),
                idsList.toArray(new String[0]),
                null, null, "random()");
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
