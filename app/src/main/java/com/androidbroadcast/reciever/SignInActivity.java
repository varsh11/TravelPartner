package com.androidbroadcast.reciever;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SignInActivity extends Activity {
    DataController mydb;
    EditText email;
    EditText pwd;
    Button signIn;
    Button signUp;
    TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        signUp = (Button) findViewById(R.id.signUp);


        signUp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SignInActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SignInActivity.this.finish();
    }

    public void login(View view) {
        Log.d("Clicked", "login: ");
        EditText email = (EditText) findViewById(R.id.loginEmail);
        EditText pwd = (EditText) findViewById(R.id.loginPwd);
        String emailEntry=email.getText().toString();
        String pwdEntry = pwd.getText().toString();
        TextView msg=(TextView) findViewById(R.id.msg);
        if(email.getText().length()>0&&pwd.getText().length()>0) {
            DataController dataController = new DataController(getBaseContext());
            dataController.open();
            Cursor rs = dataController.retrieve(emailEntry);
            rs.moveToFirst();
            Log.d("rs", "login: "+rs.moveToFirst());
            if (rs.moveToFirst()) {
                String fetchedEmail = rs.getString(rs.getColumnIndex(DataController.CONTACTS_COLUMN_EMAIL));
                String fetchedPwd = rs.getString(rs.getColumnIndex(DataController.CONTACTS_COLUMN_PASSWORD));
                dataController.close();
                if (fetchedPwd.equals(pwdEntry)) {
                    email.setText("");
                    pwd.setText("");
                    Intent intent = new Intent(SignInActivity.this, GooglePlacesExample.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Email/password does not match", Toast.LENGTH_LONG).show();
                }
            }
        else {
                Toast.makeText(getApplicationContext(), "Please Sign Up", Toast.LENGTH_LONG).show();
            }}else {
            Toast.makeText(getApplicationContext(), "Email and password is mandatory!!", Toast.LENGTH_LONG).show();

        }
    }
}
