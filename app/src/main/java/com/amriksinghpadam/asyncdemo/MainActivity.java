package com.amriksinghpadam.asyncdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private String[] listItemArray;
    private TextView textView;
    private Button btn, addBtn;
    private ArrayAdapter<String> adapter;
    private EditText editText;
    private MyDatabase db;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewId);
        textView = findViewById(R.id.textViewId);
        btn = findViewById(R.id.buttonId);
        editText = findViewById(R.id.editTextId);
        addBtn = findViewById(R.id.addBtnId);
        menu = findViewById(R.id.resetId);
        db = new MyDatabase(MainActivity.this);
        //listItemArray = getResources().getStringArray(R.array.listItems);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = new ArrayList<>();
                Cursor data = db.readItem();
                if(data.getCount()==0){
                    Toast.makeText(MainActivity.this,"No Data Found!!",Toast.LENGTH_SHORT).show();
                }else{
                    while (data.moveToNext()){
                        list.add(data.getString(1));
                    }
                    listItemArray = list.toArray(new String[list.size()]);
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute(listItemArray);
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToAdd = editText.getText().toString().trim();
                if(!TextUtils.isEmpty(textToAdd)){
                    db.insert(textToAdd);
                    Toast.makeText(MainActivity.this,textToAdd+" Added!",Toast.LENGTH_SHORT).show();
                    editText.setText("");
                }else{
                    Toast.makeText(MainActivity.this,"Empty Field!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        db.deleteItem();
        if(adapter!=null)adapter.clear();
        return true;
    }
    class MyAsyncTask extends AsyncTask<String, String ,String>{
        @Override
        protected void onPreExecute() {
            adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1);
            listView.setAdapter(adapter);
            textView.setText("Items Retrieving Started!!!");
        }
        @Override
        protected String doInBackground(String... listItemArray) {
            int l=0;
            synchronized (this){
                try {
                    while((l != listItemArray.length)){
                        wait(800);
                        publishProgress(listItemArray[l]);
                        l++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.add(values[0]);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            textView.setText("Retrieving Done!");
        }
    }

}
