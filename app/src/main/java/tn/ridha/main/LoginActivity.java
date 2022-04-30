package tn.ridha.main;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import tn.ridha.main.R;
import tn.ridha.other.Session;
import tn.ridha.Beans.UserData;

import org.bson.Document;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class LoginActivity extends AppCompatActivity {
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void onClick_Login(View view){
        EditText emailText_Email = (EditText) findViewById(R.id.signup_Email);
        EditText emailText_Password = (EditText) findViewById(R.id.signup_Password);
        String email = emailText_Email.getText().toString();
        String password = emailText_Password.getText().toString();
        connect(email, password);
    }

    public void onClick_switchRegister(View view){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    public void connect(String email, String password){
        Credentials credentials = Credentials.emailPassword(email, password);
        MainActivity.app.loginAsync(credentials, (result) -> {
            if (result.isSuccess()){
                costumeToast("Logged In Successfully");
                MainActivity.Connected = true;
                User user = MainActivity.app.currentUser();
                mongoClient = user.getMongoClient("mongodb-atlas");
                mongoDatabase = mongoClient.getDatabase("Doctor_appointments");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("users");
                Document queryFilter = new Document().append("email", email);
                mongoCollection.findOne(queryFilter).getAsync(searchResult ->{
                    if(searchResult.isSuccess()){
                        Document resultData = searchResult.get();
                        if (resultData != null ){
                            String _id = resultData.getObjectId("_id").toString();
                            String resultName = resultData.getString("name");
                            String resultEmail = resultData.getString("email");
                            String resultCity = resultData.getString("city");
                            String resultPhone = resultData.getString("phone");
                            String resultBirthDate = resultData.getString("birthdate");
                            Session session = new Session( this);
                            session.setUsername(resultName);
                            session.setPassword(password);
                            session.setEmail(resultEmail);
                            MainActivity.userData = new UserData(_id, resultName, resultEmail, resultCity, resultPhone, resultBirthDate);
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Session session = new Session( this);
                            session.setPassword(password);
                            session.setEmail(email);
                            Intent intent = new Intent(this, SetUpProfile.class);
                            intent.putExtra("email", email);
                            intent.putExtra("password", password);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }else{
                costumeToast(result.getError().toString().split(": ")[1]);
            }
        });
    }
    public void costumeToast(String value){
        Log.v("User", value);
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, value, duration);
        toast.show();
    }
    @Override
    public void onBackPressed(){

    }
}