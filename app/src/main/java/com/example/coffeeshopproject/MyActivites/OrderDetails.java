
package com.example.coffeeshopproject.MyActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.coffeeshopproject.MyActivites.ItemDetailsActivity;
import com.example.coffeeshopproject.MyActivites.MainActivity;
import com.example.coffeeshopproject.MyAdapters.CartListAdapter;
import com.example.coffeeshopproject.R;
import com.example.coffeeshopproject.database.AppDatabase;
import com.example.coffeeshopproject.database.OrderItems;

import java.util.List;

public class OrderDetails extends AppCompatActivity {
    long order_id;
    ListView order_items_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        order_id = getIntent().getExtras().getLong("orderid");
        List<OrderItems> orderitems = AppDatabase.getInstance(this).orderItemsDAO().getByOrder(order_id);
        CartListAdapter adapter = new CartListAdapter(this,orderitems);
        order_items_list = findViewById(R.id.orderitemslistview);
        order_items_list.setAdapter(adapter);
        order_items_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long itemid = AppDatabase.getInstance(getApplicationContext()).itemsDAO().getById(orderitems.get(i).getItem_id()).getId();
                Intent intent = new Intent(getApplicationContext(), ItemDetailsActivity.class);
                intent.putExtra(MainActivity.EXTRA_ITEM_ID,itemid);
                startActivity(intent);
            }
        });

    }
}