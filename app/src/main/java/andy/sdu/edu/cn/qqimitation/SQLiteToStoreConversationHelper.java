package andy.sdu.edu.cn.qqimitation;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static andy.sdu.edu.cn.qqimitation.SQLiteToStoreConversationHelper.ConversationEntry.COMMA_SEP;
import static andy.sdu.edu.cn.qqimitation.SQLiteToStoreConversationHelper.ConversationEntry.TABLE_NAME;
import static andy.sdu.edu.cn.qqimitation.SQLiteToStoreConversationHelper.ConversationEntry.TEXT_TYPE;

/**
 * Created by andy on 12/6/16.
 */

public class SQLiteToStoreConversationHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "StoreConversation.db";

    /* Inner class that defines the table contents */
    public static class ConversationEntry implements BaseColumns {
        public static final String TEXT_TYPE = " TEXT NOT NULL";
        public static final String COMMA_SEP = ",";
        public static final String TABLE_NAME = "QQConversationEntry";
        public static final String COLUMN_NAME_WHO = "WHO";
        public static final String COLUMN_NAME_FROM_TO = "FROMWHO";
        public static final String COLUMN_NAME_CONTENT = "CONTENT";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + ConversationEntry.TABLE_NAME + " (" +
                    ConversationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ConversationEntry.COLUMN_NAME_WHO + TEXT_TYPE + COMMA_SEP +
                    ConversationEntry.COLUMN_NAME_FROM_TO + TEXT_TYPE + COMMA_SEP +
                    ConversationEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + " );";


    public SQLiteToStoreConversationHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
