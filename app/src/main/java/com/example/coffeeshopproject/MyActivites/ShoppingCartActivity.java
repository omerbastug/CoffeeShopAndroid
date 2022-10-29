package com.example.coffeeshopproject.MyActivites;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeeshopproject.MyAdapters.CartListAdapter;
import com.example.coffeeshopproject.R;
import com.example.coffeeshopproject.database.AppDatabase;
import com.example.coffeeshopproject.database.Branch;
import com.example.coffeeshopproject.database.Discount;
import com.example.coffeeshopproject.database.Order;
import com.example.coffeeshopproject.database.OrderItems;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {
    private ListView cartlw;
    public double total = 0;
    private TextView carttotal;
    private Button purchasebutton;
    long order_id, user_id;
    int branch_id;
    double userlat,userlng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        order_id = getIntent().getExtras().getLong(MainActivity.EXTRA_ORDER_ID);
        user_id = getIntent().getExtras().getLong(Login.USER_ID);
        userlat = getIntent().getExtras().getDouble("lat");
        userlng = getIntent().getExtras().getDouble("lng");
        Log.i("USER LOC",userlat+" "+userlng);

        AppDatabase.getInstance(this).branchdata();

        // Get cart items and display details
        cartlw = findViewById(R.id.cart_listview);
        List<OrderItems> cartitems = AppDatabase.getInstance(this).orderItemsDAO().getByOrder(order_id);
        CartListAdapter adapter = new CartListAdapter(this,cartitems);
        cartlw.setAdapter(adapter);
        cartlw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long itemid = AppDatabase.getInstance(getApplicationContext()).itemsDAO().getById(cartitems.get(i).getItem_id()).getId();
                Intent intent = new Intent(getApplicationContext(), ItemDetailsActivity.class);
                intent.putExtra(MainActivity.EXTRA_ITEM_ID,itemid);
                startActivity(intent);
            }
        });
        for (OrderItems item : cartitems){
            double itemtotal = AppDatabase.getInstance(this).itemsDAO().getById(item.getItem_id()).getPrice() * item.getQuantity();
            total += itemtotal;
        }
        double itemstotal = total;
        carttotal = findViewById(R.id.cart_total);
        carttotal.append(total+"");

        // Check discount
        Discount discount = AppDatabase.getInstance(this).discountDAO().getUserDiscount(user_id);
        if (discount!=null){
            int resourceId = getResources().getIdentifier("discountsfx",
                    "raw","com.example.coffeeshopproject");
            MediaPlayer mplayer= MediaPlayer.create(this, resourceId);
            mplayer.start();

            carttotal.setPaintFlags(carttotal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            double discountamount = discount.getAmount();
            if (total<discountamount){
                total = 0; // set total to 0 if less than discount amount
            } else {
                total -= discountamount;
            }
            TextView totaltxvw = findViewById(R.id.afterdiscount);
            totaltxvw.setText("Total :" +total);
        }

        // Find the closest branch and assign branch id for the order
        List<String> branchlist = AppDatabase.getInstance(this).branchDAO().getAllLocations();
        Double min = Double.MAX_VALUE;
        int index = 3;
        for(int i = 0 ; i<branchlist.size();i++){
            String[] latlng = branchlist.get(i).split(" , ");
            Log.i("branch location",latlng[0]+" "+latlng[1]);
            Double distance = distance(userlat,userlng,Double.parseDouble(latlng[0]),Double.parseDouble(latlng[1]));
            if (distance<min){
                min = distance;
                index = i;
            }
        }
        String branchloc = branchlist.get(index);
        branch_id = AppDatabase.getInstance(this).branchDAO().getByLocation(branchloc);
        Log.i("Nearest BRANCHID", branch_id+"");


        purchasebutton = findViewById(R.id.purchase_button);
        purchasebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemstotal==0){
                    Toast.makeText(ShoppingCartActivity.this, "Cart empty", Toast.LENGTH_SHORT).show();
                } else {

                    String now = LocalDateTime.now().toString();

                    AppDatabase.getInstance(getApplicationContext()).orderDAO().updateState(order_id,"purchased",now,total,branch_id);
                    if(discount!=null){
                        // Change the state of discount if used
                        AppDatabase.getInstance(getApplicationContext()).discountDAO().updateUsed(discount.getId(),1);
                    }
                    Toast.makeText(ShoppingCartActivity.this, "Purchase made", Toast.LENGTH_SHORT).show();

                    // Pass new order id to refresh fragment in MainActivity
                    order_id = AppDatabase.getInstance(getApplicationContext()).orderDAO().add(new Order(user_id,"cart"));
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("order_id",order_id);
                    //resultIntent.putExtra("discountreset","code");
                    setResult(123,resultIntent);
                    finish();
                }
            }
        });


    }

    private double distance(double lat1, double lon1, double lat2, double lon2 ) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = deg2rad(dist);
        dist = dist * 1.609344;

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

}