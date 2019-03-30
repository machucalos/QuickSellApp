package com.machucalos.quicksell;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

// package private = no modifier
public class MainActivity extends AppCompatActivity {
    static final int CREATE_CUSTOM_ITEM_REQUEST = 1;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.main_listview);
        ArrayList<CustomItem> itemsList = new ArrayList<CustomItem>();
         customAdapter = new CustomAdapter(this,itemsList );
        listView.setAdapter(customAdapter);
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomizeItemActivity(new CustomItem("Test", "1232323", 5344.00));
            }
        });

    }

    public  static class ViewHolder{
        public TextView name;
    }


    private class CustomAdapter extends ArrayAdapter<CustomItem> {

        CustomAdapter(Context context, ArrayList<CustomItem> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
             CustomItem item = getItem(position);
            if(convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.row_main, parent, false);
            }
            TextView name_textView = (TextView)convertView.findViewById(R.id.name_textView);
            name_textView.setText(item.getName());

            TextView serial_textView = (TextView)convertView.findViewById(R.id.serial_textView);
            String serialstring = "serial:" + item.getSerial_id();
            serial_textView.setText(serialstring);

            TextView amount_textView = (TextView)convertView.findViewById(R.id.amount_textView);
            String amountString = "$" + String.valueOf(item.getAmount());
            amount_textView.setText(amountString);

            Button deleteBtn = convertView.findViewById(R.id.deletebtn);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customAdapter.remove(getItem(position));
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCustomizeItemActivity(getItem(position));
                }
            });
            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(getApplicationContext(), "Destroy", Toast.LENGTH_LONG).show();

        super.onDestroy();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();

        if(resultCode == RESULT_OK && requestCode == 1 && data != null){
            CustomItem customItem = data.getParcelableExtra("item");
            customAdapter.add(customItem);
        }

    }

    private void openCustomizeItemActivity(CustomItem item){
        Intent intent = new Intent(MainActivity.this,CustomItemActivity.class);
        intent.putExtra("item", item);
        startActivityForResult(intent, 1);



    }

}
