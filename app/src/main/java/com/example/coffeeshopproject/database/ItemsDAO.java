package com.example.coffeeshopproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;
@Dao
public interface ItemsDAO {
    @Insert
    void add(Items items);

    @Insert
    void addAll(List<Items> listt);

    @Query("SELECT * FROM items")
    List<Items> getAll();

    @Query("SELECT * FROM items WHERE quantity>0")
    List<Items> getCart();

    @Query("delete  FROM items")
    void delete();

    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    Items getById(long id);

    @Query("SELECT * FROM items WHERE category_id = :id")
    List<Items> getAllByCategory(int id);

    @Query("UPDATE items SET quantity = quantity+:increment WHERE id = :id AND quantity+:increment>=0")
    void addToCart(long id,int increment);

    @Query("UPDATE items SET quantity = 0")
    void resetCart();

}
