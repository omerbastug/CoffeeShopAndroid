package com.example.coffeeshopproject.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "orderitems",primaryKeys = {"order_id","item_id"})
public class OrderItems {
    private long id;
    @ColumnInfo(name = "order_id")
    private long order_id;
    @ColumnInfo(name = "user_id")
    private long user_id;
    @ColumnInfo(name = "item_id")
    private long item_id;
    @ColumnInfo(name = "quantity")
    private int quantity;

    public OrderItems(long order_id,long user_id, long item_id, int quantity) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.item_id = item_id;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
