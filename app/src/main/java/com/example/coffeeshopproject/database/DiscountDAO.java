package com.example.coffeeshopproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DiscountDAO {
    @Insert
    void add(Discount discount);

    @Query("SELECT * FROM discount")
    List<Discount> getAll();

    @Query("SELECT * FROM discount WHERE id = :id LIMIT 1")
    Discount getById(long id);

    @Query("SELECT * FROM discount WHERE user_id = :id AND used = 0 LIMIT 1")
    Discount getUserDiscount(long id);

    @Query("UPDATE discount SET used = :used WHERE id = :id")
    void updateUsed(long id,int used);

}
