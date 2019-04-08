package com.machucalos.quicksell;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// package private = no modifier
public class MainActivity extends AppCompatActivity {
    static final int CREATE_CUSTOM_ITEM_REQUEST = 1;
    private CustomAdapter customAdapter;
    private ArrayList<CustomItem> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = findViewById(R.id.main_listview);

        itemsList = loadCustomArray();
        customAdapter = new CustomAdapter(this,itemsList );
        EditText searchBar = findViewById(R.id.search_bar);


        listView.setAdapter(customAdapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomizeItemActivity(new CustomItem("New Item", "Id","size", 5344.00));
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    customAdapter.getFilter().filter(s);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public  static class ViewHolder{
        public TextView name;
    }
    private ArrayList<CustomItem>  loadCustomArray(){
         SharedPreferences sharedPreferences = getSharedPreferences("items", MODE_PRIVATE);

         String json = sharedPreferences.getString("array", null);
         if(json  == null) return  new ArrayList<CustomItem>();
         Gson gson = new Gson();

         return gson.fromJson(json,new TypeToken<ArrayList<CustomItem>>(){

         }.getType());

    }
    private void saveCustomArray(ArrayList<CustomItem> items ){
        SharedPreferences sharedPreferences = getSharedPreferences("items", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(items);
        editor.putString("array",json);
        editor.apply();

    }

    private class CustomAdapter extends ArrayAdapter<CustomItem> implements Filterable {
        private ArrayList<CustomItem> itemsList, itemsListFull;
        CustomAdapter(Context context, ArrayList<CustomItem> items) {
            super(context, 0, items);
            itemsList = items;
            itemsListFull = new ArrayList<>(items);
        }


        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();
                    ArrayList<CustomItem> filterArray = new ArrayList<>();
                    if(constraint == null || constraint.length() == 0){
                        filterArray.addAll(itemsListFull);
                    }else{
                        String filter = constraint.toString().toLowerCase().trim();
                        for(CustomItem item : itemsList){
                            if(item.getName().toLowerCase().contains(filter)){
                                filterArray.add(item);

                            }
                            Toast.makeText(getApplicationContext(), "tefef", Toast.LENGTH_LONG).show();
                        }

                    }

                    filterResults.count = filterArray.size();
                    filterResults.values = filterArray;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    itemsList.clear();
                    ArrayList<CustomItem> tempList = new ArrayList<>();
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof CustomItem) {
                            tempList.add((CustomItem) object); // <-- add to temp
                        }
                    }
                    itemsList.addAll (tempList);
                    notifyDataSetChanged();
                }
            };
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
             CustomItem item = getItem(position);
            if(convertView == null)
            {
                convertView = getLayoutInflater().inflate(R.layout.row_main, parent, false);
            }
            if(item !=null) {
                TextView name_textView = (TextView) convertView.findViewById(R.id.name_textView);
                name_textView.setText(item.getName());

                TextView serial_textView = (TextView) convertView.findViewById(R.id.serial_textView);
                String serialstring = "serial:" + item.getSerial_id();
                serial_textView.setText(serialstring);

                TextView size_textView = (TextView) convertView.findViewById(R.id.size_textView);
                String sizetString = "Size" + String.valueOf(item.getSize());
                size_textView.setText(sizetString);

                TextView amount_textView = (TextView) convertView.findViewById(R.id.amount_textView);
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
            }
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
            saveCustomArray(itemsList);

        }

    }

    private void openCustomizeItemActivity(CustomItem item){
        Intent intent = new Intent(MainActivity.this,CustomItemActivity.class);
        intent.putExtra("item", item);
        startActivityForResult(intent, 1);



    }

}
