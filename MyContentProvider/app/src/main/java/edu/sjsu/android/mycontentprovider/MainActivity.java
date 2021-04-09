package edu.sjsu.android.mycontentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickAddName(View view) {
        // Add a new student record
        ContentValues values = new ContentValues();
        values.put(StudentProvider.NAME,
                ((EditText)findViewById(R.id.txtName)).getText().toString());
        values.put(StudentProvider.GRADE,
                ((EditText) findViewById(R.id.txtGrade)).getText().toString());
        Uri uri = getContentResolver().insert(StudentProvider.CONTENT_URI, values);

        assert uri != null;
        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
    }

    public void onClickRetrieveStudents(View view) {
        // Retrieve student records
        String URL = this.getString(R.string.providerURL);
        Uri students = Uri.parse(URL);
        final Cursor c = getContentResolver().query(students,
                null, null, null, "name");
        if(c != null)
            System.out.println("Count: " + c.getCount());
        Handler handler = new Handler();
        if(c != null && c.moveToFirst()){
            do{
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this,
//                                c.getString(c.getColumnIndex((StudentProvider._ID))) + ". "
//                                        + c.getString(c.getColumnIndex(StudentProvider.NAME)) + ", "
//                                        + c.getString(c.getColumnIndex(StudentProvider.GRADE)),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }, 2000);
                Toast.makeText(MainActivity.this,
                        c.getString(c.getColumnIndex((StudentProvider._ID))) + ". "
                                + c.getString(c.getColumnIndex(StudentProvider.NAME)) + ", "
                                + c.getString(c.getColumnIndex(StudentProvider.GRADE)),
                        Toast.LENGTH_SHORT).show();
//                System.out.println(c.getString(c.getColumnIndex((StudentProvider._ID))) + ". "
//                        + c.getString(c.getColumnIndex(StudentProvider.NAME)) + ", "
//                        + c.getString(c.getColumnIndex(StudentProvider.GRADE)));
            }while(c.moveToNext());
        }
        if(c != null)
            c.close();
    }
}