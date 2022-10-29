package com.example.coffeeshopproject.MyAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.coffeeshopproject.R;
import com.example.coffeeshopproject.database.AppDatabase;
import com.example.coffeeshopproject.database.Order;

import java.util.List;

public class OrderListAdapter extends BaseAdapter {
    List<Order> orders;
    Context context;

    public OrderListAdapter( Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int i) {
        return orders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return orders.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.order_list_item, viewGroup,false);

        Order order = orders.get(i);
        Log.i("ORDER ID",order.getId() + "  " + order.getState());

        TextView date = view.findViewById(R.id.order_item_date);
        date.setText("Order date:"+order.getDate());

        TextView total = view.findViewById(R.id.order_item_total);
        total.setText("Total: "+order.getTotal());

        TextView branch_name = view.findViewById(R.id.order_item_branch);
        branch_name.setText(AppDatabase.getInstance(context).branchDAO().getById(order.getBranch_id()).getName());

        return view;
    }
}
