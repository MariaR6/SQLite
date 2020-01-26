package com.example.tryversionsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
public class MainActivity extends AppCompatActivity {

    ListView userList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userList = (ListView)findViewById(R.id.list);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());
    }
    @Override
    public void onResume() {
        super.onResume();
        db = databaseHelper.getReadableDatabase();
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE, null);
        String[] headers = new String[] {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_SURNAME, DatabaseHelper.COLUMN_YEAR};
        userAdapter = new SimpleCursorAdapter(this,R.layout.my_list_item,
                userCursor, headers, new int[]{ R.id.textView1, R.id.textView2, R.id.textView3}, 0);

        userList.setAdapter(userAdapter);
    }
    public void add(View view){
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        userCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_app, menu);
        return true;
    }
}