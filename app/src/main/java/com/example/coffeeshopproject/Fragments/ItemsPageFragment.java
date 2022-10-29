package com.example.coffeeshopproject.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.coffeeshopproject.MyActivites.ItemDetailsActivity;
import com.example.coffeeshopproject.MyActivites.Login;
import com.example.coffeeshopproject.MyActivites.MainActivity;
import com.example.coffeeshopproject.MyAdapters.ItemListAdapter;
import com.example.coffeeshopproject.R;
import com.example.coffeeshopproject.MyActivites.ShoppingCartActivity;
import com.example.coffeeshopproject.database.AppDatabase;
import com.example.coffeeshopproject.database.Category;
import com.example.coffeeshopproject.database.Items;
import com.example.coffeeshopproject.database.Order;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemsPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsPageFragment extends Fragment {
    public static final String EXTRA_ITEM_ID = "MainActivity/EXTRA_ITEM_ID";
    private ListView item_list;
    private List<Items> items;
    private Spinner spnr;
    private int category_id = 1;
    private Button cart_open;
    private EditText searchbar;
    public  ItemListAdapter items_adapter;
    private LayoutInflater mInflater;
    private long user_id;
    private long order_id;
    double latitude, longitude;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static final int reqcode = 100;
    public ItemsPageFragment() {
       // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemsPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsPageFragment newInstance(String param1, String param2) {

        ItemsPageFragment fragment = new ItemsPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_items_page, container, false);
        Bundle arg = getArguments();
        user_id = arg.getLong("userid");
        order_id = arg.getLong("order_id");

        AppDatabase.getInstance(getActivity().getApplicationContext()).subcatdata();

        checkLocationPerm();

        // CART BUTTON
        cart_open = root.findViewById(R.id.cart_button);
        cart_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderstate = AppDatabase.getInstance(getActivity().getApplicationContext()).orderDAO().getById(order_id).getState();

                if (!orderstate.equals("cart")){
                    order_id = AppDatabase.getInstance(getActivity().getApplicationContext()).orderDAO().add(new Order(user_id,"cart"));
                    MainActivity.order_id = order_id;
                    Log.i("ORDER", order_id+"");
                } // CHECK IF CART ORDER EXIST
                Intent intent = new Intent(getActivity().getApplicationContext(), ShoppingCartActivity.class)
                        .putExtra(Login.USER_ID,user_id)
                        .putExtra(MainActivity.EXTRA_ORDER_ID,order_id)
                        .putExtra("lat",latitude)
                        .putExtra("lng",longitude);
                getActivity().startActivityForResult(intent,reqcode);
            }
        });

        // CATEGORY DROPDOWN LIST
        spnr = root.findViewById(R.id.category_spinner);
        ArrayAdapter<String> spinnerAdapter = categorySetupAdapter();
        spnr.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();
        spnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Category category = AppDatabase.getInstance(getActivity().getApplicationContext()).
                        categoryDAO().getByName(spnr.getSelectedItem().toString());
                setupListAdapter((category_id = category.getId()),user_id,order_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        // ITEMS LIST
        item_list = root.findViewById(R.id.items_listview);
        setupListAdapter(1,user_id,order_id);

        // ITEMS SEARCH
        searchbar = root.findViewById(R.id.searchbar);
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
//                ItemsPage.this.items_adapter.getFilter().filter(s);
//                Log.i("Changed",s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                ItemsPageFragment.this.items_adapter.getFilter().filter(s);
                Log.i("Changed",s.toString());
            }
        });

        return root;
    }

    public ArrayAdapter<String> categorySetupAdapter(){
        List<String> list = AppDatabase.getInstance(getActivity().getApplicationContext()).categoryDAO().getAllNames();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.spinner_categorylist_item,R.id.spinnertext,list);
        return spinnerAdapter;
    }

    public void setupListAdapter(int category,long user, long order){
        items = AppDatabase.getInstance(getActivity().getApplicationContext()).itemsDAO().getAllByCategory(category);
        items_adapter = new ItemListAdapter(getActivity().getApplicationContext(),R.layout.item_list_item,items,user, order);
        item_list.setAdapter(items_adapter);
        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long itemid = parent.getItemIdAtPosition(position);
                Intent intent = new Intent(getActivity().getApplicationContext(), ItemDetailsActivity.class);
                intent.putExtra(MainActivity.EXTRA_ITEM_ID,itemid);
                startActivity(intent);
            }
        });
    }
    private void checkLocationPerm(){
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("PERMREQUEST: ","Permission does not EXISTS");
            // request the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        } else {
            Log.i("PERMREQUEST: ","Permission EXISTS");
            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                    10, mLocationListener);
        }
    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.i("LISTENER",latitude+" "+longitude);
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== 123){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                            50, mLocationListener);
                    Log.i("ONREQSUBMISSION ","REQUEST APPROVED");
                }
            }
        }
    }
}