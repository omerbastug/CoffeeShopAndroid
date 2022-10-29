package com.example.coffeeshopproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SubcategoryDAO {
    @Insert
    void add(Subcategory subcategory);
    @Insert
    void addAll(List<Subcategory> list);
    @Query("SELECT * FROM subcategory")
    List<Subcategory> getAll();

    @Query("SELECT * FROM subcategory WHERE id = :id LIMIT 1")
    Subcategory getById(long id);

}
