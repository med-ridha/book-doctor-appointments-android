package tn.ridha.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import tn.ridha.Dao.UsersDao;
import tn.ridha.main.R;
import tn.ridha.other.Session;
import tn.ridha.Beans.UserData;
import com.google.android.material.navigation.NavigationView;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import io.realm.Realm;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Button updateButton;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        setContentView(R.layout.activity_profile);
        updateButton = findViewById(R.id.updateProfile);
        UserData userData;
        userData = MainActivity.userData;
        if (userData == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        Context context = this;
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText fullNameView =  findViewById(R.id.FullName_profile);
                EditText phoneNumberView =  findViewById(R.id.PhoneNumber_profile);
                EditText birthdayView =  findViewById(R.id.Birthdate_profile);
                EditText cityView =  findViewById(R.id.city_profile);
                String name = fullNameView.getText().toString();
                String phone = phoneNumberView.getText().toString();
                String birthDate = birthdayView.getText().toString();
                String city = cityView.getText().toString();
                if (!SetUpProfile.check(name, city, phone, birthDate, context)){
                    MainActivity.costumeToast("ivalid values!!", context);
                    return;
                }
                UsersDao userDao = new UsersDao();
                Document document = new Document();
                document.append("name", name);
                document.append("phone", phone);
                document.append("email", userData.getEmail());
                document.append("city", city);
                document.append("birthdate", birthDate);
                Document filter = new Document();
                filter.append("_id", new ObjectId(userData.get_id()));
                userDao.update(filter, document, context);
                finish();
            }
        });
        Toolbar toolbar;
        NavigationView navigationView;
        toolbar = findViewById(R.id.main_toolbar1);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout2);
        navigationView = findViewById(R.id.nav_view1);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        EditText fullNameView =  findViewById(R.id.FullName_profile);
        TextView emailView =  findViewById(R.id.Email_profile);
        EditText phoneNumberView =  findViewById(R.id.PhoneNumber_profile);
        EditText birthdayView =  findViewById(R.id.Birthdate_profile);
        EditText cityView =  findViewById(R.id.city_profile);
        fullNameView.setText(userData.getName());
        emailView.setText(userData.getEmail());
        phoneNumberView.setText(userData.getPhone());
        birthdayView.setText(userData.getBirthdate());
        cityView.setText(userData.getCity());
        ImageView imageView = findViewById(R.id.imageView);
        String url = "https://avatars.dicebear.com/api/initials/"+userData.getName()+".png?r=50&size=1280";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = LoadImageFromWebOperations(url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageDrawable(drawable);
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    public static Drawable LoadImageFromWebOperations(String url){
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "srcName");
            return d;
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public void MenuItem_goProfile(MenuItem menuItem){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
        finish();
    }
    public void MenuItem_Logout(MenuItem menuItem){
        MainActivity.Connected = false;
        MainActivity.userData = null;
        Session session = new Session(this);
        session.Logout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
        finish();
    }
    public void MenuItem_goHome(MenuItem menuItem){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    public void MenuItem_goHospitals(MenuItem menuItem){
        Intent intent = new Intent(this, DoctorsActivity.class);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}