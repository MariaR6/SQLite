package com.example.tryversionsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {

    EditText FieldForName;
    EditText FieldForSurname;
    EditText FieldForYear;
    Button BtnForDelete;
    Button BtnForSave;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        FieldForName = (EditText) findViewById(R.id.name);
        FieldForYear = (EditText) findViewById(R.id.year);
        FieldForSurname = (EditText)findViewById(R.id.surname);
        BtnForDelete = (Button) findViewById(R.id.BtnForDelete);
        BtnForSave = (Button) findViewById(R.id.BtnForSave);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        // если 0, то добавление
        if (userId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            FieldForName.setText(userCursor.getString(1));
            FieldForSurname.setText(userCursor.getString(2));
            FieldForYear.setText(String.valueOf(userCursor.getInt(3)));
            userCursor.close();
        } else {
            // скрываем кнопку удаления
            BtnForDelete.setVisibility(View.GONE);
        }
    }

    public void save(View view){
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME, FieldForName.getText().toString());
        cv.put(DatabaseHelper.COLUMN_SURNAME, FieldForSurname.getText().toString());
        cv.put(DatabaseHelper.COLUMN_YEAR, Integer.parseInt(FieldForYear.getText().toString()));

        if (userId > 0) {
            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
        } else {
            db.insert(DatabaseHelper.TABLE, null, cv);
        }
        goHome();
    }
    public void delete(View view){
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }
    private void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}