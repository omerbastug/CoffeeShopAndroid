package com.example.coffeeshopproject.MyAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.coffeeshopproject.R;
import com.example.coffeeshopproject.database.AppDatabase;
import com.example.coffeeshopproject.database.Items;
import com.example.coffeeshopproject.database.Order;
import com.example.coffeeshopproject.database.OrderItems;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends BaseAdapter implements Filterable{
    private List<Items> filteredData;
    private List<Items> itemList;
    private Context context;
    private ItemFilter mFilter = new ItemFilter();
    private LayoutInflater mInflater;
    private long user;
    private long order_id;


    int Resource;
    public ItemListAdapter(Context context,int resource, List<Items> itemList,long user,long order_id) {
//        super(context,resource,itemList);
        Resource = resource;
        this.context = context;
        this.filteredData = itemList;
        this.itemList = itemList;
        mInflater = LayoutInflater.from(context);
        this.user = user;
        this.order_id = order_id;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Items getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int[] quantity = new int[1];
        convertView = mInflater.inflate(R.layout.item_list_item, parent,false);
        if(position>=filteredData.size()){
            return convertView;
        } else {
            Items item = filteredData.get(position);
            OrderItems orderitem = AppDatabase.getInstance(context).orderItemsDAO().getcartitem(order_id,user,item.getId());
            if (orderitem!=null){
                quantity[0] = orderitem.getQuantity();
            } else{
                quantity[0] = 0;
            }
            TextView quantitytext = convertView.findViewById(R.id.quantitiy);
            quantitytext.setText(quantity[0] +"");

            Button btn = convertView.findViewById(R.id.item_button_add);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( quantity[0] != 0 ){
                        AppDatabase.getInstance(context).orderItemsDAO().incrementItem(order_id,user,item.getId(),1);
                        Log.i("ORDERITEM","INCREMENTED");
                    } else {
                        AppDatabase.getInstance(context).orderItemsDAO().add(new OrderItems(order_id,user,item.getId(),1));
                        Log.i("ORDERITEM","ADDED");
                    }
                    quantity[0]++;
                    quantitytext.setText(quantity[0] +"");
                }
            });

            Button delbtn = convertView.findViewById(R.id.item_button_del);
            delbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppDatabase.getInstance(context).orderItemsDAO().incrementItem(order_id,user,item.getId(),-1);
                    quantity[0]--;
                    quantitytext.setText(quantity[0] +"");
                }
            });

            TextView name = convertView.findViewById(R.id.item_name);
            name.setText(item.getName());

            TextView price = convertView.findViewById(R.id.item_price);
            price.setText(item.getPrice()+"");

            return convertView;
                }
    }


    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Items> list = itemList;

            int count = list.size();
            final List<Items> nlist = new ArrayList<Items>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (List<Items>) results.values;
            notifyDataSetChanged();
        }

    }
}
