package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    Boolean isLogIn=true;
    EditText usernameText;
    EditText passwordText;
    TextView alternateText;
    Button nextPageButton;

    public void showUsersList()
    {
        Intent intent= new Intent(getApplicationContext(),UsersListActivity.class);
        startActivity(intent);
    }
    public void nextPage(View view) {
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username/Password can't be empty!", Toast.LENGTH_SHORT).show();
        } else {
            if (isLogIn == true) {

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e==null) {
                            showUsersList();

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
            else
            {
                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null) {
                            showUsersList();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        }
    }
    public void change(View view)
    {
        if (isLogIn==true)
        {
            nextPageButton.setText("Sign Up");
            alternateText.setText("Or, Log In");
            isLogIn=false;
        }
        else
        {
            nextPageButton.setText("Log In");
            alternateText.setText("Or, Sign Up");
            isLogIn=true;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ParseUser.getCurrentUser()!=null)
        {
            showUsersList();
        }
        usernameText= findViewById(R.id.usernameText);
        passwordText= findViewById(R.id.passwordText);
        alternateText= findViewById(R.id.alternateText);
        nextPageButton= findViewById(R.id.nextPageButton);
        ImageView logo= findViewById(R.id.logo);
        ConstraintLayout backgroundLayout = findViewById(R.id.backgroundLayout);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        backgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });



        passwordText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN )
                {
                    nextPage(v);
                }
                return false;
            }
        });


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}