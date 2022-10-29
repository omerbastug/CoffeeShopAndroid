package com.example.coffeeshopproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BranchDAO {
    @Insert
    void add(Branch branch);

    @Insert
    void addAll(List<Branch> list);

    @Query("SELECT * FROM branch")
    List<Branch> getAll();

    @Query("SELECT location FROM branch")
    List<String> getAllLocations();

    @Query("SELECT id FROM branch where location=:location LIMIT 1")
    int getByLocation(String location);

    @Query("SELECT * FROM branch WHERE id = :id LIMIT 1")
    Branch getById(long id);

}
