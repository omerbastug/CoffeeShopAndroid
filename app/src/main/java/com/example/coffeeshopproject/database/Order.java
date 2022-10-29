package com.example.coffeeshopproject.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order" )
public class Order {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "user_id")
    private long user_id;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "state")
    private String state;

    @ColumnInfo(name = "total")
    private double total;

    @ColumnInfo(name = "branch_id")
    private int branch_id;

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public Order() {
    }
    public Order(long user_id, String state) {
        this.user_id = user_id;
        this.state = state;
        this.total = 0;
    }
    public Order(long user_id, String date, String state, double total) {
        this.user_id = user_id;
        this.date = date;
        this.state = state;
        this.total = total;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
