package com.example.coffeeshopproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PersonDAO {
    @Insert
    long add(Person person);
    @Query("SELECT * FROM `person`")
    List<Person> getAll();

    @Query("SELECT * FROM `person` where email = :email LIMIT 1")
    Person getbyEmail(String email);

    @Query("SELECT id FROM `person` where loggedIn = 1 LIMIT 1")
    long getLoggedinId();

    @Query("UPDATE `person` SET loggedIn = :bool WHERE id = :id")
    void updateLoggedIn(int bool,long id);
}
