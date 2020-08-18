package com.example.adapter;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Объявление переменных БД
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    long userId=0;

    //Объявление обработки клавиатуры
    boolean keyboardVisible=true;
    //Объявление обртаботки Списков
    //ArrayList<String> products = new ArrayList();
    ArrayList<String> boughtProducts = new ArrayList();
    ArrayList<String> selectedProducts = new ArrayList();
   // ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    Button button;


    ListView firstList;
    ListView secondList;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initKeyboard();

        /*products.add("iPhone 7");
        products.add("Samsung Galaxy S7");
        products.add("Google Pixel");
        products.add("Huawei P10");
        products.add("HP Elite z3");*/

        firstList =  findViewById(R.id.firstList);
        secondList = findViewById(R.id.secondList);
        button = findViewById(R.id.delete_button);
        //===========================================
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {DatabaseHelper.COLUMN_NAME};
        // создаем адаптер, передаем в него курсор

        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice,
                userCursor, headers, new int[]{android.R.id.text1}, 0);
        firstList.setAdapter(userAdapter);
        //===========================================

       /* adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, );
        firstList.setAdapter(adapter);*/

        adapter2 = new ArrayAdapter(this,android.R.layout.simple_list_item_1,boughtProducts);
        secondList.setAdapter(adapter2);
        // обработка установки и снятия отметки в списке
        firstList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                // получаем нажатый элемент
                String product = userAdapter.getItem(position);
                if(firstList.isItemChecked(position)){
                    selectedProducts.add(product);

                }
                else{

                    selectedProducts.remove(product);
                }
            }
        });
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }
//TODO:ФУНКЦИИ БД
//-----------------------------------------------------------------------------------------------------
    @Override
    public void onResume() {
        super.onResume();


    }
    public void add(View view){
        EditText productEditText = findViewById(R.id.product);
        String product = productEditText.getText().toString();
        if(!product.isEmpty()) {
            ContentValues cv = new ContentValues();

            cv.put(DatabaseHelper.COLUMN_NAME, product);
            db.insert(DatabaseHelper.TABLE, null, cv);

            /*adapter.add(product);
            productEditText.setText("");
            adapter.notifyDataSetChanged();*/

        }

    }

    public void remove(View view){

        for(int i=0; i< selectedProducts.size();i++){
            boughtProducts.add(selectedProducts.get(i));

        }
        adapter.notifyDataSetChanged();
        db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }
//--------------------------------------------------------------------------------------------------

    private void initKeyboard(){
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        Log.d(TAG, "onVisibilityChanged: 7dsadas77777777777777777777777777777777");
                       if(keyboardVisible){
                            button.setVisibility(View.GONE);
                            keyboardVisible = false;
                            
                       }else {
                           
                           button.setVisibility(View.VISIBLE);
                           keyboardVisible = true;
                       }
                    }
                });
    }
    /*public void add(View view){

        EditText productEditText = findViewById(R.id.product);
        String product = productEditText.getText().toString();
        if(!product.isEmpty() && products.contains(product)==false){
            adapter.add(product);
            productEditText.setText("");
            adapter.notifyDataSetChanged();
        }
    }*/
    /*public void remove(View view){
        // получаем и удаляем выделенные элементы
        for(int i=0; i< selectedProducts.size();i++){
            boughtProducts.add(selectedProducts.get(i));
            adapter.remove(selectedProducts.get(i));
        }
        // снимаем все ранее установленные отметки
        firstList.clearChoices();
        // очищаем массив выбраных объектов
        selectedProducts.clear();
        adapter.notifyDataSetChanged();

        adapter2.notifyDataSetChanged();
    }*/

    public void clear(View view){
        //Очищаем второй список
        boughtProducts.clear();
        adapter2.notifyDataSetChanged();

    }
}