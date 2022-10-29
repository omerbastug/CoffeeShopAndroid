package com.example.coffeeshopproject.database;

import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "discount")
public class Discount {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "user_id")
    private long user_id;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "used")
    private int used;

    public Discount(long user_id, double amount) {
        this.user_id = user_id;
        this.amount = amount;
        this.used = 0;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
