package com.example.coffeeshopproject.MyActivites;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.coffeeshopproject.database.Person;

public class Register extends AppCompatActivity {

    EditText name,surname,email,password,phone;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.reg_name);
        surname = findViewById(R.id.reg_surname);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_password);
        phone = findViewById(R.id.reg_phone);
        submit = findViewById(R.id.register_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().length()>0 && password.getText().toString().length()>0){
                    long userid = AppDatabase.getInstance(getApplicationContext()).personDAO().add(new Person( name.getText().toString(),surname.getText().toString()
                            ,email.getText().toString(),password.getText().toString()
                            ,phone.getText().toString()));
                    AppDatabase.getInstance(getApplicationContext()).personDAO().updateLoggedIn(1,userid);
                    Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                    newIntent.putExtra(Login.USER_ID,userid);
                    Log.i("USER ID",userid+"");
                    startActivity(newIntent);
                } else {
                    Toast.makeText(Register.this, "Enter valid parameters", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}