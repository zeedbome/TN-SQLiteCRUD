package com.example.user.tn_sqlitecrud;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
      private SQLiteHelper mSQLite;
      private SQLiteDatabase mDb;

      private EditText mEditDate;
      private EditText mEditMonth;
      private EditText mEditDayName;
      private int _id = -1;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                  }
            });

             mSQLite = SQLiteHelper.getInstance(this);

            mEditDate = (EditText)findViewById(R.id.edit_date);
            mEditMonth = (EditText)findViewById(R.id.edit_month);
            mEditDayName = (EditText)findViewById(R.id.edit_day_name);

            Button button = (Button)findViewById(R.id.button_add);
            button.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        addImportantDay();
                  }
            });

            Button buttonBack = (Button)findViewById(R.id.button_back);
            buttonBack.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        finish();
                        startActivity(new Intent(Main2Activity.this, MainActivity.class));
                  }
            });

            Button buttonClear = (Button)findViewById(R.id.button_clear);
            buttonClear.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        mEditDate.setText("");
                        mEditMonth.setText("");
                        mEditDayName.setText("");
                  }
            });
      }

      @Override
      public void onStart() {
            super.onStart();
            Intent intent = getIntent();
            String id = intent.getStringExtra("_id");   //อย่าใช้ getIntExtra()
            if(id == null) {
                  return;
            } else {
                  _id = Integer.valueOf(id);
            }

            String sql =
                    "SELECT * FROM important_day " +
                     "WHERE _id = " + _id;

            mDb = mSQLite.getReadableDatabase();
            Cursor cursor = mDb.rawQuery(sql, null);
            cursor.moveToNext();
            mEditDate.setText(cursor.getString(1));
            mEditMonth.setText(cursor.getString(2));
            mEditDayName.setText(cursor.getString(3));

            cursor.close();
      }

      private void addImportantDay() {
            int date = Integer.valueOf(mEditDate.getText().toString());
            int month = Integer.valueOf(mEditMonth.getText().toString());
            String dayName = mEditDayName.getText().toString().trim();

            if(!isDataComplete(date, month, dayName)) {
                  return;
            }

            if(_id == -1) {
                  insert(date, month, dayName);
            } else {
                  update(date, month, dayName);
            }
      }

      private void insert(int date, int month, String dayName) {
            String sql =
                    "INSERT INTO important_day(date, month, day_name) " +
                     "VALUES(?, ?, ?)";

            String[] args = {date+"", month+"", dayName};
            mDb = mSQLite.getWritableDatabase();
            mDb.execSQL(sql, args);
            showMessage("บันทึกข้อมูลแล้ว");
      }

      private void update(int date, int month, String dayName) {
            String sql =
                    "UPDATE important_day " +
                     "SET date = ?, month = ?, day_name = ? " +
                    "WHERE _id = ?";

            String[] args = {date+"", month+"", dayName, _id+""};

            mDb = mSQLite.getWritableDatabase();
            mDb.execSQL(sql, args);
            showMessage("บันทึกข้อมูลแล้ว");
      }

      private boolean isDataComplete(int date, int month, String dayName) {
            if(date < 1 || date > 31) {
                  showMessage("วันที่ต้องเป็นตัวเลข 1 - 31");
                  return false;
            }

            if(month < 1 || month > 12) {
                  showMessage("เดือนต้องเป็นตัวเลข 1 - 12");
                  return false;
            }

            switch(month) {
                  case 4: case 6: case 9: case 11:
                        if(date == 31) {
                              showMessage("เดือนที่กำหนดเพียง 30 วัน");
                              return false;
                        }
                        break;
                  case 2:
                        if(date > 29) {
                              showMessage("เดือนที่กำหนดมี 28 - 29 วัน");
                              return false;
                        }
            }

            if(dayName.equals("")) {
                  showMessage("ยังไม่ได้กำหนดชื่อวันสำคัญ");
                  return false;
            }

            return true;
      }

      private void showMessage(String msg) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
      }

      @Override
      public void onBackPressed() {
            finish();
      }
}
