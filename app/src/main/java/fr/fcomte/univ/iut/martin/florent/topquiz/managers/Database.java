package fr.fcomte.univ.iut.martin.florent.topquiz.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class Database extends SQLiteOpenHelper {
    static final         String KEY_ID           = "id";
    private static final String DATABASE_NAME    = "top_quiz_db";
    private static final byte   DATABASE_VERSION = 1;

    Database(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }
}
