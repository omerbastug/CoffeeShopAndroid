package com.example.coffeeshopproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Insert
    void add(Category category);
    @Insert
    void addAll(List<Category> list);
    @Query("SELECT * FROM category")
    List<Category> getAll();

    @Query("SELECT name FROM category")
    List<String> getAllNames();

    @Query("delete from category")
    void delete();
    @Query("SELECT * FROM category WHERE id = :id LIMIT 1")
    Category getById(long id);

    @Query("SELECT * FROM category WHERE name = :name LIMIT 1")
    Category getByName(String name);


}
