package com.example.coffeeshopproject.MyActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coffeeshopproject.R;
import com.example.coffeeshopproject.database.AppDatabase;
import com.example.coffeeshopproject.database.Items;

public class ItemDetailsActivity extends AppCompatActivity {
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        long itid = extras.getLong(MainActivity.EXTRA_ITEM_ID);
        Items item = AppDatabase.getInstance(getApplicationContext()).itemsDAO().getById(itid);

        ImageView img = findViewById(R.id.item_image);
        int drawableId=getResources().getIdentifier("itemid"+itid, "drawable", getPackageName());
        img.setImageResource(drawableId);

        name = findViewById(R.id.det_item_name);
        name.setText(item.getName());

        TextView description = findViewById(R.id.item_description);
        String categoryname = AppDatabase.getInstance(getApplicationContext()).categoryDAO().getById(item.getCategory_id()).getName();
        String subcategoryname = AppDatabase.getInstance(getApplicationContext()).subcategoryDAO().getById(item.getSubcategory_id()).getName();
        description.setText(categoryname+"\n"+subcategoryname);
    }
}