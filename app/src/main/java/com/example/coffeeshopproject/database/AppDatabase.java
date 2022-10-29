package com.example.coffeeshopproject.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {Order.class,Branch.class,Items.class,Category.class,
                        Subcategory.class,Person.class,OrderItems.class,Discount.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract OrderDAO orderDAO();
    public abstract BranchDAO branchDAO();
    public abstract ItemsDAO itemsDAO();
    public abstract PersonDAO personDAO();
    public abstract CategoryDAO categoryDAO();
    public abstract SubcategoryDAO subcategoryDAO();
    public abstract OrderItemsDAO orderItemsDAO();
    public abstract DiscountDAO discountDAO();
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,AppDatabase.class, "app_databasee").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    // Sample data installation
    public static void itemdata(){
        if(INSTANCE.itemsDAO().getAll().size()==0) {
            List<Items> list = new ArrayList<Items>();
            list.add(new Items("Espresso", 2.00, 1, 1));
            list.add(new Items("Cappuccino", 3.00, 1, 1));
            list.add(new Items("Frappe", 3.50, 1, 2));
            list.add(new Items("Mocha", 3.00, 1, 1));
            list.add(new Items("Latte", 3.00, 1, 1));
            list.add(new Items("Ice Latte", 3.00, 1, 2));
            list.add(new Items("Ice Mocha", 3.00, 1, 2));
            list.add(new Items("Bagel", 3.00, 2, 4));
            list.add(new Items("Croissant", 1.00, 2, 3));
            list.add(new Items("Eclair", 1.00, 2, 3));
            list.add(new Items("Chocolate Donut", 2.00, 2, 3));
            list.add(new Items("Cheesecake", 4.00, 3, 3));
            list.add(new Items("Chocolate Cake", 3.00, 3, 3));
            INSTANCE.itemsDAO().addAll(list);
        }
    }
    public static  void catdata(){
        if(INSTANCE.categoryDAO().getAll().size()==0) {
            List<Category> cat_list = new ArrayList<Category>();
            cat_list.add(new Category("Coffee"));
            cat_list.add(new Category("Pastry"));
            cat_list.add(new Category("Dessert"));
            INSTANCE.categoryDAO().addAll(cat_list);
        }
    }
    public static  void branchdata(){
        if(INSTANCE.branchDAO().getAll().size()==0) {
            Log.i("time", Clock.systemUTC().instant().toString());
            List<Branch> brlist = new ArrayList<Branch>();
            brlist.add(new Branch("Scranton","41.411835 , -75.665245"));
            brlist.add(new Branch("New York","40.730610 , -73.935242"));
            INSTANCE.branchDAO().addAll(brlist);
        }
    }
    public  static void subcatdata(){
        List<Subcategory> subcat_list = new ArrayList<Subcategory>();
        subcat_list.add(new Subcategory("Hot"));
        subcat_list.add(new Subcategory("Cold"));
        subcat_list.add(new Subcategory("Sweet"));
        subcat_list.add(new Subcategory("Salty"));
        for(Subcategory subcat : subcat_list){
            INSTANCE.subcategoryDAO().add(subcat);
        }
    }
}