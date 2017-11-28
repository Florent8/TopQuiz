package fr.fcomte.univ.iut.martin.florent.topquiz.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.fcomte.univ.iut.martin.florent.topquiz.models.Question;

public final class QuestionBank extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "top_quiz_db";
    private static final byte   DATABASE_VERSION = 1;
    private static final String TABLE_QUESTIONS  = "questions";
    private static final String KEY_ID           = "id";
    private static final String KEY_QUESTION     = "question";
    private static final String KEY_ANSWER1      = "answer1";
    private static final String KEY_ANSWER2      = "answer2";
    private static final String KEY_ANSWER3      = "answer3";
    private static final String KEY_ANSWER4      = "answer4";
    private static final String KEY_GOOD_ANSWER  = "good_answer";
    private final Context context;
    private final String[]      columns   = new String[]{KEY_ID, KEY_QUESTION, KEY_ANSWER1, KEY_ANSWER2, KEY_ANSWER3, KEY_ANSWER4, KEY_GOOD_ANSWER};
    private final List<String>  idsList   = new ArrayList<>();
    private final StringBuilder idsString = new StringBuilder();

    public QuestionBank(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("questions.txt")));
            String question;
            final ContentValues values = new ContentValues();
            while ((question = br.readLine()) != null) {
                values.put(KEY_QUESTION, question);
                values.put(KEY_ANSWER1, br.readLine());
                values.put(KEY_ANSWER2, br.readLine());
                values.put(KEY_ANSWER3, br.readLine());
                values.put(KEY_ANSWER4, br.readLine());
                values.put(KEY_GOOD_ANSWER, br.readLine());
                db.insert(TABLE_QUESTIONS, null, values);
                br.readLine();
            }
            br.close();
        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage());
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
        idsString.append(KEY_ID + "!= ? ");
        idsList.add(cursor.getString(cursor.getColumnIndex(KEY_ID)));
        cursor.close();
        db.close();
        return question;
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }
}
