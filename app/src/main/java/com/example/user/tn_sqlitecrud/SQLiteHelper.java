package com.example.user.tn_sqlitecrud;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteHelper extends SQLiteOpenHelper {
      private static SQLiteHelper sqLiteDB;
      private static final String DB_NAME = "dbase";
      private static final int DB_VERSION = 1;

      private SQLiteHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
      }

      public static synchronized SQLiteHelper getInstance(Context context) {
            if(sqLiteDB == null) {
                  sqLiteDB = new SQLiteHelper(context.getApplicationContext());
            }
            return sqLiteDB;
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
            String sql =
                    "CREATE TABLE IF NOT EXISTS important_day (" +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "date INTEGER, " +
                            "month INTEGER, "  +
                            "day_name TEXT)";

            db.execSQL(sql);
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {

      }

}

