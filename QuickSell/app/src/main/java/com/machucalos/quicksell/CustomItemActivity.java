package com.machucalos.quicksell;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomItemActivity extends AppCompatActivity {
    TextView name_editText, serial_editText, amount_editText;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_item);
        Intent intent = getIntent();
        CustomItem item = intent.getParcelableExtra("item");
        name_editText = findViewById(R.id.name_editText);
        serial_editText = findViewById(R.id.serial_editText);
        amount_editText = findViewById(R.id.amount_editText);

        name_editText.setText(item.getName());
        serial_editText.setText(item.getSerial_id());
        amount_editText.setText(String.valueOf(item.getAmount()));

        Button createItemButton = findViewById(R.id.button);
        createItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult();
            }
        });
        Button takePictureButton = findViewById(R.id.pictureBtn);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });




    }


        private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(imageBitmap);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void sendResult(){
        Intent intent = new Intent(CustomItemActivity.this, MainActivity.class);
        CustomItem item = new CustomItem(name_editText.getText().toString(), serial_editText.getText().toString(),Double.parseDouble(amount_editText.getText().toString()));
        intent.putExtra("item", item);
        setResult(Activity.RESULT_OK, intent);
        finish();

    }


}
