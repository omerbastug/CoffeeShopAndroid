package com.example.coffeeshopproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderItemsDAO {
    @Insert
    void add(OrderItems orderItems);

    @Insert
    void addAll(List<OrderItems> list);

    @Query("SELECT * FROM orderitems")
    List<OrderItems> getAll();

    @Query("SELECT * FROM orderitems WHERE order_id = :order")
    List<OrderItems> getByOrder(long order);

    @Query("SELECT * FROM orderitems WHERE order_id = :order AND user_id = :user AND item_id = :item LIMIT 1")
    OrderItems getcartitem(long order, long user, long item);

    @Query("UPDATE orderitems SET quantity= quantity + :quantity WHERE  order_id = :order AND user_id = :user AND item_id = :item AND quantity+:quantity>=0 ")
    void incrementItem(long order, long user, long item,int quantity);

}
