package com.example.coffeeshopproject.MyActivites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.FragmentTransaction;


import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.coffeeshopproject.Fragments.ItemsPageFragment;
import com.example.coffeeshopproject.Fragments.OrdersFragment;
import com.example.coffeeshopproject.Fragments.MyMapFragment;
import com.example.coffeeshopproject.R;
import com.example.coffeeshopproject.database.AppDatabase;
import com.example.coffeeshopproject.database.Discount;
import com.example.coffeeshopproject.database.Order;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_ITEM_ID = "MainActivity/EXTRA_ITEM_ID";
    public static final String EXTRA_ORDER_ID = "MainActivity/EXTRA_ORDER_ID";
    public static final String REQUEST_CODES = "MainActivity/REQUEST_CODE";
    private static final String CHANNEL_ID = "MainActivirt/MyCHANNEL_ID";
    public static final int NOT_ID=1;
    private long user_id;
    public static long order_id;

    private Bundle arg;
    double latitude,longitude;
    Button logout;
    ItemsPageFragment itemsfrag;
    FragmentTransaction ft;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);

        AppDatabase.getInstance(this).catdata();


        user_id = getIntent().getExtras().getLong(Login.USER_ID);

        startTimer();

        // Initialize an order with cart state
        cartOrderID();

        // Bundle for fragments
        arg = new Bundle();
        arg.putLong("userid",user_id);
        arg.putLong("order_id",order_id);


        // Initial home fragment
        itemsfrag = new ItemsPageFragment();
        itemsfrag.setArguments(arg);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,itemsfrag,null);
        ft.commit();

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase.getInstance(getApplicationContext()).personDAO().updateLoggedIn(0,user_id);
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }

    private void cartOrderID(){
        Order order = AppDatabase.getInstance(this).orderDAO().getByUserState(user_id,"cart");
        if(order == null){
            Log.i("cart", "NEW");
            order_id = AppDatabase.getInstance(this).orderDAO().add(new Order(user_id,"cart"));
        } else {
            Log.i("cart", "EXISTS");
            order_id = order.getId();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Options menu fragment selection
        switch (item.getItemId()) {
            case R.id.menu_item_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,itemsfrag).commit();
                return true;
            case R.id.menu_item_orders:
                OrdersFragment ofrag = new OrdersFragment();
                ofrag.setArguments(arg);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container,ofrag);
                ft.commit();
                return true;
            case R.id.menu_item_map:
                AppDatabase.getInstance(this).branchdata();
                MyMapFragment mapfrag = new MyMapFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mapfrag).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ItemsPageFragment.reqcode) {
            if (resultCode == 123) {
                // Refresh items list fragment with new order_id
                order_id = data.getExtras().getLong("order_id");
                Bundle bund = new Bundle();
                bund.putLong("order_id",order_id);
                bund.putLong("userid",user_id);
                itemsfrag = new ItemsPageFragment();
                itemsfrag.setArguments(bund);
                Log.i("new Item frag","done");
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container,itemsfrag,null);
                ft.commit();

                startTimer();
            }
            if (resultCode == RESULT_CANCELED) {
            }
        }
    }
    public boolean discountAvailable(){
        Discount d = AppDatabase.getInstance(this).discountDAO().getUserDiscount(user_id);
        if (d!=null){
            return true;
        }
        return false;
    }
    public void startTimer(){
        if(!discountAvailable()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel= new NotificationChannel(CHANNEL_ID, "My custom channel", NotificationManager.IMPORTANCE_DEFAULT);

                NotificationManager manager=getSystemService(NotificationManager.class);

                manager.createNotificationChannel(channel);
            }

            countDownTimer = new CountDownTimer(25000, 1000) {
                //8.64e+7
                @Override
                public void onTick(long l) {
                    //l - remaining milliseconds
                }

                @Override
                public void onFinish() {
                    // Create discount for user
                    AppDatabase.getInstance(MainActivity.this).discountDAO().add(new Discount(user_id,5.00));

                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("YOU GOT A DISCOUNT")
                            .setTitle("Congratulations")
                            .show();

                    cartOrderID();

                    Intent cartintent = new Intent(getApplicationContext(), ShoppingCartActivity.class)
                            .putExtra(Login.USER_ID,user_id)
                            .putExtra(EXTRA_ORDER_ID,order_id)
                            .putExtra("lat",latitude)
                            .putExtra("lng",longitude);

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addNextIntentWithParentStack(cartintent);
                    PendingIntent pendingIntentCart = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent intent= new Intent(getApplicationContext(), Login.class);
                    PendingIntent pendingIntent= PendingIntent.getActivity(MainActivity.this, 0, intent, 0);


                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID );
                    builder.setContentTitle("Discount Notification")
                            .setContentText("Discount Available")
                            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setColor(Color.RED)
                            .setContentIntent(pendingIntent)
                            .addAction(R.drawable.ic_baseline_notifications_24, "Go to cart", pendingIntentCart)
                            .setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                    managerCompat.notify(NOT_ID, builder.build());

                }
            };
            countDownTimer.start();
        }
    }

}