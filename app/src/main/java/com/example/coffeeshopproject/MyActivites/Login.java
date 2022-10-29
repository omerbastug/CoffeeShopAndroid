package com.example.coffeeshopproject.MyActivites;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coffeeshopproject.R;
import com.example.coffeeshopproject.database.AppDatabase;
import com.example.coffeeshopproject.database.Category;
import com.example.coffeeshopproject.database.Person;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    public static final String USER_ID = "com.example.coffeeshopproject.USER_ID";
    public static final String MyPREFERENCES = "MyPrefs" ;

    //    public static final String DB_CONTEXT = "com.example.coffeeshopproject.DB_CONTEXT";
    EditText email,password;
    Button login_button;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppDatabase.getInstance(this).itemdata();
        long userid = AppDatabase.getInstance(this).personDAO().getLoggedinId();
        if (userid!=0){
            Intent newIntent = new Intent(this, MainActivity.class);
            newIntent.putExtra(Login.USER_ID,userid);
            startActivity(newIntent);
        } else {
            email = findViewById(R.id.email);
            password = findViewById(R.id.password);


            login_button = findViewById(R.id.loginbutton);
            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String e = email.getText().toString();
                    String pw = password.getText().toString();
                    if(e.length()>0 && pw.length()>0){
                        login_init(e,pw);
                    } else {
                        Toast.makeText(Login.this, "Enter valid email and password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    public void login_init(String e, String pw){
        Person user = AppDatabase.getInstance(this).personDAO().getbyEmail(e);

        if( user != null ){
            String db_password = user.getPassword();
            if(db_password.equals(pw)){
                AppDatabase.getInstance(getApplicationContext()).personDAO().updateLoggedIn(1,user.getId());
                Intent newIntent = new Intent(this, MainActivity.class);
                newIntent.putExtra(Login.USER_ID,user.getId());
                Log.i("USER ID",user.getId()+"");
                startActivity(newIntent);
                Toast.makeText(this, "You logged in " + user.getId(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Wrong e-mail", Toast.LENGTH_SHORT).show();
        }
    }


    public void register_page(View view){
        Intent newIntent = new Intent(this, Register.class);
        startActivity(newIntent);

        Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){

    }
}
