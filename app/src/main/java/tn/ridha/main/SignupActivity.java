package tn.ridha.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import tn.ridha.main.R;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import tn.ridha.main.LoginActivity;
import tn.ridha.main.MainActivity;
import tn.ridha.other.Session;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }


    public boolean register(View view){
        EditText signupEmail = findViewById(R.id.signup_Email);
        EditText signupPassword = findViewById(R.id.signup_Password);
        String email = signupEmail.getText().toString();
        String password = signupPassword.getText().toString();
        App app = new App(new AppConfiguration.Builder(MainActivity.appId).build());
        app.getEmailPassword().registerUserAsync(email, password, it ->{
            if (it.isSuccess()){
                costumeToast("Registered with email successfully");
                Intent intent = new Intent(this, SetUpProfile.class);
                Session session = new Session( this);
                session.setPassword(password);
                session.setEmail(email);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);
                finish();
            }else{
                costumeToast(it.getError().toString().split(": ")[1]);
            }
        });
        return true;
    }
    public void goLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void costumeToast(String value){
        Log.v("User", value);
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, value, duration);
        toast.show();
    }
}