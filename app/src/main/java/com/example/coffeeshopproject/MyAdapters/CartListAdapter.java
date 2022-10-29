package com.example.coffeeshopproject.MyAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.coffeeshopproject.R;
import com.example.coffeeshopproject.database.AppDatabase;
import com.example.coffeeshopproject.database.Items;
import com.example.coffeeshopproject.database.Order;
import com.example.coffeeshopproject.database.OrderItems;

import java.util.List;

public class CartListAdapter extends BaseAdapter {
    Context context;
    List<OrderItems> cart;

    public CartListAdapter(Context context,List<OrderItems> cart) {
        this.context = context;
        this.cart = cart;
    }

    @Override
    public int getCount() {
        return cart.size();
    }

    @Override
    public Object getItem(int i) {
        return cart.get(i);
    }

    @Override
    public long getItemId(int i) {
        return cart.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.cart_list_item, parent,false);

        Items item = AppDatabase.getInstance(context).itemsDAO().getById(cart.get(position).getItem_id());

        TextView name = convertView.findViewById(R.id.cart_item_name);
        name.setText(item.getName());

        int quant  = cart.get(position).getQuantity();
        TextView quantity = convertView.findViewById(R.id.cart_item_quantity);
        quantity.append(quant+"");

        double total = item.getPrice() * quant;
        TextView itemtotal = convertView.findViewById(R.id.cart_item_price);
        itemtotal.setText(total+"");

        return convertView;
    }
}
