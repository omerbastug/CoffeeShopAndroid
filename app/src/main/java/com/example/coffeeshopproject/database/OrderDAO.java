package com.example.coffeeshopproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDAO {

    @Insert
    long add(Order order);

    @Query("SELECT * FROM `order`")
    List<Order> getAll();

    @Query("SELECT * FROM `order` WHERE id = :id LIMIT 1")
    Order getById(long id);

    @Query("SELECT * FROM `order` WHERE user_id = :id AND NOT state = 'cart' ")
    List<Order> getByUser(long id);


    @Query("SELECT id FROM `order` WHERE user_id = :id AND date = :date LIMIT 1")
    long getInstantce(long id, String date);

    @Query("SELECT * FROM `order` WHERE user_id = :id AND state = :state LIMIT 1")
    Order getByUserState(long id, String state);

    @Query("UPDATE `order` SET state = :state , date = :date, total = :total, branch_id = :branch_id WHERE id = :order ")
    void updateState(long order, String state, String date,double total,long branch_id);



}
