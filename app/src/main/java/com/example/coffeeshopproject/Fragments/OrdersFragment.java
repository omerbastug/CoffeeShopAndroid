package com.example.coffeeshopproject.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.coffeeshopproject.MyAdapters.OrderListAdapter;
import com.example.coffeeshopproject.MyActivites.OrderDetails;
import com.example.coffeeshopproject.R;
import com.example.coffeeshopproject.database.AppDatabase;
import com.example.coffeeshopproject.database.Order;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment {
    private long user_id;
    public OrderListAdapter orderadapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
        user_id  = getArguments().getLong("userid");


        List<Order> orders = AppDatabase.getInstance(getActivity().getApplicationContext()).orderDAO().getByUser(user_id);
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        ListView list = root.findViewById(R.id.orders_listview);

        orderadapter = new OrderListAdapter(getActivity().getApplicationContext(),orders);
        list.setAdapter(orderadapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent orderdetails = new Intent(getActivity().getApplicationContext(), OrderDetails.class);
                orderdetails.putExtra("orderid",orders.get(i).getId());
                startActivity(orderdetails);
                // Launch new activity with order items. use cart adapter on new activity
                // put extra user id and order id. fasho
            }
        });
        return root;
    }
}