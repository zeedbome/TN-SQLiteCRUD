package com.example.user.tn_sqlitecrud;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
      private SQLiteHelper mSQLite;
      private SQLiteDatabase mDb;

      private String[] mThaiMonths = {
              "ม.ค.", "ก.พ.", "มี.ค.", "เม.ย.", "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค."};
              /*
              {"มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม",
              "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};
             */

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                        /*
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        */
                        startActivity(new Intent(MainActivity.this, Main2Activity.class));
                  }
            });

            mSQLite = SQLiteHelper.getInstance(this);
      }

      @Override
      public void onStart() {
            super.onStart();

            LayoutInflater inflater = getLayoutInflater();
            LinearLayout root = (LinearLayout) findViewById(R.id.main_layout);
            root.removeAllViewsInLayout();

            mDb = mSQLite.getReadableDatabase();
            String sql = "SELECT * FROM important_day ORDER BY month, date";
            final Cursor cursor = mDb.rawQuery(sql, null);

            int i = 0;
            while(cursor.moveToNext()) {
                  View item = inflater.inflate(R.layout.item_layout, null);
                  item.setLayoutParams(new ViewGroup.LayoutParams(
                          ViewGroup.LayoutParams.MATCH_PARENT,
                          ViewGroup.LayoutParams.WRAP_CONTENT));

                  String str = "";
                  if(cursor.getInt(1) < 10) {
                        str += "0";
                  }
                  str += cursor.getString(1) + "  " + mThaiMonths[cursor.getInt(2) - 1];
                  TextView textDate = (TextView) item.findViewById(R.id.text_date);
                  textDate.setText(str);

                  TextView textDayName = (TextView) item.findViewById(R.id.text_day_name);
                  textDayName.setText(cursor.getString(3));

                  final ImageButton btUpdate = (ImageButton) item.findViewById(R.id.button_update);
                  btUpdate.setTag(cursor.getInt(0));
                  btUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              onClickUpdate(btUpdate.getTag().toString());
                        }
                  });

                  final ImageButton btDelete = (ImageButton) item.findViewById(R.id.button_delete);
                  btDelete.setTag(cursor.getInt(0));
                  btDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              onClickDelete(Integer.valueOf(btDelete.getTag().toString()));
                        }
                  });

                  root.addView(item, i);
                  i++;
            }
            cursor.close();
      }

      private void onClickUpdate(String _id) {
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            intent.putExtra("_id", _id);
            startActivity(intent);
      }

      private void onClickDelete(final int _id) {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle("ยืนยันการลบ")
                    .setMessage("ท่านต้องการลบรายการนี้จริงหรือไม่?")
                    .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                                String sql = "DELETE FROM important_day WHERE _id = " + _id;
                                mDb = mSQLite.getWritableDatabase();
                                mDb.execSQL(sql);
                                onStart();
                          }
                    })
                    .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) { }
                    })
                    .show();
      }

      @Override
      public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
      }

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if(id == R.id.action_settings) {
                  return true;
            }

            return super.onOptionsItemSelected(item);
      }
}
