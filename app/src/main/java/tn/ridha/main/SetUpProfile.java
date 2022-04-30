package tn.ridha.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import tn.ridha.Dao.UsersDao;
import tn.ridha.main.R;

import org.bson.Document;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import tn.ridha.main.MainActivity;
import tn.ridha.other.Session;

public class SetUpProfile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
    }

    public void onClick_Save(View view){
        Bundle extras = getIntent().getExtras();
        String email = "";
        if (extras != null){
            email = extras.getString("email");
        }
        User user = MainActivity.app.currentUser();
        assert user != null;
        Document data = new Document();
        EditText setup_Name = findViewById(R.id.setup_Name);
        EditText setup_City = findViewById(R.id.setup_City);
        EditText setup_Phone = findViewById(R.id.setup_Phone);
        EditText setup_BirthDate = findViewById(R.id.setup_BirthDate);
        String name = setup_Name.getText().toString();
        String city = setup_City.getText().toString();
        String phone = setup_Phone.getText().toString();
        String birthDate = setup_BirthDate.getText().toString();
        if (!SetUpProfile.check(name, city, phone, birthDate, getApplicationContext())) {
            MainActivity.costumeToast("missing values or wrong values", getApplicationContext());
            return;
        }else{
            data.append("name", name);
            data.append("email", email);
            data.append("phone", phone);
            data.append("city", city);
            data.append("birthdate", birthDate);
            UsersDao usersDao = new UsersDao();
            Context context = this;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(usersDao.insert(data)){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                MainActivity.costumeToast("Information saved successfully", getApplicationContext());
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                MainActivity.costumeToast("something went wrong", getApplicationContext());
                            }
                        });
                    }
                }
            });
            thread.start();
           // Intent intent = new Intent(this, MainActivity.class);
           // startActivity(intent);
            // mongoCollection.insertOne(data).getAsync(result -> {
         //     if (result.isSuccess()) {
         //         MainActivity.costumeToast("Information saved successfully", getApplicationContext());
         //         Intent intent = new Intent(this, MainActivity.class);
         //         startActivity(intent);
         //     } else {
         //         MainActivity.costumeToast("something went wrong", getApplicationContext());
         //     }
         //   });
        }
    }
    public static boolean check(String name, String city, String phone, String birthdate, Context context){
        if (name.length() < 3) {
            MainActivity.costumeToast("Name should be atleast 3 chars long", context);
            return false;
        }
        if (city.length() < 2) {
            MainActivity.costumeToast("City should be at least 2 chars long", context);
            return false;
        }
        if (SetUpProfile.isNumber(phone) && phone.length() < 5) {
            MainActivity.costumeToast("Phone should be at least 5 numbers long", context);
            return false;
        }
        try {
            if (birthdate.indexOf("/") > 0) {
                String[] birth = birthdate.split("/");
                String day = birth[0];
                String month = birth[1];
                String year = birth[2];
                if (day.length() != 2 || month.length() != 2 || year.length() != 4) {
                    MainActivity.costumeToast("Birthdate should be in the format of DD/MM/YYYY", context);
                    return false;
                }
            } else {
                return false;
            }
        }catch(Exception e){
            return false;
        }
        return true;
    }

    private static boolean isNumber(String phone) {
        try{
            Double d = Double.parseDouble(phone);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    public void onClick_Cancel(View view){
        MainActivity.Connected = false;
        MainActivity.userData = null;
        Session session = new Session(this);
        session.Logout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
